package exception;

public class BaseDatosException extends Exception {
	private static final long serialVersionUID = 1L;
	public static final String BD_NO_ENCONTRADA = "La base de datos no se encontró";
	public static final String NO_SE_ENCONTRO_DRIVER = "No se encontraron los controladores para la conexión de la base de datos";
	public static final String DESCONEXION = "Ya se desconectó de la base de datos";
	public static final String ERROR_EN_CONSULTA = "La consulta no se pudo realizar por errores en la base de datos";
	public static final String ERROR_EN_ACCION = "La operación no se efectuó en la base de Datos";
	public static final String NO_SE_PUDO_CREAR_LA_TABLA = "La tabla no se pudo crear en la base de datos";
	public static final String NO_ES_LA_BASE = "La base de datos a la que se conectó no tiene los datos del sistema";
	public static final String PROBLEMA_BD = "La base de datos no se puede usar";

	public BaseDatosException(String msg) {
		super(msg);
	}
}
