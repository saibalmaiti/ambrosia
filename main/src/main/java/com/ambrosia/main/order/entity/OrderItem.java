package com.ambrosia.main.order.entity;

import com.ambrosia.main.menu.entity.Item;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class OrderItem {
    @Id
    @SequenceGenerator(
            name = "order_item_sequence",
            sequenceName = "order_item_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_sequence")
    private Long id;

    @OneToOne
    @JoinColumn(
            name = "item_id",
            referencedColumnName = "itemId"
    )
    private Item item;
    private Integer quantity;
    private Double price;
}
