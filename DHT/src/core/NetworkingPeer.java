/**
 * @author Valentine Chiwome
 * Distributed Systems Assignment 2
 * Spring 2014
 */

package core;


import util.Util;


public class NetworkingPeer extends SendingPeer {
	/**
	 * This peer is capable of doing all of the functionalities that 
	 * are required by chord. It can talk with other peers hence it is
	 * called a NetworkingPeer
	 */
	public NetworkingPeer(int id, int port) {
		super(id, port);
	}
	
	/**
	 * Find the place where key should be stored
	 */
	public int findSuccessor(int key) {
		if (this.id == key) {
			return this.id;
		}
		int nprime = findPredecessor(key);
		if (nprime == id) {
			return getLocalSuccessor();
		}
		//System.out.println("Successor for "+key+" is "+getPeerSuccessor(nprime)+" pred = " + nprime);
		return getPeerSuccessor(nprime);
	}
	
	/**
	 * Get the finger that is closest to the key but has a smaller key
	 */
	public int getLocalClosestPrecFinger(int id) {
		int m = BasicPeer.M;
		for (int i = m-1; i >= 0; i--) {
			if ((id - this.id) == 1) {
				return this.id;
			}
			int nNext = Util.next(this.getId(), m);
			int idPrev = Util.previous(id, m);
			if (Util.isInClosedRange(fingers.get(i), nNext, idPrev, m)) {
				return fingers.get(i);
			}
		}
		return this.id;
	}


	/**
	 * Return the Router that comes just before id
	 */
	public int findPredecessor(int id) {
		int nodeId = this.id;
		int m = BasicPeer.M;
		int nodeSucc = this.getLocalSuccessor();
		// (n, n.successor]
		while ( !Util.isInClosedRange(id, Util.next(nodeId,m), nodeSucc, m) ) {
			//System.out.println("id : " + id + " nodeId " + nodeId + " nodeSucc " + nodeSucc);
			if (nodeId == this.id) {
				nodeId = getLocalClosestPrecFinger(id);
			}
			else {
				nodeId = getPeerClosestPredFinger(nodeId, id);
			}
			nodeSucc = getPeerSuccessor(nodeId);
			
			if (nodeId == nodeSucc) {
				break;
			}
		}
		//System.out.println("findPredecessor returned " + nodeId + " for id " + id);
		return nodeId;
	}
	
	/**
	 * Update finger table until condition is not met anymore
	 */
	public void updateFingerTable(int s, int i) {
		// [n, finger[i].node)
		int m = BasicPeer.M;
		if (Util.isInClosedRange(s,this.id, Util.previous(fingers.get(i), m), m)) {
			this.fingers.set(i, s);
			if (this.predecessor != s) {
				if (this.predecessor != s) {
					updatePeerFingerTable(this.predecessor, s, i); 
				}
			}
		}
	}
	
	/**
	 * A new node needs to be stored in the finger tables of already existing nodes
	 */
	public void updateOthers() {
		int m = BasicPeer.M;
		for (int i = 0; i < m; i++) {
			int gap = (int) Math.pow(2.0, 1.0*i);
			//System.out.println("update others called");
			int nodeId = this.findPredecessor(Util.subtract(getId(), gap, m));
			//System.out.println("findPredessor returned");
			if (nodeId == this.id) {
				break;
			}
			//System.out.println("I is " + i);
			updatePeerFingerTable(nodeId,this.id, i);
		}
	}

	/**
	 * Method for initializing your finger table using information
	 * from some other node that already is in the network
	 */
	public void initFingerTable(int other) {
		//System.out.println("Asking " + other);
		int succOther = getPeerKeySuccessor(other, this.fingerStart(0));
		//System.out.println("Other responded with value " + succOther);
		this.fingers.set(0, succOther);
		this.predecessor = getPeerPredecessor(succOther);
		setPeerPredecessor(fingers.get(0), this.id);
		
		int m = BasicPeer.M;
		//System.out.println("About to loop");
		for (int i = 0; i < m-1; i++) {
			// [n, finger[i].node)
			int s = this.fingerStart(i+1);
			if ((fingers.get(i) - this.id) == 1) {
				succOther = getPeerKeySuccessor(other, this.fingerStart(i+1));
				fingers.set(i+1,succOther);				
			}
			else if (Util.isInClosedRange(s,Util.next(getId(),m),fingers.get(i),m)) {
				fingers.set(i+1,fingers.get(i));
			}
			else {
				succOther = getPeerKeySuccessor(other, this.fingerStart(i+1));
				fingers.set(i+1,succOther);
			}
		}
		
	}
	
	/**
	 * Code that is executed when a node joins the network
	 */
	
	public void join(int other) {
		//System.out.println("OTHER IS : " + other);
		if (other != -1) {
			//System.out.println("About to initialize finger tables");
			this.initFingerTable(other);
			//System.out.println("Init finger table returned");
			this.updateOthers();
			//System.out.println("update others returned");
			// move in (predecessor, n] from successor
		}
		else { // n is the only node in the network
			for (int i = 0; i < BasicPeer.M; i++) {
				this.fingers.set(i, this.id);
			}
			this.predecessor = this.id;
			
		}
		System.out.println(id + " succefully joined");
		displayFingerTable();
	}



}
