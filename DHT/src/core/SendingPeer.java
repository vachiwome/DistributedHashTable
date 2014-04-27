/**
 * @author Valentine Chiwome
 * Distributed Systems Assignment 2
 * Spring 2014
 */

package core;

import java.util.List;
import java.util.ArrayList;

import network.Message;
import network.SocketManager;

public class SendingPeer extends BasicPeer {
		
	/**
	 * This is a peer that is capable of sending messages to other peers
	 */
	public SendingPeer(int id, int port) {
		super(id, port);
	}
	
	/**
	 * This method is used to ask for information from the peer.
	 * infoType specifies what information you are asking for.
	 * different types are listed in the Message class.
	 * input is the list of arguments that you may want your peer
	 * to use to gather the necessary information on your behalf
	 */
	public List<Object> getPeerInfo(int remoteId, List<Object> input, int infoType) {
		SocketManager sock = sockets.get(remoteId);
		Message m = new Message(id, infoType);
		input = (input == null) ? new ArrayList<Object>() : input;
		m.setData(input);	
		//System.out.println(id + " vs " + remoteId);
		sock.sendMessage(m, sock.getAddr(),sock.getPort());
		//System.out.println("Waiting to receive response");
		Message response = sockManager.processPacket(sockManager.receivePacket());
		//System.out.println("received response");
		return response.getData();
	}
	
	/**
	 * This method is used to gather information from peers.
	 */
	public List<Object> getItemsFromPeer(int peerId, int key) {
		List<Object> input = new ArrayList<Object>();
		input.add(key);
		List<Object> peerInfo = getPeerInfo(peerId, input, Message.REQUEST_ITEM_FROM_PEER);
		return peerInfo;
	}
	
	/**
	 * Ask process remoteId for its successor
	 */
	public int getPeerSuccessor(int remoteId) {
		List<Object> peerInfo = getPeerInfo(remoteId, null, Message.FIND_PEER_SUCCESSOR);
		int peerSucc = (Integer) peerInfo.get(0);
		return peerSucc;
	}
		
	/**
	 * Ask your peer what it thinks is the successor for a certain key
	 */
	public int getPeerKeySuccessor(int remoteId, int key) {
		List<Object> input = new ArrayList<Object>();
		input.add(key);
		List<Object> peerInfo = getPeerInfo(remoteId,input, Message.FIND_PEER_KEY_SUCCESSOR);
		int peerKeySucc = (Integer) peerInfo.get(0);
		return peerKeySucc;
	}
	
	/**
	 * Ask process remoteId for its predecessor
	 */
	public int getPeerPredecessor(int remoteId) {
		List<Object> peerInfo = getPeerInfo(remoteId, null, Message.FIND_PEER_PREDECESSOR);
		int peerPred = (Integer) peerInfo.get(0);
		return peerPred;
	}
	
	/**
	 * Ask process remoteId what its closest preceding finger for key is
	 */
	public int getPeerClosestPredFinger(int remoteId, int key) {
		List<Object> input = new ArrayList<Object>();
		input.add(key);
		List<Object> peerInfo = getPeerInfo(remoteId,input, Message.FIND_PEER_CLOSEST_PRED_FINGER);
		int peerFinger = (Integer) peerInfo.get(0);
		return peerFinger;
	}
	
	/**
	 * This method is used to send information to your
	 * peers. The information is encoded in the input list
	 */
	public void sendInfoToPeer(int remoteId, List<Object> input, int type) {
		//System.out.println(id + " => " + remoteId+ " : " + input);
		SocketManager sock = sockets.get(remoteId);
		Message msg = new Message(id, type);
		msg.setData(input);
		sock.sendMessage(msg, sock.getAddr(), sock.getPort());		
	}
	
	/**
	 * Method for modifying your peer's predecessor
	 */
	public void setPeerPredecessor(int peerId, int predecessor) {
		List<Object> input = new ArrayList<Object>();
		input.add(predecessor);
		sendInfoToPeer(peerId, input, Message.SET_PEER_PREDECESSOR);
	}

	/**
	 * Method for asking your peer to change its finger table contents
	 */
	public void updatePeerFingerTable(int peerId, int s, int i) {
		List<Object> input = new ArrayList<Object>();
		input.add(s);
		input.add(i);
		//System.out.println("From here?");
		sendInfoToPeer(peerId, input, Message.UPDATE_PEER_FINGER_TABLE);	
	}

	/**
	 * Removing stuff from your peers
	 */
	public void removeItemFromPeer(int peerId, int key) {
		List<Object> input = new ArrayList<Object>();
		input.add(key);
		sendInfoToPeer(peerId, input, Message.REMOVE_ITEM_FROM_PEER);
	}
	
	/**
	 * adding items to your peers.
	 */
	public void addItemToPeer(int peerId, int key, String value) {
		List<Object> input = new ArrayList<Object>();
		input.add(key);
		input.add(value);
		sendInfoToPeer(peerId, input, Message.ADD_ITEM_TO_PEER);
	}
	/**
	 * ask peer to print its finger table
	 */
	public void askPeerToPrintFingerTable(int peerId) {
		sendInfoToPeer(peerId, null, Message.ASK_PEER_TO_PRINT__TABLE);		
	}
}
