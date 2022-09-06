package algorithms;

import util.ConfigData;
import util.Util;
import xmpp.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public abstract class Topology {

    protected String username;
    protected String password;
    protected Client client;

    private ConfigData configData;

    public Topology(String username, String password){
        try {
            this.configData = Util.getConfigData();
        } catch (Exception e) {
            System.out.println("Error al leer el archivo de configuracion");
            System.exit(1);
        }

        String checkIdentifier = this.getUserIdentifier(username);

        if (checkIdentifier == null) {
            System.out.println("\n\nError: El usuario no existe en la topologia");
            System.exit(1);
        }

        this.username = username;
        this.password = password;
        client = new Client();
    }

    protected String getUserIdentifier(String user) {
        return configData.getUserIdentifier(user);
    }

    /**
     * Obtener los nodos vecinos de un nodo
     *
     * @param username
     * @return
     */
    protected String[] getNeighbors(String username) {
        return configData.getNeighbors(username);
    }

    protected HashMap<String, String[]> getTopo() {
        return configData.getTopo();
    }

    protected HashMap<String, String> getNames() {
        return configData.getNames();
    }

    /**
     * Metodos a implementar por los algoritmos
     */
    public void connect(){}
    protected void sendMessage(String to, String message) {}
}
