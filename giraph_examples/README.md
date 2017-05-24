Giraph Examples

This document describes deployment steps, hints, and sample codes to run Giraph jobs in a "virtual" Hadoop 2 environment


Deployment Environment

The environment used is in the form of a docker container from the url:

https://github.com/uwsampa/giraph-docker

It contains an instance of Giraph running over Hadoop 2.4 in an ubuntu instance (not sure which version).

The repo contains a Dockerfile, but it can't be built because there are checksum errors, but it can be run directly.


Here is a tentative procedure for getting Giraph to work in this environment:

1 - Pull the docker instance (assuming docker has been installed on your local machine) with the following command:

docker pull uwsampa/giraph-docker

2 - Run the docker instance:

docker run --volume $HOME:/myhome --rm --interactive --tty uwsampa/giraph-docker /etc/giraph-boostrap.sh -bash

This will put you at the root directory of the container.

3 - Run a quick out-of-the-box example from Giraph.

// change directory to the GIRAPH_HOME environment variable
// NOTE: it was automatically set as /usr/local/giraph/
cd $GIRAPH_HOME 

// prepare input (basic text file) into giraph
// note it is copying a text file into an HDFS location that giraph will read
$HADOOP_HOME/bin/hdfs dfs -put tiny-graph.txt /user/root/input









