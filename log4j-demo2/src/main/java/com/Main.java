package com;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
//    private static final ;
    // log4j 和 log4j2中的Logger都是自己专属的Logger类。没有实现slf4j的标准
    private final static Logger LOG = LogManager.getLogger(Main.class);


    public static void main(String[] args) {
        LOG.info("log4j2 demo");
    }
}
