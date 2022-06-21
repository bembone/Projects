# This program allows a user to enter a
# Code. If the C-Button is pressed on the
# keypad, the input is reset. If the user
# hits the A-Button, the input is checked.
 
import RPi.GPIO as GPIO
import time 
from time import sleep
import datetime
import threading
from google.cloud import firestore
from google.cloud import storage
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import os


# These are the GPIO pin numbers where the
# lines of the keypad matrix are connected
L1 = 5
L2 = 6
L3 = 13
L4 = 19
 
# These are the four columns, switching from 12 to 7 yes sir
C1 = 7
C2 = 16
C3 = 20
C4 = 21


LedGreen=15
LedYellow=18
LedRed=23
 
# The GPIO pin of the column of the key that is currently
# being held down or -1 if no key is pressed

os.environ['GOOGLE_APPLICATION_CREDENTIALS']='/home/pi/Downloads/larm-153bb-firebase-adminsdk-umigr-45bdabd2d4.json'

cred = credentials.Certificate('/home/pi/Downloads/larm-153bb-firebase-adminsdk-umigr-45bdabd2d4.json')

firebase_admin.initialize_app(cred, {
    'databaseURL' : 'https://larm-153bb-default-rtdb.europe-west1.firebasedatabase.app/'
    })



ref = db.reference('ProjektApp')
def getPassword():
    global databasCode
    global comparison
    global Notis
    Data = ref.order_by_child('Password').get()
    databasCode=Data["Password"]
    databasCode=databasCode.replace('"','')
    comparison=Data["Comparison"]
    comparison=comparison.replace('"','')
    Notis=Data["Notis"]
    Notis=Notis.replace('"','')
    
    
def getState():
    global databasState
    State = ref.order_by_child('State').get()
    databasState=State["State"]
    databasState=databasState.replace('"','')
    


getPassword()
ref.update({
    'Password': '1236'
})
getPassword()
getState()


secretCode="1234"
z = ""

#####
keypadPressed = -1
 
#secretCode = "4672"
input = ""
 
# Setup GPIO
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
 
GPIO.setup(L1, GPIO.OUT)
GPIO.setup(L2, GPIO.OUT)
GPIO.setup(L3, GPIO.OUT)
GPIO.setup(L4, GPIO.OUT)
 
# Use the internal pull-down resistors
GPIO.setup(C1, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
GPIO.setup(C2, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
GPIO.setup(C3, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
GPIO.setup(C4, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)

GPIO.setup(LedGreen, GPIO.OUT)
GPIO.setup(LedYellow, GPIO.OUT)
GPIO.setup(LedRed, GPIO.OUT)
 
# This callback registers the key that was pressed
# if no other key is currently pressed
def keypadCallback(channel):
    global keypadPressed
    if keypadPressed == -1:
        keypadPressed = channel
 
# Detect the rising edges on the column lines of the
# keypad. This way, we can detect if the user presses
# a button when we send a pulse.
GPIO.add_event_detect(C1, GPIO.RISING, callback=keypadCallback)
GPIO.add_event_detect(C2, GPIO.RISING, callback=keypadCallback)
GPIO.add_event_detect(C3, GPIO.RISING, callback=keypadCallback)
GPIO.add_event_detect(C4, GPIO.RISING, callback=keypadCallback)
 
# Sets all lines to a specific state. This is a helper
# for detecting when the user releases a button

# Buzzer
def DoorBuzzer():
    global keypadPressed
    global z
    global y
    global count
    global val
    global loop
    y=0
    reed = 14
    buzzer = 12
    ref.update({
        'State': '1'
    })
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(reed, GPIO.IN)
    GPIO.setup(buzzer, GPIO.OUT)
    p = GPIO.PWM(buzzer, 700)
    p.ChangeDutyCycle(0)
    count = 600
    loop = True
    while loop:
        getPassword()
        if comparison=="1":
            GPIO.output(LedYellow, GPIO.LOW)
            GPIO.output(LedGreen, GPIO.HIGH)
            loop = False
        print(input)
        z=""
        if keypadPressed != -1:
            setAllLines(GPIO.HIGH)
            if GPIO.input(keypadPressed) == 0:
                keypadPressed = -1
            else:
                time.sleep(0.01)
        else:
            if not checkSpecialKeys():
                readLine(L1, ["1","2","3","A"])
                readLine(L2, ["4","5","6","B"])
                readLine(L3, ["7","8","9","C"])
                readLine(L4, ["*","0","#","D"])
                time.sleep(0.01)
            else:
                time.sleep(0.01)
        val=0
        val=GPIO.input(reed)
        print(val)
        if (val):
            while(count>0):
                print(input)
                count=count-1
                if keypadPressed != -1:
                    setAllLines(GPIO.HIGH)
                    if GPIO.input(keypadPressed) == 0:
                        keypadPressed = -1
                    else:
                        time.sleep(0.01)
                # Otherwise, just read the input
                else:
                    if not checkTimerCode():  
                        readLine(L1, ["1","2","3","A"])
                        readLine(L2, ["4","5","6","B"])
                        readLine(L3, ["7","8","9","C"])
                        readLine(L4, ["*","0","#","D"])
                        time.sleep(0.01)
                    else:
                        time.sleep(0.01)
                if (count==0):
                    y=1
                
        if(y==1):
            p.start(1)
            print("buzzer on")
        else:
            p.stop()
            GPIO.output(LedRed, GPIO.LOW)
            print("buzzer off")
        
    
    if (y==1):
        ref.update({
        'Notis': '3'
    })
        
    sleep(.75)
    print("working")
    #p.start(1)
    ref.update({
    'Comparison': '0'
})
    ref.update({
    'State': '0'
})
    
        
    ##########################################
    
def LedsOff():
    GPIO.output(LedGreen, GPIO.LOW)
    GPIO.output(LedYellow, GPIO.LOW)
    GPIO.output(LedRed, GPIO.LOW)

##########
def setAllLines(state):
    GPIO.output(L1, state)
    GPIO.output(L2, state)
    GPIO.output(L3, state)
    GPIO.output(L4, state)
    
#############
def checkTimerCode():
    global input
    global count
    global y
    global loop
    pressed = False
 
    GPIO.output(L3, GPIO.HIGH)
 
    if (GPIO.input(C4) == 1):
        print("Input reset!");
        pressed = True
 
    GPIO.output(L3, GPIO.LOW)
    GPIO.output(L1, GPIO.HIGH)
    
    if (not pressed and GPIO.input(C4) == 1):
        input=""
        LedsOff()
        GPIO.output(LedYellow, GPIO.HIGH)
        
    GPIO.output(L1, GPIO.LOW)
    
    GPIO.output(L4, GPIO.HIGH)
 
    if (not pressed and GPIO.input(C1) == 1):
        if input == secretCode:
            print("Code correct!")
            count=-1
            y=0
            loop = False
            LedsOff()
            GPIO.output(LedGreen, GPIO.HIGH)
            # TODO: Unlock a door, turn a light on, etc.
        else:
            #print(input)      #Error testing
            print("Incorrect code!")
            LedsOff()
            GPIO.output(LedRed, GPIO.HIGH)
            # TODO: Sound an alarm, send an email, etc.
            
        pressed = True
    print(input)    
    GPIO.output(L4, GPIO.LOW)
    #GPIO.output(L3, GPIO.LOW)
    
    if pressed:
        input = ""
 
    return pressed

    
def checkAlarm():
    global input
    global z
    pressed = False
 
    GPIO.output(L3, GPIO.HIGH)
 
    if (GPIO.input(C4) == 1):
        print("Input reset!");
        pressed = True
 
    GPIO.output(L3, GPIO.LOW)
    GPIO.output(L4, GPIO.HIGH)
 
    if (not pressed and GPIO.input(C1) == 1):
        z=input
        #print("Z set")       #Error testing
        if (z==secretCode):
            print("Code correct!")
            LedsOff()
            GPIO.output(LedGreen, GPIO.HIGH)
            time.sleep(1)
            # TODO: Unlock a door, turn a light on, etc.
        else:
            #print(input)      #Error testing
            print(z)          #Error testing
            print("Incorrect code!")
            LedsOff()
            GPIO.output(LedRed, GPIO.HIGH)
            # TODO: Sound an alarm, send an email, etc.
            
        pressed = True
        
    GPIO.output(L4, GPIO.LOW)
    GPIO.output(L3, GPIO.LOW)
 
    if pressed:
        input = ""
 
    return pressed
 
def checkSpecialKeys():     #check enter,clear key. compare input to secretcode
    global input
    global val
    global loop
    pressed = False
 
    GPIO.output(L3, GPIO.HIGH)
 
    if (GPIO.input(C4) == 1):
        print("Input reset!");
        pressed = True
 
    GPIO.output(L3, GPIO.LOW)
    GPIO.output(L1, GPIO.HIGH)
    
    if (not pressed and GPIO.input(C4) == 1 and not loop):
        input=""
        LedsOff()
        GPIO.output(LedYellow, GPIO.HIGH)
        #time.sleep(9)
        DoorBuzzer()
        
    GPIO.output(L1, GPIO.LOW)
    
    GPIO.output(L4, GPIO.HIGH)
 
    if (not pressed and GPIO.input(C1) == 1):
        if input == secretCode:
            print("Code correct!")
            loop = False
            GPIO.output(LedYellow, GPIO.LOW)
            GPIO.output(LedGreen, GPIO.HIGH)
            ref.update({
            'State': '0'
            })
            # TODO: Unlock a door, turn a light on, etc.
        else:
            #print(input)      #Error testing
            print("Incorrect code!")
            LedsOff()
            GPIO.output(LedRed, GPIO.HIGH)
            time.sleep(1)
            LedsOff()
            GPIO.output(LedYellow, GPIO.HIGH)
            ref.update({
            'State': '0'
            })
            # TODO: Sound an alarm, send an email, etc.
            
        pressed = True
 
    GPIO.output(L4, GPIO.LOW)
    
    GPIO.output(L4, GPIO.HIGH)
 
    if (GPIO.input(C4) == 1):
        print("Door Open");
        val=1
        pressed = True
    
    GPIO.output(L4, GPIO.LOW)
    #GPIO.output(L3, GPIO.LOW)
 
    if pressed:
        input = ""
 
    return pressed
 
# reads the columns and appends the value, that corresponds
# to the button, to a variable
def readLine(line, characters):
    global input
    # We have to send a pulse on each line to
    # detect button presses
    GPIO.output(line, GPIO.HIGH)
    if(GPIO.input(C1) == 1):
        input = input + characters[0]
    if(GPIO.input(C2) == 1):
        input = input + characters[1]
    if(GPIO.input(C3) == 1):
        input = input + characters[2]
    if(GPIO.input(C4) == 1):
        input = input + characters[3]
    GPIO.output(line, GPIO.LOW)
    
########
def checkEnter():
    global input
    global secretCode
    pressed = False
    
    GPIO.output(L3, GPIO.HIGH)
 
    if (GPIO.input(C4) == 1):
        print("Input reset!");
        input=""
 
    GPIO.output(L3, GPIO.LOW)
 
    GPIO.output(L4, GPIO.HIGH)
 
    if (not pressed and GPIO.input(C1) == 1):
        secretCode=input
        #print(secretCode)    #Error testing
        print("Code set!")
        ref.update({
            'Password': secretCode
        })
        LedsOff()
        GPIO.output(LedGreen, GPIO.HIGH)
        time.sleep(1)
        pressed=True
            # TODO: Unlock a door, turn a light on, etc.
    
    GPIO.output(L4, GPIO.LOW)
 
    if pressed:
        input = ""
 
    return pressed


## Program start, initialize door password
GPIO.output(LedGreen, GPIO.HIGH)
GPIO.output(LedYellow, GPIO.HIGH)
GPIO.output(LedRed, GPIO.HIGH)
print("Enter your secret code: ")

try:
    q = True
    while q:
        # If a button was previously pressed,
        # check, whether the user has released it yet
        if keypadPressed != -1:
            setAllLines(GPIO.HIGH)
            if GPIO.input(keypadPressed) == 0:
                keypadPressed = -1
            else:
                time.sleep(0.15)
        # Otherwise, just read the input
        else:
            if not checkEnter():
                readLine(L1, ["1","2","3","A"])
                readLine(L2, ["4","5","6","B"])
                readLine(L3, ["7","8","9","C"])
                readLine(L4, ["*","0","#","D"])
                time.sleep(0.15)
            else:
                q=False
                time.sleep(0.15)
except KeyboardInterrupt:
    print("\Password set!")
    
ref.update({
    'State': '1'
})

###### End of initialization

LedsOff()
GPIO.output(LedYellow, GPIO.HIGH)
DoorBuzzer()
    
########
try:
    while True:
        getState()
        if databasState == "1":
            GPIO.output(LedGreen, GPIO.LOW)
            GPIO.output(LedYellow, GPIO.HIGH)
            DoorBuzzer()
        
        # If a button was previously pressed,
        # check, whether the user has released it yet
        if keypadPressed != -1:
            setAllLines(GPIO.HIGH)
            if GPIO.input(keypadPressed) == 0:
                keypadPressed = -1
            else:
                time.sleep(0.15)
        # Otherwise, just read the input
        else:
            if not checkSpecialKeys():
                readLine(L1, ["1","2","3","A"])
                readLine(L2, ["4","5","6","B"])
                readLine(L3, ["7","8","9","C"])
                readLine(L4, ["*","0","#","D"])
                time.sleep(0.15)
            else:
                time.sleep(0.15)
except KeyboardInterrupt:
    print("\nApplication stopped!")
################## 
