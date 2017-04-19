import java.io.File;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelParser {
	private int ligneExcel;//nb lignes du tableau excel
	private int ligneLongue;//ligne la plus longue
	public String def[][];//definition pour tableur
	public int links[];//liens d'association dans le tableau
	//parseur general pour excel (nombre ligne * longueur premiere ligne)
	public ExcelParser(){
		
	}
	public int getMaxLineLength(){
		return ligneLongue;
	}
	
	public int getMaxColumnLength(){
		return ligneExcel;
	}
	
	public void getSourceExcel(String filename) throws EncryptedDocumentException, InvalidFormatException, IOException{

		final File file = new File(filename);
		final Workbook workbook = WorkbookFactory.create(file);
		final Sheet sheet = workbook.getSheet("Feuille1");
		int nbLignes = 0; //nb ligne dans le tableau excel
		int longueurLigne = 0; // longueur des lignes
		
		int index = 0;//1 car 1ere ligne = definition colonnes
	    Row row = sheet.getRow(index++);
	    longueurLigne = row.getLastCellNum();
	    while (row != null) {
	    	nbLignes++;
	    	row = sheet.getRow(index++);
	    }
	    
	    System.out.println("nb lignes : "+nbLignes);
	    System.out.println("longueur des lignes : "+longueurLigne);
	    nbLignes--;//on ne veux pas rajouter au tableau principal les liens entre colonnes.
	    
	    //Crée le tableau qui va contenir le tableau excel
		this.def = new String[nbLignes][longueurLigne];
	    index = 1; // 2eme ligne contiens les liens entre les elements.
	    row = sheet.getRow(index);
		
	    //reset index
	    index = 0;//1 car 1ere ligne = definition colonnes
	    row = sheet.getRow(index++);
	    int cptLigne = 0;
	    System.out.println("Lecture tableau excel :");
	    while (row != null) {
	    	
	    	//colonne 1
	    	System.out.print("||");
	    	for(int rowWidth = 0;rowWidth < row.getLastCellNum();rowWidth++){
		    	if(row.getCell(rowWidth) == null){
		    		def[cptLigne][rowWidth] = "";
		    	}
		    	else if(row.getCell(rowWidth).getCellTypeEnum() == CellType.NUMERIC){
		    		//moved elsewhere
		    	}
		    	else{
		    		def[cptLigne][rowWidth] = row.getCell(rowWidth).getStringCellValue();
		    		System.out.print(row.getCell(rowWidth).getStringCellValue()+"||");
		    	}
	    	}

	    	System.out.println("");
	    	if(row.getCell(0).getCellTypeEnum() != CellType.NUMERIC)
		    	cptLigne++;
	    	row = sheet.getRow(index++);
	    }
	    
	    row = sheet.getRow(1);
	    links = new int[longueurLigne];
	    this.ligneExcel = nbLignes;
	    for(int rowWidth = 0; rowWidth < row.getLastCellNum();rowWidth++){
    		Double val = row.getCell(rowWidth).getNumericCellValue();
    		links[rowWidth] = val.intValue();
    		System.out.print(val.intValue()+"||");
	    }
	    System.out.println();
	    this.ligneLongue = longueurLigne;
	}
}
