package seqrdepdency.testapp

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.{APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent}

class ScalaLambda extends RequestHandler[String, String] {
  override def handleRequest(event: String, context: Context): String = {

    // input is: chr=1,min=100,max=500,service=Wuxi
    val args = event.split(",")
    val map = args.map(value => (value.split("=")(0), value.split("=")(1).toUpperCase())).toMap
    var service = "Invalid"
    if(map.contains("service")) {
      service = map("service")
    }
    if (service == "WUXI") {
        return "Using Wuxi"
    } else if (service == "HAIL") {
        return "Using Hail"
    }
    return service
    }
}