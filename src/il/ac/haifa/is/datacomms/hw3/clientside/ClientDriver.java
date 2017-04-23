package il.ac.haifa.is.datacomms.hw3.clientside;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import il.ac.haifa.is.datacomms.hw3.util.Consts;

public final class ClientDriver {
	private ClientDriver() {
	}

	public static void main(String[] args) {
		// TODO
		List<Thread> al = new ArrayList<Thread>();
		Client.log("Initiating a new client session...");
		try {

			for(int i=0; i<6; i++){
				Thread tmpThread = new Thread(new Client());
				al.add(tmpThread);
				tmpThread.start();
			}
//			for(Thread th : al){
//				th.join();
//			}
						
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
