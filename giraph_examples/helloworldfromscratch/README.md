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

3 - Prepare a quick out-of-the-box example from Giraph.

// change directory to the GIRAPH_HOME environment variable

// NOTE: it was automatically set as /usr/local/giraph/

cd $GIRAPH_HOME 

// prepare input (basic text file) into giraph

// note it is copying a text file into an HDFS location that giraph will read

$HADOOP_HOME/bin/hdfs dfs -put tiny-graph.txt /user/root/input

4 - Run the quick out-of-the-box example from Giraph.

// run the example

// NOTE: it uses the "hadoop jar" command

$HADOOP_HOME/bin/hadoop jar $GIRAPH_HOME/giraph-examples/target/giraph-examples-1.1.0-SNAPSHOT-for-hadoop-2.4.1-jar-with-dependencies.jar org.apache.giraph.GiraphRunner org.apache.giraph.examples.SimpleShortestPathsComputation --yarnjars giraph-examples-1.1.0-SNAPSHOT-for-hadoop-2.4.1-jar-with-dependencies.jar --workers 1 --vertexInputFormat org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexInputFormat --vertexInputPath /user/root/input/tiny-graph.txt -vertexOutputFormat org.apache.giraph.io.formats.IdWithValueTextOutputFormat --outputPath /user/root/output


5 - Examine the output

The console output should look something like this

17/05/24 18:13:45 INFO impl.YarnClientImpl: Submitted application application_1495663977534_0001
17/05/24 18:13:45 INFO yarn.GiraphYarnClient: Got new appId after submission :application_1495663977534_0001
17/05/24 18:13:45 INFO yarn.GiraphYarnClient: GiraphApplicationMaster container request was submitted to ResourceManager for job: Giraph: org.apache.giraph.examples.SimpleShortestPathsComputation
17/05/24 18:13:46 INFO yarn.GiraphYarnClient: Giraph: org.apache.giraph.examples.SimpleShortestPathsComputation, Elapsed: 0.92 secs
17/05/24 18:13:46 INFO yarn.GiraphYarnClient: appattempt_1495663977534_0001_000001, State: ACCEPTED, Containers used: 0
17/05/24 18:13:50 INFO yarn.GiraphYarnClient: Giraph: org.apache.giraph.examples.SimpleShortestPathsComputation, Elapsed: 4.95 secs
17/05/24 18:13:50 INFO yarn.GiraphYarnClient: appattempt_1495663977534_0001_000001, State: ACCEPTED, Containers used: 1
17/05/24 18:13:54 INFO yarn.GiraphYarnClient: Giraph: org.apache.giraph.examples.SimpleShortestPathsComputation, Elapsed: 8.97 secs
17/05/24 18:13:54 INFO yarn.GiraphYarnClient: appattempt_1495663977534_0001_000001, State: RUNNING, Containers used: 1
17/05/24 18:13:58 INFO yarn.GiraphYarnClient: Giraph: org.apache.giraph.examples.SimpleShortestPathsComputation, Elapsed: 12.98 secs
17/05/24 18:13:58 INFO yarn.GiraphYarnClient: appattempt_1495663977534_0001_000001, State: RUNNING, Containers used: 3
17/05/24 18:14:02 INFO yarn.GiraphYarnClient: Giraph: org.apache.giraph.examples.SimpleShortestPathsComputation, Elapsed: 17.00 secs
17/05/24 18:14:02 INFO yarn.GiraphYarnClient: appattempt_1495663977534_0001_000001, State: RUNNING, Containers used: 3
17/05/24 18:14:06 INFO yarn.GiraphYarnClient: Giraph: org.apache.giraph.examples.SimpleShortestPathsComputation, Elapsed: 21.03 secs
17/05/24 18:14:06 INFO yarn.GiraphYarnClient: appattempt_1495663977534_0001_000001, State: RUNNING, Containers used: 3
17/05/24 18:14:10 INFO yarn.GiraphYarnClient: Giraph: org.apache.giraph.examples.SimpleShortestPathsComputation, Elapsed: 25.05 secs
17/05/24 18:14:10 INFO yarn.GiraphYarnClient: appattempt_1495663977534_0001_000001, State: RUNNING, Containers used: 3
17/05/24 18:14:14 INFO yarn.GiraphYarnClient: Giraph: org.apache.giraph.examples.SimpleShortestPathsComputation, Elapsed: 29.08 secs
17/05/24 18:14:14 INFO yarn.GiraphYarnClient: appattempt_1495663977534_0001_000001, State: RUNNING, Containers used: 3
17/05/24 18:14:18 INFO yarn.GiraphYarnClient: Giraph: org.apache.giraph.examples.SimpleShortestPathsComputation, Elapsed: 33.10 secs
17/05/24 18:14:18 INFO yarn.GiraphYarnClient: appattempt_1495663977534_0001_000001, State: RUNNING, Containers used: 2
17/05/24 18:14:22 INFO yarn.GiraphYarnClient: Cleaning up HDFS distributed cache directory for Giraph job.
17/05/24 18:14:22 INFO yarn.GiraphYarnClient: Completed Giraph: org.apache.giraph.examples.SimpleShortestPathsComputation: SUCCEEDED, total running time: 0 minutes, 35 seconds.

NOTE:
The logs for giraph are located in /usr/local/hadoop/logs/userlogs/application_*, where * is the application number shown in the submission above (e.g. application_1495663977534_0001).  Both standard error and output are located here.  Example of this later.











