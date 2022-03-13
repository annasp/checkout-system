package com.example.checkoutsystem.repository;

import com.example.checkoutsystem.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.code in (:codes)")
    List<Item> findAllByCode(@Param("codes") List<String> scannedItems);

    @Query("SELECT i FROM Item i WHERE i.code = :code")
    Item findByCode(@Param("code")String code);

}
