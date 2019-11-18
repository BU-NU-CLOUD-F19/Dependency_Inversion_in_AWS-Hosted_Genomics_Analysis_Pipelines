package example


import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder
import com.amazonaws.services.secretsmanager.model._
import main.scala.example.ExtractBinary

import sys.process._

class ScalaLambda extends RequestHandler[String, String] {
  override def handleRequest(event: String, context: Context): String = {

    // AWS Secrets Manager

    // input format: chr,min,max,service
    // input is:     "1,100,500,Wuxi"  or  "1,100,500,Hail"
    val args = event.split(",")

    if (args(3).equalsIgnoreCase("WUXI")) {

      val secretName: String = "TestSecret"
      val region: String = "us-east-2"
      var secret: String = null

      val client = AWSSecretsManagerClientBuilder.standard().withRegion(region).build()

      val getSecretValueRequest: GetSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName)

      var getSecretValueResult: GetSecretValueResult = null

      try {
        getSecretValueResult = client.getSecretValue(getSecretValueRequest)

      } catch {
        case e: InternalServiceErrorException =>
          throw e
        case e: DecryptionFailureException =>
          throw e
        case e: InvalidParameterException =>
          throw e
        case e: InvalidRequestException =>
          throw e
        case e: ResourceNotFoundException =>
          throw e
      }

      if (getSecretValueResult.getSecretString() != null) {
        secret = getSecretValueResult.getSecretString()
      }
      else {
        secret = "Not working"
      }

      //Copy the binary bundled in the jar to /tmp in the local filesystem
      ExtractBinary.copyToLocalFS("sample.exe", "/tmp/sample.exe")
      println("Copied binary to jar execution directory !")

      //chmod the binary with RWX permissions
      val result0 = "chmod 777 /tmp/sample.exe"
      val output0 = result0.!!

      //invoke the binary and capture STDOUT
      val result1 = "/tmp/sample.exe"
      val output1 = result1.!!

      //get some information about the ELF binary
      val result2 = "file /tmp/sample.exe"
      val output2 = result2.!!

      val output = output1 + "\n *RUNNING* file /tmp/sample.exe \n" + output2
      println("Output was :"+output)

      return output
      //return "gor -p chr" + args(0) + ":" + args(1) + "-" + args(2) + " #dbsnp#"

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