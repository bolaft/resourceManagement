package resourceManagement.resources;

public interface CollocationNetworkModel {	
	public void addPair(String word, String nextWord);
	public void display();
	public void save(String filename);
}