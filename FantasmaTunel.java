package es.upm.dit.adsw.pacman3;
/**
* Clase que controla los fantasmas tunel: Estos se mueven sigueindo el camino más corto hacia el jugador pero
* Cuando pasan por una casilla que es trampa y hay más de una en el terreno esas casillas funcionan como un 
* tunel si solo hay una trampa se mueve normal.
* @version 1, 19/04/14
* @author Maria Garcia Fernandez
*/
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FantasmaTunel extends Movil implements Runnable { 
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

	 public FantasmaTunel(Terreno terreno) { 
		 this.terreno = terreno; 
		 imagen = super.loadImage("haunter.png"); 
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
 	}
	 	
 /** 
  * Método con el que si muere el jugador lo eliminamos.
  * @param devorado 
  */
	public void muere(boolean devorado){
		// Lo he puesto así para poder congelar la imagen y que no se 
		// borren los móviles a no ser que le ponga un true en devorado.
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
 	// 0 - si puedo moverme en esa direccion. Casilla destino vacía o esta el jugador.
 	// 1 - si ahora mismo no puedo, pero en el futuro puede que si. (Si hay otro fantasma en la casilla o he caido en una trampa).
 	// 2 - si no esta vivo, si hay una pared en esa dirección
 	public int puedoMoverme(Direccion direccion) {
 		if (casilla == null || casilla.getY()<0 || casilla.getY()>=terreno.getN() || casilla.getX()<0 || casilla.getX()>=terreno.getN() || vivo == false||  
 				casilla.hayPared(direccion)) return 2; 
 		//if (terreno.getCasilla(casilla, direccion).getMovil() != null) {
		// Se podría poner así también:
		//if(terreno.getCasilla(casilla, direccion).getMovil().getClass() == Fantasma00.class) return 1; 
		if(terreno.getCasilla(casilla, direccion).isTrampa()) return 0;
 		if ((terreno.getCasilla(casilla, direccion).getMovil() instanceof Fantasma00) || (terreno.getCasilla(casilla, direccion).getMovil() instanceof Depredador)||(terreno.getCasilla(casilla, direccion).getMovil() instanceof FantasmaTunel))
			return 1;
 		// Si hay alguien en la casilla y ese alguien es un fantasma no puedo moverme.	
	 	return 0;
	 }
	 	
/** 
 * Getter de la casilla destino. Escanea todas las casillas y devuelve donde esta el jugador.
 * @return Casilla destino.
 */
 	private Casilla getCasillaDestino(){
 		
 		for (int x = 0; x < terreno.getN(); x++){
 			for (int y = 0; y < terreno.getN(); y++){
 				// Mediante dos bucles for anidados recorremos el terreno y si la casilla no es null, hay un movil en la casilla y el movil es el jugador, devuelvo esa casilla.
 				if ((terreno.getCasilla(x, y) != null) && (terreno.getCasilla(x, y).getMovil() != null) && (terreno.getCasilla(x, y).getMovil() instanceof Jugador)){
 					Casilla destino = terreno.getCasilla(x,y);
 					return destino;
 				}
 			}
 		} // Si no devuelvo un null
 		return null;
 	}
	 	
 /** 
  * El fantasma se activa. Mientras esta vivo y seguimos JUGANDO, elige moverse a la
  * primera casilla de la ruta mas corta para alcanzar al jugador en la posicion actual.
  * Luego duerme DELAY secs y vuelve a moverse
  */
 	public synchronized void run(){
		while (vivo && estado == EstadoJuego.JUGANDO){ 
	 			
			Casilla	destino = getCasillaDestino();	
			Casilla	optima = bfs(casilla, destino);	
			
	 			Direccion d = casilla.getDireccion(optima);
	 			
	 			terreno.move(this, d);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			// http://stackoverflow.com/questions/3342651/how-can-i-delay-a-java-program-for-a-few-seconds
				Thread.currentThread().interrupt();
			}
		}
	 		
	}
 	 // Iguales los dos siguientes metodos al depredador
/** 
 * Algoritmo BFS - Breadth First Search. Busqueda en anchura. 
 * Localiza la ruta mas corta del origen al destino.
 * @param Casilla origen
 * @param Casilla  destino
 * @return Primera celda de la ruta mas corta al destino. 
 *		   Devuelve NULL si no hay ruta posible.
 */
	public Casilla bfs(Casilla origen, Casilla destino) {
		// Nos creamos la lista y el map donde iremos guardando las casiillas que nos faltan por mirar y las que ya hemos visitado.
		ArrayList<Casilla> pendientes = new ArrayList<Casilla>();
		Map<Casilla, Casilla> visitadas = new HashMap<Casilla, Casilla>();
		// Los System.out.println son trazas para depurar el código
		System.out.println("origen: "+destino.toString());
		pendientes.add(destino);
		// Añadimos el destino a las casillas pendientes
		System.out.println("¿Esta vacío pendientes?: " + pendientes.isEmpty());
		
		if (!pendientes.isEmpty()){
			// Mientras no este vacio
			// Mas trazas
			for (int i = 0; i<pendientes.size(); i++)
				System.out.println("pendiente" +i+": "+pendientes.get(i).toString());
			}
			
			System.out.println("Entro en bfs....: ");
				//Si bfs devuelve TRUE 
				//– tenemos que devolver la primera casilla en el camino encontrado 
				// o sea, la casilla en visitadas cuyo valor asociado es el origen 
				if (bfs(origen,pendientes, visitadas)){
					System.out.println("...............");
					Casilla siguiente = visitadas.get(origen);
					// Hay un pequeño error que no he conseguido solucionar:
					// Esta devolviendo visitadas.get(origen) -> devuelve el objeto cuya clave es origen
					// Y no deberia estar en visitadas porque hay una pared por en medio.
					// Pero como el jugador rapidamente se mueve hacia el objetivo es imperceptible
				
				System.out.println("Devuelve: "+siguiente.toString());
				return siguiente;
				
			}
				//Del moodle:
				//Si bfs devuelve FALSE,
				//– no hay ruta: devuelva null 
			return null;
		}
			
/** 
 * Método privado con el que comprobamos que no queden casillas pendientes.
 * @param Casilla destino
 * @param ArrayListt<Casilla>	pendientes, lista de casillas que aún no hemos investigado	
 * @param	Map<Casilla,Casilla> visitadas, casillas ya visitadas, para cada casilla indica
 *			la casilla anterior en la ruta óptima	
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
				 System.out.println("¿He encontrado camino?: " + (c1 == destino));
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
				 // Para todas las direcciones
					 Casilla casilla = terreno.getCasilla(c1, direccion); 
					 // Me calculo la casilla de al lado
					 //System.out.println("casilla adyacente: "+ casilla.toString());
					 if (casilla != null && !visitadas.containsKey(casilla)){
						 // if (casilla != null && !casilla.hayPared(direccion) && !visitadas.containsKey(casilla)){
						 // Si compruebo que no haya pared no funciona pero de todas formas  ya lo he comprobado en el getCasilla(casilla, direccion)
						 // Si esa casilla existe en la direccion en la que me muevo no hay pared y visitadas no contiene aun la casilla
				
						 System.out.println("Casilla en la que estoy"+c1);
						 System.out.println("Hay pared?: "+c1.hayPared(direccion));
						 System.out.println("Casilla a la que me muevo:"+ casilla);
						 
						 System.out.println("casilla adyacente: "+ casilla.toString());
						 // Añadimos la casilla a pendientes y a visitadas la inicial
						 pendientes.add(casilla);
						 visitadas.put(casilla, c1); 
						 System.out.println("¿Esta vacio visitadas?: "+visitadas.isEmpty());
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
		 	return bfs(destino, pendientes, visitadas);
		} // iterativamente llamamos al método
 	
	 		
}	 	