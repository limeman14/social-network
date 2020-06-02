package com.skillbox.socialnetwork.main.security;

import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.repo.PersonRepository;
import com.skillbox.socialnetwork.main.security.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserServiceImpl userService;

    @Autowired
    public UserDetailsServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = this.userService.findByEmail(email);
        if(person == null){
            throw new UsernameNotFoundException("User with email "+email +" not found");
        }

        return UserDetailsImpl.build(person);
    }

}
