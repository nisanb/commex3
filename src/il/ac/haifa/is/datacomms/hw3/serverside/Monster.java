package il.ac.haifa.is.datacomms.hw3.serverside;

import java.util.HashMap;

/**
 * class representation of a monster in FoA.
 */
public class Monster {
	/** monster's name. */
	private String name;

	/**
	 * monster's health points. if at 0, monster is dead. can only be reduced if
	 * shield points are at 0.
	 */
	private int healthPoints;

	/**
	 * monster's shield points. if at 0, monster's health points can be reduced.
	 * else (above 0), only shield points can be reduced.
	 */
	private int shieldPoints;

	/**
	 * monster's magic attacks resistance. lowers damage taken from magic
	 * attacks by floor(0.1 * magicResist).
	 */
	private int magicResist;

	/**
	 * monster's armor. lowers damage taken from physical attacks by floor(0.1 *
	 * armor)
	 */
	private int armor;

	/** monster's attacks' damage. */
	private int damage;

	/**
	 * map of damage dealers and the amount of damage they've dealt to this
	 * monster.
	 */
	private HashMap<String, Integer> damageReceived;

	public Monster() {
		damageReceived = new HashMap<>();
	}

	/**
	 * deals physical damage to monster.
	 * 
	 * @param damageDealer
	 *            character doing the damage.
	 * @return true if succeeded, false otherwise.
	 */
	public boolean hitWithPhysicalAttack(Character damageDealer) {
		if (damageDealer == null || !damageDealer.isAlive())
			return false;
		return handleAttack(damageDealer.getNickname(), damageDealer.getPhysicalDamage(), armor);
	}

	/**
	 * deals magic damage to monster.
	 * 
	 * @param character
	 *            damage dealer's character name.
	 * @param damage
	 *            damage received (before resistances).
	 * @return true if succeeded, false otherwise (null or dealer is dead, or
	 *         monster is dead).
	 */
	public boolean hitWithMagicAttack(Character damageDealer) {
		if (damageDealer == null || !damageDealer.isAlive())
			return false;
		return handleAttack(damageDealer.getNickname(), damageDealer.getMagicalDamage(), magicResist);
	}

	/**
	 * handles attack on monster.
	 * <p>
	 * resistance reduces damage. damage reduces shield points and health
	 * points. damage added to character's entry.
	 * 
	 * @param nickname
	 *            damage dealer's character name.
	 * @param damage
	 *            damage received (before resistances).
	 * @param resist
	 *            resistance to attack.
	 * @return true if succeeded, false otherwise (monster already dead).
	 */
	private boolean handleAttack(String nickname, int damage, int resist) {
		// TODO
		return true;
	}

	/**
	 * if monster has shield, reduces monster's shield points by given damage
	 * amount. down to a minimum of 0 shield points.
	 * 
	 * @param amount
	 *            amount to be reduced from monster's shield points.
	 * @return non-negative remaining amount. positive if amount was larger than
	 *         previous shield points.
	 */
	private int reduceShieldPoints(int amount) {
		// TODO
		return 0;
	}

	/**
	 * reduces monster's health points by given damage amount. down to a minimum
	 * of 0 health points.
	 * 
	 * @param amount
	 *            amount to be reduced from monster's health points.
	 */
	private void reduceHealthPoints(int amount) {
		// TODO

	}

	/**
	 * prints monster's damage dealers leaderboard.
	 */
	public void printDamageDealers() {
		System.out.println("\n" + name + "'s damage leaderboard:\n\n");
		if (damageReceived.isEmpty())
			System.out.println("N/A");
		else
			damageReceived.entrySet().stream().sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())).forEach(
					entry -> System.out.println("\t" + entry.getKey() + " dealt " + entry.getValue() + " dmg"));
	}

	/**
	 * @return true if alive, false otherwise.
	 * @see Monster#healthPoints
	 */
	public boolean isAlive() {
		// TODO
		return true;
	}

	/**
	 * @return true if shielded, false otherwise.
	 * @see Monster#shieldPoints
	 */
	public boolean isShielded() {
		// TODO
		return true;
	}

	/**
	 * @return monster's name.
	 * @see Monster#name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return monster's health points.
	 * @see Monster#healthPoints
	 */
	public int getHealthPoints() {
		// TODO
		return 0;
	}

	/**
	 * @return monster's shield points.
	 * @see Monster#shieldPoints
	 */
	public int getShieldPoints() {
		// TODO
		return 0;
	}

	/**
	 * @return monster's magic resistance.
	 * @see Monster#magicResist
	 */
	public int getMagicResist() {
		return magicResist;
	}

	/**
	 * @return monster's armor.
	 * @see Monster#armor
	 */
	public int getArmor() {
		return armor;
	}

	/**
	 * @return monster's attacks' damage.
	 * @see Monster#damage
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * @param name
	 *            name to be set.
	 * @return reference to this instance.
	 */
	public Monster setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * @param healthPoints
	 *            health points to be set.
	 * @return reference to this instance.
	 */
	public Monster setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
		return this;
	}

	/**
	 * @param shieldPoints
	 *            shield points to be set.
	 * @return reference to this instance.
	 */
	public Monster setShieldPoints(int shieldPoints) {
		this.shieldPoints = shieldPoints;
		return this;
	}

	/**
	 * @param magicResist
	 *            magic resistance to be set.
	 * @return reference to this instance.
	 */
	public Monster setMagicResist(int magicResist) {
		this.magicResist = magicResist;
		return this;
	}

	/**
	 * @param armor
	 *            armor to be set.
	 * @return reference to this instance.
	 */
	public Monster setArmor(int armor) {
		this.armor = armor;
		return this;
	}

	/**
	 * @param damage
	 *            damage to be set.
	 * @return reference to this instance.
	 */
	public Monster setDamage(int damage) {
		this.damage = damage;
		return this;
	}

	@Override
	public String toString() {
		return String.format(
				"Monster [name=%s, healthPoints=%s, shieldPoints=%s, magicResist=%s, armor=%s, damage=%s]\n", name,
				healthPoints, shieldPoints, magicResist, armor, damage);
	}
}
