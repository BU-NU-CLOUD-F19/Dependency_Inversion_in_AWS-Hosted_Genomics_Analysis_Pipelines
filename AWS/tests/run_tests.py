import requests

#run_tests.py
#Performs the following tests on the SeqrLambdaService :
#testcase_validGor - Tests a valid GOR query string and expects a valid response
#testcase_validHail - Tests a valid Hail query string and expects a valid response
#testcase_invalidQuery1 - Tests an invalid request with incorrect number of arguments
#testcase_invalidQuery2 - Tests a valid request with an unknown service

def post_data(url, data):
    return requests.post(url, data).content
    
def testcase_validGor(url):
    resp = post_data(url, "\"Variants:Chromosome=1PosStart=100PosEnd=200:Wuxi\"")
    expected = "gor -p chr1:100:200 #wesVars#" # "GorDB credentials successfully obtained \nBCH and WuXi have not finished the REST API for this...\nQuery to be executed: gor -p chr1:100:200 #wesVars#\n"
    if expected in str(resp):
        print("testcase_validGor PASSED !")
    else:
        print("testcase_validGor FAILED !" + str(resp));
        
def testcase_validHail(url):
    resp = str(post_data(url, "\"Variants:Chromosome=1PosStart=90000PosEnd=1000000:Hail\""))
    if '\\\\"variant_class\\\\" : \\\\"deletion\\\\"\\\\n' in resp:
        print("testcase_validHail PASSED !")
       # print(str(resp))
    else:
        print("testcase_validHail FAILED ! ")
        
def testcase_invalidQuery1(url):
    resp = str(post_data(url, "\"Variants:Chromosome=1PosStart=900000PosEnd=1000000\""))
    expected = '"Bad query string: Variants:Chromosome=1PosStart=900000PosEnd=1000000 \n  Please make sure you have arguments for: chromosome, start, end, service"'
    if "Bad query string" in resp and "Please make sure you have arguments for: chromosome, start, end, service" in resp:
        print("testcase_invalidQuery1 PASSED !")
    else:
        print(resp)
        print("testcase_invalidQuery1 FAILED !")
        
def testcase_invalidQuery2(url):
    resp = str(post_data(url, "\"Variants:Chromosome=1PosStart=900000PosEnd=1000000:cat\""))
    expected = 'b\'"Could not decipher implementation: cat"\''
    if resp == expected:
        print("testcase_invalidQuery2 PASSED !")
    else:
        print(resp)
        print("testcase_invalidQuery2 FAILED !")
        
def main():
    url = "https://qssvxiw6pk.execute-api.us-east-2.amazonaws.com/dev/lambdaservice"
    testcase_validGor(url)
    testcase_validHail(url)
    testcase_invalidQuery1(url)
    testcase_invalidQuery2(url)
    
if __name__ == '__main__': main()