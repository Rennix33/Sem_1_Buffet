#Run "pip install yfinance" in a terminal

import yfinance as yf

list = ["GOOGLE" , "RKLB", "TSLA", "NVDA", "SPAX.PVT", "MNKD" , "JPM" , "TGT", "AMZN" , "MBG.DE"]
    

which_stock = input("Choose a company: GOOGL, RKLB, TSLA, NVDA, SPAX.PVT, MNKD, JPM, TGT, AMZN, MBD.DE: ")

for tick in list:
    
    

    stock = yf.Ticker("GOOGL")
    current_price = stock.history(period="1d")["Close"][0]
    info = stock.info

    print(info["longName"])
    print(info["website"])
    print("GOOGL Current Price:", current_price)






    stock = yf.Ticker("Rocket Lab")
    current_price = stock.history(period="id")["Close"][1]
    info = stock.info

    print(info["longName"])
    print(info["website"])
    print("RKLB Current Price:", current_price)





    stock = yf.Ticker("TSLA")
    current_price = stock.history(period="1d")["Close"][0]
    info = stock.info

    print(info["longName"])
    print(info["website"])
    print("TSLA Current Price:", current_price)





    stock = yf.Ticker("NVDA")
    current_price = stock.history(period="1d")["Close"][0]
    info = stock.info

    print(info["longName"])
    print(info["website"])
    print("NVDA Current Price:", current_price)



    stock = yf.Ticker("SPAX.PVT")
    current_price = stock.history(period="1d")["Close"][0]
    info = stock.info

    print(info["longName"])
    print(info["website"])
    print("SPAX.PVT Current Price:", current_price)




    stock = yf.Ticker("MNKD")
    current_price = stock.history(period="1d")["Close"][0]
    info = stock.info

    print(info["longName"])
    print(info["website"])
    print("MNKD Current Price:", current_price)



    stock = yf.Ticker("JPM")
    current_price = stock.history(period="1d")["Close"][0]
    info = stock.info

    print(info["longName"])
    print(info["website"])
    print("JPM Current Price:", current_price)



    stock = yf.Ticker("AMZN")
    current_price = stock.history(period="1d")["Close"][0]
    info = stock.info

    print(info["longName"])
    print(info["website"])
    print("AMZN Current Price:", current_price)






    stock = yf.Ticker("MBG.DE")
    current_price = stock.history(period="1d")["Close"][0]
    info = stock.info

    print(info["longName"])
    print(info["website"])
    print("MBG.DE Current Price:", current_price)