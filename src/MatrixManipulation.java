import java.util.ArrayList;
import java.util.Random;

public class MatrixManipulation {

	
	
	

	public static void shuffleSourceData(int[] links, String def[][], int lineAmount, long seed){
		Random rnd;
		if(seed>0)
			rnd = new Random(seed);
		else
			rnd = new Random();
		
		int parsedLinks[] = new int[links.length];
		
		for(int i = 0; i < links.length; i++){
			boolean isAlreadyShuffled = false;
			ArrayList<Integer> linkedCollumn = new ArrayList<Integer>();
			//verifier que l'on doit bien mélanger la colonne
			for(Integer in : parsedLinks){
				if(links[i] == in){
					isAlreadyShuffled = true;
				}
			}
			//verifier si des colonnes sont liés
			for(int j = 0;j < links.length;j++){
				if(links[i] == links[j]){
					linkedCollumn.add(j);
				}
			}
			//mélanger toutes les colonnes liées en meme temps
			if(!isAlreadyShuffled)
				for(int k : linkedCollumn)
					parsedLinks[k] = links[i];
				isAlreadyShuffled = false;
				int rand1, rand2;
				
				
				for(int r = 0; r < 25; r++){
					Boolean exists = true;
					rand1 = rnd.nextInt(lineAmount - 1) + 1;
					rand2 = rnd.nextInt(lineAmount - 1 ) + 1;
					for(int link : linkedCollumn){
						//System.out.print(link);
						if(def[rand1][link] == "" || def[rand2][link] == ""){
							exists = false;
							System.out.println("ALARME");
						}
					}
					if(exists){
						String temp = new String();
						int lo = 0;
						for(int lol: linkedCollumn){
							lo = lol;
							temp = def[rand1][lo];
							def[rand1][lo] = def[rand2][lo];
							def[rand2][lo] = temp;
						}
					}
					else{
						System.out.println("ALAAAAAAAAAAAAAAARME");
					}
				}
		}
	}
}
