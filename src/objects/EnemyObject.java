package objects;

import javax.swing.*;
import java.util.Random;

/**
 * 文件名：
 * 学号：16301054
 * 姓名：尹业立
 * 班级：软件1602班
 * 2017/12/16 10:00
 */
public class EnemyObject {
    private Icon iconbullet = new ImageIcon("images\\eU.jpg");//本地图片文件,子弹
    private JLabel jLenemy = new JLabel(iconbullet);
    final static String D_UP = "U";
    final static String D_DOWN = "D";
    final static String D_LEFT = "L";
    final static String D_RIGHT = "R";
    private String D_WHAT;//行走方向
    private Boolean CHANGE_D;//是否变更方向
    private String D_SIDE;//所在边界
    private int E_X;
    private int E_Y;

    public EnemyObject() {
        //随机选定敌人出现的边侧
        Random random = new Random();
        int choice = random.nextInt(4);
        if (choice == 0) {
            //出现在上方
            this.D_SIDE = D_UP;
        } else if (choice == 1) {
            //出现在下方
            this.D_SIDE = D_DOWN;
        } else if (choice == 2) {
            //出现在左侧
            this.D_SIDE = D_LEFT;
        } else if (choice == 3) {
            //出现在右侧
            this.D_SIDE = D_RIGHT;
        } else {
            System.out.println("产生的随机数不在（0,1,2,3）中");
        }
    }

    public JLabel getjLenemy() {
        return this.jLenemy;
    }

    public Icon getIconbullet() {
        return this.iconbullet;
    }

    public void setD_WHAT_UP() {
        this.D_WHAT = D_UP;
    }

    public void setD_WHAT_DOWN() {
        this.D_WHAT = D_DOWN;
    }

    public void setD_WHAT_LEFT() {
        this.D_WHAT = D_LEFT;
    }

    public void setD_WHAT_RIGHT() {
        this.D_WHAT = D_RIGHT;
    }

    public void setD_SIDE_UP() {
        this.D_SIDE = D_UP;
    }

    public void setD_SIDE_DOWN() {
        this.D_SIDE = D_DOWN;
    }

    public void setD_SIDE_LEFT() {
        this.D_SIDE = D_LEFT;
    }

    public void setD_SIDE_RIGHT() {
        this.D_SIDE = D_RIGHT;
    }

    public String getD_WHAT() {
        return this.D_WHAT;
    }

    public String getD_SIDE() {
        return this.D_SIDE;
    }

    public Boolean getCHANGE_D() {
        return CHANGE_D;
    }

    public void setCHANGE_D(Boolean CHANGE_D) {
        this.CHANGE_D = CHANGE_D;
    }
}

