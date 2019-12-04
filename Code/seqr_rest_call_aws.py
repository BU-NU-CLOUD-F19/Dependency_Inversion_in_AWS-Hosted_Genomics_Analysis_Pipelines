import requests

data="1,10,100,wuxi"
headers={'auth': 'allow'}
r = requests.post("https://hq0om13an6.execute-api.us-east-2.amazonaws.com/test", headers=headers, data=data)

print(r.text)