#!/bin/sh

export JAVA_HOME=/usr/java/jre1.5.0_09

$JAVA_HOME/bin/java -cp /home/sgeadmin/soars/program/db/hsqldb.jar org.hsqldb.Server -database /home/sgeadmin/soars/program/db/soarsgrid
