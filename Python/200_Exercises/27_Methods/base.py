def add(x, y):
    sum = x + y
    return sum
    
def mult(x, y):
    prod = x * y
    return prod
    
    
def printList(lst):
    i = 1
    for iteam in lst:
        print(str(i) + ". " + iteam)
        i = i + 1 

    
   #-----------------------
def addList(lst):
    sum = 0
    for num in lst:
        sum = sum + num
    return sum

christmasList = ["Legos", "RTX 5090", "money", "Phone", "E-Bike", "More money", "Air pods"]
printList(christmasList)

listFavNum = [67, 43, 41, 21, 27, 2 ,6 , 7]

print(addList(listFavNum))