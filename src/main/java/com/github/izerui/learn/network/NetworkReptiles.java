package com.github.izerui.learn.network;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
public class NetworkReptiles {

    @Autowired
    private ObjectMapper objectMapper;

    private final static OkHttpClient okHttpClient = new OkHttpClient();
    // 登录入参
    private final static String black_box = "e3Y6ICIyLjUuMCIsIG9zOiAid2ViIiwgczogMTk5LCBlOiAianMgbm90IGRvd25sb2FkIn0=";

    // 登录地址
    private final static String loginUrl = "http://learn.open.com.cn/Account/UnitLogin?bust=" + System.currentTimeMillis();
    // 获取课程列表
    private final static String listCourseUrl = "http://learn.open.com.cn/StudentCenter/MyCourse/GetMyCourse?t=" + Math.random() + "&StatusCode=1";
    // 获取作业列表
    private final static String myWorkUrl = "http://learn.open.com.cn/StudentCenter/MyWork/GetOnlineJsonAll?t=" + Math.random();
    // 获取答案
    private final static String answerUrl = "http://learn.open.com.cn/StudentCenter/OnlineJob/GetQuestionDetail?bust="+System.currentTimeMillis()+"&itemBankId=%s&questionId=%s&_="+System.currentTimeMillis();

    private Request.Builder createRequest() {
        return new Request.Builder()
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.108 Safari/537.36");
    }

    private Request.Builder createRequest(String cookie) {
        return createRequest()
                .header("Cookie", cookie);
    }


    /**
     * 登录成功，返回当前登录的Cookie
     *
     * @param loginName
     * @param passWord
     * @return
     */
    public String login(String loginName, String passWord) throws IOException {
        Request request = createRequest()
                .url(loginUrl)
                .post(
                        new FormBody.Builder()
                                .add("loginName", loginName)
                                .add("passWord", passWord)
                                .add("validateNum", "")
                                .add("black_box", black_box)
                                .build()
                )
                .build();
        Call call = new OkHttpClient().newCall(request);
        Response response = call.execute();
        JsonNode jsonNode = objectMapper.readValue(response.body().bytes(), JsonNode.class);

        String cookie = String.join(";", response.headers("Set-Cookie"));
        String message = jsonNode.path("message").asText();
        if (!message.contains("登录成功")) {
            throw new RuntimeException(message);
        }
        return cookie;
    }

    /**
     * 获取课程列表
     *
     * @param cookie
     * @return
     */
    public JsonNode getCourses(String cookie) throws IOException {
        Request request = createRequest(cookie)
                .url(listCourseUrl)
                .get()
                .build();
        Call call = new OkHttpClient().newCall(request);
        Response response = call.execute();
        JsonNode jsonNode = objectMapper.readValue(response.body().bytes(), JsonNode.class);
        return jsonNode.path("data");
    }


    /**
     * 获取作业列表
     *
     * @param cookie
     * @return
     * @throws IOException
     */
    public JsonNode getMyWork(String cookie) throws IOException {
        Request request = createRequest(cookie)
                .url(myWorkUrl)
                .get()
                .build();
        Call call = new OkHttpClient().newCall(request);
        Response response = call.execute();
        JsonNode jsonNode = objectMapper.readValue(response.body().bytes(), JsonNode.class);
        return jsonNode.path("data");
    }

    /**
     * 获取某一个试卷的key
     * @param cookie
     * @param url
     * @return
     * @throws IOException
     */
    public String getKey(String cookie, String url) throws IOException {
        Request request = createRequest(cookie)
                .url(URLDecoder.decode(url,"utf-8"))
                .get()
                .build();
        Call call = new OkHttpClient().newCall(request);
        Response response = call.execute();
        String testPaperHtml = response.body().string();
        String[] split = URLDecoder.decode(Jsoup.parse(testPaperHtml).getElementById("online").attr("src"), "utf-8").split("&");
        for (String s : split) {
            if(s.startsWith("key=")){
                return s.replace("key=","");
            }
        }
        return null;
    }

    /**
     * 获取试卷
     * @param opCookie
     * @param url
     * @return
     */
    public JsonNode getTestPaper(String opCookie, String url) throws IOException {
        Request request = createRequest(opCookie)
                .url(url)
                .get()
                .build();
        Call call = new OkHttpClient().newCall(request);
        Response response = call.execute();
        JsonNode jsonNode = new ObjectMapper().readValue(response.body().bytes(), JsonNode.class);
        return jsonNode.path("data");
    }

    /**
     * 获取答案
     * @param opCookie
     * @param itemBankId
     * @param questionId
     * @return
     * @throws IOException
     */
    public Object getAnswer(String opCookie, String itemBankId, String questionId) throws IOException {
        Request request = createRequest(opCookie)
                .url(String.format(answerUrl,itemBankId,questionId))
                .get()
                .build();
        Call call = new OkHttpClient().newCall(request);
        Response response = call.execute();
        Map ans = objectMapper.readValue(response.body().bytes(), Map.class);
        return ans.get("data");
    }
}
