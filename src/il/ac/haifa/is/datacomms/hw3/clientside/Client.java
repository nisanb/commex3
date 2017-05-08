package il.ac.haifa.is.datacomms.hw3.clientside;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Random;

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
			log("Client " + id + " is loading...");
			Thread.sleep(3000);
			log("Client " + id + " is ready!");

			/**
			 * Send client is ready
			 */
			int amountOfMonsters = sendReady(is, os);
			log("Client " + id + " is able to log in (HP: " + healthPoints + "; Monsters: " + amountOfMonsters + ")");
			Random r = new Random();
			int currentMob = 0;
			
			/**
			 * As long as there are monsters in-game & Character isn't dead
			 */
			while (amountOfMonsters > 0 && healthPoints > 0 && currentMob < amountOfMonsters) {
				
				/**
				 * Use bandages if player HP is below 100
				 */
				
				
				/*
				 * Choose an attack type
				 */
				AttackType attackType = AttackType.values()[r.nextInt(2)];

				//Repeat sending an attack to a monster unless it is already dead
				if(!sendDamage(is, os, currentMob, attackType))
					currentMob++;
				
				
				if(healthPoints<100 && bandagesUsed<2)
					if(!sendBandage(is, os))
						log("Client "+id+" failed to use bandage.");
					else log("Client "+id+" successfully used a bandage.");
				

			}
			
			sendFin(is, os);
			os.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			log("CLIENT " + id + " IO Exception" + e.getStackTrace());
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
		// Send RDY Request
		log("Client " + id + " is sending RDY Request");
		sendMessage(os, id + "", "RDY", "?", "?");

		String[] returned = is.readUTF().split(" ");
		log("Client " + id + " received answer!!");
		if (returned[0].equals("NACK")) // Message received an error
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
		log("Client " + id + " is attempting to attack mob " + monsterNum + " with " + attackType + " DMG");
		sendMessage(os, id + "", "DMG", monsterNum + "", attackType.toString());

		String receivedMessage = is.readUTF().replace("\n", "");
		if (receivedMessage.contains("NACK")) {
			log("Client " + id + " failed to attack mob " + monsterNum + ".");
			return false;
		}
		
		log("Client " + id + " successfully attacked mob " + monsterNum + ".");

		String[] msgArray = receivedMessage.split(" ");
		Integer newHP = Integer.parseInt(msgArray[4]);

		if (newHP != healthPoints) {
			log("Client " + id + " was dealt with " + (healthPoints - newHP) + " damage (New HP: " + newHP);
			healthPoints = newHP;
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
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
		os.writeUTF(id+" BND");
		String[] returnedBND = is.readUTF().split(" ");
		Integer newHP = Integer.parseInt(returnedBND[4]);
		if(returnedBND[0].equals("NACK")){
			if(newHP == 0){
				log("Client "+id+" is already dead! Cannot heal self.");
			} else {
				log("Client "+id+" has no bandages left, therefore no heal was made.");
				healthPoints = newHP;
			}
			return false;
		}
		bandagesUsed++;
		log("Client "+id+" successfully used bandage (Old HP: "+healthPoints+" new HP: "+newHP);
		healthPoints = newHP;
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
		log("Client " + id + " is closing the game..");
		sendMessage(os, id + "", "FIN", "?", "?");
		is.close();
	}

	/**
	 * Log function
	 * 
	 * @param string
	 */
	protected static void log(String string) {
		System.out.println(LocalTime.now() + " - " + string);

	}

	private void sendMessage(DataOutputStream os, String a, String b, String c, String d) {
		try {
			os.writeUTF(a + " " + b + " " + c + " " + d + " \n");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
