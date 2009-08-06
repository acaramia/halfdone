import java.util.Vector;


public class Contratti {

	private Vector<Contratto> set=new Vector<Contratto>();
	private int max=0;
	private int massimaAltezza=0;  // massima altezza rettangoli
	private int sommaLaghezze=0; // somma delle larghezze rettangoli
	private int spaziaturaRettangoli=5;  // distanza minima per visualizzazione in basso

	public Contratti() {
		super();
	}

	public Vector<Contratto> getSet() {
		return set;
	}

	public void setSet(Vector<Contratto> set) {
		this.set = set;
	}
	
	public void addContratto(int max, int min, int prop){
		Contratto c=new Contratto(max,min,prop);
		set.add(c);
		if(max>massimaAltezza)
			massimaAltezza=max;
		sommaLaghezze+=prop+spaziaturaRettangoli;
	}

	public void setMax(int i) {
		max=i;		
	}

	public void addContratto(int prop) {
		this.addContratto(this.max,0,prop);		
	}
	public int getMassimaAltezza() {
		return massimaAltezza;
	}

	public int getSommaLaghezze() {
		return sommaLaghezze;
	}

	public int getSpaziaturaRettangoli() {
		return spaziaturaRettangoli;
	}
	
}
