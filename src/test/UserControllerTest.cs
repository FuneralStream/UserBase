package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.UserBaseApplication;
import org.example.dto.UserDto;
import org.example.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(classes = UserBaseApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    Long createdId = null;

    @Test
    @Order(1)
    public void testCreateUser() throws Exception {
        UserDto userDto = new UserDto(1L, "Иван", "ivan@example.com", 25, null);
        MvcResult result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        UserDto responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);
        assertNotNull(responseUser.id());
        createdId = responseUser.id();
    }

    @Test
    @Order(2)
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber());
    }

    @Test
    @Order(3)
    public void testUpdateUser() throws Exception {
        UserDto updatedUser = new UserDto(null, "Сергей", "sergey@example.com", 36, null);
        MvcResult result = mockMvc.perform(put("/users/{id}", createdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        UserDto responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);
        assertEquals(createdId, responseUser.id());
    }

    @Test
    @Order(4)
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", createdId))
                .andExpect(status().isOk())
                .andReturn();
    }
}