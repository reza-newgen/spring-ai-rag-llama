package com.miu.compro.springai.repository;

import com.miu.compro.springai.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

}
