package it.color;

//http://processing.org/learning/tutorials/eclipse/

import processing.core.*;

public class Color1 extends PApplet {

	Punto p[]=new Punto[3];
	
	public void setup(){
	  int f=2;
	  size(1280/f, 1024/f, P2D);
	  int lx=width/2;
	  int ly=height/2;
	  p[0]=new Punto(0,ly/2,color(255,0,0),lx,ly);
	  p[1]=new Punto(-lx/2,-ly/2,color(0,255,0),lx,ly);
	  p[2]=new Punto(lx/2,-ly/2,color(0,0,255),lx,ly);	  
	}

	public void draw(){
	  background(0);
	  translate(width/2,height/2);
	  scale(1,-1);
	  int lx=width/2;
	  int ly=height/2;
	  float dm=-Float.MIN_VALUE;
	  for(int x=-lx; x<lx; x++){
	    for(int y=-ly; y<ly; y++){
	    	float d,dtot=0;
	    	int r=0,g=0,b=0;
	    	float rf=0,gf=0,bf=0;
	    	for(int i=0;i<p.length;i++){
	    		d=p[i].normDist(x, y);
	    		d=1-d;
	    		r=p[i].color >> 16 & 0xFF;
	    		g=p[i].color >> 8 & 0xFF ;
	    		b=p[i].color & 0xFF;
	    		r*=d;
	    		g*=d;
	    		b*=d;
	    		rf+=r;
	    		gf+=g;
	    		bf+=b;
	    	}
	        stroke(rf,gf,bf);
	        point(x,y);
	    }
	  }
	  System.out.println(dm);
	}

	void test(){
	  int pink = color(255, 102, 204);
	  loadPixels();
	  for (int i = 0; i < (width*height/2)-width/2; i++) {
	    pixels[i] = pink;
	  }
	  updatePixels();
	}

	public static void main(String args[]) {
		    PApplet.main(new String[] { "--present", "it.color.Color1" });
	}
}