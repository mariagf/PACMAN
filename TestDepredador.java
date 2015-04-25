package es.upm.dit.adsw.pacman3;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.*;

/**
 * Bateria de pruebas sobre el algoritmo de busqueda.
 */
public class TestDepredador {
    public static final int N = 5;

    private Terreno terreno;
    private Depredador depredador;

    @Before
    public void setup() {
        terreno = new Terreno(N);
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                Casilla casilla = terreno.getCasilla(x, y);
                terreno.ponPared(casilla, Direccion.NORTE);
                terreno.ponPared(casilla, Direccion.SUR);
                terreno.ponPared(casilla, Direccion.ESTE);
                terreno.ponPared(casilla, Direccion.OESTE);
            }
        }
        terreno.quitaPared(terreno.getCasilla(0, 0), Direccion.NORTE);
        terreno.quitaPared(terreno.getCasilla(1, 0), Direccion.NORTE);
        terreno.quitaPared(terreno.getCasilla(1, 0), Direccion.ESTE);
        terreno.quitaPared(terreno.getCasilla(2, 0), Direccion.ESTE);
        terreno.quitaPared(terreno.getCasilla(3, 0), Direccion.ESTE);
        terreno.quitaPared(terreno.getCasilla(4, 0), Direccion.NORTE);
        terreno.quitaPared(terreno.getCasilla(0, 1), Direccion.NORTE);
        terreno.quitaPared(terreno.getCasilla(0, 1), Direccion.ESTE);
        terreno.quitaPared(terreno.getCasilla(2, 1), Direccion.NORTE);
        terreno.quitaPared(terreno.getCasilla(2, 1), Direccion.ESTE);
        terreno.quitaPared(terreno.getCasilla(3, 1), Direccion.ESTE);
        terreno.quitaPared(terreno.getCasilla(0, 2), Direccion.ESTE);
        terreno.quitaPared(terreno.getCasilla(1, 2), Direccion.ESTE);
        terreno.quitaPared(terreno.getCasilla(2, 2), Direccion.NORTE);
      
    
        depredador = new Depredador(terreno);
    }
    @Test
    public void testFantasmaTunel01() {
    	
    	System.out.println("---------PRUEBA FANTASMA 1---------");
        
    	Casilla o = terreno.getCasilla(0, 0);
    	Casilla a = terreno.getCasilla(2, 2);
        Casilla b = terreno.getCasilla(3, 3);
        
        a.setTrampa(true);
        b.setTrampa(true);
        FantasmaTunel fantasmaTunel = new FantasmaTunel(terreno);
        
        terreno.put(2, 3, fantasmaTunel);
        terreno.move(fantasmaTunel, Direccion.SUR);
        terreno.move(fantasmaTunel, Direccion.SUR);
        assertEquals(o, fantasmaTunel.getCasilla());
        assertFalse(a.isTrampa());
    }
    @Test
    public void testFantasmaTunel02() {
    	
    	System.out.println("---------PRUEBA FANTASMA 2---------");
        setup();
    	Casilla o = terreno.getCasilla(0, 0);
    	Casilla a = terreno.getCasilla(2, 0);
        Casilla b = terreno.getCasilla(4, 1);
        Casilla c = terreno.getCasilla(1, 2);
        a.setTrampa(true);
        b.setTrampa(true);
        c.setTrampa(true);
        FantasmaTunel fantasmaTunel = new FantasmaTunel(terreno);
        
        terreno.put(2, 0, fantasmaTunel);
        terreno.move(fantasmaTunel, Direccion.OESTE);
      
        System.out.println(fantasmaTunel.getCasilla());
        assertEquals(o, fantasmaTunel.getCasilla());
        assertFalse(a.isTrampa());
        assertTrue(b.isTrampa());
        assertTrue(c.isTrampa());
    }
    @Test
    public void testCasillaDestino01() {
    	
    	System.out.println("---------PRUEBA CASILLA DESTINO 1---------");
        Jugador jugador = new Jugador(terreno);
    	Casilla c = new Casilla(1,3);       
        terreno.put(1, 3, jugador);
        assertEquals(c, depredador.getCasillaDestino());
        
        
    }
    @Test
    
    public void testCasillaDestino02() {
    	
    	System.out.println("---------PRUEBA CASILLA DESTINO 2---------");
        
        Jugador jugador = new Jugador(terreno);
    	Casilla c = new Casilla(0,2);       
        terreno.put(0, 2, jugador);
        assertEquals(c, depredador.getCasillaDestino());
    }
    @Test
    public void testBFS01() {
    	System.out.println("---------PRIMERA PRUEBA---------");
        Casilla a = terreno.getCasilla(0, 0);
        Casilla b = terreno.getCasilla(0, 1);
       
        assertEquals(terreno.getCasilla(0, 1), depredador.bfs(a, b));
        assertEquals(terreno.getCasilla(0, 0), depredador.bfs(b, a));
    }

    @Test
    public void testBFS02() {
    	System.out.println("---------SEGUNDA PRUEBA---------");
        Casilla a = terreno.getCasilla(0, 0);
        Casilla b = terreno.getCasilla(0, 2);
     
        assertEquals(terreno.getCasilla(0, 1), depredador.bfs(a, b));
        assertEquals(terreno.getCasilla(0, 1), depredador.bfs(b, a));
    }
   
    @Test
    public void testBFS03() {
    	System.out.println("---------TERCERA PRUEBA---------");
        Casilla a = terreno.getCasilla(0, 0);
        Casilla b = terreno.getCasilla(0, 3);
        assertNull(depredador.bfs(a, b));
        assertNull(depredador.bfs(b, a));
    }
    @Test
    public void testBFSx() {
    	System.out.println("---------X PRUEBA---------");
        Casilla a = terreno.getCasilla(2, 3);
        Casilla b = terreno.getCasilla(1, 1);
   
        assertEquals(terreno.getCasilla(2, 2), depredador.bfs(a, b));
        assertEquals(terreno.getCasilla(0, 1), depredador.bfs(b, a));
    }
       
    @Test
    public void testBFS05() {
    	
    	System.out.println("---------QUINTA PRUEBA---------");
    	Casilla a = terreno.getCasilla(0, 0);
        Casilla b = terreno.getCasilla(1, 4);
        assertNull(depredador.bfs(a, b));
        assertNull(depredador.bfs(b, a));
    }
    
    @Test
    public void testBFS06() {
  
    	System.out.println("---------SEXTA PRUEBA---------");
    	Casilla a = terreno.getCasilla(4, 0);
        Casilla b = terreno.getCasilla(1, 1);
      
        assertEquals(terreno.getCasilla(3, 0), depredador.bfs(a, b));
        assertEquals(terreno.getCasilla(1, 0), depredador.bfs(b, a));
    }
    @Test
    public void testBFS07() {
  
    	System.out.println("---------SÉPTIMA PRUEBA---------");
    	Casilla a = terreno.getCasilla(0, 0);
        Casilla b = terreno.getCasilla(1, 0);
      
        assertEquals(terreno.getCasilla(0, 1), depredador.bfs(a, b));
        assertEquals(terreno.getCasilla(1, 1), depredador.bfs(b, a));
    }
    @Test
    public void testBFS08() {
  
    	System.out.println("---------OCTAVA PRUEBA---------");
    	Casilla a = terreno.getCasilla(1, 1);
        Casilla b = terreno.getCasilla(4, 1);
      
        assertEquals(terreno.getCasilla(1, 0), depredador.bfs(a, b));
        assertEquals(terreno.getCasilla(4, 0), depredador.bfs(b, a));
    }
    @Test
    public void testBFS09() {
  
    	System.out.println("---------NOVENA PRUEBA---------");
    	Casilla a = terreno.getCasilla(2, 0);
        Casilla b = terreno.getCasilla(2, 1);
      
        assertEquals(terreno.getCasilla(3, 0), depredador.bfs(a, b));
        assertEquals(terreno.getCasilla(3, 1), depredador.bfs(b, a));
    }
    @Test
    public void testBFS10() {
  
    	System.out.println("---------DÉCIMA PRUEBA---------");
    	Casilla a = terreno.getCasilla(2, 3);
        Casilla b = terreno.getCasilla(2, 0);
      
        assertEquals(terreno.getCasilla(2, 2), depredador.bfs(a, b));
        assertEquals(terreno.getCasilla(3, 0), depredador.bfs(b, a));
    }
   
}