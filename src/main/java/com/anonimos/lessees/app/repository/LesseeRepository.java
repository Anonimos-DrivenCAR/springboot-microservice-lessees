package com.anonimos.lessees.app.repository;

import com.anonimos.lessees.app.model.entity.Lessee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LesseeRepository extends JpaRepository<Lessee,Long> {

    Optional<Lessee> findByEmail(String email);

    @Query("SELECT l FROM Lessee l WHERE l.email =?1 ")
    Optional<Lessee> getByEmail(String email);
    boolean existsByEmail(String email);

    @Modifying
    @Query("DELETE FROM LesseeRent lc WHERE lc.rentId=?1")
    void deleteLesseeRentByID(Long id);
}
