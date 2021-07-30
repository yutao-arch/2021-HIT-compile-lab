package Lexical_analyze;

public class Token {
    public final int tag;  //种别码
    public final String value;  // 单词
    public final int line;  // 行数

    public Token(int tag, String value, int line) {
        this.tag = tag;
        this.value = value;
        this.line = line;
    }

    // 返回Token序列
    @Override
    public String toString() {
        if (tag == 1 || tag == 2 || tag == 3 || tag == 4 || tag == 5 || tag == 6 || tag == 7){  //标识符、无符号整型（包括八进制和十六进制）、浮点型常量、科学计数法、字符常量、字符串常量、布尔型常量
            return line + "  " + value + "  < " + tag + ", " + value + " > ";
        }
        else{
            return line + "  " + value + "  < " + tag + ",  - > ";
        }
    }
}
