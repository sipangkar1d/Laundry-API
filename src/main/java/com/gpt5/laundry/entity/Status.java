package com.gpt5.laundry.entity;

import com.gpt5.laundry.entity.constant.EStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_status")
public class Status {
    @Id
    @GenericGenerator(strategy = "uuid2", name = "system-uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "status_id")
    private String id;

    @Enumerated(EnumType.STRING)
    private EStatus status;
}
