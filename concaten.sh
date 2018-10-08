
#!/bin/sh
echo "Hello world"
cd ~/code/hadoop-2.8.5
#source user-env.sh
#./bin/hdfs dfs -mkdir -p /users/achemoune
#./bin/hdfs dfs -touchz /users/achemoune/sourcefile.txt
find ~/Bureau/programmation_web/td2 \( -name \*.html -o -name \*.css \) -exec cat {} >> ~/Bureau/coc.txt \;
./bin/hdfs dfs -appendToFile ~/Bureau/coc.txt /users/achemoune/sourcefile.txt
echo "fin"
