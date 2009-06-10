package E247;

public class E247 {

	public static double lato(double a,double b){
		double w=b-a;
		double l1=(-w+Math.sqrt(w*w+4))/2;
		double l2=(-w-Math.sqrt(w*w+4))/2;
		return l1;		
	}
	
	public static void giro(double a,double b,int lev){
		double l=lato(a,b);
		double lato=l-a;
		System.out.println(lev+" "+a+" "+b+" "+lato);
		if(lev>0){			
			giro(l,b,lev-1);
			giro(a,l,lev-1);
		}
	}
	
	public static void test1(){
		int nq=100;
		double a=1,b=0,l;
		for(int n=1;n<15;n++){
			l=lato(a,b);
			System.out.println(n+" "+(l-a));
			a=l;
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		giro(1,0,5);
	}

}
