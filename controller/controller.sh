#! /bin/bash
export CLASSPATH=$CLASSPATH:$HOME/Desktop/controller/sensorCliente.jar
javac controller.java
java -Djava.security.policy=registrar.policy controller $1 $2
