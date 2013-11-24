package resourceManagement.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

import resourceManagement.util.MiscUtil;

public class CollocationNetworkModel_Impl implements CollocationNetworkModel, SharedResourceObject {
	private Map<String, Map<String, Double>> collocationNetwork = new HashMap<String, Map<String, Double>>();

	public void addPair(String word, String nextWord) {
		if (collocationNetwork.containsKey(word)){
			Map<String, Double> wordMap = collocationNetwork.get(word);
			
			if (wordMap.containsKey(nextWord)){
				wordMap.put(nextWord, wordMap.get(nextWord) + 1);
			} else {
				wordMap.put(nextWord, 1.0);
			}
		} else {
			Map<String, Double> initMap = new HashMap<String, Double>();
			initMap.put(nextWord, 1.0);
			collocationNetwork.put(word, initMap);
		}
	}
	
	public void display(){
		for (Entry<String, Map<String, Double>> entry : collocationNetwork.entrySet()){
		    for (Entry<String, Double> subEntry : entry.getValue().entrySet()){
			    System.out.println(entry.getKey() + " " + subEntry.getKey() + ": " + subEntry.getValue());
		    }
		}
	}
	
	public synchronized void save(String filename) {
		String csv = "";

		for (Entry<String, Map<String, Double>> entry : collocationNetwork.entrySet()){
		    for (Entry<String, Double> subEntry : entry.getValue().entrySet()){
			    csv += entry.getKey() + "\t" + subEntry.getKey() + "\t" + subEntry.getValue() + "\n";
		    }
		}
		
		MiscUtil.writeToFS(csv, filename);
	}

	public void load(DataResource aData) throws ResourceInitializationException {
		// TODO Auto-generated method stub
	}
}
