symbol = input("What symbol would you like to use: ")
width = int(input("What’s the width of your box: "))
length = int(input("What’s the length of your box: "))

for a in range(length):         
    for i in range(width):     
        print(symbol, end="")     
    print()                       


