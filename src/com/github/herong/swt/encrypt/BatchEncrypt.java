/**
 * 
 */
package com.github.herong.swt.encrypt;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.herong.comm.Environment;
import com.github.herong.comm.Util;
import com.github.herong.db.MyDataSource;

/**
 * TODO 这里用文字描述这个类的主要作用
 * 
 * @author herong
 * @createTime 2012-12-12 下午05:34:01
 * @modifier
 * @modifyDescription 描述本次修改内容
 * @see
 */

public class BatchEncrypt {

    public static void main(String[] args) {
        String sqlTpl = "insert into fw_operator (BAE001, OPERUNITID, OPERID, OPERNAME, OPERTYPE, OPERTYPE2, LOGINID, PASSWORD, PWENCRYPT, PWMODIFIED, EMAIL, CONTACTPHONE, CONTACTADDR, MEMO, AAE100, BAE006, BAE002, BAE003, POSITION, LOGINFAIL, LOGINTIME) "
                +
                " values ('441901', 711006,{ID}, '711006', '3', '10', '711006_ZZZD', '{PWD}', '1', '1', '', '', '', '', '1', '441901', '711006', 20130620120000, '', null, null);"
                +
                "insert into fw_operator (BAE001, OPERUNITID, OPERID, OPERNAME, OPERTYPE, OPERTYPE2, LOGINID, PASSWORD, PWENCRYPT, PWMODIFIED, EMAIL, CONTACTPHONE, CONTACTADDR, MEMO, AAE100, BAE006, BAE002, BAE003, POSITION, LOGINFAIL, LOGINTIME) "
                +
                " values ('441901', 711006,{ID}, '711006', '3', '10', '711006_ZZZD', '{PWD}', '1', '1', '', '', '', '', '1', '441901', '711006', 20130620120000, '', null, null);"
                +
                "insert into fw_operator (BAE001, OPERUNITID, OPERID, OPERNAME, OPERTYPE, OPERTYPE2, LOGINID, PASSWORD, PWENCRYPT, PWMODIFIED, EMAIL, CONTACTPHONE, CONTACTADDR, MEMO, AAE100, BAE006, BAE002, BAE003, POSITION, LOGINFAIL, LOGINTIME) "
                +
                " values ('441901', 711006,{ID}, '711006', '3', '10', '711006_ZZZD', '{PWD}', '1', '1', '', '', '', '', '1', '441901', '711006', 20130620120000, '', null, null);"
                +
                "insert into fw_operator (BAE001, OPERUNITID, OPERID, OPERNAME, OPERTYPE, OPERTYPE2, LOGINID, PASSWORD, PWENCRYPT, PWMODIFIED, EMAIL, CONTACTPHONE, CONTACTADDR, MEMO, AAE100, BAE006, BAE002, BAE003, POSITION, LOGINFAIL, LOGINTIME) "
                +
                " values ('441901', 711006,{ID}, '711006', '3', '10', '711006_ZZZD', '{PWD}', '1', '1', '', '', '', '', '1', '441901', '711006', 20130620120000, '', null, null);";
        List<EncryptPwd> pwdList = new ArrayList<EncryptPwd>();
        EncryptPwd encryptPwd = new EncryptPwd();
        encryptPwd.setId("111111111");
        encryptPwd.setPwd("bbbbbb");
        pwdList.add(encryptPwd);
        pwdList.add(encryptPwd);
        pwdList.add(encryptPwd);
        pwdList.add(encryptPwd);
        sqlIns(sqlTpl, pwdList);
        for (EncryptPwd sql : pwdList) {
            System.out.println(sql.getSql());
        }
    }

    private static List<EncryptPwd> makePwd(Connection conn, String pwd, int count) throws Exception {
        List<EncryptPwd> pwdList = new ArrayList<EncryptPwd>();
        while (count > 0) {
            String operid = getOperId(conn);
            String encryptPwd = md5(operid, pwd);
            EncryptPwd idEntry = new EncryptPwd();
            idEntry.setId(operid);
            idEntry.setPwd(encryptPwd);
            pwdList.add(idEntry);
            // System.out.println(operid + "," + encryptPwd);
            count--;
        }

        return pwdList;
    }

    private static void init(ParamsVO paramsVO) {
        Environment.DB_URL = paramsVO.getDbUrl();
        Environment.DB_USERNAME = paramsVO.getDbUser();
        Environment.DB_PASSWORD = paramsVO.getDbPwd();
    }

    public static List<EncryptPwd> zsksc(ParamsVO paramsVO) throws Exception {

        Connection conn = null;
        PreparedStatement ps = null;
        List<EncryptPwd> pwdList = Collections.EMPTY_LIST;
        try {
            init(paramsVO);
            long s = System.currentTimeMillis();

            conn = MyDataSource.getDirectConn();
            conn.setAutoCommit(false);

            System.out.println("/---------开始生成---------/");

            pwdList = makePwd(conn, paramsVO.getUserDefualtPwd(), paramsVO.getCount());
            if (!Util.isEmpty(paramsVO.getSqlTpl())) {
                sqlIns(paramsVO.getSqlTpl(), pwdList);
            }
            System.out.println("/---------生成完成---------/");
            System.out.println("【耗时" + (System.currentTimeMillis() - s) + "(ms)】");
            conn.commit();

        } catch (Exception ex) {
            if (conn != null)
                conn.rollback();
            throw ex;
        } finally {

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }

        }

        return pwdList;
    }

    private static String getOperId(Connection conn) {
        Statement ps = null;
        ResultSet rs = null;
        try {

            ps = conn.createStatement();
            rs = ps.executeQuery("select to_char(seq_fw_operid.nextval) operid from dual");
            String operId = "";
            while (rs.next()) {
                operId = rs.getString(1);
            }

            // System.out.println("operId："+operId);
            return operId;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }

        }
        return null;

    }

    /**
     * md5 加密
     * 
     * @param key
     *            加密key
     * @param data
     *            明文
     * @return 密文
     * @throws Exception
     */
    private static String md5(String key, String data) throws Exception {
        String Encrypt = "";

        if (Util.isEmpty(data)) {
            throw new Exception("要加密数据不能为空");
        }
        String tem = Util.nvl(key) + data.trim();
        try {
            byte[] defaultBytes = tem.getBytes();

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(defaultBytes);
            byte messageDigest[] = md.digest();

            // Encrypt = toHex(messageDigest);

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                // System.out.println( messageDigest[i] + " " +
                // Integer.toHexString(0xFF & messageDigest[i]));
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }

            Encrypt = hexString.toString();

        } catch (Exception e) {
            throw new Exception("密码加密出现异常", e);
        }

        return Encrypt;
    }

    private static String toHex(byte[] tmp) {
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

        char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
        // 所以表示成 16 进制需要 32 个字符
        int k = 0; // 表示转换结果中对应的字符位置
        for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
            // 转换成 16 进制字符的转换
            byte byte0 = tmp[i]; // 取第 i 个字节
            str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
            // >>> 为逻辑右移，将符号位一起右移
            str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
        }
        return new String(str); // 换后的结果转换为字符串

    }

    public static void sqlIns(String sqlTpl, List<EncryptPwd> pwdList) {
        String[] sqlTplItem = sqlTpl.trim().split(";");
        int index = 0;
        List<String> sqlList = new ArrayList<String>(pwdList.size());
        if (sqlTplItem.length == 1 && pwdList.size() != 1) {
            String sql = sqlTpl;
            for (EncryptPwd pwd : pwdList) {
                String sqltmp = sql.replaceFirst("\\{ID\\}", pwd.getId()).replaceFirst("\\{PWD\\}", pwd.getPwd());
                pwd.setSql(sqltmp);
                sqlList.add(sqltmp);

            }
        } else {
            for (String sql : sqlTplItem) {
                EncryptPwd pwdEntry = pwdList.get(index++);
                sql = sql.replaceFirst("\\{ID\\}", pwdEntry.getId()).replaceFirst("\\{PWD\\}", pwdEntry.getPwd());
                pwdEntry.setSql(sql+";");
                sqlList.add(sql);
    
            }
        }
        return;
    }
}
