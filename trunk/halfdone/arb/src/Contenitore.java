import java.util.*;
import traer.physics.Particle;

public class Contenitore {
	
	public static final String ROOT ="0";
	private arb a;

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
		this.a=a;
		//aggiunge la root		
		Particle p=this.a.getRoot();
		Particella pc=new Particella();
		pc.setP(p);
		pc.setId(ROOT);
		pc.setValue(ROOT);
		this.addParticella(pc);

		StrutturaAlbero.popola(this);
	}

	public void addNewLeaf(String root, String nodo) {
		for(int i=0;i<tree.size();i++){
			if(tree.elementAt(i).getId().equalsIgnoreCase(root)){
				Particle p=this.a.addNode(tree.elementAt(i).getP());
				Particella pc=new Particella();
				pc.setP(p);
				pc.setId(nodo);
				pc.setValue(root+":"+nodo);
				this.addParticella(pc);				
			}				
		}		
	}

}
