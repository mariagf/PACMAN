package es.upm.dit.adsw.pacman3;

public class Pared{
	//Declaramos las siguientes variables que vamos a utilizar en esta clase.
	private final Casilla casilla;
	private final Direccion direccion;
/** 
* Crea una pared a partir de una casilla y una dirección.
* @param casilla
* @param direccion en la uqe se encuentra la pared.
*/	
	public Pared(Casilla casilla, Direccion direccion){
		this.direccion = direccion;
		this.casilla = casilla;
	}
/** 
* Getter de la casilla donde está dicha pared
* @return casilla
*/
	public Casilla getCasilla(){
		return casilla;
	}
/** 
* Getter de la dirección donde está dicha pared
* @return direccion
*/
	public Direccion getDireccion() {
		return direccion;
	}
/** 
* Método equals que sobreescribimos sobre el que viene por defecto.
* @param Object o
* @return true o false indicando si son iguales o no.
*/	
	@Override
	public boolean equals(Object o){
		if (this ==	o) return true;	
		if (o == null || getClass() != o.getClass()) return	false;	
		Casilla	casilla2	= (Casilla)	o;	
		return (casilla.getX() == casilla2.getX() && 
				casilla.getY() == casilla2.getY());	
	}
/** 
* Método equals que sobreescribimos sobre el que viene por defecto.
* @return el entero que distan.
*/
	@Override
	public int hashCode(){
		return 31*(casilla.getX()+casilla.getY());
	}
	
}