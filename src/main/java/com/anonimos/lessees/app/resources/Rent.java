package com.anonimos.lessees.app.resources;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class Rent {
    private Long id;
    private String rentNo;
    private BigDecimal cost;
    private Boolean active;
    private List<Car>cars;
    private Lessor lessor;
}
