package com.gpt5.laundry.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_product_price")
public class ProductPrice extends BaseAuditor<String> {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    @Column(name = "product_price_id")
    private String id;

    @Column(name = "price", columnDefinition = "bigint check (price>=0)")
    private Long price;

    @Column(name = "isActive")
    private Boolean isActive;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;
}
