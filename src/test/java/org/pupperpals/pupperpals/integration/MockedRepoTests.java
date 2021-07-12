package org.pupperpals.pupperpals.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.pupperpals.pupperpals.model.Pupper;
import org.pupperpals.pupperpals.repository.PupperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class MockedRepoTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    PupperRepository repo;

    @Test
    void getAllPuppers() throws Exception {
        List<Pupper> puppers = new ArrayList<Pupper>();
        puppers.add(new Pupper("Spot", "Dalmation"));
        puppers.add(new Pupper("Goldie", "Cocker Spaniel"));

        when(repo.findAll()).thenReturn(puppers);

        mvc.perform(get("/puppers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
                .andExpect(jsonPath("[0]").value(puppers.get(0)))
                .andExpect(jsonPath("[1]").value(puppers.get(1)))
                .andDo(document("puppers GET", responseFields(
                        fieldWithPath("[]").description("An array of all puppers in the system"),
                        fieldWithPath("[].id").description("Internal ID of the pupper. For use with PATCH"),
                        fieldWithPath("[].name").description("The name of the pupper"),
                        fieldWithPath("[].breed").description("The breed name of the pupper"))
                ));
    }

    @Test
    void addPupper() throws Exception {
        Pupper adding = new Pupper("Spot", "Dalmation");

        doAnswer(invocation -> {
            Pupper p = invocation.getArgument(0);
            p.setId(1);
            return p;
        }).when(repo).save(isA(Pupper.class));

        Map<String, Object> body = new HashMap<>();
        body.put("name", adding.getName());
        body.put("breed", adding.getBreed());

        mvc.perform(post("/pupper")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("name").value("Spot"))
                .andExpect(jsonPath("breed").value("Dalmation"))
                .andDo(document("pupper POST"
                        ,requestFields(
                                fieldWithPath("name").description("The name of the pupper being added"),
                                fieldWithPath("breed").description("The name of the breed of the pupper being added"))
                        ,responseFields(
                                fieldWithPath("id").description("Internal ID of the added pupper. For use with PATCH"),
                                fieldWithPath("name").description("The name of the added pupper"),
                                fieldWithPath("breed").description("The breed name of the added pupper"))
                        )
                );
    }

    @Test
    void updatePupper() throws Exception {
        Pupper existing = new Pupper("Spot", "Dalmation");
        existing.setId(3);

        Pupper result1 = new Pupper("Bruce", "Dalmation");
        result1.setId(3);
        Pupper result2 = new Pupper("Spot", "Corgi");
        result2.setId(3);
        Pupper result3 = new Pupper("Bruce", "Corgi");
        result3.setId(3);

        doNothing().when(repo).updatePupperName(isA(String.class), isA(Long.class));
        doNothing().when(repo).updatePupperBreed(isA(String.class), isA(Long.class));
        doNothing().when(repo).updatePupperNameBreed(isA(String.class), isA(String.class), isA(Long.class));


        when(repo.findById(isA(Long.class)))
                .thenReturn(Optional.of(result1))
                .thenReturn(Optional.of(result2))
                .thenReturn(Optional.of(result3))
                .thenReturn(Optional.of(existing))
                .thenReturn(Optional.ofNullable(null));

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
                .andExpect(jsonPath("breed").value("Corgi"))
                .andDo(document("pupper PATCH"
                        ,requestFields(
                                fieldWithPath("name").description("OPTIONAL: The updated name of the Pupper"),
                                fieldWithPath("breed").description("OPTIONAL: The updated breed name of the Pupper"))
                        ,responseFields(
                                fieldWithPath("id").description("Internal ID of the updated pupper"),
                                fieldWithPath("name").description("The name of the updated pupper"),
                                fieldWithPath("breed").description("The breed name of the updated pupper"))
                ));

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
