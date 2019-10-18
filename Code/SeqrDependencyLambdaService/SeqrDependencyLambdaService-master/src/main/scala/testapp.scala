package seqrdepdency.testapp

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.{APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent}

class ScalaLambda extends RequestHandler[Int,String] {
  override def handleRequest(event: Int, context: Context): String = {
    return "Hello World"
    }
}