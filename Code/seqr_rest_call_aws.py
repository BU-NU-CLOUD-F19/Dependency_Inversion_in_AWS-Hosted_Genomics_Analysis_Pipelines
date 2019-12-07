import requests
import sys


def post_to_lambda(data):
    r = requests.post("https://2mck5641ia.execute-api.us-east-2.amazonaws.com/test-deploy-su/seqr-backend-service", headers=headers, data=data)
    return r.text


def guaranteed_to_fail():
    response = post_to_lambda('\"1,1,1,cat\"')
    if "Error Service" in response:
        return "test passed"


if __name__ == '__main__':
    headers = {'Content-Type': 'application/x-amz-json-1.0', 'Host': '2mck5641ia.execute-api.us-east-2.amazonaws.com'}

    if len(sys.argv) != 5:
        print("This script should take 4 arguments: chromosome, min, max and service. Given " + str(len(sys.argv)))
        print(guaranteed_to_fail())
        exit()

    if sys.argv[4].lower() not in ["hail", "wuxi"]:
        print("service selected should be hail or wuxi. Given " + sys.argv[4])
        print(guaranteed_to_fail())
        exit()

    if not (sys.argv[1].isdigit() and sys.argv[2].isdigit() and sys.argv[3].isdigit()):
        print("chromosome, min and max should be number format")
        print(guaranteed_to_fail())
        exit()

    data='\"' + sys.argv[1] + ',' + sys.argv[2] + ',' + sys.argv[3] + ',' + sys.argv[4] + '\"' #do not escape quotes
    print(post_to_lambda(data))
