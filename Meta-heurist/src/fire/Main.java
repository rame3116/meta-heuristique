package fire;

public class Main {

	public Main() {

	}
		
	public static void main(String[] args) throws Exception {	
		Data fichier1 = new Data("donnees") ;
		Bornes bornes = new Bornes (fichier1) ;
		
		/*		Test sur le fichier donnees 	*/
		
		/*	Test 1:	Les bornes inférieur et supérieur	*/
		bornes.borne_inf() ;
		bornes.borne_sup() ;
		
		/*	Test 2: Checker 		*/
		Checker checker = new Checker("solution", fichier1,bornes) ;
		
	}
}
