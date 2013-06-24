/**
 * 
 */
package com.github.herong.swt.encrypt;

import java.io.Serializable;

/**
 * TODO 这里用文字描述这个类的主要作用
 * @author herong
 * @createTime 2013-6-21 上午11:08:02
 * @modifier 
 * @modifyDescription 描述本次修改内容
 * @see
 */

public class EncryptPwd implements Serializable {
    private String id ;
    private String pwd;
    private String sql;
    
    /**
     * @return the sql
     */
    public String getSql() {
        return sql;
    }
    /**
     * @param sql the sql to set
     */
    public void setSql(String sql) {
        this.sql = sql;
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the pwd
     */
    public String getPwd() {
        return pwd;
    }
    /**
     * @param pwd the pwd to set
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
    
}
