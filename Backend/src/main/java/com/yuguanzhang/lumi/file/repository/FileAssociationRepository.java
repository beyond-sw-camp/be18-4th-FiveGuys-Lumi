package com.yuguanzhang.lumi.file.repository;

import com.yuguanzhang.lumi.file.entity.FileAssociation;
import com.yuguanzhang.lumi.file.enums.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileAssociationRepository extends JpaRepository<FileAssociation, Long> {
    //id 와 type 으로 select
    List<FileAssociation> findByEntityIdAndEntityType(Long entityId, EntityType entityType);

    void deleteByEntityIdAndEntityType(Long materialId, EntityType entityType);
}
