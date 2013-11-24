/**
 * 
 */
package corr.resourceManagement.annotators;
 
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import common.types.Token;

import corr.resourceManagement.resources.WordCounterModel;


/**
 * Annotator that increments the occurrence counter of word forms
 */
public class WordCounterAnnotator extends JCasAnnotator_ImplBase {
	public final static String RES_KEY = "aKey";
	@ExternalResource(key = RES_KEY)
	private WordCounterModel wordCounter;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// Prints the instance ID to the console - this proves the same instance
		// of the SharedModel is used in both Annotator instances.
		System.out.println(getClass().getSimpleName() + ": " + wordCounter);

		for (Annotation token : JCasUtil.select(aJCas, Token.class)) {
			wordCounter.inc(token.getCoveredText().toLowerCase());
		}
	}
}
