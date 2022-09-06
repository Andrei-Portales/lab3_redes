## Laboratorio 3

Para este laboratorio se realizaron distintos algoritmos de enrutamiento para poder ver la eficiencia y como realmente los enrutadores se comportan para enviar y recibir paquetes, tanto mensajes como estados de llas tablas de ruteo internas.

### Pasa esto se utilizo
* Java JDK 11
* Servidor de XMPP del curso
* Smack (libreria de java para xmpp)
* JSON
* Intellij
* Maven

### Algoritmos implementados
* Flooding
* Distance Vector
* Link State

*NOTA 📝:* Es necesario tener JDK 11 para poder correr el `.jar`

Link para descargar el JDK 11: [JDK 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)

### Demo

Para poder correr los ejecutables es necesario buscar los archivos **.jar** que estan en la carpera `/ejecutables/*.jar`. Igualemente en la direccion `/ejecutables/src/main/resources/*.json` se encuentran archivos **.json** que son necesarios para poder configurar la topologia como los identificadores dentro de los programas.


Para pode correr el jar:
```
java -jar ./lab3.jar
```

**Tambien se puede utilizar el ejecutable ubicado en `/ejecutables/lab3.exe` para correr el programa.**

------------------------------------------------------------------------------------------------------------------------------------------------

Luego de haber ejecutado los comandos, es necesario que se ingrese el JID de algun nodo existente en la topologia y su respectiva contraseña.

------------------------------------------------------------------------------------------------------------------------------------------------

Mientras que para ejecutar el algoritmo de DVR, lo que se necesita es estar en un entorno de Python y se corre el archivo:
```
main.py
```
