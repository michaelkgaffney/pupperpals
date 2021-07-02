package org.pupperpals.pupperpals.repository;

import org.pupperpals.pupperpals.model.Pupper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface PupperRepository extends JpaRepository<Pupper, Long> {



}
