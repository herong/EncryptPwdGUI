/**
 * 
 */
package com.github.herong.comm;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Enumeration;

/**
 * TODO 这里用文字描述这个类的主要作用
 * 
 * @author herong
 * @createTime 2011-12-13 下午06:17:52
 * @modifier
 * @modifyDescription 描述本次修改内容
 * @see
 */

public class Util {

    
    public static String nvl(Object o) {
        if (o == null) {
            return "";
        }
        return String.valueOf(o);
    }
    /**
     * Number 转换成 String 类型
     * 
     * <pre>
     *  e.g:
     *     String str = Util.number2String(999999998.066);
     * </pre>
     * 
     * @param n
     *            {@code Number} 类型值
     * @return 转换并格式化后的字符串,只保留两位小数
     * @see #number2String(Number,String)
     */
    public static String number2String(final Number n) {
        return number2String(n, null);
    }

    /**
     * Number 转换成 String 类型,自定格式
     * 
     * <pre>
     *  e.g:
     *     String str = Util.number2String(999999998.066,"0.##");
     * </pre>
     * 
     * @param n
     *            {@code Number} 类型值
     * @param fmt
     *            格式串，如不指定则默认为0.##(保留两位小数，并做四舍五入) 参考 {@code DecimalFormat}
     * @return 转换并格式化后的字符串
     * @see BigDecimal
     * @see DecimalFormat
     */
    public static String number2String(final Number n, final String fmt) {
        DecimalFormat df = new DecimalFormat();
        String format = fmt;
        if (fmt == null) {
            format = "0.####";
        }
        df.applyPattern(format);
        return df.format(n);
    }

    public static InputStream getResourceAsStream(String resource) throws Exception {
        String stripped = resource.startsWith("/") ? resource.substring(1) : resource;

        InputStream stream = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            stream = classLoader.getResourceAsStream(stripped);
        }
        if (stream == null) {
            stream = Environment.class.getResourceAsStream(resource);
        }
        if (stream == null) {
            stream = Environment.class.getClassLoader().getResourceAsStream(stripped);
        }
        if (stream == null) {
            throw new Exception(resource + " not found");
        }
        return stream;
    }

    /**
     * 查找类路径获取文件的绝路径
     * 
     * @param resource
     * @return
     * @throws Exception
     */
    public static URL getResourcePath(String resource) throws Exception {
        String stripped = resource.startsWith("/") ? resource.substring(1) : resource;

        URL stream = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            stream = classLoader.getResource(stripped);
        }
        if (stream == null) {
            stream = Environment.class.getResource(resource);
        }
        if (stream == null) {
            stream = Environment.class.getClassLoader().getResource(stripped);
        }
        if (stream == null) {
            throw new Exception(resource + " not found");
        }
        return stream;
    }
    
    /**
     *  判断是否为空、空中、空格串
     * @param str 要判断的字符串对象
     * @return boolean true ： 空、空中、空格串，反之为false
     */
    public static boolean isEmpty(String str){
        return str == null || "".equals(str.trim());
    }
    
    public static String getLocalIp() {
        String localIp = "";
        InetAddress ia;
        try {
            ia = InetAddress.getLocalHost();
            localIp = ia.getHostAddress();
            
        } catch (UnknownHostException e) {
        }
        
        return localIp;
    }
    
    
    /**
     * 获取本机IP地址(IPV4)
     * 
     * @return 本机IP地址
     * @throws Exception 
     * @throws SocketException
     * 
     */
    public static String getLocalIp2() throws Exception  {
        try{
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            InetAddress ip = null;
            String ips = "";
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces
                        .nextElement();
                // System.out.println(ni.getName());
                Enumeration<InetAddress> iptiems = ni.getInetAddresses();
                while (iptiems.hasMoreElements()) {
                    ip = (InetAddress) iptiems.nextElement();
                    ips = ip.getHostAddress();
                    // System.out.println(ips+","+ip.isSiteLocalAddress() +
                    // ","+ip.isLoopbackAddress());
                    if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                            && ips.indexOf(":") == -1) {
                        // System.out.println("本机的ip=" + ips);
                        return ips;
                    }
                }
            }
        }catch (Exception e) {
            throw new Exception("获取服务器ip出错",e);
        }
        
        return "";
    }
    
    /**
     * 将异常堆栈转换成字符串
     * @param e 异常
     * @param depth 堆栈深度
     * @return 堆栈字符串
     */
    public static String exception2String(Throwable e,int depth) {
        StringBuffer ex = new StringBuffer();
        int i = 1 ;
        if (e != null ) {
            ex.append(e.toString()).append("\r\n");
            StackTraceElement[] st = e.getStackTrace();
            for (StackTraceElement ste : st) {
                i ++;
                ex.append(ste.toString());
                if (depth != -1 && i > depth) {
                    break;
                }
            }
        }
        return ex.toString();
    }
    public static void main(String[] args) {
        System.out.println(getLocalIp());
    }
}
