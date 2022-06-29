package banco;

import java.awt.BorderLayout;
import java.awt.EventQueue;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import quick.dbtable.DBTable;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.border.EtchedBorder;

import javax.swing.JTextArea;

import quick.dbtable.DBTableEventListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;


@SuppressWarnings("serial")
public class ventanaPagoPrestamo extends JFrame {

	private logInEmpleadoPrestamo vPrestamo;
	private int nroCliente;
	private int empleadoLogueado;
	private DBTable tablaPago;
	private boolean arregloCuotas [];
	protected Connection conexionBD = null;
	private JPanel contentPane;
	private final JButton btnProcPago = new JButton("Procesar Pago");
	private JTextArea textArea;
	private final JLabel lblPagosSeleccionados = new JLabel("Pagos Seleccionados");
	private JScrollPane scroll;
	private JMenuBar menuBar;
	private JMenu mnMenu;
	private JMenuItem mntmConsultas;
	private JMenuItem mntmAtm;
	private JMenuItem mntmPrstamos;
	private JMenuItem mntmSalir;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ventanaPagoPrestamo frame = new ventanaPagoPrestamo();
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
	public ventanaPagoPrestamo() {
		initGUI();
	}

	public ventanaPagoPrestamo(int cliente) {
		this.nroCliente=cliente;
		initGUI();
		iniciarTabla();
	}
	private void initGUI() {
		setResizable(false);
		setTitle("Pago de Prestamos");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		
		mntmConsultas = new JMenuItem("Consultas");
		mnMenu.add(mntmConsultas);
		
		mntmAtm = new JMenuItem("ATM");
		mnMenu.add(mntmAtm);
		
		mntmPrstamos = new JMenuItem("Préstamos");
		mntmPrstamos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmPrestamosActionPerformed(e);
			}
		});
		mnMenu.add(mntmPrstamos);
		
		mntmSalir = new JMenuItem("Salir");
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
		tablaPago = new DBTable();
		tablaPago.setSortEnabled(false);
		tablaPago.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				tablaPagoMouseEvent(evt);
			}
		});
		tablaPago.addDBTableEventListener(new DBTableEventListener() {



		});
		tablaPago.setBounds(435, 13, 335, 527);
		tablaPago.getTable().setBounds(1, -1, 300, 408);

		getContentPane().add(tablaPago);           
		tablaPago.setEditable(false); 
		contentPane.add(tablaPago, BorderLayout.CENTER);
		btnProcPago.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnProcPagoActionPerformed(evt);
			}
		});

		btnProcPago.setBounds(139, 350, 163, 61);
		
		contentPane.add(btnProcPago);


		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.BOLD, 14));
		textArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		textArea.setEditable(false);
		textArea.setBounds(73, 77, 304, 189);
		scroll = new JScrollPane(textArea);
		scroll.setBounds(73, 77, 304, 189);
		getContentPane().add(scroll);
		scroll.setViewportView(textArea);
		lblPagosSeleccionados.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPagosSeleccionados.setBounds(139, 36, 163, 28);
		
		contentPane.add(lblPagosSeleccionados);
	}

	private void iniciarTabla() {

		try {

			this.conectarBD();
			String sql ="select nro_pago, valor_cuota, fecha_venc from prestamo natural join pago where fecha_pago is null and nro_cliente ="+nroCliente+";";
			java.sql.Statement stmt = conexionBD.createStatement();
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			//en este momento seteo el arreglo de pagos con tamaño a lo sumo de la cantidad de filas del ResultSet
			arregloCuotas = new boolean[cantFilas(rs)];
			rs.beforeFirst(); // Reestablezco el puntero del result set al lugar correspondiente.
			//----------------------------------------------------------------------------------------------------
			tablaPago.setSelectSql(sql);
			tablaPago.createColumnModelFromQuery(); 
			tablaPago.refresh();
			stmt.close();
			rs.close();
		}
		catch(java.sql.SQLException er) {er.printStackTrace();}
	}
	
	private void btnProcPagoActionPerformed(ActionEvent evt) {
		if(textArea.getText().equals(""))
			JOptionPane.showMessageDialog(null, "Debe seleccionar al menos una cuota de la tabla", "No hay pagos para procesar", JOptionPane.INFORMATION_MESSAGE); 
		else {
			//Obtengo el numero de préstamo asociado al cliente.
			conectarBD();
			String sql = "select nro_prestamo from prestamo where nro_cliente = "+nroCliente+";";
			int nroPrestamo=0;
			try {
				java.sql.Statement stmt = conexionBD.createStatement();
				java.sql.ResultSet rs = stmt.executeQuery(sql);
				if(rs.next())
					nroPrestamo = rs.getInt(1);
				//Actualizo cada uno de los pagos seleccionados asignandoles como fecha de pago, la fecha actual.
				for(int i=0;i<arregloCuotas.length;i++) {
					if(arregloCuotas[i]==true) {
						
						Object tabla[][] =tablaPago.getDataArray();
						int nroPago = (int)tabla[i][0];
						String update = "update pago set fecha_pago = curdate() where nro_prestamo="+nroPrestamo+" and nro_pago="+nroPago+";";
						stmt.executeUpdate(update);
					}
				}
				JOptionPane.showMessageDialog(null, "Transacción Exitosa!", "Pago(s) Procesado(s)", JOptionPane.INFORMATION_MESSAGE);
				stmt.close();
				rs.close();
			}catch(SQLException er) {er.printStackTrace();}
			
			opcionesPrestamo oPrestamo = new opcionesPrestamo();
			oPrestamo.setEmpleadoLogueado(this.empleadoLogueado);
			this.setVisible(false);
		}
			
		
	}
	
	private void mntmSalirActionPerformed(ActionEvent evt) {
		System.exit(0);
	}
	
	private void tablaPagoMouseEvent(MouseEvent evt) {
		int filaSeleccionada = tablaPago.getSelectedRow();
		if(arregloCuotas[filaSeleccionada]==false) { 
			arregloCuotas[filaSeleccionada]=true;
			textArea.setText("");
		}
		else {
			arregloCuotas[filaSeleccionada]=false;
			textArea.setText("");
		}

		refrescarTextArea();
	}
	
	private void refrescarTextArea() {
		String datosCuota = "";
		for(int i=0;i<arregloCuotas.length;i++) {
			if(arregloCuotas[i]==true) {
				for(int j=0;j<tablaPago.getColumnCount();j++) {
					datosCuota+=tablaPago.getValueAt(i, j);
					datosCuota+=" ";
				}
				textArea.append(datosCuota);
				textArea.append("\n");
			}
			datosCuota="";
		}
	}
	
	private void mntmPrestamosActionPerformed(ActionEvent evt) {
		desconectarBD();
		this.vPrestamo = new logInEmpleadoPrestamo();
		this.vPrestamo.setVisible(false);
		this.setVisible(false);
		this.vPrestamo.setVisible(true);  
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
				tablaPago.connectDatabase(driver, uriConexion, usuario, clave);


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
	
	

	public void setCliente(int c) {
		this.nroCliente=c;
	}

	public int getCliente() {
		return this.nroCliente;
	}
	
	public void setEmpleado(int emp) {
		this.empleadoLogueado=emp;
	}

	public int getEmpleado() {
		return this.empleadoLogueado;
	}
}
