#! /bin/bash

export CLASSPATH=$CLASSPATH:$HOME/Desktop/controller/cliente.jar

javac servidorWeb.java
java -Djava.security.policy=registrar.policy servidorWeb $1 $2 $3
