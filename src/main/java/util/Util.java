package util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;

public class Util {

    public static ConfigData getConfigData(){
        String dir = "src/main/resources";
        // List dir
        String[] files = new java.io.File(dir).list();

        HashMap<String, String[]> topo = new HashMap<>();
        HashMap<String, String> names = new HashMap<>();

        for (String file : files) {
            if (file.endsWith(".json")) {
                try {
                    JSONObject jsonObject = readFile(dir + "/" + file);

                    if (jsonObject == null) {
                        System.out.println("File not found");
                        return null;
                    }

                    String type = (String) jsonObject.get("type");

                    if (type.equals("topo")){
                        topo = readTopoConfig((JSONObject) jsonObject.get("config"));
                    }else if (type.equals("names")) {
                        names = readnamesConfig((JSONObject) jsonObject.get("config"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return new ConfigData(topo, names);
    }

    private static HashMap<String, String[]> readTopoConfig(JSONObject jsonObject) {
        try {
            HashMap<String, String[]> topo = new HashMap<>();
            for (Object key : jsonObject.keySet()) {
                String topoKey = (String) key;
                JSONArray topoValues = (JSONArray) jsonObject.get(topoKey);
                String[] topoValueArray = (String[]) topoValues.toArray(new String[topoValues.size()]);
                topo.put(topoKey, topoValueArray);
            }
            return topo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static HashMap<String, String> readnamesConfig(JSONObject jsonObject) {
        try {
            HashMap<String, String> names = new HashMap<>();

            for (Object key : jsonObject.keySet()) {
                String topoKey = (String) key;
                String topoValue = (String) jsonObject.get(topoKey);
                names.put(topoKey, topoValue);
            }
            return names;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static JSONObject readFile(String pathname) {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(pathname));
            JSONObject jsonObject = (JSONObject) obj;
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}