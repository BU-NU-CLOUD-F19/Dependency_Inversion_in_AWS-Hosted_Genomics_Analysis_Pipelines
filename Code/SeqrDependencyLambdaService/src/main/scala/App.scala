package seqrdependencylambdaservice

// import akka.actor.{ Actor, ActorRef, Props }
// import akka.io.{ IO, Tcp }
// import akka.util.ByteString
import java.net.InetSocketAddress
//import akka.io.{ IO, Tcp }

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
//import main.scala.example.ExtractBinary
import play.api.libs.json.Json
//import sys.process._

import seqrdependencylambdaservice.query._
import seqrdependencylambdaservice.wuxi._
import seqrdependencylambdaservice.hail._
import seqrdependencylambdaservice.aws.ManagedSecret

class SeqrDependencyLambdaService extends RequestHandler[String,String] {
    override def handleRequest(event: String, context: Context): String = {
        val tryQuery = GenomicQuery.fromString(event)
        tryQuery match {
            case Right(errorString:String) => errorString
            case Left((actualQuery,implementation)) => {
                implementation match {
                    case Wuxi => {
                        val tryCredentials = new ManagedSecret("gordb_login","us-east-2").Secret
                        tryCredentials match {
                            case Left(credString: String) => {
                                var returnString = "GorDB credentials successfully obtained \n"
                                returnString = returnString + "BCH and WuXi have not finished the REST API for this...\n"
                                val queryString = GorQuery.fromGenomicQuery(actualQuery).queryString
                                returnString = returnString + "Query to be executed: " + queryString + "\n"
                                returnString
                            }
                            case Right(e:Exception) => {
                                "Unhandled exception in accessing Secret Manager for GorDB authentication" + (e.toString())
                            }
                        }
                    }
                    case Hail => {
                        val testESEndpoint = "https://search-test2-hnkwuh5gdzna4fwynnbelmggkq.us-east-2.es.amazonaws.com/_search?"
                        val postString = testESEndpoint + HailQuery.fromGenomicQuery(actualQuery)
                        val content = scala.io.Source.fromURL(postString).mkString
                        content
                    }
                    case TestSecretManager => {
                        val trySecret = new ManagedSecret("TestSecret","us-east-2").Secret
                        trySecret match {
                            case Right(e:Exception) => {
                                "Unhandled exception in accessing Secret Manager for TestSecretManager" + (e.toString())
                            }
                            case Left(s:String) => {
                                "Successfully obtained secret for TestSecretManager: " + s
                            }
                        }
                    }
                }
            }
        }
    }
}