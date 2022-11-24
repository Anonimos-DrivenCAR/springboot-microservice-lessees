package com.anonimos.lessees.app.resources;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Lessor {
    private Long id;
    private String name;
    private String email;
    private String username;
    private String password;
}
