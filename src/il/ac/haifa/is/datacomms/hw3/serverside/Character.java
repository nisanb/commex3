package il.ac.haifa.is.datacomms.hw3.serverside;

/**
 * class representation of a human controlled character.
 */
public class Character {
	/**character's id.*/
	private final int id;
	
	/**character's nickname.*/
	private final String nickname;
	
	/**character's level.*/
	private short level;
	
	/**character's health points.*/
	private int healthPoints;
	
	/**character's physical damage.*/
	private int physicalDamage;
	
	/**character's magical damage.*/
	private int magicalDamage;
	
	/**bandages left for healing.*/
	private int bandagesLeft;
	
	/**
	 * @param id character's id.
	 * @param nickname character's nickname.
	 */
	public Character(int id, String nickname) {
		this.id = id;
		this.nickname = nickname;
		this.bandagesLeft = 2;
	}
	
	/**
	 * inflicts damage to character.
	 * @param damage damage taken.
	 * @return true if died, false otherwise.
	 */
	public boolean wound(int damage) {
		healthPoints-=damage;
		if(healthPoints<=0){
			healthPoints=0;
			return true;
		}
		return false;
	}
	
	/**
	 * heal character by use of a bandage.
	 * @return true if successful, false otherwise (no bandages left, character already dead).
	 */
	public boolean useBandage() {
		if(this.getBandagesLeft()<=0 || this.getHealthPoints()==0)
			return false;
		
		this.setHealthPoints(getHealthPoints()+25);
		this.bandagesLeft--;
		
		return true;
	}
	
	/**
	 * @return character's id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return character's nickname.
	 */
	public String getNickname() {
		return nickname;
	}
	
	/**
	 * @return character's level.
	 */
	public short getLevel() {
		return level;
	}
	
	/**
	 * @return character's health points.
	 */
	public int getHealthPoints() {
		return healthPoints;
	}
	
	/**
	 * @return true if alive, false otherwise.
	 */
	public boolean isAlive() {
		return (healthPoints != 0);
	}
	
	/**
	 * @return character's physical damage.
	 */
	public int getPhysicalDamage() {
		return physicalDamage;
	}
	
	/**
	 * @return character's magical damage.
	 */
	public int getMagicalDamage() {
		return magicalDamage;
	}
	
	/**
	 * @param level level to be set.
	 * @return reference to this instance.
	 */
	public Character setLevel(short level) {
		this.level = level;
		return this;
	}
	
	/**
	 * @param hp health points to be set.
	 * @return reference to this instance.
	 */
	public Character setHealthPoints(int hp) {
		this.healthPoints = hp;
		return this;
	}
	
	/**
	 * @param physicalDamage physical damage to be set.
	 * @return reference to this instance.
	 */
	public Character setPhysicalDamage(int physicalDamage) {
		this.physicalDamage = physicalDamage;
		return this;
	}
	
	/**
	 * @param magicalDamage magical damage to be set.
	 * @return reference to this instance.
	 */
	public Character setMagicalDamage(int magicalDamage) {
		this.magicalDamage = magicalDamage;
		return this;
	}

	public Integer getBandagesLeft(){
		return this.bandagesLeft;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Character other = (Character) obj;
		if (id!=(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format(
				"Character [id=%s, nickname=%s, level=%s, healthPoints=%s, physicalDamage=%s, magicalDamage=%s]\n", id,
				nickname, level, healthPoints, physicalDamage, magicalDamage);
	}
}
