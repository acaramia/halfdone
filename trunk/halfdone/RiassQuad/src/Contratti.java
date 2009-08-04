import java.util.Vector;


public class Contratti {

	private Vector<Contratto> set=new Vector<Contratto>();
	private int max=0;

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
	}

	public void setMax(int i) {
		max=i;		
	}

	public void addContratto(int prop) {
		this.addContratto(this.max,0,prop);		
	}
	
}
