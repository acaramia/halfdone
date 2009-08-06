import java.util.Vector;

public class Contratti {

	private Vector<Contratto> set=new Vector<Contratto>();
	private int max=0;
	private int massimaAltezza=0;  // massima altezza rettangoli
	private int sommaLarghezze=0; // somma delle larghezze rettangoli
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
	
	public void addContratto(int max, int min, int prop,String name){
		Contratto c=new Contratto(max,min,prop,name);
		set.add(c);
		if(max>massimaAltezza)
			massimaAltezza=max;
		sommaLarghezze+=prop+spaziaturaRettangoli;
	}

	public Contratto getContrattoByName(String name){
		for(int i=0;i<set.size();i++){
			if(set.elementAt(i).getName().equals(name)){
				return set.elementAt(i);
			}
		}
		return null;
	}
	
	public void setMax(int i) {
		max=i;		
	}

	public void addContratto(int prop,String name) {
		this.addContratto(this.max,0,prop,name);		
	}
	public int getMassimaAltezza() {
		return massimaAltezza;
	}

	public int getSommaLarghezze() {
		return sommaLarghezze;
	}

	public int getSpaziaturaRettangoli() {
		return spaziaturaRettangoli;
	}
}
