package snake;

import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * 用户从文件中加载静态资源
 */

public class Data {//图片类


    public static URL headerURL = GamePanel.class.getResource("/statics/star.png");//  /表示当前src目录,不加/时表示当前包


    public static  ImageIcon header = new ImageIcon(headerURL);


    public static URL musicLoopURL = GamePanel.class.getResource("/statics/musicLoop.png");
    public static URL musicStopURL = GamePanel.class.getResource("/statics/musicStop.png");

    public static ImageIcon musicLoop = new ImageIcon(musicLoopURL);
    public static ImageIcon musicStop = new ImageIcon(musicStopURL);


    static Properties properties;
    static URL str;
    static String path;
    static{
        properties = new Properties();
        str = Data.class.getResource("/utils/jdbc.properties");
        path = str.getPath();
        try {
        properties.load(new FileReader(path));
    } catch (IOException e) {
        e.printStackTrace();
    }
    }


    public static void init(){//初始化music设置

        String k = properties.getProperty("music");

        boolean music = false;

        if (k.equals("1")){
            music = true;
        }else if (k.equals("2")) {
            music = false;
        }

        GamePanel.setMusic(music);

    }

    public static void update(){//修改music开关
        if (GamePanel.isMusic()){
            properties.setProperty("music","1");
            try {
                properties.store(new FileWriter(path),"");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{

            properties.setProperty("music","2");
            try {
                properties.store(new FileWriter(path),"");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }






}
