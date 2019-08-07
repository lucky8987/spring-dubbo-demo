#!/bin/bash
cd `dirname $0`
./stop.sh $1
sleep 2
./start.sh $1
