import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class TPSpark {

	public static void main(String[] args) {
		
		SparkConf conf = new SparkConf().setAppName("TPSpark");
		JavaSparkContext context = new JavaSparkContext(conf);
		
		JavaRDD<String> textFile = context.textFile("hdfs://localhost:9000/user/raw_data/worldcitiespop.txt");
		System.out.println("le nombre de partitions : "+textFile.getNumPartitions());
		
	}
	
}