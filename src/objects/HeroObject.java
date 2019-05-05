package objects;

import javax.swing.*;

/**
 * 文件名：
 * 学号：16301054
 * 姓名：尹业立
 * 班级：软件1602班
 * 2017/12/15 10:31
 */
public class HeroObject {
    private Icon imageIcon = new ImageIcon("images\\heroU.jpg");//本地图片文件
    private static int score = 0;
    private JLabel heroJLable = new JLabel(imageIcon);

    public Icon getHeroImage() {
        return this.imageIcon;
    }

    public JLabel getHeroJLable() {
        return this.heroJLable;
    }

    public int getScore() {
        return this.score;//获取分数
    }


}
