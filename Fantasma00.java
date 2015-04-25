package es.upm.dit.adsw.pacman3;
/**
* Clase que controla los fantasmas básicos.
* @version 1, 05/04/14
* @author Maria Garcia Fernandez
*/
import java.awt.Image;
import java.util.Random;

public class Fantasma00 extends Movil implements Runnable { 
	 // Declaramos las siguientes variables que vamos a utilizar en la clase.
	 private final Image imagen; 
	 private final Terreno terreno; 
	 private Casilla casilla; 
	 private boolean vivo;
	 private EstadoJuego estado;
/** 
* Constructor de la clase donde ponemos al fantasma en el terreno le asignamos una imagen
* le decimos que esta vivo y por último ponemos el estado del juego en JUGANDO.
*/

	 public Fantasma00(Terreno terreno) { 
		 this.terreno = terreno; 
		 imagen = super.loadImage("gastly.png"); 
		 vivo = true;
		 estado = EstadoJuego.JUGANDO;

	 } 
	
/** 
* Proporciona como quiere ser presentado gráficamente.
* @return una imagen adecuada
*/
	// Es un Getter de la imagen.
	public Image getImagen() {
		return imagen;
	}
/** 
* Getter.
* @return en que casilla me encuentro.
*/
	// Es un getter de la cailla.
	public Casilla getCasilla() {
		return casilla;
	}
	
/** 
* Setter.
* @param casilla en la que me colocan.
*/
	// Setter de casilla.
	public void setCasilla(Casilla casilla) {
		this.casilla = casilla;
	}
	
/** 
* Método con el que si muere el jugador lo eliminamos.
* @param devorado 
*/
	public void muere(boolean devorado){
		vivo = false;
		// Lo he hecho asi para poder dejar congelados los moviles sin tener que borrarlos
		if (devorado){
			casilla.setMovil(null);
			estado = EstadoJuego.PIERDE_JUGADOR;
		}
	}
	
/** 
* Alguien pregunta si me puedo mover ahora o en el futuro en una cierta direccion.
* @param direccion direccion en la que intento moverme
* @return 0 si puedo moverme en esa direccion. 1 si ahora mismo no puedo, pero en 
* el futuro puede que si. 2 si no puedo moverme, ni ahora ni nunca mas.
*/
	// Con este metodo sabemos si podemos movernos o no en una direccion.
	// 0 - si puedo moverme en esa direccion. Casilla destino vacía o esta el jugador.
	// 1 - si ahora mismo no puedo, pero en el futuro puede que si. (Si hay otro fantasma en la casilla o he caido en una trampa).
	// 2 - si no esta vivo, si hay una pared en esa dirección
	public int puedoMoverme(Direccion direccion) {
		if (casilla == null || casilla.getY()<0 || casilla.getY()>=terreno.getN() || casilla.getX()<0 || casilla.getX()>=terreno.getN() 
				|| vivo == false|| true == casilla.hayPared(direccion)) return 2; 
		if (terreno.getCasilla(casilla, direccion).getMovil() != null) {
			if((terreno.getCasilla(casilla, direccion).getMovil().getClass() == Fantasma00.class)|| (terreno.getCasilla(casilla, direccion).getMovil() instanceof FantasmaTunel) ||(terreno.getCasilla(casilla, direccion).getMovil() instanceof Depredador)) return 1; // Faltaría comprobar si lo que hay es un fantasma o el jugador. terreno.getCasilla(casilla, direccion).getMovil().getClass()
		}// Si hay alguien en la casilla y ese alguien es un fantasma no puedo moverme.
		
		if (casilla.isTrampa()) return 1;
		// Si hay una trampa me quedo bloqueado un rato
		return 0;
		
	}
	/** 
	* Método auxiliar privado con el que eligimos una dirección aleatoria.
	* @return direccion 
	*/	
	private Direccion direccionRandom(){
		//http://stackoverflow.com/questions/6029495/how-can-i-generate-random-number-in-specific-range-in-android
		//Note that nextInt(int max) returns an int between 0 inclusive and max exclusive.
			Random r = new Random();
			int numero = r.nextInt(4); // Esto va a dar 0, 1, 2 ó 3.
			// Con un case vamos a generar las direcciones random según el número obtenido asignádole una dirección.
			Direccion direccion = Direccion.NORTE;
			switch (numero){
				case 0: 
					direccion = Direccion.ESTE;
					break;
				case 1:
					direccion = Direccion.NORTE;
					break;
				case 2:
					direccion = Direccion.SUR;
					break;
				case 3:
					direccion = Direccion.OESTE;
			
			}
			return direccion;
	}
/** 
 * El fantasma se activa. Mientras esta vivo y seguimos JUGANDO, elige una direccion al azar, e intenta moverse en esa direccion.
 */
	public void run(){
		
		while (vivo && estado == EstadoJuego.JUGANDO){ 
			Direccion d = direccionRandom();
			// Los comentarios siguientes de impresiones por pantalla fueron depueraciones del código en el laboratorio que hice con
			// el profesor cuando fallaba el método.
			// System.out.println(puedoMoverme(d));
			terreno.move(this, d);
			// System.out.println(d);
			// System.out.println(this);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// http://stackoverflow.com/questions/3342651/how-can-i-delay-a-java-program-for-a-few-seconds
				Thread.currentThread().interrupt();
			}
		}
		
	}

}