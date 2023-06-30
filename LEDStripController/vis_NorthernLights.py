import numpy as np
import ExternalAudio
import colorsys
import Utils
import dsp


Utils.MAX_FREQUENCY=8000
dsp.create_mel_bank()

rLerp =  0.1
gLerp =  0.1
bLerp =  0.1
rold=0;
gold=0;
bold=0;

def visualize_spectrum(y):
    """Effect that maps the Mel filterbank frequencies onto the LED strip"""
    global rold,gold,bold
    y = np.copy(ExternalAudio.interpolate(y, Utils.N_PIXELS // 2))
    ExternalAudio.common_mode.update(y)
    diff = y - ExternalAudio._prev_spectrum
    maxIndex=np.argmax(y)
    maxValue = y[maxIndex]
    avg = np.average(y)
    #print (str(maxIndex/len(y))+":"+str((avg/maxValue))+":"+str(maxValue))
    color = colorsys.hsv_to_rgb(0.9-min(0.9,pow((maxIndex/len(y))/3,0.5)),1-(avg/maxValue),maxValue)
    ExternalAudio._prev_spectrum = np.copy(y)
    # Color channel mappings
    r = np.array([color[0]])
    if(r<rold):
        r=r*(rLerp)+(rold*(1-rLerp))
    g = np.array([color[1]])
    if(g<gold):
        g=g*(gLerp)+(gold*(1-gLerp))
    b = np.array([color[2]])
    if(b<bold):
        b=b*(bLerp)+(bold*(1-bLerp))
    # Mirror the color channels for symmetric output
    #r = np.concatenate((r[::-1], r))
    #g = np.concatenate((g[::-1], g))
    #b = np.concatenate((b[::-1], b))
    rold=r
    gold=g
    bold=b
    output = np.array([r,g,b]) * 255
    return output
