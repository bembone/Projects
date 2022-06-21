import json
import string
import array
import requests


with open('WordsForPython.json') as json_file:
    Dictionary = json.load(json_file)           #Full Dictionary from: https://github.com/dwyl/english-words

keyboard = [
            ['1','2','3','4','5','6','7','8','9','0','+'],
            ['q','w','e','r','t','y','u','i','o','p','å'],
            ['a','s','d','f','g','h','j','k','l','ö','ä'],
            ['z','x','c','v','b','n','m',',','.','-','<']
            ]

Numpad = [                  # Maybe implement, maybe not
        ['7','8','9'],
        ['4','5','6'],
        ['1','2','3'],
        ['0',',']
        ]


def CheckWeakness():        #Main check function
    print("Checks weakness of password")

def CheckContainsCapital(password):
    print("Checks if password contains capital letters")
    if not password.islower():
        return True
    else:
        return False

def CheckFirstCharacterCapital(password):
    print("Check first char capital")
    Temp = ""
    for character in password:
        if character.isalpha():
            Temp += character
    if not Temp == "":
        if Temp[0].isalpha():
            if Temp[0].isupper():
                return True
        else:
            return False
        return False

def CheckDictionary(password):
    print("Checks if password contains dictionary word")
    Temp = ""
    for character in password:
        if character.isalpha():
            Temp += character.lower()

    if Temp in Dictionary:        #Password in dictionary checker
        return True
    else:
        return False

def CheckLength(password):
    print("Check length of password")
    if len(password) < 11:
        return True
    else:
        return False

def CheckSpecial(password):                     # Fixa med att kolla om special charachter
    print("Checks special characters")  # Är den sista charactern, antingen här eller i main check
    if not password.isalnum():
        return True
    else:
        return False

def CheckNumbers(password):
    print("Checks for numbers")
    for i in range (len(password)):
        if password[i].isdigit():
            return True
    return False

def CheckKeyboardPatterns(password):
    print("Checks password for different keyboard patterns")
    if CheckHorizontalLeftPattern(password):
        return True
    if CheckHorizontalRightPattern(password):
        print("found")
        return True
    if CheckRepetitionPattern(password):
        return True
    if CheckVerticalDownPattern(password):
        return True
    if CheckVerticalUpPattern(password):
        return True

def CheckHorizontalRightPattern(password):
    print("Checks password for horizontal right keyboard pattern")
    xpos = 0
    ypos = 0

    for x in range(4):
        for y in range(11): 
            if keyboard[x][y] == password[0].lower():
                xpos = x
                ypos = y

    count = 0
    for i in range(len(password)):            # Horizontal check to the right
        if ypos + i < 11:
            if keyboard[xpos][ypos+i] == password[0+i].lower() or keyboard[xpos][ypos+i] == password[0+i].upper():
                count=count+1
    if count >= 3:
        return True
    else:
        return False

def CheckHorizontalLeftPattern(password):
    print("Checks password for horizontal left keyboard pattern")
    xpos = 0
    ypos = 0

    for x in range(4):
        for y in range(11): 
            if keyboard[x][y] == password[0].lower():
                xpos = x
                ypos = y

    count = 0
    for i in range(len(password)):            # Horizontal check to the left
        if ypos-i > 0:
            if keyboard[xpos][ypos-i] == password[0+i].lower() or keyboard[xpos][ypos-i] == password[0+i].upper():
                count=count+1
    if count >= 3:
        return True
    else:
        return False

def CheckVerticalUpPattern(password):
    print("Checks password for vertical up keyboard pattern")
    xpos = 0
    ypos = 0

    for x in range(4):
        for y in range(11): 
            if keyboard[x][y] == password[0].lower():
                xpos = x
                ypos = y

    count = 0
    for i in range(len(password)):            # Horizontal check to the right
        if xpos - i > 0:
            if keyboard[xpos-i][ypos] == password[0+i].lower() or keyboard[xpos-i][ypos] == password[0+i].upper():
                count=count+1
    if count >= 3:
        return True
    else:
        return False

def CheckVerticalDownPattern(password):
    print("Checks password for vertical down keyboard pattern")
    xpos = 0
    ypos = 0

    for x in range(4):
        for y in range(11): 
            if keyboard[x][y] == password[0].lower():
                xpos = x
                ypos = y

    count = 0
    for i in range(len(password)):            # Horizontal check to the right
        if xpos + i < 4:
            if keyboard[xpos+i][ypos] == password[0+i].lower() or keyboard[xpos+i][ypos] == password[0+i].upper():
                count=count+1

    if count >= 3:
        return True
    else:
        return False

def CheckCombination():             # Ta bort kanske/ flytta till main filen
    print("Checks for combinations of patterns")

def CheckRepetitionPattern(password):
    print("Checks for repetition pattern")
    for i in range (len(password)):
        count = 0
        for j in range (len(password)):
            if password[i] == password[j]:
                count=count+1
            if count >= len(password)*0.5:
                return True
    x = (password+password).find(password, 1, -1)     # https://www.geeksforgeeks.org/python-check-if-string-repeats-itself/
    return False if x == -1 else True


def CheckLoneCharacter(password):   # Använda expand characters här
    if password[0].isalpha() and not password[1].isalpha():
        return True
    if password[len(password)-1].isalpha() and not password[len(password)-2].isalpha():
        return True
    for i in range(len(password)):
        if password[i].isalpha() and not i==0 and not i==len(password)-1:
            if password[i+1].isnumeric() and password[i-1].isnumeric():
                return True

    return False

def OnlyNumbers(password):
    print("Check if only numbers")
    for char in password:
        if not char.isnumeric():
            return False
    return True

# Kanske lägga till sen att om ordet är för kort, t ex "car" så gör man om det till
# Ett ord som liknar det som "automobile" mha dictionary api
# För att göra password längre

