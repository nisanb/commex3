package il.ac.haifa.is.datacomms.hw3.serverside;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import il.ac.haifa.is.datacomms.hw3.util.Consts;

/**
 * class representation of Server.
 */
public final class Server {

	/** players ready list. */
	private static ArrayList<Integer> playersReady;

	/** server's characters. */
	private static ArrayList<Character> characters;

	/** server's monsters. */
	private static ArrayList<Monster> monsters;

	private Server() {
	}

	public static void main(String[] args) {
		// TODO

		/*
		 * Build server
		 */
		log("Initiating server...");
		log("Importing monsters from monsters.json");
		initMonsters();
		log("Importing characters from characters.json");
		initCharacters();
		log("Initiating players queue..");
		playersReady = new ArrayList<Integer>();
		
		/*
		 * Initiate Socket
		 */
		try {
			log("Initiating socket interface..");
			ServerSocket ss = new ServerSocket(Consts.PORT);
			List<Thread> threadList = new ArrayList<Thread>();
			while (true) {
				Socket newClient = ss.accept();
				log("Accepted a new request!");
				Thread t = new Thread(new RequestHandler(newClient));
				threadList.add(t);
				t.start();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void log(String string) {
		System.out.println(LocalTime.now() + " - " + string);
	}

	/**
	 * initializes characters.
	 */
	@SuppressWarnings("unchecked")
	private static void initCharacters() {
		characters = new ArrayList<>();
		try (InputStream is = Server.class.getResourceAsStream("/characters.json");
				BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			Iterator<JSONObject> iterator = ((JSONArray) new JSONParser().parse(reader)).iterator();
			while (iterator.hasNext()) {
				JSONObject obj = (JSONObject) iterator.next();
				characters.add(new Character(((Number) obj.get("id")).intValue(), obj.get("nickname").toString())
						.setLevel(((Number) obj.get("level")).shortValue())
						.setHealthPoints(((Number) obj.get("hp")).intValue())
						.setPhysicalDamage(((Number) obj.get("physicalDamage")).intValue())
						.setMagicalDamage(((Number) obj.get("magicalDamage")).intValue()));
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		System.out.println(LocalTime.now() + " characters data fetched from file:\n\n" + characters + "\n"); // XXX
	}

	/**
	 * initializes monsters.
	 */
	@SuppressWarnings("unchecked")
	private static void initMonsters() {
		monsters = new ArrayList<>();
		try (InputStream is = Server.class.getResourceAsStream("/monsters.json");
				BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			Iterator<JSONObject> iterator = ((JSONArray) new JSONParser().parse(reader)).iterator();
			while (iterator.hasNext()) {
				JSONObject obj = (JSONObject) iterator.next();
				monsters.add(new Monster().setName(obj.get("name").toString())
						.setHealthPoints(((Number) obj.get("hp")).intValue())
						.setShieldPoints(((Number) obj.get("shield")).intValue())
						.setMagicResist(((Number) obj.get("mr")).intValue())
						.setArmor(((Number) obj.get("armor")).intValue())
						.setDamage(((Number) obj.get("damage")).intValue()));
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		System.out.println(LocalTime.now() + " monsters data fetched from file:\n\n" + monsters + "\n"); // XXX
	}

	/**
	 * Adds a player to the ready queue.
	 * <p>
	 * All players must be ready before starting the session.
	 * 
	 * @param id
	 *            player's id to be added to queue.
	 * @return true is succeeded, false if already in queue.
	 */
	public static boolean addReadyPlayer(int id) {
		log("Trying to add Player ID: "+id);
		playersReady.add(id);
		
		return true;
	}

	/**
	 * Checks whether or not all players are connected and ready
	 * @return
	 */
	public static Boolean isAllReady(){
//		return playersReady.size()==characters.size();
		//TODO CHANGE HERE ITS DEBUG MAN!
		return playersReady.size()==Consts.numOfPlayers;
	}
	
	/**
	 * @return list of monsters in play session.
	 */
	public static List<Monster> getMonsters() {
		return Collections.unmodifiableList(monsters);
	}

	/**
	 * @return list of characters.
	 */
	public static List<Character> getCharacters() {
		return Collections.unmodifiableList(characters);
	}
}
