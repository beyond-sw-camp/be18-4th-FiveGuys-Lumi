package com.yuguanzhang.lumi.material.repository;

import com.yuguanzhang.lumi.material.entity.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Page<Material> findByChannelUser_Channel_ChannelId(Long channelId, Pageable pageable);
}
