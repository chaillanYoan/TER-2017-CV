import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import org.apache.commons.cli.ParseException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.artofsolving.jodconverter.cli.Convert;
import org.json.JSONException;


public class Test {
	int ligneExcel;//nb lignes du tableau excel
	int ligneLongue;//ligne la plus longue
	String tableur[][];//tableau recevant les données pour les cv a creer.
	private String tableauDeRetour[][] ;//tableau créé qui affichera la liste des personnes+numero d'annonce+templates utilisés
	private String tableauDeRetourAvecChemins[][] ;//meme tableau que 'tableauDeRetour' mais avec les chemins des templates au lieu des noms
	
	/*chemins des CV et LM a convertir en pdf*/
	private ArrayList<String> CVdocToPdf = new ArrayList<String>();
	private ArrayList<String> LMdocToPdf = new ArrayList<String>();
	
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
		String temp[][] = returnOfGenerate(cvc.getTableur(),nbOffres,CVparOffre);
		tableauDeRetour = new String[temp.length][(temp[0].length)+3];
		tableauDeRetourAvecChemins = new String[temp.length][(temp[0].length)+3];
		
		for(int i=0; i<temp.length; i++){
			for(int j=0; j<temp[0].length; j++)
				tableauDeRetour[i][j] = temp[i][j];
		}
		
		tableauDeRetour[0][temp[0].length] = "template CV";
		tableauDeRetour[0][temp[0].length+1] = "template LM";
		tableauDeRetour[0][temp[0].length+2] = "type";
		
		
		
		for(int i=0; i<tableauDeRetour.length; i++){
			for(int j=0; j<tableauDeRetour[0].length; j++){
				if(tableauDeRetour[i][j] == null)
					tableauDeRetourAvecChemins[i][j] = null;
				else
					tableauDeRetourAvecChemins[i][j] = tableauDeRetour[i][j];
			}
		}
		
		
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
		//on crée un tableau avec le deuxième [] ayant une case en plus pour le numero d'annonce
		String [][] t = new String[s.length][(s[0].length)+1];
		
		
		//on va ensuite copié le tableau de base dans le deuxième, mais décalé d'une case
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
	public void create(int nbOffres, int nbCvParOffre, long seed, boolean createFiles, int tailleTableau) throws IOException{
        int cpt = 0, numAnnonce =  1;
        //ArrayList<Integer> templatesValide = new ArrayList<Integer>();
        //Random rd = new Random(seed);
        ArrayList<Integer> listeCV;
        ArrayList<Integer> listeLM;
        
        //si les annonces peuvent contenir différents types,
        if(!annonceMemeQualite){
        	
        	//on mélange avec Collections.shuffle en utilisant(ou non) la seed fournit par l'utilisateur
        	//et en utilisant exactement la même chose si les CV et LM doivent être liés
        	if(seed > 0){
            	if(liaisonCV_LM){
            		Collections.shuffle(templatesLM,new Random(seed));
            		Collections.shuffle(templates,new Random(seed));
            	}
            	else{
	                Collections.shuffle(templates,new Random(seed));
	                Collections.shuffle(templatesLM,new Random(3*seed));
            	}
            }
            else{
            	if(liaisonCV_LM){
            		int rnd = new Random().nextInt();
            		
            		Collections.shuffle(templatesLM,new Random(rnd));
            		Collections.shuffle(templates,new Random(rnd));
            		
            	}
            	else{
	                Collections.shuffle(templates,new Random());
	                Collections.shuffle(templatesLM,new Random());
            	}
            }
        	
        	listeCV = null;
        	listeLM = null;
        }
        else{
        	//annonces de même type
        	
        	if(liaisonCV_LM){
        		System.out.println("+annonceMemeQualite liste templates : "+templates);
	        	listeCV = CreerListeDeMemeQualitee(templates, nbCvParOffre, seed, nbOffres, false);
	        	
	        	System.out.println("+annonceMemeQualite liste templates LM: "+templatesLM);
	        	//on donne la listes de CV, mais on utilisera par la suite : templates.linkedLM
	        	listeLM = CreerListeDeMemeQualitee(templates, nbCvParOffre, seed, nbOffres, true);
        	}
        	else{
	        	System.out.println("+annonceMemeQualite liste templates : "+templates);
	        	listeCV = CreerListeDeMemeQualitee(templates, nbCvParOffre, seed, nbOffres, false);
	        	System.out.println("+annonceMemeQualite liste templates LM: "+templatesLM);
	        	listeLM = CreerListeDeMemeQualitee(templatesLM, nbCvParOffre, seed, nbOffres, true);
        	}
        }
        	
        
        for(int i = 0; i<nbOffres*nbCvParOffre; i++){
           
            if(cpt == nbCvParOffre){
                cpt = 0;
                numAnnonce++;
                
            }
            System.out.println("cv/offres:"+nbCvParOffre+"cpt="+cpt+" annonce="+numAnnonce);
           
            String path = this.templates.get(i%templates.size()).filepath;
            String pathLM = this.templatesLM.get(i%templatesLM.size()).filepath;
           
            if(annonceMemeQualite){
            	if(liaisonCV_LM){
            		path = this.templates.get(listeCV.get(i)).filepath;
              	  	pathLM = this.templates.get(listeCV.get(i)).getLinkedLM().filepath;
            	}
            	else{
            	  path = this.templates.get(listeCV.get(i)).filepath;
            	  pathLM = this.templatesLM.get(listeLM.get(i)).filepath;
            	}
            }
            
            
            
            Random randomPdf;
            if(seed > 0)
            	randomPdf = new Random(seed);
            else 
            	randomPdf = new Random();
            
            Boolean makePdf;
            if(randomPdf.nextInt(100) < 50)
            	makePdf = true;
            else
            	makePdf = false;
            
            
            
            
            if(createFiles){
            	cvc.createCV(numAnnonce, i+1, tableauDeRetourAvecChemins[i+1][(tableauDeRetourAvecChemins[0].length)-3], this.outputFolder, makePdf);
            	cvc.createLM(numAnnonce, i+1, tableauDeRetourAvecChemins[i+1][(tableauDeRetourAvecChemins[0].length)-2], this.outputFolder, liaisonCV_LM, tableauDeRetourAvecChemins[i+1][(tableauDeRetourAvecChemins[0].length)-3], makePdf);
            }
            else{
            	String nameCV, nameLM, type;
            	
            	
                
            	nameCV = path;
            	nameLM = pathLM;
            	
            	
            	//windows
            	if(nameCV.contains("\\")){
            		nameCV = nameCV.substring(nameCV.lastIndexOf("\\")+1, nameCV.length());
            		nameLM = nameLM.substring(nameLM.lastIndexOf("\\")+1, nameLM.length());
            	}
            	//linux
            	else{

            		nameCV = nameCV.substring(nameCV.lastIndexOf("/")+1, nameCV.length());
            		nameLM = nameLM.substring(nameLM.lastIndexOf("/")+1, nameLM.length());
            	}
            	
            	
            	
            	
            	if(Character.isDigit(nameCV.charAt(0))){
            		type = ""+nameCV.charAt(0);
            		nameCV = nameCV.substring(1, nameCV.length());
            	}
            	else
            		type = "none";
            
            	if(Character.isDigit(nameLM.charAt(0)))
            		nameLM = nameLM.substring(1, nameLM.length());
            
            	System.out.println("nameCV="+nameCV);
            	System.out.println("nameLM="+nameLM);
            	tableauDeRetour[i+1][tailleTableau] = nameCV;
            	tableauDeRetour[i+1][tailleTableau+1] = nameLM;
            	tableauDeRetour[i+1][tailleTableau+2] = type;
            	
            	
            	tableauDeRetourAvecChemins[i+1][tailleTableau] = path; 
            	tableauDeRetourAvecChemins[i+1][tailleTableau+1] = pathLM;
            	tableauDeRetourAvecChemins[i+1][tailleTableau+2] = type;
            	
            }
           
            cpt++;
        }
        
        CVdocToPdf = cvc.getCVdocToPdf();
        LMdocToPdf = cvc.getLMdocToPdf();
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
    	
    	Collections.sort(templateList, new Comparator<Template>() {
            public int compare(Template one, Template two)  {
                return one.filename.compareTo(two.filename);
            }
        });
    	
    	
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
        ArrayList<Integer> listeCV = new ArrayList<Integer>();
        for(int i = 0; i < nbOffres; i++){
            int qualiteeChoisie;
            if(lettreMotiv)
                qualiteeChoisie = templatesValide.get(rd2.nextInt(templatesValide.size()));
            else
                qualiteeChoisie = templatesValide.get(rd.nextInt(templatesValide.size()));
            

            int indexDuCV = 0;
            int k = 0;
            ArrayList<Integer> temp = new ArrayList<Integer>();
            while(k < nbCvParOffre){
                int genPermutation;

                if(lettreMotiv)
                    genPermutation= rd2.nextInt(getAmount[qualiteeChoisie]);
                else
                    genPermutation= rd.nextInt(getAmount[qualiteeChoisie]);
                
                if(!temp.contains(genPermutation)){
                    temp.add(genPermutation);
                    k++;
                }

            }
            for(k = 0; k < qualiteeChoisie; k++){
                indexDuCV+=getAmount[k];
            }
            
            System.out.println("taille de temp :"+temp.size());
            for(Integer in : temp){
                listeCV.add(indexDuCV + in);
            }

        }
        return listeCV;
      
    }
	
	
	
	/**
	 * Fonction pour convertir un 0doc en pdf
	 * non fonctionnelle... 
	 * 
	 **/
    public void createPdf(){
    	Convert convertor = new Convert();
    	String path[] = {null, null};
    	
    	path[0] = CVdocToPdf.get(0);
    	path[1] = CVdocToPdf.get(0).replaceAll(".doc", ".pdf");
    	
    	try {
			convertor.docToPdf(path);
		} catch (ParseException | JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	System.out.println("----- createPdf -----");
    	System.out.println("CV :");
    	for(int i=0; i<CVdocToPdf.size(); i++)
    		System.out.println(CVdocToPdf.get(i));
    	
    	System.out.println("LM :");
    	for(int i=0; i<LMdocToPdf.size(); i++)
    		System.out.println(LMdocToPdf.get(i));
    }
	
	
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
	
	
	public String[][] getTableauDeRetour() {
		return tableauDeRetour;
	}
	
	
}
