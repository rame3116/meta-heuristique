package fire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Events {
	
		Data data ;		//Contient tout les tableaux de chemins et d'arcs
	
		public int taille_tab = 0 ;	//Taille du tab, utile pour le print du tab
		public int dernier_temps = 0 ;
		
		//Tableau contenant tout les noeuds, chaque noeud contient une liste de couple (tps,flot sortant)
		ArrayList<ArrayList<ArrayList<Integer>>> tabEvents = new ArrayList<ArrayList<ArrayList<Integer>>>(); //Le 1er indice contient le
		
		//Exp: 	// 	 (id_node:1, nb_event=1)	//	(id_node2, nb_event1=2)	//	(id_node3, nb_event=0) //
		//		____________________________________________________________________________________________
		//					(5,+5)				//	(10,-6)		//				
		//		___________________________________________________________________________________________
												//	(19,+9)		//		
		//Chaque event est un couple (temps arrivée, variation de flot)
		
		
		public Events() {
			
		}
	
		public void first_event(Data file, int premier_noeud, int temps_debut, int temps_fin, int taux, int chemin) {
			
			data = file ;
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
			tabEvents.get(get_indexNoeud(premier_noeud)).get(0).set(1,2) ;	//1er get0 pour le 1er noeud, 2e get0 pour l'entete, set(1,2): place 2 à l'index 1 (nb_event)

		}
		
		
		public ArrayList<ArrayList<ArrayList<Integer>>> get_tabEvents(){
			return tabEvents ;
		}


		public void create_event(int noeud, int noeud_prec, int temps_traverse, int taux) {	//Nous avons juste besoin de connaitre le tps de traversé suite aux events des noeuds prec
			
			//On test d'abord si le node n'a pas de colonne qui lui est propre comme ds l'exmple
			//On doit itérer sur la 1ère dimension du tab (une colonne par noeud)
			int existe=0 ;		
			
			for (int i=0; i<taille_tab; i++) {	//Itérateur sur le tab
		    	  //Ce noeud existe déja ds le tab d'evenement
		    	  if (noeud == get_noeudIndex(i)) {	
				       existe=1 ;
				       break ;
		    	  }
			}
			if (existe==0) {
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
					
					//On vérifie si cet event existe déja pour savoir si on l'ajoute ou pas
					if (!tabEvents.get(index_noeud).contains(event)) {
						insert_event(index_noeud, event) ;
					}
				}	  

	      }
    	  else {
    		  System.out.println("[AddEvent] RECUP PREC: ERROOOR !! Pour le noeud "+noeud+", son noeud prec "+noeud_prec+"n'est pas ds le tab") ;
    	  }
		
	    }
		
		//On l'insère de tel sorte à ce que la liste d'evnt soit bien ordonné en ft de la chronologie
		public void insert_event(int index_noeud,ArrayList<Integer> event) {
			
			//On vérifie si ce noeud n'a pas d'evenements
			if(tabEvents.get(index_noeud).size()==1) {	//et pas ==0 à cause de l'entete qui utilise une case de la liste d'events
				tabEvents.get(index_noeud).add(event) ;
				inc_nbEventIndex(index_noeud) ;

			}
			//Il y a au moins 1 autre événement, et on vt les classer: les + récents (temps +grand) à droite
			else {
				//On parcourt les autres events pour placer celui-là
				int c ; //Index
				int stop=0 ;	//Pr arreter la boucle for car il ne vt pas 2 breaks
				for (c=1; (c<get_nbEventIndex(index_noeud)+1 && stop==0); c++) {
					
					//Si cet evnt est plus ancien que celui que l'on traite
					if (event.get(0) <= get_tempsEvent(index_noeud,c)){
						tabEvents.get(index_noeud).add(c,event) ;
						inc_nbEventIndex(index_noeud) ;		
						stop=1 ;
					}
					//S'il y a déja un evenement au meme temps, on somme alors les taux à l'aide de la méthode fusionn
					//else if (get_tempsEvent(index_noeud,c)  == get_tempsEvent(index_noeud,c+1)){
					/*if (event.get(0) == get_tempsEvent(index_noeud,c)){

						System.out.println("[Insert_event] Meme event: Pr le noeud "+get_noeudIndex(index_noeud)+" il y a un event de mm temps: "+event.get(0)+" avec un taux "+event.get(1));
						int nv_taux = event.get(1)+get_tauxEvent(index_noeud,c) ;
						System.out.println("[Insert_event] Meme event: Le taux du nv event est de "+event.get(1)+" et celui de l'ancien: "+get_tauxEvent(index_noeud,c)+" et leur somme "+nv_taux) ;
						//tabEvents.get(index_noeud).get(c).set(1, nv_taux) ;
						
						//Créer un nv event
						///ArrayList <Integer> nv_event = new ArrayList ();
						//nv_event.add(get_tempsEvent(index_noeud,c)) ;	//Le meme tps que av
						//nv_event.add(nv_taux) ;							//Nv taux
		
						//On supp l'ancien
						//tabEvents.get(index_noeud).remove(c+1) ;
						//tabEvents.get(index_noeud).remove(c+1) ;

						//On l'ajoute à l'index donné

						//tabEvents.get(index_noeud).add(nv_event);

						//tabEvents.set(index_noeud,tabEvents.get(index_noeud)) ;

						//inc_nbEventIndex(index_noeud) ;	
								
						stop=1 ; 			
						//fusion_events(index_noeud,c) ;
					} 	*/
					
				}
				//Pr savoir si on l'a intégré ou pas, si il est plus grand que ts, alr on l'add à la fin
				if (stop==0){
					tabEvents.get(index_noeud).add(event) ;
					inc_nbEventIndex(index_noeud) ;
					//System.out.println("[Insert_event] Noeud "+get_noeudIndex(index_noeud)+" est ajouté à la fin car son temps "+event.get(0)+" est sup à "+get_tempsEvent(index_noeud,c-1));
				}
			}
    		//System.out.println("[AddEvent] Ajout de l'evenement ("+nouveau_temps+","+taux_prec+") à correspondant au num "+p+" des noeuds_prec") ;
		
		}
		
		//Fusion pour fusionner 2 evnt de meme temps (Non fonctionnelle)
		public void fusion_events(int index_noeud, int index_event) {
			
			int temps = get_tempsEvent(index_noeud,index_event) ;
			int taux1 = get_tauxEvent(index_noeud,index_event) ;
			int taux2 = get_tauxEvent(index_noeud,index_event+1) ;
			int nv_taux = taux1 + taux2 ;
			//System.out.println("[fusion_events]Noeud "+get_noeudIndex(index_noeud)+": 1er event_taux: "+taux1+" et 2e "+taux2+" au temps "+temps+", leur somme sera "+nv_taux);
			//System.out.println("[fusion_events]Noeud "+get_noeudIndex(index_noeud)+": le 1er est à l'index="+index_event);
			//On crée un nv event (temps,nv_taux)
			ArrayList<Integer> nv_event = new ArrayList<Integer>();
			nv_event.add(temps) ;
			nv_event.add(nv_taux) ;
			
			tabEvents.get(index_noeud).remove(index_event+1) ;
			tabEvents.get(index_noeud).add(index_event+1, nv_event);
			tabEvents.get(index_noeud).remove(index_event) ;
			
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
				for (int e=1; e<nb_event+1; e++) {
					System.out.println("Evenement n°"+e+": Temps sortie du 1er paquet: "+get_tempsEvent(i,e)+" et taux: "+get_tauxEvent(i,e)) ;
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
		public void dec_nbEventIndex(int index_tab) {		//On décrémente le nb d'events
			int nv_nb = get_nbEventIndex(index_tab) - 1;
			tabEvents.get(index_tab).get(0).set(1,nv_nb) ;
		}
		
			//Get résultat (Temps total d'evac du chemin
		public int get_tempsEvac() {
			
			//On cherche l'index du dernier event du noeud d'evac qui contient notre temps de fin
			int noeud_evac = data.get_noeud_safe() ;	//Id node evac
			int index_noeudEvac = get_indexNoeud(noeud_evac) ;
			ArrayList<ArrayList<Integer>> events_noeudFin = new ArrayList<ArrayList<Integer>>();
			events_noeudFin = tabEvents.get(index_noeudEvac) ;
			int index_dernierEvent = events_noeudFin.size() ;
			//System.out.println("GET TEMPS EVAC: dernier_event: "+index_dernierEvent) ;
			int resultat = get_tempsEvent(index_noeudEvac,index_dernierEvent-1) ;
			dernier_temps = resultat ;		//On met à jour dernier temps pour pouvoir le récupérer à la fin d'un traitement d'un chemin
			return resultat ;	
					
		}
		
			//Retourne le nb de noeuds qui ont été parcourus
		public int get_nbNoeuds () {	
			return tabEvents.size() ;
		}
		//Trier les evenements d'un noeud donné pour construire une sorte de graphique de taux pour voir si la capa a été dépassé
		

}
