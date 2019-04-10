package com.mks.utils;

import java.io.Serializable;

@lombok.Data
public class Data implements Serializable {
    Long id;
    String name;
    double amount;

}
