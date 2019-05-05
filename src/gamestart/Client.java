package gamestart; /**
 * 文件名：
 * 学号：16301054
 * 姓名：尹业立
 * 班级：软件1602班
 * 2017/12/1 20:46
 */

import objects.EnemyObject;
import objects.HeroObject;
import playMusic.music;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

public class Client {
    static int ENEMYMOVESPEED_MAX = 50;//敌军移动（每步间隔时间）最慢速度 :t 最大
    static int ENEMYMOVESPEED_MIN = 1;//敌军移动（每步间隔时间）最快速度:t 最小
    static int ENEMYADD = 3;//敌军生成时间间隔/秒
    static int ENEMY_NUMS = 6;//敌军出现的最多个数
    private static final int PORT = 6666;
    private int score = 0;//score = time*( (100/ENEMYMOVESPEED_MAX) + 10 / ENEMYMOVESPEED_MI + 30 / ENEMYADD + ENEMY_NUMS)
    static int ENEMYSPEED = 15;//敌军移动格数
    private JFrame jf;//窗口
    private static Thread tjf;
    private static Thread tEnemy;//敌军移动线程
    private static Thread tNewOne;//产生新的敌军
    private static Thread tdisplayWords;
    private ArrayList<EnemyObject> enemyObjectArrayList = new ArrayList<>();
    private int heroX;//hero x位置
    private int heroY;//hero y位置
    private HeroObject hero = new HeroObject();//实例化英雄机对象
    private Boolean stopGame = true;//判断是否中途暂停
    private String id;//用户的姓名

    public Client(String playerName) {
        this.id = playerName;
        showGUI();
    }

    private void showGUI() {
        jf = new JFrame("疯狂躲避");
        jf.setResizable(false);//不可更改大小
        jf.setBounds(250, 190, 900, 700);//设置和窗口大小
        jf.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == ' ') {
                    if (stopGame) {
                        stopGame = false;
                    } else {
                        stopGame = true;
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLayout(null);//设置成 null
        jf.setVisible(true);

        JOptionPane.showMessageDialog(null, "点击确定，开始游戏！");
        playMusic();
        showHero();
        addEnemy();
        displayWords(id);
    }

    private void showHero() {
        JLabel heroJlable = hero.getHeroJLable();//显示英雄机到jf
        heroJlable.setSize(hero.getHeroImage().getIconWidth(), hero.getHeroImage().getIconHeight());
        heroJlable.setLocation(jf.getWidth() / 2, jf.getHeight() / 2);
        jf.getContentPane().add(heroJlable);
        jf.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                heroX = e.getX() - heroJlable.getWidth() / 2;
                heroY = e.getY() - heroJlable.getHeight();
                if (!stopGame) {
                    heroJlable.setLocation(heroX, heroY);
                    if (isOut(heroJlable)) {
                        //出界
                        heroDie();//死亡处理
                    }
                }
            }
        });
    }

    private void addEnemy() {
        tNewOne = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if ((enemyObjectArrayList.size() <= ENEMY_NUMS && !stopGame)) {
                        //敌人数量没有达到最大值
                        EnemyObject enemyObject = new EnemyObject();
                        enemyObjectArrayList.add(enemyObject);//敌人队列增加对象
                        aEnemy(enemyObject);//出现敌人对象
                    } else {
                        Thread.interrupted();//结束线程
                    }
                    try {
                        Thread.sleep(1000 * ENEMYADD);
                        //每间隔ENEMYADD秒生成一个敌军
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        tNewOne.start();
    }

    private void aEnemy(EnemyObject eO) {
        tEnemy = new Thread(new Runnable() {
            @Override
            public void run() {
                JLabel jLenemy = eO.getjLenemy();
                Icon iconEnemy = eO.getIconbullet();
                jLenemy.setSize(iconEnemy.getIconWidth(), iconEnemy.getIconHeight());
                jf.getContentPane().add(jLenemy);

                Random random = new Random();
                int x = random.nextInt(jf.getWidth() - jLenemy.getWidth());
                int y = random.nextInt(jf.getHeight() - jLenemy.getHeight());
                switch (eO.getD_SIDE()) {//判断敌人出现的边侧
                    case "R":
                        jLenemy.setLocation(jf.getWidth() - jLenemy.getWidth(), y);
                        break;
                    case "L":
                        jLenemy.setLocation(1, y);
                        break;
                    case "U":
                        jLenemy.setLocation(x, 1);
                        break;
                    case "D":
                        jLenemy.setLocation(x, jf.getHeight() - jLenemy.getHeight() * 2 + 10);
                        break;
                    default:
                        break;
                }
                while (true) {
                    //在边上,改变方向
                    stopGameNow(stopGame);
                    isSideAndChange(eO);
                    if (eO.getCHANGE_D()) {
                        changeD(eO);
                        step(eO);//行走
                        eO.setCHANGE_D(false);
                    } else {
                        step(eO);//行走
                        isOut(jLenemy);
                    }
                    try {
                        int t = ENEMYMOVESPEED_MAX * 10 / (10 + (enemyObjectArrayList.indexOf(eO) + 1));
                        if (t < ENEMYMOVESPEED_MIN) {
                            t = ENEMYMOVESPEED_MIN;
                        }
                        Thread.sleep(t);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        tEnemy.start();
    }

    private void isSideAndChange(EnemyObject eO) {
        //判断当前位置在哪一边
        JLabel label = eO.getjLenemy();
        int xb = label.getX();
        int yb = label.getY();
        if (xb < 10) {//当前位置在左边可以更改方向
            eO.setD_SIDE_LEFT();
            eO.setCHANGE_D(true);
        } else if (yb < 10) {//当前位置在上边可以更改方向
            eO.setD_SIDE_UP();
            eO.setCHANGE_D(true);
        } else if (xb + label.getWidth() + 10 > jf.getWidth()) {//当前位置在右边可以更改方向
            eO.setD_SIDE_RIGHT();
            eO.setCHANGE_D(true);
        } else if (yb + label.getHeight() * 2 - 9 > jf.getHeight()) {//当前位置在下边可以更改方向
            eO.setD_SIDE_DOWN();
            eO.setCHANGE_D(true);
        } else {
            eO.setCHANGE_D(false);//不变更方向
        }
    }

    private boolean isOut(JLabel j) {//保证不出界面
        Boolean b = false;
        if (j.getX() < 3) {
            b = true;
            j.setLocation(3, j.getY());
        }
        if (j.getY() < 3) {
            b = true;
            j.setLocation(j.getX(), 3);
        }
        if (j.getX() + j.getWidth() > jf.getWidth()) {
            b = true;
            j.setLocation(jf.getWidth() - j.getWidth(), j.getY());
        }
        if (j.getY() + j.getHeight() * 2 - 10 > jf.getHeight()) {
            b = true;
            j.setLocation(j.getX(), jf.getHeight() - j.getHeight() * 2 + 10);
        }
        return b;
    }

    private void changeD(EnemyObject eO) {//变更方向
        isSideAndChange(eO);
        if (eO.getCHANGE_D()) {
            //可变更方向
            String d = eO.getD_SIDE();
            JLabel jLenemy = eO.getjLenemy();
            int xd = heroX - jLenemy.getX();
            int yd = heroY - jLenemy.getY();
            if (xd < 0) {
                xd = 0 - xd;
            }
            if (yd < 0) {
                yd = 0 - yd;
            }

            if (xd < 30) {
                if (heroY - jLenemy.getY() > 0) {
                    eO.setD_WHAT_DOWN();
                } else {
                    eO.setD_WHAT_UP();
                }
            } else if (yd < 30) {
                if (heroX - jLenemy.getX() > 0) {
                    eO.setD_WHAT_RIGHT();
                } else {
                    eO.setD_WHAT_LEFT();
                }
            } else if (xd < yd) {
                if (heroX - jLenemy.getX() > 0) {
                    eO.setD_WHAT_RIGHT();
                } else {
                    eO.setD_WHAT_LEFT();
                }
            } else if (xd > yd) {
                if (heroY - jLenemy.getY() > 0) {
                    eO.setD_WHAT_DOWN();
                } else {
                    eO.setD_WHAT_UP();
                }
            } else if (xd == yd) {
                if (d.equals("U") || d.equals("D")) {
                    if (heroX - jLenemy.getX() > 0) {
                        eO.setD_WHAT_RIGHT();
                    } else {
                        eO.setD_WHAT_LEFT();
                    }
                } else if (d.equals("R") || d.equals("L")) {
                    if (heroY - jLenemy.getY() > 0) {
                        eO.setD_WHAT_DOWN();
                    } else {
                        eO.setD_WHAT_UP();
                    }
                }
            }

        }


    }

    private void step(EnemyObject enemyObject) {
        String d = enemyObject.getD_WHAT();//向着d方向行走
        JLabel j1 = enemyObject.getjLenemy();
        int xb = 0;
        int yb = 0;
        switch (d) {
            case "R":
                xb += ENEMYSPEED;
                break;
            case "L":
                xb -= ENEMYSPEED;
                break;
            case "U":
                yb -= ENEMYSPEED;
                break;
            case "D":
                yb += ENEMYSPEED;
                break;
            default:
                break;
        }
        j1.setLocation(j1.getX() + xb, j1.getY() + yb);
        if (isCollide(hero.getHeroJLable(), j1)) {
            heroDie();//死亡
        }
    }

    private boolean isCollide(JLabel l1, JLabel l2) {
        boolean b;
        int x1 = l1.getX();
        int w1 = l1.getWidth();
        int y1 = l1.getY();
        int h1 = l1.getHeight();
        int x2 = l2.getX();
        int w2 = l2.getWidth();
        int y2 = l2.getY();
        int h2 = l2.getHeight();
        if (x1 + w1 > x2 && x1 + w1 < x2 + w2) {
            if (y1 + h1 > y2 && y1 + h1 < y2 + h2) {
                b = true;
            } else if (y1 < y2 + h2 && y1 > y2) {
                b = true;
            } else {
                b = false;
            }
        } else if (x1 < x2 + w2 && x1 > x2) {
            if (y1 + h1 > y2 && y1 + h1 < y2 + h2) {
                b = true;
            } else if (y1 < y2 + h2 && y1 > y2) {
                b = true;
            } else {
                b = false;
            }
        } else {
            b = false;
        }
        return b;
    }

    private void playMusic() {
        try {
            Thread thread = new Thread(new music(), "music1");
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void stopGameNow(Boolean b) {
        while (stopGame) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置艺术字！！！
     */
    private void displayWords(String id) {
        tdisplayWords = new Thread(new Runnable() {
            JLabel jw = new JLabel();
            JLabel jid = new JLabel();
            int t = 0;
            @Override
            public void run() {
                jid.setForeground(Color.green);
                jid.setText("player name :" +id);
                jid.setBounds(260, 5, 180, 40);
                jf.add(jid);
                jw.setForeground(Color.green);
                jw.setBounds(20, 5, 230, 40);
                jf.add(jw);
                while (true) {
                    score = t * (100/ENEMYMOVESPEED_MAX + 10 / ENEMYMOVESPEED_MIN + 30 / ENEMYADD + ENEMY_NUMS);
                    jw.setText(String.valueOf(score));
                    if (!stopGame) {
                        t++;
                    }
                    try {
                        Thread.sleep(900);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        tdisplayWords.start();
    }

    private void heroDie(){
        stopGame = true;
        JOptionPane.showMessageDialog(null, "Game Over!");
        JOptionPane.showMessageDialog(null, "Your score is" + score);
        jf.setVisible(false);

        System.exit(0);//退出程序
    }


}
