x = int(input("Enter a number: "))
y = int(input("Enter another number: "))
a = input("Would you like to add subtract, multiply, or divide. +, -, *, /, : ")

if(a == str("+")):
    print(x + y)
elif(a == str("-")):
    print(x - y)
elif(a == str("*")):
    print(x * y)
elif(a == str("/")):
    print(x / y)
