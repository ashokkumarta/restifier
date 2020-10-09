![](restifier-logo.png)

A project to wrap any backend service and expose it through a simple REST end point. 
 
## Getting started
To get the **Restifier** tool up and running, fork, clone or copy this repository.
Do a maven build and run it as a spring boot application

```
 mvn clean install
 mvn spring-boot:run
```

**Restifier** by default starts on the port 11011 and can be accessed through the below links to view and test the sample configurations

```
 http://localhost:11011/
```


## Dependencies
```
JDK 1.8
Maven 3
```

## Features

This framework supports the following features, which can be chained through a configuration to invoke a service, merge, transform & convert the response to the desired format 

* Invoker
* Transformer
* Converter
* Accumulator
* Digester
* Executor


### Invoker 

Invokers are used to invoke any backend service. Typically they form the 1st step in the pipeline of the configuration. Output from the Invoker can be chained through to convert and reformat it before sendind the final response back 

Types of invokers currently supported are:

	| Type | Details |
	| --- | --- |
	| SoapServiceInvoker | To invoke a SOAP service |
	| RestServiceInvoker | To invoke a REST service |
	| SQLQueryServiceInvoker | To execute SQL query |
	| FileServiceInvoker | To process a file template |


### Transformer

Transformers are used to transform the structure of the response. 

Types of transformers currently supported are:

	|Type|Details|
	|---|---|
	|JsonTransformer|Applies JOLT transformation template on the response|
	|XmlTransformer|Applies XSLT transformation template on the response|


### Converter

convertors are used to convert the response from one format to the other, like XML to JSON,.. 

Types of converter's currently supported are:

	|Type|Details|
	|---|---|
	|Xml2JsonConverter|Converts Xml to Json|
	|Json2XmlConverter|Converts Json to XML|
	|WrapperConverter|Wraps the response within a given template|


### Accumulator

Accumulators are used to accumulate the response when chaining the processing. When more then one service needs to be invoked and multiple results are needed for the processing chain and/or to send the final response, accumulators can be used to hold them all and use it for further processing   

Types of accumulators currently supported are:

	|Type|Details|
	|---|---|
	|ResponseAccumulator|Accumulates the response, making best effort to preserve the type of the response|
	|StringResponseAccumulator|Accumulates the response as a string|


### Digester

Digestor extracts parameters from the response when chaining the processing. Used when output from one service is needed as input to the next service in the invocation chain 

Types of digester's currently supported are:

	|Type|Details|
	|---|---|
	|JsonToParamsDigester|Digests the parameters fron Json reponse|
	|XmlToParamsDigester|Digests the parameters fron Xml reponse|


### Executor

Executors are used to execute more than one service at one go. Typically used when more then one service needs to be executed and result of all needs to be sent back as response 

Types of executors currently supported are:

	|Type|Details|
	|---|---|
	|SequentialExecutor|Executes the list of services sequentially|
	|ParallelExecutor|Executes the list of services in parallel|


## Sample configurations


**Reservation**
> Though this is a generic utility, they have been tested for limited set of use cases. Make sure its tested for your scenarios before applying it for production purpose

