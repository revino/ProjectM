package com.woong.projectmanager.repository;

import com.woong.projectmanager.domain.Contents;
import com.woong.projectmanager.domain.Item;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContentsRepository extends JpaRepository<Contents, Long> {

    @Query("select distinct c from Contents c " +
            "left join fetch c.writer " +
            "left join fetch c.item i " +
            "where i.id = :id ")
    Optional<List<Contents>> findAllByItem(@Param(value = "id") Long id, Sort sort);
}
