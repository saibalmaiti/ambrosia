package com.ambrosia.main.menu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddItemRequestDTO {
    private String name;
    private String description;
    private Double price;
    private Boolean isVeg;
    private String category;
}
