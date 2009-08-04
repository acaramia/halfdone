
public class Contratto {

	private int min=0;
	private int max=0;
	private int prop=0;
	
	public Contratto(int max, int min, int prop) {
		super();
		this.max = max;
		this.min = min;
		this.prop = prop;
	}

	public String toString(){
		return prop+"% da "+min+" a "+max;
	}
	
	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getProp() {
		return prop;
	}

	public void setProp(int prop) {
		this.prop = prop;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
