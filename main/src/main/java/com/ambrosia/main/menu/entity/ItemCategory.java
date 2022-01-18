package com.ambrosia.main.menu.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemCategory {
    @Id
    @SequenceGenerator(
            name = "item_category_sequence",
            sequenceName = "item_category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "item_category_sequence"
    )
    private Long categoryId;
    private String name;

}
