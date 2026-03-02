#!/bin/bash
echo "Compiling HomeQuest Application..."

mkdir -p bin

javac -d bin -sourcepath src/main/java src/main/java/com/homequest/*.java \
    src/main/java/com/homequest/model/*.java \
    src/main/java/com/homequest/transaction/*.java \
    src/main/java/com/homequest/util/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Use ./run.sh to execute the application"
else
    echo "Compilation failed!"
    exit 1
fi
