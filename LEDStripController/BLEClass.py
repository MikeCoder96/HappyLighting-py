import Utils
import asyncio
from dataclasses import dataclass
from functools import cached_property
from bleak import BleakScanner, BleakClient
from bleak.backends.device import BLEDevice
from PyQt5.QtCore import QObject, pyqtSignal
import VTCodes
UART_SERVICE_UUID = ""
UART_RX_CHAR_UUID = ""
UART_TX_CHAR_UUID = ""
UART_SAFE_SIZE = 20

@dataclass
class QBleakClient(QObject):
    device : BLEDevice

    messageChanged = pyqtSignal(bytes)

    def __post_init__(self):
        global UART_SERVICE_UUID, UART_RX_CHAR_UUID, UART_TX_CHAR_UUID, UART_SAFE_SIZE
        super().__init__()

    @cached_property
    def client(self) -> BleakClient:
        return BleakClient(self.device, disconnected_callback=self._handle_disconnect)

    async def start(self):
        global UART_TX_CHAR_UUID, UART_RX_CHAR_UUID
        try:
            await self.client.connect()
            for service in self.client.services:
                if service.description == "Generic Access Profile":
                    for char in service.characteristics:
                        Utils.printLog("Set UART_RX_CHAR_UUID with {}".format(char.uuid))
                        UART_RX_CHAR_UUID = char.uuid

                elif service.description == "Vendor specific":
                    for char in service.characteristics:
                        if (','.join(char.properties) == "write-without-response,write") and UART_TX_CHAR_UUID == "":
                            Utils.printLog("Set UART_TX_CHAR_UUID with {}".format(char.uuid))
                            UART_TX_CHAR_UUID = char.uuid
                            
        except asyncio.CancelledError as ex:
            print(ex)

    async def stop(self):
        try:
            await self.client.disconnect()
        except asyncio.CancelledError as ex:
            pass

    async def writeColor(self):
            lista = [86, Utils.Colors["Red"], Utils.Colors["Green"], Utils.Colors["Blue"], (int(10 * 255 / 100) & 0xFF), 256-16, 256-86]
            values = bytearray(lista)
            try:
                if Utils.PRETTY:
                    Utils.printLog(
                        VTCodes.fg_fromRGB(Utils.Colors["Red"], Utils.Colors["Green"], Utils.Colors["Blue"],
                        "Change Color called R:{} G:{} B:{} ".format(Utils.Colors["Red"], Utils.Colors["Green"], Utils.Colors["Blue"]))+VTCodes.RESET
                        )
                else:
                    Utils.printLog("Change Color called R:{} G:{} B:{} ".format(Utils.Colors["Red"], Utils.Colors["Green"], Utils.Colors["Blue"]))
                await self.client.write_gatt_char(UART_TX_CHAR_UUID, values, False)
            except Exception as inst:
                print(inst)

    async def writePower(self, state):
            lista = [204, 35, 51]
            if state == "Off":
                lista = [204, 36, 51]

            values = bytearray(lista)
            try:
                Utils.printLog("Change Power called Power : {}".format(state))
                await self.client.write_gatt_char(UART_TX_CHAR_UUID, values, False)
            except Exception as inst:
                print(inst)

    async def writeMode(self, idx):

            #new byte[] { 256 - 69, mode, (byte)(speed & 0xFF), 68 };
            i_mode = Utils.Modes[idx]
            lista = [256 - 69, i_mode, (Utils.Speed & 0xFF), 68]
            values = bytearray(lista)
            try:
                Utils.printLog("Change Mode with ID {} Speed {}".format(i_mode, Utils.Speed))
                await self.client.write_gatt_char(UART_TX_CHAR_UUID, values, False)
            except Exception as inst:
                print(inst)

    async def writeMicState(self, enable):
            
            var_1 = -1
            var_2 = -1
            if enable:
                var_1 = 256 - 16
                var_2 = 50
            else:
                var_1 = 15
                var_2 = 30
            lista = [1, var_1, var_2,0 ,0, 24]
            values = bytearray(lista)
            try:
                #Utils.printLog("Change Mode with ID {} ".format(i_mode))
                await self.client.write_gatt_char(UART_TX_CHAR_UUID, values, False)
            except Exception as inst:
                print(inst)

    #TODO: Implement disconnect function
    def _handle_disconnect(self, device) -> None:
        Utils.printLog("Device was disconnected")
        # cancelling all tasks effectively ends the program
        for task in asyncio.all_tasks():
            task.cancel()