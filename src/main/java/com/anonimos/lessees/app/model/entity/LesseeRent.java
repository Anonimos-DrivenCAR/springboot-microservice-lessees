package com.anonimos.lessees.app.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lessee_rents")
public class LesseeRent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private Long id;

    @Column(name="rent_id", unique = true)
    private Long rentId;

    @Override
    public boolean equals(Object obj) {
        if(this  == obj)
            return true;

        if(!(obj instanceof LesseeRent))
            return false;

        LesseeRent o = (LesseeRent) obj;
        return this.rentId != null && this.rentId.equals(o.rentId);
    }
}
