package es.upm.dit.adsw.pacman3;
/**
* Recopila la informacion pertinente a una casilla del terreno. 
* Es basicamente un almacen de informacion, sin comportamiento.
* @version 1, 13/02/14
* @author Maria Garcia Fernandez
*/

import java.util.HashSet;
//Importamos HashSet para poder definir la variaible paredes
//indicando la dirección.
public class Casilla {
//Declaramos las siguientes 5 variables que vamos a utilizar 
//en esta clase tal y como aparece en el Javadoc.
	private final int x;
	private final int y;
	private Movil movil;
	private boolean objetivo;
	private boolean trampa;
	private boolean llave;
	private java.util.Set<Direccion> paredes;
/** 
* Crea una casilla a partir de las cordenadas x e y.
* Y setea las variables objetivo, movil y paredes.
* @param movil 
* @param objetivo Indica si la casilla es objetivo.
* @param paredes Direcciones en las que la celda esta separada
*  por una pared.
* @param x
* @param y
*/	
//Constructor de la clase Casilla.
	public Casilla(int x, int y){
		this.x = x;
		this.y = y;
		setObjetivo(false);
		// inicializamos el objetivo como false, es decir, 
		// que aun no ha sido alcanzado.
		setMovil(null);
		// Tambien decimos que no hay ningun movil en esa casilla.
		paredes = new HashSet<Direccion>();
	}
/** 
* Getter
* @return posicion en el eje X.
*/
	// Getter getX para sacar la cordenada x de la casilla.
	public int getX(){
		return x;
	}	
/** 
* Getter
* @return posicion en el eje Y.
*/
	// Getter getY para sacar la cordenada y de la casilla.
	public int getY(){
		return y;
	}
/** 
* Getter
* @return movil en la casilla. Null si esta vacia.
*/
	// Getter getMovil devuelve el movil que hay en la casilla.
	public Movil getMovil() {
		return movil;
	}
/** 
* Pone un movil en la casilla. Si ponemos null, se interpreta que la casilla queda vacia.
* @param movil que queremos colocar en esta casilla.
*/
	// Setter setMovil inicializa el movil con el movil que se le 
	// pasa como parametro.
	public void setMovil(Movil movil) {
		this.movil = movil;
	}
/** 
* Coloca una pared entre dos casillas.
* @param direccion en la que se encuentra la otra casilla separada por la pared.
*/
	// Método con el que añadimos la pared de la casilla, en la direccion que indicamos.
	public void ponPared(Direccion direccion){
		paredes.add(direccion);
	}
/** 
* Elimina la pared entre dos casillas. Si no hubiera pared, no pasa nada.
* @param direccion en la que se encuentra la otra casilla separada por la pared.
*/	
	// Método con el que eliminamos la pared de la casilla, en la direccion que indicamos.
	public void quitaPared(Direccion direccion){
		paredes.remove(direccion);
	}	
/** 
* Pregunta si hay pared
* @param direccion en la que miramos desde la casilla a ver si hay una pared.
* @return cierto si la hay, falso si no.
*/
	// Metodo que indica si existe una pared en la casilla, en la 
	//direccion indicada, la que se introduce como parametro.
	public boolean hayPared(Direccion direccion){
		return paredes.contains(direccion);
		
	}
/** 
* Quita todas las paredes de la casilla.
*/	
	// Método con el que quitamos todas las paredes que pueda tener la casilla.
	public void quitaParedes(){
		paredes.clear();
	}
/** 
* Getter
* @return true si la casilla es objetivo.
*/
	// Metodo que indica si la casilla a la que se aplica es el objetivo.
	public boolean isObjetivo() {
		return objetivo;
	}
/** 
* Setter
* @param objetivo valor a cargar.
* 
*/
	// Metodo con el que seteamos la variable objetivo con el objetivo que se 
	// le pasa como parametro.
	public void setObjetivo(boolean objetivo) {
		this.objetivo = objetivo;
	}
/** 
* Método equals que sobreescribimos sobre el que viene por defecto.
* @param Object o
* @return true o false indicando si son iguales o no.
*/
	@Override
	public boolean equals(Object o){ // Lo he cambiado respecto a las anteriores versiones porque no pasaba las pruebas dadas.
		if (o == this) return true;
		
		if ((o != null) &&( this.getX() == ((Casilla) o).getX())&&(this.getY() == ((Casilla) o).getY()))
			return true;
		return false;
	}
/** 
* Método equals que sobreescribimos sobre el que viene por defecto.
* @return el entero que distan.
*/
	@Override
	public int hashCode(){
		return 31*(this.getX()+this.getY());
	}
/** 
* Averigua en que direccion tengo que moverme 
* desde esta casilla para pasar a la casilla destino. 
* This y destino son casillas adyacentes.
* @param Casilla destino
* @return el entero que distan.
*/
	public Direccion getDireccion(Casilla destino) {
		if(destino==null)
			return null;
		
		// Comprobamos las coordenadas x e y de la casilla destino y de la que estamos una vez nos hubiesemos movido en esa direccion
		// si no hay pared devuelve la direccion correspondiente.
		
		if ((this.getX()+1 == destino.getX()) && (this.getY() == destino.getY()) && !hayPared(Direccion.ESTE)) return Direccion.ESTE;
		if ((this.getX()-1 == destino.getX()) && (this.getY() == destino.getY()) && !hayPared(Direccion.OESTE)) return Direccion.OESTE;
		if ((this.getY()+1 == destino.getY()) && (this.getX() == destino.getX()) && !hayPared(Direccion.NORTE)) return Direccion.NORTE;
		if ((this.getY()-1 == destino.getY()) && (this.getX() == destino.getX()) && !hayPared(Direccion.SUR)) return Direccion.SUR;
		
		
		
		return null;
	}
	
/**
 * 	Representación grafica para trazas.
 *  @return la posicion en formato (x, y).	
 */
	@Override
	public java.lang.String toString(){
		// También se podría haber puesto String.format("(%d,%d)", x, y);
		// Como viene en las diapositivas.
		return "("+ this.getX() +"," + this.getY() +")";
	}
	
/**
 * 	Getter de la trampa
 *  @return True si la casilla es una trampa.	
 */
	public boolean isTrampa(){
		return trampa;
	}
/**
 * 	Setter de la trampa.
 *  @param boolean trampa	
 */
	public void setTrampa(boolean trampa){
		this.trampa = trampa;
	}
	
/**
 * 	Getter de la llave
 *  @return True si la casilla es una llave.	
 */
	public boolean isLlave(){
		return llave;
	}
/**
 * 	Setter de la llave.
 *  @param boolean llave	
 */
	public void setLlave(boolean llave){
		this.llave = llave;
	}
}