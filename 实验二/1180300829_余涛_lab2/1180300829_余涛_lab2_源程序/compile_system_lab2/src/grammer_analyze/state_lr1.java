package grammer_analyze;

import java.util.ArrayList;
import java.util.List;

public class state_lr1 {
    private int state_id;  // lr1状态号
    private List<state_production> state_production_list;  //lr1状态集闭包

    /**
     * 初始化一个LR1状态
     * @param state_id  LR1状态号
     */
    public state_lr1(int state_id) {
        this.state_id = state_id;
        this.state_production_list = new ArrayList<>();
    }

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }

    public void setState_production_list(List<state_production> state_production_list) {
        this.state_production_list = state_production_list;
    }

    /**
     * 给状态集中增加当前状态产生式
     * @param test_state_production 待增加的状态产生式
     * @return 增加是否成功
     */
    public boolean add_state_production(state_production test_state_production){
        if (state_production_list.contains(test_state_production)){
            return false;
        }else{
            state_production_list.add(test_state_production);
            return true;
        }
    }

    /**
     * 返回一个状态集中所有.后面的符号
     * @return 所有的.后面的符号
     */
    public List<String> get_goto(){
        List<String> result = new ArrayList<>();
        for (state_production temp : state_production_list){
            if (temp.getThe_production().getProduction_right_list().size() != temp.getIndex()){  // 非规约状态
                String s = temp.getThe_production().getProduction_right_list().get(temp.getIndex());  // .后面的符号
                if (!result.contains(s)){
                    result.add(s);
                }
            }
        }
        return result;
    }

    /**
     * 返回一个项目集中，s与项目集"."后面的符号相匹配的，所在的状态产生式
     * @param s 即将读入的符号
     * @return 状态产生式列表
     */
    public List<state_production> getLRDs(String s){
        List<state_production> result = new ArrayList<>();
        for (state_production temp : state_production_list){
            if (temp.getThe_production().getProduction_right_list().size() != temp.getIndex()) {  // 非规约状态
                String s1 = temp.getThe_production().getProduction_right_list().get(temp.getIndex());  // .后面的符号
                if (s1.equals(s)){
                    result.add((state_production)temp.clone());
                }
            }
        }
        return result;
    }


    @Override
    public String toString() {
        StringBuffer result = new StringBuffer("");
        int length = state_production_list.size();
        for (int i = 0; i < length; i++){
            result.append(state_production_list.get(i));
            if (i < length - 1){
                result.append("\n");
            }
        }
        return result.toString().trim();
    }

    public int getState_id() {
        return state_id;
    }

    public List<state_production> getState_production_list() {
        return state_production_list;
    }


}
