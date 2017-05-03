import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


public class CVCreator {
	private ExcelParser ep;
	public String tableur[][];


	public CVCreator(ExcelParser ep_){
		this.ep = ep_;
	}
	
	
	public void createCVData(int nombreOffre, int nombreCVParOffre, long seed){
		//creation de la matrice
		tableur = new String[(nombreOffre * nombreCVParOffre) + 1][ep.getMaxLineLength()];
		//initialisation de la premiere ligne.
		for(int i = 0; i < ep.getMaxLineLength(); i++){
			tableur[0][i] = ep.def[0][i];
		}
		for(int i = 0; i < nombreOffre; i++){
			
			//on utilise la seed donnée + le numéro d'annonce pour changer le mélange à chaque annonce
			MatrixManipulation.shuffleSourceData(ep.links, ep.def,ep.getMaxColumnLength(), seed+i);
			System.out.println();
			System.out.println(i);
			for(int v = 0; v < 5;v++){

				System.out.print("||");
				for(int j = 0; j < 5; j++){
					System.out.print(ep.def[v][j]+"||");
				}
				System.out.println();
			}
			for(int j = 1; j <= nombreCVParOffre; j++){
				for(int k = 0; k < ep.getMaxLineLength(); k++){
					tableur[i*nombreCVParOffre + j][k] = ep.def[j][k].toString();
				}
			}
		}
		
	}
	
	
	/** Fonction pour creer les CVs 
	 * @throws IOException **/
	public void createCV(int numeroAnnonce, int nb, String templatePath, String outputFolder) throws IOException{
	    
	   	System.out.println("Creation CV "+(nb));
	   	
	   	/*pour faire simple on remplace "telephone" par "telephone : 0632548654" au lieu de rajouter le numéro ÃƒÂ  la suite*/
		//String tel = "Téléphone : "+tableur[nb][3]; // création de la ligne telephone
		//String mail = "Mail : "+tableur[nb][4]; //creation de la lignee mail
		
		FileInputStream fis = new FileInputStream(templatePath);
		POIFSFileSystem fs = new POIFSFileSystem(fis);
		HWPFDocument doc = new HWPFDocument(fs);
		
		
		Range r1 = doc.getRange();
	
		for ( int i = 0; i < r1.numSections(); ++i ) { 
			 Section s = r1.getSection(i); 
			 for (int x = 0; x < s.numParagraphs(); x++) { 
				 Paragraph p = s.getParagraph(x); 
				 for (int z = 0; z < p.numCharacterRuns(); z++){ 
					 //character run 
					 CharacterRun run = p.getCharacterRun(z); 
					 //character run text 
					 //String text = run.text(); 
					 //System.out.println(text.toString());
					 for(i=0;i < ep.getMaxLineLength(); i++){
						 String txt = tableur[0][i];
						 txt = "{{"+txt+"}}";
						 String newtxt = tableur[nb][i];
						 
						 run.replaceText(txt, newtxt);
						 
					 }
				 }
			 } 
		 } 
		
		
		
		//outputFileName = path/name.doc
		String outputFileName = createOutputPath(numeroAnnonce,outputFolder,templatePath,tableur[nb][1],tableur[nb][0]);
		
		//creation des sous dossiers output
		if(outputFileName.contains("\\"))
			new File(outputFileName.substring(0, outputFileName.lastIndexOf('\\'))).mkdirs();
		if(outputFileName.contains("/"))
			new File(outputFileName.substring(0, outputFileName.lastIndexOf('/'))).mkdirs();
		
		//System.out.println(doc.getDocumentText());
		doc.write(new File(outputFileName));
		 
		doc.close(); 
		createLM(numeroAnnonce,nb,templatePath,outputFolder);
		
	}
	
	/** Fonction pour creer les LMs 
	 * TODO changer chemin des LM
	 * 
	 * @throws IOException **/
	public void createLM(int numeroAnnonce, int nb, String templatePath, String outputFolder) throws IOException{
	    
	   	System.out.println("Creation CV "+(nb));
	   	
	   	/*pour faire simple on remplace "telephone" par "telephone : 0632548654" au lieu de rajouter le numéro ÃƒÂ  la suite*/
		//String tel = "Téléphone : "+tableur[nb][3]; // création de la ligne telephone
		//String mail = "Mail : "+tableur[nb][4]; //creation de la lignee mail
		
		FileInputStream fis = new FileInputStream("zinput\\LM\\LM"+(nb%2+1)+".doc");
		POIFSFileSystem fs = new POIFSFileSystem(fis);
		HWPFDocument doc = new HWPFDocument(fs);
		
		
		Range r1 = doc.getRange();
	
		for ( int i = 0; i < r1.numSections(); ++i ) { 
			 Section s = r1.getSection(i); 
			 for (int x = 0; x < s.numParagraphs(); x++) { 
				 Paragraph p = s.getParagraph(x); 
				 for (int z = 0; z < p.numCharacterRuns(); z++){ 
					 //character run 
					 CharacterRun run = p.getCharacterRun(z); 
					 //character run text 
					 //String text = run.text(); 
					 //System.out.println(text.toString());
					 for(i=0;i < ep.getMaxLineLength(); i++){
						 String txt = tableur[0][i];
						 txt = "{{"+txt+"}}";
						 String newtxt = tableur[nb][i];
						 
						 run.replaceText(txt, newtxt);
						 
					 }
				 }
			 } 
		 } 
		
		
		
		//outputFileName = path/name.doc
		String outputFileName = createOutputPath(numeroAnnonce,outputFolder,templatePath,tableur[nb][1],tableur[nb][0]);
		
		//creation des sous dossiers output
		if(outputFileName.contains("\\"))
			new File(outputFileName.substring(0, outputFileName.lastIndexOf('\\'))).mkdirs();
		if(outputFileName.contains("/"))
			new File(outputFileName.substring(0, outputFileName.lastIndexOf('/'))).mkdirs();
		
		//System.out.println(doc.getDocumentText());
		doc.write(new File(outputFileName.replaceAll(".doc", " LM.doc")));
		doc.close(); 
	}

	
	public static String createOutputPath(int numeroAnnonce, String outputFolder, String templatePath, String nom, String prenom){
		String outputPath = outputFolder;
		String templateName;
		
		//chemin sous windows
		if(templatePath.contains("\\")){
			templateName= templatePath.substring(templatePath.lastIndexOf("\\")+1,templatePath.length());
			outputPath += "\\annonce "+numeroAnnonce+"\\";
			
			//si le nom du template commence par un nombre, on le met dans le dossier de son type de template
			if(Character.isDigit(templateName.charAt(0))){
				outputPath += "type "+templateName.charAt(0)+"\\";
				outputPath += creatOutputName(templateName.substring(1, templateName.length()),nom,prenom);
			}
			else
				outputPath += creatOutputName(templateName,nom,prenom);
		}
		else{
		//chemin linux
			templateName= templatePath.substring(templatePath.lastIndexOf("/")+1,templatePath.length());
			outputPath += "/annonce "+numeroAnnonce+"/";
			
			//si le nom du template commence par un nombre, on le met dans le dossier de son type de template
			if(Character.isDigit(templateName.charAt(0))){
				outputPath += "type "+templateName.charAt(0)+"/";
				outputPath += creatOutputName(templateName.substring(1, templateName.length()),nom,prenom);
			}
			else
				outputPath += creatOutputName(templateName,nom,prenom);
		}

		return outputPath;
	}
	
	
	public static String creatOutputName(String templateName, String nom, String prenom){
		String outputName;
		
		if(templateName.compareTo("P_NOM.doc") == 0){
			outputName = prenom.substring(0, 1).toUpperCase()+"_"+nom.toUpperCase();
		}
		else if(templateName.compareTo("NOM.doc") == 0){
			outputName = nom.toUpperCase();
		}
		else if(templateName.compareTo("Prénom Nom.doc") == 0){
			outputName = prenom.substring(0, 1).toUpperCase()+prenom.substring(1, prenom.length()).toLowerCase()+" "+nom.substring(0, 1).toUpperCase()+nom.substring(1, nom.length()).toLowerCase();
		}
		else
			outputName = prenom+" "+nom;
		
		return outputName+".doc";
	}
	
	
	public String[][] getTableur() {
		return tableur;
	}
	
	
}
