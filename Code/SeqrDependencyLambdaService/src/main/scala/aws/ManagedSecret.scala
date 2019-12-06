import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder
import com.amazonaws.services.secretsmanager.model._

class ManagedSecret(secretName: String, region: String) {
    val Secret : String = {
        val client = AWSSecretsManagerClientBuilder.standard().withRegion(region).build()
        val getSecretValueRequest: GetSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName)
        var getSecretValueResult = try {
            client.getSecretValue(getSecretValueRequest)

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
            getSecretValueResult.getSecretString()
        }
        else {
            throw new Exception("Unhandled error in getting secret string from AWS secret request result")
        }



    }
}