import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class TP3 {
  public static class TP3Mapper
       extends Mapper<Object, Text, Text, IntWritable>{
	  
	  private final static IntWritable one = new IntWritable(1);
	  private final static IntWritable zero = new IntWritable(0);
	  private Text word = new Text();
	  
	  public void map(Object key, Text value, Context context
			  ) throws IOException, InterruptedException {
		  
		  StringTokenizer itr = new StringTokenizer(value.toString(),"\n");
		  
		  while (itr.hasMoreTokens()) {
			  String ville = itr.nextToken();
			  String prop_ville[] = ville.split(",");
			  if(prop_ville[1] != null)
				  context.getCounter("WPC", "nb_cities").increment(1);
			  
			  word.set(ville);
			  if(ville.indexOf(",,") == -1) {
				  context.getCounter("WPC", "nb_pop").increment(1);
				  if(!prop_ville[4].equals("Population"))
					  context.getCounter("WPC", "total_pop").increment(Long.parseLong(prop_ville[4]));
				  context.write(word, one);
			  }else
				  context.write(word, zero);
		  }
	  }
  }
  public static class TP3Reducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    
	  private IntWritable result = new IntWritable();
	  
	  public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
	      for (IntWritable val : values) {
	        result.set(val.get());
	        if(result.get() == 1)
	        	context.write(key, null);
	      }
	  }
  }
	  
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "TP3");
    job.setNumReduceTasks(1);
    job.setJarByClass(TP3.class);
    job.setMapperClass(TP3Mapper.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);
    job.setReducerClass(TP3Reducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    job.setOutputFormatClass(TextOutputFormat.class);
    job.setInputFormatClass(TextInputFormat.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
