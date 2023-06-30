from asyncio.windows_events import NULL
from bleak import BleakScanner, BleakClient
import asyncio
import ExternalAudio
import BLEClass
import Utils
import time
import sys
import os 
from struct import *

devices = []

reset="\x1b[1;0m"
red="\x1b[1;31m"
yellow="\x1b[1;33m"
green="\x1b[1;32m"
gray="\x1b[1;90m"

async def handle_scan(all):
    global devices
    devices = await BLEClass.BleakScanner.discover()
    x = 0
    newDevices = []
    for i, device in enumerate(devices):
        if str(device.name).startswith("QHM") or all:
            newDevices.append(device)
            x+=1
    devices=newDevices
    await handle_ls()

async def handle_ls():
    x = 0
    for i, device in enumerate(devices):
        Utils.printLog(("{}) Device: {} Address: {}".format(x, device.name, device.address)))
        x+=1
            
async def handle_macfilter(mac):
    x = 0
    for i, device in enumerate(devices.copy()):
        if device.address!=mac:
            devices.remove(device)
    await handle_ls()

def current_client():
    return Utils.client

async def handle_writeColor(cmd):    
    cmd=cmd.removeprefix("color")
    if not cmd.startswith(" "):
        Utils.Colors["Red"] = int(input("Red (1-255):"))
        Utils.Colors["Green"] = int(input("Green (1-255):"))
        Utils.Colors["Blue"] = int(input("Blue (1-255):"))
    else:
        cmd=cmd.replace(" ","")
        colors=cmd.split(",")
        Utils.Colors["Red"] = int(colors[0])
        Utils.Colors["Green"] = int(colors[1])
        Utils.Colors["Blue"] = int(colors[2])


    Utils.isModeUsed = False
    try:
        await current_client().writeColor()
    except Exception as ex:
        Utils.printLog("Colors error {}".format(ex))

async def handle_power(turnOn):
    try:
        if turnOn:
            await current_client().writePower("On")
        else:
            await current_client().writePower("Off")

    except Exception as ex:
        Utils.printLog("Power error {}".format(ex))

async def handle_audio(device):
    if (device=="" or device==NULL):        
        info = Utils.p.get_host_api_info_by_index(0)
        numdevices = info.get('deviceCount')
        for i in range(0, numdevices):
            if (Utils.p.get_device_info_by_host_api_device_index(0, i).get('maxInputChannels')) > 0:
                tmp_device = Utils.p.get_device_info_by_host_api_device_index(0, i)
                Utils.InputDevices[i] = tmp_device
                print(str(i)+": "+tmp_device["name"])
        device=input("Select device index:")
    Utils.selectedInputDevice=int(device.split(" ")[0])
    Utils.localAudio=True
    if(len(device.split(" "))>1!=NULL):
        await handle_visset("vis_"+device.split(" ")[1])
    await ExternalAudio.start_stream()
    print("Audio Over")

async def handle_visset(cmd):    

    exec("import "+cmd+" as visualizer;ExternalAudio.visualize_spectrum=visualizer.visualize_spectrum")

def handle_message_changed(message):
    pass

async def build_client(device):
    if Utils.client is not None:
        await Utils.client.stop()
    Utils.client = BLEClass.QBleakClient(device)
    Utils.client.messageChanged.connect(handle_message_changed)
    await Utils.client.start()
    Utils.printLog("Connected")

async def handle_connect(select):
    global devices
    device = devices[int(select)]
    await build_client(device)

async def main():
    args = sys.argv[1:]
    Utils.DEBUG_LOGS = True
    loop = True
    x=0
    if sys.platform == "win32":
        os.system('color')

    for arg in args:
        if arg=="/c":
            cmd=args[x+1]
            loop=False
            cmds = cmd.split(";")
            for subcmd in cmds:
                await handle_command(subcmd)
        if arg=="/pretty":
            Utils.PRETTY=True
            print("not implemented yet - will add color support to terminal output")
        if arg=="/quiet":
            Utils.DEBUG_LOGS = False
        if arg=="/verbose":
            Utils.DEBUG_LOGS = True
        x+=1
    while loop:
        cmd = input("()> ")
        cmds = cmd.split(";")
        for subcmd in cmds:
            await handle_command(subcmd)
            #if len(cmds) > 1:
                #time.sleep(0.1)
 
async def handle_command(cmd):
    if (cmd == "help"):
        print("")
        print("scan")
        print("  scans for devices with name starting with QHM")
        print("")
        print("scan *")
        print("OR")
        print("scanall")
        print("  scans for all devices")
        print("")
        print("filter")
        print("OR")
        print("filter 00:00:00:00:00:00")
        print("  filter devices by mac address")
        print("")
        print("ls")
        print("OR")
        print("list")
        print("  list known devices")
        print("")
        print("connect")
        print("  connect to a device")
        print("")
        print("color [red],[green],[blue]")
        print("  send a color")
        print("  for example:")
        print("    color 255,0,0")
        print("")
        print("on")
        print("  turn on lights")
        print("")
        print("off")
        print("  turn off lights")
        print("")
        print("audio [input device] [visualizer script]")
        print("  start audio visualizer. Without parameters, uses default visualizer and asks for input device.")
        print("  for example:")
        print("    audio 0 NorthernLights")
        print("")
        print("wait [milliseconds]")
        print("   Waits for specified milliseconds - if none specified, waits a second")
        print("")
        print("quit")
        print("  quit program gracefully")
        print("")
        print("To chain multiple commands, separate them with semicolons, no spaces")
        print("for  example:")
        print("  scanall;filter 00:00:00:00:00:00;connect;on;color 255,0,0;wait;color 255,255,255;wait;color 255,0,0;wait;off;quit")
        print("")
        print("Command line arguments")
        print ("python.exe cmdver.py [/quiet|/verbose] [/pretty] [/c \"command string\"]")
        print("  /quiet: Turns off script debug output")
        print("  /verbose: Turns on script debug output")
        print("  /pretty: To be implemented - will enable VT100 color code use")
        print("  /c \"command string\": Stick your command string  in the quotes, it'll run all the steps in the command string  then go back to the command loop (add quit to the command string to quit after command string)")
            
    if (cmd == "scan"):
        future = asyncio.ensure_future(handle_scan(False))
        await asyncio.wait({future}, return_when=asyncio.ALL_COMPLETED)
            
    if (cmd == "scan *" or cmd == "scanall"):
        future = asyncio.ensure_future(handle_scan(True))
        await asyncio.wait({future}, return_when=asyncio.ALL_COMPLETED)

    if (cmd == "ls" or cmd == "list"):
        future = asyncio.ensure_future(handle_ls())
        await asyncio.wait({future}, return_when=asyncio.ALL_COMPLETED)
            
    if (cmd.startswith("filter")):
        cmd=cmd.removeprefix("filter")
        address=""
        if not cmd.startswith(" "):
            address=input("Enter MAC address:")
        else:
            address=cmd.replace(" ","")
        future = asyncio.ensure_future(handle_macfilter(address))
        await asyncio.wait({future}, return_when=asyncio.ALL_COMPLETED)
            
    if (cmd.startswith("audio")):
        if(cmd.startswith("audio ")):
            cmd = cmd.replace("audio ","")
            future = asyncio.ensure_future(handle_audio(cmd))            
        else:
            future = asyncio.ensure_future(handle_audio(NULL))
        await asyncio.wait({future}, return_when=asyncio.ALL_COMPLETED)

    if (cmd == "connect"):
        if len(devices) > 0 :
            selection=0
            if len(devices)> 1 :    
                selection=int(input("Select Device :"))
            future = asyncio.ensure_future(handle_connect(selection))
            await asyncio.wait({future}, return_when=asyncio.ALL_COMPLETED)

    if (cmd == "on"):
        future = asyncio.ensure_future(handle_power(True))
        await asyncio.wait({future}, return_when=asyncio.ALL_COMPLETED)

    if (cmd.startswith("wait")):
        cmdsplit = cmd.split(" ")
        if len(cmdsplit)<2 :
            time.sleep(1)
        else:
            time.sleep(int(cmdsplit[1])/1000)

    if (cmd == "off"):
        future = asyncio.ensure_future(handle_power(False))
        await asyncio.wait({future}, return_when=asyncio.ALL_COMPLETED)
            
    if (cmd.startswith("color")):
        future = asyncio.ensure_future(handle_writeColor(cmd))
        await asyncio.wait({future}, return_when=asyncio.ALL_COMPLETED)

    if (cmd == "quit"):
        quit()
asyncio.run(main())