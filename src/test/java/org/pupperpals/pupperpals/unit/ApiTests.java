package org.pupperpals.pupperpals.unit;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.pupperpals.pupperpals.model.Pupper;
import org.pupperpals.pupperpals.service.PupperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ApiTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    PupperService service;

    @Test
    void getAllPuppers() throws Exception {
        List<Pupper> puppers = new ArrayList<Pupper>();
        puppers.add(new Pupper("Spot", "Dalmation"));
        puppers.add(new Pupper("Goldie", "Cocker Spaniel"));

        when(service.findAllPuppers()).thenReturn(puppers);

        mvc.perform(get("/puppers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
                .andExpect(jsonPath("[0]").value(puppers.get(0)))
                .andExpect(jsonPath("[1]").value(puppers.get(1)));
    }

    @Test
    void addPupper() throws Exception {
        Pupper adding = new Pupper("Spot", "Dalmation");

        doAnswer(invocation -> {
            Pupper p = invocation.getArgument(0);
            p.setId(1);
            return null;
        }).when(service).addPupper(isA(Pupper.class));

        Map<String, Object> body = new HashMap<>();
        body.put("name", adding.getName());
        body.put("breed", adding.getBreed());

        mvc.perform(post("/pupper")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("name").value("Spot"))
                .andExpect(jsonPath("breed").value("Dalmation"));
    }

    @Test
    void updatePupper() throws Exception {
        Pupper existing = new Pupper("Spot", "Dalmation");
        existing.setId(3);

        doAnswer(invocation -> {
            Pupper p = invocation.getArgument(0);
            Long index = invocation.getArgument(1);
            Pupper result = null;
            if (index == 3) {
                result = new Pupper(existing.getName(), existing.getBreed());
                result.setId(3);
                if (p.getName() != null)
                    result.setName(p.getName());
                if (p.getBreed() != null)
                    result.setBreed(p.getBreed());
            }
            return Optional.ofNullable(result);
        }).when(service).updatePupper(isA(Pupper.class), isA(Long.class));

        Map<String, Object> body1 = new HashMap<>();
        body1.put("name", "Bruce");

        mvc.perform(patch("/pupper/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(3))
                .andExpect(jsonPath("name").value("Bruce"))
                .andExpect(jsonPath("breed").value("Dalmation"));

        Map<String, Object> body2 = new HashMap<>();
        body2.put("breed", "Corgi");

        mvc.perform(patch("/pupper/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(3))
                .andExpect(jsonPath("name").value("Spot"))
                .andExpect(jsonPath("breed").value("Corgi"));

        Map<String, Object> body3 = new HashMap<>();
        body3.put("name", "Bruce");
        body3.put("breed", "Corgi");

        mvc.perform(patch("/pupper/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body3)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(3))
                .andExpect(jsonPath("name").value("Bruce"))
                .andExpect(jsonPath("breed").value("Corgi"));

        Map<String, Object> body4 = new HashMap<>();

        mvc.perform(patch("/pupper/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body4)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(3))
                .andExpect(jsonPath("name").value("Spot"))
                .andExpect(jsonPath("breed").value("Dalmation"));

        mvc.perform(patch("/pupper/12")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body3)))
                .andExpect(status().isNotFound());

    }

}
