package com.poseidon.api.repository.customconfig;

import java.io.Serializable;

public interface Identifiable<T extends Serializable> extends Serializable {
    T getId();

}
