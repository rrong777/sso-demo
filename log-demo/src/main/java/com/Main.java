package com;

public class Main {
    public static void main(String[] args) {
        // 如果三个项目分别是使用了log4j，log4j2，logback的实现
        // 不起冲突 此时没有使用任何配置文件，使用的就是三个被引入的本地项目的配置文件
        com.rrong777.log4jdemo.Main.main(args);
        com.rrong777.log4j2demo.Main.main(args);
        com.rrong777.logbackdemo.Main.main(args);
    }
}
