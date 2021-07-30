package grammer_analyze;

import java.util.ArrayList;
import java.util.List;

public class treenode implements Cloneable{
    private String val;  // 文法符号
    private List<treenode> allchild;  // 节点的所有子节点
    private String key;  // 属性值，对于有value值得文法符号才存在
    private int line;  // 行号

    /**
     * 初试化树的一个节点
     * @param val 树的文法符号
     * @param line 输的行数
     */
    public treenode(String val, int line) {
        this.val = val;
        this.allchild = new ArrayList<>();
        this.key = "";
        this.line = line;
    }

    public treenode(String val, String key, int line) {
        this.val = val;
        this.allchild = new ArrayList<>();
        this.key = key;
        this.line = line;
    }

    public void addchild(treenode child){
        allchild.add(child);
    }

    public String getVal() {
        return val;
    }

    public List<treenode> getAllchild() {
        return allchild;
    }

    public String getKey() {
        return key;
    }

    public int getLine() {
        return line;
    }

    @Override
    public Object clone() {
        treenode o = null;
        try {
            o = (treenode) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println(e.toString());
        }
        return o;
    }
}
