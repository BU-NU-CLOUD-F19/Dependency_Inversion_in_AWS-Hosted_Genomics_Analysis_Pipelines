package example

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}

class ScalaLambda extends RequestHandler[String, String] {
    override def handleRequest(event: String, context: Context): String = {

        // input format: chr,min,max,service
        // input is:     "1,100,500,Wuxi"  or  "1,100,500,Hail"
        val args = event.split(",")

        if (args(3).equalsIgnoreCase("WUXI")) {
            // query format: gor -p chr1:111371-563625
            return "gor -p chr" + args(0) + ":" + args(1) + "-" + args(2) + " #dbsnp#"
        } else if (args(3).equalsIgnoreCase("HAIL")) {
          /*GET test1/_search
            {
              "query": {
                "bool" : {
                  "must" : [
                    {"match" : {"locus.contig" : "1"}},
                    {"range" :
                      {
                        "locus.position" : {
                        "gte" : 900000,
                        "lt" : 1000000
                        }
                      }
                    }
                  ]
                }
              }
            }*/
          //return "GET test1/_search\n{\n  \"query\": {\n    \"bool\" : {\n      \"must\" : [\n        {\"match\" : {\"locus.contig\" : \"" + args(0) + "\"}},\n        {\"range\" : \n          {\n            \"locus.position\" : {\n              \"gte\" : " + args(1) + ",\n              \"lt\" : " + args(2) + "\n            }\n          }\n        }\n      ]\n    }\n  }\n}"

          //return "{\"query\" : {\"bool\" : {\"must\" : [ {\"match\" : {\"locus.contig\" : \"" + args(0) + "\"}}, {\"range\" :  {\"locus.position\" : {\"gte\" : " + args(1) + ", \"lt\" : " + args(2) + "}}}]}}}"
          val url = "curl -X GET \"https://search-test2-hnkwuh5gdzna4fwynnbelmggkq.us-east-2.es.amazonaws.com/_search\" -H 'Content-Type: application/json' -d'\n{\n  \"query\": {\n    \"bool\" : {\n      \"must\" : [\n        {\"match\" : {\"locus.contig\" : " + args(0) + "}},\n        {\"range\" : \n          {\n            \"locus.position\" : {\n              \"gte\" : " + args(1) + ",\n              \"lt\" : " + args(2) + "\n            }\n          }\n        }\n      ]\n    }\n  }\n}'"
          return url
        }
        return "Error Service"
    }
}