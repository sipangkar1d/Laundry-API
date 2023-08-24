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
@Table(name = "m_customer")
public class Customer extends BaseAuditor<String>{
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    @Column(name = "customer_id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "address")
    private String address;
}
