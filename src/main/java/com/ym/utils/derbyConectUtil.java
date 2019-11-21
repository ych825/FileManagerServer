package com.ym.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class derbyConectUtil {

    static Connection conn = null;
    static{

        String appPath = System.getProperty("user.dir");  //获取当前项目路径
        String db = appPath + File.separator + "derby_DB"+ File.separator +Options.DATABASE; //获取当前项目路径下的数据库完整路径名称
        String driver = "org.apache.derby.jdbc.EmbeddedDriver"; //derby数据库驱动
        String protocol = "jdbc:derby:"; //derby数据库连接协议
        try {
            System.out.println(driver);
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            String url=protocol + db + ";create=true";
            System.out.println(url);
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static  Connection GetConnection(){
        return conn;
    }

}
