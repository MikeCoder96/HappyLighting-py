from bleak import BleakScanner, BleakClient
import asyncio
import sys
import qasync
from PyQt5 import QtGui
from turtle import color
from dataclasses import dataclass
from functools import cached_property
from PyQt5.QtCore import QRect, Qt
from PyQt5.QtWidgets import (
    QApplication,
    QComboBox,
    QCheckBox,
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

import ExternalAudio
import Microphone
import BLEClass
import Utils

class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.resize(400, 300)

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

        self.deviceMic = QCheckBox(self)
        self.deviceMic.setText("Device Mic")
        self.deviceMic.setCheckState(Qt.Unchecked)
        self.deviceMic.setGeometry(QRect(10, 90, 101, 20))

        self.localMic = QCheckBox(self)
        self.localMic.setText("Local Mic")
        self.localMic.setCheckState(Qt.Unchecked)
        self.localMic.setGeometry(QRect(10, 110, 101, 20))

        self.scan_button = QPushButton(self)
        self.scan_button.setText("Scan")
        self.scan_button.setGeometry(QRect(10, 10, 75, 23))

        self.connect_button = QPushButton(self)
        self.connect_button.setText("Connect")
        self.connect_button.setGeometry(QRect(10, 40, 75, 23))

        self.devices_combobox = QComboBox(self)
        self.devices_combobox.setGeometry(QRect(90, 10, 121, 22))

        self.label = QLabel(self)
        self.label.setGeometry(QRect(90, 40, 71, 20))

        self.label = QLabel(self)
        self.label.setGeometry(QRect(90, 40, 71, 20))
        self.label.setText("Disconnected")
        self.label.setStyleSheet("QLabel {color: red; }");

        self.send_button = QPushButton(self)
        self.send_button.setText("Color")
        self.send_button.setGeometry(QRect(10, 140, 80, 23))

        self.scan_button.clicked.connect(self.handle_scan)
        self.connect_button.clicked.connect(self.handle_connect)
        self.send_button.clicked.connect(self.handle_send)
        self.modeList.itemDoubleClicked.connect(self.selectMode)
        self.deviceMic.stateChanged.connect(self.handle_mic)
        self.localMic.stateChanged.connect(self.handle_localmic)


    def selectMode(self, item):
        global isModeUsed

        isModeUsed = True
        self.handle_mode(self.modeList.indexFromItem(item).row())

    @cached_property
    def devices(self):
        return list()

    @property
    def current_client(self):
        return Utils.client

    async def build_client(self, device):

        if Utils.client is not None:
            await Utils.client.stop()
        Utils.client = BLEClass.QBleakClient(device)
        Utils.client.messageChanged.connect(self.handle_message_changed)
        await Utils.client.start()

    @qasync.asyncSlot()
    async def handle_connect(self):
        #self.log_edit.appendPlainText("try connect")
        device = self.devices_combobox.currentData()
        if isinstance(device, BLEClass.BLEDevice):
            await self.build_client(device)
            self.label.setText("Connected")
            self.scan_button.setEnabled(False)
            self.connect_button.setEnabled(False)
            self.label.setStyleSheet("QLabel {color: green; }");

    @qasync.asyncSlot()
    async def handle_scan(self):
        #self.log_edit.appendPlainText("Started scanner")
        self.devices.clear()
        devices = await BLEClass.BleakScanner.discover()
        self.devices.extend(devices)
        self.devices_combobox.clear()
        for i, device in enumerate(self.devices):
            if str(device.name).startswith("QHM"):
                Utils.printLog(("Found Device {}".format(device.name)))
                self.devices_combobox.insertItem(i, device.name, device)
        #self.log_edit.appendPlainText("Finish scanner")

    def changeSpeed(self, value):

        Utils.Speed = value
        if isModeUsed:
            self.handle_mode(self.modeList.currentIndex().row())


    def handle_message_changed(self, message):
        pass
        #self.log_edit.appendPlainText(f"msg: {message.decode()}")
        
    @qasync.asyncSlot()
    async def handle_send(self):

        Utils.isModeUsed = False
        self.res = QColorDialog.getColor()
        try:
            Utils.Colors["Red"] = self.res.red()
            Utils.Colors["Blue"] = self.res.blue()
            Utils.Colors["Green"] = self.res.green()
            await self.current_client.writeColor()
        except Exception as ex:
            Utils.printLog("Colors error {}".format(ex))


    @qasync.asyncSlot()
    async def handle_mode(self, idx):
        await self.current_client.writeMode(idx)

    @qasync.asyncSlot()
    async def handle_mic(self):
        if self.deviceMic.checkState() == Qt.Checked:
            await self.current_client.writeMicState(True)
        else:
            await self.current_client.writeMicState(False)
        
    @qasync.asyncSlot()
    async def handle_localmic(self): 
        if self.localMic.checkState() == Qt.Checked:
            Utils.localAudio = True 
            await ExternalAudio.start_stream()
        else:
            Utils.localAudio = False

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