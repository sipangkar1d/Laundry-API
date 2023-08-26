package com.gpt5.laundry.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_product")
public class Product {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    @Column(name = "product_id")
    private String id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "stock", columnDefinition = "int check (stock>=0)")
    private Integer stock;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<ProductPrice> productPrices;

    public void addProductPrices(ProductPrice productPrice) {
        this.productPrices.add(productPrice);
    }
}
