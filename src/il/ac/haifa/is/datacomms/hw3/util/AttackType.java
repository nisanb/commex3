package il.ac.haifa.is.datacomms.hw3.util;

public enum AttackType {
	PHY("PHY"),
	MAG("MAG");
	
	private String name;
	
	AttackType(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
