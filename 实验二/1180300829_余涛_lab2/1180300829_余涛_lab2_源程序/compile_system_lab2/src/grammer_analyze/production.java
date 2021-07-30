package grammer_analyze;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class production {
    private String production_left;  // 产生式左部
    private List<String> production_right_list;  // 产生式右部

    /**
     * 对输入的产生式进行分解，储存产生式左部和右部
     * @param s 产生式字符串
     */
    public production(String s) {
        this.production_right_list = new ArrayList<>();
        if (!s.contains("->")){
            System.out.println("产生式不符合规范");
        }
        String[] temp = s.split("->");  //首先用->分隔
        this.production_left = temp[0].trim();
        String right = temp[1];
        if (right.contains(" ")){
            String[] temp1 = right.split(" ");  // 然后用空格分隔
            for (String value : temp1) {
                this.production_right_list.add(value.trim());
            }
        }else{
            this.production_right_list.add(right.trim());
        }
    }

    public void setProduction_left(String production_left) {
        this.production_left = production_left;
    }

    public void setProduction_right_list(List<String> production_right_list) {
        this.production_right_list = production_right_list;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(production_left + "->");
        for (int i = 0; i < production_right_list.size(); i++){
            result.append(" ");
            result.append(production_right_list.get(i));
        }
        return result.toString().trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        production that = (production) o;
        return Objects.equals(production_left, that.production_left) &&
                Objects.equals(production_right_list, that.production_right_list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(production_left, production_right_list);
    }

    public String getProduction_left() {
        return production_left;
    }

    public List<String> getProduction_right_list() {
        return production_right_list;
    }

}
