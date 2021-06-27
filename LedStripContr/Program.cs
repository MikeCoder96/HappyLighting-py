using System;
using System.Diagnostics;
using System.Linq;
using Windows.Devices.Bluetooth;
using Windows.Devices.Bluetooth.Advertisement;
using Windows.Devices.Bluetooth.GenericAttributeProfile;
using Windows.Devices.Enumeration;
using Windows.Storage.Streams;

namespace ConsoleApplication1
{
    class Program
    {
        static void Main(string[] args)
        {
            // Start the program
            var program = new Program();

            // Close on key press
            Console.ReadLine();
        }

        public Program()
        {
            // Create Bluetooth Listener
            var watcher = new BluetoothLEAdvertisementWatcher
            {
                ScanningMode = BluetoothLEScanningMode.Active
            };

            // Only activate the watcher when we're recieving values >= -80
            //watcher.SignalStrengthFilter.InRangeThresholdInDBm = -80;

            // Stop watching if the value drops below -90 (user walked away)
            //watcher.SignalStrengthFilter.OutOfRangeThresholdInDBm = -90;

            // Register callback for when we see an advertisements
            //watcher.Received += OnAdvertisementReceived;

            // Wait 5 seconds to make sure the device is really out of range
            //.SignalStrengthFilter.OutOfRangeTimeout = TimeSpan.FromMilliseconds(5000);
            //watcher.SignalStrengthFilter.SamplingInterval = TimeSpan.FromMilliseconds(2000);

            // Starting watching for advertisements
            //watcher.Start();

            watcher.Start();
            writeChar();
        }

        private async void writeChar()
        {
            try
            {
                //var devicex=await DeviceInformation.CreateFromIdAsync(GattDeviceService.GetDeviceSelectorFromUuid(new Guid("00001800-0000-1000-8000-00805f9b34fb")), new string[] { "System.Devices.ContainerId" }); //get all connected devices
                var devices = await DeviceInformation.FindAllAsync(GattDeviceService.GetDeviceSelectorFromUuid(new Guid("0000ffd9-0000-1000-8000-00805f9b34fb")), new string[] { "System.Devices.ContainerId" }); //get all connected devices
                if (devices.Count > 0)
                {
                    foreach (var device in devices)
                    {
                        BluetoothLEDevice dev = await BluetoothLEDevice.FromIdAsync(device.Id);
                        DevicePairingResult dpr = await dev.DeviceInformation.Pairing.PairAsync(DevicePairingProtectionLevel.Encryption);
                        Debug.WriteLine(dev.ConnectionStatus);
                        var services = await dev.GetGattServicesAsync();
                        foreach (var service in services.Services)
                        {
                            //Debug.WriteLine($"Service: {service.Uuid}");
                            //service.Uuid.GetType();
                            var characteristics = await service.GetCharacteristicsAsync(BluetoothCacheMode.Cached);
                            foreach (var character in characteristics.Characteristics)
                            {
                                var result = await character.ReadValueAsync();
                                var reader = DataReader.FromBuffer(result.Value);
                                var input = new byte[reader.UnconsumedBufferLength];
                                reader.ReadBytes(input);
                                Debug.WriteLine(BitConverter.ToString(input));
                                Debug.WriteLine("Characteristic Handle: " +
                                           character.AttributeHandle + ", UUID: " +
                                           character.Uuid);
                            }
                        }
                    }
                }
            }
            catch { }
        }

        private void OnAdvertisementReceived(BluetoothLEAdvertisementWatcher watcher, BluetoothLEAdvertisementReceivedEventArgs eventArgs)
        {
            // Tell the user we see an advertisement and print some properties
            /*Console.WriteLine(String.Format("Advertisement:"));
            Console.WriteLine(String.Format("  BT_ADDR: {0}", eventArgs.BluetoothAddress));
            Console.WriteLine(String.Format("  FR_NAME: {0}", eventArgs.Advertisement.LocalName));
            Console.WriteLine();*/
        }
    }
}