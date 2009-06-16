import java.util.*;
import traer.physics.Particle;

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
	
	public void init(arb a){
		Particle p=a.addNode(a.getRoot());
//		Particella pc=new Particella();
//		pc.setP(p);
//		pc.setId("0");
//		pc.setValue("root");
//		this.addParticella(pc);
	}

}
