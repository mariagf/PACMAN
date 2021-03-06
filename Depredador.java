
package es.upm.dit.adsw.pacman3;
/**
* Clase que controla los fantasmas depredadores. Similar a la clase Fantasma00 excepto en el m�todo Run.
* @version 1, 05/04/14
* @author Maria Garcia Fernandez
*/
import java.awt.Image;
import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class Depredador extends Movil implements Runnable { 
	 // Declaramos las siguientes variables que vamos a utilizar en la clase.
	 private final Image imagen; 
	 private final Terreno terreno; 
	 private Casilla casilla; 
	 private boolean vivo;
	 private EstadoJuego estado;

/** 
 * Constructor de la clase donde ponemos al fantasma en el terreno le asignamos una imagen
 * le decimos que esta vivo y por �ltimo ponemos el estado del juego en JUGANDO.
 */

	 public Depredador(Terreno terreno) { 
	 	 this.terreno = terreno; 
	 	 imagen = super.loadImage("gengar.png"); 
	 	 vivo = true;
	 	 estado = EstadoJuego.JUGANDO;

	 } 
 /** 
  * Proporciona como quiere ser presentado gr�ficamente.
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
 * M�todo con el que si muere el jugador lo eliminamos.
 * @param devorado 
 */
 	public void muere(boolean devorado){
 		vivo = false;
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
 	// 0 - si puedo moverme en esa direccion. Casilla destino vac�a o esta el jugador.
 	// 1 - si ahora mismo no puedo, pero en el futuro puede que si. (Si hay otro fantasma en la casilla o he caido en una trampa).
 	// 2 - si no esta vivo, si hay una pared en esa direcci�n
 	public int puedoMoverme(Direccion direccion) {
 		if (casilla == null || casilla.getY()<0 || casilla.getY()>=terreno.getN() || casilla.getX()<0 || casilla.getX()>=terreno.getN() || vivo == false||  
 				casilla.hayPared(direccion)) return 2; 
 		//if (terreno.getCasilla(casilla, direccion).getMovil() != null) {
 			// Se podr�a poner as� tambi�n:
 			//if(terreno.getCasilla(casilla, direccion).getMovil().getClass() == Fantasma00.class) return 1; 
 			if ((terreno.getCasilla(casilla, direccion).getMovil() instanceof Fantasma00) || (terreno.getCasilla(casilla, direccion).getMovil() instanceof Depredador)
 					|| (terreno.getCasilla(casilla, direccion).getMovil() instanceof FantasmaTunel))
 				return 1;
 		//}// Si hay alguien en la casilla y ese alguien es un fantasma no puedo moverme.
 			if (casilla.isTrampa()) return 1;
 			// Si hay una trampa me quedo bloqueado un rato
 			return 0;
 			
 	}
	 
 /** 
 * Getter de la casilla destino. Escanea todas las casillas y devuelve donde esta el jugador.
 * @return Casilla destino.
 */
 	
 	// Lo he tenido que hacer public para probarlo en las pruebas
 	public Casilla getCasillaDestino(){
 		
 		for (int x = 0; x < terreno.getN(); x++){
 			for (int y = 0; y < terreno.getN(); y++){
 				// Mediante dos bucles for anidados recorremos el terreno y si nos encontramos con un movil jugador devolvemos la casilla.
 				if ((terreno.getCasilla(x, y) != null) &&(terreno.getCasilla(x, y).getMovil() != null) && 
 						(terreno.getCasilla(x, y).getMovil() instanceof Jugador)){
 					Casilla destino = terreno.getCasilla(x,y);
 					return destino;
 				}
 			}
 		}
 		return null;
 	}
 
 		
	
	
/** 
 * El fantasma se activa. Mientras esta vivo y seguimos JUGANDO, elige moverse a la
 * primera casilla de la ruta mas corta para alcanzar al jugador en la posicion actual.
 * Luego duerme DELAY secs y vuelve a moverse
 */
	public synchronized void run(){
		while (vivo && estado == EstadoJuego.JUGANDO){ 
			// Siguiendo las instrucciones del pdf:
			Casilla	destino = getCasillaDestino();	
			// Calculamos la casilla destino que es donde se encuentra el jugador
			Casilla	optima = bfs(casilla, destino);	
			// Y la casilla �ptima segun el agoritmo bfs
			if (casilla != null && optima != null){
			// Si es distinto de null lo movemos en la direccion �ptima
			Direccion d = casilla.getDireccion(optima);
			terreno.move(this, d);
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			// http://stackoverflow.com/questions/3342651/how-can-i-delay-a-java-program-for-a-few-seconds
			Thread.currentThread().interrupt();
			}
		}
			
	}
/** 
 * Algoritmo BFS - Breadth First Search. Busqueda en anchura. 
 * Localiza la ruta mas corta del origen al destino.
 * @param Casilla origen (donde esta el depredador)
 * @param Casilla  destino (donde esta el jugador)
 * @return Primera celda de la ruta mas corta al destino. 
 *		   Devuelve NULL si no hay ruta posible.
 */
	
	public Casilla bfs(Casilla origen, Casilla destino) {
		// Nos creamos la lista y el map donde iremos guardando las casiillas que nos faltan por mirar y las que ya hemos visitado.
		ArrayList<Casilla> pendientes = new ArrayList<Casilla>();
		Map<Casilla, Casilla> visitadas = new HashMap<Casilla, Casilla>();
		// Los System.out.println son trazas para depurar el c�digo
		System.out.println("destino: "+destino.toString());
		pendientes.add(destino);
		// A�adimos el destino a las casillas pendientes
		System.out.println("�Esta vac�o pendientes?: " + pendientes.isEmpty());
			
		if (!pendientes.isEmpty()){
			// Mientras no este vacio
			// Mas trazas
			for (int i = 0; i<pendientes.size(); i++)
				System.out.println("pendiente" +i+": "+pendientes.get(i).toString());
		}
				
		System.out.println("Entro en bfs....: ");
		//Si bfs devuelve TRUE 
		//� tenemos que devolver la primera casilla en el camino encontrado 
		// o sea, la casilla en visitadas cuyo valor asociado es el origen 
		if (bfs(origen,pendientes, visitadas)){
			System.out.println("...............");
			if (visitadas.containsValue(visitadas.get(origen))){
				Casilla siguiente = visitadas.get(origen);
				// Hay un peque�o error que no he conseguido solucionar:
				// Esta devolviendo visitadas.get(origen) -> devuelve el objeto cuya clave es origen
				// Y no deberia estar en visitadas porque hay una pared por en medio.
				// Pero como el jugador rapidamente se mueve hacia el objetivo es imperceptible
				
				System.out.println("Devuelve: "+siguiente.toString());
				return siguiente;
			}
		}
		//Del moodle:
		//Si bfs devuelve FALSE,
		//� no hay ruta: devuelva null 
		return null;
	}
		

/** 
 * M�todo privado con el que comprobamos que no queden casillas pendientes.
 * @param Casilla destino
 * @param ArrayListt<Casilla>	pendientes, lista de casillas que a�n no hemos investigado	
 * @param	Map<Casilla,Casilla> visitadas, casillas ya visitadas, para cada casilla indica
 *			la casilla anterior en la ruta �ptima	
			clave: casilla siguiente	
			valor: casilla anterior	
 * @return False si no quedan pendientes.
 */
	
	private boolean bfs (Casilla destino, ArrayList<Casilla> pendientes, Map<Casilla,Casilla> visitadas){
	
		if (pendientes.isEmpty()) {
			System.out.println("Salgo del bfs...");
			return false;
		}
		// Si no queda ninguna casilla pendiente devuelve false
		// Si  no:
		else{
				
			Casilla c1 = pendientes.get(0);
			// Si es igual al destino devuelvo true
			System.out.println("�He encontrado camino?: " + (c1 == destino));
			// Si visitadas contiene el detino devuelvo true
			if (visitadas.containsKey(destino)){
				System.out.println("Salgo del bfs...");
				return true;
			}
			
			// Trazas
			for (int i = 0; i < pendientes.size(); i++){
				Casilla c = pendientes.get(i);
				System.out.println("pendientes"+i+": " + c.toString());
			}
					
			for (Direccion direccion : Direccion.values()) { 
				 if(c1 != null){// && !terreno.hayPared(c1, direccion)){
				 // Para todas las direcciones
				 Casilla casillaAdyacente = terreno.getCasilla(c1, direccion); 
				 // Me calculo la casilla de al lado
				 	if (casillaAdyacente != null && !visitadas.containsKey(casillaAdyacente)){
				 		// Si esa casilla existe en la direccion en la que me muevo y visitadas no contiene aun la casilla
				 		System.out.println("Casilla en la que estoy"+c1);
				 		System.out.println("Hay pared?: "+c1.hayPared(direccion));
				 		System.out.println("casilla adyacente: "+ casillaAdyacente.toString());
				 		// A�adimos la casilla a pendientes y a visitadas la inicial
				 		pendientes.add(casillaAdyacente);
				 		visitadas.put(casillaAdyacente, c1); 
				 		System.out.println("�Esta vacio visitadas?: "+visitadas.isEmpty());
				 		System.out.println("Visitadas: " +visitadas.toString());
				 	}
				 }
				 // Y la borramos de pendientes (la inicial)
				 pendientes.remove(c1);
				 // Trazas
				 System.out.println("Pendientes una vez que hemos borrado la casilla en la que estabamos vigilando todas las posibles direcciones");
				 for(int k = 0; k< pendientes.size(); k++){
					System.out.println("pendientes" +k+": "+pendientes.get(k).toString());
					k++;
				 }
			} 
		}
		return bfs(destino, pendientes, visitadas);
		// iterativamente llamamos al m�todo
			
	}	
		
}