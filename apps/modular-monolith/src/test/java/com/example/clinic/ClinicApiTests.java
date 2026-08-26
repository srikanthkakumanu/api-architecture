package com.example.clinic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ClinicApiTests {

    @Autowired
    MockMvc mvc;

    @Test
    void registersPatientAndSchedulesAppointment() throws Exception {
        mvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Maya Patel",
                                  "email": "maya@example.com",
                                  "phoneNumber": "+1-555-0100",
                                  "dateOfBirth": "1990-04-12"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fullName", is("Maya Patel")));

        mvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "patientId": 1,
                                  "doctorName": "Dr. Lee",
                                  "startsAt": "2026-09-01T10:30:00",
                                  "reason": "Annual checkup"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientName", is("Maya Patel")))
                .andExpect(jsonPath("$.status", is("SCHEDULED")));

        mvc.perform(get("/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorName", is("Dr. Lee")));
    }
}
