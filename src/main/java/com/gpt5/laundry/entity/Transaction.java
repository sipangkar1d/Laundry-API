package com.gpt5.laundry.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_transaction")
public class Transaction {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    @Column(name = "transaction_id")
    private String id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "invoice")
    private String invoice;

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "transaction")
    private List<TransactionDetail> orderDetails;


}
