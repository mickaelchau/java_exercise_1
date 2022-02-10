#!/bin/bash
javac -d bin $(find ./src/ -type f -name '*.java')
if [ $? -eq 0 ] 
then
    echo "You have succesfully compiled your java code !"
else
    echo "Oops ! Something went wrong"
fi
