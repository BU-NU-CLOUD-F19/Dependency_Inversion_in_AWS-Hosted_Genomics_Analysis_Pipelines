# Dependency Inversion in AWS-Hosted Genomics Analysis Pipelines

<table>
    <tr>
    <th>Mentor</th>
    <th>Team</th>
    </tr>
    <tr>
        <td align="center">Nicholas LeCompte</td>
        <td align="center">Jay Doshi</td>
    </tr>
    <tr >
        <td align="center" ></td>
        <td align="center">Joyce Zhao</td>
    </tr>
    <tr>
        <td align="center"></td>
        <td align="center">Rong Su</td>
    </tr>
</table>


## 1. Vision and Goals Of The Project:

Dependency Inversion in AWS-Hosted Genomics Analysis Pipelines will be the project to improve current computational analysis application of raw genetic data at Boston Childrens’ Hospital for end-users of the Research Team. The high-level goals of this project include:

-   Extend the current system efficiently and be compatible with another existing genomics platform WuXi NextCODE.
    
-   Providing separation of the analysis and preparation services which are often interlinked so that in the future, support team will be able to mix-and-match these services more flexibly, and in particular use any analysis service with one specific preparation service.
    
-   Refactoring Seqr by building some interface or parameterized service class with calls to the WuXi NextCODE REST API, instead of making direct calls to a specific pipeline.
    
-   Reducing the relative footprint of the current Seqr AWS deployment.

## 2. Users/Personas Of The Project:

This project will be used as part of the computational analysis application of raw genetic data by researchers. This project won’t be available in details to the researchers. Instead, it will only serve as an improved connection between the preparation and analysis services of the application and be available to Research Computing - Genomics Team for better future improvements on flexible mix-and-match the preparation service and analysis service.

As a end-user of the application, I should be able to:

-   Get data correctly.
    
-   Use this application the same way as before.

As a developer, I should be able to:

-   Add/Remove a genomics service flexibly.
    
-   Maintain the extended feature easily.


## 3. Scope and Features Of The Project:

Replace the existing preparation service Hail with API calls to a new service WuXi NextCODE. This work would involve a few steps:

-   Refactor the front end Seqr analysis service into a parameterized service class, instead of making direct calls to a specific pipeline. A particular focus of this work will be error-handling, since Seqr will not be able to directly monitor the state of Hail or WuXi.
    
-   Develop SeqrBackendService to parse requests from Seqr and converts them to architecture-specific commands.
    
-   Link to WuXi NextCODE service can be created once we have the SeqrBackendService connected with Hail.
    
-   Reduce the relative footprint of the current Seqr AWS deployment. Currently it is on AWS EMR due to the burden of Hail. But if we aren’t deploying worker nodes to handle the primary pipeline, then we should deploy Seqr on a cheaper AWS service.
    
-   Error-handling, since Seqr will not be able to directly monitor the state of WuXi.

## 4. Solution Concept

### Current Architecture
![CurrentArchitecture](https://github.com/BU-NU-CLOUD-F19/Dependency_Inversion_in_AWS-Hosted_Genomics_Analysis_Pipelines/blob/master/Documents/Images/CurrentArchitecture.png)In the current system, the analysis service Seqr, communicates directly with the Hail preparation service using Rest API calls. This leads to inflexibility in transitioning towards a newer preparation service.

### Desired Architecture
![DesiredArchitecture](https://github.com/BU-NU-CLOUD-F19/Dependency_Inversion_in_AWS-Hosted_Genomics_Analysis_Pipelines/blob/master/Documents/Images/DesiredArchitecture1.png)![DesiredArchitecture](https://github.com/BU-NU-CLOUD-F19/Dependency_Inversion_in_AWS-Hosted_Genomics_Analysis_Pipelines/blob/master/Documents/Images/DesiredArchitecture2.png)The proposed architecture achieves the following things:

-   Introduce an intermediate service class to relay the requests from the Seqr service to the Hail backend.
    
-   When the service class(SeqrBackendService) gets the request from the Seqr front end service, it would parse the requests and convert them into architecture specific commands.
    
-   Once the requests have been modified, they can be sent to the preparation service of choice.
    
-   The preparation service used to get the response must be hidden from the Seqr front end.


## 5. Acceptance criteria

Minimum acceptance criteria is:
- At the completion of this project, the current Hail-Seqr software can be run normally.
- A small subset of Seqr genomic analyses are implemented in GOR, with results that agree with the Hail implementation. BCH will provide the GOR code needed.
- The Seqr UI has an option to choose Hail or GOR. If a user tries to run a Seqr function on GOR that does not have an implementation, the error should be handled cleanly and with proper logging.
- The SeqrBackendService is written and implemented in a way that allows us to easily add additional backend implementations beyond Hail and GOR.
- Hail and its Spark cluster should generally be kept running on as small and as few nodes as possible.
- BCH security policies are respected with good IAM practices.

Stretch goals are:
- Keep Hail/Spark at a “low burn” (a few relatively small nodes) unless it’s really needed.
- More GOR queries supported or fully replacing Hail with GOR
- Start to define a way to compare Hail and GOR on certain Seqr command



## 6. Release Planning:

[Detailed user stories and plans are on the Github projects board](https://github.com/BU-NU-CLOUD-F19/Dependency_Inversion_in_AWS-Hosted_Genomics_Analysis_Pipelines/projects/1)


Feature: collection of user stories


    Feature 0. The team is on-board with the project.
    Feature 1. AWS plan that preserves the existing Hail-Seqr infrastructure (basically making a final decision on what services/containerization to use)
    Feature 2. SeqrBackendService implementation on AWS Lambda
    Feature 3. Partial translation of Seqr requests to GOR queries for the SeqrBackendService
    Feature 4. Changes to Seqr UI to accommodate 2 and 3
    Feature 5. Thorough regression testing.

Release #1 (due by Week 5):

    Feature 1: AWS plan that preserves the existing Hail-Seqr infrastructure
Release #2 (due by Week 7):

    Feature 2: SeqrBackendService implementation on AWS Lambda
Release #3 (due by Week 9):

    Feature 3: Partial translation of Seqr requests to GOR
Release #4 (due by Week 11):

    Feature 4: Seqr UI changes
Release #5 (due by Week 13):

    Feature 5: Regression testing


## 7. Open Questions & Risks:
More detail of deployment of the project.

    Since the end goal of this project is to isolate different part of the application.
    And right now each part is running on a same server, we need to have information about how the system got deployed.
    Also to deploy each part onto different AWS server needs more attention to sercuity which we haven't touched it right now.

How can we make use of AWS considering the data protection policy which prevents us from getting real data? How can we create testing data?
    
    All the data right now in Elasticsearch is credential regarding the patients' genome information.
    Also the AWS account we are about to use is a sandbox account (haven't provided yet) which we shouldn't get access to any of the real data.
    This will potentially cause some problems: even we have implemented the middleware, it's hard to test it well.
    We will need more discussion for a more clearer test goal.

[first demo 9/27](https://docs.google.com/presentation/d/1kbRdJbfWmAZOpKtKjGDg_-BeuQq4aN98ZTA1bTgy2UM/mobilepresent?slide=id.p)