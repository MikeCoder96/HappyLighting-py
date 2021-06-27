import asyncio
from bleak import BleakClient
from bleak import BleakScanner
from time import sleep

address = "e2:3b:04:00:18:cd"
MODEL_NBR_UUID = "0000ffd9-0000-1000-8000-00805f9b34fb"

async def run(address):
    async with BleakClient(address) as client:
        while True:
            try:
                print("Select: ")
                x = int(input())
                if x == 1: #Change Color
                    #byte[] arrayOfByte = new byte[7];
                    #arrayOfByte[0] = 86;
                    #arrayOfByte[1] = (byte)(Color.red(paramMyColor.color) * paramMyColor.progress / 100);
                    #arrayOfByte[2] = (byte)(Color.green(paramMyColor.color) * paramMyColor.progress / 100);
                    #arrayOfByte[3] = (byte)(Color.blue(paramMyColor.color) * paramMyColor.progress / 100);
                    #arrayOfByte[4] = (byte)(paramMyColor.warmWhite * paramMyColor.progress / 100 & 0xFF);
                    #arrayOfByte[5] = -16;
                    #arrayOfByte[6] = -86;
                    #synTimedata((byte)65, (byte)(Color.red(paramMyColor.color) * paramMyColor.progress / 100), (byte)(Color.red(paramMyColor.color) * paramMyColor.progress / 100), (byte)(Color.green(paramMyColor.color) * paramMyColor.progress / 100), (byte)(Color.blue(paramMyColor.color) * paramMyColor.progress / 100), (byte)0);
                    #if (paramMyColor.warmWhite != 0) {
                    #  arrayOfByte[1] = 0;
                    #  arrayOfByte[2] = 0;
                    #  arrayOfByte[3] = 0;
                    #  arrayOfByte[5] = 15;
                    #  arrayOfByte[4] = (byte)(paramMyColor.progress * 255 / 100 & 0xFF);
                    #  synTimedata((byte)65, (byte)0, (byte)0, (byte)0, (byte)0, (byte)(paramMyColor.progress * 255 / 100 & 0xFF));
                    #} 

                    #lista = [86, 0, 0, 0, (int(100 * 255 / 100) & 0xFF), 15, 256-86]
                    #values = bytearray(lista)
                    #await client.write_gatt_char(MODEL_NBR_UUID, values, False)
                    print("RED: ")
                    red = int(input())
                    print("GREEN: ")
                    green = int(input())
                    print("BLUE: ")
                    blue = int(input())
                    lista = [86, red, green, blue, (int(10 * 255 / 100) & 0xFF), 256-16, 256-86]
                    values = bytearray(lista)
                    try:
                        await client.write_gatt_char(MODEL_NBR_UUID, values, False)
                    except:
                        pass
                elif x == 2: #Warzone Airplane
                    val = 50
                    count = 0 # 7
                    verse = False
                    while True:
                        lista = [86, val, 0, 0, (int(10 * 255 / 100) & 0xFF), 256-16, 256-86]
                        values = bytearray(lista)
                        try:
                            await client.write_gatt_char(MODEL_NBR_UUID, values, False)
                            if verse:
                                val += 1
                            else:
                                val -= 1
                            if val <= 20:
                                verse = True
                            if val >= 80:
                                count += 1
                                if count >= 5:
                                    lista = [86, 0, 255, 0, (int(10 * 255 / 100) & 0xFF), 256-16, 256-86]
                                    values = bytearray(lista)
                                    await client.write_gatt_char(MODEL_NBR_UUID, values, False)
                                    break
                                verse = False
                            sleep(0.01)
                            #sleep(0.2)
                        except:
                            pass
                else: #Change Mod
                    #public final byte[] Mods = new byte[] { 
                    #      37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 
                    #      47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 
                    #      97, 98, 99 };

                    mod = [37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 
                            47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 
                            97, 98, 99]

                    #if (paramInt1 < 0 || paramInt1 >= this.Mods.length)
                    #  return; 
                    #this.modId = paramInt1;
                    #byte[] arrayOfByte = new byte[4];
                    #arrayOfByte[0] = (byte)-69;
                    #arrayOfByte[1] = (byte)this.Mods[paramInt1];
                    #byte b = (byte)(paramInt2 & 0xFF);
                    #arrayOfByte[2] = b;
                    #arrayOfByte[3] = (byte)68;
                    #this.cachecmd = arrayOfByte;
                    #writeCharacteristic("0000ffd5-0000-1000-8000-00805f9b34fb", "0000ffd9-0000-1000-8000-00805f9b34fb", arrayOfByte);
                    #StringBuilder stringBuilder = new StringBuilder();
                    #stringBuilder.append("setMod Speed=");
                    #stringBuilder.append(paramInt2);
                    #stringBuilder.append(" mod=");
                    #stringBuilder.append(this.Mods[paramInt1]);
                    #Log.e("setMod", stringBuilder.toString());
                    #synTimedata(this.Mods[paramInt1], b, (byte)0, (byte)0, (byte)0, (byte)0);

                    print("Mode: ")
                    mode = int(input())
                    speed = int(input()) #255 slow, 0 fast  
                    lista = [256-69, mod[mode], (speed & 0xFF), 68]
                    values = bytearray(lista)
                    try:
                        await client.write_gatt_char(MODEL_NBR_UUID, values, False)
                    except:
                        pass
            except:
                pass

loop = asyncio.get_event_loop()
loop.run_until_complete(run(address))