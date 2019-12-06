import requests
import sys

if len(sys.argv) != 5:
    print("This script should take 4 arguments: chromosome, min, max and service")
    exit()

if sys.argv[4].lower() not in ["hail", "wuxi"]:
    print("service selected should be hail or wuxi")
    exit()

if not (sys.argv[1].isdigit() and sys.argv[2].isdigit() and sys.argv[3].isdigit()):
    print("chromosome, min and max should be number format")
    exit()

data='\"' + sys.argv[1] + ',' + sys.argv[2] + ',' + sys.argv[3] + ',' + sys.argv[4] + '\"' #do not escape quotes
print(data)
headers={'Content-Type': 'application/x-amz-json-1.0', 'x-amz-date': '20191204T04:42:05Z', 'Host': '2mck5641ia.execute-api.us-east-2.amazonaws.com'}
r = requests.post("https://2mck5641ia.execute-api.us-east-2.amazonaws.com/test-deploy-su/seqr-backend-service", headers=headers, data=data)

print(r.text)