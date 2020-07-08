package com.skillbox.socialnetwork.main;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.repository.PersonRepository;
import com.skillbox.socialnetwork.main.service.PersonService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
public class FriendsControllerTest extends AbstractMvcTest {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonService personService;

    @Value("${test.user.email}")
    private String email;

    @Value("${test.user.password}")
    private String password;

    @Value("${test.user.friendEmail}")
    private String friendEmail;

    @Value("${test.user.friendPassword}")
    private String friendPassword;

    @Override
    protected void doInit() throws Exception {
        if (personRepository.findByEmail(email) != null) {
            System.out.println("User " + email + " is already registered, trying to delete");
            System.out.println(personRepository.findByEmail(email).getId());
            deleteTestUser(email);
        }
        registerUser(email, password, "Ivan").andExpect(status().isOk());
        if (personRepository.findByEmail(friendEmail) != null) {
            System.out.println("User " + friendEmail + " is already registered, trying to delete");
            System.out.println(personRepository.findByEmail(friendEmail).getId());
            deleteTestUser(friendEmail);
        }
        registerUser(friendEmail, friendPassword, "Roman").andExpect(status().isOk());

    }

    @Test
    public void addFriendTest() throws Exception {
        Integer friendId = personRepository.findByEmail(friendEmail).getId();
        Integer userId = personRepository.findByEmail(email).getId();
        final String userToken = extractToken(login(email, password).andReturn());
        final String friendToken = extractToken(login(friendEmail, friendPassword).andReturn());

        mockMvc.perform(post("/api/v1/friends/" + friendId)
                .header("Authorization", userToken))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/api/v1/friends/request")
                .header("Authorization", friendToken))
                .andExpect(status().isOk())
                .andReturn();
        JsonObject jsonObject = new JsonParser().parse(result.getResponse().getContentAsString()
                .replaceAll("\\[", "")
                .replaceAll("\\]", ""))
                .getAsJsonObject();
        Integer responseFriendId = jsonObject.get("data").getAsJsonObject().get("id").getAsInt();

        assertEquals(userId, responseFriendId);
    }

    private ResultActions registerUser(String email, String password, String firstname) throws Exception {
        return mockMvc.perform(
                post("/api/v1/account/register")
                        .content("{\n" +
                                "  \"email\": \"" + email + "\",\n" +
                                "  \"passwd1\": \"" + password + "\",\n" +
                                "  \"passwd2\": \"" + password + "\",\n" +
                                "  \"firstName\": \"" + firstname + "\",\n" +
                                "  \"lastName\": \"Паровозов\",\n" +
                                "  \"code\": \"3675\"\n" +
                                "}").contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private void deleteTestUser(String email) {
        Person user = personService.findByEmail(email);
        System.out.println(user.getId());
        personRepository.deleteById(user.getId());
    }
}
