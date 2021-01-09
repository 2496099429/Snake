package snake;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;

/**
 * 游戏的启动类
 */

//游戏的主启动类
public class StartGame {
    private static JFrame frameGame;//游戏界面
    private static JTextField txtUser;//登录界面账号
    private static JPasswordField jPasswordField;//登录界面密码
    private static JTextField txtUserRegister;//注册界面账号
    private static JPasswordField jPasswordFieldRegister;//注册界面密码
    private static JFrame frameLogon;//登录界面
    private static JFrame frameRegister;//注册界面
    private JButton buttonBack = new JButton("返回");
    private JButton buttonRegister = new JButton("注册");

    public static JTextField getTxtUser() {
        return txtUser;
    }

    public static JPasswordField getjPasswordField() {
        return jPasswordField;
    }

    public static JTextField getTxtUserRegister() {
        return txtUserRegister;
    }

    public static JPasswordField getjPasswordFieldRegister() {
        return jPasswordFieldRegister;
    }

    public static JFrame getFrameGame() {
        return frameGame;
    }

    public static void setFrameGame(JFrame frameGame) {
        StartGame.frameGame = frameGame;
    }

    public static JFrame getFrameLogon() {
        return frameLogon;
    }

    public static void setFrameLogon(JFrame frameLogon) {
        StartGame.frameLogon = frameLogon;
    }

    public static void main(String[] args) throws Exception{

        new Thread(){
            public void run(){
                SnakeMusic.getSdl().loop(Clip.LOOP_CONTINUOUSLY);//相当于加载
                SnakeMusic.getSdl().stop();
//
            }
        }.start();
        //画出登录界面
        new StartGame().logon();


    }

    /**
     * 登录界面
     */


    public void logon() {//登录界面
        frameLogon = new JFrame("贪吃蛇");

        frameLogon.setLayout(null);
        JButton button = new JButton("登录");
        button.setBounds(190,125,85,23);
        button.addActionListener(new LogonListener(this));

        JButton button0 = new JButton("注册");
        button0.setBounds(85,125,85,23);
        frameLogon.add(button0);
        button0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new StartGame().register();
                frameLogon.setVisible(false);
            }
        });
        JButton jButtonCancel = new JButton("注销账号");
        frameLogon.add(jButtonCancel);
        jButtonCancel.setBounds(85,160,85,23);
        jButtonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameLogon.setVisible(false);
                new CancelJFrame();
            }
        });
        JButton jButtonAdministrator = new JButton("修改密码");
        frameLogon.add(jButtonAdministrator);
        jButtonAdministrator.setBounds(190,160,85,23);
        jButtonAdministrator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UpdatePassword();
                frameLogon.setVisible(false);
            }
        });


        frameLogon.add(button);
        JLabel lblUser = new JLabel("帐    号：");
        JLabel lblPassword = new JLabel("密    码：");
        lblUser.setBounds(90,50,100,20);
        lblPassword.setBounds(90,90,100,20);
        txtUser = new JTextField(20);
        txtUser.setBounds(135,50,120,20);
        jPasswordField = new JPasswordField(20);
        jPasswordField.setBounds(135,90,120,20);
        //添加监听事件
        jPasswordField.addActionListener(new LogonListener(this));


        frameLogon.add(lblUser);

        frameLogon.add(lblPassword);
        frameLogon.add(txtUser);
        frameLogon.add(jPasswordField);





        Dimension screenSize = getScreenSize();
        frameLogon.setBounds((screenSize.width-380)/2,(screenSize.height-250)/2,380,250);
        frameLogon.setResizable(false);
        frameLogon.setVisible(true);
        frameLogon.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * 注册界面
     */


    public void register(){//注册界面


        frameRegister = new JFrame("注册");

        frameRegister.setLayout(null);
        buttonBack.setBounds(90,130,80,23);
        frameRegister.add(buttonBack);
        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameLogon.setVisible(true);
                frameRegister.dispose();
            }
        });
        buttonRegister.setBounds(190,130,80,23);
        buttonRegister.addActionListener(new RegisterListener(this));

        frameRegister.add(buttonRegister);
        JLabel lblUser = new JLabel("帐    号：");
        JLabel lblPassword = new JLabel("密    码：");
        lblUser.setBounds(90,50,100,20);
        lblPassword.setBounds(90,90,100,20);
        txtUserRegister = new JTextField(20);
        txtUserRegister.setBounds(135,50,120,20);
        jPasswordFieldRegister = new JPasswordField(20);
        jPasswordFieldRegister.setBounds(135,90,120,20);
//        jPasswordFieldRegister.addActionListener(new RegisterListener(this));
        frameRegister.add(lblUser);


        frameRegister.add(lblPassword);
        frameRegister.add(txtUserRegister);
        frameRegister.add(jPasswordFieldRegister);


        Dimension screenSize = getScreenSize();
        frameRegister.setBounds((screenSize.width-380)/2,(screenSize.height-250)/2,380,250);
        frameRegister.setResizable(false);
        frameRegister.setVisible(true);
        frameRegister.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    /**
     * 连接数据库，验证账号合法性，并添加账号到数据库
     * @param username 用户名
     * @param password 密码
     */

        //连接数据库,注册
    public void connectionRegister(String username,String password){
        JLabel jLabel1 = new JLabel();
        JLabel jLabel2 = new JLabel();
        JLabel jLabel3 = new JLabel();
        jLabel1.setForeground(Color.red);
        jLabel2.setForeground(Color.red);
        frameRegister.add(jLabel1);
        frameRegister.add(jLabel2);
        frameRegister.add(jLabel3);

        boolean ok = false;

        if(!username.equals("") && !password.equals("")){//判断账号密码是否为空
            if (jLabel1!=null) jLabel1.setVisible(false);

            ok = Dao.UserRegister(username,password);

            if (ok){
                if (jLabel2!=null) jLabel2.setVisible(false);
                jLabel3.setOpaque(true);
                jLabel3.setText("注册成功");
                updateTxt();//更新文本文件
//                read();
                jLabel3.setForeground(Color.BLACK);
                jLabel3.setBounds(155,28,200,20);
                buttonBack.setVisible(false);
                buttonRegister.setVisible(false);
                JButton jButtonBackLogon = new JButton("返回登录");
                frameRegister.add(jButtonBackLogon);
                jButtonBackLogon.setBounds(140,130,100,25);
                jButtonBackLogon.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frameRegister.dispose();
                        logon();
                    }
                });



            }else{//提示账号重复

                jLabel2.setOpaque(true);
                jLabel2.setText("此账号已存在");

                jLabel2.setBounds(135,28,200,20);
                jPasswordFieldRegister.setText("");
            }


        }else{//提示账号或密码不能为空
            jLabel1.setOpaque(true);//显示背景
            jLabel1.setText("账号或密码不能为空");
            jLabel1.setBounds(135,28,200,20);
            jPasswordFieldRegister.setText("");

        }

    }

    /**
     * 数据库查询，验证密码是否正确
     * @param username 用户名
     * @param password 密码
     */


        //连接数据库,登录
    public  void connectionLogon(String username,String password) {


        boolean ok = false;

        if (username.equals("") || password.equals(""))
        {
            JLabel jLabel = new JLabel("账号或密码不能为空\n");
            jPasswordField.setText("");
            frameLogon.add(jLabel);
            jLabel.setOpaque(true);
            jLabel.setBounds(135,30,250,20);
            jLabel.setForeground(Color.red);
        }else {
            ok = Dao.SelectUserLogon(username,password);
            if(ok){ //登录
                paint();
                frameLogon.dispose();
            }else{
                JLabel jLabel = new JLabel("账号或密码不正确\n");
                jPasswordField.setText("");
                frameLogon.add(jLabel);
                jLabel.setOpaque(true);
                jLabel.setBounds(135,30,250,20);
                jLabel.setForeground(Color.red);
            }
        }




    }

    /**
     * 游戏界面，画出游戏面板
     */

    public  void paint(){

        frameGame = new JFrame("贪吃蛇");
        Dimension screenSize = getScreenSize();
        frameGame.setBounds((screenSize.width-900)/2,(screenSize.height-720)/2,900,720);
        frameGame.setResizable(false);
        frameGame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(!GamePanel.isFail())
                GamePanel.connectionEnd();
                Data.update();
                System.exit(0);
            }
        });

        //正常游戏界面都在面板上！


        frameGame.add(new GamePanel());
        frameGame.setVisible(true);


    }


    /**
     * 获取屏幕尺寸
     * @return 屏幕尺寸
     */

    public static Dimension getScreenSize(){
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        return  screenSize;
    }

    public static class LogonListener implements ActionListener {//触发该事件时，获取登录界面文本框和密码框的账号和密码，并调用方法连接数据库进行验证

        private StartGame startGame;
        public LogonListener(StartGame startGame){
            this.startGame = startGame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {


            char [] res = startGame.getjPasswordField().getPassword();

            String password = new String(res);
            String username = startGame.getTxtUser().getText();
            startGame.connectionLogon(username,password);

        }
    }

    public static class RegisterListener implements ActionListener {//获取注册界面的账号和密码，连接数据库验证合法性
        private StartGame startGame;
        public RegisterListener(StartGame startGame){
            this.startGame = startGame;
        }
        @Override
        public void actionPerformed(ActionEvent e) {

            char [] res = startGame.getjPasswordFieldRegister().getPassword();
            String password = new String(res);
            String username = startGame.getTxtUserRegister().getText();
            startGame.connectionRegister(username,password);

        }
    }

    /**
     * 将数据库中数据以对象流的形式写入文件中
     * @param o 对象
     */

    public static void writeobject(Object o){
        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(new FileOutputStream("src\\statics\\Users.txt"));
            oos.writeObject(o);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将数据库的数据更新到文本文件中
     */

    public static void updateTxt(){
        ArrayList<Users> list = Dao.SelectUser();
        writeobject(list);
    }

    /**
     * 从文本文件中读取数据
     */
    public static void read(){
        ArrayList<Users> list = new ArrayList<>();

        list =  readobject();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

    }

    /**
     * 从文件中读取数据
     * @return 用户信息
     */
    public static ArrayList<Users> readobject(){
        ObjectInputStream ois = null;
        ArrayList<Users> list = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("src\\statics\\Users.txt"));
            list = (ArrayList<Users>)ois.readObject();
            ois.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return list;

    }

}



