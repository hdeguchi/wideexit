#!/bin/sh

# please set basepath & main class
MAIN_CLASS=main.MainGUI
JAVA_HOME=/usr/java/j2sdk1.4.2_06
BASEPATH=/home/sgeadmin/workspace/SOARS
 
#
LIB_PATH=$BASEPATH/lib
LIBEXT_PATH=$BASEPATH/libext
 
#make classpath
CLASS_PATH_STR=$LIB_PATH
 
for i in $LIBEXT_PATH/*.jar ; do
        CLASS_PATH_STR=$CLASS_PATH_STR:$i
done
 
echo $CLASS_PATH_STR
 
$JAVA_HOME/bin/java -cp $CLASS_PATH_STR -D$VALUE $MAIN_CLASS $@ 

exit 0
