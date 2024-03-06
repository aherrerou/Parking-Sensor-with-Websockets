#! /bin/bash
export CLASSPATH=$CLASSPATH:$HOME/Desktop/registrador/sensorCliente.jar
export CLASSPATH=$CLASSPATH:$HOME/Desktop/registrador/registradorCliente.jar
javac Registro.java
java -Djava.security.policy=registrar.policy Registro $1
