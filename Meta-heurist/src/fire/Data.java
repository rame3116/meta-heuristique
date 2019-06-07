package fire;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//Chaz
///home/rahmoun/4IR/Meta-heuristique/meta-heuristique/Instances
public class Data {
	
	private String file;
	ArrayList<ArrayList<Integer>> tabChemins = new ArrayList<ArrayList<Integer>>();
	ArrayList<ArrayList<Integer>> tabArcs = new ArrayList<ArrayList<Integer>>();
	

	private int compteur_ligne = 0;
	private int derniere_ligne = 9999 ;

	private int nb_noeuds; // Nb noeuds total
	private int nb_arcs; // Nb arcs total
	
	
	private int nb_chemins; // nombre de sommets à évacuer
	private int noeud_safe; // noeud à atteindre



	private int[][] tab_chemins = new int[10][200];
	private int[][] tab_arcs = new int[1000][5];

	
	public Data(String file1) throws IOException {
		file =  "./../../meta-heuristique/InstancesInt/"+file1 ;
		String file2 = "./../../Meta_Heuristique/InstancesInt/" + file1 ;
		

		try {
			BufferedReader br = new BufferedReader(new FileReader(file2));
			String line;
			int cpt_arcs = 0 ;
			int type=0 ; 	//type=0 par déf,=1 qd il faut relever des chemins, =2 pour la ligne ac le nb d'arcs, =3 pour relever des arcs
			while ((line = br.readLine()) != null && compteur_ligne<=derniere_ligne) {
				compteur_ligne++;
				String[] mots = line.split(" ");	//La ligne courante
	
				//TEST DBUG
				//System.out.println("TEST: "+mots[1]) ;
				//System.out.println("type="+type);
				
				// 1ere ligne info
				if (compteur_ligne == 2) {
					//System.out.println("1ere ligne");
					nb_chemins = Integer.parseInt(mots[0]);
					noeud_safe = Integer.parseInt(mots[1]);
					type=1 ;
				}
				
				//Ligne ne contenant pas d'infos mais indiquant que la prochaine ligne contient le nb d'arcs
				else if (compteur_ligne > 2 && mots[1].startsWith("[graph") ) {
					//System.out.println("Passage aux arcs");
					type = 2 ;
				}
				
				
				// Chemins jusqu'aux noeuds finaux
				else if (type==1) {
					
					//Données relative au chemin
					ArrayList<Integer> c1 = new ArrayList <>() ;
					c1.add(Integer.parseInt(mots[0])); 	//id noeud évacuation
					c1.add(Integer.parseInt(mots[1]));	//population à évacuer
					c1.add(Integer.parseInt(mots[2]));	//Taux max
					c1.add(Integer.parseInt(mots[3]));	//k (Nb noeuds sur le chemin, en plus du noeud d'evac)
					c1.add(Integer.parseInt(mots[0])); 	//1er noeud encore, comme ca on aura une succéssivité encore le noeud initial puis le reste du chemin
					
					

					//Le chemin (depuis le 2e noeud, le 1er étant ré-inséré juste avant)
					for (int i = 0; i < c1.get(3); i++) {
						c1.add(Integer.parseInt(mots[i + 4]));															
					}
					
					//Ajout de cette ligne au tableau
					tabChemins.add(c1) ;
					
					//Print données du chemin
					//System.out.println("noeud d'evacuation : "+c1.get(0)+ " / population : "+c1.get(1)+" / taux max : "+c1.get(2)+" / nombre de noeuds à parcourir : "+c1.get(3)+" / noeud S :"+c1.get(nb_noeuds+3));					
					// print le chemin
					/*for (int i = 0; i < nb_noeuds; i++) {
						System.out.println("Noeud "+i+" : "+tabChemins.get(compteur_ligne-3).get(i+4));															
					}*/
					
				}
				
				
				//Ligne indiquant que l'on traite la ligne contenant le nb d'arcs
				else if (type==2) {
					nb_noeuds = Integer.parseInt(mots[0]);
					nb_arcs = Integer.parseInt(mots[1]);
					//System.out.println("Nb noeuds: "+nb_noeuds+" et nb arcs: "+nb_arcs);
					derniere_ligne = compteur_ligne + nb_noeuds ;
					type = 3 ;
				}
				
				// Arcs
				else if (type==3) {
					//System.out.println("Ajout de l'arc: "+mots[0]+" et "+mots[1]);
					ArrayList<Integer> a1 = new ArrayList <>() ;
					a1.add(Integer.parseInt(mots[0])) ;		//Noeud1
					a1.add(Integer.parseInt(mots[1])) ;		//Noeud2
					if (mots[2].equals("9223372036854775807")) {
						a1.add(Integer.MAX_VALUE);
					} else {
						a1.add(Integer.parseInt(mots[2]));// duetime
					}
					a1.add(Integer.parseInt(mots[3]));// travel time
					a1.add(Integer.parseInt(mots[4]));// capacité

					//Add to the tab
					tabArcs.add(a1) ;
					//Print
					//System.out.println("[tabArcs] Get arc n°"+cpt_arcs+" : "+tabArcs.get(cpt_arcs).get(0)+" et "+tabArcs.get(cpt_arcs).get(1));
					//cpt_arcs ++ ;
				}
	
		}
			
		br.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		// System.out.println("Tab arcs : "+tab_arcs);
		// e.printStackTrace();
	}

}

	public ArrayList<ArrayList<Integer>> get_tabArcs(){
		return tabArcs ;
	}
	public ArrayList<ArrayList<Integer>> get_tabChemins(){
		return tabChemins ;
	}
	
	//Getteurs de données générales liées au tableau
	public int get_nb_chemins() {
		return nb_chemins ;
	}
	
	public int get_noeud_safe() {
		return noeud_safe ;
	}
	
	//Getteurs spécifiques 
	
	// Chercher l'index dans le tab_arcs tel que les 2 noeuds correspondent +
	// Retourner capa
	public int get_capaArc(int node1, int node2, int num_chemin) {

		int retour = -1 ;
		//int nb_nodes = tabChemins.get(num_chemin).get(3) ;	//Le nb de noeuds sur ce chemin
		
		//System.out.println("[Get_CapaArc] Recherche de l'arc (" + node1 + ", " + node2 + ")") ;
	
		// On recherche un arc (node1, node2)
		int index_i = 0;
		while (((tabArcs.get(index_i).get(0) != node1) || (tabArcs.get(index_i).get(1)!= node2)) && index_i < nb_arcs) {
			//System.out.println("[Get_CapaArc] Comparaison entre: " + node1 + " / " +tabArcs.get(index_i).get(0)+" et "+ node2 + " / "+tabArcs.get(index_i).get(1));
			index_i++;
		}
		retour = tabArcs.get(index_i).get(4) ;	//On récupère la capa


		// Si c'est l'index qui sort du While, c'est qu'il n'a pas trouvé d'arc
		if (index_i == nb_arcs) { 
			//System.out.println("[Get_CapaArc] L'arc (" + node1 + ", " + node2 + ") n'existe pas ");
			index_i = 0; // On cherche l'arc (node2, node1) mnt, car les arcs ne st pas orientés
			while (((tabArcs.get(index_i).get(0) != node2) || (tabArcs.get(index_i).get(1) != node1)) && index_i < nb_arcs) {
				index_i++;
			}
			retour = tabArcs.get(index_i).get(4) ;	//On récupère la capa
			
			if (index_i == nb_noeuds-1) { // S'il parcourt tt sans rien trouver c'est que l'arc n'existe pas
				System.out.println("[Get_CapaArc] [ERROR !!] L'arc (" + node1 + ", " + node2
						+ ") n'existe pas qlque soit le sens ");
			}
		}
		

		return retour; 	//-1 si l'arc n'existe pas. Ce cas ne devrait jamais arriver car les 2 noeuds sont succéssifs selon le chemin
						//C'est pour cela que nous ne testons si =/= mais il y a tjs un print Error au cas où
}
	
	
	// Chercher l'index dans le tab_arcs tel que les 2 noeuds correspondent +
		// Retourne le temps de traversée
	public int get_tempsArc(int node1, int node2, int num_chemin) {

			int retour = -1 ;
			//int nb_nodes = tabChemins.get(num_chemin).get(3) ;	//Le nb de noeuds sur ce chemin
			
			//System.out.println("[Get_TempsArc] Recherche de l'arc (" + node1 + ", " + node2 + ")") ;
		
			// On recherche un arc (node1, node2)
			int index_i = 0;
			while (((tabArcs.get(index_i).get(0) != node1) || (tabArcs.get(index_i).get(1)!= node2)) && index_i < nb_arcs) {
				//System.out.println("[Get_TempsArc] Comparaison entre: " + node1 + " / " +tabArcs.get(index_i).get(0)+" et "+ node2 + " / "+tabArcs.get(index_i).get(1));
				index_i++;
			}
			retour = tabArcs.get(index_i).get(3) ;	//On récupère le temps
			//System.out.println("[Get_tempsArc] Retour prend la val: "+retour+" après le 1er parcours et l'index s'est arreté à "+index_i);


			// Si c'est l'index qui sort du While, c'est qu'il n'a pas trouvé d'arc
			if (index_i == nb_arcs) { 
				//System.out.println("[Get_TempsArc] L'arc (" + node1 + ", " + node2 + ") n'existe pas ");
				index_i = 0; // On cherche l'arc (node2, node1) mnt, car les arcs ne st pas orientés
				while (((tabArcs.get(index_i).get(0) != node2) || (tabArcs.get(index_i).get(1) != node1)) && index_i < nb_arcs) {
					index_i++;
				}
				retour = tabArcs.get(index_i).get(3) ;	//On récupère le temps
				
				if (index_i == nb_arcs) { // S'il parcourt tt sans rien trouver c'est que l'arc n'existe pas
					System.out.println("[Get_TempsArc] [ERROR !!] L'arc (" + node1 + ", " + node2
							+ ") n'existe pas qlque soit le sens ");
				}
				//System.out.println("[Get_tempsArc] Retour prend la val: "+retour+" après le 2e parcours");

			}
			
			//System.out.println("[Get_tempsArc] Résultat: "+retour);
			return retour; 	//-1 si l'arc n'existe pas. Ce cas ne devrait jamais arriver car les 2 noeuds sont succéssifs selon le chemin
							//C'est pour cela que nous ne testons si =/= mais il y a tjs un print Error au cas où
	}
		
	
	//Calcul le taux min d'un chemin donné en ft de son index dans le tab d'arcs
	public int tauxMax_chemin(int num_chemin) {
		
		int min_capa = 99999999 ;		//Min de départ
		
		for (int index_node=0; index_node<tabChemins.get(num_chemin).get(3); index_node++) {	//Boucle sur les noeuds d'un chemin
			
			//Les 2 noeuds succéssifs composants l'arc que l'on traite
			int noeud1 = tabChemins.get(num_chemin).get(index_node+4) ;	
			int noeud2 = tabChemins.get(num_chemin).get(index_node+5) ;
			int capa_arc = get_capaArc(noeud1, noeud2,num_chemin) ;
			//System.out.print("[TauxMin] Noeud1: "+noeud1+" et Noeud2: "+noeud2+" et de capa: "+capa_arc);
			//System.out.print("[TauxMin] DEBUG Noeud1: "+noeud1);

			
			//Test si c'est encore plus petit que notre min actuel
			//System.out.println("[tauxMin:capa] Chemin "+num_chemin+" : Comparaison entre son min "+min_capa+" et "+capa_arc);
			if (capa_arc<min_capa) {
				min_capa = capa_arc ;
				//System.out.println("[tauxMin] Maj du Chemin "+num_chemin+" qui passe à "+min_capa);
			}
		}
		//Affiche la ligne du chemin OK
		//System.out.println("[tauxMin:] Chemin n°"+num_chemin+" commencant par le noeud "+tabChemins.get(num_chemin).get(0)+" a un taux min de "+min_capa+" personnes/sec.") ;
						
				
		return min_capa ;
	}
	
	// Getteurs sur les chemins
	public int get_popChemin(int chemin) {
		return tabChemins.get(chemin).get(1) ;
	}
	public int get_tauxChemin(int chemin) {
		return tabChemins.get(chemin).get(2) ;
	}
	
	//Getteurs sur les noeuds d'un chemin
	public int get_nbNodesChemin(int chemin) {
		return tabChemins.get(chemin).get(3)+1;
	}
	
	public int get_idNode (int chemin, int index_noeud) {	//Retourne l'id d'un node en ft de son index dans le tab
		return tabChemins.get(chemin).get(index_noeud) ;
	}
	
	
	public int get_nextNode(int chemin, int index_noeud) {	//Dans un chemin donné, renvoie l'id du noeud d'un noeud à un à index donné
		return tabChemins.get(chemin).get(index_noeud+1) ;
	}
	public int get_precNode(int chemin, int index_noeud) {	//Dans un chemin donné, renvoie l'id du noeud d'un noeud à un à index donné
		return tabChemins.get(chemin).get(index_noeud-1) ;
	}
	
	
	public static void main(String[] args) throws Exception {	
		Data fichier1 = new Data("donnees") ;
		Bornes bornes = new Bornes (fichier1) ;
		//bornes.borne_sup() ;
		//bornes.borne_inf() ;
		Checker checker = new Checker("solution", fichier1,bornes) ;
		
	}
	
		

	
	
}