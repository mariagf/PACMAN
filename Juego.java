package es.upm.dit.adsw.pacman3;

/**
 * Arranca el juego.
 *
 * @author jose a. manas
 * @version 17.2.2014
 */
public class Juego {
    public static final int N = 15;

    /**
     * Crea el terreno y el jugador.
     * Crea la interfaz de usuario y le cede el control.
     *
     * @param args no se usa.
     */
    public static void main(String[] args) {
        Terreno terreno = new Terreno(N);
        View view = new GUI(terreno);
        view.registra("Fantasma básico", Fantasma00.class);
        view.registra("FantasmaTunel", FantasmaTunel.class);
        view.registra("Depredador", Depredador.class);
       
        terreno.setView(view);
        terreno.limpiaTerreno();
        terreno.ponSituacionInicial();
        view.pintame();
      
    }
}
