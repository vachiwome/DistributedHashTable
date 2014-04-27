/**
 * @author Valentine Chiwome
 * Distributed Systems Assignment 2
 * Spring 2014
 */

package test;

import core.PeerRunner;
import core.ReceivingPeer;

public class PeerB {
	/**
	 * This is the second peer to be executed. Tell it about other peers as well
	 */
	public static void main(String args[]) {
		PeerRunner peerRunner = new PeerRunner(3, 2015, 4);
		ReceivingPeer peer = peerRunner.getPeer();
		// add processes with id 0 and 6
		peer.addProcess(0, 2014);
		peer.addProcess(6, 2016);
		peer.addProcess(7, 2017);
	
		peer.join(0);
		
		peerRunner.run();
	}
}