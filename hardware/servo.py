# Import libraries
import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BCM)

def init_servo(pin):
    global servo1
    GPIO.setup(pin,GPIO.OUT)
    servo1 = GPIO.PWM(pin,50)
    servo1.start(0)

def turn(angle):
    servo1.ChangeDutyCycle(2+(angle/18))
    time.sleep(0.5)
    servo1.ChangeDutyCycle(0)

def lock():
    turn(70)

def unlock():
    turn(0)