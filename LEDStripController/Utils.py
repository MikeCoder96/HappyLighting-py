import os
import pyaudio

DEBUG_LOGS = True
Modes = [37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 
         47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 
         97, 98, 99]

Colors = {"Red":0, "Green":0, "Blue":0}

InputDevices = {}
p = pyaudio.PyAudio()
selectedInputDevice = -1
app = None
captureMode = False

Speed = 0
isModeUsed = False
client = None

localAudio = False
GreenMic = True
RedMic = True
BlueMic = True

N_PIXELS = 60
"""Number of pixels in the LED strip (must match ESP8266 firmware)"""

GAMMA_TABLE_PATH = os.path.join(os.path.dirname(__file__), 'gamma_table.npy')
"""Location of the gamma correction table"""

MIC_RATE = 44100
"""Sampling frequency of the microphone in Hz"""

FPS = 60
"""Desired refresh rate of the visualization (frames per second)

FPS indicates the desired refresh rate, or frames-per-second, of the audio
visualization. The actual refresh rate may be lower if the computer cannot keep
up with desired FPS value.

Higher framerates improve "responsiveness" and reduce the latency of the
visualization but are more computationally expensive.

Low framerates are less computationally expensive, but the visualization may
appear "sluggish" or out of sync with the audio being played if it is too low.

The FPS should not exceed the maximum refresh rate of the LED strip, which
depends on how long the LED strip is.
"""
_max_led_FPS = int(((N_PIXELS * 30e-6) + 50e-6)**-1.0)
assert FPS <= _max_led_FPS, 'FPS must be <= {}'.format(_max_led_FPS)

MIN_FREQUENCY = 60
"""Frequencies below this value will be removed during audio processing"""

MAX_FREQUENCY = 120
"""Frequencies above this value will be removed during audio processing"""

N_FFT_BINS = 24
"""Number of frequency bins to use when transforming audio to frequency domain

Fast Fourier transforms are used to transform time-domain audio data to the
frequency domain. The frequencies present in the audio signal are assigned
to their respective frequency bins. This value indicates the number of
frequency bins to use.

A small number of bins reduces the frequency resolution of the visualization
but improves amplitude resolution. The opposite is true when using a large
number of bins. More bins is not always better!

There is no point using more bins than there are pixels on the LED strip.
"""

N_ROLLING_HISTORY = 2
"""Number of past audio frames to include in the rolling window"""

MIN_VOLUME_THRESHOLD = 1e-7
"""No music visualization displayed if recorded audio volume below threshold"""

PRETTY=False
"""Controls outputting color codes"""

def printLog(text):
    if DEBUG_LOGS:
        print("[+] {}".format(text))
