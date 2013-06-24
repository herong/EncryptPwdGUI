/**
 * 
 */
package com.github.herong.swt.encrypt;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.github.herong.comm.Environment;
import com.github.herong.comm.Util;

/**
 * TODO 这里用文字描述这个类的主要作用
 * 
 * @author herong
 * @createTime 2013-6-21 上午10:14:53
 * @modifier
 * @modifyDescription 描述本次修改内容
 * @see
 */

public class MainWin {
    private static Text t_db_name;
    private static Text t_db_pwd;
    private static Text t_user_defual_pwd;
    private static Text t_db_url;
    private static Text t_user_count;
    private static Text t_sql;
    private static Text t_sql_tpl;

    /**
     * Launch the application.
     * 
     * @param args
     */
    public static void main(String[] args) {
        Display display = Display.getDefault();
        final Shell shlEncryptpwd = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
        shlEncryptpwd.setSize(775, 531);
        shlEncryptpwd.setText("系统密码工具");
        shlEncryptpwd.setLayout(null);
 
        Group group = new Group(shlEncryptpwd, SWT.NONE);
        group.setBounds(5, 5, 754, 121);
        group.setText("参数设置");

        Label label = new Label(group, SWT.NONE);
        label.setBounds(20, 49, 113, 17);
        label.setText("数据库查询用户名：");

        Label label_1 = new Label(group, SWT.NONE);
        label_1.setBounds(266, 49, 60, 17);
        label_1.setText("用户密码：");

        t_db_name = new Text(group, SWT.BORDER);
        t_db_name.setBounds(139, 47, 97, 23);

        t_db_pwd = new Text(group, SWT.BORDER | SWT.PASSWORD);
        t_db_pwd.setBounds(335, 47, 74, 23);

        Label label_2 = new Label(group, SWT.NONE);
        label_2.setBounds(242, 76, 84, 17);
        label_2.setText("初始化用户数：");

        t_user_defual_pwd = new Text(group, SWT.BORDER);
        t_user_defual_pwd.setBounds(139, 73, 97, 23);

        Label label_3 = new Label(group, SWT.NONE);
        label_3.setBounds(43, 72, 90, 17);
        label_3.setText("初始用户密码：");

        Label lblurl = new Label(group, SWT.NONE);
        lblurl.setBounds(56, 24, 74, 17);
        lblurl.setText("数据库URL：");

        t_db_url = new Text(group, SWT.BORDER);
        t_db_url.setBounds(137, 21, 374, 23);

        t_user_count = new Text(group, SWT.BORDER);
        t_user_count.setBounds(336, 76, 73, 23);

        final Button b_sc = new Button(group, SWT.NONE);
        b_sc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                b_sc.setEnabled(false);
                ParamsVO paramsVO = new ParamsVO();
                paramsVO.setDbUrl(t_db_url.getText());
                paramsVO.setDbUser(t_db_name.getText());
                paramsVO.setDbPwd(t_db_pwd.getText());
                paramsVO.setUserDefualtPwd(t_user_defual_pwd.getText());
                paramsVO.setCount(Integer.parseInt(t_user_count.getText()));
                paramsVO.setSqlTpl(t_sql_tpl.getText());
                t_sql.setText("");
                MessageBox messageBox = new MessageBox(shlEncryptpwd,SWT.OK | SWT.ICON_INFORMATION) ;
                messageBox.setText("提示");
           
                
                try {
                    List<EncryptPwd> pwdList = BatchEncrypt.zsksc(paramsVO);
                    if (!Util.isEmpty(paramsVO.getSqlTpl())) {
                        for (EncryptPwd pwd : pwdList) {
                            t_sql.append(pwd.getSql());
                            t_sql.append("\r\n");
                            t_sql.append("\r\n");
                        }
                    } else {
                        for (EncryptPwd pwd : pwdList) {
                            t_sql.append(pwd.getId() + "," + pwd.getPwd());
                            t_sql.append("\r\n");
                            t_sql.append("\r\n");
                        }
                    }
                    
                    messageBox.setMessage("生成成功!");
                   
                    
                } catch (Exception e1) {
                    messageBox.setMessage("生成失败!");
                    e1.printStackTrace();
                    t_sql.setText(Util.exception2String(e1, 50));
                } 
                messageBox.open();
               
                b_sc.setEnabled(true);
                
            }
        });

        b_sc.setBounds(639, 71, 80, 27);
        b_sc.setText("生成");

        Button btnsql = new Button(group, SWT.NONE);
        btnsql.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                t_sql_tpl.setText("");
            }
        });
        btnsql.setBounds(533, 71, 80, 27);
        btnsql.setText("清除SQL模板");
        
                Group grpSql = new Group(shlEncryptpwd, SWT.NONE);
                grpSql.setBounds(5, 131, 754, 167);
                grpSql.setText("SQL模板");
                
                        t_sql_tpl = new Text(grpSql, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
                        t_sql_tpl.setBounds(10, 21, 734, 126);
                        t_sql_tpl.setText(Environment.SQL_TPL);

        Group group_1 = new Group(shlEncryptpwd, SWT.NONE);
        group_1.setBounds(5, 303, 754, 211);
        group_1.setText("生成数据");

        t_sql = new Text(group_1, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
        t_sql.setBounds(10, 20, 734, 171);
        t_db_url.setText(Environment.DB_URL);
        t_db_name.setText(Environment.DB_USERNAME);
        t_db_pwd.setText(Environment.DB_PASSWORD);
        t_user_defual_pwd.setText(Environment.DEFUALT_PWD);
        t_user_count.setText("1");
        
        Button button = new Button(group, SWT.NONE);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                Environment.DB_URL  = t_db_url.getText();
                Environment.DB_USERNAME = t_db_name.getText();
                Environment.DB_PASSWORD = t_db_pwd.getText();
                Environment.SQL_TPL = t_sql_tpl.getText();
                Environment.DEFUALT_PWD = t_user_defual_pwd.getText();
                String msg = Environment.save();
                MessageBox messageBox = new MessageBox(shlEncryptpwd,SWT.OK  | SWT.ICON_INFORMATION);
                messageBox.setMessage(msg);
                messageBox.setText("提示");
                messageBox.open();
            }
        });
        button.setBounds(420, 71, 80, 27);
        button.setText("保存参数");
        shlEncryptpwd.open();
        shlEncryptpwd.layout();
        while (!shlEncryptpwd.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
  
}
