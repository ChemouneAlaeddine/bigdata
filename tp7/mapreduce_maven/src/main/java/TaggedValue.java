import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class TaggedValue implements Writable {

	private type t;
	private String value;
	
	public TaggedValue(type t, String value) {
		this.t = t;
		this.value = value;
	}

	public void readFields(DataInput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void write(DataOutput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public type getT() {
		return t;
	}

	public void setT(type t) {
		this.t = t;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
