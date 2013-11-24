/**
 * 
 */
package corr.resourceManagement.annotators;


import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;

import corr.resourceManagement.resources.WordCounterModel;



/**
 * Annotator that uses the resource object. Simply echo the content of the resource
 */
public class WordCounterConsumerAnnotator extends JCasAnnotator_ImplBase {
	public final static String RES_KEY = "anotherKeyOrNot";
	@ExternalResource(key = RES_KEY)
	private WordCounterModel wordCounter;

	public static final String PARAM_RESOURCE_DEST_FILE = "resourceDestFilename";
	@ConfigurationParameter(name = PARAM_RESOURCE_DEST_FILE, mandatory = true, defaultValue="/tmp/wordcounter.csv")
	private String resourceDestFilename;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
	}

	@Override
	public void collectionProcessComplete()  throws AnalysisEngineProcessException {
		// Prints the instance ID to the console - this proves the same instance
		// of the SharedModel is used in both Annotator instances.
		System.out.println(getClass().getSimpleName() + ": " + wordCounter);
		wordCounter.echo(); 
		wordCounter.save(resourceDestFilename);
	}
}
