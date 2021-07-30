package grammer_analyze;

import java.util.Objects;

public class state_production implements Cloneable{
    private production the_production;  // 产生式
    private String looking_forward_operator;  // 展望符
    private int index;  // .的位置

    /**
     * 在自动机中的每一个产生式的状态
     * @param the_production 产生式
     * @param looking_forward_operator 展望符
     * @param index .的位置
     */
    public state_production(production the_production, String looking_forward_operator, int index) {
        this.the_production = the_production;
        this.looking_forward_operator = looking_forward_operator;
        this.index = index;
    }

    public void setThe_production(production the_production) {
        this.the_production = the_production;
    }

    public void setLooking_forward_operator(String looking_forward_operator) {
        this.looking_forward_operator = looking_forward_operator;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer(the_production.getProduction_left() + "->");
        int length = the_production.getProduction_right_list().size();
        for (int i = 0; i < length; i++){
            if (length == 1 && the_production.getProduction_right_list().get(0).equals("ε")){  // 如果产生式右部只有ε
                result.append(" ε.");
                break;
            }else{
                result.append(" ");
                if (i == index){
                    result.append(".");
                }
                result.append(the_production.getProduction_right_list().get(i));
            }
        }
        if (index == length && !the_production.getProduction_right_list().get(0).equals("ε")){
            result.append(".");
        }
        result.append(" ,");
        result.append(looking_forward_operator);
        return result.toString().trim();
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        state_production that = (state_production) o;
        return index == that.index &&
                Objects.equals(the_production, that.the_production) &&
                Objects.equals(looking_forward_operator, that.looking_forward_operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(the_production, looking_forward_operator, index);
    }

    @Override
    public Object clone() {
        state_production o = null;
        try {
            o = (state_production) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println(e.toString());
        }
        return o;
    }

    public production getThe_production() {
        return the_production;
    }

    public String getLooking_forward_operator() {
        return looking_forward_operator;
    }

    public int getIndex() {
        return index;
    }

}
