package com.iot.backend.repository;

import com.iot.backend.model.RfidTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RfidTagRepository extends JpaRepository<RfidTag, Integer> {

    List<RfidTag> findByUserId(Integer userId);

    Optional<RfidTag> findByTagUid(String tagUid);

    Long countByUserId(Integer userId);
}