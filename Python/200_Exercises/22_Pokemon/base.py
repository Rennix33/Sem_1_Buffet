import requests

pokemon = requests.get("https://pokeapi.co/api/v2/pokemon/pikachu").json()

# Use this JSON formatter to better visualize the JSON from the Pokemon website
# https://jsonformatter.org/json-viewer

what_poke = input("What pokemon do you want to learn about: ")
pokemon = requests.get("https://pokeapi.co/api/v2/pokemon/" + what_poke).json()



print(pokemon["name"])
print(pokemon["height"])
print(pokemon["weight"])
print(pokemon["abilities"][0]["ability"]["name"])
print(pokemon["abilities"][1]["ability"]["name"])
