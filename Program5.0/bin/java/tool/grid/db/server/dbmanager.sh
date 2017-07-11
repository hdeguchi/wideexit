#!/bin/sh

export JAVA_HOME=/usr/java/jre1.5.0_09

$JAVA_HOME/bin/java -cp hsqldb.jar org.hsqldb.util.DatabaseManager -url jdbc:hsqldb:hsql://localhost &
