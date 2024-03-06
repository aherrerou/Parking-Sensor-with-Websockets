#! /bin/bash

export CLASSPATH=$CLASSPATH:$HOME/Desktop/registrador/sensorCliente.jar
javac RegistroRemoto.java
export CLASSPATH=$CLASSPATH:$HOME/Desktop/sensores/RegistroRemoto
javac registrador.java
rmic registrador

jar cvf registradorCliente.jar RegistroRemoto.class registrador_Stub.class

