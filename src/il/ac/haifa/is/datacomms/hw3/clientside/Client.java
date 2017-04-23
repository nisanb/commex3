package il.ac.haifa.is.datacomms.hw3.clientside;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalTime;

import il.ac.haifa.is.datacomms.hw3.util.AttackType;
import il.ac.haifa.is.datacomms.hw3.util.Consts;

/**
 * class representation of a game client ran in a player's computer.
 */
public class Client implements Runnable {
	/** used to number the clients created. */
	private static int nextId;

	/** client's id. */
	private int id;

	/** client's character's last known health points. */
	private int healthPoints;

	/** has client used all bandages available for healing. */
	private int bandagesUsed;

	public Client() {
		id = nextId++;
	}

	@Override
	public void run() {
		// TODO
		log("Starting Client ID#" + id);
		log("Client #" + id + " is aquiring socket (" + Consts.IP + ":" + Consts.PORT + ")");
		Socket s;
		try {
			s = new Socket(Consts.IP, Consts.PORT);
			s.setSoTimeout(5000);
			
			DataInputStream is = new DataInputStream(s.getInputStream());
			DataOutputStream os = new DataOutputStream(s.getOutputStream());
			
			/**
			 * Client will now load the game
			 */
			log("Client "+id+" is loading...");
			Thread.sleep(3000);
			log("Client "+id+" is ready!");
			
			
			
			/**
			 * Send client is ready
			 */
			int amountOfMonsters = sendReady(is, os);
			log("Client "+id+" is able to log in (HP: "+healthPoints+"; Monsters: "+amountOfMonsters+")");
//			while(amountOfMonsters>0){
//				/**
//				 * As long as there are monsters in-game
//				 */
//				
//			}

			os.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			log("CLIENT "+id+" IO Exception" + e.getStackTrace());
			e.printStackTrace();
			// e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Sends a RDY Request to Server.
	 * 
	 * @param is
	 *            input stream to receive server response on.
	 * @param os
	 *            output stream to send request to server on.
	 * @return amount of monsters in play session, or -1 if something went
	 *         wrong.
	 * @throws IOException
	 */
	private int sendReady(DataInputStream is, DataOutputStream os) throws IOException {
		//Send RDY Request
		log("Client "+id+" is sending RDY Request");
		os.writeUTF(id+" RDY \n");
		
		String[] returned = is.readUTF().split(" ");
		log("Client "+id+" received answer!!");
		if(returned[0].equals("NACK")) //Message received an error
			return -1;
		
		
		this.healthPoints = Integer.parseInt(returned[4]);
		
		return Integer.parseInt(returned[2]);
	}

	/**
	 * Sends a DMG Request to Server.
	 * 
	 * @param is
	 *            input stream to receive server response on.
	 * @param os
	 *            output stream to send request to server on.
	 * @param monsterNum
	 *            number of monster to be attacked.
	 * @param attackType
	 *            attack type.
	 * @return true if damage dealt successfully and monster, false otherwise.
	 * @throws IOException
	 */
	private boolean sendDamage(DataInputStream is, DataOutputStream os, int monsterNum, AttackType attackType)
			throws IOException {
		return true;
		// TODO
	}

	/**
	 * Send a BND Request to Server.
	 * 
	 * @param is
	 *            input stream to receive server response on.
	 * @param os
	 *            output stream to send request to server on.
	 * @return true if bandage applied successfully, false otherwise (or corrupt
	 *         response).
	 * @throws IOException
	 */
	private boolean sendBandage(DataInputStream is, DataOutputStream os) throws IOException {
		// TODO
		return true;
	}

	/**
	 * Sends a FIN Request to Server.
	 * 
	 * @param is
	 *            input stream to receive server response on.
	 * @param os
	 *            output stream to send request to server on.
	 * @throws IOException
	 */
	private void sendFin(DataInputStream is, DataOutputStream os) throws IOException {
		// TODO

	}
	
	/**
	 * Log function
	 * @param string
	 */
	protected static void log(String string) {
		System.out.println(LocalTime.now() + " - " + string);

	}
}
