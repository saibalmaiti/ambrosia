package com.ambrosia.main.menu.repository;

import com.ambrosia.main.menu.entity.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
    ItemCategory findByName(String name);
}
