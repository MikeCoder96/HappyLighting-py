from bleak import BleakScanner, BleakClient
import asyncio
#import ExternalAudio
import BLEClass
import Utils

devices = []

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
        if (cmd == "scan"):
            future = asyncio.ensure_future(handle_scan(False))
            await asyncio.wait({future}, return_when=asyncio.ALL_COMPLETED)
            
        if (cmd == "scan *"):
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

        if (cmd == "color"):
            future = asyncio.ensure_future(handle_writeColor())
            await asyncio.wait({future}, return_when=asyncio.ALL_COMPLETED)

asyncio.run(main())