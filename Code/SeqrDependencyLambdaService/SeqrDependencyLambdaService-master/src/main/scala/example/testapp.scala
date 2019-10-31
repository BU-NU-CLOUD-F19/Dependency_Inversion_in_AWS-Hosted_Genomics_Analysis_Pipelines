package example

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}

class ScalaLambda extends RequestHandler[String, String] {
  override def handleRequest(event: String, context: Context): String = {

    // input is: chr=1,min=100,max=500,service=Wuxi
    val args = event.split(",")


    if (args(2).equalsIgnoreCase("WUXI")) {
        return "gor -p chr1:" + args(0) + "-" + args(1)
    } else if (args(2).equalsIgnoreCase("HAIL")) {
        return "Using Hail"
    }
    return "Error Service"
    }
}