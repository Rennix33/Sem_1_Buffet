import requests

#joke = requests.get("https://v2.jokeapi.dev/joke/Any?blacklistFlags=nsfw,religious,political,racist,sexist,explicit&type=twopart").json()


# Jokes with 2 parts have a set up and delivery
# joke["setup"] -> References the String of the setup of the joke
# joke["delivery"] -> References the String of the delivery part of the joke

funny = int(input("How many jokes would you like to hear? "))
print()

total = 1

for x in range(funny):
    joke = requests.get("https://v2.jokeapi.dev/joke/Any?blacklistFlags=nsfw,religious,political,racist,sexist,explicit&type=twopart").json()
    print("Joke " + str(total))
    print(joke["setup"])
    print(joke["delivery"])
    print()
    
    total = total + 1
    