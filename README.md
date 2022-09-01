## Laboratorio 3

Para este laboratorio se realizaron distintos algoritmos de enrutamiento para poder ver la eficiencia y como realmente los enrutadores se comportan para enviar y recibir paquetes, tanto mensajes como estados de llas tablas de ruteo internas.

### Pasa esto se utilizo
* Java JDK 11
* Servidor de XMPP del curso
* Smack (libreria de java para xmpp)
* JSON
* Intellij
* Maven

### Demo

Para poder correr los ejecutables es necesario buscar los archivos **.jar** que estan en la carpera `/ejecutables/*.jar`. Igualemente en la direccion `/ejecutables/src/main/resources/*.json` se encuentran archivos **.json** que son necesarios para poder configurar la topologia como los identificadores dentro de los programas.

#### *Flooding*
```
java -jar ./Flooding.jar
```

#### *Link State Routing*
```
java -jar ./LinkStateRouting.jar
```

Luego de haber ejecutado los comandos, es necesario que se ingrese el JID de algun nodo existente en la topologia y su respectiva contraseña.
