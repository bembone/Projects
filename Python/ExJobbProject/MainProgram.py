# Class 1 - minimum security accepted, > 11 char, minst 1 capital, number och special
# Class 2 - lite mer security
# Class 3 - previous + replace w/symbols/num
#
#
#

import CheckWeakness
import PasswordCreation
import random

password = input("Enter a password: ")

def class1(password):
    newPassword1=password

    if CheckWeakness.CheckKeyboardPatterns(newPassword1):
        newPassword1 = PasswordCreation.SplitHalfAndFill(newPassword1)

    if not CheckWeakness.CheckNumbers(newPassword1):
        newPassword1 = PasswordCreation.AddNumber(newPassword1)
    if not CheckWeakness.CheckSpecial(newPassword1):
        newPassword1 = PasswordCreation.AddSpecialCharacterFirstPos(newPassword1)

    if CheckWeakness.CheckLength(newPassword1):
        if CheckWeakness.CheckDictionary(newPassword1):
            randomChoice = random.randint(0,1)
            if randomChoice==0:
                newPassword1 = PasswordCreation.AddDuplicateWord(newPassword1)
            if randomChoice==1:
                newPassword1 = PasswordCreation.ReplaceShortWithLong(newPassword1)
            if not CheckWeakness.CheckContainsCapital(newPassword1):
                newPassword1 = PasswordCreation.AddCapitalNoDictionary(newPassword1)

    if CheckWeakness.CheckFirstCharacterCapital(newPassword1):
        newPassword1 = PasswordCreation.IsFirstCapital(newPassword1)

    if CheckWeakness.CheckLength(newPassword1):
        newPassword1 = PasswordCreation.AddEasyString(newPassword1)

    if not CheckWeakness.CheckContainsCapital(newPassword1):
        newPassword1 = PasswordCreation.AddCapitalNoDictionary(newPassword1)
    return newPassword1

def class2(password):
    newPassword2 = password

    if CheckWeakness.OnlyNumbers(newPassword2):
        numTemp = PasswordCreation.ReplaceWithChar(newPassword2)
        print(numTemp)
        if not newPassword2 == numTemp:
            newPassword2 = PasswordCreation.ExpandCharacters(numTemp)
        else:
            newPassword2 == numTemp

    if CheckWeakness.CheckKeyboardPatterns(newPassword2):
        newPassword2 = PasswordCreation.SplitHalfAndFill(newPassword2)

    if CheckWeakness.CheckLoneCharacter(newPassword2):
        newPassword2 = PasswordCreation.ExpandCharacters(newPassword2)

    if CheckWeakness.CheckLength(newPassword2):
        if CheckWeakness.CheckDictionary(newPassword2):
            newPassword2 = PasswordCreation.ReplaceShortWithLong(newPassword2)
            newPassword2 = PasswordCreation.AddDuplicateWord(newPassword2)
            if not CheckWeakness.CheckContainsCapital(newPassword2):
                newPassword2 = PasswordCreation.AddCapitalNoDictionary(newPassword2)

    if not CheckWeakness.CheckNumbers(newPassword2):
        newPassword2 = PasswordCreation.AddNumber(newPassword2)
    if not CheckWeakness.CheckSpecial(newPassword2):
        newPassword2 = PasswordCreation.AddSpecialCharacterFirstPos(newPassword2)

    if CheckWeakness.CheckFirstCharacterCapital(newPassword2):
        newPassword2 = PasswordCreation.IsFirstCapital(newPassword2)

    if CheckWeakness.CheckLength(newPassword2):
        newPassword2 = PasswordCreation.AddEasyString(newPassword2)

    if not CheckWeakness.CheckContainsCapital(newPassword2):
        newPassword2 = PasswordCreation.AddCapitalNoDictionary(newPassword2)

    newPassword2 = PasswordCreation.ReplaceWithNumbers(newPassword2)

    return newPassword2

def class3(password):
    newPassword3 = password

    if CheckWeakness.OnlyNumbers(newPassword3):
        numTemp = PasswordCreation.ReplaceWithChar(newPassword3)
        if not newPassword3 == numTemp:
            newPassword3 = PasswordCreation.ExpandCharacters(numTemp)
        else:
            newPassword3 == numTemp

    if CheckWeakness.CheckKeyboardPatterns(newPassword3):
        newPassword3 = PasswordCreation.SplitHalfAndFill(newPassword3)

    if CheckWeakness.CheckLength(newPassword3):
        if CheckWeakness.CheckDictionary(newPassword3):
            newPassword3 = PasswordCreation.ReplaceShortWithLong(newPassword3)
            temp = PasswordCreation.SplitAndFill(newPassword3)
            if newPassword3 == temp:
                temp = PasswordCreation.AddDuplicateWord(newPassword3)
            if newPassword3 == temp:
                newPassword3 = PasswordCreation.SplitHalfAndFill(newPassword3)
            else:
                newPassword3 = temp
            if not CheckWeakness.CheckContainsCapital(newPassword3):
                newPassword3 = PasswordCreation.AddCapitalNoDictionary(newPassword3)

    if CheckWeakness.CheckLoneCharacter(newPassword3):
        newPassword3 = PasswordCreation.ExpandCharacters(newPassword3)

    if not CheckWeakness.CheckNumbers(newPassword3):
        newPassword3 = PasswordCreation.AddNumber(newPassword3)
    if not CheckWeakness.CheckSpecial(newPassword3):
        newPassword3 = PasswordCreation.AddSpecialCharacterFirstPos(newPassword3)

    if CheckWeakness.CheckFirstCharacterCapital(newPassword3):
        newPassword3 = PasswordCreation.IsFirstCapital(newPassword3)

    if CheckWeakness.CheckLength(newPassword3):
        newPassword3 = PasswordCreation.AddEasyString(newPassword3)

    if not CheckWeakness.CheckContainsCapital(newPassword3):
        newPassword3 = PasswordCreation.AddCapitalNoDictionary(newPassword3)

    newPassword3 = PasswordCreation.ReplaceWithNumbers(newPassword3)
    newPassword3 = PasswordCreation.ReplaceWithSymbols(newPassword3)
    newPassword3 = PasswordCreation.RandomCapital(newPassword3)

    return newPassword3

newPasswords = [class1(password), class2(password), class3(password)]
print(newPasswords)