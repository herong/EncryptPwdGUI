/**
 * 
 */
package com.github.herong.swt.encrypt;

import java.io.Serializable;

/**
 * TODO 这里用文字描述这个类的主要作用
 * @author herong
 * @createTime 2013-6-21 上午11:05:42
 * @modifier 
 * @modifyDescription 描述本次修改内容
 * @see
 */

public class ParamsVO implements Serializable {
    private String dbUrl ;
    private String dbUser ;
    private String dbPwd;
    private String userDefualtPwd;
    private int count;
    private String sqlTpl;
    
    
    /**
     * @return the sqlTpl
     */
    public String getSqlTpl() {
        return sqlTpl;
    }
    /**
     * @param sqlTpl the sqlTpl to set
     */
    public void setSqlTpl(String sqlTpl) {
        this.sqlTpl = sqlTpl;
    }
    /**
     * @return the dbUrl
     */
    public String getDbUrl() {
        return dbUrl;
    }
    /**
     * @param dbUrl the dbUrl to set
     */
    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }
    /**
     * @return the dbUser
     */
    public String getDbUser() {
        return dbUser;
    }
    /**
     * @param dbUser the dbUser to set
     */
    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }
    /**
     * @return the dbPwd
     */
    public String getDbPwd() {
        return dbPwd;
    }
    /**
     * @param dbPwd the dbPwd to set
     */
    public void setDbPwd(String dbPwd) {
        this.dbPwd = dbPwd;
    }
    /**
     * @return the userDefualtPwd
     */
    public String getUserDefualtPwd() {
        return userDefualtPwd;
    }
    /**
     * @param userDefualtPwd the userDefualtPwd to set
     */
    public void setUserDefualtPwd(String userDefualtPwd) {
        this.userDefualtPwd = userDefualtPwd;
    }
    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }
    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }
    
}
