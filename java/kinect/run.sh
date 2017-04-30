#!/bin/bash

CLASSPATH=''
CLASSPATH+='./out/production/One-Offs:'
CLASSPATH+='./java/kinect:'
CLASSPATH+='./java/kinect/freenect-1.0.jar:'
CLASSPATH+='./java/kinect/jna-4.4.0.jar:'
CLASSPATH+='./java/kinect/jna-platform-4.4.0.jar:'

SOURCEPATH='./java/kinect/**'

BUILDPATH='./out/production/One-Offs/'

javac -classpath ${CLASSPATH} -d ${BUILDPATH} ./java/kinect/*.java
java  -classpath ${CLASSPATH} kinect.Kinect $(pwd)/java/kinect/libfreenect.so