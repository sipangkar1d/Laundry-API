package com.gpt5.laundry.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
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

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @Column(name = "invoice")
    private String invoice;

    @ManyToOne()
    @JoinColumn(name = "status_id")
    private Status status;

    @CreatedBy
    @Column(name = "taken_by", updatable = false)
    private String takenBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transaction_date", updatable = false)
    private Date transactionDate;

    @LastModifiedBy
    @Column(name = "updated_by")
    protected String updatedBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    protected Date updatedDate;

    @OneToMany(mappedBy = "transaction")
    private List<TransactionDetail> transactionDetails;
}
