package com.ambrosia.main.menu.service;

import com.ambrosia.main.menu.entity.ItemCategory;
import com.ambrosia.main.menu.repository.ItemCategoryRepository;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemCategoryService {
    ItemCategoryRepository itemCategoryRepository;
    @Autowired
    public ItemCategoryService(ItemCategoryRepository repository) {
        this.itemCategoryRepository = repository;
    }

    public ItemCategory createNewCategory(String categoryName) {
        ItemCategory category = ItemCategory.builder().name(categoryName).build();
        return itemCategoryRepository.save(category);
    }

    public List<ItemCategory> getAllItemCategories() {
        return itemCategoryRepository.findAll();
    }

    public ItemCategory renameCategory(String newName, String oldName) {
        ItemCategory category = itemCategoryRepository.findByName(oldName);
        category.setName(newName);

        return itemCategoryRepository.save(category);
    }
}
