enter = int(input("Please enter a number: "))

total = 0

for x in range(enter): 
    total = x + total
    print(x + 1, end= " + ")
    print(total)
total = enter + total
print(total)