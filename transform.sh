#!/bin/bash
yaml_file="myyaml.yaml"

sed -i '/jkcd: 40976329-aaaa-444-kk/a \ \ \ jkcd-1: 40976329-aaaa-444-kk' $yaml_file

# Insert njkcd-1 after the entry njkcd
sed -i '/njkcd: 40976329-aaaa-444-kk/a \ \ \ njkcd-1: 40976329-aaaa-444-kk' $yaml_file
