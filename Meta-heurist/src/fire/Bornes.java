package fire;

import java.util.ArrayList;

public class Bornes {

	//Donnees de Data que l'on va récupérer à travers le constructeur
	Data file ;
	Events events  = new Events() ; 
	public ArrayList<ArrayList<Integer>> tabChemins = new ArrayList<ArrayList<Integer>>();
	public ArrayList<ArrayList<Integer>> tabArcs = new ArrayList<ArrayList<Integer>>();

	
	public Bornes (Data file1) {
		file = file1 ;
		tabChemins = file.get_tabChemins() ;
		tabArcs = file.get_tabArcs() ;
		//Events events  = new Events() ; 

	}
	
	
	public int evacuation_chemin(int chemin,int temps_horloge) {
			
			int chemin_nbNoeuds = file.get_nbNodesChemin(chemin) ;
			int taux_max = file.tauxMax_chemin(chemin) ;	//Taux max pour ce noeud d'evacuation
			
						//	Gestion du 1er arc	//
			//_____________________________________________________________________________//
			
			//Gérer la population sortant du noeud d'évac
			System.out.println("[TEST : temps_horloge avant execution du chemin "+chemin+": "+temps_horloge);
			int population = file.get_popChemin(chemin);	//Population à évacuer de ce noeud
			
			//L'arc que l'on traite
			int noeud_courant = tabChemins.get(chemin).get(4) ;		//Noeud courant
			int noeud_suivant = tabChemins.get(chemin).get(5) ;		//Noeud courant

			//Nb de passage et le temps auquel ils vont ts arriver
			int nb_passageArc = population/taux_max ;
			int temps_dernier = temps_horloge + nb_passageArc ;		//Tps dernier envoi du noeud d'evac
			int capa_arc = file.get_capaArc(noeud_courant, noeud_suivant, chemin) ;	//Non utile (car déja tauxMax est censé etre < ou = à chaque capa, mais pt etre utile pr le debug)
			
			//Print de l'evacuation de ce chemin
			//System.out.println("[Evacution chemin] "+chemin+" ("+population+") Depuis le node "+noeud_courant+" à "+noeud_suivant+" de capa "+capa_arc+" avec un débit de "+taux_max+", le 1er paquet va sortir à "+temps_premier+" et le dernier en "+temps_dernier);
			
			//Maj la structure en ajoutant ce couple (temps_premier,+taux_max) et (temps_dernier+1, - taux_max): Ce sont les variations du flot
			//On appele le constructeur de cette struct pour l'initialiser avec ces 2 1ères variations
			//events.first_events(file,noeud_courant, temps_premier,temps_dernier, taux_max) ; 
			events.first_event(file,noeud_courant, temps_horloge,temps_dernier, taux_max, chemin) ; 
			//events.print_tabEvents() ;

			
						// Boucle sur les autres noeuds		//
			//_____________________________________________________________________________//

			
			for (int n=2; n<chemin_nbNoeuds+1; n++) {	//On commence à 2 car le 1er noeud a déjà été traité
				
				//System.out.println("Appel de add event du noeud num "+n);
				//On récupère le temps de traversée entre notre noeud courant et le noeud qui l'a précédé dans le chemin
				int noeud_prec = file.get_precNode(chemin,n+3) ;	//Id du noeud precedent
				int noeud_actuel = file.get_idNode(chemin,n+3) ;	//Id du noeud actuel à l'aide de son index
				//Decalage de 4 car les 4 elm d'un chemin sont des données
				int temps_traverse = file.get_tempsArc(noeud_prec, noeud_actuel, chemin) ;
					
				//System.out.println("Futur appel de Add Event sur le noeud: "+noeud_actuel+" de noeud_prec "+noeud_prec+" avec un arc de tps de traversée de "+temps_traverse );
				events.create_event(noeud_actuel, noeud_prec,temps_traverse, taux_max);
				events.print_tabEvents() ;
				
				//Recup le temps d'arrivée

			}
		int resultat = events.get_tempsEvac() ;
		return resultat ;
	}
	
	//On fait démarrer les noeuds d'evac à la suite dès que chacun est complétement vidé
	public int borne_sup() {
		
		//Events events  = new Events(file,noeud_courant, temps_premier,temps_dernier, taux_max) ; //On lance la simulation
		int borne_sup = 0 ;
		//Pour chaque chemin d'evacuation: on cherche à calculer son tps d'évacuation
		for(int chemin=0; chemin<file.get_nb_chemins() ;chemin++) {	
			int temps_debut = events.dernier_temps ;
			int temps_evac= evacuation_chemin(chemin, temps_debut) ;
			System.out.println("[BorneSup] Le chemin "+chemin+" s'est evacué à "+temps_evac);
			borne_sup = events.dernier_temps ;

		}
		System.out.println("La borne sup est de "+borne_sup);
		return borne_sup ;
	}
	
	//C'est le temps d'evac le plus grand des chemins (indépendament, sans prendre en compte le fait que le taux_Max devra certainement etre plus bas 
	//car il y aura des chemins qui se croiseront)
	public int borne_inf() {
		
		int borne_inf = 0 ;
		for(int chemin=0; chemin<file.get_nb_chemins() ;chemin++) {	
			int temps_evac= evacuation_chemin(chemin,0) ;
			
			//Si ce temps d'evac est plus grand que celui que l'on a déja, on le garde car on ne pourra jamais faire moins à cause de ce chemin
			if (borne_inf < temps_evac) {
				borne_inf = temps_evac ;
			}
		}
		System.out.println("La borne inf est de "+borne_inf);
		return borne_inf ;
	}
	
}
