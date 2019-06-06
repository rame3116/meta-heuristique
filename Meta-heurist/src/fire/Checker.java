package fire;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Checker {

	private String file;
	ArrayList<ArrayList<Integer>> tabChecker = new ArrayList<ArrayList<Integer>>();	
	private int compteur_ligne = 0;
	private int derniere_ligne = 9999 ;
	private int temps_evac ;

	private int nb_check; // Nb de noeuds d'evac à vérifier

	//Données de tableau d'arcs et chemins
	public ArrayList<ArrayList<Integer>> tabChemins = new ArrayList<ArrayList<Integer>>();
	public ArrayList<ArrayList<Integer>> tabArcs = new ArrayList<ArrayList<Integer>>();
	
	public Checker(String file1, Data data) throws Exception {
		parser(file1) ;
		file = file1 ;
		tabChemins = data.get_tabChemins() ;
		tabArcs = data.get_tabArcs() ;
		
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
				System.out.println("Nous avons "+nb_check+" et le temps à respecter est de "+temps_evac);
				System.out.println("Le dernier noeud Test inséré est "+tabChecker.get(nb_check-1).get(0));
				
	}

	public int simulation() {


		int chemin_nbNoeuds = file.get_nbNodesChemin(chemin) ;
		int taux_max = file.tauxMax_chemin(0) ;	//Taux max pour ce noeud d'evacuation
		
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
		
		
		//On importe le fichier source contenant les tableaux de chemins et d'arc
		Events events = new Events ();
		return 0 ;
	}
}
