package resourceManagement.annotators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import common.types.Token;
import resourceManagement.resources.CollocationNetworkModel;

public class CollocationNetworkBuilderAnnotator extends JCasAnnotator_ImplBase {
	public final static int WINDOW = 2;
	public final static String RES_KEY = "collocationNetwork";
	public final static String SAVE_PATH = "collocation_network.csv";
	public final static String LOAD_PATH = "collocation_network_store.csv";
	
	@ExternalResource(key = RES_KEY)
	private CollocationNetworkModel collocationNetwork;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		ArrayList<Token> tokens = new ArrayList<Token>(JCasUtil.select(aJCas, Token.class));
		
		for (int i = 0; i < tokens.size(); i++){
			String head = tokens.get(i).getCoveredText().toLowerCase();
			
			for (int j = 0; j < WINDOW && i+j < tokens.size(); j++) {
				String token = tokens.get(i+j).getCoveredText().toLowerCase();
				
				collocationNetwork.addPair(head, token, true);
			}
		}
	}
	
	@Override
	public void collectionProcessComplete()  throws AnalysisEngineProcessException {
		collocationNetwork.display();
		collocationNetwork.save(SAVE_PATH);
	}
}
