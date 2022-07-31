from PyQt5.QtCore import QObject, pyqtSignal, QRect
from PyQt5 import QtGui
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

Colors = {"Red":0, "Green":0, "Blue":0}

class ColorSelector(QMainWindow):
  
    def __init__(self):
        super().__init__()
  
        # setting title
        self.setWindowTitle("Color Selector")
  
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
        
        return Colors

    def GetValue(self):
        return Colors
