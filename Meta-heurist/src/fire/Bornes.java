package fire;

import java.util.ArrayList;

public class Bornes {

	//Donnees de Data que l'on va récupérer à travers le constructeur
	Data file ;
	public ArrayList<ArrayList<Integer>> tabChemins = new ArrayList<ArrayList<Integer>>();
	public ArrayList<ArrayList<Integer>> tabArcs = new ArrayList<ArrayList<Integer>>();
	//public ArrayList<ArrayList<ArrayList<Integer>>> tabEvents ;
	
	
	public Bornes (Data file1) {
		file = file1 ;
		tabChemins = file.get_tabChemins() ;
		tabArcs = file.get_tabArcs() ;
		borne_sup() ;
	}
	
	
	public int borne_sup() {
		
		//Pour chaque chemin d'evacuation: on cherche à calculer son tps d'évacuation
		//for(int chemin=0; chemin<file.get_nb_chemins() ;chemin++) {	//A DECOM
			int chemin = 0 ;	
			int taux_max = file.tauxMax_chemin(0) ;	//Taux max pour ce noeud d'evacuation
			
			//Gérer la population sortant du noeud d'évac
			int temps_premier=0 ;		//On effectue le 1er passage à t=0
			int population = file.get_popChemin(chemin);	//Population à évacuer de ce noeud
			
			//L'arc que l'on traite
			int noeud_courant = tabChemins.get(chemin).get(4) ;		//Noeud courant
			int noeud_suivant = tabChemins.get(chemin).get(5) ;		//Noeud courant

			//Nb de passage et le temps auquel ils vont ts arriver
			int nb_passageArc = population/taux_max ;
			int temps_dernier = 	temps_premier + nb_passageArc ;		//Tps dernier envoi du noeud d'evac
			int capa_arc = file.get_capaArc(noeud_courant, noeud_suivant, chemin) ;	//Non utile (car déja tauxMax est censé etre < ou = à chaque capa, mais pt etre utile pr le debug)
			//Print de l'evacuation de ce chemin
			System.out.println("[BorneSup] Evacution chemin "+chemin+" ("+population+") Depuis le node "+noeud_courant+" à "+noeud_suivant+" de capa "+capa_arc+" avec un débit de "+taux_max+", le 1er paquet va sortir à "+temps_premier+" et le dernier en "+temps_dernier);
			
			//Maj la structure en ajoutant ce couple (temps_premier,+taux_max) et (temps_dernier+1, - taux_max): Ce sont les variations du flot
			//On appele le constructeur de cette struct pour l'initialiser avec ces 2 1ères variations
			Events events  = new Events(noeud_courant, temps_premier,temps_dernier+1, taux_max) ; 
			events.print_tabEvents();
			
			
			
			//Boucle sur les noeuds du chemin pour calculer le temps
			for (int node=1; node<tabChemins.get(chemin).get(3)+1; node ++) {
				//Test si le noeud que l'on traite n'existe pas déja 
			}
			
		//}
		
		
		
		
		return 0;
	}
	
	public int borne_inf() {
		
		return 0 ;
	}
	
}
