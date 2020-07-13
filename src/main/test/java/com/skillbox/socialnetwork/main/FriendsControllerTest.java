package com.skillbox.socialnetwork.main;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skillbox.socialnetwork.main.repository.PersonRepository;
import com.skillbox.socialnetwork.main.service.PersonService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.thymeleaf.spring5.expression.Mvc;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        personRepository.deleteAll();
        registerUser(email, password, "Ivan").andExpect(status().isOk());
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

        MvcResult requestSent = mockMvc.perform(get("/api/v1/friends/request")
                .header("Authorization", friendToken))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(userId, getIdFromData(getFromDataInResponse(requestSent, "id")));

        mockMvc.perform(post("/api/v1/friends/" + userId)
                .header("Authorization", friendToken))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult friendAdded = mockMvc.perform(get("/api/v1/friends/")
                .header("Authorization", friendToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(userId, getIdFromData(getFromDataInResponse(friendAdded, "id")));

        MvcResult isFriend = mockMvc.perform(post("/api/v1/is/friends/")
                .header("Authorization", friendToken)
                .param("idList", String.valueOf(userId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("FRIEND", getStatusFromResponse(isFriend));

        mockMvc.perform(delete("/api/v1/friends/" + userId)
                .header("Authorization", friendToken))
                .andDo(print())
                .andExpect(status().isOk());

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

    private JsonElement getFromDataInResponse(MvcResult result, String field) throws Exception {
        System.out.println(result.getResponse().getContentAsString());
        JsonElement json = new JsonParser().parse(result.getResponse().getContentAsString());

        if (json instanceof JsonArray) {
            JsonArray  parsedJson = json.getAsJsonArray();
            return parsedJson.get(0).getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject().get(field);
        }

        return json.getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject().get(field);


    }

    private Integer getIdFromData(JsonElement element) {
        return element.getAsInt();
    }

    private String getStatusFromResponse(MvcResult result) throws Exception {
        return new JsonParser().parse(result.getResponse().getContentAsString()).getAsJsonArray().get(0)
                .getAsJsonObject().get("status").getAsString();
    }
}
