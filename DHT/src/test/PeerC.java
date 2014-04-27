/**
 * @author Valentine Chiwome
 * Distributed Systems Assignment 2
 * Spring 2014
 */

package test;

import core.PeerRunner;
import core.ReceivingPeer;

public class PeerC {
	/**
	 * Third peer to be executed. Tell it about other peers as well
	 */
	public static void main(String args[]) {
		
		PeerRunner peerRunner = new PeerRunner(6, 2016, 4);
		ReceivingPeer peer = peerRunner.getPeer();
		// add processes with id 3 and 0
		peer.addProcess(0, 2014);
		peer.addProcess(3, 2015);
		peer.addProcess(7, 2017);

		peer.join(3);
		
		peerRunner.run();
	}
}
