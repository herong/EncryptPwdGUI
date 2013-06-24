/**
 * 
 */
package com.github.herong.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.github.herong.comm.Environment;

/**
 * TODO 这里用文字描述这个类的主要作用
 * 
 * @author herong
 * @createTime 2011-10-29 上午11:40:01
 * @modifier
 * @modifyDescription 描述本次修改内容
 * @see
 */

public class MyDataSource {

    private static DataSource ds;
    private static final ReentrantLock lock = new ReentrantLock();

    /*
     * static { try { init(); } catch (NamingException e) { // TODO
     * Auto-generated catch block e.printStackTrace(); } }
     */

    private static void init() throws NamingException {
        Context initContext = new InitialContext();
        String j2eeName = Environment.GLOBAL_PROPERTIES.getProperty("j2ee.container.name");
        String lookupname = "";
        if ("tomcat".equalsIgnoreCase(j2eeName)) {
            lookupname = "java:/comp/env";
        }
        Context envContext = (Context) initContext.lookup(lookupname);
        ds = (DataSource) envContext.lookup(Environment.GLOBAL_PROPERTIES.getProperty("jndi.name"));
    }

    /**
     * 获取第一个数据库连接
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    public static Connection getDirectConn() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Class csl = Class.forName(Environment.DB_JDBC_CLASS);
        lock.lock();
        try {
            return DriverManager.getConnection(Environment.DB_URL, Environment.DB_USERNAME, Environment.DB_PASSWORD);
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 获取第二个数据库连接
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    public static Connection getDirectConn2() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Class csl = Class.forName(Environment.DB_JDBC_CLASS2);
        lock.lock();
        try {
            return DriverManager.getConnection(Environment.DB_URL2, Environment.DB_USERNAME2, Environment.DB_PASSWORD2);
        } finally {
            lock.unlock();
        }
    }
    

    public static synchronized Connection getConn() throws NamingException, SQLException {
        if (ds == null) {
            init();
        }
        lock.lock();
        try {
            Connection con = ds.getConnection();
            con.setAutoCommit(false);
            System.out.println(con);
            return con;
        } finally {
            lock.unlock();
        }

    }
}
