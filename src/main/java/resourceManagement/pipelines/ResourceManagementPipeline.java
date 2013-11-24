/**
 * 
 */
package resourceManagement.pipelines;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;

import java.io.File;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ExternalResourceDescription;

import resourceManagement.annotators.CollocationNetworkBuilderAnnotator;
import resourceManagement.annotators.WordCounterAnnotator;
import resourceManagement.annotators.WordCounterConsumerAnnotator;
import resourceManagement.annotators.WordSegmenterAnnotator;
import resourceManagement.resources.CollocationNetworkModel_Impl;
import resourceManagement.resources.StopWordModel_Impl;
import resourceManagement.resources.WordCounterModel_Impl;


/**
 * Illustrate how to configure and run annotators with the shared model object.
 */
public class ResourceManagementPipeline {
	
	//http://www.ubuntu-fr.org/ubuntu/philosophie
	final private static String ubuntufrphilosophie = "Notre travail sur Ubuntu est inspiré par une philosophie de la liberté des logiciels qui, espérons nous, s'épandra et apportera les bénéfices de la technologie informatique de par le monde."
+"Logiciel libre et gratuit "
+"Ubuntu est un projet communautaire de création d'un système d'exploitation et d'un ensemble complet de programmes informatiques utilisant des logiciels libres et gratuits. Au coeur de la Philosophie d'Ubuntu sur la Liberté Logicielle se trouvent ces idéaux philosophiques : "
+"Tout utilisateur d'ordinateur devrait avoir la liberté d'exécuter, de copier, de distribuer, d'étudier, de partager, de modifier et d'améliorer ses logiciels pour toutes fins, sans payer aucun frais de licence. "
+"Tout utilisateur d'ordinateur devrait pouvoir utiliser ses logiciels dans la langue de son choix. "
+"Tout utilisateur d'ordinateur devrait être en mesure d'utiliser tous ses logiciels, même s'il souffre d'un handicap. "
+"Notre philosophie est reflétée dans les programmes que nous produisons et incluons dans notre distribution. De ce fait, les termes des licences des logiciels que nous distribuons sont en accord avec notre philosophie, selon la Politique de Licence d'Ubuntu. "
+"Quand vous installez Ubuntu, presque tous les programmes installés respectent déjà ces idéaux, et nous travaillons dans le but que chacune des pièces composant Ubuntu dont vous avez besoin soit disponible sous une licence qui vous assure ces libertés. Actuellement, nous appliquons une exception bien spécifique pour quelques pilotes qui ne sont disponibles uniquement que sous forme binaire, sans quoi de nombreux ordinateurs ne sauraient compléter correctement le processus d'installation d'Ubuntu. Nous les plaçons dans une section restreinte de votre système, ce qui les rend trivials à supprimer si vous n'en avez pas besoin. "
+"Pour plus d'informations à propos des composants d'Ubuntu, veuillez visiter Components. "
+"Logiciel libre "
+"La notion la plus importante propos d'Ubuntu n'est pas qu'elle est distribuée gratuitement, même si nous nous engageons ne jamais charger le moindre centime pour Ubuntu. C'est plutôt qu'elle confère le droit aux libertés logicielles aux personnes installant et utilisant Ubuntu. Ce sont ces libertés qui permettent la communauté d'Ubuntu de s'agrandir, d'échanger son expérience collective et son expertise pour améliorer Ubuntu et la rendre utilisable dans de nouveaux pays et de nouvelles industries. "
+"Tirés de What is Free Software de la Free Software Foundation, les libertés la base du concept des logiciels libres sont définies ainsi : "
+"La liberté d'exécuter un programme, pour quelque fin que ce soit. "
+"La liberté d'étudier le fonctionnement du programme et de l'adapter ses propres besoins. "
+"La liberté de redistribuer des copies d'un programme, pour que vous aidiez votre prochain. "
+"La liberté d'améliorer un programme et de publier vos améliorations au public, afin d'en faire bénéficier tout le monde. "
+"Le Logiciel Libre a été un mouvement social cohérent depuis plus de deux décennies. Ce mouvement a produit des millions de lignes de code et de documentation, ainsi qu'une communauté vivante, active laquelle Ubuntu est fière de faire partie. "
+"Logiciel code ouvert "
+"Le terme code ouvert (open source) est apparu en 1998 afin de supprimer l'ambiguïté entourant le mot anglais free (qui signifie aussi bien gratuit que libre). L'Open Source Initiative décrit le logiciel code source ouvert dans sa Définition du Logiciel Code Ouvert. Le mouvement du code ouvert continue de jouir d'un succès grandissant et d'une reconnaissance mondiale. "
+"Ubuntu est heureuse se qualifier code ouvert. Alors que certains préfèrent voir le code ouvert et le logiciel libre comme deux mouvements en compétition et buts différents, nous nous ne voyons pas ces mouvements comme étant distincts ou incompatibles. Ubuntu comprend fièrement des membres s'identifiant autant au mouvement du logiciel libre que du mouvement du code ouvert, ainsi que de nombreux s'attachant aux deux camps."; 
	
	
	public static void main(String[] args) throws Exception {
		// Creation of the external resource description
		ExternalResourceDescription stopWordsResourceDesc = createExternalResourceDescription(
				StopWordModel_Impl.class,
				"file:resourceManagement/data/stopwords-en.txt"
		);
		
		ExternalResourceDescription wordCounterResourceDesc = createExternalResourceDescription(
				WordCounterModel_Impl.class, 
				new File("pipo.bin")
		);
		
		ExternalResourceDescription collocationNetworkResourceDesc = createExternalResourceDescription(
				CollocationNetworkModel_Impl.class, 
				"file:" + CollocationNetworkBuilderAnnotator.LOAD_PATH
		);
		
		// Binding external resource to each Annotator individually
		AnalysisEngineDescription aed0 = createEngineDescription(
				WordSegmenterAnnotator.class, 
				WordSegmenterAnnotator.RES_KEY, 	
				stopWordsResourceDesc
		);
		
		AnalysisEngineDescription aed1 = createEngineDescription(
				WordCounterAnnotator.class, 
				WordCounterAnnotator.RES_KEY, 
				wordCounterResourceDesc
		);
		
		AnalysisEngineDescription aed2 = createEngineDescription(
				WordCounterConsumerAnnotator.class, 
				WordCounterConsumerAnnotator.RES_KEY, 
				wordCounterResourceDesc
		);
		
		AnalysisEngineDescription aed3 = createEngineDescription(
				CollocationNetworkBuilderAnnotator.class,
				CollocationNetworkBuilderAnnotator.RES_KEY,
				collocationNetworkResourceDesc
		);
		
		// Check the external resource was injected
		AnalysisEngineDescription aaed = createEngineDescription(aed0, aed1, aed2, aed3);
		AnalysisEngine ae = createEngine(aaed);

		// Creation of the jcas to process
		JCas jcas = ae.newJCas();
		jcas.setDocumentText(ubuntufrphilosophie);

		// Run the pipeline
		SimplePipeline.runPipeline(jcas, aaed);
	}
    
    
}
