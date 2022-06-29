package banco;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class datosClienteCrearPrestamo extends JFrame {

	private logInEmpleadoPrestamo vPrestamo;
	private int empleadoLogueado;
	protected Connection conexionBD = null;
	private crearNuevoPrestamo cNuevoPrestamo;
	private JPanel contentPane;
	private final JLabel lblTipoDoc = new JLabel("Tipo Documento:");
	private JComboBox<String>comboTipoDoc;
	private final JLabel lblNumeroDocumento = new JLabel("Número Documento:");
	private final JTextField campoNroDoc = new JTextField();
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu mnMenu = new JMenu("Menú");
	private final JButton btnSeleccionar = new JButton("Seleccionar");
	private final JMenuItem mntmConsultas = new JMenuItem("Consultas");
	private final JMenuItem mntmAtm = new JMenuItem("ATM");
	private final JMenuItem mntmPrstamos = new JMenuItem("Préstamos");
	private final JMenuItem mntmSalir = new JMenuItem("Salir");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					datosClienteCrearPrestamo frame = new datosClienteCrearPrestamo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public datosClienteCrearPrestamo() {
		campoNroDoc.setHorizontalAlignment(SwingConstants.CENTER);
		campoNroDoc.setFont(new Font("Tahoma", Font.BOLD, 15));
		campoNroDoc.setBounds(330, 212, 132, 22);
		campoNroDoc.setColumns(10);
		initGUI();
	}
	private void initGUI() {
		setVisible(true);
		setTitle("Datos del Cliente");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new java.awt.Dimension(800, 600));
		this.setBounds(0, 0, 800, 600); 
		this.setResizable(false);

		setJMenuBar(menuBar);

		menuBar.add(mnMenu);
		
		mnMenu.add(mntmConsultas);
		mntmAtm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmATMActionPerformed(e);
			}
		});
		
		mnMenu.add(mntmAtm);
		mntmPrstamos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmPrestamosActionPerformed(e);
			}
		});
		
		mnMenu.add(mntmPrstamos);
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmSalirActionPerformed(e);
			}
		});
		
		mnMenu.add(mntmSalir);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		lblTipoDoc.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTipoDoc.setHorizontalAlignment(SwingConstants.CENTER);
		lblTipoDoc.setBounds(330, 93, 128, 16);
		lblTipoDoc.setVerticalAlignment(SwingConstants.BOTTOM);

		contentPane.add(lblTipoDoc);
		lblNumeroDocumento.setFont(new Font("Tahoma", Font.PLAIN, 15));

		lblNumeroDocumento.setBounds(330, 170, 142, 16);

		contentPane.add(lblNumeroDocumento);

		contentPane.add(campoNroDoc);
		btnSeleccionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnSeleccionarActionPerformed(evt);
			}
		});
		btnSeleccionar.setBounds(281, 271, 233, 56);

		contentPane.add(btnSeleccionar);

		iniciarComboTipoDoc();
		comboTipoDoc.setBounds(330, 122, 128, 22);
		contentPane.add(comboTipoDoc);
	}

	private void btnSeleccionarActionPerformed(ActionEvent evt) {
		String nroDoc = campoNroDoc.getText();
		if(!validarDocumento(nroDoc).equals("-1")) {
			String tipoDoc = (String)comboTipoDoc.getSelectedItem();
			String sql = "select nro_cliente from cliente where tipo_doc = '"+tipoDoc+ "' and nro_doc = '"+nroDoc+"' ;";
			try {
				this.conectarBD();
				java.sql.Statement stmt = conexionBD.createStatement();
				java.sql.ResultSet rs = stmt.executeQuery(sql);
				if(rs.next()) {
					boolean tienePrestamo = tienePrestamo(tipoDoc, nroDoc);
					if(tienePrestamo)
						JOptionPane.showMessageDialog(null, "El cliente tiene un préstamo vigente", "Atención", JOptionPane.INFORMATION_MESSAGE);
					else {
						this.setVisible(false);
						cNuevoPrestamo = new crearNuevoPrestamo();
						cNuevoPrestamo.setEmpleadoLogueado(this.empleadoLogueado);
						cNuevoPrestamo.setTipoDoc(tipoDoc);
						cNuevoPrestamo.setCliDoc(Integer.parseInt(nroDoc));
						cNuevoPrestamo.setVisible(true);
					}
				}
				else
					JOptionPane.showMessageDialog(null, "Los datos del cliente son inválidos", "Dátos Inválidos", JOptionPane.INFORMATION_MESSAGE); 

				stmt.close();
				rs.close();
			}
			catch(java.sql.SQLException er) {er.printStackTrace();}
		}
		else
			JOptionPane.showMessageDialog(null, "El documento sólo está compuesto por dígitos", "Legajo Inválido", JOptionPane.ERROR_MESSAGE); 
	}

	private void iniciarComboTipoDoc() {
		String sql = "select tipo_doc, nro_doc from cliente";
		try {
			this.conectarBD();


			java.sql.Statement stmt = conexionBD.createStatement();
			java.sql.ResultSet rs =stmt.executeQuery(sql);

			int colTipoDoc = rs.findColumn("tipo_doc");

			String cbox [] = new String [cantFilas(rs)];
			rs.beforeFirst();
			int i = 0;
			while(rs.next()) {
				cbox[i]=rs.getString(colTipoDoc);
				i++;
			}

			DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<String>(cbox);
			comboTipoDoc = new JComboBox<String>(comboModel);

			stmt.close();
			rs.close();
		}
		catch(java.sql.SQLException er) {er.printStackTrace();}	
	}

	private int cantFilas(java.sql.ResultSet res){
		int cont = 0;
		try {
			while(res.next()) {
				cont++;
			}
		}
		catch(java.sql.SQLException er) {er.printStackTrace();}
		return cont;
	}
	
	private void mntmATMActionPerformed(ActionEvent evt){
		ATM atm = new ATM();
		atm.setVisible(true);
		this.setVisible(false);
	}
	
	private void mntmPrestamosActionPerformed(ActionEvent evt) {
		desconectarBD();
		this.vPrestamo = new logInEmpleadoPrestamo();
		this.vPrestamo.setVisible(false);
		this.setVisible(false);
		this.vPrestamo.setVisible(true);  
	}

	private boolean tienePrestamo(String tDoc, String nDoc) {
		boolean salida=false;
		String sql = "select nro_cliente from cliente natural join prestamo where tipo_doc = '"+tDoc+"' and nro_doc = '"+nDoc+"' ";
		try {
			java.sql.Statement stmt = conexionBD.createStatement();
			java.sql.ResultSet rs =  stmt.executeQuery(sql);
			if(rs.next())
				salida = true;
			stmt.close();
			rs.close();
		}
		catch(java.sql.SQLException er) {er.printStackTrace();}
		return salida;
	}

	private void conectarBD(){
		if (this.conexionBD == null)
		{ 
			try
			{  // Se carga y registra el driver JDBC de MySQL  
				// no es necesario para versiones de jdbc posteriores a 4.0 
				Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			}
			catch (Exception ex)
			{  
				System.out.println(ex.getMessage());
			}

			try
			{  //se genera el string que define los datos de la conección 
				String servidor = "localhost:3306";
				String baseDatos = "banco";
				String usuario = "empleado";
				String clave = "empleado";
				String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos +"?serverTimezone=UTC";
				//se intenta establecer la conección
				this.conexionBD = DriverManager.getConnection(uriConexion, usuario, clave);


			}
			catch (SQLException ex){
				JOptionPane.showMessageDialog(this,
						"Se produjo un error al intentar conectarse a la base de datos.\n" + 
								ex.getMessage(),
								"Error",
								JOptionPane.ERROR_MESSAGE);
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}

		}
	}
	
	private void mntmSalirActionPerformed(ActionEvent evt) {
		System.exit(0);
	}
	

	
	private void desconectarBD(){
		if (this.conexionBD != null)
		{
			try
			{
				this.conexionBD.close();
				this.conexionBD = null;
			}
			catch (SQLException ex)
			{
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}
		}
	}

	private String validarDocumento(String l) {
		String salida = l;
		int i=0;
		boolean valido = true;
		while (i<l.length() && valido) {
			if(l.charAt(i)!='0' && l.charAt(i)!='1' && l.charAt(i)!='2' && l.charAt(i)!='3' && l.charAt(i)!='4' && l.charAt(i)!='5' && l.charAt(i)!='6' && l.charAt(i)!='7' && l.charAt(i)!='8' && l.charAt(i)!='9' ) {
				valido = false;
				salida = "-1";
			}
			else
				i++;
		}
		return salida;
	}
	
	public void setEmpleadoLogueado(int emp){
		this.empleadoLogueado = emp;
	}
	
	public int getEmpleadoLogueado() {
		return this.empleadoLogueado;
	}

}
