package banco;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import quick.dbtable.DBTable;

@SuppressWarnings("serial")
public class clientesMorosos extends JFrame {
	
	private logInEmpleadoPrestamo vPrestamo;
	private int empleadoLogueado;
	protected Connection conexionBD = null;
	private JPanel contentPane;
	private final DBTable tablaDeMorosos = new DBTable();
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu mnMenu = new JMenu("Menú");
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
					clientesMorosos frame = new clientesMorosos();
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
	public clientesMorosos() {
		initGUI();
	}
	private void initGUI() {
		setResizable(false);
		setTitle("Clientes Morosos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1920, 1080);
		
		setJMenuBar(menuBar);
		
		menuBar.add(mnMenu);
		mntmConsultas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmConsultasActionPerformed(e);
			}
		});
		
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
		tablaDeMorosos.setBounds(10, 0, 1920, 1080);
		contentPane.add(tablaDeMorosos);
		iniciarTabla();
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setVisible(true);
	}
	
	private void mntmPrestamosActionPerformed(ActionEvent evt) {
		desconectarBD();
		this.vPrestamo = new logInEmpleadoPrestamo();
		this.vPrestamo.setVisible(false);
		this.setVisible(false);
		this.vPrestamo.setVisible(true);  
	}
	
	private void mntmSalirActionPerformed(ActionEvent evt) {
		System.exit(0);
	}
	
	private void mntmConsultasActionPerformed(ActionEvent evt) {
		Consultas consultas = new Consultas();
		consultas.setVisible(true);
		this.setVisible(false);
		
	}
	
	private void iniciarTabla() {

		try {

			this.conectarBD();
			String sql ="select nro_cliente, tipo_doc, nro_doc, nombre, apellido, nro_prestamo, monto, cant_meses, valor_cuota, count(nro_prestamo) as Cuotas_Atrasadas from cliente c natural join prestamo pr natural join pago pg where fecha_venc<curdate() and pg.fecha_pago is null group by nro_prestamo;";
			java.sql.Statement stmt = conexionBD.createStatement();
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			tablaDeMorosos.setSelectSql(sql);
			tablaDeMorosos.createColumnModelFromQuery(); 
			tablaDeMorosos.setRowCountSql("select count(*) from (select nro_cliente, tipo_doc, nro_doc, nombre, apellido, nro_prestamo, monto, cant_meses, valor_cuota, count(nro_prestamo) as Cuotas_Atrasadas from cliente c natural join prestamo pr natural join pago pg where fecha_venc<curdate() and pg.fecha_pago is null group by nro_prestamo)aux;");
			tablaDeMorosos.refresh();
			stmt.close();
			rs.close();
		}
		catch(java.sql.SQLException er) {er.printStackTrace();}
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
				tablaDeMorosos.connectDatabase(driver, uriConexion, usuario, clave);


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
			}catch (ClassNotFoundException e){ e.printStackTrace();}

		}
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
	
	private void mntmATMActionPerformed(ActionEvent evt){
		ATM atm = new ATM();
		atm.setVisible(true);
		this.setVisible(false);
	}
	
	public void setEmpleadoLogueado(int emp) {
		this.empleadoLogueado=emp;
	}
	
	public int getEmpleadoLogueado() {
		return this.empleadoLogueado;
	}
}
