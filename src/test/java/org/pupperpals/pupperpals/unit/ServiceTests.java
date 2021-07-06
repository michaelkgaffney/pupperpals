

package org.pupperpals.pupperpals.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pupperpals.pupperpals.model.Pupper;
import org.pupperpals.pupperpals.repository.PupperRepository;
import org.pupperpals.pupperpals.service.PupperService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceTests {

    @Mock
    PupperRepository repo;

    @InjectMocks
    PupperService service;


    @Test
    void testFindAllPuppers() {
        List<Pupper> puppers = new ArrayList<Pupper>();
        puppers.add(new Pupper("Spot", "Dalmation"));
        puppers.add(new Pupper("Goldie", "Cocker Spaniel"));

        when(repo.findAll()).thenReturn(puppers);

        List<Pupper> actual = service.findAllPuppers();

        assertEquals(puppers, actual);
    }

    @Test
    void testAddPupper() {
        //No possibility of failing unit tests currently
    }

    @Test
    void testUpdatePupperName() {
        //Update is to the following Pupper:
        //Pupper pupper = new Pupper("Spot", "Dalmation");
        //pupper.setId(3);

        Pupper update = new Pupper("Bruce", null);
        Pupper expected = new Pupper("Bruce", "Dalmation");
        expected.setId(3);


        when(repo.findById(isA(Long.class)))
                .thenReturn(Optional.of(expected))
                .thenReturn(Optional.ofNullable(null));

        Pupper result = service.updatePupper(update, 3).orElse(null);
        assertEquals(expected, result);



        result = service.updatePupper(update, 12).orElse(null);
        assertNull(result);

    }

    @Test
    void testUpdatePupperBreed() {
        //Update is to the following Pupper:
        //Pupper pupper = new Pupper("Spot", "Dalmation");
        //pupper.setId(3);

        Pupper update = new Pupper(null, "Corgi");
        Pupper expected = new Pupper("Spot", "Corgi");
        expected.setId(3);


        when(repo.findById(isA(Long.class)))
                .thenReturn(Optional.of(expected))
                .thenReturn(Optional.ofNullable(null));

        Pupper result = service.updatePupper(update, 3).orElse(null);
        assertEquals(expected, result);

        result = service.updatePupper(update, 12).orElse(null);
        assertNull(result);

    }

    @Test
    void testUpdatePupperBoth() {
        //Update is to the following Pupper:
        //Pupper pupper = new Pupper("Spot", "Dalmation");
        //pupper.setId(3);

        Pupper update = new Pupper("Bruce", "Corgi");
        Pupper expected = new Pupper("Bruce", "Corgi");
        expected.setId(3);


        when(repo.findById(isA(Long.class)))
                .thenReturn(Optional.of(expected))
                .thenReturn(Optional.ofNullable(null));

        Pupper result = service.updatePupper(update, 3).orElse(null);
        assertEquals(expected, result);


        result = service.updatePupper(update, 12).orElse(null);
        assertNull(result);

    }

    @Test
    void testUpdatePupperNone() {
        //Update is to the following Pupper:
        //Pupper pupper = new Pupper("Spot", "Dalmation");
        //pupper.setId(3);

        Pupper update = new Pupper(null, null);
        Pupper expected = new Pupper("Spot", "Dalmation");
        expected.setId(3);


        when(repo.findById(isA(Long.class)))
                .thenReturn(Optional.of(expected))
                .thenReturn(Optional.ofNullable(null));

        Pupper result = service.updatePupper(update, 3).orElse(null);
        assertEquals(expected, result);



        result = service.updatePupper(update, 12).orElse(null);
        assertNull(result);

    }

}
