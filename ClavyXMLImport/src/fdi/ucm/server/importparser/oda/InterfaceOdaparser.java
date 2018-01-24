package fdi.ucm.server.importparser.oda;

/**
 * Interface parseModel, funciones necesarias para parseal un objeto, parsear su modelo y sus instancias
 * @author Joaquin Gayoso-Cabada
 *
 */
public interface InterfaceOdaparser {

	/**
	 * Funcion inicial del proceso del modelo.
	 */
	public void ProcessAttributes();
	
	/**
	 * Funcion inicial del proceso las instancias.
	 */
	public void ProcessInstances();
}
