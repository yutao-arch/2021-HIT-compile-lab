package grammer_analyze;

import java.io.*;
import java.util.*;

public class grammer_init {
    public static List<String> non_terminal = new ArrayList<>();  // 非终结符集合
    public static List<String> terminal = new ArrayList<>();  // 终结符集合
    public static List<String> all_symbol = new ArrayList<>();  // 所有符号
    public static Map<String, HashSet<String>> first = new HashMap<>();  // first集
    public static List<production> allproduction = new ArrayList<>();  // 文件中所有的产生式


    // 初始化所有符号
    public static void init_all_symbol(){
        for (production temp : allproduction){
            if (!all_symbol.contains(temp.getProduction_left())){
                all_symbol.add(temp.getProduction_left());
            }
        }
        for (production temp : allproduction){
            for (String a : temp.getProduction_right_list()){
                if (!all_symbol.contains(a)){
                    all_symbol.add(a);
                }
            }
        }
    }


    // 初始化非终结符
    public static void init_non_terminal(){
        for (production temp : allproduction){
            if (!non_terminal.contains(temp.getProduction_left())){
                non_terminal.add(temp.getProduction_left());
            }
        }
    }

    // 初始化终结符
    public static void init_terminal(){
        all_symbol.removeIf(temp -> non_terminal.contains(temp));  // 从所有符号中去除掉非终结符
        terminal.addAll(all_symbol);

    }

    // 读取文件中的所有产生式到allproduction中
    public static void readfromfile(String filename) {
        File myfile = new File(filename);
        if (myfile.isFile() && myfile.exists()) {
            try {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(myfile), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    String[] temp = lineTxt.split("->");
                    if (temp[1].contains("丨")){
                        String[] right = temp[1].split("丨");
                        for (String s : right) {
                            allproduction.add(new production(temp[0] + "->" + s.trim()));
                        }
                    }else{
                        allproduction.add(new production(temp[0] + "->" + temp[1].trim()));
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 计算first集
    public static void buildfirst(){
        for (String temp : terminal){  // 终结符的first为本身
            first.put(temp, new HashSet<>());
            first.get(temp).add(temp);
        }
        for (String temp : non_terminal){  // 终结符的first
            first.put(temp, new HashSet<>());
            first.get(temp).addAll(findfirst(temp));
        }
    }


    // 计算某个非终结符的first集
    public static Set<String> findfirst(String temp) {
        Set<String> set = new HashSet<>();
        int size1;
        int size2;
        do{
            size1 = set.size();
            for (production d : allproduction){
                if (d.getProduction_left().equals(temp)){
                    if (terminal.contains(d.getProduction_right_list().get(0))){  // 产生式右部第一个符号为终结符直接加入
                        set.add(d.getProduction_right_list().get(0));
                    }else if (d.getProduction_right_list().get(0).equals("ε")){  // 产生式右部第一个符号为ε直接加入
                        set.add("ε");
                    }else if (non_terminal.contains(d.getProduction_right_list().get(0))){  // 产生式右部第一个符号为非终结符，递归
                        if (!temp.equals(d.getProduction_right_list().get(0))){  // 去除左递归
                            Set<String> set1 = findfirst(d.getProduction_right_list().get(0));
                            set.addAll(set1);
                            if (set1.contains("ε")){  // 如果含有ε，继续往后递归
                                for (int j = 1; j < d.getProduction_right_list().size(); j++){
                                    Set<String> set2 = findfirst(d.getProduction_right_list().get(j));
                                    set.addAll(set2);
                                    if (!set2.contains("ε")){
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            size2 = set.size();
        } while (size1 != size2);
        return set;
    }

}
