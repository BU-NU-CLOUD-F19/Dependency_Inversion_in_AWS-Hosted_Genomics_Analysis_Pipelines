package seqrdependencylambdaservice.aws

import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder
import com.amazonaws.services.secretsmanager.model._

class ManagedSecret(secretName: String, region: String) {
    val Secret : Either[String,Exception] = {
        val client = AWSSecretsManagerClientBuilder.standard().withRegion(region).build()
        val getSecretValueRequest: GetSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName)
         try {
            var getSecretValueResult = client.getSecretValue(getSecretValueRequest)
            val gotString : String = getSecretValueResult.getSecretString()
            Left(gotString)

        } catch {
            case e: Exception => Right(e)
        }
    }
}