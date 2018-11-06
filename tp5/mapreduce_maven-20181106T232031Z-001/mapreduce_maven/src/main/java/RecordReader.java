import java.io.IOException;

import org.apache.hadoop.io.LongWritable;

public class RecordReader<LongWritable, Point2DWritable> {
	
	FakeInputSplit fis;
	
	public RecordReader() {};
	
	public RecordReader(FakeInputSplit fis) throws IOException, InterruptedException {
		this.fis = fis;
		for(int i=0; i<fis.getLength(); i++) {
			
		}
	}
}
