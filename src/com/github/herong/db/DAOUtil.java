/**
 * 
 */
package com.github.herong.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

/**
 * TODO 这里用文字描述这个类的主要作用
 * 
 * @author herong
 * @createTime 2011-12-15 下午07:58:20
 * @modifier
 * @modifyDescription 描述本次修改内容
 * @see
 */

public class DAOUtil {
    /**
     * 获取业务统计信息
     * 
     * @return
     */
    public static List<Map<String, String>> getSqlRS(final String sql) {
        Connection con = null;
        try {
            con = MyDataSource.getConn();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCnt = rsmd.getColumnCount();
            List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
            while (rs.next()) {
                Map<String, String> row = new HashMap<String, String>(colCnt);
                for (int i = 1; i <= colCnt; i++) {
                    row.put(rsmd.getColumnName(i), String.valueOf(rs.getObject(i)));
                }
                rows.add(row);
            }

            return rows;
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
            }
        }
        return Collections.EMPTY_LIST;
    }

    public static boolean createData() {
        Connection con = null;
        try {
            String sql = "{call P_EventMonitor(?,?)}";

            try {
                con = MyDataSource.getConn();
                CallableStatement cs = con.prepareCall(sql);
                cs.registerOutParameter(1, Types.VARCHAR);
                cs.registerOutParameter(2, Types.VARCHAR);
                System.out.println(sql);
                cs.execute();
                String fhz = cs.getString(1);
                String msg = cs.getString(2);
                if ("1".equals(fhz)) {
                    con.commit();
                    return true;
                }
                System.out.println(msg);
                con.rollback();
                return false;
            } catch (NamingException e) {
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                e.printStackTrace();
            } catch (SQLException e) {
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }

        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
            }
        }
        return false;
    }

    public static void createDDL(String str) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Connection con = null;
        Statement  st = null;
        try {
            con = MyDataSource.getDirectConn();

            st = con.createStatement();
            st.execute(str);
            //System.out.println("---"+rs);
            SQLWarning sqlW = st.getWarnings();
           // System.out.println(sqlW.getSQLState()+":"+sqlW.getMessage());
           // printSQLException((SQLException) sqlW.getCause());
            //System.out.println(sqlW.getCause().getMessage());
            //con.commit();
            printWarnings(sqlW);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
            }
            
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                }
            }
            
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
       /* String procedure = "CREATE OR REPLACE PROCEDURE P_TEST ("
                          + " PI_MSG IN VARCHA "
                          + "    )"
                          + " IS "
                          + " BEGIN"
                          + " insert into test1(name) values(PI_MSG);"
                          + " EXCEPTION"
                          + " WHEN others THEN"
                          + " null; "
                          + " END; ";

        DAOUtil.createDDL(procedure);*/ 
        
        Connection conn =  null;
        PreparedStatement  ps = null;
        try {
            long s = System.currentTimeMillis();
            conn =  MyDataSource.getDirectConn();
            System.out.println("【获取数据库连接，耗时"+(System.currentTimeMillis() - s)+"(ms)】");
            testPreparedStatement(conn,"8008420");
            testPreparedStatement(conn,"8008460");
            testStatement(conn,"8008420");
            testStatement(conn,"8008460");
            
            ps = conn.prepareStatement("select AAB001,AAC001,AAC058,AAC002,CAC002,AAC003,AAC004,AAC006 from AC01 WHERE CAC002=? ");
            testPreparedStatementCache(conn,ps,"8008420");
            testPreparedStatementCache(conn,ps,"8008460");
            testPreparedStatementAAC001(conn,"228702");
            testStatementAAC001(conn,"228702");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
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
    }
    
    private static void testPreparedStatement(Connection conn,String CAC002) {

        PreparedStatement  ps = null;
        ResultSet rs = null;
        try {
            long s = System.currentTimeMillis();
            ps = conn.prepareStatement("select AAB001,AAC001,AAC058,AAC002,CAC002,AAC003,AAC004,AAC006 from AC01 WHERE CAC002=? ");
            ps.setString(1, CAC002);
            rs = ps.executeQuery();
            Map respMap = new HashMap<String,Object>(12);
            while (rs.next()) {
                for (int i =1 ; i <= 8 ;i++) {
                    respMap.put(String.valueOf(i), rs.getObject(i));
                }
            }
            
            System.out.println("【人员："+CAC002+" ，jdbc PreparedStatement查询耗时"+(System.currentTimeMillis() - s)+"(ms)】");
            respMap= null;
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
        
    }
    
    private static void testPreparedStatementAAC001(Connection conn,String CAC002) {

        PreparedStatement  ps = null;
        ResultSet rs = null;
        try {
            long s = System.currentTimeMillis();
            ps = conn.prepareStatement("select AAB001,AAC001,AAC058,AAC002,CAC002,AAC003,AAC004,AAC006 from AC01 WHERE AAC001=? ");
            ps.setLong(1, Long.parseLong(CAC002));
            rs = ps.executeQuery();
            Map respMap = new HashMap<String,Object>(12);
            while (rs.next()) {
                for (int i =1 ; i <= 8 ;i++) {
                    respMap.put(String.valueOf(i), rs.getObject(i));
                }
            }
            
            System.out.println("【人员："+CAC002+" ，jdbc PreparedStatement AAC001查询耗时"+(System.currentTimeMillis() - s)+"(ms)】");
            respMap= null;
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
        
    }
    
    private static void testPreparedStatementCache(Connection conn,PreparedStatement  ps,String CAC002) {

        ResultSet rs = null;
        try {
            long s = System.currentTimeMillis();

            ps.setString(1, CAC002);
            rs = ps.executeQuery();
            Map respMap = new HashMap<String,Object>(12);
            while (rs.next()) {
                for (int i =1 ; i <= 8 ;i++) {
                    respMap.put(String.valueOf(i), rs.getObject(i));
                }
            }
            
            System.out.println("【人员："+CAC002+" ，jdbc PreparedStatementCache查询耗时"+(System.currentTimeMillis() - s)+"(ms)】");
            respMap= null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            
        }
        
    }
    
     private static void testStatementAAC001(Connection conn,String CAC002) {
        Statement  ps = null;
        ResultSet rs = null;
        try {
            long s = System.currentTimeMillis();
            ps = conn.createStatement();
            rs = ps.executeQuery("select AAB001,AAC001,AAC058,AAC002,CAC002,AAC003,AAC004,AAC006 from AC01 WHERE AAC001="+CAC002+" ");
            Map respMap = new HashMap<String,Object>(12);
            while (rs.next()) {
                for (int i =1 ; i <= 8 ;i++) {
                    respMap.put(String.valueOf(i), rs.getObject(i));
                }
            }
            
            System.out.println("【人员："+CAC002+" ，jdbc Statement AAC001查询耗时"+(System.currentTimeMillis() - s)+"(ms)】");
            respMap= null;
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
        
    }
    
    private static void testStatement(Connection conn,String CAC002) {
        Statement  ps = null;
        ResultSet rs = null;
        try {
            long s = System.currentTimeMillis();
            ps = conn.createStatement();
            rs = ps.executeQuery("select AAB001,AAC001,AAC058,AAC002,CAC002,AAC003,AAC004,AAC006 from AC01 WHERE CAC002='"+CAC002+"' ");
            Map respMap = new HashMap<String,Object>(12);
            while (rs.next()) {
                for (int i =1 ; i <= 8 ;i++) {
                    respMap.put(String.valueOf(i), rs.getObject(i));
                }
            }
            
            System.out.println("【人员："+CAC002+" ，jdbc Statement查询耗时"+(System.currentTimeMillis() - s)+"(ms)】");
            respMap= null;
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
        
    }
    
    public static boolean ignoreSQLException(String sqlState) {
        if (sqlState == null) {
          System.out.println("The SQL state is not defined!");
          return false;
        }
        // X0Y32: Jar file already exists in schema
        if (sqlState.equalsIgnoreCase("X0Y32"))
          return true;
        // 42Y55: Table already exists in schema
        if (sqlState.equalsIgnoreCase("42Y55"))
          return true;
        return false;
      }
    
    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
          if (e instanceof SQLException) {
            if (ignoreSQLException(((SQLException)e).getSQLState()) == false) {
              e.printStackTrace(System.err);
              System.err.println("SQLState: " + ((SQLException)e).getSQLState());
              System.err.println("Error Code: " + ((SQLException)e).getErrorCode());
              System.err.println("Message: " + e.getMessage());
              Throwable t = ex.getCause();
              while (t != null) {
                System.out.println("Cause: " + t);
                t = t.getCause();
              }
            }
          }
        }
      }
    
    public static void printWarnings(SQLWarning warning) throws SQLException {
        if (warning != null) {
          System.out.println("\n---Warning---\n");
          while (warning != null) {
            System.out.println("Message: " + warning.getMessage());
            System.out.println("SQLState: " + warning.getSQLState());
            System.out.print("Vendor error code: ");
            System.out.println(warning.getErrorCode());
            System.out.println("");
            warning = warning.getNextWarning();
          }
        }
      }

}
