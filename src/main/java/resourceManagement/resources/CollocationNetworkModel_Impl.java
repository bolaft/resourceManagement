package resourceManagement.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

import resourceManagement.util.MiscUtil;

public class CollocationNetworkModel_Impl implements CollocationNetworkModel, SharedResourceObject {
	private Map<String, Map<String, Double>> collocationNetwork = new HashMap<String, Map<String, Double>>();

	public void addPair(String word, String nextWord, boolean lookBack) {
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
		
		if (lookBack) {
			addPair(nextWord, word, false);
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
		InputStream inStr = null;
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(aData.getInputStream()));
			String line;
			
			while ((line = br.readLine()) != null) {
                String[] columns = line.split("\t");
                
                if (columns.length == 3){
                	if (!collocationNetwork.containsKey(columns[0])){
                    	Map<String, Double> pair = new HashMap<String, Double>();
                        collocationNetwork.put(columns[0], pair);
                	}
                	
                	collocationNetwork.get(columns[0]).put(columns[1], Double.valueOf(columns[2]));
                	
                }
			}
		} catch (IOException e) {
			throw new ResourceInitializationException(e);
		} finally {
			if (inStr != null) {
				try {
					inStr.close();
				} catch (IOException e) {}
			}
		}
	}
	
	public double computeCosine(HashMap<String, Double> word1, HashMap<String, Double> word2) {                

        ArrayList<String> words = new ArrayList<String>(word1.keySet());
        words.addAll(word2.keySet());
        
        Double total = 0.0;
        
        for (String word : words) {
            Double w1count = word1.get(word);
            Double w2count = word2.get(word);
            
            total = total + w1count * w2count;
        }
        
        return total / (Math.sqrt(word1.size()) * Math.sqrt(word2.size()));
	}
}
