package org.noname.onestep.chitchat.domain.service.user;

import org.noname.onestep.chitchat.domain.entity.User;

public interface UserService {

    /**
     * ログインユーザを登録(更新)するメソッド
     * @param user ログインユーザ情報を格納
     * user.idがDBに存在しない場合：登録
     *　　　　　　　　存在する場合：更新
     */
    void save(User user);

    /**
     * メールアドレスをもとにログインユーザ情報を検索するメソッド
     * @param emailAddress
     */
    User findUser(String emailAddress);

    /**
     * ログインユーザがDBに存在するかを確認するメソッド
     * @param emailAddress
     * ログインユーザが存在する場合：true
     */
    boolean userExits(String emailAddress);

}
