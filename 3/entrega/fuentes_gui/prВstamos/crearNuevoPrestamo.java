package banco;


import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import quick.dbtable.DBTable;
import java.awt.Font;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class crearNuevoPrestamo extends JFrame {
	
	private logInEmpleadoPrestamo vPrestamo;
	private int empleadoLogueado;
	private String cliTipoDoc;
	private int cliDoc;
	private int nroCliente;
	private Double tInteres;
	protected Connection conexionBD = null;
	private DBTable tablaTasaPrestamo;
	private JPanel contentPane;
	private final JTextField txtIngreseElMonto = new JTextField();
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu mnMenu = new JMenu("Menu");
	private final JLabel lblMontoAPrestar = new JLabel("Monto a Prestar");
	private JTextField campoMontoAPrestar = new JTextField("<Enter al Ingresar>",JTextField.CENTER);
	private final JLabel lblPeriodo = new JLabel("Periodo (Meses)");
	private JComboBox <String>comboPeriodo = new JComboBox<String>();
	private final JLabel lblTasaprestamo = new JLabel("Tasa Prestamo");
	private final JButton btnAceptar = new JButton("Aceptar");
	private final JLabel lblValorCuota = new JLabel("Valor Cuota");
	private final JLabel lblInters = new JLabel("Inter\u00E9s ($)");
	private final JTextField campoCuota = new JTextField();
	private final JTextField campoInteres = new JTextField();
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
					crearNuevoPrestamo frame = new crearNuevoPrestamo();
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

	
	public crearNuevoPrestamo() {
		campoInteres.setFont(new Font("Tahoma", Font.BOLD, 13));
		campoInteres.setEditable(false);
		campoInteres.setBounds(140, 236, 145, 22);
		campoInteres.setColumns(10);
		campoCuota.setFont(new Font("Tahoma", Font.BOLD, 13));
		campoCuota.setEditable(false);
		campoCuota.setBounds(140, 182, 145, 22);
		campoCuota.setColumns(10);
		campoMontoAPrestar.setFont(new Font("Tahoma", Font.BOLD, 13));
		campoMontoAPrestar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent evt) {
				campoMontoAPrestarActionPerformed(evt);
			}
		});

		campoMontoAPrestar.setBounds(140, 81, 145, 22);
		campoMontoAPrestar.setColumns(10);
		txtIngreseElMonto.setText("Ingrese el Monto");
		txtIngreseElMonto.setColumns(10);
		initGUI();
	}
	private void initGUI() {
		setResizable(false);
		setTitle("Nuevo Pr\u00E9stamo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
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
			public void actionPerformed(ActionEvent evt) {
				mntmPrestamosActionPerformed(evt);
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
		iniciarTabla();
		lblMontoAPrestar.setBounds(28, 82, 97, 16);
		
		campoMontoAPrestar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent evt) {
				campoMontoAPrestarActionPerformed(evt);
					
			}
		});
		
		contentPane.add(lblMontoAPrestar);
		contentPane.add(campoMontoAPrestar);
		lblPeriodo.setBounds(28, 133, 97, 16);
		
		contentPane.add(lblPeriodo);
	

		comboPeriodo.setBounds(140, 130, 145, 22);
		comboPeriodo.setEnabled(false);
		contentPane.add(comboPeriodo);
		lblTasaprestamo.setBounds(494, 53, 97, 16);
		
		contentPane.add(lblTasaprestamo);
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnAceptarActionPerformed(evt);
			}
		});
		btnAceptar.setBounds(90, 314, 181, 49);
		
		contentPane.add(btnAceptar);
		lblValorCuota.setBounds(28, 185, 97, 16);
		
		
		contentPane.add(lblValorCuota);
		lblInters.setBounds(28, 239, 61, 16);
		
		contentPane.add(lblInters);
		
		contentPane.add(campoCuota);
		
		contentPane.add(campoInteres);
		
	}
	

	
	
	private void iniciarTabla() {

		try {
			tablaTasaPrestamo = new DBTable();
			tablaTasaPrestamo.setControlPanelVisible(false);
			tablaTasaPrestamo.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			tablaTasaPrestamo.setBackground(SystemColor.activeCaption);
			tablaTasaPrestamo.setEditable(false);
			tablaTasaPrestamo.setBounds(394, 82, 299, 394);
			this.conectarBD();
			String sql = "select * from tasa_prestamo;";
			java.sql.Statement stmt = conexionBD.createStatement();
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			tablaTasaPrestamo.setSelectSql(sql);
			tablaTasaPrestamo.createColumnModelFromQuery(); 
			tablaTasaPrestamo.refresh();
			contentPane.setLayout(null);
			tablaTasaPrestamo.setLayout(new GridLayout());
			tablaTasaPrestamo.setEditable(false);
			contentPane.add(tablaTasaPrestamo);
			stmt.close();
			rs.close();
		}
		catch(java.sql.SQLException er) {er.printStackTrace();}

	}
	
	private void mntmATMActionPerformed(ActionEvent evt){
		ATM atm = new ATM();
		atm.setVisible(true);
		this.setVisible(false);
	}
	
	private void iniciarComboPeriodo(){
		
		if(!campoMontoAPrestar.getText().equals("")) {
			if(comboPeriodo!=null)
				contentPane.remove(comboPeriodo);
			comboPeriodo=new JComboBox<String>();

			String mto = campoMontoAPrestar.getText();
			String sql = "select distinct periodo from tasa_prestamo where '"+mto+"' >= monto_inf and '"+mto+"' <= monto_sup;";
			try {
				this.conectarBD();
				java.sql.Statement stmt = conexionBD.createStatement();
				java.sql.ResultSet rs =stmt.executeQuery(sql);
				int colPeriodo = rs.findColumn("periodo");
				String cbox [] = new String [cantFilas(rs)];
				rs.beforeFirst();
				int i = 0;
				while(rs.next()) {
					cbox[i]=rs.getString(colPeriodo);
					i++;
				}
				
				if(cbox.length == 0) {
					JOptionPane.showMessageDialog(null, "El monto ingresado está fuera del rango permitido", "Monto Inválido", JOptionPane.INFORMATION_MESSAGE);
					comboPeriodo.setEnabled(false);
					campoMontoAPrestar.setText("");

				}
				else{
					
					DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<String>(cbox);
					comboPeriodo = new JComboBox<String>(comboModel);
					comboPeriodo.setBounds(140, 130, 145, 22);
					comboPeriodo.setSelectedIndex(-1);
					comboPeriodo.addItemListener(new ItemListener() {
						public void itemStateChanged(ItemEvent evt) {
							comboPeriodoActionPerformed(evt);
						}
					});
					comboPeriodo.setFocusable(false);
					contentPane.add(comboPeriodo);
					contentPane.revalidate();
					contentPane.repaint();
				}


				stmt.close();
				rs.close();
			}
			catch(java.sql.SQLException er) {er.printStackTrace();}	
		}
		
	}
	private void comboPeriodoActionPerformed(ItemEvent evt) {
		if(evt.getStateChange()==ItemEvent.SELECTED) {
			java.sql.Statement stmt;
			java.sql.ResultSet rs;
			String montoStr = campoMontoAPrestar.getText();
			double montoDbl = Double.parseDouble(montoStr);
			double tasaInteres = 0;
			String item =(String)comboPeriodo.getSelectedItem();
			try {
				String sql = "select tasa from tasa_prestamo where monto_inf<='"+montoStr+"' and monto_sup>='"+montoStr+"' and periodo="+item+";";
				stmt = conexionBD.createStatement();
				rs = stmt.executeQuery(sql);
				if(rs.next()) {
					tasaInteres=rs.getDouble(1);
					tInteres = tasaInteres;
				}
				Double interes = ((montoDbl*tasaInteres*Integer.valueOf(item))/1200);
				campoInteres.setText(String.valueOf(interes));
				Double cuotaDouble = ((montoDbl+interes)/Integer.valueOf(item));
				campoCuota.setText(String.valueOf(cuotaDouble));
				stmt.close();
				rs.close();
			}catch(SQLException er) {er.printStackTrace();}
		}
		campoCuota.setText(redondearCampos(campoCuota.getText()));
		campoInteres.setText(redondearCampos(campoInteres.getText()));
	}

	
	private String redondearCampos(String str) {
		char [] aux = str.toCharArray();
		String rounded = "";
		int i=0;
		while(aux[i]!='.') {
			rounded+=aux[i];
			i++;
		}
		rounded+=aux[i];
		i++;
		if(aux.length-i >= 2) {
			rounded+=aux[i];
			rounded+=aux[i+1];
		}
		else
			rounded+=aux[i];
		return rounded;
	}
	
	private void btnAceptarActionPerformed(ActionEvent evt) {
		if(!campoMontoAPrestar.getText().equals("") && comboPeriodo.getSelectedIndex()>-1) {
			this.setNroCliente(cliTipoDoc, cliDoc);;
			String cantMeses = String.valueOf(comboPeriodo.getSelectedItem());
			String monto  = campoMontoAPrestar.getText();
			String tasaInteres = String.valueOf(this.tInteres);
			String interes = campoInteres.getText();
			String valorCuota = campoCuota.getText().trim();
			String leg = String.valueOf(this.empleadoLogueado);
			String cli = String.valueOf(this.nroCliente);
					
			this.conectarBD();
			try {
				java.sql.Statement stmt = conexionBD.createStatement();
				
				String sql = "insert into prestamo(fecha, cant_meses, monto, tasa_interes,interes, valor_cuota,legajo,nro_cliente)values(CURDATE(),"+cantMeses+","+monto+","+tasaInteres+","+interes+","+valorCuota+","+leg+","+cli+");";	
				stmt.execute(sql);
				java.sql.ResultSet rs = stmt.executeQuery("select max(nro_prestamo) from prestamo;");
				/*if(rs.next())
					insertarCuotas(rs.getInt(1),Integer.valueOf(cantMeses));*/
				stmt.close();
				rs.close();
				JOptionPane.showMessageDialog(null, "Transacción Exitosa!", "Préstamo Creado", JOptionPane.INFORMATION_MESSAGE);
				opcionesPrestamo oPrestamo = new opcionesPrestamo();
				oPrestamo.setEmpleadoLogueado(this.empleadoLogueado);
				this.setVisible(false);
			}catch(SQLException er) {er.printStackTrace();}
		}	
		else
			JOptionPane.showMessageDialog(null, "Debe ingresar monto y periodo", "Datos Insuficientes", JOptionPane.INFORMATION_MESSAGE);
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
	
	/*private void insertarCuotas(int nroPrestamo, int cantCuotas) {
		conectarBD();
		Date fechaActual = new Date();
		java.sql.Date fechaActualSQL = Fechas.convertirDateADateSQL(fechaActual);
		try {
			for(int i=0; i<cantCuotas;i++) {
				java.sql.Statement stmt = this.conexionBD.createStatement();
				String sql = "INSERT INTO Pago(nro_prestamo, nro_pago, fecha_venc, fecha_pago)VALUES ("+nroPrestamo+","+(i+1)+", date_add('"+fechaActualSQL+"', interval "+(i+1)+" month) , NULL);";
				System.out.println(sql);
				stmt.execute(sql);
				stmt.close();
			}
		}catch(SQLException er) {er.printStackTrace();}
		
	}*/
	
	   private void conectarBD()
	   {
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
	            String usuario = "admin";
	            String clave = "admin";
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
	         }catch (ClassNotFoundException e){
	             e.printStackTrace();}
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
	   
	   private void campoMontoAPrestarActionPerformed(KeyEvent evt) {
		   int tecla = evt.getKeyCode();
		   if(tecla == KeyEvent.VK_ENTER) {
			   if(comboPeriodo.isEnabled()) {
				   campoInteres.setText("");
				   campoCuota.setText("");
				   
			   }
			   iniciarComboPeriodo();
		   }
	   }
	   
	   private void campoMontoAPrestarActionPerformed(MouseEvent evt) {
		   if(campoMontoAPrestar.getText().equals("<Enter al Ingresar>"))
			   campoMontoAPrestar.setText("");
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
		
		private void setNroCliente(String tDoc, int cliDoc) {
			this.conectarBD();
			try{
				String sql = "select nro_cliente from cliente where tipo_doc = '"+tDoc+"' and nro_doc = '"+cliDoc+"' ";
				java.sql.Statement stmt = conexionBD.createStatement();
				java.sql.ResultSet rs = stmt.executeQuery(sql);	
				if(rs.next()) {
					this.nroCliente=rs.getInt(1);
				}
					
			}catch(SQLException er) {er.printStackTrace();}
		}
		
		public void setEmpleadoLogueado(int emp){
			this.empleadoLogueado = emp;
		}
		
		public int getEmpleadoLogueado() {
			return this.empleadoLogueado;
		}
		
		public void setTipoDoc(String tDoc) {
			this.cliTipoDoc = tDoc;
		}
		
		public void setCliDoc(int cliDoc) {
			this.cliDoc=cliDoc;
		}
		
		public String getTipoDoc() {
			return this.cliTipoDoc;
		}
		
		public int getCliDoc() {
			return this.cliDoc;
		}
}
