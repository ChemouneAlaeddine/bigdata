package bigdata;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.*;
import java.net.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.io.IOUtils;


public class MonApplication {
	public static class MonProg extends Configured implements Tool {
		// TP2 EX6
		/*public int run(String[] args) throws Exception {
			System.out.println("Hello World");
			Path outputPath = new Path("hdfs://localhost:9000/achemoune/file.txt");
			Configuration conf = getConf();
			FileSystem fs = FileSystem.get(conf);
			OutputStream os = fs.create(outputPath);
			InputStream is = new BufferedInputStream(new FileInputStream("/home/chemoune/Bureau/bigdata/coc.txt"));
			IOUtils.copyBytes(is, os, conf);

			//CODE DE VOTRE PROGRAMME ICI
			return 0;
		}*/
		
		// TP2 EX7
		/*public int run(String[] args) throws Exception {
			URI uri = new URI(args[args.length - 1]);
			uri = uri.normalize();
			Configuration conf = getConf();
			FileSystem fs = FileSystem.get(uri, conf);
			Path outputPath = new Path(uri.getPath());
			OutputStream os = fs.create(outputPath);
			for (int i=0; i < args.length - 1; ++i) {
				InputStream is = new BufferedInputStream(new FileInputStream(args[i]));
				IOUtils.copyBytes(is, os, conf, false);
				is.close();
			}
			os.close();
			return 0;
		}*/
		
		// TP2 EX8
		public int run(String[] args) throws Exception {
			
			Configuration configuration = new Configuration(); 
			FileSystem hdfs = FileSystem.get(new URI("hdfs://localhost:9000"), configuration); 
			Path file = new Path("hdfs://localhost:9000/achemoune/file.txt"); 
			
			OutputStream os = hdfs.create(file);
			BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			
			for(int i=0; i<10; i++) {
				String chars = "abcdefghijklmnopqrstuvwxyz";
			    String pass = "";
			    for(int x=0;x<10;x++)
			    {
			       int n = (int)Math.floor(Math.random() * 24);
			       pass += chars.charAt(n);
			    }
			    
				br.write(pass+"\n"); 
			}
			
			br.close();
			os.close();
			hdfs.close();
			return 0;
		}
	}
	
	public static void main( String[] args ) throws Exception {
		
		int returnCode = ToolRunner.run(new MonApplication.MonProg(), args);
		System.exit(returnCode);

		//===================================

		// TP2 EX6
		/*System.out.println("Hello World");
		String localPath = "/home/chemoune/Bureau/bigdata/coc.txt";
		String uri = "hdfs://localhost:9000";
		String hdfsDir = "hdfs://localhost:9000/achemoune";
		
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		
		fs.copyFromLocalFile(new Path(localPath), new Path(hdfsDir));*/
	}
}