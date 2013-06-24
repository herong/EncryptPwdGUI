/**
 * @文件名 : Environment.java
 * @包名 : cn.sinobest.framework.comm
 * @描述 : TODO(用一句话描述该文件做什么)
 * @作者 : herong 填写您的邮箱地址
 * @版权 : cn.sinobest 版权所有
 * @创建时间: 2010-11-1 下午11:04:28
 * @版本 : V1.0
 */
package com.github.herong.comm;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.Properties;

/**
 * @类型名称: Environment
 * @功能说明: TODO(这里用一句话描述这个类的作用)
 * @作者 : herong
 * @创建时间: 2010-11-1 下午11:04:28
 * @修改人员: herong
 * @修改说明: TODO(描述本次修改内容)
 * @修改时间: 2010-11-1 下午11:04:28
 * @版本 : V1.0
 * @参考 :
 */
public class Environment implements Serializable {

    /**
     * 系统配置文件
     */
    public static final String JDBC_PROPERTIES = "./sysconfig.properties";

    /**
     * 系统配置信息
     */
    public static final Properties GLOBAL_PROPERTIES;

    public static String DB_JDBC_CLASS = "";
    public static String DB_URL = "";
    public static String DB_USERNAME = "";
    public static String DB_PASSWORD = "";

    public static String DB_JDBC_CLASS2 = "";
    public static String DB_URL2 = "";
    public static String DB_USERNAME2 = "";
    public static String DB_PASSWORD2 = "";

    public static String SQL_TPL = "";
    public static String DEFUALT_PWD = "aaaaaa";
    static {
        GLOBAL_PROPERTIES = new Properties();

        try {

            InputStream stream = Util.getResourceAsStream(JDBC_PROPERTIES);
            try {
                System.out.println("加载" + JDBC_PROPERTIES);
                GLOBAL_PROPERTIES.load(stream);
                System.out.println("初始化host");
                init();
            } catch (IOException e) {
                System.out.println("加载" + JDBC_PROPERTIES + "失败!");
                e.printStackTrace();
                System.exit(1);
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            System.out.println(JDBC_PROPERTIES + "未找到");
        }
    }

    private static void init() {
        DB_JDBC_CLASS = Environment.GLOBAL_PROPERTIES.getProperty("jdbc.driver");
        DB_URL = Environment.GLOBAL_PROPERTIES.getProperty("jdbc.url");
        DB_USERNAME = Environment.GLOBAL_PROPERTIES.getProperty("jdbc.username");
        DB_PASSWORD = Environment.GLOBAL_PROPERTIES.getProperty("jdbc.password");

        SQL_TPL = Environment.GLOBAL_PROPERTIES.getProperty("sql.tpl");
        DEFUALT_PWD = Environment.GLOBAL_PROPERTIES.getProperty("defualt.pwd");
        
        /*
         * DB_JDBC_CLASS2 =
         * Environment.GLOBAL_PROPERTIES.getProperty("jdbc.driver2"); DB_URL2 =
         * Environment.GLOBAL_PROPERTIES.getProperty("jdbc.url2"); DB_USERNAME2
         * = Environment.GLOBAL_PROPERTIES.getProperty("jdbc.username2");
         * DB_PASSWORD2 =
         * Environment.GLOBAL_PROPERTIES.getProperty("jdbc.password2");
         */

    }

    public static String save() {
        BufferedWriter fileWriter = null;
        try {
            GLOBAL_PROPERTIES.setProperty("jdbc.url",Environment.DB_URL);
            GLOBAL_PROPERTIES.setProperty("jdbc.username",Environment.DB_USERNAME);
            GLOBAL_PROPERTIES.setProperty("jdbc.password",Environment.DB_PASSWORD);
            GLOBAL_PROPERTIES.setProperty("sql.tpl",Environment.SQL_TPL);
            GLOBAL_PROPERTIES.setProperty("defualt.pwd",Environment.DEFUALT_PWD);
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(JDBC_PROPERTIES),"GBK"));
            GLOBAL_PROPERTIES.store(fileWriter,"保存参数");
            return "保存成功";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "保存失败!" + Util.exception2String(ex, 20);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

}
