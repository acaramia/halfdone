
public class E247b {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
/*
void setup() {
 size(700, 700);
 noLoop();
}

void draw() {
 float a=1;
 float b=0;
 background(0,0,0);
 float s=width/4;
 translate(0,height);
 scale(s,-s);
 stroke(255,255,255);
 strokeWeight(1/s);
 //noStroke();
 fill(0,0,255);
 //noFill();

 //rect(1,0,1,1);
 //rect(10,10,10,10);

 quad(a,b,12);
}

void quad(float a,float b,int lev){
 if (lev>0){
     float w=b-a;
     float lato1=(-w+(float)Math.sqrt(w*w+4))/2;
     float lato2=(-w-(float)Math.sqrt(w*w+4))/2;
     float lato=lato1;
     if(lato<0) lato=lato2;
     lato=lato-a;

     rect(a-1,b,lato,lato);
     System.out.println(lato);

     quad(a+lato,b,lev-1);//dx
     quad(a,b+lato,lev-1);//top
 }
} */