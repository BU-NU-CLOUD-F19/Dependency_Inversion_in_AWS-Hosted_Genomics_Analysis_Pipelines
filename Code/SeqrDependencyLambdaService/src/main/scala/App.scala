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
    // handleRequest is the (Java) abstract class that a JVM runtime AWS Lambda must implement
    override def handleRequest(event: String, context: Context): String = {
        // GenomicQuery.fromString takes in a string and tries to parse out a
        // seqrdependencylambdaservice.query.GenomicQuery and GenomicQueryImplementation
        val tryQuery = GenomicQuery.fromString(event)
        tryQuery match {
            // If we get an error, GenomicQuery.fromString wraps it into a string,
            // which we present to the user.
            case Right(errorString:String) => errorString
            // Parse succeeded: we have a GenomicQuery object and a GenomicQueryImplementation object
            case Left((actualQuery,implementation)) => {
                // Pattern match over implementation to get the Hail/Wuxi/etc-specific details
                implementation match {
                    case Wuxi => {
                        // Try to get the AWS ManagedSecret.
                        // This will return either a string (the secret) or an Exception if something
                        // went wrong on the AWS side (for instance, IAM not configured correctly)
                        val tryCredentials = new ManagedSecret("gordb_login","us-east-2").Secret
                        tryCredentials match {
                            case Left(credString: String) => {
                                // gor REST authentication isn't working yet :(
                                // For the NEU class it is fine to just return the gor query generated.

                                // returnString is an informative message to the user
                                var returnString = "GorDB credentials successfully obtained \n"
                                returnString = returnString + "BCH and WuXi have not finished the REST API for this...\n"

                                // GorQuery.fromGenomicQuery takes in a GenomicQuery and spits out a corresponding gor query string
                                val queryString = GorQuery.fromGenomicQuery(actualQuery).queryString
                                returnString = returnString + "Query to be executed: " + queryString + "\n"
                                returnString
                            }
                            case Right(e:Exception) => {
                                // Print the error message to the user
                                "Unhandled exception in accessing Secret Manager for GorDB authentication" + (e.toString())
                            }
                        }
                    }
                    case Hail => {
                        // This testESEndpoint should go in a config file
                        val testESEndpoint = "https://search-test2-hnkwuh5gdzna4fwynnbelmggkq.us-east-2.es.amazonaws.com/_search?"
                        // HailQuery.fromGenomicQuery takes a GenomicQueryObject
                        // and returns the API parameters for an Elasticsearch search request
                        val esQueryString = HailQuery.fromGenomicQuery(actualQuery).queryString
                        val postString = testESEndpoint + esQueryString
                        val content = scala.io.Source.fromURL(postString).mkString
                        content
                    }
                    case TestSecretManager => {
                        // TestSecretManager doesn't do any searches,
                        // it just demonstrates how the AWS ManagedSecret service works with Scala/Java.
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