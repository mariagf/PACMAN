package es.upm.dit.adsw.pacman3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

// Todos los métodos de esta clase son synchronized (TEORÍA).
public class Terreno {
/**
 * @param casillas
 */
		private final Casilla[][] casillas;
		private View view;
		private EstadoJuego estado;
		private Jugador jugador = new Jugador(this);
		private int contador = 0;
/** 
 * Constructor.
 * @param n numero de casillas en horizontal (X) y en vertical (Y).
 */
	public Terreno (int n){
			
			casillas = new Casilla[n][n];
			for (int i=0; i<n;i++){// se podría poner casillas.length en vez de n;
				for (int j=0; j<n;j++){
					casillas[i][j] = new Casilla(i,j);
				}
			}
		}
/** 
 * Setter de la View
 * @param view
 */	
	public synchronized void setView (View view){
		this.view = view;
	}
/** 
 * Método que devuelve la longitud del terreno
 * @return la longitud del terreno
 */
	// En vez de crear una variable n como hice en el lab1 que puede ser modificada, de esta forma está mejor.
	public synchronized int getN (){
		return casillas.length;
	}
/** 
 * Método que pone las paredes en el tablero dejando
 * siempre un camino posible al objetivo.
 */
	// Este método nos lo dan.
	public synchronized void ponParedes (){
		
		Random random = new Random();
		int N = getN(); 
		Direccion[] direcciones = Direccion.values(); 
		// pongo todas las paredes 
		for (int x=0; x<N; x++) { 
			for (int y=0; y<N; y++) { 
			Casilla casilla = getCasilla(x,y);
				for (Direccion direccion: direcciones) { 
					ponPared(casilla, direccion); 
				} 
			} 
		} 
		// quito el mínimo para interconectar todo 
		// Antes ponía eso pero lo he tenido que cambiar porque daba errores.
		// List<Casilla> conectadas = new ArrayList<Casilla>(); 
		// List<Pared> paredes = new ArrayList<Pared>(); 

		ArrayList<Casilla> conectadas = new ArrayList<Casilla>(); 
		ArrayList<Pared> paredes = new ArrayList<Pared>(); 
		 
		int x0 = random.nextInt(N); 
		int y0 = random.nextInt(N); 
		Casilla casilla0 = getCasilla(x0, y0); 
		conectadas.add(casilla0); 
		for (Direccion direccion : direcciones) 
			paredes.add(new Pared(casilla0, direccion)); 
		while (paredes.size() > 0) { 
			int i = random.nextInt(paredes.size()); 
			Pared pared = paredes.remove(i); 
			Casilla origen = pared.getCasilla(); 
			Direccion direccion = pared.getDireccion(); 
			Casilla destino = getCasilla(origen.getX(),origen.getY(),direccion); 
			if (destino != null && !conectadas.contains(destino)) { 
				quitaPared(origen, direccion); 
				conectadas.add(destino); 
				for (Direccion d : direcciones) { 
					if (destino.hayPared(d)) 
						paredes.add(new Pared(destino,d)); 
				} 
			} 
		} 
		// Quito unas cuantas paredes más para abrir rutas alternativas 
		for (int q = 0; q < 2 * N; q++) { 
			 int x = random.nextInt(N); 
			 int y = random.nextInt(N); 
			 Casilla casilla = getCasilla(x,y); 
			 Direccion direccion = direcciones[random.nextInt(direcciones.length)]; 
			 quitaPared(casilla, direccion); 
		 } 	 
	 } 
		
/** 
 * Casilla correspondiente a las coordenadas pasadas como parámetro.
 * @param x posicion horizontal (0..N-1).
 * @param y posicion vertical (0..N-1).
 * @return casilla 
 */	
	public synchronized Casilla getCasilla (int x, int y){
		return casillas[x][y];
	}
	
/** 
 * Casilla anexa en una cierta direccion.
 * @param x posicion horizontal (0..N-1).
 * @param y posicion vertical (0..N-1).
 * @param direccion
 * @return casilla anexa en la direccion indicada o null si no existe, por ejemplo al borde del terreno.
 */	
	public synchronized Casilla getCasilla(int x, int y, Direccion direccion){ // metodo que devuelve la casilla opuesta a otra dada una direccion.
		try{
			if(!hayPared(new Casilla(x,y),direccion)){
			switch(direccion){
			case NORTE:	
				
				return casillas[x][y+1];
			case SUR:
				
				return casillas[x][y-1];
			case ESTE:
				
				return casillas[x+1][y];
			default:
				
				return casillas[x-1][y];
			} } return null;
		} catch (Exception e) { // Salta excepcion y devuelve null si intenta devolver una casilla que esta fuera de los limites del terreno.
				return null;
	   	  }	
	}
	  
/** 
 * Casilla anexa en una cierta direccion.
 * @param casilla
 * @param direccion
 * @return casilla anexa en la direccion indicada o null si no existe, por ejemplo al borde del terreno.
 */	
	public synchronized Casilla getCasilla (Casilla casilla, Direccion direccion){
				
		try{
			if (!hayPared(casilla, direccion)){
			switch(direccion){
			case NORTE:	
			
				return casillas[casilla.getX()][casilla.getY()+1];
			
			case SUR:
				
				return casillas[casilla.getX()][casilla.getY()-1];
			
			case ESTE:
			
				return casillas[casilla.getX()+1][casilla.getY()];
				
			case OESTE:
				
				return casillas[casilla.getX()-1][casilla.getY()];
			default:
					return null;
				
			}
			}
			return null;
		} catch (Exception e) { // salta excepcion y devuelve null si intenta devolver una casilla que esta fuera de los limites del terreno.
				return null;
		  }	 
		
		}

/** 
* Coloca un movil sobre el terreno. Indirectamente, hay una casilla afectada.
* @param x  posicion horizontal (0..n-1).
* @param y  posicion vertical (0..n-1).
* @param movil lo que queremos colocar.
* @return true si se puede y queda colocado; false si no es posible.
*/	
	public synchronized boolean put(int x, int y, Movil movil){
		if (x<0 || y<0 || x>getN()-1|| y>getN()-1) return false;
		if (casillas[x][y].getMovil() == null){ //Si no hay un movil ya en la casilla de las coordenadas que nos dan.
			casillas[x][y].setMovil(movil);	// pongo el movil que me han dado en la casilla cuyas coordenadas me han pasado como parametro
			movil.setCasilla(casillas[x][y]); // y tambien tengo que asignar la casilla al movil.
			return true;
		}
		else return false;
	}
	
/** 
 * Método con el que obtenemos la casilla con trampa más cercana al objetivo que tenga trampa.
 * @param trampa1 Casilla con trampa en la que estoy ahora mismo
 * @return trampa2 Casilla con trampa más cerca de trammpa1
 */	
	private Casilla buscaTrampaMasCercanaObjetivo(Casilla trampa1){
		//for(int i = 0; i <= nTrampas(); i++){
			for(int x = getN()-1; x>= 0; x--){
				for(int y = getN(); y>= 0; y--){
					if(casillas[x][y].isTrampa())
						return casillas[x][y];
				}
			//}
		}
		return null;
	}

/** 
 * Método con el que obtenemos el numero de trampas en el terreno
 * @return nTrampas 
 */	
	private int nTrampas(){
		int nTrampas = 0;
			for (int x = 0; x< getN(); x++){
				for (int y = 0; y< getN(); y++){
					if (casillas[x][y].isTrampa())
						nTrampas++;
				}
			}
		return nTrampas;
	}
/** 
 * Método con el que movemos un móvil hacia una dirección pasada como parámetro.
 * @param movil
 * @param direccion
 * @return casilla a la que se mueve, la cassilla en la que se encuentra si no puede moverse.
 */	

		
public synchronized Casilla move(Movil movil, Direccion direccion){
		
		Casilla casillaMovil = movil.getCasilla();
		try{
			// va dentro del try la casilla siguiente porque puede ser null
			Casilla casillaSiguiente = getCasilla(casillaMovil, direccion);
			
			if (movil.puedoMoverme(direccion)==2 || casillaSiguiente.hayPared(direccion.opuesto())) {
				contador = 0; // reseteo flag
				
				estado = EstadoJuego.JUGANDO;
				notifyAll();
				return casillaMovil;
			}
			
			if (movil.puedoMoverme(direccion)==1 ){
				estado = EstadoJuego.JUGANDO;
				contador++; // Mediante una variable contador controlo que no se quedan chocandose mucho tiempo.
				while (movil.puedoMoverme(direccion)==1) {
					wait();
					if (contador == 3){
						// Si se chocan dos fantasmas espero a que se intenten mover 3 veces y los muevo en dirección contraria, 
						// basándome en el documento FAQ
						// Habría que implementarlo de otra forma más óptima porque si entra en un callejón se queda bloqueado.
						move(movil, direccion.opuesto());
						contador = 0;
					}
				}		
				notifyAll();
				
				return casillaMovil;
				
			}
			if (movil instanceof FantasmaTunel && casillaMovil.isTrampa()){
				 // Cuando pisa una trampa un fantasma tunel aparece en el origen
				 	Casilla c = new Casilla(0,0);
				
				 	casillaMovil.setTrampa(false);
				 	c.setMovil(movil);
			 		movil.setCasilla(c);
			 		casillaMovil.setMovil(null);
					notifyAll();	
					
					return casillaMovil;
				 }
			if (casillaSiguiente != null){
			if((movil instanceof Jugador) && ((casillaSiguiente.getMovil() instanceof Fantasma00) 
					|| (casillaSiguiente.getMovil() instanceof Depredador) || 
						casillaSiguiente.getMovil() instanceof FantasmaTunel))	{
				movil.muere(true);
				paraMoviles();
				notifyAll();
				estado = EstadoJuego.PIERDE_JUGADOR;
				view.muestra("Ha perdido, inténtelo otra vez!");
				return casillaMovil;
			}
			
			if ((((movil instanceof Fantasma00) || (movil instanceof FantasmaTunel) || 
					 (movil instanceof Depredador)) && casillaSiguiente.getMovil() instanceof Jugador) && !casillaSiguiente.hayPared(direccion.opuesto())){
					// muere el jugador, paro los moviles -> congelo la imagen y saco mensaje de hemos perdido.
					casillaSiguiente.getMovil().muere(true);
					casillaSiguiente.setMovil(movil);
					movil.setCasilla(casillaSiguiente);
					casillaMovil.setMovil(null);
					paraMoviles();
					notifyAll();
					estado = EstadoJuego.PIERDE_JUGADOR;
					view.muestra("Ha perdido, inténtelo otra vez!");
					return casillaSiguiente;
				}
			}
			
			
			 if (movil instanceof Jugador && casillaSiguiente.isObjetivo()){
				 	casillaSiguiente.setMovil(movil);
					movil.setCasilla(casillaSiguiente);
					casillaMovil.setMovil(null);
					estado = EstadoJuego.GANA_JUGADOR;
					view.muestra("¡Felicidades, ha ganado!");
					view.pintame();
					notifyAll();
					paraMoviles();
					return casillaSiguiente;
				 }
			 

			 else{
				 	casillaSiguiente.setMovil(movil);
					movil.setCasilla(casillaSiguiente);
					casillaMovil.setMovil(null);
					view.pintame();
					notifyAll();
					return casillaSiguiente;
			 }
			 
		}catch(Exception NullPointerException){
			return casillaMovil; 

		}
	}
 /*
  * De los métodos de prueba he sacado estos 3 siguientes métodos	
  */
private boolean conectadas(Terreno terreno, Casilla c1, Casilla c2) {
//  Set<Casilla> visitadas = new HashSet<Casilla>();
//  return conectadas_recursivo(terreno, c1, c2, visitadas);
  return conectadas_iterativo(terreno, c1, c2);
}

private boolean conectadas_recursivo(Terreno terreno, Casilla c1, Casilla objetivo, Set<Casilla> visitadas) {
  if (c1.equals(objetivo))
      return true;
  visitadas.add(c1);
  for (Direccion direccion : Direccion.values()) {
      if (c1.hayPared(direccion))
          continue;
      Casilla sig = terreno.getCasilla(c1, direccion);
      if (sig == null)
          continue;
      if (visitadas.contains(sig))
          continue;
      if (conectadas_recursivo(terreno, sig, objetivo, visitadas))
          return true;
  }
  return false;
}

private boolean conectadas_iterativo(Terreno terreno, Casilla c1, Casilla objetivo) {
  Set<Casilla> visitadas = new HashSet<Casilla>();
  List<Casilla> todo = new ArrayList<Casilla>();
  todo.add(c1);
  do {
      Casilla casilla = todo.remove(0);
      if (casilla.equals(objetivo))
          return true;
      visitadas.add(casilla);
      for (Direccion direccion : Direccion.values()) {
          if (casilla.hayPared(direccion))
              continue;
          Casilla sig = terreno.getCasilla(casilla, direccion);
          if (sig == null)
              continue;
          if (visitadas.contains(sig))
              continue;
          todo.add(sig);
      }
  } while (todo.size() > 0);
  return false;
}
/** 
* Plantamos una pared en el terreno. Las paredes separan casillas, de forma que hay 2 casillas afectadas.
* @param casilla una de las casillas en la que colocar la pared.
* @param direccion en que lado de la casilla se coloca.
*/
	public synchronized void ponPared(Casilla casilla, Direccion direccion){
		// Metodo que pone una pared pero hay que ponerla en dos casillas
		// 1. en la que nos dan
		casilla.ponPared(direccion);
		Casilla casillaOpuesta = getCasilla(casilla, direccion);
		// 2. en la casilla opuesta en la direccion opuesta a la que nos dan si esta no es null es decir esta dentro de los limites del terreno.
		if(casillaOpuesta != null) casillaOpuesta.ponPared(direccion.opuesto());
	}
	
/** 
* Quitamos una pared del terreno. Las paredes separan casillas, de forma que hay 2 casillas afectadas.
* @param casilla una de las casillas de la que retirar la pared.
* @param direccion de que lado de la casilla se retira
*/
	public synchronized void quitaPared(Casilla casilla, Direccion direccion){
		// Metodo que quita una pared pero hay que quitarla en dos casillas
		// 1. en la que nos dan
		casilla.quitaPared(direccion);
		
		Casilla casillaOpuesta = getCasilla(casilla, direccion);
		// 2. en la casilla opuesta en la direccion opuesta a la que nos dan si esta no es null es decir esta dentro de los limites del terreno.
		if(casillaOpuesta != null) casillaOpuesta.quitaPared(direccion.opuesto());
	}
		
/** 
* Pregunta si mirando desde una casilla en una cierta direccion nos topamos con una pared. Si estamos al 
* borde del terreno, también devuelve TRUE.
* @param casilla casilla desde la que miramos.
* @param direccion en que direccion miramos.
* @return cierto si hay pared.
*/
	public synchronized boolean hayPared(Casilla casilla, Direccion direccion){
		return casilla.hayPared(direccion);
	}	
		
/** 
* Marca una posicion como objetivo para que el jugador gane.
* @param x  posicion horizontal (0..n-1).
* @param y  posicion vertical (0..n-1).
* @return true si es posible marcar la casilla como objetivo; false si no es posible.
*/
	public synchronized boolean setObjetivo(int x, int y){ 
		if (x<0 || x>(getN()-1) ||y<0 || y>(getN()-1)) return false; // no es posible fijar el objetivo si esto fuera de los limites del terreno
		if (casillas[x][y].getMovil()!= null) return false; // Habría que tener en cuenta si hay bichos ya en la casilla
		else
			casillas[x][y].setObjetivo(true); // Pone el objetivo en la casilla cuyas coordenadas nos dan
			return true;// y devuelve true
	}
		
	// nuevos métodos que no aparecían en el laboratorio1
/** 
 * Método que para los moviles tanto jugadores como fantasmas, quedándose
 * congelados en el terreno.
 */	
	public synchronized void paraMoviles(){
		for (int i=0; i<getN(); i++){// se podría poner casillas.length en vez de n;
			for (int j=0; j<getN(); j++){
				if(casillas[i][j] != null && (casillas[i][j].getMovil() != null))
					casillas[i][j].getMovil().muere(false);

			}
		}
		view.pintame();
	}
/** 
 * Método que elimina los moviles tanto jugadores como fantasmas, borrandolos del terreno.
 */
	public synchronized void limpiaTerreno(){
		
		for (int i=0; i<getN();i++){// se podría poner casillas.length en vez de n;
			for (int j=0; j<getN();j++){
				if(casillas[i][j] != null && (casillas[i][j].getMovil() != null)){
					casillas[i][j].quitaParedes();
					casillas[i][j].getMovil().muere(true);
				}
			}
		}	
		view.pintame();
	}
/** 
 * Método que pone el terrreno en situación inicial.
 */
	public synchronized void ponSituacionInicial(){
		estado = EstadoJuego.JUGANDO;
		ponParedes();
		quitaTrampasLlave();
		setObjetivo(getN()-1,getN()-1);
		Casilla origen = new Casilla(0,0);
		put(origen.getX(), origen.getY(), jugador);
		view.setJugador(jugador);
		
		view.pintame();
		
	}
/**
 * Recorre todas las casillas eliminando trampas y llaves.	
 */
	public synchronized void quitaTrampasLlave(){
		for (int i=0; i<getN();i++){
			for (int j=0; j<getN();j++){
				// Recorremos todas las casillas
				if (casillas[i][j].isLlave()) 
					casillas[i][j].setLlave(false);
				// Si la casilla es una llave se pone a false quitando la llave
				if (casillas[i][j].isTrampa()) 
					casillas[i][j].setTrampa(false);
				// Si la casilla es una trampa se pone a false quitando la trampa
			}
		}
		view.pintame();
	}
}
