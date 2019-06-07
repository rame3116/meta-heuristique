package fire;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Checker {

	private String file;
	private Bornes bornes ;
	private Events events ;
	ArrayList<ArrayList<Integer>> tabChecker = new ArrayList<ArrayList<Integer>>();	
	private int compteur_ligne = 0;
	private int derniere_ligne = 9999 ;
	private int temps_evac ;
	private Data data ;

	private int nb_check; // Nb de noeuds d'evac à vérifier

	//Données de tableau d'arcs et chemins
	public ArrayList<ArrayList<Integer>> tabChemins = new ArrayList<ArrayList<Integer>>();
	public ArrayList<ArrayList<Integer>> tabArcs = new ArrayList<ArrayList<Integer>>();
	
	public Checker(String file1, Data data2, Bornes bornes2) throws Exception {
		parser(file1) ;
		file = file1 ;
		bornes = bornes2 ;
		data = data2 ;
		tabChemins = data.get_tabChemins() ;
		tabArcs = data.get_tabArcs() ;
		
		parser(file) ;
		if (simulation()!=-1) {	//Le temps est respecté
			if (test_capa()==-1) {
				System.out.println("SOLUTION NON VALIDE");
			}
			else System.out.println("SOLUTION VALIDE");

		}
		//events.print_tabEvents();

	}
	
	public void parser(String file1) throws Exception, IOException {
		
		//Récupérer le fichier
		file =  "./../../meta-heuristique/InstancesInt/"+file1 ;
		String file2 = "./../../Meta_Heuristique/InstancesInt/" + file1 ;
		
		//Lecture dans le fichier
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			
			while ((line = br.readLine()) != null && compteur_ligne<=derniere_ligne) {
				compteur_ligne++;
				String[] mots = line.split(" ");	//La ligne courante
	
				
				// 1ere ligne info
				if (compteur_ligne == 2) {
					//System.out.println("1ere ligne");
					nb_check = Integer.parseInt(mots[0]);
				}
				//On recopie les noeuds d'evac + leurs infos
				else if (compteur_ligne > 2 && compteur_ligne<nb_check+3) {	//+2 A cause du decalage de 2 (2 1ères lignes de données) et +1 car on commence à l'indice 1
					
					//Données relative à ce noeud d'evac à test
					ArrayList<Integer> c1 = new ArrayList <>() ;	
					c1.add(Integer.parseInt(mots[0]));	//Noeud initial, figé
			    	c1.add(Integer.parseInt(mots[1]));	//Taux d'évacation
			    	c1.add(Integer.parseInt(mots[2]));	//Heure de début
			    	tabChecker.add(c1) ;				//On l'ajoute au tab du checker 
				}
				//Ligne contenant le temps auquel l'evacuation est censée finir
				else if (compteur_ligne == nb_check+4) {
					temps_evac = Integer.parseInt(mots[0]);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {

		}

	}

	public int simulation() {

		//Boucle sur ts les noeuds d'evac à traiter
		for (int index_evac=0; index_evac<nb_check;index_evac++) {
			//On lance la simulation
			//int temps_debut = events.dernier_temps ;		
			int temps_evac= bornes.evacuation_chemin(index_evac, get_tempsDebut(index_evac)) ;
			//System.out.println("[Simulation Checker] Le chemin "+index_evac+" s'est evacué à "+temps_evac+" en commencant à "+get_tempsDebut(index_evac));


		}
		events = bornes.events ;
		
		//On calcul le temps auquelle est arrivé le dernier paquet, si ca respecte le temps de la solution alors ok, sinon renvoie -1
		int temps_simulation = events.get_tempsEvac() ;
		System.out.println("L'evacuation se fait en "+temps_simulation+" et doit etre inf à "+temps_evac);
		if (temps_simulation>temps_evac) {	
			temps_simulation = -1 ;
		}
		return temps_simulation ;
	}

	public int test_capa () {
		
		int error = 0 ; 	//Pr tester la fin 
		int nb_noeuds=events.get_nbNoeuds() ;
		
		//On itére sur tt les noeuds qui ont été parcourus durant la simulation
		for (int num_noeud=0; (num_noeud<nb_noeuds-1 && error!=-1 ); num_noeud++) {
			
			if 	(num_noeud != events.get_indexNoeud(data.get_noeud_safe())) {
				
				int id_noeud = events.get_noeudIndex(num_noeud) ;
				//System.out.println("[[[[[[[TEST_NODE_SUIVANT] Traitement de "+id_noeud);
				//On determine la capacité de l'arc sortant du noeud qu"on traite 
				int noeud_suivant = data.get_suivant(id_noeud) ;
				int capa_arc = data.get_capaArc(id_noeud,noeud_suivant);
				//System.out.println("[[[[[[[TEST_NODE_SUIVANT] Le noeud suivant de "+id_noeud+" est: "+noeud_suivant+", capa= "+capa_arc);
				int nb_events= events.get_nbEventIndex(num_noeud) ;
				//System.out.println("[TestCapa] Events du noeud "+id_noeud+" ayant "+nb_events+" events") ;
				
				
				//On itère sur les evenements qu'ils recoient 
				for (int num_event=1; num_event<nb_events; num_event ++ ) {
					
					int taux_actuel = 0 ;

					
					//Pour le dernier evnt, on ne check pas si l'event suivant est en meme temps car il n'e
					if (num_event==nb_events) {
						taux_actuel = taux_actuel + events.get_tauxEvent(num_noeud, num_event) ;

					}
					else {
						//Cet event n'est pas suivi d'un autre qui est en meme temps
						if (events.get_tempsEvent(num_noeud, num_event) != events.get_tempsEvent(num_noeud, num_event+1)) {
							taux_actuel = taux_actuel + events.get_tauxEvent(num_noeud, num_event) ;
						}
						//2 events en mm temps: on les somme entre eux avant de l'additionner au taux
						else {
							int fusion = events.get_tauxEvent(num_noeud, num_event) + events.get_tauxEvent(num_noeud, num_event+1) ;
							taux_actuel = taux_actuel + fusion ;
							taux_actuel ++ ;
							//System.out.println("[TestCapa] Fusion: noeud "+id_noeud+", events de taux: "+events.get_tauxEvent(num_noeud, num_event)+" et "+events.get_tauxEvent(num_noeud, num_event+1)+", taux_actuel: "+taux_actuel);
						}
					}
						
					//Test si le flux actuel a  dépassé la capa
					if (taux_actuel > capa_arc) {
						error=-1 ;			
						//System.out.println("[TestCapa] La capa du noeud "+id_noeud+" explose: taux de "+taux_actuel+" et capa de "+capa_arc) ;
					}
				}

		}
		}
			
			
			
		return error ;
	}
	
	//  	Getteurs		//

	//Get date_debut d'un d'evac en ft de son index
	public int get_tempsDebut(int index) {
		return tabChecker.get(index).get(2) ;
	}
}
