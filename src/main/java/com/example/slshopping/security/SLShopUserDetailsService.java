package com.example.slshopping.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.slshopping.entity.User;
import com.example.slshopping.user.UserRepository;

@Service
public class SLShopUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

	@Override
	public SLShopUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("ユーザが見つかりません");
        }

        return new SLShopUserDetails(user);
	}

}
