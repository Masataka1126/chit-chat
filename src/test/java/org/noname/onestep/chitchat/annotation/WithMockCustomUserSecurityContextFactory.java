package org.noname.onestep.chitchat.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public WithMockCustomUserSecurityContextFactory(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // ユーザ名・パスワードで認証するためのトークンを発行
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                customUser.emailAddress(),customUser.password()
        );

        // ログイン情報をSecurity Contextに登録
        context.setAuthentication(authenticationManager.authenticate(authToken));

        return context;
    }
}
