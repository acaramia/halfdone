import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;


/*http://www.exampledepot.com/egs/javax.xml.parsers/CreateDom.html?l=rel*/

public class StrutturaAlbero {

	// Parses an XML file and returns a DOM document.
	// If validating is true, the contents is validated against the DTD
	// specified in the file.
	public static Document parseXmlFile(String filename, boolean validating) {
		try {
			// Create a builder factory
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(validating);

			// Create the builder and parse the file
			Document doc = factory.newDocumentBuilder().parse(
					new File(filename));
			return doc;
		} catch (SAXException e) {
			// A parsing error occurred; the xml input is not valid
		} catch (ParserConfigurationException e) {
		} catch (IOException e) {
		}
		return null;
	}

    // This method visits all the nodes in a DOM tree
    public static void visit(Node node, int level) {
        // Process node
        if(node.getNodeName().equalsIgnoreCase("leaf")){
        	//prende il valore diretto
        	//System.out.println(node.getNodeName()+"="+node.getTextContent());	
        	String s="na";
        	if(node.hasAttributes()){
        		s=node.getAttributes().getNamedItem("id").getTextContent();        			
        	}
        	System.out.println(node.getNodeName()+"="+s);
        }
        if(node.getNodeName().equalsIgnoreCase("tree")){
        	String s="na";
        	if(node.hasAttributes()){
        		s=node.getAttributes().getNamedItem("id").getTextContent();        			
        	}
        	System.out.println(node.getNodeName()+"="+s);
        }
    	
        // If there are any children, visit each one
        NodeList list = node.getChildNodes();
        for (int i=0; i<list.getLength(); i++) {
            // Get child node
            Node childNode = list.item(i);    
            // Visit child node
            visit(childNode, level+1);
        }
    }

    // This method visits all the nodes in a DOM tree
    public static void visitTree(Contenitore tree, String root,Node node, int level) {
        // Process node
    	String s=tree.ROOT;
        if(node.getNodeName().equalsIgnoreCase("leaf")){
        	//prende il valore diretto
        	//System.out.println(node.getNodeName()+"="+node.getTextContent());	        	
        	if(node.hasAttributes()){
        		s=node.getAttributes().getNamedItem("id").getTextContent();        			
        	}
        	System.out.println(node.getNodeName()+"="+s);
        	tree.addNewLeaf(root,s);
        }
        if(node.getNodeName().equalsIgnoreCase("tree")){        	
        	if(node.hasAttributes()){
        		s=node.getAttributes().getNamedItem("id").getTextContent();        			
        	}
        	System.out.println(node.getNodeName()+"="+s);
        	tree.addNewLeaf(root,s);        	
        }
    	
        // If there are any children, visit each one
        NodeList list = node.getChildNodes();
        for (int i=0; i<list.getLength(); i++) {
            // Get child node
            Node childNode = list.item(i);    
            // Visit child node
            visitTree(tree,s,childNode,level+1);
        }
    }
    
    public static void popola(Contenitore tree){
		Document doc = parseXmlFile("C:\\Docs\\mysourcegoogle\\arb\\doc\\doc2.xml", false);   
	    visitTree(tree,tree.ROOT,doc,0);	        	
    }
    
	public static void main(String[] args) {
		Document doc = parseXmlFile("c://temp//doc.xml", false);   
	    visit(doc, 0);	    
	}
}
