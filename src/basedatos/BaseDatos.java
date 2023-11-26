package basedatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import exception.BaseDatosException;

public class BaseDatos {
	private Connection conexion;
	private String controlador = "org.sqlite.JDBC";
	private String url = "jdbc:sqlite:";

	public BaseDatos(String archivo) {
		this.url = url + archivo;
	}

	protected boolean realizarConexion() throws BaseDatosException {
		try {
			Class.forName(controlador);
			conexion = DriverManager.getConnection(url);
			return true;
		} catch (ClassNotFoundException e) {
			throw new BaseDatosException(BaseDatosException.NO_SE_ENCONTRO_DRIVER);
		} catch (SQLException e) {
			throw new BaseDatosException(BaseDatosException.BD_NO_ENCONTRADA);
		}
	}

	protected void cerrarConexion() throws BaseDatosException {
		try {
			conexion.close();
		} catch (SQLException e) {
			throw new BaseDatosException(BaseDatosException.DESCONEXION);
		}
	}

	protected ResultSet realizarConsulta(String consulta) throws BaseDatosException {
		try {
			Statement instruccion;
			instruccion = conexion.createStatement();
			return instruccion.executeQuery(consulta);
		} catch (SQLException e) {
			throw new BaseDatosException(BaseDatosException.ERROR_EN_CONSULTA);
		}
	}

	protected int realizarAccion(String accion) throws BaseDatosException {
		try {
			Statement instruccion;
			instruccion = conexion.createStatement();
			return instruccion.executeUpdate(accion);
		} catch (SQLException e) {
			throw new BaseDatosException(BaseDatosException.ERROR_EN_ACCION);
		}
	}

	protected Vector<String> consultarTablas() throws BaseDatosException {
		try {
			String[] tipoTabla = { "TABLE" };
			ResultSet nomTablas;
			nomTablas = conexion.getMetaData().getTables(null, null, null, tipoTabla);
			Vector<String> tablas = new Vector<String>();
			while (nomTablas.next()) {
				tablas.add(nomTablas.getString(3));
			}
			return tablas;
		} catch (SQLException e) {
			throw new BaseDatosException(BaseDatosException.PROBLEMA_BD);
		}
	}

	public String getControlador() {
		return controlador;
	}

	public String getUrl() {
		return url;
	}
}
