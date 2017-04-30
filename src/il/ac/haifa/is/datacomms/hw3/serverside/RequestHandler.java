package il.ac.haifa.is.datacomms.hw3.serverside;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Random;

import il.ac.haifa.is.datacomms.hw3.util.AttackType;
import il.ac.haifa.is.datacomms.hw3.util.Log;

/**
 * class for handling client requests.
 */
public final class RequestHandler implements Runnable {
	/** socket between server and client. */
	private Socket socket;

	/** RDY Lock **/
	private static Object rdyLock = new Object();
	/** character associated with the connection. */
	private Character character;

	/** All Ready Checkpoint **/
	private static Boolean allReady = false;

	/**
	 * @param socket
	 *            open socket with client.
	 */
	public RequestHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		Server.log("Initiating client connection handler");
		try {

			DataInputStream streamIn = new DataInputStream(this.socket.getInputStream());
			DataOutputStream streamOut = new DataOutputStream(this.socket.getOutputStream());

			while (true) {
				String MESSAGE = streamIn.readUTF().replace("\n", "");

				// Take care of BR
				Server.log("Received Message: " + MESSAGE);
				String[] tmpMessage = MESSAGE.split(" ");

				switch (tmpMessage[1]) {
				case "RDY":
					handleReady(tmpMessage, streamOut);
					break;
				case "DMG":
					handleDamage(tmpMessage, streamOut);
					break;
				case "BND":
					handleBandage(tmpMessage, streamOut);
					break;
				case "FIN":
					handleFin();
					return;

				default:
					Server.log("Received unknown protocol message: " + MESSAGE);
					break;
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			// System.out.println("Tried to accept a message but it was not
			// successfull!");
			// Server.log("Closing connection..");
			// System.out.println("Socket is " + this.socket);
		}

	}

	/**
	 * handles RDY Requests from client.
	 * <p>
	 * adds client id to server's ready list, attaches relevant character to
	 * this handler, and responds with an ACK.
	 * 
	 * @param req
	 *            request message sent by client.
	 * @param os
	 *            output stream to respond to client on.
	 * @throws IOException
	 */
	private void handleReady(String[] req, DataOutputStream os) throws IOException {
		int ClientID = -1;
		try {
			ClientID = Integer.parseInt(req[0]);

			Server.log("Client " + req[0] + " - received RDY Request");

			// Load character
			Server.log("Loading character #" + req[0] + " (Num of chars: " + Server.getCharacters().size() + ")");
			this.character = Server.getCharacters().get(Server.getCharacters().indexOf(new Character(ClientID, "")));
			synchronized (rdyLock) {
				if (this.character == null || !Server.addReadyPlayer(ClientID)) {
					// Character was not found
					Server.log("Character " + ClientID + " wasn't found!");
					sendMessage(os, "NACK", "?", "?", "?", "?");
					return;
				}
				if (!Server.isAllReady()) {
					Server.log(ClientID + " is waiting for all players to start.");
					while (!allReady)
						rdyLock.wait();
				} else {
					Server.log("All players are ready! Starting session..");
					allReady = true;
					rdyLock.notifyAll();
				}

			}

			Server.log("Sending approval: ACK RDY " + Server.getMonsters().size() + " ? "
					+ this.character.getHealthPoints());
			sendMessage(os, "ACK", "RDY", Server.getMonsters().size() + "", "?", this.character.getHealthPoints() + "");

		} catch (NumberFormatException nfe) {
			Server.log("Couldn't retreive client ID!");
			closeConn();
		} catch (ArrayIndexOutOfBoundsException aio) {
			Server.log("Couldn't find character " + ClientID);
			closeConn();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void closeConn() throws IOException {

		socket.close();
	}

	/**
	 * handles DMG Requests from client. ex: "4 DMG 2 PHY" -> if alive (monster
	 * and character) before request: "ACK DMG 2 14057 96" if dead (monster or
	 * character) before request: "NACK DMG 2"
	 * 
	 * @param req
	 *            request message send by client.
	 * @param os
	 *            output stream to respond to client on.
	 * @throws IOException
	 */
	private void handleDamage(String[] req, DataOutputStream os) {

		AttackType atkType = AttackType.valueOf(req[3]);
		Monster monAttacked = Server.getMonsters().get(Integer.parseInt(req[2]));
		Boolean attackSuccess = false;

		/**
		 * Check if monster and character are alive
		 */
		if (!monAttacked.isAlive() || !character.isAlive()) {
			sendMessage(os, "NACK", "DMG", req[2], "", "");
			return;
		}

		switch (atkType) {
		case MAG:
			attackSuccess = monAttacked.hitWithMagicAttack(character);
			break;
		case PHY:
			attackSuccess = monAttacked.hitWithPhysicalAttack(character);
			break;
		}

		if(!attackSuccess){
			sendMessage(os, "NACK", "DMG", req[2], "", "");
			return;
		}
		
		int hpBefore = character.getHealthPoints();
		/**
		 * Return Attack to character
		 */
		if (new Random().nextBoolean())
			character.wound(monAttacked.getDamage());
		Log.addTeamLog(character.getNickname(), new Object[]{LocalTime.now(), monAttacked.getName(),
				req[3] + ": " + (req[3].equals("PHY") ? character.getPhysicalDamage() : character.getMagicalDamage()),
				hpBefore, character.getHealthPoints()});
		sendMessage(os, "ACK", "DMG", req[2], monAttacked.getHealthPoints() + "", character.getHealthPoints() + "");


	}

	/**
	 * handles BND Requests from client. 11.3 : Bandage. "4 BND" -> if alive and
	 * hasn't used all bandages: "ACK BND ? ? 131" if dead: "NACK BND ? ? 0" if
	 * used all bandages: "NACK BND ? ? 96"
	 * 
	 * @param req
	 *            request message send by client.
	 * @param os
	 *            output stream to respond to client on.
	 * @throws IOException
	 */
	private void handleBandage(String[] req, DataOutputStream os) throws IOException {
		Integer ClientID = Integer.parseInt(req[0]);
		Integer finalClientHP = character.getHealthPoints();
		Server.log("Received BND Request from " + character.getNickname() + " (Has " + character.getBandagesLeft()
				+ " bandages)");
		if (!character.useBandage()) {
			Server.log("Failed healing " + character.getNickname() + " (HP: " + character.getHealthPoints() + ")");
			sendMessage(os, "NACK", "BND", "?", "?", character.getHealthPoints() + "");
			return;
		}
		Server.log("Successfully healed " + character.getNickname() + " with 25 HP (New HP: "
				+ character.getHealthPoints() + ");");
		sendMessage(os, "ACK", "BND", "?", "?", character.getHealthPoints() + "");

	}

	/**
	 * handles FIN Requests from client.
	 */
	private void handleFin() {
		Server.log("Receiving closing request from " + character.getNickname());
		Server.removePlayer();

		try {
			closeConn();
		} catch (Exception e) {
			Server.log("Couldn't close request due to:");
			e.printStackTrace();
		}

	}

	private void sendMessage(DataOutputStream os, String a, String b, String c, String d, String e) {
		Server.log("Sending Message: " + a + " " + b + " " + c + " " + d + " " + e + " \n");
		try {
			os.writeUTF(a + " " + b + " " + c + " " + d + " " + e + " \n");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	class NoHPException extends Exception {
	}
}
