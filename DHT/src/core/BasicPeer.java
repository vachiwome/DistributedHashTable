/**
 * @author Valentine Chiwome
 * Distributed Systems Assignment 2
 * Spring 2014
 */
package core;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.SocketManager;

public class BasicPeer {

	/** 
	 * id is this is the identifier of this peer
	 * predecessor is the identifier space predecessor of the current process
	 * sockManager is the object that is responsible for listening to incoming messages
	 * sockets stores the sockets that this process can use to talk to peer processes
	 * items is where the files are stored(strings in my case)
	 * hashes map keys to their computed hashvalues. This allows the clients
	 * to use any system independent hash functions
	 */
	protected int id;
	protected int predecessor;
	protected SocketManager sockManager;
	protected Map<Integer, SocketManager> sockets = new HashMap<Integer, SocketManager>();
	protected List<Integer> fingers = new ArrayList<Integer>();
	protected Map<Integer, String> items = new HashMap<Integer, String>();
	protected Map<Integer, Integer> hashes = new HashMap<Integer, Integer>();

	public static final int M = 3;

	public BasicPeer(int id, int port) {
		this.id = id;
		this.sockManager = new SocketManager(port);
		for (int i = 0; i < BasicPeer.M; i++){
			fingers.add(-1);
		}
		try {
			this.sockManager.setSocket(new DatagramSocket(port));
		}
		catch(SocketException ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * Methods for accessing the parameters
	 * for this process.
	 */
	public int getLocalSuccessor() {
		return fingers.get(0);
	}
	
	public int getLocalPredecessor() {
		return predecessor;
	}
	
	public void setLocalPredecessor(int predecessor) {
		this.predecessor = predecessor;
	}
	
	public void setLocalFinger(int i, int finger) {
		fingers.set(i, finger);
	}
	
	public int getLocalFinger(int i) {
		return fingers.get(i);
	}
	
	/**
	 * Display the state of the finger tables
	 */
	public void displayFingerTable() {
		System.out.println("Current Process is P#" + id);
		for (int i = 0; i < fingers.size(); i++) {
			System.out.println("Finger F#" + i + " is " + "P#" + fingers.get(i));
		}
	}
	
	public SocketManager getSockManager() {
		return sockManager;
	}
	
	public void setSockManager(SocketManager sockManager) {
		this.sockManager = sockManager;
	}
	/**
	 * Get the first possible value that can be stored in finger[i]
	 */
	public int fingerStart(int i) {
		int d = (int ) (this.getId() + Math.pow(2.0, i));
		int thresh = (int ) Math.pow(2.0,BasicPeer.M);
		return (d % thresh);
	}

	/**
	 * Notify this peer of another peer with identifier id
	 * and listening on port port
	 */

	public void addProcess(int id, int port) {
		sockets.put(id, new SocketManager(port));
	}
		
	public int getId() {
		return id;
	}

}
