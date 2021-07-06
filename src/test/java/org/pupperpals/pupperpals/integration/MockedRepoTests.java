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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                        fieldWithPath("[].breed").description("The name of the breed of the pupper"))
                ));
    }

    @Test
    void addPupper() throws Exception {
        Pupper adding = new Pupper("Spot", "Dalmation");

        doAnswer(invocation -> {
            Pupper p = invocation.getArgument(0);
            p.setId(1);
            return null;
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
                                fieldWithPath("breed").description("The name of the breed of the added pupper"))
                        )
                );
    }

}
