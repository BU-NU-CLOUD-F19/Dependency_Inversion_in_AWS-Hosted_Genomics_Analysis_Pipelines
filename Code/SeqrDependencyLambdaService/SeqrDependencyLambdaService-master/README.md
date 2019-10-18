# SeqrDependencyLambdaService

This is really a prototype, and for now just a Scala/AWS Lambda "Hello World."

## Requirements

- Java 8

- Scala 2.13 [I thiink versions > 2.11 should work fine]

## Installation/deployment

- `$ git clone`
- `$ cd SeqrDependencyLambdaService`
- `sbt compile`
- `sbt package`
- Upload the JAR in the `target\sala-2.13` folder to AWS Lambda and deploy the function

## Testing

- Currently the Hello World example takes in an integer and spits out "Hello World"

- If you have your AWS Access ID and Secret Access Key configured on your system (for instance, in ~/.aws/credentials), you can test the Lambda function with the following from bash:

```
$ aws lambda invoke --function-name <your function name> --payload 1 output.txt && cat output.txt
```

- You can also test in the AWS Lambda web console. When configuring test data, do not format it as a JSON - my test is just a document with only the character `1`.