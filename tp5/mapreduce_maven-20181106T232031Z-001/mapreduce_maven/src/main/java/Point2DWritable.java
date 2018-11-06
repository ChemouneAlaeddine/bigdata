import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class Point2DWritable implements Writable {
	
	Double x;
	Double y;
	public Object p;
	
	public Point2DWritable(Double x, Double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point2DWritable() {
		this.x = (double) 0;
		this.y = (double) 0;
	}
	
	public Double getX() {
		return x;
	}
	
	public void setX(Double x) {
		this.x = x;
	}
	
	public Double getY() {
		return y;
	}
	
	public void setY(Double y) {
		this.y = y;
	}

	public void readFields(DataInput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void write(DataOutput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}
}