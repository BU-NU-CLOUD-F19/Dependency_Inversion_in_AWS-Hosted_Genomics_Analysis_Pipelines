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
    resp = post_data(url, "\"1,100,200,Wuxi\"")
    expected = b'"gor -p chr1:100-200 #dbsnp#"'
    if resp ==  expected:
        print("testcase_validGor PASSED !")
    else:
    
        print("testcase_validGor FAILED !");

def testcase_validHail(url):
    resp = post_data(url, "\"1,100,200,Hail\"")
    expected = b'"{\\n  \\"took\\" : 7,\\n  \\"timed_out\\" : false,\\n  \\"_shards\\" : {\\n    \\"total\\" : 6,\\n    \\"successful\\" : 6,\\n    \\"skipped\\" : 0,\\n    \\"failed\\" : 0\\n  },\\n  \\"hits\\" : {\\n    \\"total\\" : {\\n      \\"value\\" : 0,\\n      \\"relation\\" : \\"eq\\"\\n    },\\n    \\"max_score\\" : null,\\n    \\"hits\\" : [ ]\\n  }\\n}\\n"'
    if resp ==  expected:
        print("testcase_validHail PASSED !")
    else:
        print(resp)
        print("testcase_validHail FAILED !")

def testcase_invalidQuery1(url):
    resp = post_data(url, "\"1:100:200\"")
    expected = b'"Please make sure you have arguments for: chromosome, min, max, service"'
    if resp ==  expected:
        print("testcase_invalidQuery1 PASSED !")
    else:
        print(resp)
        print("testcase_invalidQuery1 FAILED !")
        
def testcase_invalidQuery2(url):
    resp = post_data(url, "\"1,100,200,NONSERVICE\"")
    expected = b'"Service NONSERVICE is not supported. Please contact support team."'
    if resp ==  expected:
        print("testcase_invalidQuery1 PASSED !")
    else:
        print(resp)
        print("testcase_invalidQuery1 FAILED !")

def main():
    url = "https://qssvxiw6pk.execute-api.us-east-2.amazonaws.com/dev/lambdaservice"
    testcase_validGor(url)
    testcase_validHail(url)
    testcase_invalidQuery1(url)
    testcase_invalidQuery2(url)
if __name__ == '__main__': main()
