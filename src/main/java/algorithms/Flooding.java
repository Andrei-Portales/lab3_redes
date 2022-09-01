package algorithms;


import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import util.Util;
import xmpp.Client;

import java.util.*;

/**
 * Flooding algorithm implementacion con xmpp
 */
public class Flooding {
    private String username;
    private String password;
    private Client client;

    HashMap<String, String[]> topo;
    HashMap<String, String> names;

    public Flooding(String username, String password) {
        this.username = username;
        this.password = password;
        client = new Client();
        this.topo = Util.readTopoConfig();
        this.names = Util.readnamesConfig();
    }

    /**
     * Obtener el identificador del nodo en el archivo de configuraciones
     * @param user
     * @return
     */
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
     * @param username
     * @return
     */
    public String[] getNeighbors(String username){
        String identifier = getUserIdentifier(username);
        String[] neighbors = topo.get(identifier);

        ArrayList<String> emails = new ArrayList<>();

        for (String neighbor : neighbors) {
            emails.add(names.get(neighbor));
        }
        return emails.toArray(new String[emails.size()]);
    }

    /**
     * Metodo para reenviar o aceptar los mensajes entrantes
     * @param message
     */
    public void messageReceiver(JSONObject message){
        try{

            String to = (String) message.get("to");
            String from = (String) message.get("from");
            String tempTo = (String) message.get("tempTo");

            if (to.equals(username)) {
                System.out.println(message.toJSONString());
            } else {
                // Obtener saltos y distancia
                Long saltos = (Long) message.get("saltos");
                Long distance = (Long) message.get("distance");

                if (saltos < names.size()){
                    // Obtener los vecinos del nodo actual
                    String[] neighbors = getNeighbors(username);
                    neighbors = Arrays.stream(neighbors).filter(neighbor -> !from.equals(neighbor) || !tempTo.equals(neighbor)).toArray(String[]::new);

                    // Agregar nuevo nodo a la lista de nodos del mensaje
                    JSONArray nodos = (JSONArray) message.get("nodos");
                    nodos.add(username);

                    // Incrementar saltos y distancia
                    saltos += 1;
                    distance += 1;

                    message.put("saltos", saltos);
                    message.put("distance", distance);
                    message.put("nodos", nodos);

                    // Enviar mensaje a todos los

                    for (String neighbor : neighbors) {
                        message.put("tempTo", neighbor);
                        Chat chat = client.getConnection().getChatManager().createChat(neighbor, null);
                        chat.sendMessage(message.toJSONString());
                    }
                    System.out.println("Mesanje redireccionado a todos los vecinos");

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Metodo para conectarse con el servidor de XMPP
     */
    public void connect() {
        client.login(username, password);

        client.getConnection().addPacketListener((packet) -> {
            try{
                if (packet instanceof Message){
                    Message message = (Message) packet;

                    if (message.getBody() != null){

                        JSONObject jsonObject = (JSONObject) new JSONParser().parse(message.getBody());

                        String tempTo = (String) jsonObject.get("tempTo");
                        String from = (String) jsonObject.get("from");

                        if (username.equals(from) || !username.equals(tempTo)){
                            return;
                        }

                        messageReceiver(jsonObject);
                    }
                }
            }catch(Exception e){

            }

        }, new PacketTypeFilter(Packet.class));
    }

    /**
     * Envia un mensaje a un destinatario
     * @param to
     * @param message
     */
    public void sendMessage(String to, String message) {
        String[] neighbors = getNeighbors(username);
        JSONObject json = new JSONObject();
        json.put("from", this.username);
        json.put("to", to);
        json.put("message", message);

        json.put("saltos", 0);
        json.put("distance", 0);
        json.put("nodos", new JSONArray());

        for (String neighbor : neighbors) {
            Message newMessage = new Message(neighbor, Message.Type.chat);
            json.put("tempTo", neighbor);
            newMessage.setBody(json.toJSONString());
            client.getConnection().sendPacket(newMessage);
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese su nombre de usuario (Flooding): ");
        String username = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String password = scanner.nextLine();

        Flooding flooding = new Flooding(username, password);
        flooding.connect();

        while (true){
            System.out.print("Ingrese el nombre de usuario del destinatario: ");
            String to = scanner.nextLine();
            System.out.print("Ingrese el mensaje: ");
            String message = scanner.nextLine();
            flooding.sendMessage(to, message);
        }
    }
}
