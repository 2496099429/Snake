package snake;

import java.io.Serializable;

/**
 * 排行榜
 */
public class Ranks implements Serializable {//排行榜类


    String username;
    int score;
    int length;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
