import sys
import RPi.GPIO as GPIO
import time

name = "dany" #replace this with your username
sys.path.append(f'/home/{name}/Project')
sys.path.append(f'/home/{name}/Project/buzzer')

import keypad
import servo
import buzzer
import melodies
import interfaces
import tcp
import connect


keypad.init_keypad(5, 13, 19, 26, 12, 16, 20, 21)
buzzer.init_buzzer(25)
servo.init_servo(11)
servo.lock()

def user_interface():
    servo.unlock()
    quit = False
    interfaces.d_print(4)
    while (not quit):
        character = None
        character = keypad.read()
        if character == "A":
            quit = True
            servo.lock()

def admin():
    quit = False
    while (not quit):
        interfaces.d_print(5)
        character = None
        character = keypad.read()
        if character == "A":
            if (interfaces.states["lock"] == 0):
                state = {"lock":1}
                interfaces.states.update(state)
                servo.unlock()
            else:
                state = {"lock":0}
                interfaces.states.update(state)
                servo.lock()
        elif character == "B":
            settings()
        elif character == "C":
            if (interfaces.states["lock"] == 1):
                state = {"lock":0}
                interfaces.states.update(state)
                servo.lock()
            quit = True

def settings():
    quit = False
    while (not quit):
        interfaces.d_print(6)
        character = None
        character = keypad.read()
        if character == "A":
            hardware()
        elif character == "B":
            passw()
        elif character == "C":
            quit = True
            
def hardware():
    quit = False
    while (not quit):
        interfaces.d_print(7)
        character = None
        character = keypad.read()
        if character == "A":
            if (interfaces.states["lights"] == 6):
                state = {"lights":7}
                interfaces.states.update(state)
                tcp.send_data('LED~0')
            else:
                state = {"lights":6}
                interfaces.states.update(state)
                tcp.send_data('LED~1')
        elif character == "B":
            if (interfaces.states["sounds"] == 6):
                state = {"sounds":7}
                interfaces.states.update(state)
                tcp.send_data('BUZZER~0')
            else:
                state = {"sounds":6}
                interfaces.states.update(state)
                tcp.send_data('BUZZER~1')
        elif character == "C":
            quit = True

def passw():
    quit = False
    while (not quit):
        interfaces.d_print(8)
        character = None
        character = keypad.read()
        if character == "A":
            change_pass(0)
            quit = True
        elif character == "B":
            change_pass(1)
            quit = True
        elif character == "C":
            quit = True

def change_pass(user):
    interfaces.output = "____"
    input = ""
    if user == 0:
        update = "UPDATE~user~"
        new = {"user":4}
        interfaces.states.update(new)
    else:
        update = "UPDATE~admin~"
        new = {"user":5}
        interfaces.states.update(new)
    quit = False
    while (not quit):
        interfaces.d_print(9)
        character = None
        character = keypad.read()
        if character != None:
            if character == "*":
                input = ""
                interfaces.output = "____"
                quit = True
            elif character == "#":
                if len(input) == 4:
                    tcp.send_data(update + input)
                    input = ""
                    interfaces.output = "____"
                    quit = True
                else:
                    input = ""
                    interfaces.output = "____"
                    interfaces.d_print(10)
                    time.sleep(2)
            elif len(input) < 4:
                if character not in invalid:
                    input = input + character
                    interfaces.output = ""
                    i = 0
                    while i < len(input):
                        i += 1
                        interfaces.output += "*"
                    while len(interfaces.output) < 4:
                        interfaces.output += "_"
                else:
                    interfaces.d_print(1)
                    time.sleep(2)

def start():
    global invalid
    brute = 0
    input = ""
    invalid = ["A", "B", "C", "D"]
    if (connect.check_hardware('LED') == 1):
        state = {"lights":6}
        interfaces.states.update(state)
    else:
        state = {"lights":7}
        interfaces.states.update(state)
    if (connect.check_hardware('buzzer') == 1):
        state = {"sounds":6}
        interfaces.states.update(state)
    else:
        state = {"sounds":7}
        interfaces.states.update(state)
    while True:
        interfaces.d_print(0)
        
        character = None
        character = keypad.read()
        if character != None:
            if character == "*":
                input = ""
                interfaces.output = "____"
            elif character == "#":
                if  1 == connect.check_pass(input):
                    brute = 0
                    interfaces.d_print(2)
                    buzzer.play_melody(melodies.round.MELODY, melodies.round.DURATIONS)
                    user_interface()
                elif 0 == connect.check_pass(input):
                    brute = 0
                    interfaces.d_print(2)
                    buzzer.play_melody(melodies.round.MELODY, melodies.round.DURATIONS)
                    admin()
                else:
                    interfaces.d_print(3)
                    brute +=1
                    buzzer.play_melody(melodies.rickroll.MELODY, melodies.rickroll.DURATIONS)
                    if brute == 1:
                        brute = 0
                        tcp.send_data("BRUTE")
                interfaces.output = "____"
                input = ""
            elif len(input) < 4:
                if character not in invalid:
                    input = input + character
                    interfaces.output = ""
                    i = 0
                    while i < len(input):
                        i += 1
                        interfaces.output += "*"
                    while len(interfaces.output) < 4:
                        interfaces.output += "_"
                else:
                    interfaces.d_print(1)

try:                    
    start()
except:
    print("ending")
finally:
    interfaces.d_clear()