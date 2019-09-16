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


## 6. Release Planning:



