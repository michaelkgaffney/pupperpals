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

    public void addPupper(Pupper p) {
        repo.save(p);
    }

    public Optional<Pupper> updatePupper(Pupper p, long id) {
        if(p.getName() == null && p.getBreed() == null) {}
        else if(p.getName() != null && p.getBreed() == null)
            repo.updatePupperName(p.getName(), id);
        else if(p.getName() == null && p.getBreed() != null)
            repo.updatePupperBreed(p.getBreed(), id);
        else {
            repo.updatePupperNameBreed(p.getName(), p.getBreed(), id);
        }


        return repo.findById(id);
    }



}
