package helloworld;


import org.apache.giraph.edge.Edge;
import org.apache.giraph.GiraphRunner;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.util.ToolRunner;

public class GiraphHelloWorld extends BasicComputation<IntWritable, IntWritable, NullWritable, NullWritable> {

  @Override
  public void compute(Vertex<IntWritable, IntWritable, NullWritable> vertex, Iterable<NullWritable> messages) {
    System.out.println("Hello world from the: " + vertex.getId().toString() + " who is following:");

    //iterating over vertex's neighbors
    for(Edge<IntWritable,NullWritable> e : vertex.getEdges()) {
      System.out.print(" " + e.getTargetVertexId());
    }
    System.out.println("");

    //signaling the end of the current BSP computation for the current vertex
    vertex.voteToHalt();

  }

  public static void main(String [] args) throws Exception {
    System.exit(ToolRunner.run(new GiraphRunner(), args));
  }

}

