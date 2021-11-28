package org.noname.onestep.chitchat.domain.service.user;

import org.noname.onestep.chitchat.domain.entity.User;
import org.noname.onestep.chitchat.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository repository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
    }

    @Override
    @Transactional
    public void save(User user) {

        user.setPassword(this.passwordEncoder.encode((user.getPassword())));
        repository.save(user);
    }

    @Override
    public User findUser(String emailAddress) {
        return repository.findByEmailAddressEquals(emailAddress);
    }

    @Override
    public boolean userExits(String emailAddress) {

        Optional<User> exist =
                Optional.ofNullable(repository.findByEmailAddressEquals(emailAddress));

        return exist.isPresent();
    }
}
