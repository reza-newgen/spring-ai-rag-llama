package com.miu.compro.springai.service;

import com.miu.compro.springai.entity.File;
import com.miu.compro.springai.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {
    private final Logger log = LoggerFactory.getLogger(FileService.class);

    private final FileRepository fileRepository;


    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    public File getFileById(Long id) {
        return fileRepository.findById(id).orElse(null);
    }

    public File saveFile(File file) {
        File savedFile = fileRepository.save(file);
        log.info("File saved: " + savedFile);
        return savedFile;
    }

    public File editFile(Long id,File file) {
        return fileRepository.findById(id)
                .map(f -> {
                    f.setFileName(file.getFileName());
                    log.info("File edited: " + f);
                    return fileRepository.save(f);
                })
                .orElse(null);
    }


    public List<String> filesName() {
        return fileRepository.findAll().stream()
                .map(File::getFileName)
                .collect(Collectors.toList());
    }

}
