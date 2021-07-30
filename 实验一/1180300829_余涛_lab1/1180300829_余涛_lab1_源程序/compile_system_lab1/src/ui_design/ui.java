package ui_design;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import method.Lexical;
import method.Token;

/**
 * @author Brainrain
 */
public class ui extends JFrame {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ui frame = new ui();
                    frame.pack();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ui() {
        initComponents();
    }

    private void button1ActionPerformed(ActionEvent e) {
        // TODO add your code here
        JFileChooser chooser = new JFileChooser("src/test_file");;  //选择文件
        FileNameExtensionFilter filter  = new FileNameExtensionFilter("所有文件", "txt", "jar");//设置过滤器
        chooser.setFileFilter(filter);
        int value = chooser.showOpenDialog(ui.this);
        if (value == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            Lexical myLexical = new Lexical();  //创建Lexical进行判断
            myLexical.readfromfile(file.getAbsolutePath());
            textArea2.setText(myLexical.text);
            myLexical.lex();
            myLexical.show();

            StringBuffer tempstring = new StringBuffer();  // 得到token序列并输出
            for (Token a : myLexical.allTokenClass){
                tempstring.append(a.toString() + "\n");
            }
            textArea3.setText(tempstring.toString());

            tempstring = new StringBuffer();  // 得到错误信息并输出
            for (String a : myLexical.allmistake){
                tempstring.append(a + "\n");
            }
            textArea4.setText(tempstring.toString());
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label2 = new JLabel();
        label1 = new JLabel();
        label3 = new JLabel();
        button1 = new JButton();
        scrollPane2 = new JScrollPane();
        textArea2 = new JTextArea();
        scrollPane3 = new JScrollPane();
        textArea3 = new JTextArea();
        scrollPane4 = new JScrollPane();
        textArea4 = new JTextArea();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {73, 212, 203, 346, 0};
                ((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {26, 369, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};
                ((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

                //---- label2 ----
                label2.setText("\u5f85\u5206\u6790\u4ee3\u7801");
                label2.setHorizontalAlignment(SwingConstants.CENTER);
                contentPanel.add(label2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- label1 ----
                label1.setText("Token\u5e8f\u5217");
                label1.setHorizontalAlignment(SwingConstants.CENTER);
                contentPanel.add(label1, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- label3 ----
                label3.setText("\u9519\u8bef\u4fe1\u606f");
                label3.setHorizontalAlignment(SwingConstants.CENTER);
                contentPanel.add(label3, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- button1 ----
                button1.setText("\u9009\u62e9\u6587\u4ef6");
                button1.addActionListener(e -> button1ActionPerformed(e));
                contentPanel.add(button1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //======== scrollPane2 ========
                {
                    scrollPane2.setViewportView(textArea2);
                }
                contentPanel.add(scrollPane2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //======== scrollPane3 ========
                {
                    scrollPane3.setViewportView(textArea3);
                }
                contentPanel.add(scrollPane3, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //======== scrollPane4 ========
                {
                    scrollPane4.setViewportView(textArea4);
                }
                contentPanel.add(scrollPane4, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));
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
    private JLabel label3;
    private JButton button1;
    private JScrollPane scrollPane2;
    private JTextArea textArea2;
    private JScrollPane scrollPane3;
    private JTextArea textArea3;
    private JScrollPane scrollPane4;
    private JTextArea textArea4;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
