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

	private int nb_check; // Nb de noeuds d'evac à vérifier

	//Données de tableau d'arcs et chemins
	public ArrayList<ArrayList<Integer>> tabChemins = new ArrayList<ArrayList<Integer>>();
	public ArrayList<ArrayList<Integer>> tabArcs = new ArrayList<ArrayList<Integer>>();
	
	public Checker(String file1, Data data, Bornes bornes2) throws Exception {
		parser(file1) ;
		file = file1 ;
		bornes = bornes2 ;
		tabChemins = data.get_tabChemins() ;
		tabArcs = data.get_tabArcs() ;
		
		parser(file) ;
		if (simulation()!=-1) {	//Le temps est respecté
			if (test_capa()!=-1) {
		
			}
			//events.print_tabEvents();
		}
		
	}
	
	public void parser(String file1) throws Exception, IOException {
		
		//Récupérer le fichier
		file =  "./../../meta-heuristique/InstancesInt/"+file1 ;
		String file2 = "./../../Meta_Heuristique/InstancesInt/" + file1 ;
		
		//Lecture dans le fichier
		try {
			BufferedReader br = new BufferedReader(new FileReader(file2));
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
		
		//Print de vérif:
		//System.out.println("Nous avons "+nb_check+" et le temps à respecter est de "+temps_evac);
		//System.out.println("Le dernier noeud Test inséré est "+tabChecker.get(nb_check-1).get(0));
		
	}

	public int simulation() {

		//Boucle sur ts les noeuds d'evac à traiter
		for (int index_evac=0; index_evac<nb_check-1;index_evac++) {
			//On lance la simulation
			//int temps_debut = events.dernier_temps ;
			int temps_evac= bornes.evacuation_chemin(index_evac, get_tempsDebut(index_evac)) ;
			System.out.println("[Simulation Checker] Le chemin "+index_evac+" s'est evacué à "+temps_evac+" en commencant à "+get_tempsDebut(index_evac));
			//borne_sup = borne_sup + temps_evac ;
			//events.print_tabEvents();

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
		
		return 0 ;
	}
	
	//  	Getteurs		//

	//Get date_debut d'un d'evac en ft de son index
	public int get_tempsDebut(int index) {
		return tabChecker.get(index).get(1) ;
	}
}
