package gamestart;
/**
 * 文件名：
 * 学号：16301054
 * 姓名：尹业立
 * 班级：软件1602班
 * 2017/12/16 16:09
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame implements ActionListener {
    private String playerName;
    private JFrame jf = new JFrame();
    private JLabel jlName = new JLabel("    用户名:");
    private JButton[] jbArray = {new JButton("gamestart"),
            new JButton("Flush!")};
    private JTextField jtxtName = new JTextField();
    private String newID = null;
    private Boolean isADD = false;


    public Login() {
        showGUI();
    }

    private void showGUI() {
        /**
         * 设置各个组件排列位置
         */
        jf.setLayout(null);
        JMenuBar menuBar = new JMenuBar();//创建并添加菜单栏
        jf.getJMenuBar();
        jf.setJMenuBar(menuBar);
        JMenu menuFile = new JMenu("文件"),
                menuScore = new JMenu("分数");
        menuBar.add(menuFile);
        menuBar.add(menuScore);
        JMenuItem itemSave = new JMenuItem("生成分数排行表"),
                itemScore = new JMenuItem("分数排行查看");

        itemSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                saveScore();
            }
        });
        itemScore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                showScore();
            }
        });

        menuFile.add(itemSave);
        menuScore.add(itemScore);

        jlName.setBounds(20, 20, 80, 26);
        jf.add(jlName);
        for (int i = 0; i < 2; i++) {
            jbArray[i].setBounds(50 + i * 110, 130, 80, 26);
            jf.add(jbArray[i]);
            jbArray[i].addActionListener(this);
        }

        jtxtName.setBounds(80, 20, 180, 30);
        jf.add(jtxtName);
        jtxtName.addActionListener(this);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setTitle("gamestart");
        jf.setResizable(false);
        jf.setBounds(300, 200, 300, 250);
        jf.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        /**
         * 设置操作
         */
        if (e.getSource() == jbArray[0]){ // 点击gamestart 按钮
            try {
                Thread.sleep(1000);//睡眠1秒
                jf.setVisible(false);
                // 进入用户界面，关闭登录界面
                new Client(this.playerName);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }else {// 点击flush 按钮,清空用户名
            jtxtName.setText("");
        }

    }




//    private void showScore() {
//        String s = sendMessageAndGet("1" + "#&" + "2" + "#&" + "Score#&");
//        String[] sco = s.split("##&");
//        ArrayList<Integer> scoList = new ArrayList<>();
//        JFrame jf2 = new JFrame("分数显示");
//        jf2.setBounds(350, 250, 500, 500);
//        JTextArea jTextArea = new JTextArea();
//
//        for (int i = 0; i < sco.length - 1; i++) {
//            int d = Integer.parseInt(sco[i]);
//            scoList.add(d);
//        }
//
//        Collections.sort(scoList, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                if (o1 > o2) {
//                    return -1;
//                } else {
//                    return 1;
//                }
//            }
//        });//排序
//        for (int i = 0; i < scoList.size(); i++) {
//            jTextArea.append("第" + (i + 1) + "名 ：  ");
//            jTextArea.append(scoList.get(i) + "\r\n");
//        }
//        jTextArea.setEditable(false);//不可更改
//        JScrollPane js = new JScrollPane(jTextArea);
//        jf2.add(js);
//
//        jf2.setResizable(false);
//        jf2.setVisible(true);
//        jf2.setDefaultCloseOperation(HIDE_ON_CLOSE);
////        System.out.println("showScore OK!");
//    }


    public static void main(String args[]) {
        new Login();
    }

}
