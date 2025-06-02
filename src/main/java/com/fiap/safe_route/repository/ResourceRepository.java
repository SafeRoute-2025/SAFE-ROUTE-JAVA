package com.fiap.safe_route.repository;

import com.fiap.safe_route.model.Resource;
import com.fiap.safe_route.model.SafePlace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends JpaRepository <Resource, Long> {
    Page<Resource> findBySafePlaceId(Long safePlaceId, Pageable pageable);

}
