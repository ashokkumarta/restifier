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
 http://localhost:11011/endpoints
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

Used to invoke a backend service. 

Types of invokers supported:

	|Type|Details|
	|---|---|
	|SoapServiceInvoker|To invoke a SOAP service|
	|RestServiceInvoker|To invoke a REST service|
	|SQLQueryServiceInvoker|To execute SQL query|
	|FileServiceInvoker|To process a file template|


### Transformer

Used to transform the structure of the response. 

Types of transformers supported:

	|Type|Details|
	|---|---|
	|JsonTransformer|Applies JOLT transformation template on the response|
	|XmlTransformer|Applies XSLT transformation template on the response|


### Converter

Used to convert the response from one format to the other. 

Types of converter's supported:

	|Type|Details|
	|---|---|
	|Xml2JsonConverter|Converts Xml to Json|
	|Json2XmlConverter|Converts Json to XML|
	|WrapperConverter|Wraps the response within a given template|


### Accumulator

Used to accumulate the response when chaining the processing. 

Types of accumulators supported:

	|Type|Details|
	|---|---|
	|ResponseAccumulator|Accumulates the response, making best effort to preserve the type of the response|
	|StringResponseAccumulator|Accumulates the response as a string|


### Digester

Used to extract parameters from the response when chaining the processing. 

Types of digester's supported:

	|Type|Details|
	|---|---|
	|JsonToParamsDigester|Digests the parameters fron Json reponse|
	|XmlToParamsDigester|Digests the parameters fron Xml reponse|


### Executor

Used to execute more than one service at one go. 

Types of executors supported:

	|Type|Details|
	|---|---|
	|SequentialExecutor|Executes the list of services sequentially|
	|ParallelExecutor|Executes the list of services in parallel|


**Reservation**
> Though this is a generic utility, they have been tested for limited set of use cases. Make sure its tested for your scenarios before applying it for production purpose

