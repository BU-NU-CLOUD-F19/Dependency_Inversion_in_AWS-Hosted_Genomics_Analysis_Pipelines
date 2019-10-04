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
    <tr>
        <td align="center"></td>
        <td align="center">Naveen Muralidhar</td>
    </tr>
</table>


## 1. Vision and Goals Of The Project:

Dependency Inversion in AWS-Hosted Genomics Analysis Pipelines will be the project to improve current computational analysis application of raw genetic data at Boston Childrens’ Hospital for end-users of the Research Team. The high-level goals of this project include:
-   Extend the current system to be compatible with another existing genomics analysis platform, WuXi NextCODE. 

- Providing separation of the data analysis and preparation services which are often interlinked so that in the future, support team will be able to mix-and-match these services more flexibly, and in particular use any analysis service with one specific preparation service.
    
- Refactoring web application Seqr by building some interface or parameterized service class with calls to the WuXi NextCODE REST API, instead of making direct calls to a specific pipeline.
    
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

- Refactor web application Seqr analysis service into a parameterized service class, instead of making direct calls to a specific pipeline. A particular focus of this work will be error-handling, since Seqr will not be able to directly monitor the state of Hail or WuXi.

- Develop SeqrBackendService to parse requests from Seqr and converts them to architecture-specific commands.

- Link to WuXi NextCODE service can be created once we have the SeqrBackendService connected with Hail.

- Reduce the relative footprint of the current Seqr AWS deployment. Currently it is on AWS EMR due to the burden of Hail. But if we aren’t deploying worker nodes to handle the primary pipeline, then we should deploy Seqr on a cheaper AWS service.

- Error-handling, since Seqr will not be able to directly monitor the state of WuXi.

## 4. Solution Concept

### Current Architecture
![CurrentArchitecture](https://github.com/BU-NU-CLOUD-F19/Dependency_Inversion_in_AWS-Hosted_Genomics_Analysis_Pipelines/blob/master/Documents/Images/CurrentArchitecture1.png)![CurrentArchitecture](https://github.com/BU-NU-CLOUD-F19/Dependency_Inversion_in_AWS-Hosted_Genomics_Analysis_Pipelines/blob/master/Documents/Images/CurrentArchitecture2.png)

In the current system, web application Seqr, communicates with ElasticSearch index which stores genome mutant information of patients. Seqr searches against ElasticSearch with a patient’s ID, and if ElasticSearch already has this record, it will return with the corresponding data; otherwise, it tells Hail, which is a data analysis tool, to generate information of this patient then passes to ElasticSearch, finally to Seqr user interface.

### Desired Architecture
![DesiredArchitecture](https://github.com/BU-NU-CLOUD-F19/Dependency_Inversion_in_AWS-Hosted_Genomics_Analysis_Pipelines/blob/master/Documents/Images/DesiredArchitecture.png)

The proposed architecture achieves the following things:

- Introduce an intermediate service class to relay the requests from the Seqr service to the Hail data analyze pipeline.
- When the service class(SeqrBackendService) gets the request from web application Seqr, it would parse the requests and convert them into architecture specific commands.  
- Once the requests have been modified, they can be sent to the preparation service of choice. 
- The preparation service used to get the response must be hidden from the Seqr the web application.

Below is a description of the system components that are building blocks of the architectural design:

- seqr - web application that manages analyzed genetic data - makes it easier to search for specific mutations and diseases, manage patients within the same disease group. It consists of javascript + react.js on the client-side, python + django on the server-side. 
- hail -  Spark application that analyzes genetic data - discovers mutations, associates diseases to mutations. It generates very large documents called "variant effect prediction" files, which are about 250MB per patient, and are loaded into Elasticsearch to make it easier to search for specific variants.
- postgres - SQL database used by seqr and phenotips to store project metadata and user-generated content such as variant notes, etc.
- phenotips - 3rd-party web-based form for entering structured phenotype data.
- matchbox - a tool for connecting with the Match Maker Exchange.
- nginx - http server used as the main gateway between seqr and the internet.
- pipeline-runner - container for running hail pipelines to annotate and load new datasets.
- redis - in-memory cache used to speed up request handling.
- elasticsearch - NoSQL database used to store variant callsets.
- kibana - dashboard and visual interface for elasticsearch.
- mongo - legacy NoSQL database originally used for variant callsets and still used now to store some reference data and logs.


## 5. Acceptance criteria

Minimum acceptance criteria is:

- At the completion of this project, the current Hail-Seqr software can be run normally.
- Implement SeqrBackendService in Scala running on AWS Lambda, which can consume abstract genomic queries and wrap them into GOR or Hail queries and call Hail or Wuxi NextCODE appropriately.
- Error should be handled cleanly and with proper logging.
- The SeqrBackendService is written and implemented in a way that allows us to choose and easily add additional backend implementations beyond Hail and Wuxi NextCODE.
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

    Get local workspace and environment setup ready - Already done and seqr can run locally

Release #2 (due by Week 7):

    Work on AWS Lambda:
    - Explore and learn AWS Lambda and AWS Cloudformation
    - Try building a “hello world” Scala app on AWS Lambda and try using Cloudformation templates.
    - Explore and learn current codebase, interfaces and endpoints

Release #3 (due by Week 9):

    Implementation of SeqrBackendService:
    - Explore and learn WuXi interfaces and endpoints APIs.
    - Start development of SeqrBackendService in Scala on AWS Lambda
    - Release #3 targets partial progress of SeqrBackendService

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