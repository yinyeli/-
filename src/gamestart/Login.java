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
                this.playerName = jtxtName.getText(); // 获取用户名字
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



    public static void main(String args[]) {
        new Login();
    }

}
