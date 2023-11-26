package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import basedatos.BaseDatosLibros;
import basedatos.ResultSetTableModel;
import exception.BaseDatosException;

public class ConsultaLibros extends JFrame implements ActionListener {
	private JCheckBox casillaIsbn;
	private JTextField isbn;

	private JCheckBox casillaNombre;
	private JTextField nombre;

	private JCheckBox casillaAutor;
	private JComboBox<String> autor;

	private JCheckBox casillaEditorial;
	private JComboBox<String> editorial;

	private JCheckBox casillaTema;
	private JComboBox<String> tema;

	private JButton botonConsultar;

	private JTable tablaLibros;

	private BaseDatosLibros bd;

	private String CONSULTA_PREDETERMINADA = "SELECT isbn AS 'ISBN', nombreLibro AS 'Nombre del libro', nombreAutor AS 'Nombre del autor', nombreEditorial AS 'Editorial', edicion AS 'Edición', archivo AS 'Archivo', descripcion AS 'Tema' "
			+ "FROM libro, autor, editorial, tema WHERE Libro.idAutor = Autor.idAutor AND Libro.idEditorial = editorial.idEditorial AND Libro.idTema = tema.idTema";
	// "SELECT * FROM libro"; //FIXME

	private ResultSetTableModel modelo;

	private static final long serialVersionUID = 1L;

	public ConsultaLibros() {
		super("Consulta de libros");
		setSize(1000, 700);
		setResizable(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		JPanel panelNorte = new JPanel();

		panelNorte.setLayout(new GridLayout(2, 6, 0, 0));

		JPanel panelTmp;

		panelTmp = new JPanel();
		casillaIsbn = new JCheckBox("ISBN");
		panelTmp.add(casillaIsbn);
		panelNorte.add(panelTmp);

		isbn = new JTextField();
		isbn.setPreferredSize(new Dimension(140, 25));
		panelTmp.add(isbn);
		panelNorte.add(panelTmp);

		panelTmp = new JPanel();
		casillaNombre = new JCheckBox("Nombre");
		panelTmp.add(casillaNombre);
		panelNorte.add(panelTmp);

		nombre = new JTextField();
		nombre.setPreferredSize(new Dimension(140, 25));
		panelTmp.add(nombre);
		panelNorte.add(panelTmp);

		panelTmp = new JPanel();
		casillaAutor = new JCheckBox("Autor");
		panelTmp.add(casillaAutor);
		panelNorte.add(panelTmp);

		autor = new JComboBox<String>();
		autor.setPreferredSize(new Dimension(150, 25));
		autor.setEditable(true);
		panelTmp.add(autor);
		panelNorte.add(panelTmp);

		panelTmp = new JPanel();
		casillaEditorial = new JCheckBox("Editorial");
		panelTmp.add(casillaEditorial);
		panelNorte.add(panelTmp);

		editorial = new JComboBox<String>();
		editorial.setPreferredSize(new Dimension(150, 25));
		editorial.setEditable(true);
		panelTmp.add(editorial);
		panelNorte.add(panelTmp);

		panelTmp = new JPanel();
		casillaTema = new JCheckBox("Tema");
		panelTmp.add(casillaTema);
		panelNorte.add(panelTmp);

		tema = new JComboBox<String>();
		tema.setPreferredSize(new Dimension(190, 25));
		tema.setEditable(true);
		panelTmp.add(tema);
		panelNorte.add(panelTmp);

		panelTmp = new JPanel();
		botonConsultar = new JButton("Consultar");
		botonConsultar.addActionListener(this);
		panelTmp.add(botonConsultar);
		panelNorte.add(panelTmp);

		panelNorte.setPreferredSize(new Dimension(990, 200));

		this.add(panelNorte, BorderLayout.NORTH);

		/***************************************************************************************************/
		try {
			bd = new BaseDatosLibros("Libros.db");
			inicializar();
		} catch (BaseDatosException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Conexión no establecida", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Creación de tablas", JOptionPane.ERROR_MESSAGE);
		}

		/***************************************************************************************************/
		JPanel panelCentro = new JPanel();

		try {
			modelo = new ResultSetTableModel(bd.getControlador(), bd.getUrl(), CONSULTA_PREDETERMINADA);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Faltan controladores", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Extracción de la Batos de Datos",
					JOptionPane.ERROR_MESSAGE);
		}

		tablaLibros = new JTable(modelo);
		JScrollPane panel = new JScrollPane(tablaLibros);
		panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel.setPreferredSize(new Dimension(900, 400));
		panelCentro.add(panel);
		this.add(panelCentro, BorderLayout.CENTER);

		/** Clase interna para cerrar ventana **/
		class MiEventoVentana extends WindowAdapter {
			public void windowClosing(WindowEvent arg0) {
				cerrar();
				System.exit(0);
			}
		}

		this.addWindowListener(new MiEventoVentana());

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource().equals(botonConsultar)) {

			consultar();
		}
	}

	public static void main(String[] args) {
		new ConsultaLibros();
	}

	public void inicializar() {
		try {
			Vector<String> auxConsulta;
			auxConsulta = bd.consultarAutores();

			for (String mat : auxConsulta) {
				autor.addItem(mat);
			}

			auxConsulta = bd.consultarEditoriales();

			for (String auxEditorial : auxConsulta) {
				editorial.addItem(auxEditorial);
			}

			auxConsulta = bd.consultarTemas();

			for (String auxTemas : auxConsulta) {
				tema.addItem(auxTemas);
			}

		} catch (BaseDatosException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Extracción de la Batos de Datos",
					JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Base No disponible", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void consultar() {
		String consulta = CONSULTA_PREDETERMINADA;// FIXME

		if (casillaIsbn.isSelected()) {
			consulta += " AND isbn LIKE '%" + isbn.getText() + "%'";
		}
		if (casillaNombre.isSelected()) {
			consulta += " AND nombreLibro LIKE '%" + nombre.getText() + "%'";
		}
		if (casillaAutor.isSelected()) {
			consulta += " AND nombreAutor LIKE '%" + (String) autor.getSelectedItem() + "%'";
		}
		if (casillaEditorial.isSelected()) {
			consulta += " AND nombreEditorial LIKE '%" + (String) editorial.getSelectedItem() + "%'";
		}
		if (casillaTema.isSelected()) {
			consulta += " AND descripcion LIKE '%" + (String) tema.getSelectedItem() + "%'";
		}

		try {
			modelo.establecerConsulta(consulta);
		} catch (IllegalStateException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, BaseDatosException.ERROR_EN_CONSULTA, "Extracción de Datos",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	private void cerrar() {
		modelo.desconectar();
	}
}
