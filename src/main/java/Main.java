import util.Util;

import java.util.Arrays;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        HashMap<String, String[]> topo =  Util.readTopoConfig();
        HashMap<String, String> names = Util.readnamesConfig();

        for (String key : topo.keySet()) {
            String[] values = topo.get(key);
            System.out.println(key + " -> " + Arrays.toString(values));
        }
        for (String key : names.keySet()) {
            String value = names.get(key);
            System.out.println(key + " -> " + value);
        }
    }
}
