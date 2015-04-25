package es.upm.dit.adsw.pacman3;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Bateria de pruebas sobre casillas.
 */
public class TestCasilla {

    @Test
    public void testGetDireccion00() {
        Casilla a = new Casilla(0, 0);
        Casilla b = new Casilla(4, 5);
        assertNull(a.getDireccion(b));
        assertNull(b.getDireccion(a));
    }

    @Test
    public void testGetDireccion01() {
        Casilla a = new Casilla(4, 4);
        Casilla b = new Casilla(4, 5);
        assertEquals(Direccion.NORTE, a.getDireccion(b));
        assertEquals(Direccion.SUR, b.getDireccion(a));
    }

}

