/**
 * 
 */
package corr.resourceManagement.resources;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

import resourceManagement.util.MiscUtil;

/**
 * Implementation of the resource model for Word Counters
 * No resource actually loaded 
 */
public final class WordCounterModel_Impl implements WordCounterModel, SharedResourceObject {
	private Map<String,Integer> wordCounterMap;

	final static private int initialValue = 0;
	final static private int incrementValue = 1;

	private Boolean isSaved = false;

	private synchronized  Map<String, Integer> getWordCounter() {
		return this.wordCounterMap;
	} 

	private synchronized void put(String key, Integer value) {
		getWordCounter().put(key, value);
	} 

	public synchronized Integer get(String key) {
		if (!getWordCounter().containsKey(key)) put(key, initialValue);
		return getWordCounter().get(key);
	}

	public synchronized void inc (String key) {
		put(key, get(key) + incrementValue);
	} 

	public synchronized void dec (String key) {
		put(key, getWordCounter().get(key) - incrementValue);
	} 

	public synchronized Integer size() {
		return getWordCounter().size();
	}

	public synchronized Set<String> keySet() {
		return getWordCounter().keySet();
	}

	/**DONE */
	public synchronized Integer getTotalCounter() {
		int total = 0;
		for (String key : keySet())
			total += get(key);
		return total;
	}

	/**/
	public synchronized void echo() {
		for (String word : keySet()) {
			System.out.printf("word >%s< count>%d<\n", word, get(word));
		}
		/**DONE*/  
		System.out.printf("sum of all word counters>%d<\n", getTotalCounter());
	}

	/***/
	public void load(DataResource aData) throws ResourceInitializationException {
		System.out.println(getClass().getSimpleName()+": load +1");
		if (wordCounterMap == null) {
			wordCounterMap = new HashMap<String,Integer>();
		}
	}

	/**
	 * Save the content of the resource as a CSV file
	 * a line per word, word and counter as columns with tab character as separator
	 * use the MiscUtil.writeToFS(textString,filenameString) 
	 * DONE */
	public synchronized void save(String filename){

		if (!isSaved) {
			String content = "";
			String columnSeparator = "\t";

			for (String word : keySet()) {
				content += word +columnSeparator+ get(word) + System.getProperty("line.separator");
			}
			MiscUtil.writeToFS(content,filename);
			isSaved = true;
		}
	}



}
