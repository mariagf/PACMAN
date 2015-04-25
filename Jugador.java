package es.upm.dit.adsw.pacman3;
/**
 * Uno de los moviles sobre el terreno. Elmas importante, por cierto.
 * @version 1, 13/02/14
 * @author Maria Garcia Fernandez
 */
import java.awt.Image;

public class Jugador extends Movil {	
/** 
* @param casilla Casilla en la que esta. 
* @param imagen
* @param objetivoAlcanzado TRUE si ha alcanzado una casilla objetivo.
* @param terreno El terreno en el que se mueve.
* @param vivo TRUE si no ha muerto (comido por alguien).
*/	
	//private int estado; Lo dijo Mañas en clase del 25 para añadir superpoderes a los fantasmas.
	private Casilla casilla;
	private final java.awt.Image imagen;
	private boolean vivo;
	private EstadoJuego estado;
	private Terreno terreno;
	
/** 
* Constructor.
* @param terreno para que el jugador pueda actuar en funcion de su entorno.
*/	
	public Jugador(Terreno terreno){
		// Inicializamos las variables imagen con la foto del jugador, diciendo que 
		// esta vivo y que por tanto no se ha alcanzado el objetivo.
		imagen = loadImage("wartortle.png");
		vivo = true;
		estado = EstadoJuego.JUGANDO;
		this.terreno = terreno;
	}

/** 
* Proporciona como quiere ser presentado gráficamente.
* @return Una imagen adecuada.
*/
	// Es un Getter de la imagen.
	public Image getImagen() {
		return imagen;
	}
/** 
* Getter.
* @return en que casilla me encuentro.
*/
	// Es un getter de la casilla.
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
		if ((casilla != null) && (casilla.isObjetivo())){
			estado = EstadoJuego.GANA_JUGADOR;
			vivo = true;
		}
		
	// Comprobamos que si el jugador llega a una casilla llave se quitan 
	// todas las trampas y la llave.	
		 
		if ((casilla != null) &&(casilla.isLlave()))
			terreno.quitaTrampasLlave();
	}
	
/** 
* Jugador eliminado.
* @param devorado
*/
	// Con este metodo eliminamos el jugador cuando se lo come un fantasma.
	public void muere(boolean devorado) {
		vivo = false;
		if (devorado == true){
			estado = EstadoJuego.PIERDE_JUGADOR;
			casilla.setMovil(null);
			setCasilla(null);
		}
	}

/** 
* Alguien pregunta si me puedo mover ahora o en el futuro en una cierta direccion.
* @param direccion direccion en la que intento moverme
* @return 0 si puedo moverme en esa direccion. 1 si ahora mismo no puedo, pero en 
* el futuro puede que si. 2 si no puedo moverme, ni ahora ni nunca mas.
*/
	// Con este metodo sabemos si podemos movernos o no en una dirección.
	// 0 - si puedo moverme en esa direccion.
	// 2 - si no puedo moverme, ni ahora ni nunca. Porque me voy fuera, ha una pared o hay un bicho y muero.
	public int puedoMoverme(Direccion direccion) {
		// He quitado el vivo == false respecto de versiones anteriores porque en este caso al reiniciar no se movia el jugador
		if (casilla.hayPared(direccion)|| casilla.getY()>= 15 || casilla.getX()<0||casilla.getY()<0 || casilla.getX()>=15|| estado == EstadoJuego.GANA_JUGADOR) return 2; 
		// Los 15 que aparecen son la N del terreno que no puedo llamarla desde el jugador.
		
		// En cualquier otro  caso, es decir, si puedo moverme me muevo.
		
		return 0;
	}

}
