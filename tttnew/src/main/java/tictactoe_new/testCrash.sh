#!/bin/bash

echo "Connecting 10,000 clients..."

for i in $(seq 1 100); do
    bash -c 'exec 3<>/dev/tcp/localhost/9020; sleep 30' &
done

echo "Done! Check if server crashed."