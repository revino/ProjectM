package com.woong.projectmanager.repository;

import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select distinct i from Item i join fetch i.writer join fetch i.channel where i.id = :id ")
    Optional<Item> findByIdWithWriterAndChannel(@Param(value = "id") Long id);

    @Query("select distinct i from Item i left join fetch i.contentsList left join fetch i.writer left join fetch i.channel where i.id = :id ")
    Optional<Item> findByIdWithAll(@Param(value = "id") Long id);

    @Query("select distinct i from Item i left join fetch i.alarmUserList where i.id = :id ")
    Optional<Item> findByIdWithAllAlarmUser(@Param(value = "id") Long id);



}
