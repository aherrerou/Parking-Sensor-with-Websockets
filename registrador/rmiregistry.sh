#! /bin/bash

unset CLASSPATH

export CLASSPATH=$CLASSPATH:$HOME/Desktop/registrador/sensorCliente.jar
export CLASSPATH=$CLASSPATH:$HOME/Desktop/registrador/registradorCliente.jar

rmiregistry -J-Djava.security.policy=registrar.policy

