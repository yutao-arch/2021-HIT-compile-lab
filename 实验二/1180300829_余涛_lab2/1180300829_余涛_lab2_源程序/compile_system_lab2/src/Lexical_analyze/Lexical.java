package Lexical_analyze;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Lexical {
    public String text;  //待分析代码

    public List<String> allmistake = new ArrayList<>();  // 错误集合
    public List<Token> allTokenClass = new ArrayList<>();  // Token集合

    // 读文件
    public void readfromfile(String filename) {
        File myfile = new File(filename);
        if (myfile.isFile() && myfile.exists()) {
            try {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(myfile), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                StringBuilder tempstring = new StringBuilder();
                while ((lineTxt = br.readLine()) != null) {
                    tempstring.append(lineTxt).append("\n");
                }
                text = tempstring.delete(tempstring.length() - 1, tempstring.length()).toString();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void show() {
        System.out.println("Token序列如下：");
        for (Token temp : allTokenClass) {
            System.out.println(temp.toString());
        }
        System.out.println("错误信息如下：");
        for (String temp : allmistake) {
            System.out.println(temp);
        }
    }

    /**
     * 核心函数
     * 根据已经构成的DFA状态转换表
     * 按行分析数据，识别相应信息
     */
    public void lex() {
        words.init();
        String[] texts = text.split("\n");
        for (int m = 0; m < texts.length; m++) {
            String str = texts[m];
            if (str.equals(""))
                continue;
            else {
                char[] strline = str.toCharArray();
                for (int i = 0; i < strline.length; i++) {
                    char ch = strline[i];
                    if (ch == ' ') {
                        continue;
                    }
                    String token = "";
                    if (words.isAlpha(ch)) {  // 识别关键字和标识符
                        do {
                            token += ch;
                            i++;
                            if (i >= strline.length) {
                                break;
                            }
                            ch = strline[i];
                        } while (ch != '\0' && (words.isAlpha(ch) || words.isDigit(ch)));
                        int temp = m + 1;
                        if (words.iskeywords(token)) { // 识别关键字
                            Token tempToken = new Token(words.keywords_node.get(token), token, temp);
                            allTokenClass.add(tempToken);
//                        } else if (token.equals("true") || token.equals("false")) {  //识别boolean常量，实验二中将boolean常量当做关键字
//                            Token tempToken = new Token(7, token, temp);
//                            allTokenClass.add(tempToken);
                        } else {  // 识别标识符
                            Token tempToken = new Token(1, token, temp);
                            allTokenClass.add(tempToken);
                        }
                        i--;
                    } else if (words.isDigit(ch)) {  // 识别无符号数
                        int k;
                        String str1 = "";
                        if (ch == '0') {  // 识别八进制和十六进制整数
                            token = "";
                            int state = 0;
                            token += ch;
                            str1 += ch;
                            while ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || ch == 'x' || (ch >= 'A' && ch <= 'F')) {
                                i++;
                                if (i >= strline.length) {
                                    break;
                                }
                                ch = strline[i];
                                str1 += ch;
                                for (k = 0; k < 4; k++) {
                                    char tmpstr[] = words.eightsixDFA[state].toCharArray();
                                    if (words.is_eightsix_state(ch, tmpstr[k])) {
                                        token += ch;
                                        state = k;
                                        break;
                                    }
                                }
                                if (k == 4) {
                                    break;
                                }
                            }
                            if (state == 0) {
                                int temp = m + 1;
                                Token tempToken = new Token(2, token, temp); //八进制或十六进制整型常量
                                allTokenClass.add(tempToken);
                                i--;
                            } else if (state == 1 || state == 3) {
                                int temp = m + 1;
                                Token tempToken = new Token(2, token, temp); //八进制或十六进制整型常量
                                allTokenClass.add(tempToken);
                            } else {
                                int temp = m + 1;
                                allmistake.add("Lexical error at Line " + "[" + temp + "]" + ": [" + str1 + "八进制或十六进制整数不符合规范]");
                            }

                        } else {  // 识别无符号数
                            token = "";
                            int state = 1;
                            boolean isfloat = false;
                            boolean isSci_not = false;
                            while ((ch != '\0') && (words.isDigit(ch) || ch == '.' || ch == 'e'
                                    || ch == '-' || ch == 'E' || ch == '+')) {
                                if (ch == '.') {
                                    isfloat = true;
                                }
                                if (ch == 'e' || ch == 'E') {
                                    isfloat = false;
                                    isSci_not = true;
                                }
                                for (k = 0; k < 7; k++) {  // 进行当前状态的自动机识别并更换自动机状态
                                    char tmpstr[] = words.digitDFA[state].toCharArray();
                                    if (words.is_digit_state(ch, tmpstr[k]) == 1) {
                                        token += ch;
                                        state = k;
                                        break;
                                    }
                                }
                                if (k == 7) {
                                    break;
                                }
                                i++;
                                if (i >= strline.length) {
                                    break;
                                }
                                ch = strline[i];  // 读取下一个字符
                            }
                            boolean haveMistake = false;  // 识别词法错误
                            if (state == 2 || state == 4 || state == 5)  // 非终态
                            {
                                haveMistake = true;
                            } else { // 终态只能接受[0-9]
                                if ((ch == '.') || (!words.isoperators(String.valueOf(ch))
                                        && !words.isDigit(ch) && !words.isdelimiters(String.valueOf(ch)) && ch != ' ')) {
                                    haveMistake = true;
                                }
                            }
                            int temp = m + 1;
                            if (haveMistake) { // 错误处理
                                while (ch != '\0' && ch != ',' && ch != ';' && ch != ' ') {  // 若出现错误一直往后读
                                    token += ch;
                                    i++;
                                    if (i >= strline.length)
                                        break;
                                    ch = strline[i];
                                }
                                allmistake.add("Lexical error at Line " + "[" + temp + "]" + ": [" + token + "无符号数或标识符不合规范]");
                            } else {
                                if (isSci_not) {
                                    Token tempToken = new Token(4, token, temp); //科学计数法
                                    allTokenClass.add(tempToken);
                                } else if (isfloat) {
                                    Token tempToken = new Token(3, token, temp); //浮点型常量
                                    allTokenClass.add(tempToken);
                                } else {
                                    Token tempToken = new Token(2, token, temp); //整型常量
                                    allTokenClass.add(tempToken);
                                }
                            }
                            i--;
                        }
                    } else if (ch == '\'') {  //识别字符常量
                        token = "";
                        int state = 0;
                        token += ch;
                        int k;
                        boolean flag = false;
                        while (state != 3) {
                            i++;
                            if (i >= strline.length) {
                                break;
                            }
                            ch = strline[i];
                            for (k = 0; k < 4; k++) {
                                char tmpstr[] = words.charDFA[state].toCharArray();
                                if (state == 1 && k == 2 && !words.is_char_state(ch, tmpstr[k])){  // 为了防止读入非法的转义字符导致自动机停止
                                    token += ch;
                                    i++;
                                    ch = strline[i];
                                    if (ch == '\''){
                                        token += ch;
                                    }
                                    flag = true;
                                    break;
                                }
                                if (words.is_char_state(ch, tmpstr[k])) {
                                    token += ch;
                                    state = k;
                                    break;
                                }
                            }
                            if (k == 4) {
                                break;
                            }
                            if (flag){
                                break;
                            }
                        }
                        int temp = m + 1;
                        if (state != 3) {
                            allmistake.add("Lexical error at Line " + "[" + temp + "]" + ": [" + token + "字符常量引号未封闭或字符中转义字符后的字符不合法]");
                        } else {  //字符常量
//                            StringBuffer temphaha = new StringBuffer(token);  // 去掉引号的情况
//                            temphaha.delete(0, 1);
//                            temphaha.delete(temphaha.length() - 1, temphaha.length());
                            Token tempToken = new Token(5, token, temp);
                            allTokenClass.add(tempToken);
                        }
                    } else if (ch == '"') {  // 识别字符串常量
                        token = "";
                        token += ch;
                        int state = 0;
                        int k;
                        boolean haveMistake = false;
                        while (state != 3 && !haveMistake) {
                            i++;
                            if (i >= strline.length) {
                                break;
                            }
                            ch = strline[i];
                            for (k = 0; k < 4; k++) {
                                char tmpstr[] = words.stringDFA[state].toCharArray();
                                if (state == 1 && k == 2 && !words.is_string_state(ch, tmpstr[k])){  // 为了防止读入非法的转义字符导致自动机停止
                                    haveMistake = true;
                                    token += ch;
                                    while (ch != '\"'){
                                        i++;
                                        if (i >= strline.length) {
                                            break;
                                        }
                                        ch = strline[i];
                                        token += ch;
                                    }
                                    if (ch == '\"'){
                                        break;
                                    }
                                    if (ch == '\n'){
                                        break;
                                    }
                                }
                                if (haveMistake){
                                    break;
                                }
                                if (words.is_string_state(ch, tmpstr[k])) {
                                    token += ch;
                                    state = k;
                                    break;
                                }
                            }
                            if (k == 4) {
                                break;
                            }
                        }
                        int temp = m + 1;
                        if (state != 3) {
                            allmistake.add("Lexical error at Line " + "[" + temp + "]" + ": [" + token + "字符串常量引号未封闭或字符串中转义字符后的字符不合法]");
                            System.out.println(i);
                        } else {  // 字符串常量
                            Token tempToken = new Token(6, token, temp);
                            allTokenClass.add(tempToken);
                        }
                    } else if (ch == '/') {  // 识别注释
                        token = "";
                        token += ch;
                        i++;
                        int tempm = m;  // 定义/*所在的行
                        if (i >= strline.length) {
                            break;
                        }
                        ch = strline[i];
                        //不是多行注释及单行注释
                        if (ch != '*' && ch != '/') {
                            if (ch == '=')
                                token += ch; // 识别/=
                            else {
                                i--; // 指针回退 // /
                            }
                            int temp = m + 1;
                            Token tempToken = new Token(words.operators_node.get(token), token, temp);
                            allTokenClass.add(tempToken);
                        } else {
                            boolean haveMistake = false;
                            if (ch == '*') {  // 识别/*
                                token += ch;
                                int state = 2;
                                while (state != 4) {
                                    if (i == strline.length - 1) {  // 到了最后一位则换行跳过
                                        token += '\n';
                                        m++;
                                        if (m >= texts.length) {
                                            haveMistake = true;
                                            break;
                                        }
                                        str = texts[m];
                                        if (str.equals("")) {  // 不为空则对下一行分析
                                            continue;
                                        } else {
                                            strline = str.toCharArray();
                                            i = 0;
                                            ch = strline[i];
                                        }
                                    } else {
                                        i++;
                                        ch = strline[i];
                                    }
                                    for (int k = 2; k <= 4; k++) {
                                        char tmpstr[] = words.noteDFA[state].toCharArray();
                                        if (words.is_note_state(ch, tmpstr[k], state)) {
                                            token += ch;
                                            state = k;
                                            break;
                                        }
                                    }
                                }
                            } else {  // 识别//， 识别到直接跳过这一行即可
                                break;
                            }
                            if (haveMistake) {
                                int temp = tempm + 1;
                                allmistake.add("Lexical error at Line " + "[" + temp + "]" + ": [" + "/*" + "注释未封闭]");
                            }
                        }
                    } else if (words.isoperators(String.valueOf(ch)) || words.isdelimiters(String.valueOf(ch))) { // 运算符或者界符
                        token = "";
                        token += ch;
                        if (words.canAddEqual(ch)) { // 普通运算符后面都可以加上一个"="，用来判断是否运算符
                            i++;
                            if (i >= strline.length) {
                                break;
                            }
                            ch = strline[i];
                            if (ch == '=') {
                                token += ch;
                            } else {
                                if (words.canAddSame(strline[i - 1]) && ch == strline[i - 1]) { // 部分普通运算符后面也可以用一个和自己一样的
                                    token += ch;
                                } else {  // 不是则指针回退
                                    i--;
                                }
                            }
                        }
                        if (token.length() == 1) {  //若不满足后面能加等号则为界符
                            String signal = token;
                            int temp = m + 1;
                            if (words.isdelimiters(signal)) {
                                Token tempToken = new Token(words.delimiters_node.get(token), token, temp);
                                allTokenClass.add(tempToken);
                            } else {
                                Token tempToken = new Token(words.operators_node.get(token), token, temp);
                                allTokenClass.add(tempToken);
                            }
                        } else {
                            int temp = m + 1;
                            Token tempToken = new Token(words.operators_node.get(token), token, temp);
                            allTokenClass.add(tempToken);
                        }
                    } else {  // 不合法字符
                        int temp = m + 1;
                        allmistake.add("Lexical error at Line " + "[" + temp + "]" + ": [" + token + "存在不合法字符]");
                    }
                }
            }
        }
    }
}
