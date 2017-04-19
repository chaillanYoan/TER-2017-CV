import java.io.IOException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/*
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
*/

public class Test {
	int ligneExcel;//nb lignes du tableau excel
	int ligneLongue;//ligne la plus longue
	String tableur[][];//tableau recevant les données pour les cv a creer.
	
	
	public static void main(String[] args) throws IOException, EncryptedDocumentException, InvalidFormatException {
		Test olol = new Test();
		
		//olol.exec("TEST EXCEL.xls");
		ExcelParser ep = new ExcelParser();
		ep.getSourceExcel("EXCEL.xls");
		CVCreator cvc = new CVCreator(ep);
		cvc.createCVData(2, 3);//2offres 3CV/offre
		olol.tempprint(cvc);
		cvc.createCV(1, "TEST CV - NOM.doc");
	}
	
	public void tempprint(CVCreator cvc){
		System.out.println();
		for(int i = 0; i < 7;i++){

			System.out.print("||");
			for(int j = 0; j < 5; j++){
				System.out.print(cvc.tableur[i][j]+"||");
			}
			System.out.println();
		}
		
	}
	


	

	/**
	 * Creer un tableau excel contenant la matrice de parametre de cv fourni
	 * @param source matrice contenant les informations pour les cv a generer
	 */
	//implementation plus tard
	//public void creerExcelDepuisSource(String source[][]){
		
	//}
	

	
	
	
	
	/** PARSE LE TABLEAU EXCEL ET REMPLIT LE TABLEAU "String tableur[][]"
	 * renvoie le nombre de lignes du tableau excel 
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws EncryptedDocumentException **/
	//pas utilisé atm
/*	public void parseExcel(String filename) throws EncryptedDocumentException, InvalidFormatException, IOException{

		final File file = new File("TEST EXCEL.xls");
		final Workbook workbook = WorkbookFactory.create(file);
		final Sheet sheet = workbook.getSheet("Feuille1");
		int nbLignes = 0; //nb ligne dans le tableau excel
		
		int index = 1;//1 car 1ere ligne = definition colonnes
	    Row row = sheet.getRow(index++);
	    while (row != null) {
	    	nbLignes++;
	    	row = sheet.getRow(index++);
	    }
	    System.out.println("nb lignes : "+nbLignes);
	    
	    //Crée le tableau qui va contenir le tableau excel
		this.tableur = new String[nbLignes][5];
	    
		
	    //reset index
	    index = 1;//1 car 1ere ligne = definition colonnes
	    row = sheet.getRow(index++);
	    int cptLigne = 0;
	    System.out.println("Lecture tableau excel :");
	    while (row != null) {
	    	
	    	//colonne 1
	    	if(row.getCell(0) == null)
	    		tableur[cptLigne][0] = "";
	    	else{
	    		tableur[cptLigne][0] = row.getCell(0).getStringCellValue();
	    		System.out.print(row.getCell(0).getStringCellValue());
	    	}
	    	
	    	
	    	//colonne 2
	    	if(row.getCell(1) == null)
	    		tableur[cptLigne][1] = "";
	    	else{
	    		tableur[cptLigne][1] = row.getCell(1).getStringCellValue();
	    		System.out.print(" - "+row.getCell(1).getStringCellValue());
	    	}
	    	
	    	
	    	//colonne 3
	    	if(row.getCell(2) == null)
	    		tableur[cptLigne][2] = "";
	    	else{
	    		tableur[cptLigne][2] = row.getCell(2).getStringCellValue();
	    		System.out.print(" - "+row.getCell(2).getStringCellValue());
	    	}
	    	

	    	//colonne 4
	    	if(row.getCell(3) == null)
	    		tableur[cptLigne][3] = "";
	    	else{
	    		tableur[cptLigne][3] = row.getCell(3).getStringCellValue();
	    		System.out.print(" - "+row.getCell(3).getStringCellValue());
	    	}
	    	

	    	//colonne 5
	    	if(row.getCell(4) == null)
	    		tableur[cptLigne][4] = "";
	    	else{
	    		tableur[cptLigne][4] = row.getCell(4).getStringCellValue();
	    		System.out.print(" - "+row.getCell(4).getStringCellValue());
	    	}

	    	System.out.println("");
	    	row = sheet.getRow(index++);
	    	cptLigne++;
	    }
	    this.ligneExcel = nbLignes;
	}
	
	*/
	
	/** Fonction pour creer les CVs 
	 * @throws IOException **/
	//1ere version du createCV, gardée au cas ou pour l'instant
/*	public void createCV(int nb, String templateName, String fileIdentifior) throws IOException{
	    
	   	System.out.println("Creation CV "+(nb+1));
	   	
	   	//pour faire simple on remplace "telephone" par "telephone : 0632548654" au lieu de rajouter le numéro Ã  la suite
		String tel = "Téléphone : "+tableur[nb][3]; // création de la ligne telephone
		String mail = "Mail : "+tableur[nb][4]; //creation de la lignee mail
		
		FileInputStream fis = new FileInputStream(templateName);
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
					 String text = run.text(); 
					 //System.out.println(text.toString());
					 
					 run.replaceText("Prénom", tableur[nb][0]);
					 run.replaceText("Nom",tableur[nb][1]);
					 run.replaceText("adresse",tableur[nb][2]);
					 run.replaceText("tel",tel);
					 run.replaceText("email",mail); //ATTENTION : utiliser 'email' et pas simplement 'mail' sinon problÃ¨me avec les fin d'adresse 'gmail' ou 'hotmail'
				 }
			 } 
		 } 
		 String outputFileName = fileIdentifior+tableur[nb][1].toUpperCase()+".doc";
		 //System.out.println(doc.getDocumentText());
		 doc.write(new File(outputFileName));
		 doc.close(); 
	}
*/	
	
}//fin class
