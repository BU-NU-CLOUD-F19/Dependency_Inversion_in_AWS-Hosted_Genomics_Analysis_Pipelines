import requests

data='\"1,10,100,wuxi\"' #do not escape quotes
headers={'Content-Type': 'application/x-amz-json-1.0', 'x-amz-date': '20191204T04:42:05Z', 'Host': '2mck5641ia.execute-api.us-east-2.amazonaws.com'}
r = requests.post("https://2mck5641ia.execute-api.us-east-2.amazonaws.com/test-deploy-su/seqr-backend-service", headers=headers, data=data)

print(r.text)