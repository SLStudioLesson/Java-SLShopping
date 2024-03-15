package com.example.slshopping.dev;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.example.slshopping.entity.Role;
import com.example.slshopping.entity.User;
import com.example.slshopping.security.SLShopUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

@Component
@Profile("dev")
public class DevAuthenticationFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request,
            ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // ダミーの権限を生成
        Set<Role> dummyAuthority = new HashSet<>(Collections.singletonList(
            new Role(1L, "Admin", "管理者")
        ));

        // ダミーのユーザーを生成
        User dummyUser = new User(1L, "dummy@example.com", "dummyPassword", "dummyユーザー", true, dummyAuthority);
        SLShopUserDetails user = new SLShopUserDetails(dummyUser);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        // SecurityContext に設定
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}
