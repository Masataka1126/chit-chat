package org.noname.onestep.chitchat.domain.repository;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class, databaseConnection = {"databaseConnection"})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class UserRepositoryTest {

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    @DatabaseSetup(value = "/db/testdata/setup/domain/repository/users/")
    @ExpectedDatabase(value = "/db/testdata/expecteddata/domain/repository/users/",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void userRepository_saveUser_registeredToDb() throws Exception{

        User user = new User();
        user.setEmailAddress("nogi@example.com");
        user.setPassword("0kadja3d");
        user.setName("ガルル");

        userRepository.save(user);
    }

    @Test
    @DatabaseSetup(value = "/db/testdata/setup/domain/repository/users/")
    public void userRepository_findAllUsers_correctlyAcquired() throws Exception{

        List<User> userList = userRepository.findAll();
        assertThat(userList.size()).isEqualTo(2);
        assertThat(userList.get(1).getEmailAddress()).isEqualTo("hinata@example.com");
        assertThat(userList.get(1).getPassword()).isEqualTo("kfw943ksd");
        assertThat(userList.get(1).getName()).isEqualTo("青春の馬");
    }

    @Test
    @DatabaseSetup(value = "/db/testdata/setup/domain/repository/users/")
    public void userRepository_findUserByEmailAddress_correctlyAcquired(){

        User user = userRepository.findByEmailAddressEquals("sakura@example.com");
        assertThat(user.getEmailAddress()).isEqualTo("sakura@example.com");
        assertThat(user.getPassword()).isEqualTo("abc123def");
        assertThat(user.getName()).isEqualTo("無言の宇宙");
    }

}
