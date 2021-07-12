package org.pupperpals.pupperpals.controller;


import org.pupperpals.pupperpals.model.Pupper;
import org.pupperpals.pupperpals.service.PupperService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PupperController {


    PupperService service;

    public PupperController(PupperService service) {
        this.service = service;
    }

    @GetMapping("/puppers")
    public ResponseEntity<Iterable<Pupper>> findAllPuppers(){
        return new ResponseEntity(service.findAllPuppers(), HttpStatus.OK);
    }

    @PostMapping("/pupper")
    public ResponseEntity<Pupper> addPupper(@RequestBody Pupper p) {


        return new ResponseEntity(service.addPupper(p), HttpStatus.CREATED);
    }

    @PatchMapping("/pupper/{id}")
    public ResponseEntity<Pupper> updatePupper(@RequestBody Pupper p, @PathVariable long id) {
        Pupper result = service.updatePupper(p, id).orElse(null);
        if(result != null)
            return new ResponseEntity(result, HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
