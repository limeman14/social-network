package com.skillbox.socialnetwork.main;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.skillbox.socialnetwork.main.repository.PersonRepository;
import com.skillbox.socialnetwork.main.service.FriendsService;
import com.skillbox.socialnetwork.main.service.PersonService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
public class FriendsControllerTest extends AbstractMvcTest {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    FriendsService friendsService;

    @Autowired
    PersonService personService;

    @Value("${test.user.email}")
    private String email;

    @Value("${test.user.password}")
    private String password;

    @Value("${test.user.friendEmail}")
    private String friendEmail;

    @Value("${test.user.myFriendsFriendEmail}")
    private String myFriendsFriendEmail;

    @Value("${test.user.userFromMyCityEmail}")
    private String userFromMyCityEmail;

    private String url;

    @Override
    protected void doInit() throws Exception {
        personRepository.deleteAll();

        registerUser(email, password, "Ivan");
        registerUser(friendEmail, password, "Roman");
        registerUser(myFriendsFriendEmail, password, "Semen");
        registerUser(userFromMyCityEmail, password, "Karen");
        activateAccounts(personRepository, email, friendEmail, myFriendsFriendEmail, userFromMyCityEmail);
        personRepository.findByEmail(email).setCity("Москва");
        personRepository.findByEmail(userFromMyCityEmail).setCity("Москва");
        friendsService.addFriend(personRepository.findByEmail(friendEmail),
                personRepository.findByEmail(userFromMyCityEmail));
        friendsService.addFriend(personRepository.findByEmail(userFromMyCityEmail),
                personRepository.findByEmail(friendEmail));

    }

    @Test
    public void addFriendTest() throws Exception {
        Integer friendId = personRepository.findByEmail(friendEmail).getId();
        Integer userId = personRepository.findByEmail(email).getId();
        final String userToken = extractToken(login(email, password).andReturn());
        final String friendToken = extractToken(login(friendEmail, password).andReturn());

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

        MvcResult recommendations = getRecommendationsResult(userToken);

//        assertNotEquals(friendId, getIdFromData(getFromDataInResponse(recommendations, "id")));
        assertTrue(getDataFromResponse(recommendations).getAsJsonArray().size() == 0);


        addRecommendedFriends();
        Integer personFromMyCityId = personRepository.findByEmail(userFromMyCityEmail).getId();
        Integer friendsFriendId = personRepository.findByEmail(myFriendsFriendEmail).getId();
        recommendations = getRecommendationsResult(userToken);
        assertTrue(getIdListFromData(recommendations).contains(friendsFriendId));
        assertTrue(getIdListFromData(recommendations).contains(personFromMyCityId));

        mockMvc.perform(delete("/api/v1/friends/" + userId)
                .header("Authorization", friendToken))
                .andDo(print())
                .andExpect(status().isOk());

    }

    private JsonElement getFromDataInResponse(MvcResult result, String field) throws Exception {
        System.out.println(result.getResponse().getContentAsString());
        JsonElement json = new JsonParser().parse(result.getResponse().getContentAsString());

        if (json instanceof JsonArray) {
            return getDataFromResponse(result).getAsJsonArray().get(0).getAsJsonObject().get(field);
        }

        return getDataFromResponse(result).getAsJsonArray().get(0).getAsJsonObject().get(field);
    }

    private List<Integer> getIdListFromData(MvcResult result) throws Exception {
        List<Integer> idList = new ArrayList<>();
        JsonArray jsonArray = getDataFromResponse(result).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            idList.add(jsonElement.getAsJsonObject().get("id").getAsInt());
        }
        return idList;
    }

    private JsonElement getDataFromResponse(MvcResult result) throws Exception {
        JsonElement data = new JsonParser().parse(result.getResponse().getContentAsString()).getAsJsonObject().get("data");
        System.out.println(data);
        return data;
    }

    private Integer getIdFromData(JsonElement element) {
        return element.getAsInt();
    }

    private String getStatusFromResponse(MvcResult result) throws Exception {
        return new JsonParser().parse(result.getResponse().getContentAsString()).getAsJsonArray().get(0)
                .getAsJsonObject().get("status").getAsString();
    }

    private MvcResult getRecommendationsResult(String userToken) throws Exception {
        return mockMvc.perform(get("/api/v1/friends/recommendations/")
                .header("Authorization", userToken)
                .param("offset", "0")
                .param("itemPerPage", "20"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    private void addRecommendedFriends() throws Exception {
        registerUser(myFriendsFriendEmail, password, "Semen");
        registerUser(userFromMyCityEmail, password, "Karen");
        personRepository.findByEmail(email).setCity("Москва");
        personRepository.findByEmail(userFromMyCityEmail).setCity("Москва");
        friendsService.addFriend(personRepository.findByEmail(friendEmail),
                personRepository.findByEmail(userFromMyCityEmail));
        friendsService.addFriend(personRepository.findByEmail(userFromMyCityEmail),
                personRepository.findByEmail(friendEmail));
    }
}
