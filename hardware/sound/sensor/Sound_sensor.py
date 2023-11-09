import RPi.GPIO as GPIO
import time

Audio_sensor_pin = 0

def init_sound_sensor(pin):

    """
    Connect the sensor using 3.3v, GND and pin.
    """
    global Audio_sensor_pin
    
    GPIO.setmode (GPIO.BCM)
    Audio_sensor_pin = pin
    GPIO.setup(Audio_sensor_pin, GPIO.IN)

def detect_sound (Audio_sensor_pin):
    """
    Boolean function. Returns true if clap detected.
    """
    if GPIO.input(Audio_sensor_pin):
        time.sleep(1)
        return True
        #print("Clap detected!")

#test code
#while True:
#    detect_sound(Audio_sensor_pin)