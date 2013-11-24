/**
 * 
 */
package corr.resourceManagement.annotators;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import common.types.Token;

import corr.resourceManagement.resources.WordCounterModel;


/**
 * Annotator that builds collocation 
 * 
 */
public class CollocationNetworkBuilderAnnotator extends JCasAnnotator_ImplBase {

	// Size 
	final static Integer WINDOW_SIZE = 2;

	final static String RES_KEY = "aKey";

	@ExternalResource(key = RES_KEY)
	private WordCounterModel wordCounter;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// Prints the instance ID to the console - this proves the same instance
		// of the SharedModel is used in both Annotator instances.
		System.out.println(getClass().getSimpleName() + ": " + wordCounter);

		// Create a token iterator 
		FSIterator<Annotation> tokenAnnotationIterator =  aJCas.getAnnotationIndex(Token.type).iterator();

		// For each token, explore the forward vicinity and store the association from/to the tokens in the vicinity 
		while(tokenAnnotationIterator.isValid()) {
			// TODO to be continued...
			Annotation tokenAnnotation =  (Annotation) tokenAnnotationIterator.get();

			// Exploring the vicinity
			int localStep = 0;
			Boolean pursueExploration = true;
			while (localStep < WINDOW_SIZE && pursueExploration) {
				localStep++;
				tokenAnnotationIterator.moveToNext();
				// Store the association 
				if (tokenAnnotationIterator.isValid()) {

				}
				// We are at the end of the stream
				else pursueExploration = false;
			}
			while (localStep != 0) {tokenAnnotationIterator.moveToPrevious(); localStep--;}

			tokenAnnotationIterator.moveToNext();
		}
	}
}
