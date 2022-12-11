package com.woong.projectmanager.repository;

import com.woong.projectmanager.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
