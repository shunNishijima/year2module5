import RPi.GPIO as GPIO

import tcp
import serial
import time

ser = serial.Serial('/dev/ttyS0',115200)
ser.flushInput

power_key = 6
rec_buff = ''
time_count = 0

def handle_data(data):
    coordinates = data.split(',', 4)

    lat = "35.661910"
    long = "139.708794"
    
    if coordinates[0] != '':
        shortLat = coordinates[0][:2]
        longLat = coordinates[0][2:11]
        northSouth = coordinates[1]

        shortLong = coordinates[2][:3]
        longLong = coordinates[2][3:12]
        eastWest = coordinates[3]

        lat = float(shortLat) + (float(longLat)/60)
        long = float(shortLong) + (float(longLong)/60)

        if northSouth == 'S': lat = -lat
        if eastWest == 'W': long = -long

    tcp.send_data("GPS~admin~" + str(lat) + "," + str(long))

def send_at(command,back,timeout):
    rec_buff = ''
    ser.write((command + '\r\n').encode())
    time.sleep(timeout)
    if ser.inWaiting():
        time.sleep(0.01)
        rec_buff = ser.read(ser.inWaiting())
    
    if rec_buff != '':
        received = rec_buff.decode()
        if back not in received:
            print('ERROR:')
            print(command + ' returned:' + str(received)[2:])
            return 0
        else:
            handle_data(received[25:])
            return 1
    else:
        print('GPS is not ready')

def get_gps_position():
    rec_null = True
    answer = 0
    print('Start GPS session....')
    send_at('AT+CGPS=1', 'OK', 1)
    
    time.sleep(2)
    i = 0
    while rec_null:
        print(i)
        i += 1
        answer = send_at('AT+CGPSINFO', '+CGPSINFO: ', 1)
        if 1 != answer:
            print('error %d'%answer)
            send_at('AT+CGPS=0','OK',1)
        
def init_gps(powerkey):
    print('turning on....')
    GPIO.setmode(GPIO.BCM)
    GPIO.setwarnings(False)
    GPIO.setup(power_key, GPIO.OUT)
    time.sleep(0.1)
    GPIO.output(power_key,GPIO.HIGH)
    time.sleep(2)
    GPIO.output(power_key, GPIO.LOW)
    time.sleep(20)
    ser.flushInput()

def power_down(power_key):
    print('\nturning off')
    GPIO.output(power_key, GPIO.HIGH)
    time.sleep(3)
    GPIO.output(power_key, GPIO.LOW)
    time.sleep(18)
    print('finished')

def start():
    try:
        init_gps(power_key)
        get_gps_position()
        power_down(power_key)
    except:
        if ser != None:
            ser.close()
            power_down(power_key)

start()