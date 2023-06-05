#!/bin/bash

find . \( -name '*.java'\
 -o -name '*.js'\
 -o -name '*.jsx'\
 -o -name '*.html'\
 -o -name '*.adoc'\
 -o -name '*.css'\
 -o -name '*.gradle'\
 -o -name '*.yml'\
 \) \
 -a -type f \
 | xargs wc -l \
 | tail -1 \
 | awk '{print $1}'
