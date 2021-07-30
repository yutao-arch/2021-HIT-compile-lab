package Lexical_analyze;

import java.util.HashMap;
import java.util.Map;

public class words {

    /*
     * 种别码的初始化函数
     */
    public static void init(){
        for (int i = 0; i < keywords.length; i++) {  // 关键字初始化
            keywords_node.put(keywords[i], 101 + i);
        }
        for (int i = 0; i < operators.length; i++) {  // 操作符初始化
            operators_node.put(operators[i], 201 + i);
        }
        for (int i = 0; i < delimiters.length; i++) {  // 界符初始化
            delimiters_node.put(delimiters[i], 301 + i);
        }
    }

    // 关键字
    public static String[] keywords = new String[] {
            "auto", "double", "int", "struct",
            "break", "else", "long", "switch", "case", "enum", "register",
            "typedef", "char", "extern", "return", "union", "const", "float",
            "short", "unsigned", "continue", "for", "signed", "void",
            "default", "goto", "sizeof", "volatile", "do", "if", "while",
            "static", "String", "declare",
    "proc", "record", "integer", "real", "then", "call", "begin", "end", "true", "false", "and", "or",  // 实验二补充
    "bool"};

    // 设置关键字-种别码对照表：101开始
    public static Map<String, Integer> keywords_node = new HashMap<String, Integer>();

    // 判断是否为关键字
    public static Boolean iskeywords(String s){
        return keywords_node.containsKey(s);
    }

    // 运算符：算数运算符，关系运算符，逻辑运算符
    public static String[] operators = new String[] {
            "+", "-", "*", "/", "%", "++", "--",
            "<", "<=", ">", ">=", "==", "!=",
            "&&", "||", "!","~", "&", "|", "^", ">>", "<<",
            "+=", "-=", "*=", "/=", "%=", "&=", " ^=", "|=", "<<=", ">>="};

    // 设置运算符-种别码对照表：201开始
    public static Map<String, Integer> operators_node = new HashMap<String, Integer>();

    // 判断是否为运算符
    public static Boolean isoperators(String s){
        return operators_node.containsKey(s);
    }

    // 界符
    public static String[] delimiters = new String[] {"=", ",", ";", "[", "]", "{", "}", "(", ")",
            "."};  // 为何要识别.；；；；；；；；；；；；；；

    // 设置界符-种别码对照表：301开始
    public static Map<String, Integer> delimiters_node = new HashMap<String, Integer>();

    // 判断是否为界符
    public static Boolean isdelimiters(String s){
        return delimiters_node.containsKey(s);
    }

    // 字符串转为大写
    public static String toUpper(String s){
        return s.toUpperCase();
    }

    // 判断该字符后面是否可以加"="
    public static Boolean canAddEqual(char ch){
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '=' || ch == '>'
                || ch == '<' || ch == '&' || ch == '|'  || ch == '^' || ch == '%' || ch == '!' ;
    }

    // 判断该字符后面是否可以加上自己的字符
    public static Boolean canAddSame(char ch)
    {
        return ch == '+' || ch == '-' || ch == '&' || ch == '|' || ch == '>' || ch == '<';
    }

    // 判断该字符是否为标识符的组成之一：字母或下划线
    public static Boolean isAlpha(char ch)
    {
        return ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_');
    }

    // 判断该字符是否为标识符的组成之一：数字0~9
    public static Boolean isDigit(char ch)
    {
        return (ch >= '0' && ch <= '9');
    }

    // 判断该字符是都可以前面加上"\"
    public static Boolean isEsSt(char ch)
    {
        return ch == 'a' || ch == 'b' || ch == 'f' || ch == 'n' || ch == 'r'
            || ch == 't' || ch == 'v' || ch == '?' || ch == '0' ||
            ch == '\"'; // 纠结要不要ch == '\"'
}

    // String DFA : a为能转义的字符，b代表除\和"之外的字符
    public static String stringDFA[] =
            {
                    "#\\b#",
                    "##a#",
                    "#\\b\"",
                    "####"
            };

    /**
     * 字符串DFA状态匹配函数
     * @param ch 当前字符
     * @param key 状态表中的字符
     * @return 匹配成功返回true，否则返回false
     */
    public static Boolean is_string_state(char ch, char key)
    {
        if (key == 'a')
            return isEsSt(ch);
        if (key == '\\')
            return ch == key;
        if (key == '"')
            return ch == key;
        if (key == 'b')
            return ch != '\\' && ch != '"';
        return false;
    }

    // char DFA :a为能转义的字符，b代表除\和'之外的字符
    public static String charDFA[] =
            {
                    "#\\b#",
                    "##a#",
                    "###\'",
                    "####"
            };

    /**
     * 字符DFA状态匹配函数
     * @param ch 当前字符
     * @param key 状态表中的字符
     * @return 匹配成功返回true，否则返回false
     */
    public static Boolean is_char_state(char ch, char key)
    {
        if (key == 'a')
            return isEsSt(ch);
        if (key == '\\')
            return ch == key;
        if (key == '\'')
            return ch == key;
        if (key == 'b')
            return ch != '\\' && ch != '\'';
        return false;
    }

    // 八进制和十六进制无符号整数DFA
    // c为[0-7]，d为[0-9a-fA-F]
    public static String eightsixDFA[] =
            {
                    "#cx#",
                    "#c##",
                    "###d",
                    "###d",
            };

    /**
     * 八进制和十六进制无符号整数DFA状态匹配函数
     * @param ch 当前字符
     * @param test 状态表中的字符
     * @return 匹配成功返回true，否则返回false
     */
    public static Boolean is_eightsix_state(char ch, char test){
        if (test == 'c'){
            return (ch >= '0' && ch <= '7');
        }
        if (test == 'x'){
            return ch == test;
        }
        if (test == 'd'){
            return isDigit(ch) || (ch >= 'a' && ch <='f') || (ch >= 'A' && ch <='F');
        }
        return false;
    }


    // 十进制无符号数DFA，包括小数
    public static String digitDFA[] =
            {
                    "#d#####",
                    "#d.#e##",
                    "###d###",
                    "###de##",
                    "####+-d",
                    "######d",
                    "######d"
            };

    /**
     * 数字DFA状态匹配函数
     * @param ch 当前字符
     * @param test 状态表中的字符
     * @return 匹配成功返回1，否则返回0
     */
    public static int is_digit_state(char ch, char test)
    {
        if (test == 'd')
        {
            if (isDigit(ch))
                return 1;
            else
                return 0;
        }
        else if (test == '+')
        {
            if (ch == '+')
                return 1;
            else
                return 0;
        }
        else if (test == '-')
        {
            if (ch == '-')
                return 1;
            else
                return 0;
        }
        else if (test == 'e')
        {
            if (ch == 'e' || ch == 'E')
                return 1;
            else
                return 0;
        }
        else
        {
            if (ch == test)
                return 1;
            else
                return 0;
        }
    }

    // 注释DFA：c表示[.|\r|\n]
    public static String noteDFA[] =
            {
                    "#####",
                    "##*##",
                    "##c*#",
                    "##c*/",
                    "#####"
            };

    /**
     * 注释DFA状态匹配函数
     * @param ch 当前字符
     * @param nD 状态表中的字符
     * @param s 状态
     * @return 匹配成功返回true，否则返回false
     */
    public static Boolean is_note_state(char ch, char nD, int s)
    {
        if (s == 2)
        {
            if (nD == 'c')
            {
                if (ch != '*')
                    return true;
                else
                    return false;
            }
        }
        if (s == 3)
        {
            if (nD == 'c')
            {
                if (ch != '*' && ch != '/')
                    return true;
                else
                    return false;
            }
        }
        return (ch == nD) ? true : false;
    }
}
