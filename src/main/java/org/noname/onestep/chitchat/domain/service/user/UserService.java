package org.noname.onestep.chitchat.domain.service.user;

import org.noname.onestep.chitchat.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    /**
     * ログインユーザを登録(更新)するメソッド
     * @param user ログインユーザ情報を格納
     * user.idがDBに存在しない場合：登録
     *　　　　　　　　存在する場合：更新
     */
    void save(User user);

    /**
     * ログインユーザがDBに存在するかを確認するメソッド
     * @param emailAddress
     * ログインユーザが存在する場合：true
     */
    boolean userExits(String emailAddress);

}
