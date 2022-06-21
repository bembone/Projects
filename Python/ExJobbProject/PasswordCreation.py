from distutils.dep_util import newer_pairwise
from hashlib import new
import random
from tkinter import W
from CheckWeakness import Dictionary
from DictionaryAPI import get_synonyms
import string

symbols = ['!', '@','#','$','&','£','-','_','.','>','<']
numbers = ['0','1','2','3','4','5','6','7','8','9']

def ReplaceWithSymbols(password):   #Kanske göra att alla av en char blir converted
    print("ReplaceWithSymbols")     #Så att det blir enklare att remember
    temp = ""
    for i in range(len(password)):
        var = random.randint(1,5)
        if var==1 or var==2:
            if password[i] == 'a':
                temp += '@'
            elif password[i] == 's':
                temp += '$'
            elif password[i] == '8':
                temp += '&'
            elif password[i] == 'c':
                temp += '('
            elif password[i] == 'h':
                temp += '#'
            else:
                temp += password[i]
        else:
            temp += password[i]
    return temp

def ReplaceWithNumbers(password):
    print("ReplaceWithNumbers")
    temp = ""
    for i in range(len(password)):
        var = random.randint(1,5)
        if var==1 or var==2:
            if password[i] == 'o':
                temp += '0'
            elif password[i] == 'i':
                temp += '1'
            elif password[i] == 'e':
                temp += '3'
            elif password[i] == 'a':
                temp += '4'
            elif password[i] == 's':
                temp += '5'
            elif password[i] == 'b':
                temp += '6'
            else:
                temp += password[i]
        else:
            temp += password[i]
    return temp   

def ReplaceWithChar(password):
    print("ReplaceWithChar")
    temp = ""
    pos=[]
    for char in password:
        if char=='1' or char=='3' or char=='4' or char=='5' or char=='6':
            pos.append(password.index(char))

    if len(pos)==0:
        return password
    
    var = pos[random.randint(0,len(pos)-1)]
    for i in range(len(password)):
        if password[var] == '1' and i==var:
            temp += 'l'
        elif password[var] == '3' and i==var:
            temp += 'e'
        elif password[var] == '4' and i==var:
            temp += 'a'
        elif password[var] == '5' and i==var:
            temp += 's'
        elif password[var] == '6' and i==var:
            temp += 'b'
        else:
            temp += password[i]
    return temp   

def SplitAndFill(password):
    print("SplittAndFill")
    temp = ""
    alphaPassword = IgnoreNumbersandSpecial(password)
    for i in range(len(alphaPassword)):
        temp += alphaPassword[i]
        if len(temp) > 2:
            if temp in Dictionary:
                if len(temp) != len(alphaPassword):
                    s1 = slice(0,len(temp))
                    s2 = slice(len(temp), len(alphaPassword))
                    Method = random.randint(0,1)
                    newWord = ""
                    if Method == 0:     # Fill with numbers
                        newWord += alphaPassword[s1]
                        newWord += str(random.randint(000,999))
                        newWord += alphaPassword[s2]
                        return RestoreStart(password) + newWord + RestoreEnd(password)
                    elif Method == 1: #fill with synonym
                        TheSyns = get_synonyms(alphaPassword[s1])
                        newWord += alphaPassword[s1]
                        if TheSyns:
                            newWord += TheSyns[random.randint(0,len(TheSyns)-1)]
                        newWord += alphaPassword[s2]
                        return RestoreStart(password) + newWord + RestoreEnd(password)
                    else:
                        print("Error")
    else:
        return password

def SplitHalfAndFill(password):
    print("SplitHalfAndFill")
    newPassword = []
    s1 = slice(0,(len(password)//2))
    s2 = slice((len(password)//2), len(password))
    res = {key:val for key, val in Dictionary.items() 
                        if len(key) < 9 and len(key) > 4}

    newArray = list(res.keys())
    newPassword += password[s1]
    newPassword += newArray[random.randint(0,len(newArray)-1)]
    newPassword += password[s2]
    newPassword[len(password[s1])] = newPassword[len(password[s1])].upper()
    newPassword[0] = newPassword[0].upper()
    newPassword[len(newPassword)-len(password[s2])] = newPassword[len(newPassword)-len(password[s2])].upper()
    return "".join(newPassword)

def ReplaceShortWithLong(password):
    print("ReplaceShortWithLong")
    newWord = ""
    tempList=[]
    Temp = IgnoreNumbersandSpecial(password)
    print(Temp)
    if Temp in Dictionary:
        if len(Temp) < 7:
            Syns = get_synonyms(Temp)
            if Syns:
                for i in range(len(Syns)):
                    if len(Syns[i]) > len(Temp):
                        tempList.append(Syns[i])
                    else:
                        tempList.append(password)
                newWord = tempList[random.randint(0,len(tempList)-1)]
                newWord = newWord.replace(" ","")
            else:
                return password
        else:
            return password

    return RestoreStart(password) + newWord + RestoreEnd(password)

def AddEasyString(password):
    print("AddEasyString")
    newPassword=""
    res = {key:val for key, val in Dictionary.items() 
                        if len(key) < 5 and len(key) > 2}

    newArray = list(res.keys())

    newPassword += password
    newPassword += newArray[random.randint(0,len(newArray)-1)]
        
    return newPassword

def ExpandCharacters(password):     # Hämta ord som börjar på bokstav från dictionary
    print("ExpandCharacters")          # Sen replace bokstav i password med ett ord
    temp=""
    for i in range(len(password)):
        if password[i].isalpha():
            temp += password[i]

    # Se om antal bokstäver i ordet är <= 3 sen replace sista bokstav med random ord som börjar på den
    res = {key:val for key, val in Dictionary.items() 
                    if key.startswith(temp[len(temp)-1].lower()) and len(key) <= 7 and len(key) > 2}

    newArray = list(res.keys())
    newPassword= list("")

    for i in range(len(temp)-1):
        newPassword.append(temp[i])
        
    newPassword.append(newArray[random.randint(0,len(newArray)-1)])

    return RestoreStart(password) + "".join(newPassword) + RestoreEnd(password)

def EveryOtherCharFill(password):   # Fylla varanan med symbol/siffra om kort lösenord
    print("innit")

def AddCapitalDictionary(password): # kolla i main om det returnas samma password
    print("AddCapitalDictionary")   # Samma betyder att operationen inte genomförts
    temp = ""
    count = 0
    newWord = ""
    first = ""
    for i in range(len(password)):
        temp += password[i]
        if len(temp) > 2:
            if temp in Dictionary:
                count +=1
                if count == 1:
                    first += temp
                    temp = ""
                if count == 2:
                    for j in range(len(temp)):
                        if j == 0:
                            newWord += temp[j].swapcase()
                        if j == len(temp)-1:
                            newWord += temp[len(temp)-1].swapcase()
                        elif not j == 0 and not j == len(temp)-1:
                            newWord += temp[j]
    
    if password.find((first+newWord)) == len(password)-1-len((first+newWord)):
        return first+newWord
    else:
        return password

def AddCapitalNoDictionary(password):   # Lägg till capital i slutet oavsett vilken string det är
    print("AddCapitalNoDictionary")
    newPassword=""  
    count=0  
    for i in reversed(range(len(password))):
        if password[i].isalpha() and count==0:
            newPassword += password[i].upper()
            count+=1
        else:
            newPassword += password[i]
    newPassword=newPassword[::-1]
    return newPassword

def AddSpecialCharacterFirstPos(password):
    print("AddSpecialCharacterFirstPos")
    temp = []

    temp.append(symbols[random.randint(0,len(symbols)-1)])
    temp.append(password)

    return "".join(temp)

def AddDuplicateWord(password):     # ex "car123 -> car123car"     
    print("AddDuplicateWord")
    temp = ""
    newPassword=""
    alphaPassword = IgnoreNumbersandSpecial(password)
    for i in range(len(alphaPassword)):
        temp += alphaPassword[i]
        if len(temp) > 2:
            if temp in Dictionary:
                newPassword+=alphaPassword
                newPassword+=temp
                break

    return RestoreStart(password) + newPassword + RestoreEnd(password)

def AddNumber(password):
    print("AddNumber")
    newPassword=""
    count=0
    for i in reversed(range(len(password))):
        if password[i].isnumeric():
            count+=1
    for i in range(len(password)):
        newPassword += password[i]
    if count == 1:
        newPassword += str(random.randint(00,99))
    if count == 0:
        newPassword += str(random.randint(000,999))
    return newPassword

def RandomCapital(password):
    print("Random capital")
    newPassword=""
    for i in range(len(password)):
        randy = random.randint(0,1)
        if password[i].isalpha() and randy == 1:
            newPassword += password[i].upper()
        else:
            newPassword += password[i]
    return newPassword

def IsFirstCapital(password):
    print("innit")
    print(password)
    Temp = ""
    for character in password:
        if character.isalpha():
            Temp += character
    newPassword = AddCapitalNoDictionary(Temp)
    return RestoreStart(password) + newPassword + RestoreEnd(password)

def IgnoreNumbersandSpecial(password):
    Temp = ""
    for character in password:
        if character.isalpha():
            Temp += character.lower()
    return Temp

def RestoreStart(password):
    nums=""
    for i in range(len(password)):
        if password[i].isnumeric() or not password[i].isalnum():
            nums += password[i]
        else:
            break
    return nums

def RestoreEnd(password):
    nums=""
    for i in reversed(range(len(password))):
        if password[i].isnumeric() or not password[i].isalnum():
            nums += password[i]
        else:
            break
    return nums[::-1]



    #gör något annat för att lösa repetition
    #identify zigazag pattern
    #do sumfing wif numpad -> detect patterns
    #classes
    #kommenkommen -> behålla repeating word och göra extra
    # dedrdfdcdxds -> deDrfrgfhcjxks