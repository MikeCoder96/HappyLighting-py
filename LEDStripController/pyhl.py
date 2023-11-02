from bleak import BleakScanner, BleakClient
import asyncio
import sys
import pyaudio
import qasync
import numpy as np
import scipy.cluster
from PIL import ImageGrab
from PyQt5.QtGui import *
from turtle import color
from dataclasses import dataclass
from functools import cached_property
from PyQt5.QtCore import *
from PyQt5.QtWidgets import *
import ExternalAudio
import BLEClass
import Utils
import matplotlib.image as img
try:
    from ctypes import windll
except ImportError:
    print("ctypes not imported due to different OS (Non Windows)")


class MainWindow(QMainWindow):
    def closeEvent(self, event):
        if self.device_address.text() != "":
            device = BLEClass.BleakScanner.find_device_by_address(self.device_address.text())
        else:
            device = self.devices_combobox.currentData()        

        if isinstance(device, BLEClass.BLEDevice):
            self.build_client(device)
            self.connect_button.disconnect()
            self.connect_button.clicked.connect(self.destroy_client)
            self.connect_button.setText("Disconnect")

    def __init__(self):
        global isModeUsed, idx
        super().__init__()
        self.setFixedSize(400, 300)
        isModeUsed = False
        idx = -1
        self.setWindowTitle("HappyLigthing-py")
        self.setWindowIcon(QIcon('HappyLighting-py_icon.png'))


        self.modeList = QListWidget(self)
        self.modeList.setGeometry(210, 110, 180, 180)
        self.modeList.itemDoubleClicked.connect(self.selectMode)

        self.horizontalSlider = QSlider(self)
        self.horizontalSlider.setObjectName(u"horizontalSlider")
        self.horizontalSlider.setGeometry(QRect(250, 80, 131, 22))
        self.horizontalSlider.setMinimum(1)
        self.horizontalSlider.setMaximum(10)
        self.horizontalSlider.setOrientation(Qt.Horizontal)
        self.horizontalSlider.valueChanged.connect(self.changeSpeed)

        self.deviceMic = QRadioButton(self)
        self.deviceMic.setText("Device Mic")
        self.deviceMic.setChecked(True)
        self.deviceMic.setGeometry(QRect(100, 170, 91, 20))

        self.micDevices_combobox = QComboBox(self)
        self.micDevices_combobox.setGeometry(QRect(10, 210, 191, 22))

        self.localMic = QRadioButton(self)
        self.localMic.setText("Local Mic")
        self.localMic.setChecked(False)
        self.localMic.setGeometry(QRect(10, 170, 91, 20))

        self.startCapture = QCheckBox(self)
        self.startCapture.setText("Start Capture")
        self.startCapture.setCheckState(Qt.Unchecked)
        self.startCapture.setGeometry(QRect(290, 60, 101, 20))

        self.scan_button = QPushButton(self)
        self.scan_button.setText("Scan")
        self.scan_button.setGeometry(QRect(10, 10, 75, 23))
        

        self.powerOn_button = QPushButton(self)
        self.powerOn_button.setText("Power On")
        self.powerOn_button.setGeometry(QRect(10, 65, 75, 23))
        self.powerOn_button.clicked.connect(self.changePowerToOn)

        self.powerOff_button = QPushButton(self)
        self.powerOff_button.setText("Power Off")
        self.powerOff_button.setGeometry(QRect(85, 65, 75, 23))
        self.powerOff_button.clicked.connect(self.changePowerToOff)

        self.connect_button = QPushButton(self)
        self.connect_button.setText("Connect")
        self.connect_button.setGeometry(QRect(10, 40, 75, 23))

        self.devices_combobox = QComboBox(self)
        self.devices_combobox.setGeometry(QRect(90, 10, 121, 22))
                # Label Create 
        self.label = QLabel(self) 
        self.label.setGeometry(QRect(220, 11, 20, 20)) 
        #self.label.setMinimumSize(QSize(100, 100)) 
        #self.label.setMaximumSize(QSize(300, 300)) 
        self.label.setObjectName("lb1") 
        self.label.setScaledContents(True)
        # Loading the GIF 
        self.movie = QMovie("Flower.gif") 
        self.label.setMovie(self.movie) 
        self.label.hide()

        self.device_address = QLineEdit(self)
        self.device_address.setGeometry(QRect(160, 40, 121, 22))

        self.label1 = QLabel(self)
        self.label1.setGeometry(QRect(90, 40, 71, 20))    
        self.label1.setText("Disconnected")
        self.label1.setStyleSheet("QLabel {color: red; }");

        self.label2 = QLabel(self)
        self.label2.setGeometry(QRect(10, 190, 71, 20))
        self.label2.setText("Input Devices")
        #self.label2.setStyleSheet("QLabel {color: red; }");

        self.send_button = QPushButton(self)
        self.send_button.setText("Color")
        self.send_button.setGeometry(QRect(310, 10, 80, 23))

        self.bass_button = QCheckBox(self)
        self.bass_button.setChecked(True)
        self.bass_button.setText("Bass")
        self.bass_button.setGeometry(QRect(10, 230, 51, 23))

        self.middle_button = QCheckBox(self)
        self.middle_button.setChecked(True)
        self.middle_button.setText("Middle")
        self.middle_button.setGeometry(QRect(80, 230, 51, 23))

        self.high_button = QCheckBox(self)
        self.high_button.setChecked(True)
        self.high_button.setText("High")
        self.high_button.setGeometry(QRect(150, 230, 51, 23))


        self.scan_button.clicked.connect(self.handle_scan)
        self.connect_button.clicked.connect(self.handle_connect)
        self.send_button.clicked.connect(self.handle_send)
        self.modeList.itemDoubleClicked.connect(self.selectMode)
        self.deviceMic.toggled.connect(self.handle_musicmode)
        self.localMic.toggled.connect(self.handle_musicmode)
        self.startCapture.stateChanged.connect(self.handle_startcapture)
        self.micDevices_combobox.currentIndexChanged.connect(self.updateMicDevice)
        self.bass_button.clicked.connect(lambda: self.handle_enabledisable("B"))
        self.middle_button.clicked.connect(lambda: self.handle_enabledisable("M"))
        self.high_button.clicked.connect(lambda: self.handle_enabledisable("H"))
    

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
        self.modeList.addItem("Music Mode")

        self.setElemetsActiveStatus(False)

    def setElemetsActiveStatus(self, status):
        self.micDevices_combobox.setEnabled(status)
        self.bass_button.setEnabled(status)
        self.middle_button.setEnabled(status)
        self.high_button.setEnabled(status)
        self.localMic.setEnabled(status)
        self.deviceMic.setEnabled(status)
        self.modeList.setEnabled(status)
        self.send_button.setEnabled(status)
        self.horizontalSlider.setEnabled(status)
        

    def selectMode(self, item):
        global isModeUsed, idx
        isModeUsed = True
        idx = self.modeList.indexFromItem(item).row()
        if idx <= 21: 
            self.handle_mode(idx)
        elif idx >= 22:
            if idx == 22:
                self.handle_musicmode()
            

    @cached_property
    def devices(self):
        return list()

    @property
    def current_client(self):
        return Utils.client

    @qasync.asyncSlot()
    async def build_client(self, device):
        if Utils.client is not None:
            await Utils.client.stop()
        Utils.client = BLEClass.QBleakClient(device)
        Utils.client.messageChanged.connect(self.handle_message_changed)
        await Utils.client.start()
 
    
    @qasync.asyncSlot()
    async def destroy_client(self):
        if Utils.client is not None:
            await Utils.client.stop()
            self.connect_button.disconnect()
            self.connect_button.clicked.connect(self.handle_connect)
            self.setElemetsActiveStatus(False)
            self.scan_button.setEnabled(True)
            self.label1.setText("Disconnected")
            self.connect_button.setText("Connect")
            self.label1.setStyleSheet("QLabel {color: red; }");
            

    @qasync.asyncSlot()
    async def handle_connect(self):
        #self.log_edit.appendPlainText("try connect")
        s_Address = self.device_address.text()
        if self.device_address.text() != "":
            device = await BLEClass.BleakScanner.find_device_by_address(self.device_address.text())
        else:
            device = self.devices_combobox.currentData()

        if isinstance(device, BLEClass.BLEDevice):
            await self.build_client(device)
            self.label1.setText("Connected")
            self.scan_button.setEnabled(False)
            #self.connect_button.setEnabled(False)
            info = Utils.p.get_host_api_info_by_index(0)
            numdevices = info.get('deviceCount')
            for i in range(0, numdevices):
                if (Utils.p.get_device_info_by_host_api_device_index(0, i).get('maxInputChannels')) > 0:
                    tmp_device = Utils.p.get_device_info_by_host_api_device_index(0, i)
                    Utils.InputDevices[i] = tmp_device
                    dev_name = tmp_device["name"]
                    self.micDevices_combobox.addItem(dev_name)
            self.label1.setStyleSheet("QLabel {color: green; }");
            self.setElemetsActiveStatus(True)
            self.connect_button.disconnect()
            self.connect_button.clicked.connect(self.destroy_client)
            self.connect_button.setText("Disconnect")

    @qasync.asyncSlot()
    async def handle_scan(self):
        #self.log_edit.appendPlainText("Started scanner")
        self.devices.clear()
        self.label.show()
        self.movie.start()
        devices = await BLEClass.BleakScanner.discover(timeout=8.0)
        self.devices.extend(devices)
        self.devices_combobox.clear()
        for i, device in enumerate(self.devices):
            if str(device.name).startswith("QHM"):
                Utils.printLog(("Found Device {}".format(device.name)))
                self.devices_combobox.insertItem(i, device.name, device)
        #self.log_edit.appendPlainText("Finish scanner")
        self.movie.stop() 
        self.label.hide()

    def changeSpeed(self, value):
        global isModeUsed
        Utils.Speed = value
        if isModeUsed:
            self.handle_mode(self.modeList.currentIndex().row())

    def changePowerToOn(self):
        self.handle_powerOn()

    def changePowerToOff(self):
        self.handle_powerOff()

    @qasync.asyncSlot()
    async def handle_powerOff(self):
        await self.current_client.writePower("Off")

    @qasync.asyncSlot()
    async def handle_powerOn(self):
        await self.current_client.writePower("On")

    @qasync.asyncSlot()
    async def handle_enabledisable(self, what):

        if what == "B":
            Utils.BlueMic = not Utils.BlueMic                  
        if what == "M":
            Utils.RedMic = not Utils.RedMic
        if what == "H":
            Utils.GreenMic = not Utils.GreenMic

        self.handle_rewrite()

    @qasync.asyncSlot()
    async def handle_rewrite(self):
          await self.current_client.writeColor()


    def handle_message_changed(self, message):
        pass
        #self.log_edit.appendPlainText(f"msg: {message.decode()}")
        
    @qasync.asyncSlot()
    async def handle_send(self):

        Utils.isModeUsed = False
        self.res = QColorDialog.getColor()
        try:
            await self.current_client.writeColor(self.res.red(), self.res.green(), self.res.blue())
        except Exception as ex:
            Utils.printLog("Colors error {}".format(ex))


    @qasync.asyncSlot()
    async def handle_mode(self, idx):
        await self.current_client.writeMode(idx)
    
    def updateMicDevice(self, index):
        Utils.selectedInputDevice = index


    @qasync.asyncSlot()
    async def handle_musicmode(self):
        global isModeUsed, idx
       
        if isModeUsed and idx == 22:
            if self.deviceMic.isChecked():
                Utils.localAudio = False
                await self.current_client.writeMicState(True)
            elif self.localMic.isChecked():
                await self.current_client.writeMicState(False)
                Utils.localAudio = True 
                await ExternalAudio.start_stream()
          

    async def captureImage(self):
        #NUM_CLUSTERS = 5
        while Utils.captureMode:
            def bincount_app(a):
                try:
                    a2D = a.reshape(-1,a.shape[-1])
                    col_range = (256, 256, 256) # generically : a2D.max(0)+1
                    a1D = np.ravel_multi_index(a2D.T, col_range)
                    return np.unravel_index(np.bincount(a1D).argmax(), col_range)
                except Exception as err:
                    Utils.printLog(err)
                    
            #print('reading image')
            im = ImageGrab.grab()
            #im = Image.open('image.jpg')
            im = im.resize((150, 150))      # optional, to reduce time
            im = np.array(im)

            colour = bincount_app(im)
            Utils.printLog(colour)
            await self.current_client.writeColor(colour[0], colour[1], colour[2])

    @qasync.asyncSlot()
    async def handle_startcapture(self): 
        if self.startCapture.checkState() == Qt.Checked:
            Utils.captureMode = True
            loop = asyncio.get_running_loop()
            loop.run_in_executor(None, lambda: asyncio.run(self.captureImage()))
        else:
            Utils.captureMode = False



def main():
    try:
        user32 = windll.user32
        user32.SetProcessDPIAware()
    except:
        pass
    Utils.app = QApplication(sys.argv)
    #Utils.app.aboutToQuit.connect(myExitHandler)
    loop = qasync.QEventLoop(Utils.app)
    asyncio.set_event_loop(loop)
    w = MainWindow()
    w.show()
    with loop:
        loop.run_forever()

def test():
    pass

if __name__ == "__main__":
    main()