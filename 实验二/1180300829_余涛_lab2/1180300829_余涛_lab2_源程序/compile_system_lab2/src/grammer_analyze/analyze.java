package grammer_analyze;

import Lexical_analyze.Lexical;
import Lexical_analyze.Token;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class analyze {
    private String text;  // 文法文件中的文法
    private List<String> allerror;  // 所有的错误
    private List<String> statute;  // 所有规约的式子
    private StringBuffer result = new StringBuffer();  // 语法分析的结果
    private List<Token> alltoken;  // 所有Token值
    private List<String> alltokenvalue;  // 转化为文法符号的Token值,加$
    private Stack<Integer> statestack;  // 状态栈
    private Stack<String> keystack;  // 符号栈
    private Stack<treenode> treestack;  // 树栈
    private String begin_key;  // 开始符号
    private static String init_key = "$";
    private static String error = "--";  // 错误符号
    private static String acc = "acc";  // ACC，接收成功符号\
    private Lexical myLexical;  //创建Lexical进行判断

    public analyze() {
        this.alltokenvalue = new ArrayList<>();
        this.statestack = new Stack<>();
        this.keystack = new Stack<>();
        this.treestack = new Stack<>();
        this.allerror = new ArrayList<>();
        this.statute = new ArrayList<>();
    }

    // 读取所有文法
    public void allproduction_tostring() {
        StringBuilder allproductions = new StringBuilder();
        for (production temp : grammer_init.allproduction){
            allproductions.append(temp.toString()).append("\n");
        }
        text = allproductions.toString();
    }

    // 词法分析
    public void lexical_analyze(String filename) {
        myLexical = new Lexical();
        myLexical.readfromfile(filename);
        myLexical.lex();
        myLexical.show();
        alltoken = myLexical.allTokenClass;
        for (Token temp : alltoken) {
            // 加上这个符号
            alltokenvalue.add(getValue(temp));
        }
        alltokenvalue.add(init_key);  // 上一行加上$
    }

    // 返回种别码对应的文法单词
    private String getValue(Token valueType) {
        try {
            int code = valueType.tag;
            if (code == 1)
                return "id";
            else if (code == 2 || code == 3 || code == 4)
                return "num";
            else if (code == 5)
                return "char";
            else if (code == 6)
                return "string";
            else if (code < 400 && code >= 101)
                return valueType.value;
            else if (valueType.value.equals("$"))
                return "$";
            else
                return " ";
        } catch (Exception NullPointerException) {
            return "";
        }
    }

    // 语法分析
    public void grammer_analyze(String filename) {
        int i = 0;
        build_table grammer_ana = new build_table(filename);
        allproduction_tostring();  // 得到文法的所有产生式
        this.begin_key = grammer_ana.getBegin_key();
        Pattern pattern1 = Pattern.compile("(s)([0-9]*)");  // s移入
        Pattern pattern2 = Pattern.compile("(r)([0-9]*)");  // r规约
        statestack.push(0);  // 初试状态为0
        keystack.push(init_key);  // 将$首先入栈
        treestack.push(new treenode("begin", -100));
        System.out.println("规约的产生式如下：");
        while (true){
            int s = statestack.peek();
            Matcher matcher1 = pattern1.matcher(grammer_ana.ACTION(s, alltokenvalue.get(i)));
            Matcher matcher2 = pattern2.matcher(grammer_ana.ACTION(s, alltokenvalue.get(i)));
            if (matcher1.matches()){
                if (matcher1.group(1).equals("s")) {  // 匹配移入
                    statestack.push(Integer.parseInt(matcher1.group(2)));
                    keystack.push(alltokenvalue.get(i));
                    if (alltokenvalue.get(i).equals("num") || alltokenvalue.get(i).equals("id") ||  alltokenvalue.get(i).equals("string") || alltokenvalue.get(i).equals("char") ){  // 对于有属性值的情况
                        treestack.push(new treenode(alltokenvalue.get(i), alltoken.get(i).value, alltoken.get(i).line));
                    }else{
                        treestack.push(new treenode(alltokenvalue.get(i), alltoken.get(i).line));
                    }
                    i++;  // 只有移入操作才往后读入符号
                }
            }else if (matcher2.matches()) {
                production temp_production = grammer_init.allproduction.get(Integer.parseInt(matcher2.group(2)));
                System.out.println(temp_production.toString());
                statute.add(temp_production.toString());  // 用到的所有的规约的产生式
                if (temp_production.getProduction_right_list().get(0).equals("ε")){  // 空产生式的情况
                    keystack.push(temp_production.getProduction_left());
                    // 父亲节点连接空节点
                    treenode fathernode = new treenode(temp_production.getProduction_left(), -1);  // 空产生式行号默认为-1
                    fathernode.addchild(new treenode("ε", -1));
                    treestack.push(fathernode);
                    s = statestack.peek();
                    statestack.push(grammer_ana.GOTO(s, keystack.peek()));
                }else {  // 正常产生式的情况,如果为空产生式不需要出栈的操作
                    int right_size = temp_production.getProduction_right_list().size();
                    List<treenode> tempnodelist = new ArrayList<>();
                    for (int j = 0; j < right_size; j++) {
                        statestack.pop();
                        keystack.pop();
                        tempnodelist.add((treenode)treestack.pop().clone());
                    }
                    // 父亲节点连接所有规约前的子节点
                    treenode fathernode = new treenode(temp_production.getProduction_left(), tempnodelist.get(tempnodelist.size() - 1).getLine());
                    for (int k = tempnodelist.size() - 1; k >= 0; k--){
                        treenode aa = tempnodelist.get(k);
                        fathernode.addchild(aa);
                    }
                    keystack.push(temp_production.getProduction_left());
                    treestack.push(fathernode);
                    s = statestack.peek();
                    statestack.push(grammer_ana.GOTO(s, keystack.peek()));
                }
            }else if (grammer_ana.ACTION(s, alltokenvalue.get(i)).equals(acc)){  // 语法分析成功
                System.out.println("语法分析成功");
//                i++;
                break;
            }else{  // 遇到错误，错误处理
                System.out.println("遇到了错误进行错误处理");
                if (i < alltokenvalue.size() - 1){
                    allerror.add("Syntax error at Line " + "[" + alltoken.get(i).line + "]" + ": [单词 " + alltoken.get(i).value + " 遇到了错误]");
                }
                System.out.println("Syntax error at Line " + "[" + alltoken.get(i).line + "]" + ": [单词 " + alltoken.get(i).value + " 遇到了错误]");
                boolean flag = false;
                while (!statestack.isEmpty()){
                    for (int p = grammer_init.non_terminal.size() - 1; p >= 0; p--){  // 对所有非终结符，判断GOTO表中是否有对应的转移状态
//                    for (int p = 0; p < grammer_init.non_terminal.size(); p++){  // 对所有非终结符，判断GOTO表中是否有对应的转移状态
                        String nt = grammer_init.non_terminal.get(p);
                        if (grammer_ana.GOTO(s, nt) != -1){  // 对每一个GOTO表中可以转移后的状态判断输入字符中是否含有可以接受的字符
                            int temp_s = grammer_ana.GOTO(s, nt);
                            List<String> behind = new ArrayList<>();
                            List<String> terminal_initial = new ArrayList<>(grammer_init.terminal);
                            terminal_initial.add(init_key);
                            for (String t : terminal_initial){  // 得到该状态下能接受的所有终结符，包括$，用于输入符号的移除
                                if (!grammer_ana.ACTION(temp_s, t).equals(error)){
                                    behind.add(t);
                                }
                            }
                            for (int j = i; j < alltokenvalue.size(); j++){  // 遍历还剩下的输入串，看是否有能接受的终结符
                                if (behind.contains(alltokenvalue.get(j))){
                                    i = j;  // 找到下一个输入字符的位置
                                    statestack.push(temp_s);
                                    int pre_line = -100;  // 错误处理规约的非终结符的行数
//                                    int pre_line;  // 错误处理规约的非终结符的行数
//                                    if (!treestack.isEmpty()){  // 如果栈不空，则为上一个字符所在行数
//                                        pre_line = treestack.peek().getLine();
//                                    }else{  // 栈为空行数设置为-100，并且树栈中第一个起始点的行数也为-100
//                                        pre_line = -100;
//                                    }
                                    keystack.push(nt);
                                    treestack.push(new treenode(nt, pre_line));  // 行数为树栈上一个字符的行数
                                    flag = true;
                                    break;
                                }
                            }
                            if (flag){
                                break;
                            }
                        }
                    }
                    if (flag){
                        break;
                    }else{  // 依次弹出栈顶元素
                        statestack.pop();
                        keystack.pop();
                        treestack.pop();
                        if (statestack.isEmpty()){
                            break;
                        }
                        s = statestack.peek();
                    }
                }
                if (statestack.isEmpty()){  // 所有状态栈中的状态都没有对应的GOTO表中非空
                    System.out.println("该错误无法调用错误处理进行处理");
                    break;
                }
            }
        }
    }


    // 得到输出结果
    public void printtree(treenode root, String can){
        if (root == null){
            return;
        }
        if (root.getLine() == -1){  // 空产生式
            result.append(can).append(root.getVal()).append("\n");
            System.out.println(can + root.getVal());
        }else if (!root.getKey().equals("")){  // 没有属性值
            result.append(can).append(root.getVal()).append("  :").append(root.getKey()).append("(").append(root.getLine()).append(")").append("\n");
            System.out.println(can + root.getVal() + "  :" + root.getKey() + "(" + root.getLine() + ")");
        }else{  // 有属性值
            result.append(can).append(root.getVal()).append("(").append(root.getLine()).append(")").append("\n");
            System.out.println(can + root.getVal() + "(" + root.getLine() + ")");
        }
        for (treenode child : root.getAllchild()){
            printtree(child, can + "      ");
        }
    }

    public List<Token> getAlltoken() {
        return alltoken;
    }

    public List<String> getAlltokenvalue() {
        return alltokenvalue;
    }

    public Stack<Integer> getStatestack() {
        return statestack;
    }

    public Stack<String> getKeystack() {
        return keystack;
    }

    public Stack<treenode> getTreestack() {
        return treestack;
    }

    public String getBegin_key() {
        return begin_key;
    }

    public static String getInit_key() {
        return init_key;
    }

    public static String getError() {
        return error;
    }

    public static String getAcc() {
        return acc;
    }

    public Lexical getMyLexical() {
        return myLexical;
    }

    public String getText() {
        return text;
    }

    public StringBuffer getResult() {
        return result;
    }

    public List<String> getStatute() {
        return statute;
    }

    public List<String> getAllerror() {
        return allerror;
    }

}
