package es.upm.dit.adsw.pacman3;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
/**
 * Modela jugadores y fantasmas.
 * @version 1, 07/04/14
 * @author Maria Garcia Fernandez
 */
public abstract class Movil {
/** 
* Getter.
* @return en que casilla me encuentro.
*/	
	public abstract Casilla getCasilla();
/** 
* Proporciona como quiere ser presentado graficamente.
* @return una imagen adecuada.
*/		
	public abstract java.awt.Image getImagen();
/** 
* Setter.
* @param en que casilla me colocan.
*/	
	public abstract void setCasilla(Casilla casilla);
/** 
* Lo que tengo que hacer cuando me eliminan. Cuando desaparezco del terreno de juego.
*/		
	public abstract void muere(boolean devorado);
/** 
* Alguien pregunta si mpuedo moverme ahora o en el futuro en una cierta direccion.
* @param direccion direccion en la que intento moverme.
* @return 0 si puedo moverme en esa direccion. 1 si ahora mismo no puedo, pero en 
* el futuro puede que si.Solo pueden devolver 1 los fantasmas en los jugadores ya 
* no se contempla porque si hay un bicho muere 2 si no puedo moverme, ni ahora ni 
* nunca mas.
*/	
	// 0 - si puedo moverme en esa direccion. 
	// 1 - si ahora mismo no puedo, pero en el futuro puede que si.(solo para fantasmas)
	// 2 - si no puedo moverme, ni ahora ni nunca mas.	
	public abstract int puedoMoverme(Direccion direccion);
/** 
* Carga una imagen de un fichero.
* @param fichero con la imagen
* @return imagen en formato java.
*/	
	protected Image loadImage(String fichero) { 
		 String path = "imgs/" + fichero; 
		 Class<Juego> root = Juego.class; 
		 try { 
			 URL url = root.getResource(path); 
			 ImageIcon icon = new ImageIcon(url); 
			 return icon.getImage(); 
		 } catch (Exception e) { 
			 System.err.println("No ha sido posible cargar la imagen."); 
			 return null; 
		   } 
	} 

	
}
