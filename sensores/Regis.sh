#! /bin/bash
export CLASSPATH=$CLASSPATH:$HOME/Desktop/sensores/registradorCliente.jar
javac Registro.java
java -Djava.security.policy=registrar.policy Registro $1 $2 $3
