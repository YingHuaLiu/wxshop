package com.xiaowei.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import com.xiaowei.XiaoweiApplication;
import com.xiaowei.entity.LoginResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static com.xiaowei.service.TelVerificationServiceTest.VALID_PARAMETER;
import static com.xiaowei.service.TelVerificationServiceTest.VALID_PARAMETER_CODE;
import static java.net.HttpURLConnection.*;

@ExtendWith(SpringExtension.class) //可以使用Spring相关的应用
@SpringBootTest(classes = XiaoweiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.yml")
public class AuthIntegrationTest {
    @Autowired
    Environment environment;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static class HttpResponse {
        int code;
        String body;
        Map<String, List<String>> headers;

        public HttpResponse(int code, String body, Map<String, List<String>> headers) {
            this.code = code;
            this.body = body;
            this.headers = headers;
        }
    }

    private HttpResponse doHttpRequest(String apiName, boolean isGet, Object requestBody, String cookie) throws JsonProcessingException {
        HttpRequest request = isGet ? HttpRequest.get(getUrl(apiName)) : HttpRequest.post(getUrl(apiName));
        if (cookie != null) {
            request.header("Cookie", cookie);
        }
        request.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE);
        if (requestBody != null) {
            request.send(objectMapper.writeValueAsString(requestBody));
        }
        return new HttpResponse(request.code(), request.body(), request.headers());
    }

    @Test
    public void loginLogoutTest() throws JsonProcessingException {
        //默认情况下访问/api/status处于未登录状态
        String statusResponse = doHttpRequest("/api/status", true, null, null).body;

        LoginResponse response = objectMapper.readValue(statusResponse, LoginResponse.class);
        Assertions.assertFalse(response.isLogin());

        //发送验证码
        int responseCode = doHttpRequest("/api/code", false, VALID_PARAMETER, null).code;
        Assertions.assertEquals(HTTP_OK, responseCode);

        //带着验证码登录，得到cookie
        Map<String, List<String>> responseHeaders = doHttpRequest("/api/login", false, VALID_PARAMETER_CODE, null).headers;
        List<String> setCookie = responseHeaders.get("Set-Cookie");
        String sessionId = getSessionIdFromSetCookie(setCookie.stream().filter(cookie -> cookie.contains("JSESSIONID")).findFirst().get());

        //带者cookie访问/api/status，处于登陆状态
        statusResponse = doHttpRequest("/api/status", true, null, sessionId).body;
        response = objectMapper.readValue(statusResponse, LoginResponse.class);
        Assertions.assertTrue(response.isLogin());
        Assertions.assertEquals(VALID_PARAMETER.getTel(), response.getUser().getTel());

        //注销登录
        doHttpRequest("/api/logout", false, null, sessionId);

        //查看是否登录
        statusResponse = doHttpRequest("/api/status", true, null, sessionId).body;
        response = objectMapper.readValue(statusResponse, LoginResponse.class);
        Assertions.assertFalse(response.isLogin());
    }

    private String getSessionIdFromSetCookie(String setCookie) {
        int semicolonIndex = setCookie.indexOf(";");

        return setCookie.substring(0, semicolonIndex);
    }

    @Test
    public void returnHttpOKWhenParameterIsCorrect() throws JsonProcessingException {
        int responseCode = HttpRequest.post(getUrl("/api/code"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .send(objectMapper.writeValueAsString(VALID_PARAMETER))
                .code();
        Assertions.assertEquals(HTTP_OK, responseCode);
    }

    @Test
    public void returnHttpBadRequestWhenParameterIsCorrect() throws JsonProcessingException {
        int responseCode = HttpRequest.post(getUrl("/api/code"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .send(objectMapper.writeValueAsString(TelVerificationServiceTest.EMPTY_TEL))
                .code();
        Assertions.assertEquals(HTTP_BAD_REQUEST, responseCode);

    }

    @Test
    public void returnUnauthorizedIfNotLogin() {
        int responseCode = HttpRequest.post(getUrl("/api/any"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .code();
        Assertions.assertEquals(HTTP_UNAUTHORIZED, responseCode);

    }

    private String getUrl(String apiName) {
        //获取集成测试的端口号
        return "http://localhost:" + environment.getProperty("local.server.port") + apiName;
    }
}
