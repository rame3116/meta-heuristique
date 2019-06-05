package fire;

import java.util.ArrayList;

public class Events {
	
		public int taille_tab = 0 ;	//Taille du tab, utile pour le print du tab
	
		//Tableau contenant tout les noeuds, chaque noeud contient une liste de couple (tps,flot sortant)
		ArrayList<ArrayList<ArrayList<Integer>>> tabEvents = new ArrayList<ArrayList<ArrayList<Integer>>>(); //Le 1er indice contient le
		//Exp: 	// 	 1	//		2		//		3		//
		//		__________________________________________
		//		(5,+5)	//	(10,-6)		//				
		//		__________________________________________
						//	(19,+9)
		
		public Events(int premier_noeud, int temps_debut, int temps_fin, int taux) {
			//On crée le 1er array (1 dimension) contenant le nom du noeud puis un array (un dimension) pour le 1er noeud
			
			//1ère ligne de la 1ère colonne de notre tab contenant le nom du noeud, puis le nb d'evenement
			ArrayList <Integer> nom_noeud = new ArrayList <Integer> () ;
			nom_noeud.add(premier_noeud) ;
			nom_noeud.add(0) ;
			
			//2e ligne: contenant le 1ère evenement: Début activation flot
			ArrayList <Integer> premier_event = new ArrayList <Integer> () ;
			premier_event.add(temps_debut) ;
			premier_event.add(taux) ;

			//3e ligne: contenant le 2e evenement
			ArrayList <Integer> deuxieme_event = new ArrayList <Integer> () ;
			deuxieme_event.add(temps_fin) ;
			deuxieme_event.add(-taux) ;
			
			//On crée un array x array contenant tout ce qui relatif au 1er noeud (les 3 array plus haut)
			ArrayList<ArrayList<Integer>> noeud1 = new ArrayList<ArrayList<Integer>>();
			noeud1.add(nom_noeud) ;
			noeud1.add(premier_event) ;
			noeud1.add(deuxieme_event) ;
			
			taille_tab ++ ;
			//tabEvents.get(0).get(0).get(1) = 2 ;
		}
		
		
		public ArrayList<ArrayList<ArrayList<Integer>>> get_tabEvents(){
			return tabEvents ;
		}

		public void add_Event(int node, int temps_debut, int temps_fin) {
			
			//On test d'abord si le node n'a pas de colonne qui lui est propre comme ds l'exmple
			//On doit itérer sur la 1ère dimension du tab
				//tabEvents.iterator(){
					
				//}	
			
		}
		
		public void print_tabEvents() {
			
			//Boucle pour chaque colonne du tab
			for (int i=0; i<taille_tab; i++) {
				int noeud = get_noeudIndex(i) ;
				int nb_event = get_nbEventIndex(i) ;
				//Print le num de la ligne 
				System.out.println("Ligne du tabEvents n°"+i+" correspondant au noeud "+noeud+" ayant "+nb_event+" events") ;
				
				//Boucle for sur le nb d'e

			}
		}
			
			//Getteurs pour une colonne donnée (son noeud et le nb d'events)
		
		//Retourne le nom du noeud correspondant à un index donné du tableau d'events
		public int get_noeudIndex(int index_tab) {
			return tabEvents.get(index_tab).get(0).get(0) ;
		}
		//Retourne le nb d'events correspondant à un index donné du tableau d'events
		public int get_nbEventIndex(int index_tab) {
			return tabEvents.get(index_tab).get(0).get(0) ;
		}
}
