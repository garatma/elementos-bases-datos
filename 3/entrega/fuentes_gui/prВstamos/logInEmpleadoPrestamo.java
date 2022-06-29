package banco;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import quick.dbtable.DBTable;

@SuppressWarnings("serial")
public class logInEmpleadoPrestamo extends JFrame {
	protected Connection conexionBD = null;
	private opcionesPrestamo oPrestamo;
	private JPanel contentPane;


	DBTable tablaTasaPrestamo;

	private final JLabel lblLegajo = new JLabel("Legajo:");
	private final JLabel lblPassword = new JLabel("Password:");
	private final JLabel tipoDoc = new JLabel("tipo_doc");
	private final JLabel nroDoc = new JLabel("nro_doc");
	private final JLabel lblMonto = new JLabel("Monto");

	private final JPasswordField campoPassword = new JPasswordField(25);
	private final JTextField campoLegajo = new JTextField(25);
	private final JTextField campoDoc = new JTextField(10);
	private final JTextField campoMonto = new JTextField("Ingrese Monto",10);
	private final JButton btnLogIn = new JButton("Ingresar");
	private final JButton btnCreaPrestamos = new JButton("Creación de Préstamos");

	private final JButton btnSelCli = new JButton("Seleccionar");
	private JComboBox<String> combo;

	protected int seleccionado = -1;

	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu mnMenu = new JMenu("Menu");
	private final JMenuItem mntmConsultas = new JMenuItem("Consultas");
	private final JMenuItem mntmAtm = new JMenuItem("ATM");
	private final JMenuItem mntmPrstamos = new JMenuItem("Pr\u00E9stamos");
	private final JMenuItem mntmSalir = new JMenuItem("Salir");


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					logInEmpleadoPrestamo frame = new logInEmpleadoPrestamo();
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
	public logInEmpleadoPrestamo() {
		campoLegajo.setFont(new Font("Tahoma", Font.BOLD, 15));
		campoLegajo.setBounds(317, 159, 174, 24);
		campoLegajo.setColumns(10);
		initGUI();
	}
	private void initGUI() {
		setResizable(false);
		setTitle("Pr\u00E9stamos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new java.awt.Dimension(640, 480));
		this.setBounds(0, 0, 800, 600);    
		
		setJMenuBar(menuBar);
		
		menuBar.add(mnMenu);
		mntmConsultas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmConsultasActionPerformed(e);
			}
		});
		
		mnMenu.add(mntmConsultas);
		mntmAtm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mntmATMActionPerformed(evt);
			}
		});
		
		mnMenu.add(mntmAtm);

		
		mnMenu.add(mntmPrstamos);
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmSalirActionPerformed(e);
			}
		});

		
		mnMenu.add(mntmSalir);
		contentPane = new JPanel();
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		tablaTasaPrestamo = new DBTable();
		

		contentPane.repaint();
		


		btnCreaPrestamos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCreaPrestamosActionPerformed(e);
			}
		});
		btnLogIn.setBackground(UIManager.getColor("Button.light"));
		btnLogIn.setForeground(Color.BLACK);


		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnLogInActionPerformed(e);
			}
		});

		btnSelCli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSelCliActionPerformed(e);
			}
		});

		contentPane.add(btnLogIn);

		contentPane.setLayout(null);
		lblLegajo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblLegajo.setBounds(225, 159, 80, 24);
		contentPane.add(lblLegajo);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPassword.setBounds(225, 203, 80, 16);
		contentPane.add(lblPassword);
		campoPassword.setBounds(317, 200, 174, 24);
		contentPane.add(campoPassword);
		contentPane.add(campoLegajo);
		btnLogIn.setBounds(225, 254, 266, 50);
		

	}
	
	private void mntmSalirActionPerformed(ActionEvent evt) {
		System.exit(0);
	}

	private void btnLogInActionPerformed(ActionEvent evt){


		String leg = campoLegajo.getText();
		leg = validarLegajo(leg);
		char [] pwd = campoPassword.getPassword();
		String pwdToString = String.valueOf(pwd);
		if(leg.equals("-1")) {
			JOptionPane.showMessageDialog(null, "El legajo sólo está compuesto por dígitos", "Legajo Inválido", JOptionPane.ERROR_MESSAGE);  
			campoLegajo.setText("");
			campoPassword.setText("");
		}
		else {
			String sql=null;
			if(leg.length()!=0 && pwdToString.length()!=0) {
				sql = "select legajo, password from empleado where legajo = "+leg+" and password = md5('"+pwdToString+"')";
				try{
					this.conectarBD();
					java.sql.Statement stmt = conexionBD.createStatement();
					java.sql.ResultSet rs =stmt.executeQuery(sql);
					if(rs.next()){
						this.setVisible(false);
					    this.oPrestamo = new opcionesPrestamo();
						oPrestamo.setEmpleadoLogueado(Integer.valueOf(leg));
						oPrestamo.setVisible(true);

					}
					else {
						JOptionPane.showMessageDialog(null, "Legajo y/o Password inválidos", "Dátos Inválidos", JOptionPane.ERROR_MESSAGE); 
						campoLegajo.setText("");
						campoPassword.setText("");
					}
					rs.close();
					stmt.close();

				}catch(java.sql.SQLException er){er.printStackTrace();;}
			}
			else
				JOptionPane.showMessageDialog(null, "Complete los datos Requeridos", "Dátos Inválidos", JOptionPane.INFORMATION_MESSAGE); 

		}
	}






	private void btnCreaPrestamosActionPerformed(ActionEvent evt) {
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


			combo = new JComboBox<String>(cbox);

			this.setPreferredSize(new java.awt.Dimension(640, 90));
			this.setBounds(0, 0, 640, 90);
			contentPane.add(tipoDoc);
			contentPane.add(combo);
			contentPane.add(nroDoc);
			contentPane.add(campoDoc);
			contentPane.add(btnSelCli);
			contentPane.revalidate();
			contentPane.repaint();

			stmt.close();
			rs.close();
		}
		catch(java.sql.SQLException er) {er.printStackTrace();}	


	}

	private void btnSelCliActionPerformed(ActionEvent evt) {
		String nroDoc = campoDoc.getText();
		String tipoDoc = (String)combo.getSelectedItem();
		String sql = "select tipo_doc, nro_doc from cliente where tipo_doc = '"+tipoDoc+ "' and nro_doc = '"+nroDoc+"' ;";
		try {
			this.conectarBD();
			java.sql.Statement stmt = conexionBD.createStatement();
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				boolean tienePrestamo = tienePrestamo(tipoDoc, nroDoc);
				if(tienePrestamo)
					JOptionPane.showMessageDialog(null, "El cliente tiene un préstamo vigente", "Atención", JOptionPane.INFORMATION_MESSAGE);
				else {

					contentPane.removeAll();
					contentPane.setLayout(new BorderLayout());
					this.setPreferredSize(new Dimension(640, 480));
					this.setBounds(0,0,640,480);
					sql = "select * from tasa_prestamo;";



					tablaTasaPrestamo.setSelectSql(sql);

					tablaTasaPrestamo.createColumnModelFromQuery(); 

					tablaTasaPrestamo.refresh();
					//lblMonto.setPreferredSize(new Dimension(50,50));
					contentPane.add(lblMonto, BorderLayout.NORTH);
					campoMonto.setPreferredSize(new Dimension(50,50));
					contentPane.add(campoMonto,BorderLayout.NORTH);
					//tablaPrestamo.setSize(new Dimension(80,80));
					contentPane.add(tablaTasaPrestamo, BorderLayout.CENTER);




					contentPane.revalidate();
					contentPane.repaint();


				}
			}
			else
				JOptionPane.showMessageDialog(null, "Los datos del cliente son iválidos", "Dátos Inválidos", JOptionPane.INFORMATION_MESSAGE); 

			stmt.close();
			rs.close();
		}
		catch(java.sql.SQLException er) {er.printStackTrace();}
	}
	
	private void mntmConsultasActionPerformed(ActionEvent evt) {
		Consultas vConsultas = new Consultas();
		this.setVisible(false);
		vConsultas.setVisible(true);
	}



	private String validarLegajo(String l) {
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

	private void mntmATMActionPerformed(ActionEvent evt){
		ATM atm = new ATM();
		atm.setVisible(true);
		this.setVisible(false);
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
				String driver ="com.mysql.cj.jdbc.Driver";
				String servidor = "localhost:3306";
				String baseDatos = "banco";
				String usuario = "empleado";
				String clave = "empleado";
				String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos +"?serverTimezone=UTC";
				//se intenta establecer la conección
				this.conexionBD = DriverManager.getConnection(uriConexion, usuario, clave);
				tablaTasaPrestamo.connectDatabase(driver, uriConexion, usuario, clave);
				
			}
			catch (SQLException ex)
			{
				JOptionPane.showMessageDialog(this,
						"Se produjo un error al intentar conectarse a la base de datos.\n" + 
								ex.getMessage(),
								"Error",
								JOptionPane.ERROR_MESSAGE);
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}
			catch(ClassNotFoundException er) {er.printStackTrace();}
		}
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

}



