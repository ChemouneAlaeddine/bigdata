import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class TaggedValue implements Writable {

	private String key;
	private String value;
	
	public TaggedValue(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public void readFields(DataInput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void write(DataOutput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public String getT() {
		return key;
	}

	public void setT(String t) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
