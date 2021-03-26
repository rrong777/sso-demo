package com.rrong777.logbackdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    // logback中 这里的Logger类 并不是logback专属的Logger类，而是slf4j的Logger。
    // logback默认是实现了slf4j标准的
    private static final Logger LOG= LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.info("logback demo");
    }
}
