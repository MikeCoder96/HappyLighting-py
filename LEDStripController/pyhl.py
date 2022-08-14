import sys
import qasync
import asyncio
from PyQt5 import QtGui
from turtle import color
from ColorSelectorWin import *
from dataclasses import dataclass
from functools import cached_property
from bleak import BleakScanner, BleakClient
from bleak.backends.device import BLEDevice
from PyQt5.QtCore import QObject, pyqtSignal, QRect, Qt

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
    QListWidget,
    QLabel,
    QSlider
)

DEBUG_LOGS = True

UART_SERVICE_UUID = ""
UART_RX_CHAR_UUID = ""
UART_TX_CHAR_UUID = ""
UART_SAFE_SIZE = 20

Colors = {"Red":0, "Green":0, "Blue":0}
Modes = [37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 
         47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 
         97, 98, 99]
Speed = 0
KodeUsed = -1
isModeUsed = False

def printLog(text):
    if DEBUG_LOGS:
        print("[+] {}".format(text))

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
                printLog("Set UART_TX_CHAR_UUID with {}".format(svcs.characteristics[x].uuid))
                UART_TX_CHAR_UUID = svcs.characteristics[x].uuid
           elif svcs.characteristics[x].properties[0] == "read" and UART_RX_CHAR_UUID == "":
                printLog("Set UART_RX_CHAR_UUID with {}".format(svcs.characteristics[x].uuid))
                UART_RX_CHAR_UUID = svcs.characteristics[x].uuid

        #await self.client.start_notify(UART_TX_CHAR_UUID, self._handle_read)

    async def stop(self):
        await self.client.disconnect()

    async def writeColor(self):
            lista = [86, Colors["Red"], Colors["Green"], Colors["Blue"], (int(10 * 255 / 100) & 0xFF), 256-16, 256-86]
            values = bytearray(lista)
            try:
                printLog("Change Color called R:{} G:{} B:{} ".format(Colors["Red"], Colors["Green"], Colors["Blue"]))
                await self.client.write_gatt_char(UART_TX_CHAR_UUID, values, False)
            except Exception as inst:
                print(inst)

    async def writeMode(self, idx):
            global Speed
            #new byte[] { 256 - 69, mode, (byte)(speed & 0xFF), 68 };
            i_mode = Modes[idx]
            lista = [256 - 69, i_mode, (Speed & 0xFF), 68]
            values = bytearray(lista)
            try:
                printLog("Change Mode with ID {} ".format(i_mode))
                await self.client.write_gatt_char(UART_TX_CHAR_UUID, values, False)
            except Exception as inst:
                print(inst)

    #TODO: Implement disconnect function
    def _handle_disconnect(self, device) -> None:
        printLog("Device was disconnected")
        # cancelling all tasks effectively ends the program
        for task in asyncio.all_tasks():
            task.cancel()


class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.resize(400, 300)

        self._client = None
        self.setWindowIcon(QtGui.QIcon('about_icon.png'))
        self.setWindowTitle("PyHL")
        
        self.modeList = QListWidget(self)
        
        self.modeList.itemDoubleClicked.connect(self.selectMode)

        self.horizontalSlider = QSlider(self)
        self.horizontalSlider.setObjectName(u"horizontalSlider")
        self.horizontalSlider.setGeometry(QRect(160, 60, 160, 22))
        self.horizontalSlider.setMinimum(1)
        self.horizontalSlider.setMaximum(10)
        self.horizontalSlider.setOrientation(Qt.Horizontal)
        self.horizontalSlider.valueChanged.connect(self.changeSpeed)

        self.modeList.setGeometry(150, 90, 180, 180)
        self.modeList.addItem("Pulsating rainbow")
        self.modeList.addItem("Pulsating red")
        self.modeList.addItem("Pulsating green")
        self.modeList.addItem("Pulsating blue")
        self.modeList.addItem("Pulsating yellow")
        self.modeList.addItem("Pulsating cyan")
        self.modeList.addItem("Pulsating purple")
        self.modeList.addItem("Pulsating white")
        self.modeList.addItem("Pulsating red/green")
        self.modeList.addItem("Pulsating red/blue")
        self.modeList.addItem("Pulsating green/blue")
        self.modeList.addItem("Rainbow strobe")
        self.modeList.addItem("Red strobe")
        self.modeList.addItem("Green strobe")
        self.modeList.addItem("Blue strobe")
        self.modeList.addItem("Yellow strobe")
        self.modeList.addItem("Cyan strobe")
        self.modeList.addItem("Purple strobe")
        self.modeList.addItem("white strobe")
        self.modeList.addItem("Rainbow jumping change")
        self.modeList.addItem("Pulsating RGB")
        self.modeList.addItem("RGB jumping change")


        self.scan_button = QPushButton(self)
        self.scan_button.setText("Scan")
        self.scan_button.setGeometry(QRect(10, 10, 75, 23))

        self.connect_button = QPushButton(self)
        self.connect_button.setText("Connect")
        self.connect_button.setGeometry(QRect(10, 40, 75, 23))

        self.devices_combobox = QComboBox(self)
        self.devices_combobox.setGeometry(QRect(90, 10, 121, 22))

        label = QLabel(self)
        label.setGeometry(QRect(90, 40, 71, 20))

        self.label = QLabel(self)
        self.label.setGeometry(QRect(90, 40, 71, 20))
        self.label.setText("Disconnected")
        self.label.setStyleSheet("QLabel {color: red; }");

        self.send_button = QPushButton(self)
        self.send_button.setText("Color")
        self.send_button.setGeometry(QRect(10, 140, 80, 23))

        #central_widget = QWidget()
        #self.setCentralWidget(central_widget)
        #lay = QVBoxLayout(central_widget)
        #lay.addWidget(pushButton)
        #lay.addWidget(self.devices_combobox)
        #lay.addWidget(connect_button)
        #lay.addWidget(self.message_lineedit)
        #lay.addWidget(send_button)
        #lay.addWidget(self.log_edit)

        self.scan_button.clicked.connect(self.handle_scan)
        self.connect_button.clicked.connect(self.handle_connect)
        self.send_button.clicked.connect(self.handle_send)
        self.modeList.itemDoubleClicked.connect(self.selectMode)


    def selectMode(self, item):
        global isModeUsed

        isModeUsed = True
        self.handle_mode(self.modeList.indexFromItem(item).row())

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
        #self.log_edit.appendPlainText("try connect")
        device = self.devices_combobox.currentData()
        if isinstance(device, BLEDevice):
            await self.build_client(device)
            self.label.setText("Connected")
            self.scan_button.setEnabled(False)
            self.connect_button.setEnabled(False)
            self.label.setStyleSheet("QLabel {color: green; }");

    @qasync.asyncSlot()
    async def handle_scan(self):
        #self.log_edit.appendPlainText("Started scanner")
        self.devices.clear()
        devices = await BleakScanner.discover()
        self.devices.extend(devices)
        self.devices_combobox.clear()
        for i, device in enumerate(self.devices):
            if str(device.name).startswith("QHM"):
                printLog("Found Device {}".format(device.name))
                self.devices_combobox.insertItem(i, device.name, device)
        #self.log_edit.appendPlainText("Finish scanner")

    def changeSpeed(self, value):
        global Speed

        Speed = value
        if isModeUsed:
            self.handle_mode(self.modeList.currentIndex().row())


    def handle_message_changed(self, message):
        pass
        #self.log_edit.appendPlainText(f"msg: {message.decode()}")
        
    @qasync.asyncSlot()
    async def handle_send(self):
        global Colors
        global isModeUsed

        isModeUsed = False
        self.res = ColorSelector()
        self.res.show()
        Colors = res.GetValue()
        await self.current_client.writeColor()


    @qasync.asyncSlot()
    async def handle_mode(self, idx):
        await self.current_client.writeMode(idx)

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