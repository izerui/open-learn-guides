package com.github.izerui.learn.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.izerui.learn.network.NetworkReptiles;
import com.github.izerui.learn.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.github.izerui.learn.response.Response.error;
import static com.github.izerui.learn.response.Response.success;

@RestController
public class LearnController {

    @Autowired
    private NetworkReptiles network;

    @GetMapping("/login")
    public Response getUserInfo(@RequestParam("username") String username,
                                        @RequestParam("password") String password,
                                        HttpSession session) throws IOException {
        String opCookie = network.login(username, password);
        System.out.println(opCookie);
        session.setAttribute("opCookie", opCookie);
        session.setAttribute("opUser", username);
        return success();
    }

    @GetMapping("/userInfo")
    public Response getUserInfo(@SessionAttribute("opUser") String opUser) {
        if (StringUtils.isEmpty(opUser)) {
            return error("用户未登录");
        }
        return success(opUser);
    }

    @GetMapping("/myWork")
    public Response myWork(@SessionAttribute("opCookie") String opCookie) throws IOException {
        JsonNode myWork = network.getMyWork(opCookie);
        return success(myWork);
    }

    @GetMapping("/getTestPagerKey")
    public Response getKey(@RequestParam("url") String url,
                           @SessionAttribute("opCookie") String opCookie) throws IOException {
        String key = network.getKey(opCookie,url);
        return success(key);
    }


}
