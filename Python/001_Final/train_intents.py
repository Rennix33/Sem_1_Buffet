import json
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.naive_bayes import MultinomialNB
from sklearn.pipeline import Pipeline
import joblib

with open("./Buffet_Poole26/Python/001_Final/intents.json") as file:
    data = json.load(file)["intents"]
    
X = [] # patterns
Y = [] # tags

for intent in data:
    for pattern in intent["patterns"]:
        X.append(pattern)
        Y.append(intent["tag"])

model = Pipeline([
    ("vectorizer", CountVectorizer()),
    ("classifier", MultinomialNB())
])

model.fit(X,Y)

joblib.dump(model, "./Buffet_Poole26/Python/001_Final/intents.pKl")

print("Job done!")

