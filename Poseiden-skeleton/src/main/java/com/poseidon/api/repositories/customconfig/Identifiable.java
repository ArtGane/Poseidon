package com.poseidon.api.repositories.customconfig;

import java.io.Serializable;

public interface Identifiable<T extends Serializable> extends Serializable {
    T getId();
}
