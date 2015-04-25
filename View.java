package es.upm.dit.adsw.pacman3;

/**
 * Vista del juego.
 * Cubre View + Controller en un modelo MVC.
 * Se presta a diversas implementaciones.
 *
 * @author Jose A. Manas
 * @author Juan A. de la Puente
 * @version 17.2.2014
 */
public interface View {

    /**
     * Registra un fantasma.
     * Se usará para poder crear diferentes tipos de fantasmas.
     *
     * @param nombre texto para identificar al tipo de fantasma.
     * @param clase  clase que implementa el fantasma.
     */
    void registra(String nombre, Class<?> clase);

    /**
     * Le dice al visor que refresque la pantalla.
     */
    void pintame();

    /**
     * Informa al tablero del móvil que hace de jugador.
     * Es el que se moverá con las flechas.
     *
     * @param jugador jugador sobre el terreno.
     */
    void setJugador(Jugador jugador);

    /**
     * Muestra un mensaje en una pantalla emergente sobre el terreno.
     *
     * @param mensaje texto a presentar en la ventana.
     */
    void muestra(String mensaje);
}
