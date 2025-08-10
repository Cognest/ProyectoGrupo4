package com.atm.buenas_practicas_java.controllers;


import com.atm.buenas_practicas_java.PostgreSQLContainerTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@Log4j2
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class DefaultControllerTest extends PostgreSQLContainerTest {

    @Autowired
    private MockMvc mockMvc;

}