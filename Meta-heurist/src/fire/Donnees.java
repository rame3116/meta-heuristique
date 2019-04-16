package fire;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
//Chaz
///home/rahmoun/4IR/Meta-heuristique/meta-heuristique/Instances
public class Donnees {
private static String file;
private static int nbevac; //nombre de sommets à évacuer
private static int noeud_safe; //noeud à atteindre
private static int compteur_ligne=0;
private static int tab [][] = new int [10][200];

	/*public Donnees() {
		file="/home/rahmoun/4IR/Meta-heuristique/meta-heuristique/Instances/dense_10_30_3_1.full";
	}*/
	
	//Ouverture du fichier
	public static void main(String[] args) throws IOException {
		try {
			
			BufferedReader br = new BufferedReader(new FileReader("/home/rahmoun/4IR/Meta-heuristique/meta-heuristique/Instances/dense_10_30_3_1.full"));
			String line;
			
			while ((line = br.readLine()) != null) {
			   compteur_ligne ++;


			   
			   if(compteur_ligne==2) {
			    	String[] mots = line.split(" ") ;
			    	nbevac = Integer.parseInt(mots[0]);
			    	noeud_safe = Integer.parseInt(mots[1]);
			
			    	//Affichage de l'en-tete
					System.out.println("Nom de l'instance résolue : "+nbevac) ; 
					System.out.println("Nombre de sommets à évacuer : "+noeud_safe) ; 

			    }
			   
			   if(compteur_ligne>2 && compteur_ligne<13) {
			    	String[] mots = line.split(" ") ;
			    	int nb_noeuds = Integer.parseInt(mots[3]) ;
			    	System.out.println("noeud : "+mots[0]+ " population : "+mots[1]+"; taux max "+mots[2]+"; nombre de noeuds à parcourir : "+mots[3]+"; noeud S : "+mots[nb_noeuds+3]); 
			    	tab[compteur_ligne-3][0]=Integer.parseInt(mots[0]) ;
			    	tab[compteur_ligne-3][1]=Integer.parseInt(mots[1]) ;
			    	tab[compteur_ligne-3][2]=Integer.parseInt(mots[2]) ;
			    	tab[compteur_ligne-3][3]=Integer.parseInt(mots[3]) ;
			    	for (int i=0; i<nb_noeuds; i++) {
				    	tab[compteur_ligne-3][i+4]=Integer.parseInt(mots[i+4]) ;
				    		    		
			    	}

		    		/*System.out.println("chemin: "); 
			    	for(int i=1; i<(tab[0][3]+1);i++) {
			    		System.out.print(tab[0][3+i]+" "); //ça print le chemin 
			    	}		    	*/
				   
			   }
			   
			   if(compteur_ligne == 14) {
				   
			   }
				   				
			}
	

			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(file);
			e.printStackTrace();
		}
	

	}
}
