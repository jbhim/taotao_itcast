package com.taotao.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

public class JedisClientPoolTest {
    ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");

    @Test
    public void fun() {
        JedisClient bean = context.getBean(JedisClient.class);
        bean.set("jedisclient","hello world");
        String jedisclient = bean.get("jedisclient");
        System.out.println(jedisclient);
    }
}