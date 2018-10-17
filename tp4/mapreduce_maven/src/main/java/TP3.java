import java.io.IOException;
import java.util.StringTokenizer;
import java.lang.*;

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

	  
	  private Text nombre = new Text();
	  
	  public void map(Object key, Text value, Context context
			  ) throws IOException, InterruptedException {
		  
		  StringTokenizer itr = new StringTokenizer(value.toString(),"\n");
		  
		  while (itr.hasMoreTokens()) {
			  String ville = itr.nextToken();
			  String prop_ville[] = ville.split(",");
			  
			  if(ville.indexOf(",,") == -1) {
				  if(!prop_ville[4].equals("Population")) {
					  nombre = new Text(String.valueOf( (long)  Math.pow(10,  (long)Math.log10(Long.parseLong(prop_ville[4])))  ));
					  context.write(nombre, new IntWritable(Integer.parseInt(prop_ville[4])));
				  }
			  }
		  }
	  }
  }
  public static class TP3Reducer
       extends Reducer<Text,IntWritable,Text, Text> {
	  
	  private IntWritable somme = new IntWritable();
	  private IntWritable moyenne = new IntWritable();
	  private IntWritable minimum = new IntWritable();
	  private IntWritable maximum = new IntWritable();
	 
    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
    	int sum = 0;
    	int occ = 0;
    	int min = 99999999;
    	int max = 0;
	    for (IntWritable val : values) {
	      sum += val.get();
	      occ += 1;
	      if(val.get() < min)
	    	  min = val.get();
	      if(val.get() > max)
	    	  max = val.get();
	    }
	    somme.set(sum);
	    moyenne.set(sum/occ);
	    minimum.set(min);
	    maximum.set(max);
	    
	    StringBuilder str = new StringBuilder();
	    str.append(occ);
	    str.append(" ");
	    str.append(moyenne);
	    str.append(" ");
	    str.append(minimum);
	    str.append(" ");
	    str.append(maximum);
	    str.append(" ");
	    
	    
	    context.write(key, new Text(String.valueOf(str)));
	    
	    
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
