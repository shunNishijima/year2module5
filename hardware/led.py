import RPi.GPIO as GPIO
import time    
import connect

def rc_time(LDR_pin):

    GPIO.setup(LDR_pin, GPIO.OUT)
    GPIO.output(LDR_pin, GPIO.LOW)
    time.sleep(0.1)

    GPIO.setup(LDR_pin, GPIO.IN)
    
    start_time = round(time.time()*1000)
    while (GPIO.input(LDR_pin) == GPIO.LOW):
        pass

    end_time = round(time.time()*1000)
        
    time_diff = end_time - start_time

    return time_diff

LDR_pin = 24
LED_pin = 27

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False) # Ignore warnings
GPIO.setup(LED_pin, GPIO.OUT)
GPIO.output(LED_pin, GPIO.LOW)
def start():
    while True:
        charge_time = rc_time(LDR_pin)

        if charge_time > 200 and connect.check_hardware('LED') == 1:
            GPIO.output(LED_pin, GPIO.HIGH)
        else:
            GPIO.output(LED_pin, GPIO.LOW)

try:
    start()
except:
    print("ending")