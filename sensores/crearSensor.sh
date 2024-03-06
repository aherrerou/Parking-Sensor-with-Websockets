#! /bin/bash

javac InterfazRemoto.java
export CLASSPATH=$CLASSPATH:$HOME/Desktop/sensores/InterfazRemoto
javac Sensor.java
rmic Sensor

jar cvf sensorCliente.jar InterfazRemoto.class Sensor_Stub.class

