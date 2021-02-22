package com.xiaowei.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaowei.XiaoweiApplication;
import com.xiaowei.entity.Goods;
import com.xiaowei.entity.Response;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;

@ExtendWith(SpringExtension.class) //可以使用Spring相关的应用
@SpringBootTest(classes = XiaoweiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:test-application.yml"})
public class GoodsIntegrationTest extends AbstractIntegrationTest {
    @Value("${spring.datasource.url}")
    private String databaseUrl;
    @Value("${spring.datasource.username}")
    private String databaseUsername;
    @Value("${spring.datasource.password}")
    private String databasePassword;

    @BeforeEach
    public void initDataBase() {
        ClassicConfiguration conf = new ClassicConfiguration();
        conf.setDataSource(databaseUrl, databaseUsername, databasePassword);
        Flyway flyway = new Flyway(conf);
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void testCreateGoods() throws JsonProcessingException {
        String cookie = loginAndGetCookie();

        Goods goods = new Goods();
        goods.setName("肥皂");
        goods.setDescription("纯天然无污染肥皂");
        goods.setDetails("这是一块好肥皂");
        goods.setImgUrl("https://img.url");
        goods.setPrice(500L);
        goods.setStock(10);
        goods.setShopId(12345L);

        HttpResponse response = doHttpRequest("/api/v1/goods",
                false,
                goods,
                cookie);

        Response<Goods> responseData = objectMapper.readValue(response.body, new TypeReference<Response<Goods>>() {
        });

        Assertions.assertEquals(SC_CREATED, response.code);
        Assertions.assertEquals("肥皂", responseData.getData().getName());

    }

    @Test
    public void testDeleteGoods() {

    }
}
