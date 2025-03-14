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
1. PDF files convert to RAG (I put some check that same file does not convert to RAG)
   
@RestController
@RequestMapping("/api/v1/ragload")
public class RagController {

    private final RagService ragService;


    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/files")
    public ResponseEntity<?> loadAllFiles() throws IOException {
        ragService.loadRag();
        return ResponseEntity.ok("Loaded Rags successfully");
    }
}

2. ChatController

   @RestController
public class ChatController {
    Logger LOGGER = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/")
    public String chat(@RequestParam String message) {
        return chatService.llamaChat(message);
    }


    @GetMapping("/chat-labour-market")
    public String questionandAnswer(@RequestParam(value = "question",
            defaultValue = "List all the Articles in the USA Labour Market")
                                    String question) {
        LOGGER.info("question is: {}",question);
        return chatService.ragDocumentChat(question);
    }


}

chatService:

@Service
public class ChatService {
    Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

    private ChatClient chatClient;
    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    public ChatService(ChatClient.Builder  builder, ChatModel chatModel, VectorStore vectorStore) {
        this.chatClient = builder.defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory())).build();
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }


    private String prompt = """
            Your task is to answer the questions about USA Labour Market. Use the information from the DOCUMENTS
            section to provide accurate answers. If unsure or if the answer isn't found in the DOCUMENTS section, 
            simply state that you don't know the answer.
                        
            QUESTION:
            {input}
                        
            DOCUMENTS:
            {documents}
                        
            """;


    public String llamaChat(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

public String ragDocumentChat(String question) {
    PromptTemplate template
            = new PromptTemplate(prompt);
    Map<String, Object> promptsParameters = new HashMap<>();
    promptsParameters.put("input", question);
    LOGGER.info("Answer is searching for {}", question);
    promptsParameters.put("documents", findSimilarData(question));

    return chatModel
            .call(template.create(promptsParameters))
            .getResult()
            .getOutput()
            .getText();
}

    private String findSimilarData(String question) {

        SearchRequest searchRequest = SearchRequest.builder()
                .query(question) // Correct way to set the query
                .topK(5)         // Setting the number of similar results to fetch
                .build();


        List<Document> documents = vectorStore.similaritySearch(searchRequest);

        return documents
                .stream()
                .map(document -> document.getFormattedContent().toString())
                .collect(Collectors.joining());

    }
}

#  Making Requests
1. Query to LLM
http://localhost:8080/chat-labour-market
2. simple chating
   http://localhost:8080/
3. convert pdf files to RAG
http://localhost:8080/api/v1/ragload/files

#  Architecture Highlights
1. Document Processing: Uses Spring AI's PDF document reader to parse documents into manageable chunks
2. Vector Storage: Utilizes PGVector for efficient similarity searches
3. Context Retrieval: Automatically retrieves relevant document segments based on user queries
4. Response Generation: Combines document context with Llama 3.2 Model capabilities for informed responses




  
