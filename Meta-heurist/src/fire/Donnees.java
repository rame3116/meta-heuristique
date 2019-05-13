package fire;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
//Chaz
///home/rahmoun/4IR/Meta-heuristique/meta-heuristique/Instances
public class Donnees {
private String file;
private int nbevac; //nombre de sommets à évacuer
private int noeud_safe; //noeud à atteindre

private int nb_noeuds_tot; //Nb noeuds
private int nb_arcs_tot ; //Nb arcs

private int compteur_ligne=0;
private int[][] tab_chemins = new int [10][200];
private int[][] tab_arcs = new int [1000][5];


	public Donnees(String file1) throws IOException {
		file="./../../meta-heuristique/InstancesInt/"+file1;
		crea_tableaux();
		
	}
	
	//Ouverture du fichier
	public void crea_tableaux() throws IOException {
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			
			while ((line = br.readLine()) != null) {
			   compteur_ligne ++;


			   //1ere ligne info
			   if(compteur_ligne==2) {
			    	String[] mots = line.split(" ") ;
			    	nbevac = Integer.parseInt(mots[0]);
			    	noeud_safe = Integer.parseInt(mots[1]);
			
			    	//Affichage de l'en-tete
					//System.out.println("Nom de l'instance résolue : "+nbevac) ; 
					//System.out.println("Nombre de sommets à évacuer : "+noeud_safe) ; 

			    }
			   
			   //Chemins jusqu'aux noeuds finaux
			   if(compteur_ligne>2 && compteur_ligne<13) {
			    	String[] mots = line.split(" ") ;
			    	int nb_noeuds = Integer.parseInt(mots[3]) ;
			    	//System.out.println("noeud : "+mots[0]+ " population : "+mots[1]+"; taux max "+mots[2]+"; nombre de noeuds à parcourir : "+mots[3]+"; noeud S : "+mots[nb_noeuds+3]); 
			    	tab_chemins[compteur_ligne-3][0]=Integer.parseInt(mots[0]) ;
			    	tab_chemins[compteur_ligne-3][1]=Integer.parseInt(mots[1]) ;
			    	tab_chemins[compteur_ligne-3][2]=Integer.parseInt(mots[2]) ;
			    	tab_chemins[compteur_ligne-3][3]=Integer.parseInt(mots[3]) ;
			    	for (int i=0; i<nb_noeuds; i++) {
			    		tab_chemins[compteur_ligne-3][i+4]=Integer.parseInt(mots[i+4]) ;
				    		    		
			    	}

			    	//print le chemin
		    		/*System.out.println("chemin: "); 
			    	for(int i=1; i<(tab_chemins[0][3]+1);i++) {
			    		System.out.print(tab_chemins[0][3+i]+" "); //ça print le chemin 
			    	}		    	*/
				   
			   }
			   
			   //Nb noeuds, nb arcs
			   if(compteur_ligne == 14) {
			    	String[] mots= line.split(" ") ;
			    	nb_noeuds_tot = Integer.parseInt(mots[0]);
			    	nb_arcs_tot = Integer.parseInt(mots[1]);
			
			    	//Affichage de l'en-tete
					//System.out.println("Nb noeuds: "+nb_noeuds_tot) ; 
					//System.out.println("Nb arcs : "+nb_arcs_tot) ; 
			   }
			   
			 //Chemins jusqu'aux noeuds finaux
			   if(compteur_ligne>14 ) {
			    	String[] mots = line.split(" ") ;
			    	int nb_noeuds = Integer.parseInt(mots[3]) ;
			    	//System.out.println("noeud n°1 : "+mots[0]+ " noeud n°2 : "+mots[1]+"; duedate "+mots[2]+"; length : "+mots[3]+"; capacity : "+mots[4]); 
			    	tab_arcs[compteur_ligne-15][0]=Integer.parseInt(mots[0]) ;
			    	tab_arcs[compteur_ligne-15][1]=Integer.parseInt(mots[1]) ;
			    	if (mots[2].equals("9223372036854775807")) {
			    		tab_arcs[compteur_ligne-15][2] = Integer.MAX_VALUE; 
			    	}
			    	else {
			    		tab_arcs[compteur_ligne-15][2]=Integer.parseInt(mots[2]) ;
			    	}
			    	tab_arcs[compteur_ligne-15][3]=Integer.parseInt(mots[3]) ;
			    	tab_arcs[compteur_ligne-15][4]=Integer.parseInt(mots[4]) ;


			    	//print le chemin
		    		/*System.out.println("chemin: "); 
			    	for(int i=1; i<(tab_chemins[0][3]+1);i++) {
			    		System.out.print(tab_chemins[0][3+i]+" "); //ça print le chemin 
			    	}		    	*/
				   
			   }
				   				
			}
	

			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//System.out.println("Tab arcs : "+tab_arcs);
			//e.printStackTrace();
		}
	

	}

	public int[][] getTab_chemins() {
		return tab_chemins;
	}


}
