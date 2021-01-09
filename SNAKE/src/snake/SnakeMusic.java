package snake;

import javax.sound.sampled.*;
import java.applet.AudioClip;

import java.awt.*;
import java.io.File;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;

/**
 * 加载音乐
 */
public class SnakeMusic {//音乐类


    private static  Clip sdl;
    private static Clip sdlfail;
    private static Clip sdlFood;
    private static int num = 0;

    public static Clip getSdl() {
        return sdl;
    }


    public static Clip getSdlfail() {
        return sdlfail;
    }


    public static Clip getSdlFood() {
        return sdlFood;
    }




    static{
        try {

            sdlFood = AudioSystem.getClip();
            sdlfail = AudioSystem.getClip();
            sdl = AudioSystem.getClip();
            URL sdlURL = SnakeMusic.class.getResource("/music/7895.wav");
            URL sdlFailURL = SnakeMusic.class.getResource("/music/7896.wav");
            URL sdlFoodURL = SnakeMusic.class.getResource("/music/2310.wav");


            AudioInputStream ais = AudioSystem.getAudioInputStream(sdlURL);
            AudioInputStream aisFail = AudioSystem.getAudioInputStream(sdlFailURL);
            AudioInputStream aisFood = AudioSystem.getAudioInputStream(sdlFoodURL);

            sdl.open(ais);

            sdlfail.open(aisFail);

            sdlFood.open(aisFood);

//                    sdl.start();这里开的是一个守护线程，开启守护线程的线程结束，守护线程也会结束
//            sdl.loop(Clip.LOOP_CONTINUOUSLY);//循环播放

            FloatControl fc1 = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            FloatControl fc2 = (FloatControl) sdlfail.getControl(FloatControl.Type.MASTER_GAIN);
            FloatControl fc3 = (FloatControl) sdlFood.getControl(FloatControl.Type.MASTER_GAIN);
            // value可以用来设置音量，从-80.0 ~ 6.020599842071533
            float value = -8.457575f;

//            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc1.setValue(value);
            fc2.setValue(value);
            fc3.setValue(value);



        } catch (Exception e) {
            e.printStackTrace();
        }

    }





}
