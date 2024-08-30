package com.earthdefender.earthcpr;

import com.earthdefender.earthcpr.DTO.UserDTO;
import com.earthdefender.earthcpr.controller.UserController;
import com.earthdefender.earthcpr.restdocs.AbstractRestDocsTests;
import com.earthdefender.earthcpr.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest extends AbstractRestDocsTests {
    @MockBean
    UserService userService;

    @Test
    void CreateUserTest() throws Exception {
        UserDTO.UserData userData = UserDTO.UserData.builder()
                .loginId("loginId")
                .userNickname("nickname")
                .password("password")
                .build();

        mockMvc.perform(post("/api/v1/user/create")
                        .content(objectMapper.writeValueAsString(userData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
                                        fieldWithPath("userNickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                        ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("data").type(JsonFieldType.NULL).description("반환되는 데이터 없음")
                                )
                        )
                );
    }
    @Test
    void LoginUserTest() throws Exception {
        UserDTO.UserLoginRequest userLoginRequest = UserDTO.UserLoginRequest.builder()
                .loginId("loginId")
                .password("password")
                .build();

        mockMvc.perform(post("/api/v1/user/login")
                        .content(objectMapper.writeValueAsString(userLoginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("data").type(JsonFieldType.NULL).description("반환되는 데이터 없음")
                                )
                        )
                );
    }

}
