import random

how_many = int(input("How many random numbers would you like? "))

numbers = []

for i in range(how_many):
    num = random.randint(1, 10)
    numbers.append(num)

print("Your numbers are:", numbers)
