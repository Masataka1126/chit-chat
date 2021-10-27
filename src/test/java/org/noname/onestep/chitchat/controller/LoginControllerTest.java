package org.noname.onestep.chitchat.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import org.junit.jupiter.api.Test;
import org.noname.onestep.chitchat.Application;
import org.noname.onestep.chitchat.common.CsvDataSetLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners( {
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
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

        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("view/login"));
    }

    // 正しいユーザ情報が入力された場合にログインできること
    @Test
    @DatabaseSetup(value = "/db/testdata/setup/controller/login/")
    public void LoginPage_PostRequest_AllowLogin() throws Exception{
        this.mockMvc.perform(
                formLogin("/login")
                        .userParameter("emailAddress")
                        .passwordParam("password")
                        .user("testdata@example.com")
                        .password("password")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/chatspace/top"));
    }

    // 不正なユーザー情報が入力された場合にログインが出来ないこと
    @Test
    @DatabaseSetup(value = "/db/testdata/setup/controller/login/")
    public void LoginPage_PostRequest_RejectLogin() throws Exception{
        this.mockMvc.perform(
                        formLogin("/login")
                                .userParameter("emailAddress")
                                .passwordParam("password")
                                .user("testdata@example.com")
                                .password("incorrect_password")
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/?error"));
    }

}
