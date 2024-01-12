# -*- mode: python ; coding: utf-8 -*-

block_cipher = None

a = Analysis(['pyhl.py'],
             pathex=['LEDStripController'],
             binaries=[],
             datas=[('Flower.gif', '.'), ('gamma_table.npy', '.')],
             hiddenimports=[])

pyz = PYZ(a.pure, a.zipped_data,
             cipher=block_cipher)

exe = EXE(pyz,
          a.scripts,
          a.binaries,
          a.zipfiles,
          a.datas,
          [],
          name='PyHL - GUI',
          debug=False,
          bootloader_ignore_signals=False,
          bootloader_silent=False,
          runtime_tmpdir=None,
          console=True)
