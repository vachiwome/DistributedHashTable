/**
 * @author Valentine Chiwome
 * Distributed Systems Assignment 2
 * Spring 2014
 */

package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Util {
	/**
	 * Serialize an object into a byte array
	 */
	public static byte[] serialize(Object obj) {
		try {
		    ByteArrayOutputStream out = new ByteArrayOutputStream();
		    ObjectOutputStream os = new ObjectOutputStream(out);
		    os.writeObject(obj);
		    return out.toByteArray();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	/**
	 * Deserialize a byte array into a message object
	 */
	public static Object deserialize(byte[] data) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(data);
			ObjectInputStream is = new ObjectInputStream(in);
			return is.readObject();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * return true is a is in the closed range [lower, upper]. 
	 * 2^m is unreachable. 
	 */
	public static boolean isInClosedRange(int a, int lower,int upper, int m) {
		if (a == upper || a == lower) {
			return true;
		}
		int max = (int) Math.pow(2.0, 1.0*m);
		//a = (a == max) ? 0 : a;
		int iter = lower;

		while (iter != upper) {
			//System.out.println(a + " a lower " + lower + " upper " + upper + " iter "+iter + " max "+max);

			if (iter == a){
				//System.out.println("Returning true");
				return true;
			}
			iter = (iter+1) % max;
		}
		//System.out.println("Returning false");
		return false;
	}

	/**
	 * gets the nextId after id. 
	 * 2^m is unreachable.
	 */
	public static int next(int id, int m) {
		int max = (int) Math.pow(2.0, 1.0*m);
		return ((id + 1) %  max);
	}
	
	/**
	 * get the previousId just before id. 
	 * 2^m is unreachable
	 */
	 public static int previous(int id, int m) {
		 int max = (int) Math.pow(2.0, 1.0*m);
		 return (id == 0) ? (max-1) : (id - 1);
	 }
	 
	 /**
	  * Subtract in modulo 2^m
	  */
	 public static int subtract(int a, int b, int m) {
		 int max = (int) Math.pow(2.0, 1.0*m);
		 if (a >= b) {
			 return a - b;
		 }
		 if (a + (max - b) < 0) {
			 throw new RuntimeException("Negative values are invalid, a="+a+" b="+b+" max="+max);
		 }
		 return a + (max - b);
	 }
}

