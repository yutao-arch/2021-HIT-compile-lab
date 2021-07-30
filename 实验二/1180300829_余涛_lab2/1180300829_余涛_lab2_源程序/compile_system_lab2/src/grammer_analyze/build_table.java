package grammer_analyze;

import java.util.*;

public class build_table {
    private String begin_key;  // 文法开始符号
    private static String null_key = "ε";
    private static String init_key = "$";
    private static String error = "--";  // 错误符号
    private static String acc = "acc";  // ACC，接收成功符号
    private List<state_lr1> all_states_lr1 = new ArrayList<>();  // 所有状态
    private int statenum;  // lr1状态数
    private int actionLength;  // Action表列数
    private int gotoLength;  // GoTo表列数
    private String[] actionCol;  // Action表列名数组
    private String[] gotoCol;  // GoTo表列名数组
    private String[][] actionTable;  // Action表，与ppt一致，二维数组
    private int[][] gotoTable;  // GoTo表，与ppt不一样，包含所有的状态转化，二维数组，列中不含$

    //  当第x号DFA状态,输入S符号时,转移到第y号DFA状态,则:
    private ArrayList<Integer> gotoStart = new ArrayList<>();  // 存储第x号lr1状态
    private ArrayList<Integer> gotoEnd = new ArrayList<>();  // 存储第y号lr1状态
    private ArrayList<String> gotoPath = new ArrayList<>();  // 存储S符号

    /**
     * 构造分析表
     */
    public build_table(String filename) {
        grammer_init.readfromfile(filename);
        grammer_init.init_all_symbol();
        grammer_init.init_non_terminal();
        grammer_init.init_terminal();
        grammer_init.buildfirst();

        begin_key = grammer_init.allproduction.get(0).getProduction_left();
        create_header();  // 建表
        this.actionLength = actionCol.length;
        this.gotoLength = gotoCol.length;
        create_allstate();  // 创建状态集闭包
        this.statenum = all_states_lr1.size();
        this.gotoTable = new int[statenum][gotoLength];
        this.actionTable = new String[statenum][actionLength];
        create_analyze_table();  // 填充语法分析表的相关内容
    }


    /**
     * 利用这个方法建立一个LR(1)语法分析表的表头
     */
    private void create_header() {
        this.actionCol = new String[grammer_init.terminal.size() + 1];
        this.gotoCol = new String[grammer_init.non_terminal.size() + grammer_init.terminal.size()];
        int i;
        for (i = 0; i < grammer_init.terminal.size(); i++) {
            String vt = grammer_init.terminal.get(i);
            actionCol[i] = vt;
            gotoCol[i] = vt;
        }
        actionCol[i] = init_key;
        for (String vn : grammer_init.non_terminal) {
            gotoCol[i] = vn;
            i++;
        }
    }

    // 获得所有左部为left的产生式
    public List<production> get_production_by_left(String left) {
        List<production> result = new ArrayList<>();
        for (production d : grammer_init.allproduction) {
            if (d.getProduction_left().equals(left)) {
                result.add(d);
            }
        }
        return result;
    }

    // 获取文法符号v的first集
    private Set<String> first(String v) {
        Set<String> result = new HashSet<>();
        if (v.equals(init_key)) {
            result.add(init_key);
        } else {
            result.addAll(grammer_init.first.get(v));
        }
        return result;
    }

    /**
     * 利用这个递归方法建立一个用于语法分析的lr1自动机
     * 不再有项集可插入的判定标准为:
     */
    private void create_allstate() {
        state_lr1 state0 = new state_lr1(0);  // 首先建立state0
        List<production> byleft = get_production_by_left(begin_key);
        state0.add_state_production(new state_production(byleft.get(0), "$", 0));  // 首先加入S'-> S, $
        for (int i = 0; i < state0.getState_production_list().size(); i++) {  // 对项目集闭包中的每一个状态产生式
            state_production temp_state_production = state0.getState_production_list().get(i);
            int index = temp_state_production.getIndex();
            List<String> rightlist = temp_state_production.getThe_production().getProduction_right_list();
            if (index < rightlist.size()) {  // 不是规约状态
                String A = rightlist.get(index);  // 获取.后面的符号
                Set<String> firstB = new HashSet<>();  // “.B”后的first集合
                if (index == rightlist.size() - 1) {  // 如果形式为“A->.B, 展望符”的形式
                    firstB.add(temp_state_production.getLooking_forward_operator());
                } else {
                    int m;
                    for (m = index + 1; m < rightlist.size(); m++) { // “.B”后面的所有部分
                        Set<String> list1 = first(rightlist.get(m));
                        firstB.addAll(list1);
                        if (!list1.contains(null_key)) {  // 只要first包含ε，则往后继续添加
                            break;
                        }
                    }
                    if (m == rightlist.size()) {  // 如果“.B”后的所有非终结符都含有ε，则最后还需加入本来的展望符
                        firstB.add(temp_state_production.getLooking_forward_operator());
                    }
                }
                if (grammer_init.non_terminal.contains(A)) {  // 如果.后面为非终结符需要往产生式闭包中加入新的产生式
                    List<production> A_productions = get_production_by_left(A);  // 得到左部为A的所有产生式
                    for (production a_production : A_productions) {  // 对firstB中的每个符号将其作为展望符加入产生式中
                        for (String f : firstB) {
                            if (!f.equals(null_key)) {
                                state_production new_state_production;
                                if (a_production.getProduction_right_list().get(0).equals(null_key)) {
                                    new_state_production = new state_production(a_production, f, 1);
                                } else {
                                    new_state_production = new state_production(a_production, f, 0);
                                }
                                if (!state0.getState_production_list().contains(new_state_production)) {
                                    state0.add_state_production(new_state_production);
                                }
                            }
                        }
                    }
                }
            }
        }
        all_states_lr1.add(state0);  // 加入state0后开始递归建立其他状态
        List<String> goto_path = state0.get_goto();  // 得到state0中所有.后面的符号
        for (String path : goto_path) {
            List<state_production> list = state0.getLRDs(path);  // 得到该符号的所有状态产生式
            add_new_state(0, path, list); //开始进行递归，建立用于分析的lr1自动机
        }
    }

    /**
     * 通过输入一个从上一个状态传下来的LR产生式的list获取下一个状态，
     * 如果该状态已经存在，则不作任何操作，跳出递归，如果该状态不存在，则加入该状态，继续进行递归
     *
     * @param lastState 上一个状态的编号
     * @param path      输入的符号
     * @param list      新状态可以包含的所有产生式
     */
    private void add_new_state(int lastState, String path, List<state_production> list) {
        state_lr1 temp_state = new state_lr1(10000);
        for (state_production productionState : list) {
            productionState.setIndex(productionState.getIndex() + 1);
            temp_state.add_state_production(productionState);
        }
        for (int i = 0; i < temp_state.getState_production_list().size(); i++) {
            state_production temp_state_production = temp_state.getState_production_list().get(i);
            int index = temp_state_production.getIndex();
            List<String> rightlist = temp_state_production.getThe_production().getProduction_right_list();
            if (index != rightlist.size()) {  // 不是规约状态
                String A = rightlist.get(index);  // 获取.后面的符号
                Set<String> firstB = new HashSet<>();  // “.B”后的first集合
                if (index == rightlist.size() - 1) {  // 如果形式为“A->.B, 展望符”的形式
                    firstB.add(temp_state_production.getLooking_forward_operator());
                } else {
                    int m;
                    for (m = index + 1; m < rightlist.size(); m++) { // “.B”后面的所有部分
                        Set<String> list1 = first(rightlist.get(m));
                        firstB.addAll(list1);
                        if (!list1.contains(null_key)) {  // 只要first包含ε，则往后继续添加
                            break;
                        }
                    }
                    if (m == rightlist.size()) {  // 如果“.B”后的所有非终结符都含有ε，则最后还需加入本来的展望符
                        firstB.add(temp_state_production.getLooking_forward_operator());
                    }
                }
                if (grammer_init.non_terminal.contains(A)) {  // 如果.后面为非终结符需要往产生式闭包中加入新的产生式
                    List<production> A_productions = get_production_by_left(A);  // 得到左部为A的所有产生式
                    for (production a_production : A_productions) {  // 对firstB中的每个符号将其作为展望符加入产生式中
                        for (String f : firstB) {
                            if (!f.equals(null_key)) {
                                state_production new_state_production;
                                if (a_production.getProduction_right_list().get(0).equals(null_key)) {
                                    new_state_production = new state_production(a_production, f, 1);
                                } else {
                                    new_state_production = new state_production(a_production, f, 0);
                                }
                                if (!temp_state.getState_production_list().contains(new_state_production)) {
                                    temp_state.add_state_production(new_state_production);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < all_states_lr1.size(); i++) {  // 如果该状态已经存在
            if (all_states_lr1.get(i).toString().equals(temp_state.toString())) {
                gotoStart.add(lastState);
                gotoEnd.add(i);
                gotoPath.add(path);
                return;
            }
        }
        // 如果该状态不存在
        temp_state.setState_id(all_states_lr1.size());
        all_states_lr1.add(temp_state);
        gotoStart.add(lastState);
        gotoEnd.add(temp_state.getState_id());
        gotoPath.add(path);
        List<String> goto_path = temp_state.get_goto();  // 得到temp_state中所有.后面的符号
        for (String p : goto_path) {
            List<state_production> l = temp_state.getLRDs(p);  // 得到该符号的所有状态产生式
            add_new_state(temp_state.getState_id(), p, l); //开始进行递归，建立用于分析的lr1自动机
        }
    }

    // 返回goto中的列数
    private int gotoIndex(String s) {
        for (int i = 0; i < gotoLength; i++) {
            if (gotoCol[i].equals(s)) {
                return i;
            }
        }
        return -1;
    }

    // 返回action中的列数
    private int actionIndex(String s) {
        for (int i = 0; i < actionLength; i++) {
            if (actionCol[i].equals(s)) {
                return i;
            }
        }
        return -1;
    }

    //返回产生式下标
    private int getgoto_index(production d) {
        int size = grammer_init.allproduction.size();
        for (int i = 0; i < size; i++) {
            if (grammer_init.allproduction.get(i).equals(d)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 利用这个方法填充语法分析表的相关内容
     */
    private void create_analyze_table() {
        for (int i = 0; i < gotoTable.length; i++) {  // goto表初始化为-1
            for (int j = 0; j < gotoTable[0].length; j++) {
                gotoTable[i][j] = -1;
            }
        }
        for (int i = 0; i < actionTable.length; i++) {  // action表初始化为error
            for (int j = 0; j < actionTable[0].length; j++) {
                actionTable[i][j] = build_table.error;
            }
        }
        //完善语法分析表的goto部分
        for (int i = 0; i < gotoStart.size(); i++) {
            int start = gotoStart.get(i);
            int end = gotoEnd.get(i);
            String path = gotoPath.get(i);
            int path_index = gotoIndex(path);
            gotoTable[start][path_index] = end;
        }
        //完善语法分析表的action部分
        for (int i = 0; i < all_states_lr1.size(); i++) {
            state_lr1 state = all_states_lr1.get(i);
            for (state_production temp : state.getState_production_list()) {
                int index = temp.getIndex();
                List<String> rightlist = temp.getThe_production().getProduction_right_list();
                if (index == rightlist.size()) {  // 如果为规约状态
                    if (!temp.getThe_production().getProduction_left().equals(begin_key)) {  // 只要不是S’开头的产生式
                        String value = "r" + getgoto_index(temp.getThe_production());
                        actionTable[i][actionIndex(temp.getLooking_forward_operator())] = value;  // 规约
                    } else {
                        actionTable[i][actionIndex(init_key)] = build_table.acc; // 接受
                    }
                } else {  // 如果为待约状态
                    String next = rightlist.get(temp.getIndex());  // 获取·后面的文法符号
                    if (grammer_init.terminal.contains(next)) {  // 必须是一个终结符号
                        if (gotoTable[i][gotoIndex(next)] != -1) {  // 根据goto表来得到转移后的状态
                            actionTable[i][actionIndex(next)] = "s" + gotoTable[i][gotoIndex(next)];
                        }
                    }
                }
            }
        }
    }

    public String ACTION(int stateIndex, String vt) {
        if (stateIndex == -1) {
            return build_table.error;
        }
        int index = actionIndex(vt);
        if (index == -1){
            return build_table.error;
        }
        return actionTable[stateIndex][index];
    }

    public int GOTO(int stateIndex, String vn) {
        if (stateIndex == -1) {
            return -1;
        }
        int index = gotoIndex(vn);
        if (index == -1){
            return -1;
        }
        return gotoTable[stateIndex][index];
    }

    /**
     * 打印语法分析表
     */
    public String print() {
        StringBuilder result = new StringBuilder();
        StringBuilder colLine = new StringBuilder(form(""));
        for (String s : actionCol) {
            if (!s.equals("integer") && !s.equals("record"))
                colLine.append("\t");
            colLine.append(form(s));
        }
        for (int j = actionCol.length - 1; j < gotoCol.length; j++) {
            colLine.append("\t");
            colLine.append(form(gotoCol[j]));
        }
        result.append(colLine).append("\n");
        int index = 0;
        for (int i = 0; i < all_states_lr1.size(); i++) {
            StringBuilder line = new StringBuilder(form(String.valueOf(i)));
            while (index < actionCol.length) {
                line.append("\t");
                line.append(form(actionTable[i][index]));
                index++;
            }
            index = actionCol.length - 1;
            while (index < gotoCol.length) {
                line.append("\t");
                if (gotoTable[i][index] == -1) {
                    line.append(form("--"));
                } else {
                    line.append(form(String.valueOf(gotoTable[i][index])));
                }
                index++;
            }
            index = 0;
            line.append("\t");
            result.append(line).append("\n");

        }
        return result.toString();
    }

    public String getBegin_key() {
        return begin_key;
    }

    public String form(String str) {
        StringBuilder strBuilder = new StringBuilder(str);
        for (int i = 0; i < 9 - strBuilder.length(); i++) {
            strBuilder.append(" ");
        }
        str = strBuilder.toString();
        return str;
    }

}
