package com.github.izerui.learn;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class OpenAnswerTests {

    private String cookie = "ASP.NET_SessionId=givqo5hcucezxbn4f1yymdr2; path=/; HttpOnly;__jsluid=038bb445e1c4375163522fd42e57c430; max-age=31536000; path=/; HttpOnly";

    // get 该地方返回所有作业列表
    // http://learn.open.com.cn/StudentCenter/MyWork/GetOnlineJsonAll?t=0.2601177278482374

    // get 根据作业列表里面的相关参数 请求点击跳转做作业页面， 跳转后就可以获取到key
    // http://learn.open.com.cn/StudentCenter/OnLineJob/Redirect?mode=1&courseExerciseID=0&submitCount=2&studentHomeworkId=006f32f7-c6a2-4863-9283-4ead1791f1ed

    // 马克思
//    private String testPaper = "http://learn.open.com.cn/StudentCenter/OnlineJob/DoHomework?courseExerciseId=0&key=d7bbb0aefb0bc5f6&studentHomeworkId=b93a771e-f2b4-4680-8bc5-397f11429667&_=1556386233016";

    // 计算机2
//    private String testPaper = "http://learn.open.com.cn/StudentCenter/OnlineJob/DoHomework?courseExerciseId=0&key=7b265f5841adbdd9&studentHomeworkId=fe37f00f-3d35-4c90-ada9-fcd55e488b62&_=1556387530051";

    // 英语2
//    private String testPaper = "http://learn.open.com.cn/StudentCenter/OnlineJob/DoHomework?courseExerciseId=0&key=568b2ec695484433&studentHomeworkId=dca41117-e0df-487a-9863-2305aed03356&_=1556422278873";

    // 计算机1
    private String testPaper = "http://learn.open.com.cn/StudentCenter/OnlineJob/DoHomework?courseExerciseId=0&key=3bd53e5e81a1fa9c&studentHomeworkId=006f32f7-c6a2-4863-9283-4ead1791f1ed&_=1556424100027";

    //    private String questionId = "ddec230e-8299-46aa-9fcb-b881d3420ba8a";
//    private String questionId = "qes-4c0893fc-6bfa-4c8c-9952-1781b2b8e132";

    public void getAnswers(String itemBankId, String questionId) throws IOException {
        Request request = new Request.Builder()
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.108 Safari/537.36")
                .header("Cookie", cookie)
                .url("http://learn.open.com.cn/StudentCenter/OnlineJob/GetQuestionDetail?bust=1556386164136&itemBankId=" + itemBankId + "&questionId=" + questionId + "&_=1556386163650")
                .get()
                .build();
        Call call = new OkHttpClient().newCall(request);
        Response response = call.execute();
        String string = response.body().string();
        JsonNode ans = new ObjectMapper().readValue(string, JsonNode.class);
        String title = ans.path("data").path("I2").asText();
        System.out.println(title);
        JsonNode choices = ans.path("data").path("Choices");
        choices.elements().forEachRemaining(choice -> {
            boolean isCorrect = choice.path("IsCorrect").asBoolean();
            if (isCorrect) {
                System.out.print("√ ");
            } else {
                System.out.print("  ");
            }
            System.out.print(choice.path("I1").asText());
            System.out.print(" : ");
            System.out.print(choice.path("I2").asText());
            System.out.println();
        });
//        System.out.println("::::::::::::::::::" + string);
    }

    @Test
    public void getQuestions() throws IOException {
        Request request = new Request.Builder()
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.108 Safari/537.36")
                .header("Cookie", cookie)
                .url(testPaper)
                .get()
                .build();
        Call call = new OkHttpClient().newCall(request);
        Response response = call.execute();
        String string = response.body().string();
        JsonNode jsonNode = new ObjectMapper().readValue(string, JsonNode.class);
        JsonNode testPaperContent = jsonNode.path("data").path("TestPaperContent");
        String itemBankId = testPaperContent.path("Model").path("P3").asText();
        JsonNode path = testPaperContent.path("Sections");
        path.elements().forEachRemaining(s -> {
            System.out.println("--------------------" + s.path("Title").asText() + "------------------");
            AtomicReference<Integer> i = new AtomicReference<>(1);
            s.path("ItemID").elements().forEachRemaining(item -> {
                try {
                    System.out.print(i.getAndSet(i.get() + 1) + ". ");
                    getAnswers(itemBankId, item.asText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                System.out.println(item.asText());
            });
        });

//        System.out.println(string);

    }
}
