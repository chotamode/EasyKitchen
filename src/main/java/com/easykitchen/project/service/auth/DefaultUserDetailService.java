package com.easykitchen.project.service.auth;

import com.easykitchen.project.dao.UserDao;
import com.easykitchen.project.model.User;
import com.easykitchen.project.model.auth.DefaultUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DefaultUserDetailService implements UserDetailsService {

    private final UserDao userDao;

    public DefaultUserDetailService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Objects.requireNonNull(s);
        User user = userDao.findByUsername(s);
        return new DefaultUserDetails(user);
    }
}
