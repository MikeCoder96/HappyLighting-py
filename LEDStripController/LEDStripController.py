import asyncio
from dataclasses import dataclass
from functools import cached_property
import sys

from PyQt5.QtCore import QObject, pyqtSignal
from PyQt5.QtWidgets import (
    QApplication,
    QComboBox,
    QLineEdit,
    QMainWindow,
    QPlainTextEdit,
    QPushButton,
    QVBoxLayout,
    QWidget,
    QColorDialog,
    QLabel,
    QGraphicsColorizeEffect,
)

import qasync

from bleak import BleakScanner, BleakClient
from bleak.backends.device import BLEDevice

UART_SERVICE_UUID = ""
UART_RX_CHAR_UUID = ""
UART_TX_CHAR_UUID = ""
UART_SAFE_SIZE = 20

Colors = {"Red":0, "Green":0, "Blue":0}


@dataclass
class QBleakClient(QObject):
    device : BLEDevice

    messageChanged = pyqtSignal(bytes)

    def __post_init__(self):
        super().__init__()

    @cached_property
    def client(self) -> BleakClient:
        return BleakClient(self.device, disconnected_callback=self._handle_disconnect)

    async def start(self):
        global UART_TX_CHAR_UUID, UART_RX_CHAR_UUID
        await self.client.connect()
        svcs = await self.client.get_services()
        for x in svcs.characteristics:
           if svcs.characteristics[x].properties[0] == "write-without-response" and UART_TX_CHAR_UUID == "":
                UART_TX_CHAR_UUID = svcs.characteristics[x].uuid
           elif svcs.characteristics[x].properties[0] == "read" and UART_RX_CHAR_UUID == "":
                UART_RX_CHAR_UUID = svcs.characteristics[x].uuid

        #await self.client.start_notify(UART_TX_CHAR_UUID, self._handle_read)

    async def stop(self):
        await self.client.disconnect()

    async def writeColor(self):
            lista = [86, Colors["Red"], Colors["Green"], Colors["Blue"], (int(10 * 255 / 100) & 0xFF), 256-16, 256-86]
            values = bytearray(lista)
            try:
                await self.client.write_gatt_char(UART_TX_CHAR_UUID, values, False)
            except Exception as inst:
                print(inst)

    def _handle_disconnect(self, device) -> None:
        print("Device was disconnected, goodbye.")
        # cancelling all tasks effectively ends the program
        for task in asyncio.all_tasks():
            task.cancel()

    def _handle_read(self, _: int, data: bytearray) -> None:
        print("received:", data)
        self.messageChanged.emit(data)


class ColorSelector(QMainWindow):
  
    def __init__(self):
        super().__init__()
  
        # setting title
        self.setWindowTitle("Python ")
  
        # setting geometry
        self.setGeometry(100, 100, 500, 400)
  
        # calling method
        self.UiComponents()
  
        # showing all the widgets
        self.show()
  
  
    # method for components
    def UiComponents(self):
  
        # opening color dialog
        color = QColorDialog.getColor()
 
        Colors["Red"] = color.red()
        Colors["Blue"] = color.blue()
        Colors["Green"] = color.green()



class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.resize(640, 480)

        self._client = None

        scan_button = QPushButton("Scan Devices")
        self.devices_combobox = QComboBox()
        connect_button = QPushButton("Connect")
        self.message_lineedit = QLineEdit()
        send_button = QPushButton("Send Message")
        self.log_edit = QPlainTextEdit()

        central_widget = QWidget()
        self.setCentralWidget(central_widget)
        lay = QVBoxLayout(central_widget)
        lay.addWidget(scan_button)
        lay.addWidget(self.devices_combobox)
        lay.addWidget(connect_button)
        lay.addWidget(self.message_lineedit)
        lay.addWidget(send_button)
        lay.addWidget(self.log_edit)

        scan_button.clicked.connect(self.handle_scan)
        connect_button.clicked.connect(self.handle_connect)
        send_button.clicked.connect(self.handle_send)

    @cached_property
    def devices(self):
        return list()

    @property
    def current_client(self):
        return self._client

    async def build_client(self, device):
        if self._client is not None:
            await self._client.stop()
        self._client = QBleakClient(device)
        self._client.messageChanged.connect(self.handle_message_changed)
        await self._client.start()

    @qasync.asyncSlot()
    async def handle_connect(self):
        self.log_edit.appendPlainText("try connect")
        device = self.devices_combobox.currentData()
        if isinstance(device, BLEDevice):
            await self.build_client(device)
            self.log_edit.appendPlainText("connected")

    @qasync.asyncSlot()
    async def handle_scan(self):
        self.log_edit.appendPlainText("Started scanner")
        self.devices.clear()
        devices = await BleakScanner.discover()
        self.devices.extend(devices)
        self.devices_combobox.clear()
        for i, device in enumerate(self.devices):
            self.devices_combobox.insertItem(i, device.name, device)
        self.log_edit.appendPlainText("Finish scanner")

    def handle_message_changed(self, message):
        self.log_edit.appendPlainText(f"msg: {message.decode()}")
        
    @qasync.asyncSlot()
    async def handle_send(self):
        ColorSelector().show()
        await self.current_client.writeColor()

def main():
    app = QApplication(sys.argv)
    loop = qasync.QEventLoop(app)
    asyncio.set_event_loop(loop)
    w = MainWindow()
    w.show()
    with loop:
        loop.run_forever()


if __name__ == "__main__":
    main()