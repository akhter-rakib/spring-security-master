package com.rakib.easybankapi.domain.account;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Setter
@Getter
public class Account {

    @Id
    private AccountId id;
    private Long customerId;
    private String branchAddress;
    private Date createdAt;
}
