package basedatos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import exception.BaseDatosException;

public class BaseDatosLibros extends BaseDatos {

	public BaseDatosLibros(String archivo) throws BaseDatosException, SQLException {
		super(archivo);
		if (!validarBD()) {
			crearBD();
			llenarBD();
		}
	}

	private boolean validarBD() throws BaseDatosException, SQLException {
		realizarConexion();
		Vector<String> tablas = new Vector<String>();
		tablas = consultarTablas();
		cerrarConexion();
		if (tablas.size() > 0) {
			for (String auxTablas : tablas) {
				if (auxTablas.compareTo("autor") == 0) {
					return true;
				}
			}
			throw new BaseDatosException(BaseDatosException.NO_ES_LA_BASE);
		} else {
			return false;
		}
	}

	private void crearBD() throws BaseDatosException {
		try {
			realizarConexion();
			realizarAccion(
					"CREATE TABLE autor(idAutor INTEGER PRIMARY KEY autoincrement NOT NULL , nombreAutor VARCHAR(40))");

			realizarAccion(
					"CREATE TABLE editorial(idEditorial INTEGER PRIMARY KEY autoincrement NOT NULL, nombreEditorial VARCHAR(40))");

			realizarAccion(
					"CREATE TABLE tema(idTema INTEGER PRIMARY KEY autoincrement NOT NULL, descripcion VARCHAR(60))");

			realizarAccion(
					"CREATE TABLE libro(isbn INTEGER NOT NULL PRIMARY KEY, nombreLibro VARCHAR(30), idAutor INTEGER, idEditorial INTEGER,"
							+ "edicion INTEGER, archivo VARCHAR(40), idTema INTEGER, FOREIGN KEY(idAutor) REFERENCES autor(idAutor) ON DELETE CASCADE ON UPDATE CASCADE, "
							+ "FOREIGN KEY(idEditorial) REFERENCES editorial(idEditorial) ON DELETE CASCADE ON UPDATE CASCADE, "
							+ "FOREIGN KEY(idTema) REFERENCES tema(idTema) ON DELETE CASCADE ON UPDATE CASCADE)");
			cerrarConexion();
		} catch (BaseDatosException e) {
			throw new BaseDatosException(BaseDatosException.NO_SE_PUDO_CREAR_LA_TABLA);
		}
	}

	private void llenarBD() throws BaseDatosException {
		realizarConexion();

		realizarAccion("INSERT INTO autor VALUES(null, 'Emilio García Gómez')");
		realizarAccion("INSERT INTO autor VALUES(null, 'Juan Carlos Onetti')");
		realizarAccion("INSERT INTO autor VALUES(null, 'Mario Vargas Llosa')");
		realizarAccion("INSERT INTO autor VALUES(null, 'Desconocido')");
		realizarAccion("INSERT INTO autor VALUES(null, 'David Solano')");

		realizarAccion("INSERT INTO editorial VALUES(null, 'Acantilado')");
		realizarAccion("INSERT INTO editorial VALUES(null, 'Aguilar')");
		realizarAccion("INSERT INTO editorial VALUES(null, 'Akal')");
		realizarAccion("INSERT INTO editorial VALUES(null, 'Grifo')");

		realizarAccion("INSERT INTO tema VALUES(null, 'Terror')");
		realizarAccion("INSERT INTO tema VALUES(null, 'Suspenso y Fantasia')");
		realizarAccion("INSERT INTO tema VALUES(null, 'Aventura')");
		realizarAccion("INSERT INTO tema VALUES(null, 'Fantasia')");
		realizarAccion("INSERT INTO tema VALUES(null, 'Acción')");

		realizarAccion("INSERT INTO libro VALUES(1234567890123, 'La cueva', 1, 1,1, 'PDF', 1)");
		realizarAccion("INSERT INTO libro VALUES(1239876543210, 'El lobo', 2, 2,2, 'Papel', 2)");
		realizarAccion("INSERT INTO libro VALUES(1478523690147, 'La luz', 3, 3,3, 'txt', 3)");
		realizarAccion("INSERT INTO libro VALUES(1478523690146, 'La oscuridad', 4, 4,5, 'PDF', 5)");
		realizarAccion("INSERT INTO libro VALUES(1478523690864, 'Soledad', 5, 4,3, 'Papel', 4)");
		realizarAccion("INSERT INTO libro VALUES(9638523690147, 'Perfume', 2, 2,3, 'txt', 1)");
		realizarAccion("INSERT INTO libro VALUES(1848523690147, '100 Años de Soledad', 1, 4,10, 'PDF', 2)");

		cerrarConexion();
	}

	public Vector<String> consultarAutores() throws BaseDatosException, SQLException {

		realizarConexion();
		Vector<String> nombre = new Vector<String>();
		ResultSet resultados = realizarConsulta("SELECT nombreAutor FROM autor ORDER BY nombreAutor ASC");
		while (resultados.next()) {
			nombre.add(resultados.getString(1));
		}
		resultados.close();
		cerrarConexion();
		return nombre;
	}

	public Vector<String> consultarEditoriales() throws BaseDatosException, SQLException {

		realizarConexion();
		Vector<String> nombre = new Vector<String>();
		ResultSet resultados = realizarConsulta("SELECT nombreEditorial FROM editorial ORDER BY nombreEditorial ASC");
		while (resultados.next()) {
			nombre.add(resultados.getString(1));
		}
		resultados.close();
		cerrarConexion();
		return nombre;
	}

	public Vector<String> consultarTemas() throws BaseDatosException, SQLException {

		realizarConexion();
		Vector<String> nombre = new Vector<String>();
		ResultSet resultados = realizarConsulta("SELECT descripcion FROM tema ORDER BY descripcion ASC");
		while (resultados.next()) {
			nombre.add(resultados.getString(1));
		}
		resultados.close();
		cerrarConexion();
		return nombre;
	}

}
