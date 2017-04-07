//Representation of a checkers piece for sparse representation
//tuple of type and location

public class SparsePiece<type,location> {
	    private type t;
	    private location l;
	    public SparsePiece(type t, location l){
	        this.t = t;
	        this.l = l;
	    }
	    public type getType(){
	    	return t; 
	    	}
	    public location getLocation(){ 
	    	return l; 
	    	}
	    public void setType(type t){ 
	    	this.t = t; 
	    	}
	    public void setLocation(location l){ 
	    	this.l = l; 
	    	}
	    public String toString() {
	    	return t.toString() + l.toString();
	    }
	    
	}