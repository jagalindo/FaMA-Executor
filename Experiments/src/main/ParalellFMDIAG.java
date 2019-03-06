package main;

import java.io.File;
import java.io.IOException;

import es.us.isa.Choco.fmdiag.ChocoExplainErrorFMDIAG;
import es.us.isa.Choco.fmdiag.configuration.ChocoExplainErrorFMDIAGParalell;
import es.us.isa.ChocoReasoner.ChocoReasoner;
import es.us.isa.FAMA.models.FAMAfeatureModel.FAMAFeatureModel;
import es.us.isa.FAMA.models.FAMAfeatureModel.fileformats.XMLReader;
import es.us.isa.FAMA.models.featureModel.Product;
import es.us.isa.FAMA.models.variabilityModel.parsers.WrongFormatException;
import helpers.ProductManager;

public class ParalellFMDIAG {

	public static void main(String[] args) throws WrongFormatException, IOException {
		String op = args[0];// flexdiag - prods - evolutionary

		

		String modelPath = args[1];
		String productPath = args[2];

		if (op.equals("FMDIAG")) {
			Integer t = Integer.parseInt(args[3]);
			if (t == 1) {
				FMDiag(modelPath, productPath);
			} else if (t > 1) {
				PFMDiag(modelPath, productPath, t);
			}

		}

	}

	static void PFMDiag(String modelPath, String productPath, Integer t) throws WrongFormatException {
		XMLReader reader = new XMLReader();
		ProductManager pman = new ProductManager();

		FAMAFeatureModel fm = (FAMAFeatureModel) reader.parseFile(modelPath);
		Product prod = pman.readProduct(fm, productPath);

		ChocoReasoner r = new ChocoReasoner();
		fm.transformTo(r);

		ChocoExplainErrorFMDIAGParalell flexdiagP = new ChocoExplainErrorFMDIAGParalell(1, t);
		flexdiagP.flexactive = false;
		flexdiagP.m = 1;

		flexdiagP.setConfiguration(prod);
		flexdiagP.setRequirement(new Product());

		long start = System.currentTimeMillis();
		r.ask(flexdiagP);
		long end = System.currentTimeMillis();

		System.out.println(modelPath.substring(modelPath.lastIndexOf(File.separator) + 1) + "|"
				+ productPath.substring(productPath.lastIndexOf(File.separator) + 1) + "|" + prod + "|" +  t
				+ "|" + fm.getFeaturesNumber() + "|" + fm.getNumberOfDependencies() + "|" + r.getVariables().size()
				+ "|" + r.getRelations().size() + "|" + start + "|" + end + "|" + flexdiagP.result.keySet());

	}

	static void FMDiag(String modelPath, String productPath) throws WrongFormatException {
		XMLReader reader = new XMLReader();
		ProductManager pman = new ProductManager();

		FAMAFeatureModel fm = (FAMAFeatureModel) reader.parseFile(modelPath);
		Product prod = pman.readProduct(fm, productPath);

		ChocoReasoner reasoner = new ChocoReasoner();
		fm.transformTo(reasoner);

		ChocoExplainErrorFMDIAG fmdiag = new ChocoExplainErrorFMDIAG();
		fmdiag.setConfiguration(prod);
		fmdiag.setRequirement(new Product());
		fmdiag.flexactive = false;
		fmdiag.m = 1;
		long start = System.currentTimeMillis();
		reasoner.ask(fmdiag);
		long end = System.currentTimeMillis();

//		System.out.println("Prod Size: " + prod.getNumberOfFeatures());

		System.out.println(modelPath.substring(modelPath.lastIndexOf(File.separator) + 1) + "|"
				+ productPath.substring(productPath.lastIndexOf(File.separator) + 1) + "|" + prod + "|" + 1 + "|"
				+ fm.getFeaturesNumber() + "|" + fm.getNumberOfDependencies() + "|" + reasoner.getVariables().size()
				+ "|" + reasoner.getRelations().size() + "|" + start + "|" + end + "|" + fmdiag.result.keySet());
	}
}