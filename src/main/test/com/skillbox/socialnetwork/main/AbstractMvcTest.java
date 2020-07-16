package com.skillbox.socialnetwork.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skillbox.socialnetwork.main.dto.auth.request.AuthenticationRequestDto;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@RunWith(SpringRunner.class)
@Ignore
public class AbstractMvcTest {
    protected MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();
    private static Set<Class> inited = new HashSet<>();


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Before
    public void init() throws Exception {
        if (!inited.contains(getClass())) {
            doInit();
            inited.add(getClass());
        }
    }

    protected void doInit() throws Exception {
    }

    protected String json(Object o) throws IOException {
        return mapper.writeValueAsString(o);
    }

    protected ResultActions login(String email, String password) throws Exception {
        final AuthenticationRequestDto auth = new AuthenticationRequestDto();
        auth.setEmail(email);
        auth.setPassword(password);
        return mockMvc.perform(
                post("/api/v1/auth/login")
                        .content(json(auth))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print());
    }

    protected String extractToken(MvcResult result) throws UnsupportedEncodingException {
        JsonObject jsonObject = (JsonObject) (new JsonParser().parse(result.getResponse().getContentAsString()));
        return jsonObject.get("data").getAsJsonObject().get("token").getAsString();
    }


}
