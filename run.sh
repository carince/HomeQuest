#!/bin/bash
if [ ! -d "bin" ]; then
    echo "Compiled classes not found. Running compile.sh first..."
    ./compile.sh
fi

echo "Running HomeQuest Application..."
echo "================================"
java -cp bin com.homequest.HomeQuest
