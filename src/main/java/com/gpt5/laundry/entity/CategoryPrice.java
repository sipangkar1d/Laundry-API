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
@Table(name = "m_category_price")
public class CategoryPrice{
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    @Column(name = "category_price_id")
    private String id;

    @Column(name = "price")
    private Long price;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;
}

