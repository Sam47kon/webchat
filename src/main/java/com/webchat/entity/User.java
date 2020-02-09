package com.webchat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "usr")
public class User extends BaseEntity {

    @Column
    private String userName;

    @Column
    private String email;

//    @Column
//    @Enumerated(EnumType.STRING)
//    private Status status;
}