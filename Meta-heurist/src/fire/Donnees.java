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
			    	tab_chemins[compteur_ligne-3][0]=Integer.parseInt(mots[0]) ; //id du noeud
			    	tab_chemins[compteur_ligne-3][1]=Integer.parseInt(mots[1]) ; //population
			    	tab_chemins[compteur_ligne-3][2]=Integer.parseInt(mots[2]) ; //taux max
			    	tab_chemins[compteur_ligne-3][3]=Integer.parseInt(mots[3]) ; //k
			    	for (int i=0; i<nb_noeuds; i++) {
			    		tab_chemins[compteur_ligne-3][i+4]=Integer.parseInt(mots[i+4]) ; //chemin mais sans le noeud init
				    		    		
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
			    	tab_arcs[compteur_ligne-15][0]=Integer.parseInt(mots[0]) ;//noeud 1
			    	tab_arcs[compteur_ligne-15][1]=Integer.parseInt(mots[1]) ;//noeud 2
			    	if (mots[2].equals("9223372036854775807")) {
			    		tab_arcs[compteur_ligne-15][2] = Integer.MAX_VALUE; 
			    	}
			    	else {
			    		tab_arcs[compteur_ligne-15][2]=Integer.parseInt(mots[2]) ;//duetime
			    	}
			    	tab_arcs[compteur_ligne-15][3]=Integer.parseInt(mots[3]) ;//travel time
			    	tab_arcs[compteur_ligne-15][4]=Integer.parseInt(mots[4]) ;//capacité


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
	public int[][] getTab_arcs() {
		return tab_arcs;
	}

	public int get_capaArc(int node1, int node2, int[][] tab) {
		
		//Chercher l'index dans le tab_arcs tel que les 2 noeuds correspondent + Retourner capa
		int index_i=0 ;
		while (((tab[index_i][0] != node1) || (tab[index_i][1] != node2)) && index_i<1000) {
			index_i ++ ;
		}
		return tab[index_i][4] ;
	}
	
	public int get_indexArc(int node1, int node2) {
		
		//Chercher l'index dans le tab_arcs tel que les 2 noeuds correspondent
		int index_i=0 ;
		while (((tab_arcs[index_i][0] != node1) || (tab_arcs[index_i][1] != node2)) && index_i<1000) {
			index_i ++ ;
		}
		return index_i ;
	}
	
	public void set_capaArc(int node1, int node2, int[][] tab, int new_capa) {
		int index = get_indexArc(node1, node2) ;
		tab[index][4]=new_capa ;
	}
	
	
	public int next_node(int noeud_init,int noeud_courant) {
		
		int index_i=get_index_fn(noeud_init) ;	//Recup la ligne du chemin à regarder
		int index_j = 0 ;
		while (tab_chemins[index_i][index_j] != noeud_courant) {		//Chercher le noeud courant ds le chemin
			index_j ++ ;
		}
		if(noeud_courant==noeud_init) {
			return tab_chemins[index_i][index_j+4];	
		}
		else {
			return tab_chemins[index_i][index_j+1];	//On s'interesse au noeud juste après le noeud courant 

		}
		
			
	}
	
	public int prec_node(int noeud_init,int noeud_courant) {
		
		int index_i=get_index_fn(noeud_init) ;	//Recup la ligne du chemin à regarder
		int index_j = 1 ;
		while (tab_chemins[index_i][index_j] != noeud_courant) {		//Chercher le noeud courant ds le chemin
			index_j ++ ;
		}
		return tab_chemins[index_i][index_j-1];	//On s'interesse au noeud juste après le noeud courant 
		
			
	}
	
	public int get_index_fn(int s) {		//Get index of tab_chemins from the initial node
		
		int index=0 ;
		while (tab_chemins[index][0] != s) {	// Tq ne l'a pas trouvé
			index ++ ;
		}	
		//CHECK : ERROR CASE, meme si on est sur que tt les noeuds initiaux ont un chemin
		return index ;
			
	}
	
	public int get_finalNode() {
		return noeud_safe ;
	}

}
