import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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
	
	private ArrayList<Template> templates = new ArrayList<Template>();//liste des templates
	private ArrayList<Template> templatesLM = new ArrayList<Template>();
	private String outputFolder, excelPath;//dossier de sortie et chemin du .xls
	
	private long seed;
	private boolean liaisonCV_LM = false, annonceMemeQualite = false;
	
	private CVCreator cvc;
	
	
	public void init(ArrayList<Template> t, ArrayList<Template> tlm, String output, String excel, boolean liaison, boolean qualite, long seed){
		this.liaisonCV_LM = liaison;
		this.annonceMemeQualite = qualite;
		this.seed = seed;
		
		
		//le caractere "\" est special et il faut le signaler avec un \ avant donc ca donne : "\\" pour les chemins windows
		//en fait pas besoin, autant pour moi (ça marchait quand même avec les .replace mais pas besoin)
		/*for(int i=0; i<t.size(); i++)
			t.set(i, new Template(t.get(i).filename.replace("\\","\\\\"),t.get(i).filepath.replace("\\","\\\\")));
		for(int i=0; i<t.size(); i++)
			System.out.println(t.get(i).filepath);
		
		for(int i=0; i<tlm.size(); i++)
			tlm.set(i, new Template(tlm.get(i).filename.replace("\\","\\\\"),tlm.get(i).filepath.replace("\\","\\\\")));
		for(int i=0; i<tlm.size(); i++)
			System.out.println(tlm.get(i).filepath);*/
		
		
		this.templates = t;
		this.templatesLM = tlm;
		this.outputFolder = output;//.replace("\\","\\\\");
		this.excelPath = excel;//.replace("\\","\\\\");
	}
	
	
	//ancienne fonction main() ( avec create() )
	public String[][] generate(int nbOffres, int CVparOffre) throws EncryptedDocumentException, InvalidFormatException, IOException{
		//int nbOffres = 2, CVparOffre = 4;
		
		ExcelParser ep = new ExcelParser();
		ep.getSourceExcel(this.excelPath);
		cvc = new CVCreator(ep);
		cvc.createCVData(nbOffres, CVparOffre, seed);
		
		this.tempprint(cvc);
		
		//renvoie le tableau mélangé avec en plus la colonne n° annonce 
		return returnOfGenerate(cvc.getTableur(),nbOffres,CVparOffre);
	}
	
	
	/**
	 * Fonction créant la colonne du numero d'annonce
	 * 
	 * @param s tableau des données
	 * @param nbOffres nombre d'annonces d'offres
	 * @param nbCvParOffre nombre de CV pour chaque offre
	 * 
	 * @return tableau de données en entrée + une colonne avec le numéro d'annonce
	 */
	public String[][] returnOfGenerate(String[][] s, int nbOffres, int CVparOffre){
		String [][] t = new String[s.length][(s[0].length)+1];
		
		int num = 1;
		int cpt = 0;
		for(int i=1; i<s.length; i++){
			if(cpt == CVparOffre){
				cpt = 0;
				num++;
			}
			t[i][0] = ""+num;
			cpt++;
			for(int j=0; j<s[0].length; j++){
				t[i][j+1] = s[i][j];
			}
		}
		
		for(int j=0; j<s[0].length; j++){
			t[0][j+1] = s[0][j];
		}
		t[0][0] = "annnonce";
		
		return t;
	}
	
	/**
	 * Fonction créant 'nbCvParOffre' pour chaque offre (définit par 'nbOffres') 
	 * 
	 * @param nbOffres nombre d'annonces d'offres
	 * @param nbCvParOffre nombre de CV pour chaque offre
	 * @throws IOException
	 */
	public void create(int nbOffres, int nbCvParOffre, long seed) throws IOException{
        int cpt = 0, numAnnonce =  1;
        //ArrayList<Integer> templatesValide = new ArrayList<Integer>();
        //Random rd = new Random(seed);
        ArrayList<Integer> listeCV;
        ArrayList<Integer> listeLM;
        
        if(!annonceMemeQualite){
        	if(seed > 0){
            	if(liaisonCV_LM){
            		Collections.shuffle(templatesLM,new Random(seed));
            		Collections.shuffle(templates,new Random(seed));
            	}
            	else{
	                //System.out.println("liste templates : "+templates);
	                Collections.shuffle(templates,new Random(seed));
	                Collections.shuffle(templatesLM,new Random(3*seed));
	                //System.out.println("seed:"+(seed+i)+" - liste templates shuffled : "+templates);
            	}
            }
            else{
            	if(liaisonCV_LM){
            		int rnd = new Random().nextInt();
            		//System.out.println("liste templates : "+templates);
            		//System.out.println("liste templates LM: "+templatesLM);
            		
            		Collections.shuffle(templatesLM,new Random(rnd));
            		Collections.shuffle(templates,new Random(rnd));
            		
            		
            		//System.out.println("no seed - liste templates shuffled : "+templates);
            		//System.out.println("no seed - liste templates LM shuffled : "+templatesLM);
            		
            	}
            	else{
	                //System.out.println("no liaison - liste templates : "+templates);
            		//System.out.println("no liaison - liste templates LM: "+templatesLM);
	                Collections.shuffle(templates,new Random());
	                Collections.shuffle(templatesLM,new Random());
	                //System.out.println("no liaison - no seed - liste templates shuffled : "+templates);
            		//System.out.println("no liaison - no seed - liste templates LM shuffled : "+templatesLM);
            	}
            }
        	
        	listeCV = null;
        	listeLM = null;
        }
        else{
        	System.out.println("liste templates : "+templates);
        	System.out.println("liste templates LM: "+templatesLM);
        	listeCV = CreerListeDeMemeQualitee(templates, nbCvParOffre, seed, nbOffres, false);
        	listeLM = CreerListeDeMemeQualitee(templatesLM, nbCvParOffre, seed, nbOffres, true);
        }
        	
        
        for(int i = 0; i<nbOffres*nbCvParOffre; i++){
           
            if(cpt == nbCvParOffre){
                cpt = 0;
                numAnnonce++;
                
            }
            System.out.println("cv/offres:"+nbCvParOffre+"cpt="+cpt+" annonce="+numAnnonce);
           
            String path = this.templates.get(i%templates.size()).filepath;
            String pathLM = this.templatesLM.get(i%templatesLM.size()).filepath;
            System.out.println(" de base pathLM:"+pathLM);
           
            if(annonceMemeQualite){
            	  path = this.templates.get(listeCV.get(i)).filepath;
            	  pathLM = this.templatesLM.get(listeLM.get(i)).filepath;
                
            }
            cvc.createCV(numAnnonce, i+1, path, this.outputFolder);
            System.out.println(" après pathLM:"+pathLM);
            cvc.createLM(numAnnonce, i+1, pathLM, this.outputFolder, liaisonCV_LM, path);
           
            cpt++;
        }
    }
	
	
	
	
	 /**
     * Creer une liste d'entier qui correspond aux numeros des cv ou lm a choisir
     * @param templateList la liste de templates de cv ou lm pour laquelle on veux generer la liste de choix.
     * @param nbOffres nombre d'annonces d'offres
     * @param nbCvParOffre nombre de CV pour chaque offre
     * @throws IOException
     * @return renvois une liste de cv ou lm de meme qualitée, conserve les liens de liaisonCV_LM si coché.
     */
    public ArrayList<Integer> CreerListeDeMemeQualitee(ArrayList<Template> templateList, int nbCvParOffre, long seed, int nbOffres, boolean lettreMotiv){
    	System.out.println("---templateList : "+templateList);
        ArrayList<Integer> templatesValide = new ArrayList<Integer>();
        //random pour mélanger l'ordre de creation des cv
        Random rd = new Random(seed);
        //random pour mélanger l'ordre de creation des LM
        Random rd2;
        if(liaisonCV_LM){
            rd2 = new Random(seed);
        }
        else{
            rd2 = new Random(seed+12);
        }
        
        //10 qualitées de cv
        int getAmount[] = new int[10];
        //compte combien de cv de chaque qualitée il y a
        for(Template s : templateList){
            int i = s.filename.charAt(0) - 48;//48 = 0 ascii
            getAmount[i]++;
        }
        //contiens les valeurs de cv qu'on garde
        for(int i = 0; i < getAmount.length; i++){
            if(getAmount[i] >= nbCvParOffre){
                templatesValide.add(i);
            }
        }
        //genere une liste de cv à generer
        ArrayList<Integer> listeTemplate = new ArrayList<Integer>();
        for(int i = 0; i < nbOffres; i++){
        	int qualiteeChoisie = templatesValide.get(rd.nextInt(templatesValide.size()));
        	
        	//System.out.println("qualiteeChoisie : "+qualiteeChoisie+" templatesValide.size() "+templatesValide.size());
            for(int j = 0; j < nbCvParOffre; j++){
                int indexDuCV = 0;
                for(int k = 0; k < qualiteeChoisie; k++){
                    indexDuCV+=getAmount[k];
                }
                //choisis un des cv de la qualitée en question(valeur de 1 au nombre de cv de cette qualitée)
                if(lettreMotiv){
                	indexDuCV+= rd2.nextInt(getAmount[qualiteeChoisie]);
                	
                	//on passe une valeur de rd dans le vide, pour en être au même moment que si c'était un CV
                	@SuppressWarnings("unused")
					int x = rd.nextInt(getAmount[qualiteeChoisie]-1)+1;
                }
                else
                	indexDuCV+= rd.nextInt(getAmount[qualiteeChoisie]);
                
                //System.out.println("indexduCV : "+indexDuCV);
                listeTemplate.add(indexDuCV);
            }
        }
        
        System.out.println(">>>listeTemplate : "+listeTemplate);
        return listeTemplate;
    }
	
	
	
	
	
	
	/*public static void main(String[] args) throws IOException, EncryptedDocumentException, InvalidFormatException {
		Test olol = new Test();
		
		//olol.exec("TEST EXCEL.xls");
		ExcelParser ep = new ExcelParser();
		ep.getSourceExcel("EXCEL.xls");
		CVCreator cvc = new CVCreator(ep);
		cvc.createCVData(2, 3);//2offres 3CV/offre
		olol.tempprint(cvc);
		cvc.createCV(1, "TEST CV - NOM.doc");
	}*/
	
	
	
	public void tempprint(CVCreator cvc){
		System.out.println("Temp Print :");
		for(int i = 0; i < cvc.tableur.length;i++){

			System.out.print("||");
			for(int j = 0; j < cvc.tableur[i].length; j++){
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
	   	
	   	//pour faire simple on remplace "telephone" par "telephone : 0632548654" au lieu de rajouter le numéro ÃƒÂ  la suite
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
					 run.replaceText("email",mail); //ATTENTION : utiliser 'email' et pas simplement 'mail' sinon problÃƒÂ¨me avec les fin d'adresse 'gmail' ou 'hotmail'
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
