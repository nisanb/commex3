package il.ac.haifa.is.datacomms.hw3.serverside;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * class for handling client requests.
 */
public final class RequestHandler implements Runnable {
	/**socket between server and client.*/
	private Socket socket;
	
	/**character associated with the connection.*/
	private Character character;
	
	/**
	 * @param socket open socket with client.
	 */
	public RequestHandler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		// TODO
	}
	
	/**
	 * handles RDY Requests from client.
	 * <p>adds client id to server's ready list, 
	 * attaches relevant character to this handler, and responds with an ACK.
	 * @param req request message sent by client.
	 * @param os output stream to respond to client on.
	 * @throws IOException
	 */
	private void handleReady(String[] req, DataOutputStream os) throws IOException {
		// TODO
	}
	
	/**
	 * handles DMG Requests from client.
	 * @param req request message send by client.
	 * @param os output stream to respond to client on.
	 * @throws IOException
	 */
	private void handleDamage(String[] req, DataOutputStream os) throws IOException {
		// TODO
	}
	
	/**
	 * handles BND Requests from client.
	 * @param req request message send by client.
	 * @param os output stream to respond to client on.
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
