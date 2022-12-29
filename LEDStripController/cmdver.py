from bleak import BleakScanner, BleakClient
import asyncio
import ExternalAudio
import BLEClass
import Utils

async def handle_scan():
    devices = await BLEClass.BleakScanner.discover()
    devices.extend(devices)
    for i, device in enumerate(devices):
        if str(device.name).startswith("QHM"):
            print(("Device: {} Address: {}".format(device.name, device.address)))

async def main():
    while True:
        cmd = input("()> ")
        if (cmd == "scan"):
            future = asyncio.ensure_future(handle_scan())
            await asyncio.wait({future}, return_when=asyncio.ALL_COMPLETED)

asyncio.run(main())