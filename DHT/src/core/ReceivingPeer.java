/**
 * @author Valentine Chiwome
 * Distributed Systems Assignment 2
 * Spring 2014
 */

package core;

import java.util.List;

import network.Message;

public class ReceivingPeer extends StoringPeer {
	/**
	 * This class can receive and process any message
	 * that it receives from its peers
	 */
	public ReceivingPeer(int id, int port) {
		super(id, port);
	}
	/**
	 * This method receives a message and checks what type
	 * of message it is before passing it to the right 
	 * method
	 */
	public void receiveFromPeer(Message m) {
		//System.out.println("source " + m.getSourceId() + " destination " + id + " type " + m.getType());
		switch(m.getType()) {
		
			case Message.ADD_ITEM_TO_PEER:
				processAddItemToPeer(m);
				break;
			case Message.FIND_PEER_CLOSEST_PRED_FINGER:
				processFindPeerClosestPredFinger(m);
				break;
			case Message.FIND_PEER_KEY_SUCCESSOR:
				processFindPeerKeySuccessor(m);
				break;
			case Message.FIND_PEER_PREDECESSOR:
				processFindPeerPredecessor(m);
				break;
			case Message.FIND_PEER_SUCCESSOR:
				processFindPeerSuccesor(m);
				break;
			case Message.REMOVE_ITEM_FROM_PEER:
				processRemoveItemFromPeer(m);
				break;
			case Message.REQUEST_ITEM_FROM_PEER:
				processRequestItemFromPeer(m);
				break;
			case Message.SET_PEER_PREDECESSOR:
				processSetPeerPredecessor(m);
				break;
			case Message.UPDATE_PEER_FINGER_TABLE:
				processUpdatePeerFingerTable(m);
				break;
			case Message.ASK_PEER_TO_PRINT__TABLE:
				displayFingerTable();
				break;
			default:
				System.out.println("UNKNOWN TYPE : " + m.getType());
		}
	}
	
	/**
	 * Processing a request to add an item to my buffer
	 */
	public void processAddItemToPeer(Message m) {
		List<Object> data = m.getData();
		if (data.size() != 2) {
			throw new RuntimeException("Too few data for addItem");
		}
		int key = (Integer) data.get(0);
		String value = (String) data.get(1);
		int hash = (int) (key % Math.pow(2.0, BasicPeer.M));
		addItemLocally(key, value, hash);
	}

	/**
	 * code for handling a request to remove an item from my buffer
	 */
	public void processRemoveItemFromPeer(Message m) {
		List<Object> data = m.getData();
		//System.out.println(m.getData() + " " + m.getData().size());
		if (data.size() != 1) {
			throw new RuntimeException("Too few data for removeItem");
		}
		int key = (Integer) data.get(0);
		removeItemLocally(key);		
	}

	/**
	 * Method for handling a request to set my predecessor to a new value
	 */
	public void processSetPeerPredecessor(Message m) {
		List<Object> data = m.getData();
		if (data.size() != 1) {
			throw new RuntimeException("Too few data for setPeerPredecessor");
		}
		int pred = (Integer) data.get(0);
		setLocalPredecessor(pred);
	}

	/**
	 * Method to process a request to update my finger table
	 */
	public void processUpdatePeerFingerTable(Message m) {
		List<Object> data = m.getData();
		if (data.size() != 2) {
			throw new RuntimeException("Too few data for setPeerPredecessor");
		}
		//System.out.println(id + " processing update table from " + m.getSourceId());
		int s = (Integer) data.get(0);
		int i = (Integer) data.get(1);
		updateFingerTable(s, i);
	}

	/**
	 * Method for handling a request to find the closest preceding finger for
	 * some id
	 */
	public void processFindPeerClosestPredFinger(Message m) {
		List<Object> data = m.getData();
		if (data.size() != 1) {
			throw new RuntimeException("Data size too small for findClosestPF");
		}
		int id = (Integer) data.get(0);
		int pcf = getLocalClosestPrecFinger(id);
		data.set(0, pcf);
		sendInfoToPeer(m.getSourceId(), data, Message.REPLY);
	}
	
	/**
	 * Method for handling a request for the successor of a certain key
	 */
	public void processFindPeerKeySuccessor(Message m) {
		List<Object> data = m.getData();
		if (data.size() != 1) {
			throw new RuntimeException("Data size too small for findPKS");
		}
		int key = (Integer) data.get(0);
		int ks = findSuccessor(key);
		data.set(0, ks);
		sendInfoToPeer(m.getSourceId(), data, Message.REPLY);
	}
	
	/**
	 * Method for finding the predecessor of the current peer
	 */
	public void processFindPeerPredecessor(Message m){
		m.getData().add(predecessor);
		sendInfoToPeer(m.getSourceId(), m.getData(), Message.REPLY);
	}
	
	public void processFindPeerSuccesor(Message m) {
		m.getData().add(getLocalSuccessor());
		sendInfoToPeer(m.getSourceId(), m.getData(), Message.REPLY);
	}
	
	public void processRequestItemFromPeer(Message m) {
		List<Object> data = m.getData();
		if (data.size() != 1) {
			throw new RuntimeException("Data size too small for findClosest");
		}
		int key = (Integer) data.get(0);
		data.set(0, getItem(key, key));
		sendInfoToPeer(m.getSourceId(), data, Message.REPLY);
	}

}
