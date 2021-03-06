package com.rrong777.log4j2demo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
//    private static final ;
    // log4j 和 log4j2中的Logger都是自己专属的Logger类。没有实现slf4j的标准
    private final static Logger LOG = LoggerFactory.getLogger(Main.class);

//    private final static Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.info("log4j2 demo");
    }
}
