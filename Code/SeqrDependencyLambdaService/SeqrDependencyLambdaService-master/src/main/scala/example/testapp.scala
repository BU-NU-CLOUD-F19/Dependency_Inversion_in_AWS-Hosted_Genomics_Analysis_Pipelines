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

          val url = "https://search-test2-hnkwuh5gdzna4fwynnbelmggkq.us-east-2.es.amazonaws.com/_search?q=locus.contig:" +
                    args(0) + "%20AND%20locus.position:[" + args(1) + "+TO+" + args(2) + "]&pretty"
          val content = scala.io.Source.fromURL(url).mkString
          return content
        }
        return "Error Service"
    }
}