#!/bin/bash

echo "Connecting 10,000 clients..."

for i in $(seq 1 10000)
do
    nc localhost 9020 &
done

echo "Done! Check if server crashed."