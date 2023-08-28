package com.gpt5.laundry.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_revenue")
public class Revenue {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    @Column(name = "revenue_id")
    private String id;

    @Column(name = "revenue")
    private Long revenue;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "paid_date", updatable = false)
    private Date paidDate;

    @OneToOne
    @JoinColumn(name = "transaction_id", unique = true)
    private Transaction transaction;
}
