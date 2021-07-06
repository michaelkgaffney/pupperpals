package org.pupperpals.pupperpals.repository;

import org.pupperpals.pupperpals.model.Pupper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface PupperRepository extends JpaRepository<Pupper, Long> {

    @Transactional
    @Modifying
    @Query("Update Pupper p set p.name = :name where p.id = :id")
    void updatePupperName(@Param("name") String name, @Param("id") Long id);

    @Transactional
    @Modifying
    @Query("Update Pupper p set p.breed = :breed where p.id = :id")
    void updatePupperBreed(@Param("breed") String breed, @Param("id") Long id);

    @Transactional
    @Modifying
    @Query("Update Pupper p set p.breed = :breed, p.name = :name where p.id = :id")
    void updatePupperNameBreed(@Param("name") String name, @Param("breed") String breed,  @Param("id") Long id);

}
