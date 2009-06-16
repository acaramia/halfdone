import java.util.*;


public class Contenitore {

	private Vector<Particella> tree=new Vector<Particella>();
	
	public Vector<Particella> getTree() {
		return tree;
	}

	public void setTree(Vector<Particella> tree) {
		this.tree = tree;
	}

	public void addParticella(Particella p){
		tree.add(p);
	}
	
}
