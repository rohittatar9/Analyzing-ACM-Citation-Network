import org.apache.spark.graphx._
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import scala.util.MurmurHash

object PaperGraph{
  def main(args: Array[String]): Unit = {
    if (args.length <2){
      
        System.err.println("Usage: graph <input file>  <output file>")
        System.exit(1);
      }
    
    //initalize sparkconf
    val conf = new SparkConf().setAppName("graph").setMaster("local[2]");
    val sc = new SparkContext(conf)
    
    //split with space characters and form a pair rdd of paper1 and paper 2
    val input = sc.textFile(args(0)).map(i=>i.split("\\s+"))
    val pairInput = input.map(x=>(x(0),x(1)))

    //zip every vertex with an index  
    val zipPair = pairInput.map(_._1).distinct.zipWithIndex.collect.toMap
    val pairInputHash = pairInput.map{case(v1,v2)=>(zipPair.get(v1),zipPair.get(v2))}
    val cleanInput = pairInputHash.map{case(Some(v1),Some(v2))=>(v1,v2)}

    val edges: RDD[(VertexId,VertexId)] = cleanInput
    val graph = Graph.fromEdgeTuples(edges,null)
    
    val numofVertices=graph.numVertices
    val graph_inDegree = graph.inDegrees.map{case(vertex,indegree)=>(indegree,1)}  
    val distinctIndegree = graph_inDegree.reduceByKey((v1,v2)=>v1+v2)
    val output = distinctIndegree
       .map{case(indegree,index)=>indegree+","+ (index.toDouble/numofVertices).toDouble}
    
    output.saveAsTextFile(args(1))
    
  }
}
