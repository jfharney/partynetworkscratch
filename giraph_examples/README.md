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



HOW TO BUILD GIRAPH APPS FROM SCRATCH

The previous example was using the giraph examples from the uber jar (*-examples-with-dependencies-*.jar).  This section describes how to create an app from "scratch" in two different ways.  Here is the first way (probably not the optimal deployment strategy but it works).  There were some CLASSPATH issues with this method, and thus that is why I added the jars to the explicit shared directory of HADOOP.


1 - Copy the helloworldfromscratch example in the git repo into the docker container

//Assuming helloworldfromscratchfolder is located in $HELLOWORLD on host

docker cp $HELLOWORLD/helloworldfromscratch <docker_instance_name>/

This will copy that directory into the root directory ("/") of the docker container.  It is probably best to find another place for it.


2 - Go to the helloworldfromscratch directory and run mvn package.

NOTE: Everything from here onwards is in the docker container itself (i.e. the console used when "docker run" was called")

cd /helloworldfromscratch

mvn clean package



This should create a "target" directory in the helloworldfromscratch directory with a jar called "book-examples-1.0.0.jar".


3 - Restart Hadoop.  

Might be a good idea to stop and start hdfs and yarn in the container.

$HADOOP_HOME/sbin/stop-dfs.sh (or .cmd)
$HADOOP_HOME/sbin/stop-yarn.sh (or .cmd)
$HADOOP_HOME/sbin/start-dfs.sh (or .cmd)
$HADOOP_HOME/sbin/start-yarn.sh




4 - Copy the resources to HDFS.

//create the directories for input
$HADOOP_HOME/bin/hdfs dfs -mkdir /user/root/input/book
$HADOOP_HOME/bin/hdfs dfs -mkdir /user/root/input/book/helloworld

/usr/local/hadoop/bin/hdfs dfs -put resources/combinedgraph/graph.txt /user/root/input/book/helloworld/


5 - Copy the jar into the hadoop shared library.  

cp target/book-examples-1.0.0.jar /usr/local/hadoop/share/hadoop/yarn/lib/

6 - Copy the dependencies jar into the hadoop shared library

cp /usr/local/giraph/giraph-examples/target/giraph-examples-1.1.0-SNAPSHOT-for-hadoop-2.4.1-jar-with-dependencies.jar /usr/local/hadoop/share/hadoop/yarn/lib/

7 - Run the application.  Note the class name and resources must be consistent with what you place in the jar:

/usr/local/giraph/bin/giraph target/*.jar GiraphHelloWorld -vip /user/root/input/book/helloworld -vif org.apache.giraph.io.formats.IntIntNullTextInputFormat -w 1 -ca giraph.SplitMasterWorker=false,giraph.logLevel=error

8 - Examine the log output


Output should be in userlogs:


/usr/local/hadoop/logs/userlogs/application_*std_out.log

(where * is the application number)

The output should look something like this:

Hello world from the: 6 who is following: 3 4
Hello world from the: 5 who is following: 1 2 4
Hello world from the: 7 who is following: 3 5
Hello world from the: 2 who is following:
Hello world from the: 1 who is following: 2
Hello world from the: 3 who is following: 1 4
Hello world from the: 4 who is following: 2 7














