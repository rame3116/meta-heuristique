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
	
	public  int check_solution(int node_id,String fichiertest) throws IOException {

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
		int[][] tab_chemins = donnee.getTab_chemins();
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
			   }
		}
				
		//Vérification
		
		/*fin : tous les noeuds sont arrivés au noeud safe ou erreur trouvée
		 * ATTENTION IL FAUT GERER LE TEMPS DE TRAVERSEE !!!!! ET LE DEPART QUI N'EST PAS A 0
		 * pour l'instant on oublie les duetime des arcs (l'arc devient inutilisable)
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
				//Comparaison entre le taux du noeud initial et celui de l'arc
				if(date_debut<h) {
					if (taux_noeudInit <= capa_arc) {	//Le transport pt avoir lieu
						int difference = capa_arc - taux_noeudInit ; //Ce que l'on doit soustraire
						donnee.set_capaArc(noeud_courant, noeud_next, tab_arcsChecker, difference) ; //Maj du poids de l'arc qui devient occupé mnt
						
						//Test si on doit libérer un arc déja occupé auparavant
						if (codearraylist(index_a,0) != codearraylist(index_a,1)) {		//Donc si le noeud n'est plus dans son état initial (comme ds le fichier test)
							int noeud_prec = donnee.prec_node(noeud_init, noeud_courant) ;
							int capa_arcprecedent = donnee.get_capaArc(noeud_prec,noeud_next,tab_arcsChecker) ;
							donnee.set_capaArc(noeud_prec, noeud_courant, tab_arcsChecker, capa_arcprecedent+taux_noeudInit) ; //On rend à l'arc précédent le taux qui a été utilisé
						}
						 //Maj du noeud courant de ce chemin suivi, il avance après av occupé le noeud suivant
						noeudsTest.set(index_a*4+1, noeud_next) ; 
			
					}
					else {
						i=-1;
						break; //Sort du for pour economiser du temps(j'espere) 
					}
				}
			}
			for(int j=0;j<nb_noeudsTest;j++) {//pour tous les noeuds à tester
				if(codearraylist(j,1)==noeud_safe) {
					cpt+=1;
				}
			}
			if (cpt == nb_noeudsTest) {
				continuer=false;
			}
		}
			// [TO BE DONE] 
				// Prendre en compte le tps de début des noeudsS + tps propag arcs
			

		return i;

	}
	
	//[TO BE DONE]
	public void write_solution() {
		//invalid
		//inf
		//10.5
		//handmade 0.1.0
	}
	
	
	/*DANS LA LISTE NOEUDTEST : SI ON VEUT UTILISER LE NOEUD I, IL FAUT ECRIRE
	 * noeudsTest(i*4)
	 * 
	 * SI ON VEUT LE TAUX D'EVACUATION noeudsTest(i*4+2)
	 * SI ON VEUT LE TEMPS DE DEPART : noeudsTest(i*4+3)
	 * ON CREE UNE FONCTION PRENANT 2 PARAMETRES : i : LE NUMERO DU NOEUD
	 * ET j : VALANT 0,1, 2 OU 3 pour savoir si on veut le noeud initial, le noeud courant, le taux d'evacuation
	 * ou la date de debut d'evacuation  
	 */
	public int codearraylist(int index,int code) {
		return noeudsTest.get(index*4+code);
	}
	
	public static void main(String[] args) throws IOException {
		Checkzer chaz = new Checkzer();
		System.out.println(chaz.check_solution(493,"dense_10_30_3_8"));
		
		
	}
	
	
	
	
	
}
