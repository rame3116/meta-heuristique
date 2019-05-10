package fire;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
public class Checkzer {
	
	public Checkzer() {
		
	}
	
	public static int check_chemin(int node_id,String test,Donnees donnee) {
		int i=-1;
		int[][] tab_chemins = donnee.getTab_chemins();
		String[] mots = test.split(" ") ;
	
		for (int t=0; t <10; t++) {
			
			
			System.out.println("Test de la ligne n° "+t) ; 
			System.out.println("Comparaison entre "+tab_chemins[t][0]+" et "+node_id) ; 

			if (tab_chemins[t][0] == node_id){
				System.out.println("je suis dans le if");
				int c= 0 ;
				int taille_mots =mots.length-1;
				while(tab_chemins[t][c+4]== Integer.parseInt(mots[c]) && c<taille_mots) {
					c ++ ;
					System.out.println("Comparaison entre "+tab_chemins[t][c+4]+" et "+mots[c]) ; 

				}
				
				//Si toutes les valeurs ont matché jusqu'à la dernière case
				if (c==taille_mots) {
					i=t ;
				}
			}
		}
		return i;

	}
	
	public static int check_solution(int node_id,String test,String fichiertest) throws IOException {
		//Donnees donnee = new Donnees(fichiertest);
		/*BufferedReader br = new BufferedReader(new FileReader("/home/rahmoun/4IR/Meta-heuristique/meta-heuristique/Test_solutions"+fichiertest));
		
		
		String line;
		String file = null;
		int cpt=0;
		if ((line = br.readLine()) != null) {
			file=line;
		}*/
		//Donnees donnee = new Donnees(file+"_I.full");
		Donnees donnee = new Donnees("dense_10_30_3_1_I.full") ;
		int i=-1;
		int[][] tab_chemins = donnee.getTab_chemins();
		String[] mots = test.split(" ") ;
		
		
	
		for (int t=0; t <10; t++) {
			
			
			System.out.println("Test de la ligne n° "+t) ; 
			System.out.println("Comparaison entre "+tab_chemins[t][0]+" et "+node_id) ; 

			if (tab_chemins[t][0] == node_id){
				System.out.println("je suis dans le if");
				int c= 0 ;
				int taille_mots =mots.length-1;
				while(tab_chemins[t][c+4]== Integer.parseInt(mots[c]) && c<taille_mots) {
					c ++ ;
					System.out.println("Comparaison entre "+tab_chemins[t][c+4]+" et "+mots[c]) ; 

				}
				
				//Si toutes les valeurs ont matché jusqu'à la dernière case
				if (c==taille_mots) {
					i=t ;
				}
			}
		}
		return i;

	}
	
	
	public static void main(String[] args) throws IOException {
		System.out.println(check_solution(323,"333 337 342 498 0 520 209 216 473 161 166 170 211 250 281 310 328 336 346 373","dense_10_30_3_8"));
		
		
	}
	
	
}
