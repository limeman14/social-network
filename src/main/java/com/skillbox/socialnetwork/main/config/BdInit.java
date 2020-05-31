package com.skillbox.socialnetwork.main.config;

import com.skillbox.socialnetwork.main.model.Role;
import com.skillbox.socialnetwork.main.repository.RoleRepository;
import com.skillbox.socialnetwork.main.service.PersonService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BdInit {

//    @Bean
//    CommandLineRunner initProperties(PersonService personService, RoleRepository repository) {
//        Role role = new Role();
//        role.setName("ROLE_USER");
//
//        repository.save(role);
//        personService.registration("alchin@mail.ru", "password", "password", "Yuriy", "Alchin", "3333");
//        personService.registration("blchin@mail.ru", "passwor1", "password1", "Yuriy", "Blchin", "3333");
//
//        return args -> {};
//    }
}
