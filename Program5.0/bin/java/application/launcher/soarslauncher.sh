#!/bin/sh

cd $HOME/workspace/SOARS_Work/bin/soars

# please set basepath & main class
MAIN_CLASS=soars.application.launcher.main.Application
BASEPATH=$HOME/workspace/SOARS_Program2.0

#
CLASSES_PATH=$BASEPATH/classes
LIBRARY_PATH=$BASEPATH/bin/lib

#make classpath
CLASS_PATH_STR=$CLASSES_PATH

for i in $LIBRARY_PATH/*.jar ; do
        CLASS_PATH_STR=$CLASS_PATH_STR:$i
done

$JAVA_HOME/bin/java -cp $CLASS_PATH_STR -D$VALUE $MAIN_CLASS $@ 

exit 0
