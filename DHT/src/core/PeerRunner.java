/**
 * @author Valentine Chiwome
 * Distributed Systems Assignment 2
 * Spring 2014
 */

package core;

import network.Message;
import network.SocketManager;

public class PeerRunner implements Runnable {
	
	private ReceivingPeer peer;
	
	public PeerRunner(int peerId, int peerPort, int numPeers) {
		peer = new ReceivingPeer(peerId, peerPort);
	}
	
	public void run() {
		SocketManager sock = peer.getSockManager();
		// wait for messages
		while (true) {
			Message m = sock.processPacket(sock.receivePacket());
			peer.receiveFromPeer(m);
		}
	}
	
	public ReceivingPeer getPeer() {
		return peer;
	}
}
