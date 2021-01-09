package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 修改密码界面
 */
public class UpdatePassword extends JFrame {

    private static JLabel lblUsername;
    private static JLabel lblPasswordOld;
    private static JLabel lblPasswordNew;
    private static JLabel lblPasswordAgain;
    private static JTextField jTextUsername;
    private static JPasswordField jPasswordFieldOld;
    private static JPasswordField jPasswordFieldNew;
    private static JPasswordField jPasswordFieldAgain;
    private static JButton jButton;
    private static JButton jButtonBack;

    private static JButton jButtonBackLogon;
    private static JLabel jLabelUsername;
    private static JLabel jLabelPassword;
    private static JLabel jLabelNull;
    private static JLabel jLabelCom;
    private static JLabel jLabelOK;


    /**
     *修改密码界面
     *
     */

    public UpdatePassword(){
        setTitle("修改密码");
        lblUsername = new JLabel("账    号    ：");
        lblPasswordOld = new JLabel("原 密 码  ：");
        lblPasswordNew = new JLabel("新 密 码  ：");
        lblPasswordAgain = new JLabel("确认密码：");
        lblUsername.setBounds(80,40,100,20);
        lblPasswordOld.setBounds(80,70,100,20);
        lblPasswordNew.setBounds(80,100,100,20);
        lblPasswordAgain.setBounds(80,130,100,20);


        jTextUsername = new JTextField(20);
        jPasswordFieldOld = new JPasswordField(20);
        jPasswordFieldNew = new JPasswordField(20);
        jPasswordFieldAgain = new JPasswordField();
        jTextUsername.setBounds(140,40,120,20);
        jPasswordFieldOld.setBounds(140,70,120,20);
        jPasswordFieldNew.setBounds(140,100,120,20);
        jPasswordFieldAgain.setBounds(140,130,120,20);

        add(lblUsername);
        add(lblPasswordOld);
        add(lblPasswordNew);
        add(lblPasswordAgain);

        add(jTextUsername);
        add(jPasswordFieldOld);
        add(jPasswordFieldNew);
        add(jPasswordFieldAgain);

        jButton = new JButton("确认修改");
        jButtonBack = new JButton("返回");
        jButtonBack.setBounds(80,160,85,23);
        jButton.setBounds(178,160,85,23);
        add(jButton);
        add(jButtonBack);
        jButtonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StartGame.getFrameLogon().setVisible(true);
                dispose();
            }
        });
        jPasswordFieldAgain.addActionListener(new UpdatePasswordListener());
        jButton.addActionListener(new UpdatePasswordListener());


        setResizable(false);
        setLayout(null);
        Dimension screenSize = StartGame.getScreenSize();
        setBounds((screenSize.width-400)/2,(screenSize.height-270)/2,380,270);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * 连接数据库修改密码
     * @param username 用户名
     * @param PasswordOld 原密码
     * @param PasswordNew 新密码
     * @param PasswordAgain 确认密码
     */

    public void connUpdate(String username,String PasswordOld,String PasswordNew,String PasswordAgain){

        if (Dao.SelectUserLogon(username)){
            if (jLabelUsername != null)
                jLabelUsername.setText("");
            if (Dao.SelectUserLogon(username,PasswordOld)){
                if (jLabelPassword != null)
                    jLabelPassword.setText("");

                if (!PasswordNew.equals("")){
                    if (jLabelNull != null)
                        jLabelNull.setText("");

                    if (PasswordNew.equals(PasswordAgain)){
                        if (jLabelCom != null)
                            jLabelCom.setText("");
                        Dao.UpDateUser(username,PasswordNew);
                        jLabelOK = new JLabel("修改成功");
                        jLabelOK.setForeground(Color.BLACK);
                        add(jLabelOK);
                        jLabelOK.setBounds(150,10,70,20);

                        jButton.setVisible(false);
                        jButtonBack.setVisible(false);
                        jButtonBackLogon = new JButton("返回登录");
                        jButtonBackLogon.setBounds(140,160,95,25);
                        add(jButtonBackLogon);
                        jButtonBackLogon.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                dispose();
                                StartGame.getFrameLogon().setVisible(true);
                            }
                        });


                    }else{
                        jLabelCom = new JLabel("与新密码不同");
                        jLabelCom.setOpaque(true);
                        jLabelCom.setForeground(Color.red);
                        add(jLabelCom);
                        jLabelCom.setBounds(270,127,100,20);

                    }



                }else{
                    jLabelNull = new JLabel("密码不能为空");
                    jLabelNull.setOpaque(true);
                    jLabelNull.setForeground(Color.red);
                    add(jLabelNull);
                    jLabelNull.setBounds(270,97,100,20);
                }


            }else{
                jLabelPassword = new JLabel("账号或密码不正确");
                jLabelPassword.setOpaque(true);
                jLabelPassword.setForeground(Color.red);
                add(jLabelPassword);
                jLabelPassword.setBounds(270,67,100,20);
            }


        }else{

            jLabelUsername = new JLabel("账号不存在");
            jLabelUsername.setOpaque(true);
            jLabelUsername.setForeground(Color.red);
            add(jLabelUsername);
            jLabelUsername.setBounds(270,37,100,20);

        }


    }

    public  class UpdatePasswordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = jTextUsername.getText();
            char [] chPasswordOld = jPasswordFieldOld.getPassword();
            String PasswordOld = new String(chPasswordOld);
            char [] chPasswordNew = jPasswordFieldNew.getPassword();
            String PasswordNew = new String(chPasswordNew);
            char [] chPasswordAgain = jPasswordFieldAgain.getPassword();
            String PasswordAgain = new String(chPasswordAgain);
            connUpdate(username,PasswordOld,PasswordNew,PasswordAgain);


        }
    }
}
