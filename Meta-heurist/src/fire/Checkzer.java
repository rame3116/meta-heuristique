package fire;

import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
public class Checkzer {
	
	private ArrayList<Integer> noeudsTest = new ArrayList <>() ;

	public Checkzer() {
	
	}
	
	public  int check_solution(String fichiertest) throws IOException {

		int i=1;	//Si solution non valide, elle devient -1
		int nb_noeudsTest=0 ;

		
		//Lire fichier solution
		BufferedReader br = new BufferedReader(new FileReader("./../../meta-heuristique/Test_solutions/"+fichiertest));
		
		String line;
		String file = null;
		
		//Recupere 1ère ligne <==> fichier contenant les données
		int compteur_ligne=0;
		if((line = br.readLine())!=null) {
			compteur_ligne ++;
			file=line;//fichier de données
		}
		
		//Ouverture et lecture du fichier contenant les données
		Donnees donnee = new Donnees(file);
		//int[][] tab_chemins = donnee.getTab_chemins(); 
		int[][] tab_arcsChecker = donnee.getTab_arcs();



		while ((line = br.readLine()) != null) { //lit dans le fichier TestSolution
			   compteur_ligne ++;
			   
			   if(compteur_ligne==2) { 		//Contient le nb de noeuds de départ
					nb_noeudsTest = Integer.parseInt(line);
			   }	
			   
			   if(2<compteur_ligne && compteur_ligne<(nb_noeudsTest+2)) {
			    	String[] mots = line.split(" ") ;	 //Exp: 233 1354 0
			    	//Par défaut tous les noeuds ont un chemin
			    	noeudsTest.add(Integer.parseInt(mots[0]));	//Noeud initial, figé
			    	noeudsTest.add(Integer.parseInt(mots[0]));	//Progression, noeud courant
			    	noeudsTest.add(Integer.parseInt(mots[1]));	//Taux d'évacation
			    	noeudsTest.add(Integer.parseInt(mots[2]));	//Heure de début
			    	noeudsTest.add(0); /*Variable gérant le temps de traversée : Initialement à 0
			    	elle s'incrémente en même temps que l'horloge et se remet à 0 une fois
			    	qu'elle est égale au temps de traversée*/
			    	
			    	
			   }
		}
				
		//Vérification
		
		/*fin : tous les noeuds sont arrivés au noeud safe ou erreur trouvée
		 * 
		 */
		boolean continuer = true;
		int noeud_safe = donnee.get_finalNode();
		int cpt=0;

		while(continuer==true && i==1) { //Si les noeuds ne sont pas tous dans le noeud safe et que les capacités des arcs ne sont pas dépassées
			int h=0 ; 		//Horloge
			for (int index_a=0; index_a < nb_noeudsTest ; index_a ++) {
				int noeud_init = codearraylist(index_a,0) ;
				int noeud_courant = codearraylist(index_a,1) ;
				int noeud_next = donnee.next_node(noeud_init, noeud_courant) ;
				int capa_arc = donnee.get_capaArc(noeud_courant,noeud_next,tab_arcsChecker) ;	//La capa max de l'arc
				int taux_noeudInit = codearraylist(index_a,2) ; //taux d'evacuation associé au noeud
				int date_debut =codearraylist(index_a,3);
				int temps_traversee = donnee.get_timeArc(noeud_courant, noeud_next, tab_arcsChecker); 
				int traversee_actuelle = codearraylist(index_a,4);//Variable gérant le temps de traversée
				
				if(date_debut<=h) {
				
					if(traversee_actuelle==0) {//commencer la traversée
					
						if (taux_noeudInit <= capa_arc) {	//Le transport pt avoir lieu
							int difference = capa_arc - taux_noeudInit ; //Ce que l'on doit soustraire
							donnee.set_capaArc(noeud_courant, noeud_next, tab_arcsChecker, difference) ; //Maj du poids de l'arc qui devient occupé mnt
							
							//Test si on doit libérer un arc déja occupé auparavant
							
							 //Maj du noeud courant de ce chemin suivi, il avance après av occupé le noeud suivant
							
							noeudsTest.set(codearraylist(index_a,4)+1,0); //On incrémente la traversée actuelle
				
						}
						else {
							i=-1;
							break; //Sort du for pour economiser du temps (j'espere) 
						}
					}
					if(traversee_actuelle==temps_traversee) {			
						noeudsTest.set(codearraylist(index_a,1), noeud_next) ; 

						donnee.set_capaArc(noeud_courant, noeud_next, tab_arcsChecker, capa_arc+taux_noeudInit) ; //On rend à l'arc précédent le taux qui a été utilisé
						
						noeudsTest.set(codearraylist(index_a,4),0); //On remet la variable de traversee à 0
						
					}
					else {
						noeudsTest.set(codearraylist(index_a,4)+1,0); //On incrémente la traversée actuelle
					}
				}
				
			}
				
				//Comparaison entre le taux du noeud initial et celui de l'arc
				
			for(int j=0;j<nb_noeudsTest;j++) {//pour tous les noeuds à tester
				if(codearraylist(j,1)==noeud_safe) {
					cpt+=1;
				}
			}
			if (cpt == nb_noeudsTest) {
				continuer=false;
			}
			h++;
		
		}
			
		return i;

	}
	
	//[TO BE DONE]
	public void write_solution() {
		//invalid
		//inf
		//10.5
		//handmade 0.1.0
	}
	
	
	/*CodeArrayList -> code :
	 * 0 -> noeud initial (figé)
	 * 1 -> noeud courant
	 * 2 -> taux d'evacuation
	 * 3 -> heure de début
	 * 4 -> Variable gerant le temps de traversee
	 * */
	public int codearraylist(int index,int code) {
		return noeudsTest.get(index*5+code);
	}
	
/***********			TP n°2 			****************/
	
	
	public  int borne_inf(int node_id,String fichiertest) throws IOException {

		int i=1;	//Si solution non valide, elle devient -1
		int nb_noeudsTest=0 ;

		
		//Lire fichier solution
		BufferedReader br = new BufferedReader(new FileReader("./../../meta-heuristique/Test_solutions/"+fichiertest));
		
		String line;
		String file = null;
		
		//Recupere 1ère ligne <==> fichier contenant les données
		int compteur_ligne=0;
		if((line = br.readLine())!=null) {
			compteur_ligne ++;
			file=line;
		}
		
		//Ouverture et lecture du fichier contenant les données
		Donnees donnee = new Donnees(file);
		int[][] tab_chemins = donnee.getTab_chemins();
		int[][] tab_arcsChecker = donnee.getTab_arcs();



		while ((line = br.readLine()) != null) {
			   compteur_ligne ++;
			   
			   if(compteur_ligne==2) { 		//Contient le nb de noeuds de départ
					nb_noeudsTest = Integer.parseInt(line);
			   }	
			   
			   if(2<compteur_ligne && compteur_ligne<(nb_noeudsTest+3)) {
			    	String[] mots = line.split(" ") ;	 //Exp: 233 1354 0
			    	//Par défaut tous les noeuds ont un chemin
			    	noeudsTest.add(Integer.parseInt(mots[0]));	//Noeud initial, figé
			    	noeudsTest.add(Integer.parseInt(mots[0]));	//Progression, noeud courant
			    	noeudsTest.add(Integer.parseInt(mots[1]));	//Taux d'evacuation
			    	noeudsTest.add(Integer.parseInt(mots[2]));	//Heure de début
					//System.out.println("Remplissage de NoeudsTest: "+mots[0]+", "+mots[0]+", "+mots[1]+"et "+mots[2])  ;

			   }
		}
		
		
		//System.out.println("Il y a "+nb_noeudsTest+" noeuds d'evac et le tab noeudsTest a une taille de "+noeudsTest.size())  ;
		int borne_inf = 999999999 ;	//Valeur par déf qu'on cherchera à optimiser
		
		//On test chaque noeud et on calcul son tps d'evacuation, indépendament des autres noeuds d'evacuations et noeuds intermediaires
		for (int n=0; n<nb_noeudsTest; n++) {
			
			//System.out.println("Recherches de chemins pour le Noeud n°"+codearraylist(n,0))  ;
			
			//Chercher tt les chemins qui commencent par le noeud à évacuer que l'on traite
			for (int c=0; c<donnee.get_nb_chemins(); c++) {			

				//System.out.println("Comparaison entre le noeud "+codearraylist(n,0)+" d'indice "+noeudsTest.indexOf(codearraylist(n,0))+" avec "+tab_chemins[c][0])  ;
				if (codearraylist(n,0) == tab_chemins[c][0]) {	//Ce chemin est valide
					//On calcul la durée de ce chemin (Somme durée des arcs)
			    	int long_chemin = tab_chemins[c][3] ;		//Recup la taille de ce chemin
					int temps_cumul= donnee.get_timeArc(tab_chemins[c][0],tab_chemins[c][4],tab_arcsChecker) ;
				
					for (int a=0; a<(long_chemin-1); a++) {		
			    		//System.out.print("noeud "+n+": Debug calcul cumul: Get capa sur "+tab_chemins[c][a+4]+" et "+tab_chemins[c][a+5]); //ça print le chemin 
						temps_cumul = temps_cumul + donnee.get_timeArc(tab_chemins[c][a+4], tab_chemins[c][a+5],tab_arcsChecker) ;
						
					}
					//On regarde si c'est + petit que la borne_inf actuelle 
		    		System.out.print("Maj temps_cumul au noeud n°"+n+": temps_cumul="+temps_cumul+" et borne_inf="+borne_inf) ;

					if (temps_cumul<borne_inf) {
						borne_inf = temps_cumul ;
					}
					//Affiche la ligne du chemin OK
					System.out.println("Noeud n°"+codearraylist(n,0)+" commence par le chemin ligne "+c+" (1er elm: "+tab_chemins[c][0]+") arrive en borne inf en: "+temps_cumul+" sec.") ;
				}
			}
			
			
		}
		System.out.println("[Borne_inf] La borne inférieur est: "+borne_inf);
		
		
		/*
		//Vérif si les chemins ont les bons temps:
		for (int n=0; n<10; n++) {		//Tt les chemins	
			int somme = 0 ;
			
			//On calcul le tps de ce chemin
			for (int a=0; a<tab_chemins[n][3]; a++) {		
	    		//System.out.print("noeud "+n+": Debug calcul cumul: Get capa sur "+tab_chemins[c][a+4]+" et "+tab_chemins[c][a+5]); //ça print le chemin 
				somme = somme + donnee.get_capaArc(tab_chemins[a][a+4], tab_chemins[c][a+5],tab_arcsChecker) ;
				
			}
		}*/
		
		
		return borne_inf ;
	}
	
	public  int borne_sup(int node_id,String fichiertest) throws IOException {

		int i=1;	//Si solution non valide, elle devient -1
		int nb_noeudsTest=0 ;

		
		//Lire fichier solution
		BufferedReader br = new BufferedReader(new FileReader("./../../meta-heuristique/Test_solutions/"+fichiertest));
		
		String line;
		String file = null;
		
		//Recupere 1ère ligne <==> fichier contenant les données
		int compteur_ligne=0;
		if((line = br.readLine())!=null) {
			compteur_ligne ++;
			file=line;
		}
		
		//Ouverture et lecture du fichier contenant les données
		Donnees donnee = new Donnees(file);
		int[][] tab_chemins = donnee.getTab_chemins();
		int[][] tab_arcsChecker = donnee.getTab_arcs();



		while ((line = br.readLine()) != null) {
			   compteur_ligne ++;
			   
			   if(compteur_ligne==2) { 		//Contient le nb de noeuds de départ
					nb_noeudsTest = Integer.parseInt(line);
			   }	
			   
			   if(2<compteur_ligne && compteur_ligne<(nb_noeudsTest+3)) {
			    	String[] mots = line.split(" ") ;	 //Exp: 233 1354 0
			    	//Par défaut tous les noeuds ont un chemin
			    	noeudsTest.add(Integer.parseInt(mots[0]));	//Noeud initial, figé
			    	noeudsTest.add(Integer.parseInt(mots[0]));	//Progression, noeud courant
			    	noeudsTest.add(Integer.parseInt(mots[1]));	//Taux d'evacuation
			    	noeudsTest.add(Integer.parseInt(mots[2]));	//Heure de début
			    	noeudsTest.add(0);//Variable gerant le temps de traversee
					//System.out.println("Remplissage de NoeudsTest: "+mots[0]+", "+mots[0]+", "+mots[1]+"et "+mots[2])  ;

			   }
		}
		
		
		//System.out.println("Il y a "+nb_noeudsTest+" noeuds d'evac et le tab noeudsTest a une taille de "+noeudsTest.size())  ;
		int borne_sup = 0 ;	//Valeur par déf
		
		//On test chaque noeud et on calcul son tps d'evacuation, indépendament des autres noeuds d'evacuations et noeuds intermediaires
		for (int n=0; n<nb_noeudsTest; n++) {
			
			//System.out.println("Recherches de chemins pour le Noeud n°"+codearraylist(n,0))  ;
			
			//Chercher tt les chemins qui commencent par le noeud à évacuer que l'on traite
			for (int c=0; c<donnee.get_nb_chemins(); c++) {			

				//System.out.println("Comparaison entre le noeud "+codearraylist(n,0)+" d'indice "+noeudsTest.indexOf(codearraylist(n,0))+" avec "+tab_chemins[c][0])  ;
				if (codearraylist(n,0) == tab_chemins[c][0]) {	//Ce chemin est valide
					//On calcul la durée de ce chemin (Somme durée des arcs)
			    	int long_chemin = tab_chemins[c][3] ;		//Recup la taille de ce chemin
					int temps_cumul= donnee.get_timeArc(tab_chemins[c][0],tab_chemins[c][4],tab_arcsChecker) ;
				
					for (int a=0; a<(long_chemin-1); a++) {		
			    		//System.out.print("noeud "+n+": Debug calcul cumul: Get capa sur "+tab_chemins[c][a+4]+" et "+tab_chemins[c][a+5]); //ça print le chemin 
						temps_cumul = temps_cumul + donnee.get_timeArc(tab_chemins[c][a+4], tab_chemins[c][a+5],tab_arcsChecker) ;
						
					}
					//On met à jour la borne_sup
					borne_sup = borne_sup + temps_cumul ;
					
					//Affiche la ligne du chemin OK
					System.out.println("[Borne_sup] Noeud n°"+codearraylist(n,0)+" commence par le chemin ligne "+c+" (1er elm: "+tab_chemins[c][0]+") arrive en borne inf en: "+temps_cumul+" sec.") ;
				}
			}
			
			
		}
		System.out.println("[Borne_sup] La borne sup est: "+borne_sup);
		
		

		
		return borne_sup ;
	}
	
	
	public static void main(String[] args) throws IOException {
		Checkzer chaz = new Checkzer();
		//System.out.println(chaz.check_solution("dense_10_30_3_8"));
		//chaz.borne_inf(493,"dense_10_30_3_8") ; //==42
		chaz.borne_sup(493,"dense_10_30_3_8") ;
		
	}
	
	
	
	
	
}
