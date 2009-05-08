package it.color;

public class Punto {
	int x,y;
	int color; 	
	float distmax;	
	
	public Punto(int x, int y, int color,int lx,int ly) {
		this.x=x;
		this.y=y;
		this.color=color;
		this.distmax=-Float.MAX_VALUE;
		this.distmax=Math.max(this.distmax,quadDist( lx, ly));
		this.distmax=Math.max(this.distmax,quadDist(-lx, ly));
		this.distmax=Math.max(this.distmax,quadDist( lx,-ly));
		this.distmax=Math.max(this.distmax,quadDist(-lx,-ly));
		//System.out.println(this);
	}
	
	/**
	 * quadrato della distanza
	 */
	public float quadDist(int x, int y){
		float dx=this.x-x;
		float dy=this.y-y;
		float d=dx*dx+dy*dy;
		return d;
	}

	/**
	 * distanza normalizzata 0-1 
	 * la distanza massima vale 1
	 * la distanza minima  vale 0 
	 */
	public float normDist(int x, int y){
		float d=quadDist(x, y);
		d=d/this.distmax;
		return d;
	}
	
	public String toString(){
		return "["+x+","+y+"];"+color+";"+distmax;
	}
	
}
