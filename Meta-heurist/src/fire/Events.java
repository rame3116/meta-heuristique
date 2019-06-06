package fire;

import java.util.ArrayList;
import java.util.Iterator;

public class Events {
	
		Data data ;		//Contient tout les tableaux de chemins et d'arcs
	
		public int taille_tab = 0 ;	//Taille du tab, utile pour le print du tab
	
		//Tableau contenant tout les noeuds, chaque noeud contient une liste de couple (tps,flot sortant)
		ArrayList<ArrayList<ArrayList<Integer>>> tabEvents = new ArrayList<ArrayList<ArrayList<Integer>>>(); //Le 1er indice contient le
		//Exp: 	// 	 (id_node, nb_event)	//		2		//		3		//
		//		__________________________________________
		//					(5,+5)			//	(10,-6)		//				
		//		__________________________________________
											//	(19,+9)
		
		public Events(Data file, int premier_noeud, int temps_debut, int temps_fin, int taux) {
			
			
			Data data = file ;
			int chemin_nbNoeuds = file.get_nbNodesChemin(0) ;
			int taux_max = file.tauxMax_chemin(0) ;	//Taux max pour ce noeud d'evacuation
			/*
			
						//	Gestion du 1er arc	//
			//_____________________________________________________________________________//
			
			//Gérer la population sortant du noeud d'évac
			int temps_premier=0 ;							//On effectue le 1er passage à t=0
			int population = file.get_popChemin(chemin);	//Population à évacuer de ce noeud
			
			//L'arc que l'on traite
			int noeud_courant = tabChemins.get(chemin).get(4) ;		//Noeud courant
			int noeud_suivant = tabChemins.get(chemin).get(5) ;		//Noeud courant

			//Nb de passage et le temps auquel ils vont ts arriver
			int nb_passageArc = population/taux_max ;
			int temps_dernier = 	temps_premier + nb_passageArc ;		//Tps dernier envoi du noeud d'evac
			int capa_arc = file.get_capaArc(noeud_courant, noeud_suivant, chemin) ;	//Non utile (car déja tauxMax est censé etre < ou = à chaque capa, mais pt etre utile pr le debug)
				
			
			
			*/
			
			//On crée le 1er array (1 dimension) contenant le nom du noeud puis un array (un dimension) pour le 1er noeud
			
			//1ère ligne de la 1ère colonne de notre tab contenant le nom du noeud, puis le nb d'evenement
			ArrayList <Integer> nom_noeud = new ArrayList <Integer> () ;
			nom_noeud.add(premier_noeud) ;		//Id noeud
			nom_noeud.add(0) ;					//Nb events
			//nom_noeud.add(2) ;
			
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
			
			//On ajoute cet array au tab général
			tabEvents.add(noeud1) ;
			
			taille_tab ++ ;
			tabEvents.get(0).get(0).set(1,2) ;	//1er get0 pour le 1er noeud, 2e get0 pour l'entete, set(1,2): place 2 à l'index 1 (nb_event)
			//print_tabEvents();

		}
		
		
		public ArrayList<ArrayList<ArrayList<Integer>>> get_tabEvents(){
			return tabEvents ;
		}

		public void add_event(int noeud, int noeud_prec, int temps_traverse, int taux) {	//Nous avons juste besoin de connaitre le tps de traversé suite aux events des noeuds prec
			
			//On test d'abord si le node n'a pas de colonne qui lui est propre comme ds l'exmple
			//On doit itérer sur la 1ère dimension du tab (une colonne par noeud)
			int existe=0 ;		
			
			for (int i=0; i<taille_tab; i++) {	//Itérateur sur le tab
		    	  //Ce noeud existe déja ds le tab d'evenement
		    	  if (noeud == get_noeudIndex(i)) {	
				       System.out.println("Le noeud "+noeud+" existe déja dans la tab");
				       break ;
		    	  }
			}
			if (existe==0) {
				//System.out.println("Le noeud "+noeud+" est nv");
		    	//On le créer alr  
		    	//1ère ligne de la 1ère colonne de notre tab contenant le nom du noeud, puis le nb d'evenement
				ArrayList <Integer> nom_noeud = new ArrayList <Integer> () ;
				nom_noeud.add(noeud) ;				//Id noeud
				nom_noeud.add(0) ;					//Nb events
				//On crée un array x array contenant tout ce qui relatif au 1er noeud (les 3 array plus haut)
				ArrayList<ArrayList<Integer>> noeud1 = new ArrayList<ArrayList<Integer>>();
				noeud1.add(nom_noeud) ;
				tabEvents.add(noeud1) ; //On l'ajoute au tab général 	
				taille_tab ++ ;
			}
			
		int index_noeud = get_indexNoeud(noeud) ;	//L'index du noeud ds le tab, mnt qu'on est sur qu'il existe

			
    	  		//Récupérer les events précédents:
    	  
    	  //Vérif si son noeud prec existe bien au cas où
    	  int index_noeudPrec = get_indexNoeud(noeud_prec) ;
    	  if (index_noeudPrec!= -1) {		//Il existe déja 
    		  
	    		//Boucle for sur les evnts de son noeud prec
	    		for (int p=1; p<get_nbEventIndex(index_noeudPrec)+1; p++) {	//On commence par 1 car la ligne n'est pas un event mais (id node, nb events)
	    			int temps_event = get_tempsEvent(index_noeudPrec,p) ;
	    			int nouveau_temps = temps_event + temps_traverse ;	//Ce noeud l'a recu à ce temps: Tps d'emission + propagation
	    			int taux_prec = get_tauxEvent(index_noeudPrec,p) ;	///Taux de l'event du noeud prec
	    			
	    			//On lui crée son nouveau event
					ArrayList <Integer> event = new ArrayList <Integer> () ;
					event.add(nouveau_temps) ;
					event.add(taux_prec) ;
					tabEvents.get(index_noeud).add(event) ;
					inc_nbEventIndex(index_noeud) ;
		    		//System.out.println("[AddEvent] Ajout de l'evenement ("+nouveau_temps+","+taux_prec+") à correspondant au num "+p+" des noeuds_prec") ;
	    		}	  

	      }
    	  else {
    		  System.out.println("[AddEvent] RECUP PREC: ERROOOR !! Pour le noeud "+noeud+", son noeud prec "+noeud_prec+"n'est pas ds le tab") ;
    	  }
		
	    }
			
		
	
		public void print_tabEvents() {
			
			//Boucle pour chaque colonne du tab
			for (int i=0; i<taille_tab; i++) {
				//System.out.println("[PrintTabEvents: ligne "+i+" et taille_tab: "+taille_tab) ;
				int noeud = get_noeudIndex(i) ;
				int nb_event = get_nbEventIndex(i) ;
				//Print le num de la ligne 
				System.out.println("Ligne du tabEvents n°"+i+" correspondant au noeud "+noeud+" ayant "+nb_event+" events") ;
				
				//Boucle for sur le nb d'evnt:
				for (int e=0; e<nb_event; e++) {
					System.out.println("Evenement n°"+(e+1)+": Temps sortie du 1er paquet: "+get_tempsEvent(i,e+1)+" et taux: "+get_tauxEvent(i,e+1)) ;
					//e+1 car le 1er event est en réalité une entete avec (id noeud, nb events)
				}

			}
		}
			
				//Getteurs pour une colonne donnée (son noeud et le nb d'events)
		
		//Retourne le nom du noeud correspondant à un index donné du tableau d'events
		public int get_noeudIndex(int index_tab) {
			return tabEvents.get(index_tab).get(0).get(0) ;
		}
		//Retourne le nb d'events correspondant à un index donné du tableau d'events
		public int get_nbEventIndex(int index_tab) {
			return tabEvents.get(index_tab).get(0).get(1) ;
		}
		
				// Getteurs pour un evenement donné
		
		//Retourne le temps de sortie d'un paquet d'un event donnée
		public int get_tempsEvent(int index_tab, int index_event) {
			return tabEvents.get(index_tab).get(index_event).get(0) ;
		}
		//Retourne le taux de sortie d'un paquet d'un event donnée
		public int get_tauxEvent(int index_tab, int index_event) {
			return tabEvents.get(index_tab).get(index_event).get(1) ;
		}
		
				// Getteur pour noeud donné
		
		//Retourne l'index ds le tab d'events d'un noeud en ft de son id 
		public int get_indexNoeud(int noeud) {
			int retour=-1 ;
			for (int i=0; i<taille_tab; i++) {	//Itérateur sur le tab
		    	  //Ce noeud existe déja ds le tab d'evenement
		    	  if (noeud == get_noeudIndex(i)) {	//
		    		  retour = i ;
		    		  break ;
		    	  }
		    	  else {
			    	//System.out.println("[Get_IndexNoeud] ERROR !! le noeud "+noeud+" n'existe pas");
		    	  }
			}
			return retour ;
		
		}
	
				//Setteur pour event
		public void set_nbEventIndex(int index_tab, int nb_event) {		//On modifie le nb d'events
			tabEvents.get(index_tab).get(0).set(1,nb_event) ;
		}
		public void inc_nbEventIndex(int index_tab) {		//On incrémente le nb d'events
			int nv_nb = get_nbEventIndex(index_tab) + 1;
			tabEvents.get(index_tab).get(0).set(1,nv_nb) ;
		}
		
			//Get résultat (Temps total d'evac du chemin
		public int get_tempsEvac() {
			
			//On cherche l'index du dernier event du noeud d'evac qui contient notre temps de fin
			ArrayList<ArrayList<Integer>> events_noeudFin = new ArrayList<ArrayList<Integer>>();
			events_noeudFin = tabEvents.get(taille_tab-1) ;
			int index_dernierEvent = events_noeudFin.size() ;
			//System.out.println("GET TEMPS EVAC: dernier_event: "+index_dernierEvent) ;
			int resultat = get_tempsEvent(taille_tab-1,index_dernierEvent-1) ;
			return resultat ;	
					
		}

}
