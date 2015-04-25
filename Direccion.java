package es.upm.dit.adsw.pacman3;

public enum Direccion {
	// Posibles direcciones que puede tomar.
	
	NORTE, SUR, ESTE, OESTE;
/** 
* Dada una direccion, devuelve la opuesta. Por ejemplo, lo opuesto al NORTE es el SUR.
* @return direccion opuesta
*/	
	// Metodo que devuelve la direccion opuesta.
	public Direccion opuesto(){
		
		if (this == NORTE) return SUR;
		else if (this == SUR) return NORTE;
		else if (this == ESTE) return OESTE;
		else return ESTE;		
	}
	// Los otros dos metodos que aparecen en el javadoc values y valueOf 
	// ya vienen por defecto en los enumerados.
}
