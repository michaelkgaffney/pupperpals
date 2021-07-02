package org.pupperpals.pupperpals.service;

import org.pupperpals.pupperpals.model.Pupper;
import org.pupperpals.pupperpals.repository.PupperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PupperService {

    @Autowired
    PupperRepository repo;

    public List<Pupper> findAllPuppers() {
        return repo.findAll();
    }


}
