package com.woong.projectmanager.repository;

import com.woong.projectmanager.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlarmUserItemRepository  extends JpaRepository<AlarmUserItem, Long> {
    Optional<AlarmUserItem> findByUserAndItem (Users user, Item item);
}
