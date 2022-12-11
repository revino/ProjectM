package com.woong.projectmanager.repository;

import com.woong.projectmanager.domain.Contents;
import com.woong.projectmanager.domain.Item;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContentsRepository extends JpaRepository<Contents, Long> {

    Optional<List<Contents>> findAllByItem(Item item, Sort sort);
}
