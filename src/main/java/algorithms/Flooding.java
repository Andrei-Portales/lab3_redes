package algorithms;


import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;

/**
 * Flooding algorithm implementacion con xmpp
 */
public class Flooding extends Topology {

    public Flooding(String username, String password) {
        super(username, password);
    }

    /**
     * Metodo para reenviar o aceptar los mensajes entrantes
     *
     * @param message
     */
    public void messageReceiver(JSONObject message) {
        try {

            String to = (String) message.get("to");
            String from = (String) message.get("from");
            String tempTo = (String) message.get("tempTo");

            if (to.equals(username)) {
                System.out.println(message.toJSONString());
            } else {
                // Obtener saltos y distancia
                Long saltos = (Long) message.get("saltos");
                Long distance = (Long) message.get("distance");

                if (saltos < getNames().size()) {
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
        } catch (Exception e) {
            System.out.println("Error al recibir el mensaje");
            e.printStackTrace();
        }
    }

    /**
     * Metodo para conectarse con el servidor de XMPP
     */
    @Override
    public void connect() {
        client.login(username, password);

        client.getConnection().addPacketListener((packet) -> {
            try {
                if (packet instanceof Message) {
                    Message message = (Message) packet;

                    if (message.getBody() != null) {

                        JSONObject jsonObject = (JSONObject) new JSONParser().parse(message.getBody());

                        String tempTo = (String) jsonObject.get("tempTo");
                        String from = (String) jsonObject.get("from");

                        if (username.equals(from) || !username.equals(tempTo)) {
                            return;
                        }

                        messageReceiver(jsonObject);
                    }
                }
            } catch (Exception e) {

            }

        }, new PacketTypeFilter(Packet.class));
    }

    /**
     * Envia un mensaje a un destinatario
     *
     * @param to
     * @param message
     */
    @Override
    protected void sendMessage(String to, String message) {
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

    /**
     * Start all
     */
    public static void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese su nombre de usuario (Flooding): ");
        String username = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String password = scanner.nextLine();

        Flooding flooding = new Flooding(username, password);
        flooding.connect();

        while (true) {
            System.out.print("Ingrese el nombre de usuario del destinatario: ");
            String to = scanner.nextLine();
            System.out.print("Ingrese el mensaje: ");
            String message = scanner.nextLine();
            flooding.sendMessage(to, message);
        }
    }
}
