package main;

import java.io.File;
import java.io.IOException;

import es.us.isa.Choco.fmdiag.ChocoPFMDIAGNoRecursive;
import es.us.isa.Choco.fmdiag.configuration.ChocoExplainErrorFMDIAG;
import es.us.isa.Choco.fmdiag.configuration.ChocoExplainErrorFMDIAGParalell;
import es.us.isa.ChocoReasoner.ChocoReasoner;
import es.us.isa.FAMA.models.FAMAfeatureModel.FAMAFeatureModel;
import es.us.isa.FAMA.models.FAMAfeatureModel.fileformats.XMLReader;
import es.us.isa.FAMA.models.featureModel.Product;
import es.us.isa.FAMA.models.variabilityModel.parsers.WrongFormatException;
import es.us.isa.Sat4j.fmdiag.SATPFMDIAGNoRecursive;
import es.us.isa.Sat4j.fmdiag.Sat4jExplainErrorFMDIAG;
import es.us.isa.Sat4j.fmdiag.Sat4jExplainErrorFMDIAGParalell;
import es.us.isa.Sat4jReasoner.Sat4jReasoner;
import helpers.ProductManager;

public class ParalellFMDIAG {

	public static void main(String[] args) throws WrongFormatException, IOException {
		String op = args[0];// flexdiag - prods - evolutionary
		String modelPath = args[1];
		String productPath = args[2];

		if (op.equals("FMDIAG")) {
			Integer t = Integer.parseInt(args[3]);
			if (t == 1) {
				SATFMDiag(modelPath, productPath);
			} else if (t > 1) {
				SATPFMDiag(modelPath, productPath, t);
			}
		}
		
//		long time1=ChocoFMDiag(modelPath, productPath);
//		long time2=ChocoPFMDiag(modelPath, productPath, 2);
//		long time3=ChocoPFMDiag(modelPath, productPath, 4);
//		long time4=ChocoPFMDiag(modelPath, productPath, 8);
//
//		
//		long time5=SATFMDiag(modelPath, productPath);
//		long time6=SATPFMDiag(modelPath, productPath, 2);
//		long time7=SATPFMDiag(modelPath, productPath, 4);
//		long time8=SATPFMDiag(modelPath, productPath, 8);
//		
//		System.out.println(time1+";"+time2+";"+time3+";"+time4);
//		System.out.println(time5+";"+time6+";"+time7+";"+time8 );
	}

	
	
	
	
	
	static long SATPFMDiag(String modelPath, String productPath, Integer t) throws WrongFormatException {
		XMLReader reader = new XMLReader();
		ProductManager pman = new ProductManager();

		FAMAFeatureModel fm = (FAMAFeatureModel) reader.parseFile(modelPath);
		Product prod = pman.readProduct(fm, productPath);

		Sat4jReasoner r = new Sat4jReasoner();
		fm.transformTo(r);

		//Sat4jExplainErrorFMDIAGParalell flexdiagP = new Sat4jExplainErrorFMDIAGParalell(1, t);
		SATPFMDIAGNoRecursive flexdiagP = new SATPFMDIAGNoRecursive(t);
		flexdiagP.flexactive = false;
		flexdiagP.m = 1;

		flexdiagP.setConfiguration(prod);
		flexdiagP.setRequirement(new Product());

		long start = System.currentTimeMillis();
		r.ask(flexdiagP);
		long end = System.currentTimeMillis();

		System.out.println(modelPath.substring(modelPath.lastIndexOf(File.separator) + 1) + "|"
				+ productPath.substring(productPath.lastIndexOf(File.separator) + 1) + "|" + prod + "|" + t + "|"
				+ fm.getFeaturesNumber() + "|" + fm.getNumberOfDependencies() + "|" + r.getVariables().size() + "|"
				+ r.getClauses().size() + "|" + start + "|" + end + "|" + flexdiagP.result.keySet());
		
		return end-start;

	}
	
	
	static long SATFMDiag(String modelPath, String productPath) throws WrongFormatException {
		XMLReader reader = new XMLReader();
		ProductManager pman = new ProductManager();

		FAMAFeatureModel fm = (FAMAFeatureModel) reader.parseFile(modelPath);
		Product prod = pman.readProduct(fm, productPath);

		Sat4jReasoner r = new Sat4jReasoner();
		fm.transformTo(r);

		Sat4jExplainErrorFMDIAG flexdiagP = new Sat4jExplainErrorFMDIAG();


		flexdiagP.setConfiguration(prod);
		flexdiagP.setRequirement(new Product());

		long start = System.currentTimeMillis();
		r.ask(flexdiagP);
		long end = System.currentTimeMillis();

		System.out.println(modelPath.substring(modelPath.lastIndexOf(File.separator) + 1) + "|"
				+ productPath.substring(productPath.lastIndexOf(File.separator) + 1) + "|" + prod + "|" + 1 + "|"
				+ fm.getFeaturesNumber() + "|" + fm.getNumberOfDependencies() + "|" + r.getVariables().size() + "|"
				+ r.getClauses().size() + "|" + start + "|" + end + "|" + flexdiagP.explanations);

		return end-start;

	}
	

	
	static long ChocoPFMDiag(String modelPath, String productPath, Integer t) throws WrongFormatException {
		XMLReader reader = new XMLReader();
		ProductManager pman = new ProductManager();

		FAMAFeatureModel fm = (FAMAFeatureModel) reader.parseFile(modelPath);
		Product prod = pman.readProduct(fm, productPath);

		ChocoReasoner r = new ChocoReasoner();
		fm.transformTo(r);

		//ChocoExplainErrorFMDIAGParalell flexdiagP = new ChocoExplainErrorFMDIAGParalell(1, t);
		ChocoPFMDIAGNoRecursive flexdiagP= new ChocoPFMDIAGNoRecursive(t);
		flexdiagP.flexactive = false;
		flexdiagP.m = 1;

		flexdiagP.setConfiguration(prod);
		flexdiagP.setRequirement(new Product());

		long start = System.currentTimeMillis();
		r.ask(flexdiagP);
		long end = System.currentTimeMillis();

		System.out.println(modelPath.substring(modelPath.lastIndexOf(File.separator) + 1) + "|"
				+ productPath.substring(productPath.lastIndexOf(File.separator) + 1) + "|" + prod + "|" + t + "|"
				+ fm.getFeaturesNumber() + "|" + fm.getNumberOfDependencies() + "|" + r.getVariables().size() + "|"
				+ r.getPConstraintsNumber() + "|" + start + "|" + end + "|" + flexdiagP.result.keySet());
		return end-start;

	}
	
	
	static long ChocoFMDiag(String modelPath, String productPath) throws WrongFormatException {
		XMLReader reader = new XMLReader();
		ProductManager pman = new ProductManager();

		FAMAFeatureModel fm = (FAMAFeatureModel) reader.parseFile(modelPath);
		Product prod = pman.readProduct(fm, productPath);

		ChocoReasoner r = new ChocoReasoner();
		fm.transformTo(r);

		ChocoExplainErrorFMDIAG flexdiagP = new ChocoExplainErrorFMDIAG();


		flexdiagP.setConfiguration(prod);
		flexdiagP.setRequirement(new Product());

		long start = System.currentTimeMillis();
		r.ask(flexdiagP);
		long end = System.currentTimeMillis();

		System.out.println(modelPath.substring(modelPath.lastIndexOf(File.separator) + 1) + "|"
				+ productPath.substring(productPath.lastIndexOf(File.separator) + 1) + "|" + prod + "|" + 1 + "|"
				+ fm.getFeaturesNumber() + "|" + fm.getNumberOfDependencies() + "|" + r.getVariables().size() + "|"
				+ r.getPConstraintsNumber() + "|" + start + "|" + end + "|" + flexdiagP.result.keySet());
		return end-start;

	}

}