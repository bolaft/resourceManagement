package resourceManagement.annotators;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import common.types.Token;

import resourceManagement.resources.CollocationNetworkModel;

public class CollocationNetworkBuilderAnnotator extends JCasAnnotator_ImplBase {
	public final static String RES_KEY = "collocationNetwork";
	public final static String resource_path = "collocation_network.csv";
	
	@ExternalResource(key = RES_KEY)
	private CollocationNetworkModel collocationNetwork;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		String previousWord = null;
		
		for (Annotation token : JCasUtil.select(aJCas, Token.class)) {
			String word = token.getCoveredText().toLowerCase();
			
			if (previousWord != null && word != null){
				collocationNetwork.addPair(previousWord, word);
			}
			
			previousWord = word;
		}
	}
	
	@Override
	public void collectionProcessComplete()  throws AnalysisEngineProcessException {
		collocationNetwork.display();
		collocationNetwork.save(resource_path);
	}
}
