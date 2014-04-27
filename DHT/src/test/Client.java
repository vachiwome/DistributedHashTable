/**
 * @author Valentine Chiwome
 * Distributed Systems Assignment 2
 * Spring 2014
 */

package test;

import core.PeerRunner;
import core.ReceivingPeer;

public class Client {
	/**
	 * This is the client that adds and removes items from the 
	 * distributed system. Modify it and run it several times
	 */
	public static void main(String args[]) {
		PeerRunner peerRunner = new PeerRunner(7, 2017, 4);
		ReceivingPeer peer = peerRunner.getPeer();
		// add processes with id 0, 3 and 6
		peer.addProcess(0, 2014);
		peer.addProcess(3, 2015);
		peer.addProcess(6, 2016);
		
		System.out.println("About to join");
		peer.join(6);
		// hashes have to be in the range [0,8)
		// otherwise hash % 8 is performed
		peer.addItemGlobally(0, "ZERO", 0);
		
		peer.addItemGlobally(2, "TWO", 2);
		
		peer.addItemGlobally(4, "FOUR", 4);

		peer.addItemGlobally(5, "FOUR", 5);

		peer.addItemGlobally(7, "SEVEN", 7);
		
		peer.addItemGlobally(13, "THIRTEEN", 5);
		
		//remove 4 so that you get null return value
		peer.removeItemGlobally(5, 5);
		// expect null since 5 has been removed
		if(peer.getItem(5, 5) == null) {
			System.out.println("Trying to access item returned null");
		}

		System.out.println("key 0 is stored at Peer " + peer.getOwner(0, 0));
		System.out.println("key 2 is stored at Peer " + peer.getOwner(2, 2));
		System.out.println("key 4 is stored at Peer " + peer.getOwner(4, 4));

		System.out.println("key 7 is stored at Peer " + peer.getOwner(7, 7));
		System.out.println("key 13 is stored at Peer " + peer.getOwner(13, 5));
		
		// go to each process' console and see the final finger tables
		// the printed values are the correct ones according to the chord
		// algorithm. we have four peers, 0, 3, 6 and 7
		peer.askPeerToPrintFingerTable(0);
		peer.askPeerToPrintFingerTable(3);
		peer.askPeerToPrintFingerTable(6);
	}
	
	
}
