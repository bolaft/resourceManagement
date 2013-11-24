/**
 * 
 */
package corr.resourceManagement.annotators;


import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;

import corr.resourceManagement.resources.WordCounterModel;

/**
 * Annotator that uses the resource object. Simply echo the content of the resource
 */
public class IDFWordFilterAnnotator extends JCasAnnotator_ImplBase {
	public final static String RES_KEY = "anotherKeyOrNot";

	@ExternalResource(key = RES_KEY)
	private WordCounterModel wordCounter;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// Prints the instance ID to the console - this proves the same instance
		// of the SharedModel is used in both Annotator instances.
		System.out.println(getClass().getSimpleName() + ": " + wordCounter);

		for (String word : wordCounter.keySet()) {
			System.out.printf("word >%s< count>%d<\n", word, wordCounter.get(word));
		}
	}

	/**
	 * 
	 * @param docTermCount
	 * @param totalNumDocuments
	 * @return
	 */
	static double idf(int docTermCount, int totalNumDocuments) { 
		return Math.log((double)docTermCount / (double)totalNumDocuments); 
	}

}
