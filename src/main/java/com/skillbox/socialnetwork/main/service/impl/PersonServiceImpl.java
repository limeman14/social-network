package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.dto.ResponseDto;
import com.skillbox.socialnetwork.main.dto.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseErrorResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseDto;
import com.skillbox.socialnetwork.main.model.Permission;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.Role;
import com.skillbox.socialnetwork.main.repository.PersonRepository;
import com.skillbox.socialnetwork.main.repository.RoleRepository;
import com.skillbox.socialnetwork.main.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PersonServiceImpl implements PersonService {

    private PersonRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Autowired
    public PersonServiceImpl(PersonRepository repository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository){
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Person> getAll() {
        return repository.findAll();
    }

    @Override
    public Person findByUsername(String username) {
        return null;
    }

    @Override
    public Person findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Person findById(Integer id) {
        return repository.findPersonById(id);
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public ResponseDto registration(RegisterRequestDto dto) {

        if(checkUserRegisterPassword(dto.getPassword1(), dto.getPassword2())) {
            if(checkUserLogin(dto.getEmail())) {
                Person person = new Person();
                person.setEmail(dto.getEmail());
                person.setPassword(passwordEncoder.encode(dto.getPassword1()));
                person.setFirstName(dto.getFirstName());
                person.setLastName(dto.getLastName());
                person.setConfirmationCode(dto.getCode());
                person.setBlocked(false);
                person.setApproved(true);
                person.setRegDate(new Date());
                person.setMessagesPermission(Permission.ALL);
                person.setRoles(getBasePermission());
                person.setLastOnline(LocalDateTime.now());

                repository.save(person);

                return new BaseResponseDto();
            }else{
                return new BaseErrorResponseDto("invalid_request", "Данный email уже зарегистрирован");
            }
        }else{
            return new BaseErrorResponseDto("invalid_request", "Пароль указан некорректно");
        }
    }

    private boolean checkUserRegisterPassword(String password1, String password2){
        return password1.equals(password2);
    }
    private boolean checkUserLogin(String email){
        return repository.findByEmail(email) == null;
    }

    private List<Role> getBasePermission(){
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        return userRoles;
    }

}
