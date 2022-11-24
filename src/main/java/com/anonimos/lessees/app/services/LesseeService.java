package com.anonimos.lessees.app.services;

import com.anonimos.lessees.app.model.entity.Lessee;
import com.anonimos.lessees.app.resources.Rent;

import java.util.List;
import java.util.Optional;

public interface LesseeService {
    List<Lessee> findAll();
    Optional<Lessee> findLessorById(Long id);
    Optional<Lessee> findByEmail(String email);
    void delete(Long id);
    Lessee create(Lessee newLessor);
    Lessee update(Long id, Lessee lessor);
    boolean existByEmail(String email);




    /**Microservices-iteration*/

    void deleteLessorCarById(Long id);

    Optional<Lessee> findByIdWithCars(Long id);
    Optional<Rent> assignCar(Rent car,Long lessorId);
    Optional<Rent> createCar(Rent car,Long LessorId);
    Optional<Rent> unAssignCar(Rent car,Long lessorId);
}
