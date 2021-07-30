/*
 * Created by JFormDesigner on Tue Apr 27 08:47:08 CST 2021
 */

package lab2_ui;

import Lexical_analyze.Lexical;
import Lexical_analyze.Token;
import grammer_analyze.analyze;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Brainrain
 */
public class grammer_ui extends JFrame {

    analyze ll;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    grammer_ui frame = new grammer_ui();
                    frame.pack();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public grammer_ui() {
        initComponents();
    }

    private void button1ActionPerformed(ActionEvent e) {
        // TODO add your code here
        JFileChooser chooser = new JFileChooser("src/grammer_file");;  //选择文件
        FileNameExtensionFilter filter  = new FileNameExtensionFilter("所有文件", "txt", "jar");//设置过滤器
        chooser.setFileFilter(filter);
        int value = chooser.showOpenDialog(grammer_ui.this);
        if (value == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            ll.grammer_analyze(file.getAbsolutePath());
            System.out.println("所有的Token值为：");
            for (String pp : ll.getAlltokenvalue()){
                System.out.println(pp);
            }
            textArea1.setText(ll.getText());  // 输出文法
            StringBuilder statute = new StringBuilder();
            for (String jj : ll.getStatute()){
                statute.append(jj).append("\n");
            }
            textArea5.setText(statute.toString());  // 输出产生式
            StringBuilder allerror = new StringBuilder();
            for (String pp : ll.getAllerror()){
                allerror.append(pp).append("\n");
            }
            textArea4.setText(allerror.toString());
            if (ll.getTreestack().isEmpty()){
                textArea3.setText("该错误无法调用错误处理进行处理");
                return;
            }
            StringBuilder result = new StringBuilder();
            System.out.println(ll.getBegin_key() + "(" + ll.getTreestack().peek().getLine() + ")");
            ll.printtree(ll.getTreestack().peek(), "      ");
            result.append(ll.getBegin_key()).append("(").append(ll.getTreestack().peek().getLine()).append(")").append("\n");
            result.append(ll.getResult());
            textArea3.setText(result.toString());  // 输出树
        }
    }

    private void button2ActionPerformed(ActionEvent e) {  // 词法分析
        ll = new analyze();
        // TODO add your code here
        JFileChooser chooser = new JFileChooser("src/grammer_file");;  //选择文件
        FileNameExtensionFilter filter  = new FileNameExtensionFilter("所有文件", "txt", "jar");//设置过滤器
        chooser.setFileFilter(filter);
        int value = chooser.showOpenDialog(grammer_ui.this);
        if (value == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            ll.lexical_analyze(file.getAbsolutePath());
        }
        textArea2.setText(ll.getMyLexical().text);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label2 = new JLabel();
        label1 = new JLabel();
        button2 = new JButton();
        scrollPane2 = new JScrollPane();
        textArea2 = new JTextArea();
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();
        button1 = new JButton();
        label3 = new JLabel();
        label4 = new JLabel();
        scrollPane3 = new JScrollPane();
        textArea3 = new JTextArea();
        scrollPane4 = new JScrollPane();
        textArea4 = new JTextArea();
        label5 = new JLabel();
        scrollPane5 = new JScrollPane();
        textArea5 = new JTextArea();

        //======== this ========
        setTitle("grammer_analyse");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {120, 278, 260, 17, 0};
                ((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {31, 134, 123, 31, 213, 33, 194, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};
                ((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                //---- label2 ----
                label2.setText("\u5f85\u5206\u6790\u4ee3\u7801");
                label2.setHorizontalAlignment(SwingConstants.CENTER);
                contentPanel.add(label2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- label1 ----
                label1.setText("\u6587\u6cd5");
                label1.setHorizontalAlignment(SwingConstants.CENTER);
                contentPanel.add(label1, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- button2 ----
                button2.setText("\u5f85\u5206\u6790\u6587\u4ef6");
                button2.addActionListener(e -> button2ActionPerformed(e));
                contentPanel.add(button2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //======== scrollPane2 ========
                {
                    scrollPane2.setViewportView(textArea2);
                }
                contentPanel.add(scrollPane2, new GridBagConstraints(1, 1, 1, 2, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(textArea1);
                }
                contentPanel.add(scrollPane1, new GridBagConstraints(2, 1, 1, 2, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- button1 ----
                button1.setText("\u9009\u62e9\u6587\u6cd5\u6587\u4ef6\u5e76\u8fdb\u884c\u5206\u6790");
                button1.addActionListener(e -> button1ActionPerformed(e));
                contentPanel.add(button1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- label3 ----
                label3.setText("\u5206\u6790\u7ed3\u679c");
                label3.setHorizontalAlignment(SwingConstants.CENTER);
                contentPanel.add(label3, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- label4 ----
                label4.setText("\u9519\u8bef\u4fe1\u606f");
                label4.setHorizontalAlignment(SwingConstants.CENTER);
                contentPanel.add(label4, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //======== scrollPane3 ========
                {
                    scrollPane3.setViewportView(textArea3);
                }
                contentPanel.add(scrollPane3, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //======== scrollPane4 ========
                {
                    scrollPane4.setViewportView(textArea4);
                }
                contentPanel.add(scrollPane4, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- label5 ----
                label5.setText("\u89c4\u7ea6\u4fe1\u606f");
                label5.setHorizontalAlignment(SwingConstants.CENTER);
                contentPanel.add(label5, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //======== scrollPane5 ========
                {
                    scrollPane5.setViewportView(textArea5);
                }
                contentPanel.add(scrollPane5, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label2;
    private JLabel label1;
    private JButton button2;
    private JScrollPane scrollPane2;
    private JTextArea textArea2;
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    private JButton button1;
    private JLabel label3;
    private JLabel label4;
    private JScrollPane scrollPane3;
    private JTextArea textArea3;
    private JScrollPane scrollPane4;
    private JTextArea textArea4;
    private JLabel label5;
    private JScrollPane scrollPane5;
    private JTextArea textArea5;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
