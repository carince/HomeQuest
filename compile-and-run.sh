#!/bin/bash
echo "HomeQuest - Compile and Run"
echo "==========================="

./compile.sh
if [ $? -eq 0 ]; then
    echo ""
    ./run.sh
fi
