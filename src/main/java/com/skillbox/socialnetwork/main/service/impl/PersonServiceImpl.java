package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.dto.ResponseDto;
import com.skillbox.socialnetwork.main.dto.request.PasswordSetDto;
import com.skillbox.socialnetwork.main.dto.request.RegisterRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseErrorResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.ErrorResponseDto;
import com.skillbox.socialnetwork.main.model.Permission;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.Role;
import com.skillbox.socialnetwork.main.model.enumerated.ERole;
import com.skillbox.socialnetwork.main.repository.PersonRepository;
import com.skillbox.socialnetwork.main.repository.RoleRepository;
import com.skillbox.socialnetwork.main.security.jwt.JwtTokenProvider;
import com.skillbox.socialnetwork.main.service.EmailService;
import com.skillbox.socialnetwork.main.service.PersonService;
import com.skillbox.socialnetwork.main.util.CodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private final EmailService emailService;
    private final JwtTokenProvider tokenProvider;

    @Value("${project.name}")
    private String projectName;

    @Autowired
    public PersonServiceImpl(PersonRepository repository,
                             BCryptPasswordEncoder passwordEncoder,
                             RoleRepository roleRepository,
                             EmailService emailService,
                             JwtTokenProvider tokenProvider
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.tokenProvider = tokenProvider;
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

        if (checkUserRegisterPassword(dto.getPassword1(), dto.getPassword2())) {
            if (checkUserLogin(dto.getEmail())) {
                Person person = new Person();
                person.setEmail(dto.getEmail());
                person.setPassword(passwordEncoder.encode(dto.getPassword1()));
                person.setFirstName(dto.getFirstName());
                person.setLastName(dto.getLastName());
                person.setConfirmationCode(dto.getCode());
                person.setIsBlocked(false);
                person.setMessagesPermission(Permission.ALL);
                person.setIsApproved(true);
                person.setRoles(getBasePermission());
                person.setLastOnlineTime(new Date());
                person.setConfirmationCode("Random");

                repository.save(person);
                emailService.sendSimpleMessageUsingTemplate(dto.getEmail(), projectName, dto.getFirstName(), "Рады приветствовать Вас на нашем ресурсе!");
                return new BaseResponseDto();
            } else {
                return new BaseErrorResponseDto("invalid_request", "Данный email уже зарегистрирован");
            }
        } else {
            return new BaseErrorResponseDto("invalid_request", "Пароль указан некорректно");
        }
    }


    private boolean checkUserRegisterPassword(String password1, String password2) {
        return password1.equals(password2);
    }

    private boolean checkUserLogin(String email) {
        return repository.findByEmail(email) == null;
    }

    private List<Role> getBasePermission() {
        Role roleUser = roleRepository.findByName(ERole.ROLE_USER);
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        return userRoles;
    }

    public void logout(Person person) {
        person.setLastOnlineTime(new Date());
        log.info("IN logout - user: {} logged out", repository.save(person).getEmail());
    }

    @Override
    public ResponseDto passwordRecovery(String email, String url) {
        Person person = repository.findByEmail(email);
        if (person != null) {
            person.setConfirmationCode(CodeGenerator.codeGenerator());
            repository.save(person);
            String token = tokenProvider.createToken(person.getEmail() + ":" + person.getConfirmationCode());
            emailService.sendPasswordRecovery(email, projectName, person.getFirstName(), url + "/change-password?token=" + token);
            return new BaseResponseDto();
        } else {
            return new BaseErrorResponseDto("invalid_request", "Данный email не найден");
        }
    }

    @Override
    public ResponseDto passwordSet(PasswordSetDto dto) {
        String token = dto.getToken().replaceAll("token=", "");
        String[] strings = tokenProvider.getUsername(token).split(":");
        if(strings.length == 2){
            Person person = repository.findByEmail(strings[0]);
            if(person.getConfirmationCode().equals(strings[1])){
                person.setPassword(passwordEncoder.encode(dto.getPassword()));
                person.setConfirmationCode("");
                repository.save(person);
                return new BaseResponseDto();
            }
        }
        return new BaseErrorResponseDto("invalid_request", "token error");
    }
}
