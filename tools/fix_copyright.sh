#!/bin/bash

usage() {
    echo "Usage: $0 copyright_file src_directory"
    exit 1
}

error() {
    echo "Error: $1"
    usage
}

[ $# -ne 2 ] && usage
[ ! -r $1 ] && error "copyright_file is not readable"
[ ! -d $2 ] && error "src_directory could not be found or is not a directory"

copyright=$1
src_dir=$2

tmp=/tmp/$$.java
lines=`wc -l $copyright | awk '{print $1}'`

for f in `find $src_dir -name *.java`; do
    head -n$lines $f > $tmp

    if [ `diff -q $copyright $tmp | wc -l` -ne 0 ]; then
	cat $copyright | cat - $f > $tmp
	mv $tmp $f
	echo Fixed $f
    fi
done

exit 0
