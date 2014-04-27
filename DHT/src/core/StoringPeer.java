/**
 * @author Valentine Chiwome
 * Distributed Systems Assignment 2
 * Spring 2014
 */

package core;

import java.util.Map;


public class StoringPeer extends NetworkingPeer {

	/**
	 * This is a peer that is capable of storing information about keys and values that
	 * are supplied by the client
	 */
	public StoringPeer(int id, int port) {
		super(id, port);
	}
	
	/**
	 * Check if this host contains the specified key
	 */
	public boolean containsKey(Integer key) {
		return items.containsKey(key);
	}
	
	/**
	 * returns the item with key "key".
	 * If not found, null is returned
	 */
	
	public String getItem(Integer key, int hash) {
		checkHashValidity(hash);
		if (items.containsKey(key)) {
			return items.get(key);
		}
		
		int succ = findSuccessor(hash);
		if (succ == id) {
			return null;
		}
		Object item = getItemsFromPeer(succ, key).get(0);
		return (String) item;
	}
	
	/**
	 * resolves the host that is currently storing the key "key"
	 */
	public int getOwner(Integer key, int hash) {
		checkHashValidity(hash);
		if (items.containsKey(key)) {
			return this.id;
		}
		
		int succ = findSuccessor(hash);
		Object item = getItemsFromPeer(succ, key).get(0);
		if (item == null) {
			return -1;
		}
		return succ;
		
	}
	/**
	 * adds items to this host
	 */
	public void addItemLocally(Integer key, String value, int hash) {
		System.out.println("Storing [" + key + " => " + value + "] at P#" + id);
		checkHashValidity(hash);
		items.put(key, value);
		hashes.put(key, hash);
	}
	
	/**
	 * searches for the responsible host before adding items
	 * to it
	 */
	public void addItemGlobally(Integer key, String value, int hash) {
		checkHashValidity(hash);
		int succ = this.findSuccessor(hash);
		//System.out.println("Successor for "+ hash + " = " + succ);
		if (succ == this.id) {
			addItemLocally(key, value, hash);
		}
		else {
			addItemToPeer(succ, key, value);
		}
	}
	/**
	 * remove items at this host
	 */
	public void removeItemLocally(Integer key) {
		if (items.containsKey(key)) {
			System.out.println("Removing key " + key +"from Peer" + id);
			items.remove(key);
			hashes.remove(key);
		}
	}
	
	/**
	 * resolves who owns an item before deleting it.
	 * You need the key together with the hashcode.
	 * Use a hash function of your choice.
	 */
	public void removeItemGlobally(Integer key, int hash) {
		checkHashValidity(hash);
		int succ = this.findSuccessor(hash);
		if (succ == this.id) {
			removeItemLocally(key);
		}	
		else {
			removeItemFromPeer(succ, key);
		}
	}
	
	
	public Map<Integer, Integer> getHashes() {
		return hashes;
	}
	
	/**
	 * guard against error conditions
	 * make sure the hash is within the bounds of [0,2^m)
	 */
	public void checkHashValidity(int hash) {
//		if (hash < 0 || hash >= Math.pow(2.0, BasicPeer.M)) {
//			throw new RuntimeException("The value of your hash " + hash + " is invalid");
//		}
	}

}
