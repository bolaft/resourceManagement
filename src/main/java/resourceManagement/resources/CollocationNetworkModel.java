package resourceManagement.resources;

public interface CollocationNetworkModel {	
	public void addPair(String word, String colWord, boolean lookBack);
	public void display();
	public void save(String filename);
}