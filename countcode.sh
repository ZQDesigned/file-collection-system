#!/bin/bash

declare -i filenumber=0
declare -i linenumber=0

list_alldir(){
    for file in $(ls -a $1)
        do
            if [ x"$file" != x"." -a x"$file" != x".." ];then
                if [ -d "$1/$file" ];then
			# 忽略 build 文件夹
            		if [[ "$(basename "$file")" == "build" ]]; then
                		continue
           		 fi
                    list_alldir "$1/$file"
	              else
		                if [[ $file =~ \.java$ ]] || [[ $file =~ \.kt$ ]] || [[ $file =~ \.kts$ ]] || [[ $file =~ \.xml$ ]] || [[ $file =~ \.gradle$ ]] || [[ $file =~ \.proto$ ]] || [[ $file =~ \.properties$ ]]; then
			                  echo "$1/$file"
			                  filenumber=$filenumber+1
			                  linenumber=$linenumber+$(cat "$1/$file"|wc -l)
		                fi
                fi
            fi
        done
}

if [ "$1" = "" ];then
    arg="."
else
    arg="./"$1
fi

list_alldir "$arg"

echo "There are $filenumber code files under directory: $arg"
echo "--total code lines are: $linenumber"