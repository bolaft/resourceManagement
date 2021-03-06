/**
 * 
 */
package corr.resourceManagement.annotators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;

import common.types.Token;

import corr.resourceManagement.resources.StopWordModel;


/**
 * Annotator that segments the text into words and filter the one present 
 * in a stop word set
 */
public class WordSegmenterAnnotator extends JCasAnnotator_ImplBase {
	final static String WORD_SEPARATOR_PATTERN = "[^\\s\\.:]+";
	public final static String RES_KEY = "aKey";

	@ExternalResource(key = RES_KEY)
	private StopWordModel stopWords;
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// Prints the instance ID to the console - this proves the same instance
		// of the SharedModel is used in both Annotator instances.
		System.out.println(getClass().getSimpleName() + ": " + stopWords);

		Pattern wordSeparatorPattern = Pattern.compile(WORD_SEPARATOR_PATTERN);
		Matcher matcher = wordSeparatorPattern.matcher(aJCas.getDocumentText());
		while (matcher.find()) {
			if (!stopWords.contains(matcher.group().toLowerCase()))
				new Token(aJCas, matcher.start(), matcher.end()).addToIndexes(); 
		}
	}
}
