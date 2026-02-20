#!/bin/bash

# Create bin directory if it doesn't exist
mkdir -p bin

# Compile all Java files
echo "Compiling Java files..."
javac -d bin -sourcepath src/main/java src/main/java/com/homequest/*.java src/main/java/com/homequest/model/*.java src/main/java/com/homequest/transaction/*.java src/main/java/com/homequest/util/*.java

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful!"
    echo "Run the program with: ./run.sh"
else
    echo "✗ Compilation failed!"
    exit 1
fi
