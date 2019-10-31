package example

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}


class ScalaLambda extends RequestHandler[String, String] {
  override def handleRequest(event: String, context: Context): String = {

    // input is: chr=1,min=100,max=500,service=Wuxi
    val args = event.split(",")

    if (args(2).equalsIgnoreCase("WUXI")) {
        return "Using Wuxi"
    } else if (args(2).equalsIgnoreCase("HAIL")) {
        val content = scala.io.Source.fromURL("https://search-test2-hnkwuh5gdzna4fwynnbelmggkq.us-east-2.es.amazonaws.com/_search").mkString
        return content
    }
    return "Error Service"
    }
}