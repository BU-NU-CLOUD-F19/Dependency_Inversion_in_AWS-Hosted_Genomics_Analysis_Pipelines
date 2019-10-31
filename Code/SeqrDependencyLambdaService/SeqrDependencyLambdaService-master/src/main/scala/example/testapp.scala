package example

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}

class ScalaLambda extends RequestHandler[String, String] {
    override def handleRequest(event: String, context: Context): String = {

        // input format: chr,min,max,service
        // input is:     "1,100,500,Wuxi"
        val args = event.split(",")

        if (args(3).equalsIgnoreCase("WUXI")) {
            // query format: gor -p chr1:111371-563625
            return "gor -p chr" + args(0) + ":" + args(1) + "-" + args(2) + " #dbsnp#"
        } else if (args(3).equalsIgnoreCase("HAIL")) {
            return "Using Hail"
        }

        return "Error Service"
    }
}