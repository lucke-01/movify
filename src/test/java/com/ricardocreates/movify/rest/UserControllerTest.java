package com.ricardocreates.movify.rest;

import com.ricardocreates.movify.utiltest.HttpUtilTest;
import com.swagger.client.codegen.rest.model.UserDTO;
import com.swagger.client.codegen.rest.model.UserPatchDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerTest {
    private static final String USERS_API = "/api/users";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private HttpUtilTest httpUtilTest;

    //GET
    @Test
    void should_getUsers() throws Exception {
        //GIVEN
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(USERS_API))
                .andDo(MockMvcResultHandlers.print())
                //THEN
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(3)))
                .andReturn();
    }

    //GET BY ID
    @Test
    void should_getUser() throws Exception {
        //GIVEN
        final String userId = "1";
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(String.format("%s/%s", USERS_API, userId)))
                .andDo(MockMvcResultHandlers.print())
                //THEN
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(userId))
                .andReturn();
    }

    @Test
    void should_not_getUser() throws Exception {
        //GIVEN
        final String userId = "5";
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        get(String.format("%s/%s", USERS_API, userId)))
                .andDo(MockMvcResultHandlers.print())
                //THEN
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorCode", containsString("User could not find")))
                .andReturn();
    }

    //POST
    @Test
    void should_create_user() throws Exception {
        //GIVEN
        int userId = 4;
        UserDTO userDTO = mockCreateUserDTO(userId);
        String userDTOString = httpUtilTest.asJsonString(userDTO);
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        post(USERS_API)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userDTOString))
                .andDo(MockMvcResultHandlers.print())
                //THEN
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(userId))
                .andReturn();
    }

    //PUT
    @Test
    void should_update_user() throws Exception {
        //GIVEN
        int userId = 1;
        UserDTO userDTO = mockCreateUserDTO(userId);
        String userDTOString = httpUtilTest.asJsonString(userDTO);
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        put(String.format("%s/%s", USERS_API, userId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userDTOString))
                .andDo(MockMvcResultHandlers.print())
                //THEN
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(userId))
                .andReturn();
    }

    //DELETE
    @Test
    void should_deleteUser() throws Exception {
        //GIVEN
        final String userId = "1";
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        delete(String.format("%s/%s", USERS_API, userId)))
                .andDo(MockMvcResultHandlers.print())
                //THEN
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(userId))
                .andReturn();
    }

    //PATCH
    @Test
    void should_patch_user() throws Exception {
        //GIVEN
        final String userId = "1";
        String newName = "Pepe";
        BigDecimal newSalary = new BigDecimal("500");
        UserPatchDTO userPatchDTO = mockUserPatchDTO(newName, newSalary);
        String userPatchDTOString = httpUtilTest.asJsonString(userPatchDTO);
        //WHEN
        MvcResult response = this.mockMvc.perform(
                        patch(String.format("%s/%s", USERS_API, userId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userPatchDTOString))
                .andDo(MockMvcResultHandlers.print())
                //THEN
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value(newName))
                .andExpect(jsonPath("$.salary").value(newSalary))
                .andReturn();
    }

    private UserDTO mockCreateUserDTO(Integer id) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(id);
        userDTO.setName("name");
        userDTO.setLastName("lastName");
        userDTO.setBirthDay(LocalDate.now());
        userDTO.setSalary(new BigDecimal("200"));
        userDTO.setActive(true);

        return userDTO;
    }

    private UserPatchDTO mockUserPatchDTO(String name, BigDecimal salary) {
        UserPatchDTO userPatchDTO = new UserPatchDTO();

        userPatchDTO.setName(name);
        userPatchDTO.setSalary(salary);

        return userPatchDTO;
    }


}
