from bleak import BleakScanner, BleakClient
import asyncio
import ExternalAudio
import BLEClass
import Utils

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
            print(("{}) Device: {} Address: {}".format(x, device.name, device.address)))
            x+=1
    devices=newDevices

async def handle_ls():
    x = 0
    for i, device in enumerate(devices):
        print(("{}) Device: {} Address: {}".format(x, device.name, device.address)))
        x+=1
            
async def handle_macfilter(mac):
    x = 0
    for i, device in enumerate(devices.copy()):
        if device.address!=mac:
            devices.remove(device)
    await handle_ls()

def current_client():
    return Utils.client

async def handle_writeColor():

    Utils.isModeUsed = False
    try:
        Utils.Colors["Red"] = int(input("Red (1-255):"))
        Utils.Colors["Green"] = int(input("Green (1-255):"))
        Utils.Colors["Blue"] = int(input("Blue (1-255):"))
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

def handle_message_changed(message):
    pass

async def build_client(device):
    if Utils.client is not None:
        await Utils.client.stop()
    Utils.client = BLEClass.QBleakClient(device)
    Utils.client.messageChanged.connect(handle_message_changed)
    await Utils.client.start()
    print("Connected")

async def handle_connect(select):
    global devices
    device = devices[int(select)]
    await build_client(device)

async def main():
    while True:
        cmd = input("()> ")
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
            print("color")
            print("  send a color")
            print("")
            print("on")
            print("  turn on lights")
            print("")
            print("off")
            print("  turn off lights")
            print("")
            print("quit")
            print("  quit program gracefully")
            
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

        if (cmd == "off"):
            future = asyncio.ensure_future(handle_power(False))
            await asyncio.wait({future}, return_when=asyncio.ALL_COMPLETED)
            
        if (cmd == "color"):
            future = asyncio.ensure_future(handle_writeColor())
            await asyncio.wait({future}, return_when=asyncio.ALL_COMPLETED)

        if (cmd == "quit"):
            quit()

asyncio.run(main())