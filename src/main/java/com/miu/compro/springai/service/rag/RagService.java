package com.miu.compro.springai.service.rag;

import com.miu.compro.springai.entity.File;
import com.miu.compro.springai.repository.FileRepository;
import com.miu.compro.springai.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    Logger logger = LoggerFactory.getLogger(RagService.class);

    private final VectorStore vectorStore;
    private final FileService fileService;
    private final ResourceLoader resourceLoader;
    private final ResourcePatternResolver resourcePatternResolver;

    @Value("classpath:/docs/")
    private String fileDirectory;

    @Value("classpath:/docs/*")
    private String filesPath;


    public RagService(VectorStore vectorStore, FileService fileService, ResourceLoader resourceLoader,
                      ResourcePatternResolver resourcePatternResolver) {
        this.vectorStore = vectorStore;
        this.fileService = fileService;
        this.resourceLoader = resourceLoader;
        this.resourcePatternResolver = resourcePatternResolver;
    }

    public void loadRag()  {
        try {
            List<String> dirfileList = getAllFilesFromResources(filesPath);
            List<String> fileList = fileService.filesName();
            dirfileList.forEach(fileName ->  {
                if (!fileList.contains(fileName.trim().toLowerCase())) {
                    logger.info( "Start the processing...{}",fileName);
                    fileToRag(fileName);
                    File file = new File();
                    file.setFileName(fileName.trim().toLowerCase());
                    fileService.saveFile(file);
                    logger.info("Saved file: {} ", fileName);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void fileToRag(String fileName) {
        try {

            PdfDocumentReaderConfig config
                    = PdfDocumentReaderConfig.builder()
                    .withPagesPerDocument(1)
                    .build();
            Resource fileResource = resourceLoader.getResource(fileDirectory+fileName);
            PagePdfDocumentReader reader
                    = new PagePdfDocumentReader(fileResource, config);

            var textSplitter = new TokenTextSplitter();
            vectorStore.accept(textSplitter.apply(reader.get()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info("File covert to RAG error");
        }
        logger.info( "Finish the processing...{}",fileName);
    }


    public List<String> getAllFilesFromResources(String locationPattern) throws IOException {
        Resource[] resources = resourcePatternResolver.getResources(locationPattern);
        return Arrays.stream(resources)
                .map(Resource::getFilename)
                .collect(Collectors.toList());
    }
}
