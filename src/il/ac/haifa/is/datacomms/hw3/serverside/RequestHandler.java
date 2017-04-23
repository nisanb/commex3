package il.ac.haifa.is.datacomms.hw3.serverside;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * class for handling client requests.
 */
public final class RequestHandler implements Runnable {
	/** socket between server and client. */
	private Socket socket;

	/** character associated with the connection. */
	private Character character;

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

			Server.log("Awaiting input");
			// DataInputStream streamIn = new DataInputStream(new
			// BufferedInputStream(this.socket.getInputStream()));
			DataInputStream streamIn = new DataInputStream(this.socket.getInputStream());
			DataOutputStream streamOut = new DataOutputStream(this.socket.getOutputStream());
			// DataOutputStream streamOut = new DataOutputStream(new
			// BufferedOutputStream(this.socket.getOutputStream()));
			while (true) {
				String MESSAGE = streamIn.readUTF();
				// Take care of BR
				MESSAGE = MESSAGE.replace("\n", "");
				Server.log("Received Message: " + MESSAGE);
				String[] tmpMessage = MESSAGE.split(" ");

				/** Handle RDY Request **/
				if (tmpMessage[1].toString().equals("RDY")) {
					handleReady(tmpMessage, streamOut);
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
			if (this.character == null || !Server.addReadyPlayer(ClientID)) {
				// Character was not found
				Server.log("Character " + ClientID + " wasn't found!");
				os.writeUTF("NACK\n");
				return;
			}

			
		
			
			Server.log("Sending approval: ACK RDY " + Server.getMonsters().size() + " ? "
					+ this.character.getHealthPoints());
			os.writeUTF("ACK RDY " + Server.getMonsters().size() + " ? " + this.character.getHealthPoints());
		} catch (NumberFormatException nfe) {
			Server.log("Couldn't retreive client ID!");
			closeConn();
		} catch (ArrayIndexOutOfBoundsException aio) {
			Server.log("Couldn't find character " + ClientID);
			closeConn();
		}
	}

	private void closeConn() throws IOException {
		socket.close();
	}

	/**
	 * handles DMG Requests from client.
	 * 
	 * @param req
	 *            request message send by client.
	 * @param os
	 *            output stream to respond to client on.
	 * @throws IOException
	 */
	private void handleDamage(String[] req, DataOutputStream os) throws IOException {
		// TODO
	}

	/**
	 * handles BND Requests from client.
	 * 
	 * @param req
	 *            request message send by client.
	 * @param os
	 *            output stream to respond to client on.
	 * @throws IOException
	 */
	private void handleBandage(String[] req, DataOutputStream os) throws IOException {
		// TODO
	}

	/**
	 * handles FIN Requests from client.
	 */
	private void handleFin() {
		// TODO
	}
}
