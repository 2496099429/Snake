package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * 注销账号界面
 */
public class CancelJFrame extends JFrame {



    private static JTextField jTextUsername;
    private static JPasswordField jPasswordField;
    private static JLabel lblUsername;
    private static JLabel lblPassword;
    private static JButton jButton;
    private static JButton jButtonBack;
    private static JButton jButtonBackLogon;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;

    /**
     * 注销账号界面
     */
    public CancelJFrame(){

        setTitle("注销账号");
        lblUsername = new JLabel("账    号  ：");
        lblPassword = new JLabel("密    码  ：");
        lblUsername.setBounds(90,50,100,20);
        lblPassword.setBounds(90,90,100,20);

        jTextUsername = new JTextField(20);
        jPasswordField = new JPasswordField(20);
        jTextUsername.setBounds(140,50,120,20);
        jPasswordField.setBounds(140,90,120,20);

        jButton = new JButton("确认注销");
        jButtonBack = new JButton("返回");
        jButtonBack.setBounds(90,130,85,23);
        jButton.setBounds(190,130,85,23);


        add(lblUsername);
        add(lblPassword);

        add(jTextUsername);
        add(jPasswordField);

        add(jButton);
        add(jButtonBack);

        jButtonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                StartGame.getFrameLogon().setVisible(true);
            }
        });


        jButton.addActionListener(new DeleteListener());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        Dimension screenSize = StartGame.getScreenSize();
        setBounds((screenSize.width-380)/2,(screenSize.height-250)/2,380,250);
        setResizable(false);
        setVisible(true);
    }

    /**
     * 连接数据库删除账号
     * @param username 用户名
     * @param password 密码
     */

    public boolean connDelete(String username,String password){

        boolean status = false;
        if (!username.equals("") && !password.equals("")){
            if (jLabel1 != null){
                jLabel1.setVisible(false);
            }
            if (Dao.SelectUserLogon(username)){
                if (jLabel2!=null) jLabel2.setVisible(false);
                if (Dao.SelectUserLogon(username,password)){
                    if (jLabel3!=null) jLabel3.setVisible(false);
                    Dao.DelUser(username,password);
                    status = true;
                    jLabel4 = new JLabel("注销成功");
                    jLabel4.setText("注销成功");
                    jLabel4.setOpaque(true);
                    jLabel4.setForeground(Color.BLACK);
                    add(jLabel4);
                    jLabel4.setBounds(150,28,200,20);
                    jButton.setVisible(false);
                    jButtonBack.setVisible(false);
                    jButtonBackLogon = new JButton("返回登录");
                    add(jButtonBackLogon);
                    jButtonBackLogon.setBounds(140,130,100,25);
                    jButtonBackLogon.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dispose();
                            StartGame.getFrameLogon().setVisible(true);
                        }
                    });


                }else{
                    jLabel3 = new JLabel("账号或密码不正确");
                    jLabel3.setOpaque(true);
                    jLabel3.setForeground(Color.red);
                    add(jLabel3);
                    jLabel3.setBounds(145,28,200,20);

                }


            }else{
                jLabel2 = new JLabel("账号不存在");
                jLabel2.setOpaque(true);
                jLabel2.setForeground(Color.red);
                add(jLabel2);
                jLabel2.setBounds(145,28,200,20);


            }

        }else{
            jLabel1 = new JLabel("账号或密码不能为空");
            jLabel1.setOpaque(true);
            jLabel1.setForeground(Color.red);
            add(jLabel1);
            jLabel1.setBounds(145,28,200,20);

        }

        return true;
    }

    public class DeleteListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String username = jTextUsername.getText();
            char [] ch = jPasswordField.getPassword();
            String password = new String(ch);
            connDelete(username,password);
        }
    }

}
