package com.skillbox.socialnetwork.main.security.jwt;

import com.skillbox.socialnetwork.main.model.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public JwtUserFactory() {}

    public static JwtUser create(Person person) {
        List<GrantedAuthority> authorities = person.getRoles().stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList());
        return new JwtUser(
                person.getId(),
                person.getEmail(),
                person.getPassword(),
                authorities);
    }
}
