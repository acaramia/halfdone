
public class Area {

	public int x1,y1,x2,y2;
	public String name;

	public Area(int x1, int y1, int x2, int y2,String name) {
		super();
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.name=name;
	}

	public boolean isInside(int x, int y) {
		return ((x>=x1)&&(x<=x1+x2)&&(y>=y1)&&(y<=y1+y2));
	}
	
}
