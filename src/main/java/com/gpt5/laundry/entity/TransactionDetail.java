package com.gpt5.laundry.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_transaction_detail")
public class TransactionDetail {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "order_transaction_id")
    private String id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "softener_quantity")
    private Integer softenerQuantity;

    @ManyToOne()
    @JoinColumn(name = "softener_id")
    private ProductPrice softener;

    @ManyToOne()
    @JoinColumn(name = "category_price_id")
    private CategoryPrice categoryPrice;

    @ManyToOne()
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
}
