import java.io.IOException;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class TP6 {
  public static class TP6Mapper
       extends Mapper<Object, Text, Text, IntWritable>{
	  
	  private SortedMap<Integer, String> TopK = new TreeMap<Integer, String>();
	  private Text word = new Text();
	  public int k = 0;
	  
	  @Override
      public void setup(Context context){
		  Configuration conf = context.getConfiguration();
		  k = Integer.parseInt(conf.get("k"));
      }
	  
	  public void map(Object key, Text value, Context context
			  ) throws IOException, InterruptedException {
		  
		  String prop_ville[] = value.toString().split(",");
		  
		  if(!prop_ville[4].isEmpty() && !prop_ville[4].equals("Population")) {
			  
			  context.getCounter("WPC", "villes_valides").increment(1);
			  word.set(prop_ville[2]);
			  
			  if(!TopK.containsValue(prop_ville[2])) {
				  context.getCounter("WPC", "ajout_ville").increment(1);
				  TopK.put(Integer.parseInt(prop_ville[4]), prop_ville[2]);
			  }
		  
			  if(TopK.size() > k) {
				  context.getCounter("WPC", "suppression_ville").increment(1);
				  TopK.remove(TopK.firstKey());
			  }
		  }
		  context.getCounter("WPC", "toutes_les_villes").increment(1);
	  }
	  
	  @Override
      public void cleanup(Context context) throws IOException, InterruptedException{
        for(SortedMap.Entry<Integer, String> entry : TopK.entrySet()){
        	context.getCounter("WPC", "les_villes_retournées_par_mapper").increment(1);
            context.write(new Text(entry.getValue()), new IntWritable(entry.getKey()));
        }
      }
  }
  public static class TP6Reducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
	  
	  public int k = 0;
	  private SortedMap<Integer, String> TopK = new TreeMap<Integer, String>();
	  
	  public void setup(Context context) {
		  Configuration conf = context.getConfiguration();
		  k = Integer.parseInt(conf.get("k"));
	  }
	  
	  public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
	      for (IntWritable val : values) {
	    	  TopK.put(val.get(), key.toString());
	    	  if(TopK.size() > k) {
				  TopK.remove(TopK.firstKey());
			  }
	      }
	  }
	  
	  @Override
      public void cleanup(Context context) throws IOException, InterruptedException{
    	  for (SortedMap.Entry<Integer, String> entry : TopK.entrySet()) {
	    	  context.getCounter("WPC", "les_villes_retournées_par_reducer").increment(1);
	    	  context.write(new Text(entry.getValue()), new IntWritable(entry.getKey()));
	      }
      }
  }
	  
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Path k = new Path(args[2]);
    conf.setInt("k", Integer.parseInt(k.toString()));
    Job job = Job.getInstance(conf, "TP6");
    job.setNumReduceTasks(1);
    job.setJarByClass(TP6.class);
    job.setMapperClass(TP6Mapper.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);
    job.setReducerClass(TP6Reducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    job.setOutputFormatClass(TextOutputFormat.class);
    job.setInputFormatClass(TextInputFormat.class);
    SequenceFileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
