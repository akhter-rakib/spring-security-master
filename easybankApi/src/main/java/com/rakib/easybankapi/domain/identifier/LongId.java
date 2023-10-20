package com.rakib.easybankapi.domain.identifier;

import java.io.*;

public interface LongId extends Serializable {
    long getValue();

    default long asLong() {
        return getValue();
    }

    default String asText() {
        return Long.toString(getValue());
    }

    static final long serialVersionUID = -15577L;

}
