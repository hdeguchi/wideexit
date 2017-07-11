#!/bin/sh

MAIN_CLASS=soars.plugin.modelbuilder.chart.live_viewer.main.Application

CLASSES_PATH=../../../../../classes
LIBRARY_PATH=../../../../../lib

CLASS_PATH_STR=$CLASSES_PATH

for i in $LIBRARY_PATH/chart/*.jar ; do
        CLASS_PATH_STR=$CLASS_PATH_STR:$i
done

$JAVA_HOME/bin/java -cp $CLASS_PATH_STR -D$VALUE $MAIN_CLASS $@ 

exit 0
