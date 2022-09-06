package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ConfigData {
    private HashMap<String, String[]> topo;
    private HashMap<String, String> names;

    public ConfigData(HashMap<String, String[]> topo, HashMap<String, String> names) {
        this.topo = topo;
        this.names = names;
    }

    public HashMap<String, String[]> getTopo() {
        return topo;
    }

    public HashMap<String, String> getNames() {
        return names;
    }

    public String getUserIdentifier(String user) {
        Set<String> keys = names.keySet();
        for (String key : keys) {
            if (names.get(key).equals(user)) {
                return key;
            }
        }
        return null;
    }

    /**
     * Obtener los nodos vecinos de un nodo
     *
     * @param username
     * @return
     */
    public String[] getNeighbors(String username) {
        String identifier = getUserIdentifier(username);
        String[] neighbors = topo.get(identifier);

        ArrayList<String> emails = new ArrayList<>();

        for (String neighbor : neighbors) {
            emails.add(names.get(neighbor));
        }
        return emails.toArray(new String[emails.size()]);
    }


}
