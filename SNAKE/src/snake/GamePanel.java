package snake;

import utils.JDBCUtils;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * 游戏面板
 */
public class GamePanel  extends JPanel implements KeyListener, ActionListener {

    //定义蛇的数据结构
    private static boolean music = true;//音乐是否打开，默认关闭，初始化打开
    private static int length;//蛇的长度
    private static int score;//积分
    private int [] snakeX = new int[1000];//蛇的X坐标：25*25
    private int [] snakeY = new int[1000];//蛇的Y坐标：25*25
    private String fx;//初始方向向右
    private boolean start = false;//默认是不开始
    private static boolean max = false;//是否创造新纪录
    private static int scoremax = 0;//记录最高成绩
    private static boolean fail = false;
    private static boolean rank = true;//判断是否打开排行榜,默认是游戏界面
    private static int page = 0;//排行榜翻页
    //食物的坐标
    private int foodx;
    private int foody;
    private Random random = new Random();
    private static ArrayList<Ranks> ranks = new ArrayList<>();//存储排行榜
    private  int level = 0;//难度等级
    private static boolean Plug = false;//是否打开脚本


    //定时器
    private Timer timer = new Timer(90,this);//100毫秒执行一次


    private JButton jButtonBack = new JButton("返回");//排行榜返回按钮
    private JButton jButtonMusicLoop = new JButton();//音乐播放按钮
    private JButton jButtonMusicStop = new JButton();//音乐暂停按钮
    private JButton jButtonRank = new JButton("排行榜");
    JButton jButtonPlug = new JButton("开启防撞");
    private JButton jButtonNext = new JButton("下一页");
    private JButton jButtonLast = new JButton("上一页");
    private JButton jButtonFirst = new JButton("首页");

    private static int [][] w = new int[40][40];
    private static LinkedList<Point> queue = new LinkedList<>();
    private static boolean flag = false;
    private static LinkedList<Point> list = new LinkedList<>();

    public static boolean isFail() {
        return fail;
    }

    public static boolean isMusic() {
        return music;
    }

    public static void setMusic(boolean music) {
        GamePanel.music = music;
    }

    public static boolean isPlug() {
        return Plug;
    }

    public static void setPlug(boolean plug) {

        Plug = plug;
    }

    /**
     * 画出游戏面板
     */

    public GamePanel(){

        Data.init();//配置初始化
        setLayout(null);
        setSize(900,720);


        //获得焦点和键盘事件
        this.setFocusable(true);
        addKeyListener(this);
        rank();//获取最高分
        jButtonRank.addActionListener(new RankListener());
        jButtonRank.setBounds(210,38,70,23);//排行榜位置
        jButtonBack.addActionListener(new RankListener());
        jButtonBack.setBounds(725,623,70,23);//排行榜返回键
        jButtonBack.setVisible(false);

        jButtonLast.setBounds(340,623,70,23);
        jButtonNext.setBounds(540,623,70,23);
        jButtonFirst.setBounds(200,623,70,23);

        jButtonFirst.setVisible(false);
        jButtonNext.setVisible(false);
        jButtonLast.setVisible(false);

        jButtonFirst.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                page = 0;
                jButtonFirst.setVisible(false);
                jButtonFirst.setVisible(true);
            }
        });

        jButtonLast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (page>0){
                    page--;
                }
                jButtonLast.setVisible(false);
                jButtonLast.setVisible(true);
            }
        });

        jButtonNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                page++;
                jButtonNext.setVisible(false);
                jButtonNext.setVisible(true);
            }
        });

        this.add(jButtonFirst);
        this.add(jButtonNext);
        this.add(jButtonLast);
        this.add(jButtonRank);
        this.add(jButtonBack);

        jButtonMusicLoop.setIcon(Data.musicLoop);
        jButtonMusicLoop.setBounds(670,8,70,55);
        jButtonMusicLoop.addActionListener(new MusicListener());
        this.add(jButtonMusicLoop);
        jButtonMusicLoop.setVisible(false);
        jButtonMusicStop.setIcon(Data.musicStop);
        jButtonMusicStop.setBounds(670,8,70,55);
        jButtonMusicStop.addActionListener(new MusicListener());
        this.add(jButtonMusicStop);


        jButtonMusicLoop.setToolTipText("播放");
        jButtonMusicStop.setToolTipText("暂停");

        this.add(jButtonPlug);
        jButtonPlug.setBounds(572,38,83,23);
        jButtonPlug.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Plug = !Plug;

                if (Plug){
                    jButtonPlug.setText("关闭防撞");
                }else{
                    jButtonPlug.setText("开启防撞");
                }
                jButtonPlug.setVisible(false);
                jButtonPlug.setVisible(true);
            }
        });

        init();
        timer.start();//以指定的间隔触发一个或多个ActionEvent



    }

    /**
     * 游戏数据初始化
     */

    public void init(){


//      SnakeMusic.sdl.setMicrosecondPosition(0);
        SnakeMusic.getSdl().loop(Clip.LOOP_CONTINUOUSLY);
        SnakeMusic.getSdl().stop();

        musicSwitch();
        level = 0;
        length = 3;
        score = 0;
        max = false;
        timer.setDelay(90);
        snakeX[0] = 100;snakeY[0] = 100;//蛇的脑袋
        snakeX[1] = 75;snakeY[1] = 100;
        snakeX[2] = 50;snakeY[2] = 100;
        fx = "R";
        foodx = 25+25*random.nextInt(34);
        foody = 75+25*random.nextInt(24);
        for (int i = 0; i < length; i++) {
            if (foodx == snakeX[i] && foody == snakeY[i]){
                foodx = 25+25*random.nextInt(34);
                foody = 75+25*random.nextInt(24);
                i = 0;
            }
        }



    }

    /**
     * 绘制面板
     * @param g 画笔
     */


    //绘制面板,游戏中的所有东西，都使用这个画笔来画
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);//清屏

        //绘制静态的面板
        Color c = g.getColor();
        g.setColor(Color.green);
            /*URL url = GamePanel.class.getResource("/statics/star.png");//表示当前src目录
            //        String url = "GUI/statics/star.png";
            ImageIcon icon = new ImageIcon(url);*/
        Data.header.paintIcon(this,g,25,11);

        g.setColor(c);
        g.fillRect(25,75,850,600);

        //画积分
        g.setColor(Color.white);
        g.setFont(new Font("微软雅黑",Font.BOLD,20));
        g.drawString("长度"+length,750,30);
        g.drawString("分数"+score,750,55);
        g.drawString("用户："+StartGame.getTxtUser().getText(),67,30);
        g.drawString("最高分："+scoremax,67,55);
        g.setColor(Color.MAGENTA);
        g.drawString("难度："+(90-timer.getDelay())/2,577,29);

        g.setColor(Color.red);
        g.setFont(new Font("微软雅黑",Font.BOLD,40));
        g.drawString("贪",900/2-100-30,54);
        g.drawString("吃",900/2-30,54);
        g.drawString("蛇",900/2+100-30,54);

        if(rank){

            //画小蛇
            if (fx.equals("R")){
                g.fillRect(snakeX[0],snakeY[0],23,23);
            }else if (fx.equals("L")){
                g.fillRect(snakeX[0],snakeY[0],23,23);

            }else if (fx.equals("U")){
                g.fillRect(snakeX[0],snakeY[0],23,23);

            }else if (fx.equals("D")){
                g.fillRect(snakeX[0],snakeY[0],23,23);
            }
            g.setColor(Color.blue);


            for (int i = 1; i < length; i++) {

                g.fillRect(snakeX[i],snakeY[i],23,23);
            }
            //画食物
            g.setColor(Color.red);
            g.fillOval(foodx,foody,20,20);

            //游戏状态
            if(start == false){
                g.setColor(Color.WHITE);
                g.setFont(new Font("微软雅黑",Font.BOLD,40));
                g.drawString("按下空格开始游戏",280,300);
            }
            if(fail){
                if (max){
                    g.setColor(Color.orange);
                    g.setFont(new Font("微软雅黑",Font.BOLD,40));
                    g.drawString("恭喜你创造了新纪录！！！",250,500);

                }

                g.setColor(Color.red);
                g.setFont(new Font("微软雅黑",Font.BOLD,40));
                g.drawString("失败,按下空格重新开始",250,300);
            }




        } else{
            //画排行榜
            g.setColor(Color.BLUE);
            g.setFont(new Font("微软雅黑",Font.BOLD,40));
            g.drawString("排",900/2-150-30,120);
            g.drawString("行",900/2-30,120);
            g.drawString("榜",900/2+150-30,120);
            g.setColor(Color.white);
            g.setFont(new Font("微软雅黑",Font.BOLD,20));
            g.drawString("排名",210,150);
            g.drawString("用户",350,150);
            g.drawString("长度",490,150);
            g.drawString("分数",630,150);
            g.drawString("第 "+(page+1)+" 页",440,640);


            for (int i = 15*page; i < 15*(page+1) && i<ranks.size() ; i++) {
                g.setColor(Color.CYAN);
                g.setFont(new Font("微软雅黑",Font.BOLD,20));
                g.drawString(""+(i+1),210,180+30*(i%15));
                g.drawString(ranks.get(i).username,350,180+30*(i%15));
                g.drawString(""+ranks.get(i).length,490,180+30*(i%15));
                g.drawString(""+ranks.get(i).score,630,180+30*(i%15));
            }



        }
        setBackground(Color.BLACK);
        g.setColor(c);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * 键盘按下事件
     * @param e
     */
    //键盘监听事件
    @Override
    public void keyPressed(KeyEvent e) {
        int KeyCode = e.getKeyCode();
        if(KeyCode == KeyEvent.VK_SPACE){
            if (fail){
                //重新开始
                fail = false;
                init();
                SnakeMusic.getSdl().setMicrosecondPosition(0);
            }else{
                start = !start;
                if (start);
            }
            repaint();
        }
        //小蛇移动
        if (KeyCode == KeyEvent.VK_UP ){
            if(snakeX[0] != snakeX[1] )
                fx = "U";
        }else if(KeyCode == KeyEvent.VK_DOWN ){
            if(snakeX[0] != snakeX[1] )
            fx = "D";
        }else if(KeyCode == KeyEvent.VK_LEFT ){
            if(snakeY[0] != snakeY[1] )
            fx = "L";
        }else if(KeyCode == KeyEvent.VK_RIGHT ){
            if(snakeY[0] != snakeY[1] )
            fx = "R";
        }



    }

    @Override
    public void keyReleased(KeyEvent e) {

    }



    public void zero(){//DFS的数组、集合初始化
        int sign = 0;
        int x =0,y=0;
        queue.clear();
        list.clear();
        flag = false;
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 34; j++) {

                w[i][j]=0;
            }
        }
        for (int i = 1; i < length-1; i++) {
            x =  (snakeX[i]-25)/25;
            y = (snakeY[i]-75)/25;
            w[y][x] = 1;
        }

        w[(snakeY[0]-75)/25][(snakeX[0]-25)/25] = 2;
        w[(snakeY[length-1]-75)/25][(snakeX[length-1]-25)/25] = 3;
        queue.addLast(new Point((snakeY[0]-75)/25,(snakeX[0]-25)/25));

    }



    public  void dfs(){//DFS搜素蛇头到蛇尾的路径



        Point check = queue.pollFirst();
        if (check == null) return;
        int x = 0, y = 0;
        x = check.getX();
        y = check.getY();
        if (flag) return;
        if (x - 1 >= 0) {
            if (w[x - 1][y] == 3) {
                w[x - 1][y] = 2;
                flag = true;
                list.add(new Point(x-1,y));

                return;
            }
            if (w[x - 1][y] == 0) {
                queue.addLast(new Point(x - 1, y));
                w[x-1][y] = 2;

                dfs();
                if (flag)
                    list.add(new Point(x-1,y));
            }

        }
        if (flag) return;
        if (x + 1 < 24) {
            if (w[x+1][y] == 3) {
                w[x+1][y] = 2;
                flag = true;
                list.add(new Point(x+1,y));

                return;
            }

            if (w[x+1][y] == 0) {
                queue.addLast(new Point(x + 1, y));
                w[x+1][y] = 2;

                dfs();
                if (flag)
                    list.add(new Point(x+1,y));
            }
        }
        if (flag) return;
        if (y - 1 >= 0) {
            if (w[x][y - 1] == 3) {
                w[x][y - 1] = 2;
                flag = true;
                list.add(new Point(x,y-1));

                return;
            }
            if (w[x][y - 1] == 0) {
                queue.addLast(new Point(x, y - 1));
                w[x][y - 1] = 2;//2表示已经走过的地方，0表示可以走的，1表示障碍物，3表示终点

                dfs();
                if (flag)
                    list.add(new Point(x,y-1));
            }
        }
        if (flag) return;
        if (y + 1 < 34) {
            if (w[x][y + 1] == 3) {
                w[x][y + 1] = 2;
                flag = true;
                list.add(new Point(x,y+1));

                return;
            }

            if (w[x][y + 1] == 0) {
                queue.addLast(new Point(x, y + 1));
                w[x][y + 1] = 2;

                dfs();
                if (flag)
                    list.add(new Point(x,y+1));
            }
        }

        return;

    }

    /**
     * 使用DFS算法，求出蛇头到蛇尾的路径，以得到安全路径
     */
    public void SnakePlug(){//防撞

        int X = snakeX[0];
        int Y = snakeY[0];
        int X0 = 0,Y0=0;
        boolean flag0 = true;
        int k = 0;
        while (true){
            if (fx.equals("R")){
                snakeX[0] = snakeX[0]+25;
                //边界判断
                if (snakeX[0] > 850){
                    snakeX[0] = 25;
                }
            }else if (fx.equals("L")){
                snakeX[0] = snakeX[0]-25;
                if (snakeX[0] <25){
                    snakeX[0] = 850;
                }
            }else if(fx.equals("U")){
                snakeY[0] = snakeY[0]-25;
                if (snakeY[0] < 75){
                    snakeY[0] = 650;
                }
            }else if (fx.equals("D")){
                snakeY[0] = snakeY[0] + 25;
                if(snakeY[0] > 650){
                    snakeY[0] = 75;
                }
            }
            flag0 = true;
            for (int i = 1; i < length; i++) {
                if(snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]){
                    X0 = snakeX[0];
                    Y0 = snakeY[0];
                    snakeX[0] = X;
                    snakeY[0] = Y;
                    vbs();
                    snakeX[0] = X0;
                    snakeY[0] = Y0;
                    if(!list.isEmpty()) { break;}
                    k++;
                    if(k == 1){
                        fx = "R";
                    }else if (k == 2){
                        fx = "L";
                    }else if (k == 3){
                        fx = "D";
                    }else if (k == 4){
                        fx = "U";
                    }
                    flag0 = false;
                    snakeX[0] = X;
                    snakeY[0] = Y;
                }

            }

            if (flag0 || k == 4) break;

        }
        snakeX[0] = X;
        snakeY[0] = Y;


    }
    public void vbs(){//通过DFS调整蛇头方向
        zero();
        dfs();
        Point result = list.pollLast();
        if (result == null) return;
        if (result.getY()*25< snakeX[0]-25){
            fx = "L";
        }else if (result.getY()*25>snakeX[0]-25){
            fx = "R";
        }else if (result.getX()*25>snakeY[0]-75){
            fx = "D";
        }else if (result.getX()*25<snakeY[0]-75){
            fx = "U";
        }

    }


    /**
     * 添加计时器的事件监听
     * @param e
     */

    //事件监听---需要通过固定事件来刷新，1秒10次
    @Override
    public void actionPerformed(ActionEvent e) {

        if (!rank){
            repaint();
        }
        else if(start && fail == false ){//如果游戏是开始状态,排行榜是关闭状态，就让小蛇动起来
            //吃食物
            if (snakeX[0] == foodx && snakeY[0] == foody){
                if (music){
                    new Thread(){
                        @Override
                        public void run() {
                            SnakeMusic.getSdlFood().setMicrosecondPosition(0);
                            SnakeMusic.getSdlFood().start();
                        }
                    }.start();

                }

                length++;
                if (((length-3)%16)/15 == 1){

                    timer.setDelay(timer.getDelay()-2);
                }


                score+=10;
                foodx = 25+25*random.nextInt(34);
                foody = 75+25*random.nextInt(24);
                for (int i = 0; i < length; i++) {//防止食物在身体上
                    if (foodx == snakeX[i] && foody == snakeY[i]){
                        foodx = 25+25*random.nextInt(34);
                        foody = 75+25*random.nextInt(24);
                        i = 0;
                    }
                }
            }

            //身体移动，后面一节的下一个位置是上一节移动之前的位置
            for (int i = length-1; i > 0 ; i--) {
                snakeX[i] = snakeX[i-1];
                snakeY[i] = snakeY[i-1];
            }
            if (Plug){

                SnakePlug();//防撞

            }
            //头的移动，走向
            if (fx.equals("R")){
                snakeX[0] = snakeX[0]+25;
                //边界判断
                if (snakeX[0] > 850){
                    snakeX[0] = 25;
                }
            }else if (fx.equals("L")){
                snakeX[0] = snakeX[0]-25;
                if (snakeX[0] <25){
                    snakeX[0] = 850;
                }
            }else if(fx.equals("U")){
                snakeY[0] = snakeY[0]-25;
                if (snakeY[0] < 75){
                    snakeY[0] = 650;
                }
            }else if (fx.equals("D")){
                snakeY[0] = snakeY[0] + 25;
                if(snakeY[0] > 650){
                    snakeY[0] = 75;

                }
            }

            //失败判定，撞到自己就算失败
            for (int i = 1; i <length ; i++) {
                if(snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]){
                    fail = true;
                    SnakeMusic.getSdl().stop();
                    if(music){
                        SnakeMusic.getSdlfail().setMicrosecondPosition(0);
                        SnakeMusic.getSdlfail().start();
                    }

                    connectionEnd();
                    rank();
                }
            }


        }
        repaint();



    }

    /**
     * 获取排行榜
     */


    public static ArrayList<Ranks> rank(){//获取排行榜
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "select * from rankt order by score desc";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            ranks.clear();
            if (rs.next()){
                Ranks r0 = new Ranks();
                r0.score = rs.getInt("score");
                r0.username = rs.getString("username");
                r0.length = rs.getInt("length0");
                ranks.add(r0);
                scoremax = rs.getInt("score");//最高分
            }
            while (rs.next()){//读取排行榜
                Ranks r = new Ranks();
                r.score = rs.getInt("score");
                r.username = rs.getString("username");
                r.length = rs.getInt("length0");
                ranks.add(r);
            }

            conn.commit();
            conn.close();
            pstmt.close();
            rs.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ranks;



    }

    /**
     * 游戏结束时连接数据库，添加游戏数据
     */
    public static void connectionEnd(){//失败或关闭程序时连接数据库插入记录

        String username = StartGame.getTxtUser().getText();
        Dao.Insert(username,length,score);
        if (scoremax<score){
            max = !max;
        }
    }

    /**
     * 根据music开关调整按钮图标
     */
    public  void musicSwitch(){//音乐按钮开关

        if (music){
            SnakeMusic.getSdl().loop(Clip.LOOP_CONTINUOUSLY);
            jButtonMusicLoop.setVisible(!music);
            jButtonMusicStop.setVisible(music);
        }else{
            SnakeMusic.getSdl().stop();
            jButtonMusicLoop.setVisible(!music);
            jButtonMusicStop.setVisible(music);
        }
    }


    //排行榜监听器
    class RankListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            rank = !rank;
            jButtonFirst.setVisible(!rank);
            jButtonNext.setVisible(!rank);
            jButtonLast.setVisible(!rank);
            jButtonBack.setVisible(!rank);
            jButtonRank.setVisible(false);
            jButtonRank.setVisible(true);
        }
    }

    //失败音效监听器
    class MusicListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            music = !music;
            musicSwitch();
        }
    }

}

