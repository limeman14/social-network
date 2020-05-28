package com.skillbox.socialnetwork.main.security.jwt;

import com.skillbox.socialnetwork.main.model.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(Person person) {
        return new JwtUser(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                mapToGrantedAuthorities(),
                person.getPassword(),
                person.getEmail(),
                !person.getBlocked(),
                person.getLastOnline()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities() {
        ArrayList<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("USER"));
        return roles;
    }
}
