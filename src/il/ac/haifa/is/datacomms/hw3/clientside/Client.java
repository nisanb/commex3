package il.ac.haifa.is.datacomms.hw3.clientside;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import il.ac.haifa.is.datacomms.hw3.util.AttackType;

/**
 * class representation of a game client ran in a player's computer.
 */
public class Client implements Runnable {
	/**used to number the clients created.*/
	private static int nextId;
	
	/**client's id.*/
	private int id;
	
	/**client's character's last known health points.*/
	private int healthPoints;
	
	/**has client used all bandages available for healing.*/
	private int bandagesUsed;
	
	public Client() {
		id = nextId++;
	}
	
	@Override
	public void run() {
		// TODO
	}
	
	/**
	 * Sends a RDY Request to Server.
	 * @param is input stream to receive server response on.
	 * @param os output stream to send request to server on.
	 * @return amount of monsters in play session, or -1 if something went wrong.
	 * @throws IOException 
	 */
	private int sendReady(DataInputStream is, DataOutputStream os) throws IOException {
		// TODO
		return 0;
	}
	
	/**
	 * Sends a DMG Request to Server.
	 * @param is input stream to receive server response on.
	 * @param os output stream to send request to server on.
	 * @param monsterNum number of monster to be attacked.
	 * @param attackType attack type.
	 * @return true if damage dealt successfully and monster, false otherwise.
	 * @throws IOException
	 */
	private boolean sendDamage(DataInputStream is, DataOutputStream os, 
			int monsterNum, AttackType attackType) throws IOException {
		return true;
		// TODO
	}
	
	/**
	 * Send a BND Request to Server.
	 * @param is input stream to receive server response on.
	 * @param os output stream to send request to server on.
	 * @return true if bandage applied successfully, false otherwise (or corrupt response).
	 * @throws IOException
	 */
	private boolean sendBandage(DataInputStream is, DataOutputStream os) throws IOException {
		// TODO
		return true;
	}
	
	/**
	 * Sends a FIN Request to Server.
	 * @param is input stream to receive server response on.
	 * @param os output stream to send request to server on.
	 * @throws IOException
	 */
	private void sendFin(DataInputStream is, DataOutputStream os) throws IOException {
		// TODO

	}
}
