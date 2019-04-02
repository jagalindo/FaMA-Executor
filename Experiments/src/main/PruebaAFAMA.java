package main;

import es.us.isa.ChocoReasoner.attributed.ChocoReasoner;
import es.us.isa.ChocoReasoner.attributed.questions.ChocoValidQuestion;
import es.us.isa.FAMA.models.FAMAAttributedfeatureModel.FAMAAttributedFeatureModel;
import es.us.isa.FAMA.models.FAMAAttributedfeatureModel.fileformats.AttributedReader;

public class PruebaAFAMA {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		AttributedReader reader = new AttributedReader();
		FAMAAttributedFeatureModel parseFile = (FAMAAttributedFeatureModel) reader.parseFile("./model.afm");
		
		
		ChocoReasoner reasoner = new ChocoReasoner();
		parseFile.transformTo(reasoner);
		
		ChocoValidQuestion cvq= new ChocoValidQuestion();
		
		reasoner.ask(cvq);
		System.out.println(cvq.isValid());
	}

}
