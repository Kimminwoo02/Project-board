package com.example.projectboard.controller;

import com.example.projectboard.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
@Import(SecurityConfig.class)
@DisplayName("뷰 컨트롤러 - 인증")
@WebMvcTest
public class AuthControllerTest {
    private final MockMvc mvc;
    public AuthControllerTest(@Autowired MockMvc mvc) { this.mvc = mvc;}
    @DisplayName("[view][GET] 뷰 로그인 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenTryingtoLogin_thenReturnsLoginView() throws Exception {
        //given

        //when&then
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));

    }

}
