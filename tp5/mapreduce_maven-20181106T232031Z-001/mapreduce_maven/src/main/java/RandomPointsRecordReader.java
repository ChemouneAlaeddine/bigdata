import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.TaskAttemptContext;
import org.apache.hadoop.mapreduce.InputSplit;

public class RandomPointsRecordReader extends RecordReader<LongWritable, Point2DWritable> {
	private LongWritable curKey = new LongWritable(0);
	private Point2DWritable curPoint = new Point2DWritable();
	private long nbPoints = 10000;
	
	public RandomPointsRecordReader() {}
	
	public void close() throws IOException {}
	
	public LongWritable getCurrentKey() throws IOException, InterruptedException {
		if (!(curKey.get() < this.nbPoints)) throw new IOException("no more points");
			return curKey;
	}
	
	public Point2DWritable getCurrentValue() throws IOException, InterruptedException {
		if (!(curKey.get() < this.nbPoints)) throw new IOException("no more points");
			return curPoint;
	}
	
	public float getProgress() throws IOException, InterruptedException {
		return (float)((double)curKey.get()/(double)this.nbPoints);
	}
	
	public void initialize(InputSplit arg0, TaskAttemptContext context) throws IOException, InterruptedException {
		this.nbPoints = arg0.getLength();
		curKey.set(-1);
	}
	
	public boolean nextKeyValue() throws IOException, InterruptedException {
		curKey.set(curKey.get()+1);
		curPoint.x = Math.random();
		curPoint.y = Math.random();
		return curKey.get() < this.nbPoints;
	}
}