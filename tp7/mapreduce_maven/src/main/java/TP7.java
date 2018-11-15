import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class TP7 {
  public static class TP7MapperVille
       extends Mapper<Object, Text, TaggedValue, IntWritable>{
	  
	  public void map(Object key, Text value, Context context
			  ) throws IOException, InterruptedException {
		  
		  String tab[] = value.toString().split(",");
		  String s = tab[0]+","+tab[3];
		  TaggedValue tv = new TaggedValue(s, value.toString());
		  context.write(tv, new IntWritable(1));
	  
	  }
  }
  
  public static class TP7MapperRegion
  extends Mapper<Object, Text, TaggedValue, IntWritable>{
 
 public void map(Object key, Text value, Context context
		  ) throws IOException, InterruptedException {
	 
	 TaggedValue tv = new TaggedValue(type.REGION, value.toString());
	 context.write(tv, new IntWritable(0));
 
 }
}
  
  public static class TP7Reducer
       extends Reducer<TaggedValue,IntWritable,Text,Text> {
	  
	  private ArrayList<String> villes = new ArrayList<String>();
	  private ArrayList<String> regions = new ArrayList<String>();
	  
	  public void reduce(TaggedValue key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
		  for(IntWritable i : values) {
			  if(i.get() == 1) {
				  villes.add(key.toString()); 
			  }else {
				  regions.add(key.toString());
			  }
		  }
	  }
	  
	  @Override
	  public void cleanup(Context context) throws IOException, InterruptedException {
		  
		  for(String stv : villes) {
			  String prop_ville[] = stv.split(",");
			  for(String str : regions) {
				  String prop_region[] = str.split(",");
				  try {
					  if(prop_ville[0].toLowerCase().equals(prop_region[0].toLowerCase())
							  && Integer.parseInt(prop_ville[3]) == Integer.parseInt(prop_region[1])) {
						  context.write(new Text(prop_ville[1]), new Text(str));
						  break;
					  }
				  }catch(Exception e) {
					  
				  }
			  }
		  }
	  }
  }
	  
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    //Path k = new Path(args[2]);
    //conf.setInt("k", Integer.parseInt(k.toString()));
    Job job = Job.getInstance(conf, "TP7");
    job.setNumReduceTasks(1);
    job.setJarByClass(TP7.class);
    job.setMapperClass(TP7MapperVille.class);
    job.setMapperClass(TP7MapperRegion.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);
    job.setReducerClass(TP7Reducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    job.setOutputFormatClass(TextOutputFormat.class);
    job.setInputFormatClass(TextInputFormat.class);
    MultipleInputs.addInputPath(job, new Path(args[0]), FileInputFormat.class, TP7MapperVille.class);
    MultipleInputs.addInputPath(job, new Path(args[1]), FileInputFormat.class, TP7MapperRegion.class);
    //FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[2]));
    System.exit(job.waitForCompletion(true) ? 0 : 2);
  }
}
