import RPi.GPIO as GPIO
import time

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)

#setting up the pins to the raspberry pi which are related to the keypad
def init_keypad(row0, row1, row2, row3, col0, col1, col2, col3):
    
    global r0, r1, r2, r3, c0, c1, c2, c3, c4
    
    #rows are the black wires
    r0 = row0
    r1 = row1
    r2 = row2
    r3 = row3

    #columns are the white wires
    c0 = col0
    c1 = col1
    c2 = col2
    c3 = col3
    
    #setting the rows to output pins
    GPIO.setup(r0, GPIO.OUT)
    GPIO.setup(r1, GPIO.OUT)
    GPIO.setup(r2, GPIO.OUT)
    GPIO.setup(r3, GPIO.OUT)

    #setting the columns to input pins
    #by using pull down resistor the logical state of the pin is 0
    #however, when pressed it would be 1
    GPIO.setup(c0, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
    GPIO.setup(c1, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
    GPIO.setup(c2, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
    GPIO.setup(c3, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
    
#this function checks which column is high and returns a character accordingly
def readColumn(row, characters):
    GPIO.output(row, GPIO.HIGH)
    if(GPIO.input(c0) == 1):
        return(characters[0])
    if(GPIO.input(c1) == 1):
        return(characters[1])
    if(GPIO.input(c2) == 1):
        return(characters[2])
    if(GPIO.input(c3) == 1):
        return(characters[3])
    GPIO.output(row, GPIO.LOW)

#this function checks 
def read():
        x1 = readColumn(r0, ["1","2","3","A"])
        if x1 != None:
             time.sleep(0.3)
             return x1
        x2 = readColumn(r1, ["4","5","6","B"])
        if x2 != None:
             time.sleep(0.3)
             return x2
        x3 = readColumn(r2, ["7","8","9","C"])
        if x3 != None:
             time.sleep(0.3)
             return x3
        x4 = readColumn(r3, ["*","0","#","D"])
        if x4 != None:
             time.sleep(0.3)
             return x4