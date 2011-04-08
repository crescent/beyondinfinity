package infinity;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

public class MyUUID  {
	
	long mostSignificantBits;
	long leastSignificantBits;

	public MyUUID(UUID UUID) {
		mostSignificantBits = UUID.getMostSignificantBits();
		leastSignificantBits = UUID.getLeastSignificantBits();
	}
	public static MyUUID random() {
		return new MyUUID(UUID.randomUUID());
	}
	
	public void reset()
	{
		mostSignificantBits=0;
		leastSignificantBits=0;
	}
	
	public void readExternal(DataInput in) throws IOException  {
		mostSignificantBits= in.readLong();
		leastSignificantBits= in.readLong();
	}

	public void writeExternal(DataOutput out) throws IOException {
		out.writeLong(mostSignificantBits);
		out.writeLong(leastSignificantBits);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (leastSignificantBits ^ (leastSignificantBits >>> 32));
		result = prime * result
				+ (int) (mostSignificantBits ^ (mostSignificantBits >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyUUID other = (MyUUID) obj;
		if (leastSignificantBits != other.leastSignificantBits)
			return false;
		if (mostSignificantBits != other.mostSignificantBits)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return Integer.toString(hashCode());
	}
}
