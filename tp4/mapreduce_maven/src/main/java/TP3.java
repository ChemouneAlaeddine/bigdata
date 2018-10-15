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

	  private Text dix = new Text("10");
	  private Text cent = new Text("100");
	  private Text mille = new Text("1000");
	  private Text dix_mille = new Text("10000");
	  private Text cent_mille = new Text("100000");
	  private Text million = new Text("1000000");
	  private Text dix_million = new Text("10000000");
	  private Text cent_million = new Text("100000000");
	  
	  public void map(Object key, Text value, Context context
			  ) throws IOException, InterruptedException {
		  
		  StringTokenizer itr = new StringTokenizer(value.toString(),"\n");
		  
		  while (itr.hasMoreTokens()) {
			  String ville = itr.nextToken();
			  String prop_ville[] = ville.split(",");
			  
			  if(ville.indexOf(",,") == -1) {
				  if(!prop_ville[4].equals("Population")) {
					  int valeur = (int)/*Math.log(*/Long.parseLong(prop_ville[4]/*)*/);
					  if(valeur < 10)
						  context.write(dix, new IntWritable((int)Long.parseLong(prop_ville[4])));
					  if(valeur >= 10 && valeur < 100)
						  context.write(cent, new IntWritable((int)Long.parseLong(prop_ville[4])));
					  if(valeur >= 100 && valeur < 1000)
						  context.write(mille, new IntWritable((int)Long.parseLong(prop_ville[4])));
					  if(valeur >= 1000 && valeur < 10000)
						  context.write(dix_mille, new IntWritable((int)Long.parseLong(prop_ville[4])));
					  if(valeur >= 10000 && valeur < 100000)
						  context.write(cent_mille, new IntWritable((int)Long.parseLong(prop_ville[4])));
					  if(valeur >= 100000 && valeur < 1000000)
						  context.write(million, new IntWritable((int)Long.parseLong(prop_ville[4])));
					  if(valeur >= 1000000 && valeur < 10000000)
						  context.write(dix_million, new IntWritable((int)Long.parseLong(prop_ville[4])));
					  if(valeur >= 10000000)
						  context.write(cent_million, new IntWritable((int)Long.parseLong(prop_ville[4])));
				  }
			  }
		  }
	  }
  }
  public static class TP3Reducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
	  
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
	    context.write(key, somme);
	    context.write(key, moyenne);
	    context.write(key, minimum);
	    context.write(key, maximum);
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
