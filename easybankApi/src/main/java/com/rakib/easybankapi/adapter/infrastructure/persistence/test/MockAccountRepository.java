package com.rakib.easybankapi.adapter.infrastructure.persistence.test;

import com.rakib.easybankapi.domain.account.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class MockAccountRepository implements AccountRepository {

    private final List<Account> accountList = new ArrayList<>();

    @Override
    public void add(Account account) {
        accountList.add(account);
    }
}
