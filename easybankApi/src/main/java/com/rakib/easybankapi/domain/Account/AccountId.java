package com.rakib.easybankapi.domain.Account;

import com.rakib.easybankapi.domain.identifier.*;

import java.util.*;

public class AccountId implements UuidId {
    private final UUID value;

    public AccountId(UUID value) {
        this.value = value;
    }

    public static AccountId from(String id){
        return new AccountId(UUID.fromString(id));
    }

    public static AccountId from(UUID uuid){
        return new AccountId(uuid);
    }
    @Override
    public UUID getValue() {
        return value;
    }
}
