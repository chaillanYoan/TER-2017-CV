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
	
	
	public void createCVData(int nombreOffre, int nombreCVParOffre){
		//creation de la matrice
		tableur = new String[(nombreOffre * nombreCVParOffre) + 1][ep.getMaxLineLength()];
		//initialisation de la premiere ligne.
		for(int i = 0; i < ep.getMaxLineLength(); i++){
			tableur[0][i] = ep.def[0][i];
		}
		for(int i = 0; i < nombreOffre; i++){

			MatrixManipulation.shuffleSourceData(ep.links, ep.def,ep.getMaxColumnLength());
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
	public void createCV(int nb, String templateName, String outputFolder) throws IOException{
	    
	   	System.out.println("Creation CV "+(nb+1));
	   	
	   	/*pour faire simple on remplace "telephone" par "telephone : 0632548654" au lieu de rajouter le numéro Ã  la suite*/
		//String tel = "Téléphone : "+tableur[nb][3]; // création de la ligne telephone
		//String mail = "Mail : "+tableur[nb][4]; //creation de la lignee mail
		
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
		 String outputFileName = outputFolder+"\\"+"TEST CV - "+tableur[nb][1].toUpperCase()+".doc";
		 //System.out.println(doc.getDocumentText());
		 doc.write(new File(outputFileName));
		 doc.close(); 
	}
}
