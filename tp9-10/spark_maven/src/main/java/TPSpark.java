import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class TPSpark {

	public static void main(String[] args) {
		
		SparkConf conf = new SparkConf().setAppName("TPSpark");
		JavaSparkContext context = new JavaSparkContext(conf);
		
		//JavaRDD<String> textFile = context.textFile("hdfs://localhost:9000/user/raw_data/worldcitiespop.txt");
		//System.out.println("le nombre de partitions : "+textFile.getNumPartitions());
		
		
		JavaRDD<String> textFile = context.textFile("hdfs://localhost:9000/user/raw_data/worldcitiespop.txt");
		JavaPairRDD<String, Integer> counts = textFile.flatMap(s -> Arrays.asList(s.split(" ")).iterator())
		    .mapToPair(word -> new Tuple2<>(word, 1))
		    .reduceByKey((a, b) -> a + b);
		counts.saveAsTextFile("hdfs://localhost:9000/user/achemoune/test1");
		
	}
	
}