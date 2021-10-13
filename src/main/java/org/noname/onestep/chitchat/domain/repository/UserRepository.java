package org.noname.onestep.chitchat.domain.repository;

import org.noname.onestep.chitchat.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    /**
     * メールアドレスをキーにログインユーザを取得する
     * @param emailAddress　ユーザのメールアドレス
     */
    User findByEmailAddressEquals(String emailAddress);
}
