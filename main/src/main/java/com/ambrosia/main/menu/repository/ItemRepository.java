package com.ambrosia.main.menu.repository;

import com.ambrosia.main.menu.entity.Item;
import com.ambrosia.main.menu.entity.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByCategory(ItemCategory category);
}
