package com.rakib.easybankapi.domain.identifier;

import java.io.*;
import java.util.*;

public interface UuidId extends Serializable {
    UUID getValue();

    default UUID asUuid() {
        return getValue();
    }

    default String asText() {
        return asUuid().toString();
    }

    long serialVersionUID = -450794418L;
}
