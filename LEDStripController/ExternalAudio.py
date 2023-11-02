from __future__ import print_function
from __future__ import division
import time
import qasync
import pyaudio
import numpy as np
from scipy.ndimage.filters import gaussian_filter1d
import Utils
import dsp
#import led

# Number of audio samples to read every time frame
samples_per_frame = int(Utils.MIC_RATE / Utils.FPS)

# Array containing the rolling audio sample window
y_roll = np.random.rand(Utils.N_ROLLING_HISTORY, samples_per_frame) / 1e16

fft_plot_filter = dsp.ExpFilter(np.tile(1e-1, Utils.N_FFT_BINS),
                         alpha_decay=0.5, alpha_rise=0.99)
mel_gain = dsp.ExpFilter(np.tile(1e-1, Utils.N_FFT_BINS),
                         alpha_decay=0.01, alpha_rise=0.99)
mel_smoothing = dsp.ExpFilter(np.tile(1e-1, Utils.N_FFT_BINS),
                         alpha_decay=0.5, alpha_rise=0.99)
volume = dsp.ExpFilter(Utils.MIN_VOLUME_THRESHOLD,
                       alpha_decay=0.02, alpha_rise=0.02)
fft_window = np.hamming(int(Utils.MIC_RATE / Utils.FPS) * Utils.N_ROLLING_HISTORY)
prev_fps_update = time.time()

r_filt = dsp.ExpFilter(np.tile(0.01, Utils.N_PIXELS // 2),
                       alpha_decay=0.2, alpha_rise=0.99)
g_filt = dsp.ExpFilter(np.tile(0.01, Utils.N_PIXELS // 2),
                       alpha_decay=0.05, alpha_rise=0.3)
b_filt = dsp.ExpFilter(np.tile(0.01, Utils.N_PIXELS // 2),
                       alpha_decay=0.1, alpha_rise=0.5)
common_mode = dsp.ExpFilter(np.tile(0.01, Utils.N_PIXELS // 2),
                       alpha_decay=0.99, alpha_rise=0.01)
p_filt = dsp.ExpFilter(np.tile(1, (3, Utils.N_PIXELS // 2)),
                       alpha_decay=0.1, alpha_rise=0.99)
p = np.tile(1.0, (3, Utils.N_PIXELS // 2))
gain = dsp.ExpFilter(np.tile(0.01, Utils.N_FFT_BINS),
                     alpha_decay=0.001, alpha_rise=0.99)

_prev_spectrum = np.tile(0.01, Utils.N_PIXELS // 2)

pixels = np.tile(1, (3, Utils.N_PIXELS))

_gamma = np.load(Utils.GAMMA_TABLE_PATH)

def memoize(function):
    """Provides a decorator for memoizing functions"""
    from functools import wraps
    memo = {}

    @wraps(function)
    def wrapper(*args):
        if args in memo:
            return memo[args]
        else:
            rv = function(*args)
            memo[args] = rv
            return rv
    return wrapper

@memoize
def _normalized_linspace(size):
    return np.linspace(0, 1, size)


def interpolate(y, new_length):
    """Intelligently resizes the array by linearly interpolating the values

    Parameters
    ----------
    y : np.array
        Array that should be resized

    new_length : int
        The length of the new interpolated array

    Returns
    -------
    z : np.array
        New array with length of new_length that contains the interpolated
        values of y.
    """
    if len(y) == new_length:
        return y
    x_old = _normalized_linspace(len(y))
    x_new = _normalized_linspace(new_length)
    z = np.interp(x_new, x_old, y)
    return z

def visualize_spectrum(y):
    """Effect that maps the Mel filterbank frequencies onto the LED strip"""
    global _prev_spectrum
    y = np.copy(interpolate(y, Utils.N_PIXELS // 2))
    common_mode.update(y)
    diff = y - _prev_spectrum
    _prev_spectrum = np.copy(y)
    # Color channel mappings
    r = r_filt.update(y - common_mode.value)
    g = np.abs(diff)
    b = b_filt.update(np.copy(y))
    # Mirror the color channels for symmetric output
    #r = np.concatenate((r[::-1], r))
    #g = np.concatenate((g[::-1], g))
    #b = np.concatenate((b[::-1], b))
    output = np.array([r, g,b]) * 255
    return output


async def updateLedColor(red, green, blue):

    if Utils.RedMic:
        if red >= 256:
            red = 255
    else:
        red = 0
    
    if Utils.GreenMic:
        if green >= 256:
            green = 255 
    else:
        green = 0

    if Utils.BlueMic:
        if blue >= 256:
            blue = 255
    else:
        blue = 0

    await Utils.client.writeColor(red, green, blue)

async def updateLed():
    """Writes new LED values to the Blinkstick.
        This function updates the LED strip with new values.
    """
    global pixels
    
    # Truncate values and cast to integer
    pixels = np.clip(pixels, 0, 255).astype(int)
    # Optional gamma correction
    p = _gamma[pixels]
    np.copy(pixels)
    # Read the rgb values
    r = p[0][:].astype(int)
    g = p[1][:].astype(int)
    b = p[2][:].astype(int)


    medianRed = max(r)
    medianGreen = max(g)
    medianBlue= max(b)

    await updateLedColor(int(medianRed), int(medianGreen), int(medianBlue))
 
async def start_stream():
    frames_per_buffer = int(Utils.MIC_RATE / Utils.FPS)
    stream = Utils.p.open(format=pyaudio.paInt16,
                    channels=1,
                    input_device_index=Utils.selectedInputDevice,
                    rate=Utils.MIC_RATE,
                    input=True,
                    frames_per_buffer=frames_per_buffer)
    overflows = 0
    prev_ovf_time = time.time()
    while True:
        try:
            if Utils.localAudio:
                y = np.fromstring(stream.read(frames_per_buffer, exception_on_overflow=False), dtype=np.int16)
                y = y.astype(np.float32)
                stream.read(stream.get_read_available(), exception_on_overflow=False)
                await microphone_update(y)
            else:
                break
        except IOError:
            overflows += 1
            if time.time() > prev_ovf_time + 1:
                prev_ovf_time = time.time()
                print('Audio buffer has overflowed {} times'.format(overflows))
    
    stream.stop_stream()
    stream.close()
    Utils.p.terminate()
    Utils.p = pyaudio.PyAudio()

async def microphone_update(y):
    global y_roll, prev_rms, prev_exp, prev_fps_update, pixels
    # Normalize samples between 0 and 1
    y = y / 2.0**15
    # Construct a rolling window of audio samples
    y_roll[:-1] = y_roll[1:]
    y_roll[-1, :] = np.copy(y)
    y_data = np.concatenate(y_roll, axis=0).astype(np.float32)
    
    vol = np.max(np.abs(y_data))
    if False:
        #print('No audio input. Volume below threshold. Volume:', vol)
        #led.pixels = np.tile(0, (3, Utils.N_PIXELS))
        #led.update()
        pass
    else:
        # Transform audio input into the frequency domain
        N = len(y_data)
        N_zeros = 2**int(np.ceil(np.log2(N))) - N
        # Pad with zeros until the next power of two
        y_data *= fft_window
        y_padded = np.pad(y_data, (0, N_zeros), mode='constant')
        YS = np.abs(np.fft.rfft(y_padded)[:N // 2])
        # Construct a Mel filterbank from the FFT data
        mel = np.atleast_2d(YS).T * dsp.mel_y.T
        # Scale data to values more suitable for visualization
        # mel = np.sum(mel, axis=0)
        mel = np.sum(mel, axis=0)
        mel = mel**2.0
        # Gain normalization
        mel_gain.update(np.max(gaussian_filter1d(mel, sigma=1.0)))
        mel /= mel_gain.value
        mel = mel_smoothing.update(mel)
        # Map filterbank output onto LED strip
        pixels = visualize_spectrum(mel)
        await updateLed()
