package org.noname.onestep.chitchat.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Test;
import org.noname.onestep.chitchat.Application;
import org.noname.onestep.chitchat.common.CsvDataSetLoader;
import org.noname.onestep.chitchat.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class)
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class,databaseConnection = "databaseConnection")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public class UserControllerTest {

    private final MockMvc mockMvc;

    @Autowired
    public UserControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    // ユーザー登録用ページにGETリクエストを送信した場合、ステータス「200」が返ってくること
    @Test
    public void userCreatePage_getRequest_returnHTTPStatus200() throws Exception {

        this.mockMvc.perform(get("/users/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("view/user"));
    }

    // DBに存在するメールアドレスで登録処理を行った場合、登録が失敗すること
    @Test
    @DatabaseSetup(value = "/db/testdata/setup/controller/user/")
    @Transactional
    public void userCreatePage_postRequest_withRegisteredUser_failRegistration() throws Exception {

        this.mockMvc.perform(post("/users/create")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("emailAddress","testdata@example.com")
                        .param("password","password")
                        .param("name","無言の宇宙"))
                .andExpect(model().attribute("message","すでに登録されているメールアドレスです。"))
                .andExpect(view().name("/view/user"));
    }

    @Test
    @DatabaseSetup(value = "/db/testdata/setup/controller/user/")
    @ExpectedDatabase(value = "/db/testdata/expecteddata/controller/user/"
    ,assertionMode = DatabaseAssertionMode.NON_STRICT)
    @Transactional
    public void userCreatePage_postRequest_succeedRegistration() throws Exception {

        this.mockMvc.perform(post("/users/create")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("emailAddress","mydata@example.com")
                .param("password","password")
                .param("name","宇宙人"))
                .andExpect(model().attribute("message","ユーザー登録が完了しました。"))
                .andExpect(view().name("/view/login"));
    }
}
