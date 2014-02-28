#!/usr/bin/env bash

if [ ! -d "bin" ]; then
	mkdir "bin"
fi

if [ ! -d "bin" ]; then
	printf "Could not make bin directory\n"
	exit
fi


existing_class_files=$(find bin -name '*.class')
for existing_class in $existing_class_files; do
	rm "$existing_class"
done

find ./src -name *.java > sources_list.txt
javac -d bin @sources_list.txt
rm sources_list.txt
