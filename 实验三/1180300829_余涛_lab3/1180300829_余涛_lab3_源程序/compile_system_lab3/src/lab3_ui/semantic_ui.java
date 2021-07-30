/*
 * Created by JFormDesigner on Fri May 14 19:28:03 CST 2021
 */

package lab3_ui;

import semantic.FourAddr;
import semantic.Smantic;
import semantic.Symbol;
import semantic.util;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 * @author Brainrain
 */
public class semantic_ui extends JFrame {

    private static final long serialVersionUID = 1L;
    private static String file_name;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    semantic_ui frame = new semantic_ui();
                    frame.pack();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public semantic_ui() {
        initComponents();
    }


    // 写入待分析代码
    private void button1ActionPerformed(ActionEvent e) {
        // TODO add your code here
        JFileChooser file_open_filechooser = new JFileChooser();
//        file_open_filechooser.setCurrentDirectory(new File("."));
        file_open_filechooser.setCurrentDirectory(new File("src/all_file"));
        file_open_filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = file_open_filechooser.showOpenDialog(scrollPane1);
        if (result == JFileChooser.APPROVE_OPTION) {
            file_name = file_open_filechooser.getSelectedFile().getPath();
            try {
                FileReader reader = new FileReader(file_name);
                BufferedReader br = new BufferedReader(reader);
                String line;
                while ((line = br.readLine()) != null) {
                    textArea1.append(line);
                    textArea1.append("\n");
                }
                reader.close();
            } catch (Exception event) {
                event.printStackTrace();
            }
        }
    }

    private void button2ActionPerformed(ActionEvent e) {
        // TODO add your code here
        List<Stack<Symbol>> table = new ArrayList<>();  // 符号表
        List<String> three_addr = new ArrayList<>();  // 三地址指令序列
        List<FourAddr> four_addr = new ArrayList<>();  // 三地址指令序列
        List<String> errors = new ArrayList<>();  // 错误序列

        Smantic se = new Smantic(file_name, table, three_addr, four_addr, errors);

        Object[][] gui_ins = util.gui_ins(three_addr, four_addr);
        Object[][] gui_table = util.gui_table(table);
        Object[][] gui_errors = util.gui_errors(errors);


        String[] name1 = new String[]{"序号", "四元式", "三地址"};
        DefaultTableModel model1 = new DefaultTableModel(gui_ins, name1);
        table1.setModel(model1);

        String[] name2 = new String[]{"表号", "符号", "类型", "offset"};
        DefaultTableModel model2 = new DefaultTableModel(gui_table, name2);
        table2.setModel(model2);

        String[] name3 = new String[]{"错误报告"};
        DefaultTableModel model3 = new DefaultTableModel(gui_errors, name3);
        table3.setModel(model3);

        if (table1.getRowCount() == 0 && table2.getRowCount() == 0 && table3.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "没有可分析的程序", "Warning", JOptionPane.DEFAULT_OPTION);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();
        button1 = new JButton();
        button2 = new JButton();
        panel2 = new JPanel();
        scrollPane2 = new JScrollPane();
        table1 = new JTable();
        panel3 = new JPanel();
        scrollPane3 = new JScrollPane();
        table2 = new JTable();
        panel4 = new JPanel();
        scrollPane4 = new JScrollPane();
        table3 = new JTable();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("semantic_analyze");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {475, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 268, 0};
                ((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0E-4};
                ((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

                //======== tabbedPane1 ========
                {

                    //======== panel1 ========
                    {
                        panel1.setLayout(new GridBagLayout());
                        ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {196, 121, 211, 0, 0};
                        ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 262, 0, 0, 0};
                        ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};
                        ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};

                        //======== scrollPane1 ========
                        {
                            scrollPane1.setViewportView(textArea1);
                        }
                        panel1.add(scrollPane1, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- button1 ----
                        button1.setText("\u9009\u62e9\u6587\u4ef6");
                        button1.addActionListener(e -> button1ActionPerformed(e));
                        panel1.add(button1, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                        //---- button2 ----
                        button2.setText("\u8bed\u4e49\u5206\u6790");
                        button2.addActionListener(e -> button2ActionPerformed(e));
                        panel1.add(button2, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));
                    }
                    tabbedPane1.addTab("\u5f85\u5206\u6790\u4ee3\u7801", panel1);

                    //======== panel2 ========
                    {
                        panel2.setLayout(new GridBagLayout());
                        ((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {348, 0, 0};
                        ((GridBagLayout)panel2.getLayout()).rowHeights = new int[] {328, 0, 0};
                        ((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0E-4};
                        ((GridBagLayout)panel2.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

                        //======== scrollPane2 ========
                        {

                            //---- table1 ----
                            table1.setModel(new DefaultTableModel(
                                new Object[][] {
                                },
                                new String[] {
                                    "\u5e8f\u53f7", "\u56db\u5143\u5f0f", "\u4e09\u5730\u5740"
                                }
                            ));
                            scrollPane2.setViewportView(table1);
                        }
                        panel2.add(scrollPane2, new GridBagConstraints(0, 0, 2, 2, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
                    }
                    tabbedPane1.addTab("\u4e09\u5730\u5740\u548c\u56db\u5143\u5f0f", panel2);

                    //======== panel3 ========
                    {
                        panel3.setLayout(new GridBagLayout());
                        ((GridBagLayout)panel3.getLayout()).columnWidths = new int[] {399, 0, 0};
                        ((GridBagLayout)panel3.getLayout()).rowHeights = new int[] {320, 0, 0};
                        ((GridBagLayout)panel3.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0E-4};
                        ((GridBagLayout)panel3.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

                        //======== scrollPane3 ========
                        {

                            //---- table2 ----
                            table2.setModel(new DefaultTableModel(
                                new Object[][] {
                                },
                                new String[] {
                                    "\u8868\u53f7", "\u7b26\u53f7", "\u504f\u79fb", "\u7c7b\u578b"
                                }
                            ));
                            scrollPane3.setViewportView(table2);
                        }
                        panel3.add(scrollPane3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));
                    }
                    tabbedPane1.addTab("\u7b26\u53f7\u8868", panel3);

                    //======== panel4 ========
                    {
                        panel4.setLayout(new GridBagLayout());
                        ((GridBagLayout)panel4.getLayout()).columnWidths = new int[] {383, 0, 0};
                        ((GridBagLayout)panel4.getLayout()).rowHeights = new int[] {320, 0, 0};
                        ((GridBagLayout)panel4.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0E-4};
                        ((GridBagLayout)panel4.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

                        //======== scrollPane4 ========
                        {

                            //---- table3 ----
                            table3.setModel(new DefaultTableModel(
                                new Object[][] {
                                },
                                new String[] {
                                    "\u9519\u8bef"
                                }
                            ));
                            scrollPane4.setViewportView(table3);
                        }
                        panel4.add(scrollPane4, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));
                    }
                    tabbedPane1.addTab("\u9519\u8bef\u62a5\u544a", panel4);
                }
                contentPanel.add(tabbedPane1, new GridBagConstraints(0, 0, 2, 2, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
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
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    private JButton button1;
    private JButton button2;
    private JPanel panel2;
    private JScrollPane scrollPane2;
    private JTable table1;
    private JPanel panel3;
    private JScrollPane scrollPane3;
    private JTable table2;
    private JPanel panel4;
    private JScrollPane scrollPane4;
    private JTable table3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
