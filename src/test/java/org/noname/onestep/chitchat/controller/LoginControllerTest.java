package org.noname.onestep.chitchat.controller;

import org.junit.jupiter.api.Test;
import org.noname.onestep.chitchat.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class)
public class LoginControllerTest {

    private final MockMvc mockMvc;

    @Autowired
    public LoginControllerTest(MockMvc mockMvc){
        this.mockMvc = mockMvc;
    }

    // ログインページにGETリクエストを送信した場合、ステータス「200」が返ってくること
    @Test
    public void LoginPage_GetRequest_ReturnHTTPStatus200() throws Exception {

        this.mockMvc.perform(get("/login")).andDo(print())
                .andExpect(status().isOk());
    }

    // ログインページからPOSTリクエストを行った場合、モデルにデータが格納されていること
    @Test
    public void LoginPage_PostRequest_StoredModel() throws Exception {

    }



}
