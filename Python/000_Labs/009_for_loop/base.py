q = int(input("Please enter a line length: "))
w = input("Do you want it horizontal or vertical: ")



if w == "vertical":
    for a in range (q):
        print("*")
elif w == "horizontal":
    for a in range (q):
        print("*", end="")