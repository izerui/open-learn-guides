package com.github.izerui.learn.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.izerui.learn.network.NetworkReptiles;
import com.github.izerui.learn.response.Response;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.github.izerui.learn.response.Response.error;
import static com.github.izerui.learn.response.Response.success;

@Slf4j
@RestController
public class LearnController {

    @Autowired
    private NetworkReptiles network;

    // 登录
    @ApiOperation("登录")
    @GetMapping("/login")
    public Response getUserInfo(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpSession session) throws IOException {
        String opCookie = network.login(username, password);
        session.setAttribute("opCookie", opCookie);
        session.setAttribute("opUser", username);
        return success();
    }

    // 获取用户信息
    @ApiOperation("获取用户信息")
    @GetMapping("/userInfo")
    public Response getUserInfo(@SessionAttribute(value = "opUser", required = false) String opUser) {
        if (StringUtils.isEmpty(opUser)) {
            return error("用户未登录");
        }
        return success(opUser);
    }

    // 获取作业
    @ApiOperation("获取作业")
    @GetMapping("/myWork")
    public Response myWork(@SessionAttribute(value = "opCookie", required = false) String opCookie) throws IOException {
        if (StringUtils.isEmpty(opCookie)) {
            return error("用户未登录");
        }
        JsonNode myWork = network.getMyWork(opCookie);
        return success(myWork);
    }

    // 获取key
    @ApiOperation("获取key")
    @GetMapping("/getTestPagerKey")
    public Response getKey(@RequestParam("url") String url,
                           @SessionAttribute(value = "opCookie", required = false) String opCookie) throws IOException {
        if (StringUtils.isEmpty(opCookie)) {
            return error("用户未登录");
        }
        String key = network.getKey(opCookie, url);
        return success(key);
    }

    // 获取试卷
    @GetMapping("/getTestPaper")
    public Response getTestPaper(@RequestParam("url") String url,
                                 @SessionAttribute(value = "opCookie", required = false) String opCookie) throws IOException {
        if (StringUtils.isEmpty(opCookie)) {
            return error("用户未登录");
        }
        JsonNode testPaper = network.getTestPaper(opCookie, url);
        return success(testPaper);
    }

    // 获取答案
    @PostMapping("/getAnswer")
    public Response getAnswers(@RequestParam("itemBankId") String itemBankId,
                               @RequestParam("questionId") String questionId,
                               @SessionAttribute(value = "opCookie", required = false) String opCookie) throws IOException {
        if (StringUtils.isEmpty(opCookie)) {
            return error("用户未登录");
        }
        JsonNode answer = network.getAnswer(opCookie, itemBankId, questionId);
        return success(answer);
    }

    // 保存答案
    @PutMapping("/saveHomeWork")
    public Response saveHomeWork(@RequestParam("answerSheet") String answerSheet,
                                 @RequestParam("homeworkID") String homeworkID,
                                 @RequestParam("homeworkAnswerId") String homeworkAnswerId,
                                 @SessionAttribute(value = "opCookie", required = false) String opCookie) throws IOException {
        if (StringUtils.isEmpty(opCookie)) {
            return error("用户未登录");
        }
        network.saveHomeWork(opCookie, homeworkID, homeworkAnswerId, answerSheet);
        return success();
    }

    // 提交答案
    @PutMapping("/submitHomeWork")
    public Response submitHomeWork(@RequestParam("answerSheet") String answerSheet,
                                   @RequestParam("homeworkID") String homeworkID,
                                   @RequestParam("homeworkAnswerId") String homeworkAnswerId,
                                   @SessionAttribute(value = "opCookie", required = false) String opCookie) throws IOException {
        if (StringUtils.isEmpty(opCookie)) {
            return error("用户未登录");
        }
        JsonNode jsonNode = network.submitHomeWork(opCookie, homeworkID, homeworkAnswerId, answerSheet);
        System.out.println(jsonNode.toString());
        return success(jsonNode);
    }


}
