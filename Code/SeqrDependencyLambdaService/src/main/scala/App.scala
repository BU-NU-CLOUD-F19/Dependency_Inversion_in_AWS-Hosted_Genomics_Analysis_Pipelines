package SeqrDependencyLambdaService

// import akka.actor.{ Actor, ActorRef, Props }
// import akka.io.{ IO, Tcp }
// import akka.util.ByteString
import java.net.InetSocketAddress
//import akka.io.{ IO, Tcp }

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
//import main.scala.example.ExtractBinary
//import play.api.libs.json.Json
//import sys.process._

class SeqrDependencyLambdaService extends RequestHandler[String,String] {
    override def handleRequest(event: String, context: Context): String = {
        ""
    }
}