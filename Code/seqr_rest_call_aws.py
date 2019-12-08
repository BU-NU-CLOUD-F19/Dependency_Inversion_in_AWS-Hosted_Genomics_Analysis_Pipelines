import requests
import sys


def post_to_lambda(data):
    r = requests.post("https://ed6bixg19e.execute-api.us-east-2.amazonaws.com/demo/seqr-backend-service", headers=headers, data=data)
    return r.text


def guaranteed_to_fail():
    response = post_to_lambda('\"1,1,1,cat\"')
    if "Error Service" in response:
        return "test passed"


if __name__ == '__main__':
    headers = {'Content-Type': 'application/x-amz-json-1.0', 'Host': 'ed6bixg19e.execute-api.us-east-2.amazonaws.com'}

    if sys.argv[1] == '1':
        data = '\"Variants:Chromosome=1:TestSecretManager\"'
        print("Passing parameter --  Variants:Chromosome=1:TestSecretManager to Lambda")
    elif sys.argv[1] == '2':
        data = '\"Variants:Chromosome=1PosStart=900000PosEnd=1000000:Hail\"'
        print("Passing parameter --  Variants:Chromosome=1PosStart=900000PosEnd=1000000:Hail to Lambda")
    elif sys.argv[1] == '3':
        data = '\"Variants:Chromosome=1:Wuxi\"'
        print("Passing parameter --  Variants:Chromosome=1:Wuxi to Lambda")
    elif sys.argv[1] == '4':
        data = '\"Variants:Chromosome=1PosStart=10PosEnd=100:Wuxi\"'
        print("Passing parameter --  Variants:Chromosome=1PosStart=10PosEnd=100:Wuxi to Lambda")

    print(post_to_lambda(data))
