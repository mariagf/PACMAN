package es.upm.dit.adsw.pacman3;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.*;

/**
 * Bateria de pruebas sobre el terreno. He cambiado 
 * las mías por las que venían en el javadoc.
 */
public class TestTerreno {
    public static final int N = 10;

    private Terreno terreno;
    private Jugador jugador;

    @Before
    public void setup() {
        terreno = new Terreno(N);
        // visor nulo
        terreno.setView(new TestingView());
        // sin paredes
        jugador = new Jugador(terreno);
        Casilla casilla1 = new Casilla(2,2);
        casilla1.setTrampa(true);
        Casilla casilla2 = new Casilla(4,2);
        casilla2.setTrampa(true);
        Casilla casilla3 = new Casilla(8,8);
        casilla3.setTrampa(true);
    }

    @Test
    public void testGetCasilla01() {
        Casilla casilla = terreno.getCasilla(2, 3);
        assertEquals(2, casilla.getX());
        assertEquals(3, casilla.getY());
    }

    @Test
    public void testGetCasilla012() {
        Casilla casilla = terreno.getCasilla(3, N - 1);
        assertEquals(3, casilla.getX());
        assertEquals(N - 1, casilla.getY());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetCasilla03() {
        Casilla casilla = terreno.getCasilla(0, N);
    }

    @Test
    public void testPut01() {
        assertTrue(terreno.put(2, 3, jugador));
        assertEquals(jugador, terreno.getCasilla(2, 3).getMovil());
    }

    @Test
    public void testPut02() {
        terreno.put(0, N - 1, jugador);
        assertEquals(jugador, terreno.getCasilla(0, N - 1).getMovil());
    }

    @Test
    public void testPut03() {
        assertFalse(terreno.put(0, N, jugador));
    }

    @Test
    public void testMove01() {
        Casilla origen = terreno.getCasilla(3, 5);
        terreno.put(origen.getX(), origen.getY(), jugador);
        Casilla destino = terreno.move(jugador, Direccion.NORTE);
        assertEquals(3, destino.getX());
        assertEquals(6, destino.getY());
        assertEquals(jugador, destino.getMovil());
        assertNull(origen.getMovil());
    }

    @Test
    public void testMove02() {
        Casilla origen = terreno.getCasilla(3, N - 1);
        terreno.put(origen.getX(), origen.getY(), jugador);
        Casilla destino = terreno.move(jugador, Direccion.NORTE);
        assertEquals(origen, destino);
        assertEquals(jugador, origen.getMovil());
    }

    @Test
    public void testParedes00() {
        terreno.ponParedes();
        Casilla c1 = terreno.getCasilla(0, 0);
        Casilla c2 = terreno.getCasilla(N - 1, N - 1);
        assertTrue(conectadas(terreno, c1, c2));
    }
    

    private boolean conectadas(Terreno terreno, Casilla c1, Casilla c2) {
//      Set<Casilla> visitadas = new HashSet<Casilla>();
//      return conectadas_recursivo(terreno, c1, c2, visitadas);
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
}
