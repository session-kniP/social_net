package com.sessionknip.socialnet.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sessionknip.socialnet.web.dto.request.LoginRequestDto;
import com.sessionknip.socialnet.web.dto.request.UserCommunityRequestDto;
import com.sessionknip.socialnet.web.dto.response.LoginResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.StringWriter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommunityControllerTest {

    @Autowired
    MockMvc mockMvc;

    private final String mappingPath = "/api/community";

    @Test
    public void subscribeTest() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("Chatsky");
        loginRequestDto.setPassword("123");

        ObjectMapper objectMapper = new ObjectMapper();

        StringWriter sw = new StringWriter();

        objectMapper.writeValue(sw, loginRequestDto);

        String contentAsString = mockMvc.perform(post("/api/auth/login").contentType("application/json").content(sw.toString())).andDo(print()).andReturn().getResponse().getContentAsString();

        LoginResponseDto response = objectMapper.readValue(contentAsString, LoginResponseDto.class);

        UserCommunityRequestDto request = new UserCommunityRequestDto();
        request.setId(3L);

        sw = new StringWriter();
        objectMapper.writeValue(sw, request);

        mockMvc.perform(
                post(mappingPath + "/subscribe")
                        .contentType("application/json")
                        .header("Authorization", "Bearer_" + response.getToken())
                        .content(sw.toString()))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
