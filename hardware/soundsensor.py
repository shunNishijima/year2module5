import RPi.GPIO as GPIO

import sys
name = "dany" #replace this with your username
sys.path.append(f'/home/{name}/Project/buzzer')

import buzzer
import melodies

buzzer.init_buzzer(25)

pin = 17

def init_sound_sensor(number):
    pin = number
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(pin, GPIO.IN)
    

def detect_sound():
    if GPIO.input(pin):
        buzzer.play_melody(melodies.round.MELODY, melodies.round.DURATIONS)

def start():
    init_sound_sensor(17)
    while True:
        detect_sound()

try:
    start()
except:
    print("ending")