package il.ac.haifa.is.datacomms.hw3.clientside;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalTime;

import il.ac.haifa.is.datacomms.hw3.util.Consts;

public final class ClientDriver {
	private ClientDriver() {
	}

	public static void main(String[] args) {
		// TODO
		log("Initiating a new client session...");
		try {
			log("Aquiring socket ("+Consts.IP+":"+Consts.PORT+")");
			Socket s = new Socket(Consts.IP, Consts.PORT);

			InputStream receive = s.getInputStream();
			DataOutputStream os = new DataOutputStream(s.getOutputStream());
			os.writeUTF("4 RDY\n");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void log(String string) {
		System.out.println(LocalTime.now() + " - " + string);

	}
}
