/**
 * @author Valentine Chiwome
 * Distributed Systems Assignment 2
 * Spring 2014
 */

package test;

import core.PeerRunner;
import core.ReceivingPeer;

public class PeerA {
	/**
	 * Run this peer first. Tell it about other peers that are coming using the code below
	 */
	public static void main(String args[]) {
		PeerRunner peerRunner = new PeerRunner(0, 2014, 4);
		ReceivingPeer peer = peerRunner.getPeer();
		peer.join(-1);
		// add processes with id 3 and 6
		peer.addProcess(3, 2015);
		peer.addProcess(6, 2016);
		peer.addProcess(7, 2017);
		
		peerRunner.run();
	}
	
}
