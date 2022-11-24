package com.anonimos.lessees.app.model.entity;

import com.anonimos.lessees.app.resources.Car;
import com.anonimos.lessees.app.resources.Rent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lessees")
@Entity
public class Lessee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "email",unique = true)
    @Email
    @NotBlank
    private String email;

    @Column(name = "user_name")
    @NotBlank
    @Size(min = 2, max = 20)
    private String username;

    @Column(name = "password")
    @NotBlank
    private String password;

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "lessor_id")
    private List<LesseeRent> lesseeRents = new ArrayList<>();

    @Transient
    private List<Rent> rents = new ArrayList<>();

    @Transient
    private List<Car> cars = new ArrayList<>();

    public void addLesseeRent(LesseeRent lesseeRent){
        lesseeRents.add(lesseeRent);
    }
    public void removeLesseeRent(LesseeRent lesseeRent){
        lesseeRents.remove(lesseeRent);
    }


}
