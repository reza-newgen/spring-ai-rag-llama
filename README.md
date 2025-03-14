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
  
