package com.github.izerui.learn;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.izerui.learn.network.NetworkReptiles;
import org.junit.Test;

import java.io.IOException;

public class OpenLearnTests {

    private NetworkReptiles network = new NetworkReptiles();

    private String cookie = "ASP.NET_SessionId=lv4jswadzdh5dord1cr2zrza; path=/; HttpOnly;__jsluid=aba55d4a170a25a36a08751520bc558d; max-age=31536000; path=/; HttpOnly";

    @Test
    public void testLogin() throws IOException {
        String login = network.login("liuyhopen1903", "P8425409");
        System.out.println(login);
    }

    @Test
    public void testGetCourses() throws IOException {
        JsonNode courses = network.getCourses(cookie);
        System.out.println(courses);
    }

}
