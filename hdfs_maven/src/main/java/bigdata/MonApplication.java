//=====================================================================
/**

* Squelette minimal d'une application Hadoop
* A exporter dans un jar sans les librairies externes
* A exécuter avec la commande ./hadoop jar NOMDUFICHER.jar ARGUMENTS....
*/
package bigdata;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.net.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Tool;


public class MonApplication {
	public static class MonProg extends Configured implements Tool {
		public int run(String[] args) throws Exception {
			System.out.println("Hello World");

			//String localInputPath = args[0];
			//Path outputPath = new Path(args[0]);// ARGUMENT FOR OUTPUT_LOCATION
			Path outputPath = (Path) Paths.get(URI.create("hdfs://localhost:9000/achemoune/file.txt"));
			
			Configuration conf = getConf();
			FileSystem fs = FileSystem.get(conf);
			OutputStream os = fs.create(outputPath);
			InputStream is = new BufferedInputStream(new FileInputStream("/home/chemoune/code/java_programs/coc.txt"));//Data set is getting copied into input stream through buffer mechanism.
			IOUtils.copyBytes(is, os, conf); // Copying the dataset from input stream to output stream
			
			//CODE DE VOTRE PROGRAMME ICI
			return 0;
		}
	}
	
	public static void main( String[] args ) throws Exception {
		int returnCode = ToolRunner.run(new MonApplication.MonProg(), args);
		System.exit(returnCode);
	}
}
//=====================================================================

