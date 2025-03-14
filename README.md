# spring-ai-rag-llama

A demonstration project showcasing Retrieval Augmented Generation (RAG) implementation using Spring AI and llama models. This application enables intelligent document querying by combining the power of Large Language Models (LLMs) with local document context.

# Overview

This project demonstrates how to:

Ingest PDF documents into a vector database

Perform semantic searches using Spring AI

Augment LLM responses with relevant document context

Create an API endpoint for document-aware chat interactions

#  Project Requirements :

Java 23

Maven`  ``

Docker Desktop

Ollama (llama model 3.2)

Dependencies: Spring Initializer


#  Dependencies

    	<dependency>
		<groupId>org.springframework.ai</groupId>
		<artifactId>spring-ai-pdf-document-reader</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.ai</groupId>
		<artifactId>spring-ai-pgvector-store-spring-boot-starter</artifactId>
	</dependency>
   	<dependency>
		<groupId>org.postgresql</groupId>
		<artifactId>postgresql</artifactId>
		<scope>runtime</scope>
	</dependency>
        <dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.ai</groupId>
		<artifactId>spring-ai-ollama-spring-boot-starter</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-jpa</artifactId>
	</dependency>

  #  Getting Started
   1. Updared the properties file
      
  	spring.ai.ollama.chat.options.model=llama3.2
	spring.ai.ollama.embedding.enabled=true
	spring.ai.ollama.embedding.model=mxbai-embed-large
	spring.ai.vectorstore.pgvector.initialize-schema=true
	logging.level.org.apache.pdfbox.pdmodel.font.FileSystemFontProvider=Error

	spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
	spring.datasource.username=USER_NAME	
	spring.datasource.password=Password
	spring.datasource.driver-class-name=org.postgresql.Driver

	 PGVector Configuration :
  
	spring.ai.vectorstore.pgvector.table-name=vector_store
	spring.ai.vectorstore.pgvector.column-embedding=embedding
	spring.ai.vectorstore.pgvector.column-metadata=metadata

	Hibernate Configuration :
 
	spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
	spring.jpa.hibernate.ddl-auto=update
	spring.jpa.show-sql=true

 2. Place your PDF documents in the src/main/resources/docs directory

#  Running the Application 
1. Start Docker Desktop

2. Launch the application:

./mvnw spring-boot:run
The application will:

Start a PostgreSQL database with PGVector extension
Initialize the vector store schema
Ingest documents from the configured location
Start a web server on port 8080

#  Key Components
  
