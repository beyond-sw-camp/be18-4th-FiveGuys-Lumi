package com.yuguanzhang.lumi.file.repository;

import com.yuguanzhang.lumi.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByFileId(Long fileId);

    List<File> findByDeletedTrue();
}
