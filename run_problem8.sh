#!/usr/bin/env bash

if [ ! -d "bin" ]; then
	printf "No compiled classes. Did you run compile.sh?\n"
	exit
fi

java -classpath bin/ com/scottlessans/simpledtw/examples/Problem8
