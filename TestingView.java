package es.upm.dit.adsw.pacman3;

/**
 * Vista del juego.
 * Implementacion para pruebas que básicamente no hace nada.
 *
 * @author Jose A. Manas
 * @author Juan A. de la Puente
 * @version 17.2.2014
 */
// Esta clase la daban y supongo que no hay que tocar nada.
public class TestingView
        implements View {

    /**
     * Registra un fantasma.
     * Se usara para poder crear diferentes tipos de fantasmas.
     *
     * @param nombre texto para identificar al tipo de fantasma.
     * @param clase  clase que implementa el fantasma.
     */
    public void registra(String nombre, Class clase) {
    }

    /**
     * Refresca la pantalla.
     */
    public void pintame() {
    	
    }

    /**
     * Informa a la vista del movil que hace de jugador.
     * Es el que se movera con las flechas.
     *
     * @param jugador jugador sobre el terreno.
     */
    public void setJugador(Jugador jugador) {
    }

    /**
     * Muestra un mensaje en una pantalla emergente sobre el terreno.
     *
     * @param mensaje texto a presentar en la ventana.
     */
    public void muestra(String mensaje) {
    }
}
