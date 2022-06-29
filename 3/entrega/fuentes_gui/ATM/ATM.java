package banco;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

import quick.dbtable.*;
import javax.swing.JSeparator;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class ATM extends JFrame {

	private JPanel contentPane;
	private JButton boton_pass;
	private JPanel panel_pass;
	private static ATM frame;
	java.sql.Connection conexion;
	int control = 0;
	private JLabel lblIngreseNroTarjeta;
	private JLabel lblIngreseLaContrasea;
	private JPasswordField PIN;
	private JTextField field_tarjeta;
	private JLabel lblTarjeta;
	private JLabel monto;
	private JLabel caja_ahorro;
	protected Connection conexionBD = null;
	private JPanel panel_tarjeta;
	private JButton saldo;
	private JButton ult_mov;
	private JButton mov_periodo;
	private JButton transferencia;
	private JButton extraccion;
	private JButton atras;
	private JButton confirmar;
	String tarjeta;
	private JButton otra_operacion;
	boolean conectado=false;
	private DBTable tabla_Tarjeta;
	private JLabel eti_fecha_ini;
	private JTextField fecha_inicio;
	private JTextField fecha_fin;
	private JTextField ingresar_monto;
	private JTextField ingresar_caja;
	private JLabel eti_fecha_fin;
	private JButton ver_transacciones;
	private JSeparator separator;
	private JMenuBar menuBar;
	private JMenu mnMen;
	private JMenuItem mntmConsultas;
	private JMenuItem mntmAtm;
	private JMenuItem mntmPrstamos;
	private JMenuItem mntmSalir;
	private JLabel lblFormatoDeFechas;
	boolean transf=false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new ATM();
					frame.setVisible(true);
					frame.pack();
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	
	public void establecer_conexion(char [] pass_arr) {
		String clave = "";
		int i = 0;
		while ( i < pass_arr.length ) {
			clave += pass_arr[i];
			// por seguridad se limpia el password
			pass_arr[i++] = 0;
		}
			try {
				if (field_tarjeta.getText().length()!=0)  {
					String sql = "select * from Tarjeta where nro_tarjeta like " + field_tarjeta.getText() +  " and PIN like  md5('"+clave+"')"  ;
					consulta(sql);
					ResultSet rs =consulta(sql);
					if(rs.next())  {
						tarjeta=field_tarjeta.getText();
						panel_tarjeta.setVisible(true);
						panel_pass.setVisible(false);
						ult_mov.setVisible(true);
						mov_periodo.setVisible(true);
						otra_operacion.setVisible(true);
						transferencia.setVisible(true);
						extraccion.setVisible(true);
						saldo.setVisible(true);
						otra_operacion.setVisible(true);
						this.pack();
					}
					else {
						JOptionPane.showMessageDialog(this,"Número de tarjeta o PIN incorrecto. Vuelva a intentar.","Error", JOptionPane.ERROR_MESSAGE);
						field_tarjeta.setText("");
						PIN.setText("");
					}
				}
			}
			catch (java.sql.SQLException ex) {
				JOptionPane.showMessageDialog(this, "Error al establecer la conexión con la base de datos.\nDetalle: "+ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
	}
	
	public ResultSet consulta(String sql) {
		ResultSet r=null;
		try {
		if (!conectado) {
			try {
				tabla_Tarjeta.connectDatabase("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/banco?serverTimezone=UTC", "atm","atm");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			conexionBD = tabla_Tarjeta.getConnection();
			conectado=true;
		}
			Statement stmt =this.conexionBD.createStatement(); 
			r = stmt.executeQuery(sql);
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Error al establecer la conexión con la base de datos.\nDetalle: "+ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		return  r;
	}
	
	
	public ATM() {
		initGUI();
	}
	private void initGUI() {
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("ATM");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(1000, 1000, 982, 4260);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnMen = new JMenu("Men\u00FA");
		menuBar.add(mnMen);
		
		mntmConsultas = new JMenuItem("Consultas");
		mntmConsultas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmConsultasActionPerformed(e);
			}
		});
		mnMen.add(mntmConsultas);
		
		mntmAtm = new JMenuItem("ATM");
		mnMen.add(mntmAtm);
		
		mntmPrstamos = new JMenuItem("Pr\u00E9stamos");
		mntmPrstamos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mntmPrestamosActionPerformed(evt);
			}
		});
		mnMen.add(mntmPrstamos);
		
		mntmSalir = new JMenuItem("Salir");
		mnMen.add(mntmSalir);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		panel_pass = new JPanel();
		contentPane.add(panel_pass);
		GridBagLayout gbl_panel_pass = new GridBagLayout();
		gbl_panel_pass.columnWidths = new int[]{125, 108, 49, 0};
		gbl_panel_pass.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_pass.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_pass.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_pass.setLayout(gbl_panel_pass);
		
		lblTarjeta = new JLabel("Tarjeta");
		GridBagConstraints gbc_lblTarjeta = new GridBagConstraints();
		gbc_lblTarjeta.anchor = GridBagConstraints.WEST;
		gbc_lblTarjeta.insets = new Insets(0, 0, 5, 5);
		gbc_lblTarjeta.gridx = 0;
		gbc_lblTarjeta.gridy = 0;
		panel_pass.add(lblTarjeta, gbc_lblTarjeta);
		
		lblIngreseNroTarjeta = new JLabel("Ingrese nro. de tarjeta");
		GridBagConstraints gbc_lblIngreseNroTarjeta = new GridBagConstraints();
		gbc_lblIngreseNroTarjeta.anchor = GridBagConstraints.WEST;
		gbc_lblIngreseNroTarjeta.insets = new Insets(0, 0, 5, 5);
		gbc_lblIngreseNroTarjeta.gridx = 0;
		gbc_lblIngreseNroTarjeta.gridy = 1;
		panel_pass.add(lblIngreseNroTarjeta, gbc_lblIngreseNroTarjeta);
		
		field_tarjeta = new JTextField();
		field_tarjeta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (field_tarjeta.getText().length()!=0) 
					establecer_conexion(PIN.getPassword());
			}
		});
		GridBagConstraints gbc_tarjeta = new GridBagConstraints();
		gbc_tarjeta.insets = new Insets(0, 0, 5, 5);
		gbc_tarjeta.fill = GridBagConstraints.HORIZONTAL;
		gbc_tarjeta.gridx = 1;
		gbc_tarjeta.gridy = 1;
		panel_pass.add(field_tarjeta, gbc_tarjeta);
		field_tarjeta.setColumns(10);
		
		lblIngreseLaContrasea = new JLabel("Ingrese el nro. de PIN");
		GridBagConstraints gbc_lblIngreseLaContrasea = new GridBagConstraints();
		gbc_lblIngreseLaContrasea.anchor = GridBagConstraints.WEST;
		gbc_lblIngreseLaContrasea.insets = new Insets(0, 0, 0, 5);
		gbc_lblIngreseLaContrasea.gridx = 0;
		gbc_lblIngreseLaContrasea.gridy = 2;
		panel_pass.add(lblIngreseLaContrasea, gbc_lblIngreseLaContrasea);
		
		PIN = new JPasswordField(); 
		PIN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (field_tarjeta.getText().length()!=0) 
					establecer_conexion(PIN.getPassword());
			}
		});
		GridBagConstraints gbc_PIN = new GridBagConstraints();
		gbc_PIN.insets = new Insets(0, 0, 0, 5);
		gbc_PIN.fill = GridBagConstraints.HORIZONTAL;
		gbc_PIN.gridx = 1;
		gbc_PIN.gridy = 2;
		panel_pass.add(PIN, gbc_PIN);
		
		boton_pass = new JButton("OK");
		GridBagConstraints gbc_boton_pass = new GridBagConstraints();
		gbc_boton_pass.anchor = GridBagConstraints.NORTHWEST;
		gbc_boton_pass.gridx = 2;
		gbc_boton_pass.gridy = 2;
		panel_pass.add(boton_pass, gbc_boton_pass);
		
		boton_pass.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if (field_tarjeta.getText().length()!=0) 
					establecer_conexion(PIN.getPassword());
			}
		});
		
		panel_tarjeta = new JPanel();
		contentPane.add(panel_tarjeta);
		panel_tarjeta.setVisible(false);
		GridBagLayout gbl_panel_tarjeta = new GridBagLayout();
		gbl_panel_tarjeta.columnWidths = new int[]{90};
		gbl_panel_tarjeta.rowHeights = new int[]{0, 23, 0, 0, 0, 23, 0, 0, 23, 23, 0, 0, 0};
		gbl_panel_tarjeta.columnWeights = new double[]{1.0};
		gbl_panel_tarjeta.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		panel_tarjeta.setLayout(gbl_panel_tarjeta);
		
		eti_fecha_ini = new JLabel("Fecha inicio de período:");
		eti_fecha_ini.setVisible(false);
		
		
		saldo = new JButton("Consulta de Saldo");
		saldo.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				try {
				String sql = "select distinct saldo from Tarjeta t join trans_cajas_ahorro v on t.nro_ca=v.nro_ca where nro_tarjeta like "+tarjeta;
				ResultSet rs = consulta(sql); 
				if(rs.next()){	
					JOptionPane.showMessageDialog(null,"Su saldo es: "+rs.getString("saldo"), "Saldo", JOptionPane.INFORMATION_MESSAGE);
					pack();
					}
				else JOptionPane.showMessageDialog(null,"Su saldo es: 0", "Saldo", JOptionPane.INFORMATION_MESSAGE);

				}
				catch (java.sql.SQLException ex) {
					JOptionPane.showMessageDialog(null, "Error en la conexión", "Error", JOptionPane.ERROR_MESSAGE);
					}
			
		}  }  );
		GridBagConstraints gbc_saldo = new GridBagConstraints();
		gbc_saldo.fill = GridBagConstraints.BOTH;
		gbc_saldo.insets = new Insets(0, 0, 5, 0);
		gbc_saldo.gridx = 0;
		gbc_saldo.gridy = 0;
		
		panel_tarjeta.add(saldo, gbc_saldo);
		
		ult_mov = new JButton("Últimos Movimientos ");
		ult_mov.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		ult_mov.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String sql = " select distinct tipo, fecha, hora, monto, cod_caja, destino from trans_cajas_ahorro where nro_ca in (select nro_ca from Tarjeta where nro_tarjeta = "+tarjeta+")  order by fecha desc, hora desc limit 15";
				try {
					tabla_Tarjeta.setSelectSql(sql.trim());
					tabla_Tarjeta.createColumnModelFromQuery();
					tabla_Tarjeta.refresh();
					boolean es_deposito = false; 
					int max = tabla_Tarjeta.getRowCount() ;
			    	Column col_monto = tabla_Tarjeta.getColumnByHeaderName("monto") ;
					for (int i=0;  i<15 && i<max;i++) {
						for (int j = 0; j < tabla_Tarjeta.getColumnCount();j++) {
								if	 (tabla_Tarjeta.getColumn(j).getType()==Types.TIME)   {
									 Date nuevaFecha = new Date();
					                 Calendar cal = Calendar.getInstance(); 
					                 cal.setTime((Date)tabla_Tarjeta.getValueAt(i,j)); 
					                 cal.add(Calendar.HOUR, 3);
					                 
					                 nuevaFecha = cal.getTime();
					                 tabla_Tarjeta.setValueAt(nuevaFecha, i, j);
					    			 tabla_Tarjeta.getColumn(j).setDateFormat("HH:mm:ss");
									  
					  	       	 }
					    		 if	 (tabla_Tarjeta.getColumn(j).getType()==Types.DATE) {
					    			 Date nuevaFecha = new Date();
					                 Calendar cal = Calendar.getInstance(); 
					                 Object s= tabla_Tarjeta.getValueAt(i,j);
					                 if (s!=null)
					                 cal.setTime((Date)tabla_Tarjeta.getValueAt(i,j)); 
					                 cal.add(Calendar.DATE, 1);
					                 nuevaFecha = cal.getTime();
					                 tabla_Tarjeta.setValueAt(nuevaFecha, i, j);
					    			 tabla_Tarjeta.getColumn(j).setDateFormat("yyyy/MM/dd");
					    		}
					    		 
					    		 Object s= tabla_Tarjeta.getValueAt(i,j);
								if ( j == 0 && s!=null) es_deposito = tabla_Tarjeta.getValueAt(i,j).equals("depósito");
				    			if ( !es_deposito && tabla_Tarjeta.getColumn(j).equals(col_monto) ) {
				    				BigDecimal ob = (BigDecimal) tabla_Tarjeta.getValueAt(i,j);
				    				BigDecimal negativo = new BigDecimal(0);
				    				negativo = negativo.subtract(ob);
				    				tabla_Tarjeta.setValueAt(negativo, i,j);
				    			}
				    	}	
		    			es_deposito = false;
					}
						tabla_Tarjeta.setVisible(true);
						ult_mov.setVisible(false);
						saldo.setVisible(false);
						transferencia.setVisible(false);
						extraccion.setVisible(false);
						otra_operacion.setVisible(false);
						mov_periodo.setVisible(false);
						atras.setVisible(true);
						pack();
				}
				catch (SQLException e1) {
					JOptionPane.showMessageDialog(null,"Error al realizar la consulta","Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		GridBagConstraints gbc_ult_mov = new GridBagConstraints();
		gbc_ult_mov.fill = GridBagConstraints.BOTH;
		gbc_ult_mov.insets = new Insets(0, 0, 5, 0);
		gbc_ult_mov.gridx = 0;
		gbc_ult_mov.gridy = 1;
		panel_tarjeta.add(ult_mov, gbc_ult_mov);
		
		mov_periodo = new JButton("Movimientos por período");
		mov_periodo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		mov_periodo.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				saldo.setVisible(false);
				ult_mov.setVisible(false);
				mov_periodo.setVisible(false);
				tabla_Tarjeta.setVisible(false);
				otra_operacion.setVisible(false);
				transferencia.setVisible(false);
				extraccion.setVisible(false);
				eti_fecha_ini.setVisible(true);
				eti_fecha_fin.setVisible(true);
				fecha_inicio.setVisible(true);
				fecha_fin.setVisible(true);
				ver_transacciones.setVisible(true);
				lblFormatoDeFechas.setVisible(true);
				atras.setVisible(true);
				pack();
			}
				
		});
		GridBagConstraints gbc_mov_periodo = new GridBagConstraints();
		gbc_mov_periodo.fill = GridBagConstraints.BOTH;
		gbc_mov_periodo.insets = new Insets(0, 0, 5, 0);
		gbc_mov_periodo.gridx = 0;
		gbc_mov_periodo.gridy = 2;
		panel_tarjeta.add(mov_periodo, gbc_mov_periodo);
		
		
		tabla_Tarjeta = new DBTable();
		tabla_Tarjeta.setSortEnabled(false);
		tabla_Tarjeta.removeColumn(tabla_Tarjeta.getColumn(0));
		tabla_Tarjeta.removeColumn(tabla_Tarjeta.getColumn(0));
		tabla_Tarjeta.removeColumn(tabla_Tarjeta.getColumn(0));
		tabla_Tarjeta.removeColumn(tabla_Tarjeta.getColumn(0));
		GridBagConstraints gbc_tabla_Tarjeta = new GridBagConstraints();
		gbc_tabla_Tarjeta.anchor = GridBagConstraints.SOUTH;
		gbc_tabla_Tarjeta.fill = GridBagConstraints.HORIZONTAL;
		gbc_tabla_Tarjeta.insets = new Insets(0, 0, 5, 0);
		gbc_tabla_Tarjeta.gridx = 0;
		gbc_tabla_Tarjeta.gridy = 3;
		panel_tarjeta.add(tabla_Tarjeta,gbc_tabla_Tarjeta);           
		tabla_Tarjeta.setEditable(false); 
		tabla_Tarjeta.setVisible(false);
		
		lblFormatoDeFechas = new JLabel("Formato de fechas: yyyy/mm/dd");
		GridBagConstraints gbc_lblFormatoDeFechas = new GridBagConstraints();
		gbc_lblFormatoDeFechas.insets = new Insets(0, 0, 5, 0);
		gbc_lblFormatoDeFechas.gridx = 0;
		gbc_lblFormatoDeFechas.gridy = 4;
		panel_tarjeta.add(lblFormatoDeFechas, gbc_lblFormatoDeFechas);
		lblFormatoDeFechas.setVisible(false);
		
		GridBagConstraints gbc_eti_fecha_ini = new GridBagConstraints();
		gbc_eti_fecha_ini.insets = new Insets(0, 0, 5, 0);
		gbc_eti_fecha_ini.gridx = 0;
		gbc_eti_fecha_ini.gridy = 5;
		panel_tarjeta.add(eti_fecha_ini, gbc_eti_fecha_ini);
		eti_fecha_ini.setVisible(false);
		
		otra_operacion = new JButton("Ingresar otra tarjeta");
		otra_operacion.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				saldo.setVisible(false);
				ult_mov.setVisible(false);
				mov_periodo.setVisible(false);
				transferencia.setVisible(false);
				extraccion.setVisible(false);
				caja_ahorro.setVisible(false);
				monto.setVisible(false);
				ingresar_caja.setVisible(false);
				ingresar_monto.setVisible(false);
				field_tarjeta.setVisible(true);
				panel_pass.setVisible(true);
				field_tarjeta.setText("");
				PIN.setText("");
				otra_operacion.setVisible(false);
				pack();
			}
		});
		
		
		caja_ahorro = new JLabel ("Ingrese el nro. de la caja de ahorro destino:");
		GridBagConstraints gbc_caja_ahorro = new GridBagConstraints();
		gbc_caja_ahorro.fill = GridBagConstraints.HORIZONTAL;
		gbc_caja_ahorro.insets = new Insets(0, 0, 5, 0);
		gbc_caja_ahorro.gridx = 0;
		gbc_caja_ahorro.gridy = 0;
		panel_tarjeta.add(caja_ahorro,gbc_caja_ahorro);
		caja_ahorro.setVisible(false);
		
		monto = new JLabel ("Ingrese el monto a transferir:");
		GridBagConstraints gbc_monto = new GridBagConstraints();
		gbc_monto.fill = GridBagConstraints.HORIZONTAL;
		gbc_monto.insets = new Insets(0, 0, 5, 0);
		gbc_monto.gridx = 0;
		gbc_monto.gridy = 4;
		panel_tarjeta.add(monto,gbc_monto);
		monto.setVisible(false);
		
		ingresar_caja = new JTextField();
		GridBagConstraints gbc_ingresar_caja = new GridBagConstraints();
		gbc_ingresar_caja.fill = GridBagConstraints.HORIZONTAL;
		gbc_ingresar_caja.insets = new Insets(0, 0, 5, 0);
		gbc_ingresar_caja.gridx = 0;
		gbc_ingresar_caja.gridy = 1;
		panel_tarjeta.add(ingresar_caja,gbc_ingresar_caja);
		ingresar_caja.setVisible(false);
		
		ingresar_monto = new JTextField();
		GridBagConstraints gbc_ingresar_monto = new GridBagConstraints();
		gbc_ingresar_monto.fill = GridBagConstraints.HORIZONTAL;
		gbc_ingresar_monto.insets = new Insets(0, 0, 5, 0);
		gbc_ingresar_monto.gridx = 0;
		gbc_ingresar_monto.gridy = 5;
		panel_tarjeta.add(ingresar_monto,gbc_ingresar_monto);
		ingresar_monto.setVisible(false);
		
		confirmar = new JButton ("Confirmar");
		GridBagConstraints gbc_confirmar = new GridBagConstraints();
		gbc_confirmar.fill = GridBagConstraints.HORIZONTAL;
		gbc_confirmar.insets = new Insets(0, 0, 5, 0);
		gbc_confirmar.gridx = 0;
		gbc_confirmar.gridy = 9;
		panel_tarjeta.add(confirmar,gbc_confirmar);
		confirmar.setVisible(false);
		confirmar.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
				String sql;
				
				int nro_cliente ; 
				String nro_cli = "select distinct nro_cliente from Tarjeta where nro_tarjeta = "+tarjeta;
				String nro_origen_sql = "select distinct nro_ca from Tarjeta where nro_tarjeta = "+tarjeta;
				ResultSet rs = consulta(nro_cli);
				ResultSet rs_ca_origen = consulta(nro_origen_sql);
				if(rs.next()) {
				if ( rs_ca_origen.next()){
					nro_cliente = rs.getInt("nro_cliente");
					int nro_ca = rs_ca_origen.getInt("nro_ca");
					if (transf ) {
						sql = "call transf("+ingresar_monto.getText()+","+100+","+nro_cliente+","+nro_ca+","+ingresar_caja.getText()+");";
					}
					else
						sql = "call extr("+ingresar_monto.getText()+","+100+","+nro_cliente+","+nro_ca+");";
					ResultSet sp;
					sp = consulta(sql);
					if(sp.next()) {
						if (!transf) { //es extraccion
							String nuevo_saldo = "select distinct saldo from trans_cajas_ahorro where nro_ca like "+nro_ca+";";
							ResultSet rs_saldo =consulta(nuevo_saldo);
							if(rs_saldo.next()) {
								int saldo_actual = rs_saldo.getInt("saldo");
								JOptionPane.showMessageDialog(null,sp.getString("resultado") +" Su saldo actual es: "+saldo_actual, "Transacción Exitosa", JOptionPane.INFORMATION_MESSAGE);
							}
						}
						else JOptionPane.showMessageDialog(null,sp.getString("resultado"), "Transacción Exitosa", JOptionPane.INFORMATION_MESSAGE);
					}
					else 
						JOptionPane.showMessageDialog(null, sp.getString("resultado"), "Error", JOptionPane.ERROR_MESSAGE);
					}
				else 
					JOptionPane.showMessageDialog(null, rs_ca_origen.getString("resultado"), "Error", JOptionPane.ERROR_MESSAGE);
				}
				else 
					JOptionPane.showMessageDialog(null, rs.getString("resultado"), "Error", JOptionPane.ERROR_MESSAGE);
				
				}
				catch (java.sql.SQLException ex) {
					JOptionPane.showMessageDialog(null, "Error en la conexión", "Error", JOptionPane.ERROR_MESSAGE);
				}
				pack();
				ingresar_monto.setText("");
				ingresar_caja.setText("");
			}
		});
		
		
		transferencia = new JButton("Realizar Transferencia");
		GridBagConstraints gbc_transferencia = new GridBagConstraints();
		gbc_transferencia.fill = GridBagConstraints.HORIZONTAL;
		gbc_transferencia.insets = new Insets(0, 0, 5, 0);
		gbc_transferencia.gridx = 0;
		gbc_transferencia.gridy = 4;
		panel_tarjeta.add(transferencia, gbc_transferencia);
		transferencia.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				transf=true;
				saldo.setVisible(false);
				ult_mov.setVisible(false);
				mov_periodo.setVisible(false);
				transferencia.setVisible(false);
				extraccion.setVisible(false);
				otra_operacion.setVisible(false);
				atras.setVisible(true);
				caja_ahorro.setVisible(true);
				monto.setVisible(true);
				ingresar_caja.setVisible(true);
				ingresar_monto.setVisible(true);
				confirmar.setVisible(true);
				pack();
			}
		});
		
		extraccion = new JButton("Realizar Extracción");
		GridBagConstraints gbc_extraccion = new GridBagConstraints();
		gbc_extraccion.fill = GridBagConstraints.HORIZONTAL;
		gbc_extraccion.insets = new Insets(0, 0, 5, 0);
		gbc_extraccion.gridx = 0;
		gbc_extraccion.gridy = 5;
		panel_tarjeta.add(extraccion,gbc_extraccion);
		extraccion.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				transf=false;
				saldo.setVisible(false);
				ult_mov.setVisible(false);
				mov_periodo.setVisible(false);
				transferencia.setVisible(false);
				extraccion.setVisible(false);
				otra_operacion.setVisible(false);
				atras.setVisible(true);
				monto.setVisible(true);
				ingresar_monto.setVisible(true);
				confirmar.setVisible(true);
				monto.setText("Ingrese el monto a extraer:");
				pack();
			}
		});
		
		
		ver_transacciones = new JButton("Ver transacciones");
		ver_transacciones.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				tabla_Tarjeta.setEditable(false);
				if (fecha_inicio.getText().equals("") || fecha_fin.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"Ingrese fecha de inicio y de fin","Error",JOptionPane.ERROR_MESSAGE);
				}
				else {
				boolean error=false;
				
			String sql="select distinct tipo, fecha, hora, monto, cod_caja, destino from trans_cajas_ahorro where nro_ca in (select nro_ca from Tarjeta where nro_tarjeta = "+tarjeta+") and fecha>= \""+fecha_inicio.getText()+"\" and fecha<= \""+fecha_fin.getText()+"\" order by fecha desc, hora desc ";
			if(!Fechas.validar(fecha_inicio.getText()) || !Fechas.validar(fecha_fin.getText()) ) {
				JOptionPane.showMessageDialog(null,"Ingrese fechas válidas","Error",JOptionPane.ERROR_MESSAGE);
				error=true;
			}
			
			else {
				try {
					tabla_Tarjeta.setSelectSql(sql.trim());
					tabla_Tarjeta.createColumnModelFromQuery();
					tabla_Tarjeta.refresh();
					boolean es_deposito = false; 
			    	Column col_monto = tabla_Tarjeta.getColumnByHeaderName("monto") ;
			    	for (int i=0; i<tabla_Tarjeta.getRowCount(); i++) {
						for (int j = 0; j < tabla_Tarjeta.getColumnCount(); j++) {
								if	 (tabla_Tarjeta.getColumn(j).getType()==Types.TIME)   {
									 Date nuevaFecha = new Date();
					                 Calendar cal = Calendar.getInstance(); 
					                 cal.setTime((Date)tabla_Tarjeta.getValueAt(i,j)); 
					                 cal.add(Calendar.HOUR, 3);
					                 nuevaFecha = cal.getTime();
					                 tabla_Tarjeta.setValueAt(nuevaFecha, i, j);
					    			 tabla_Tarjeta.getColumn(j).setDateFormat("HH:mm:ss");
									  
					  	       	 }
					    		 if	 (tabla_Tarjeta.getColumn(j).getType()==Types.DATE) {
					    			 Date nuevaFecha = new Date();
					                 Calendar cal = Calendar.getInstance(); 
					                 cal.setTime((Date)tabla_Tarjeta.getValueAt(i,j)); 
					                 cal.add(Calendar.DATE, 1);
					                 nuevaFecha = cal.getTime();
					                 tabla_Tarjeta.setValueAt(nuevaFecha, i, j);
					    			 tabla_Tarjeta.getColumn(j).setDateFormat("YYYY/MM/dd");
					    		}
								if ( j == 0 ) es_deposito = tabla_Tarjeta.getValueAt(i,j).equals("depósito");
				    			if ( !es_deposito && tabla_Tarjeta.getColumn(j).equals(col_monto) ) {
				    				BigDecimal ob = (BigDecimal) tabla_Tarjeta.getValueAt(i,j);
				    				BigDecimal negativo = new BigDecimal(0);
				    				negativo = negativo.subtract(ob);
				    				tabla_Tarjeta.setValueAt(negativo, i,j);
				    			}
				    	}	
						es_deposito = false;
					}
					
					pack();
					}
			 catch (SQLException e1) {
				JOptionPane.showMessageDialog(null,"Error al ingresar los datos, vuelva a intentar","Error",JOptionPane.ERROR_MESSAGE);
				error=true;
			}
			}
			fecha_inicio.setText("");
		  	fecha_fin.setText("");
			if(!error) {
			ver_transacciones.setVisible(false);
				eti_fecha_ini.setVisible(false);
				eti_fecha_fin.setVisible(false);
				fecha_inicio.setVisible(false);
				fecha_fin.setVisible(false);
				tabla_Tarjeta.setVisible(true);
				tabla_Tarjeta.setEditable(false);
			pack();
			}
			}
			}
		});
		ver_transacciones.setVisible(false);
		
		fecha_inicio = new JTextField();
		GridBagConstraints gbc_fecha_ini = new GridBagConstraints();
		gbc_fecha_ini.insets = new Insets(0, 0, 5, 0);
		gbc_fecha_ini.fill = GridBagConstraints.HORIZONTAL;
		gbc_fecha_ini.gridx = 0;
		gbc_fecha_ini.gridy = 6;
		panel_tarjeta.add(fecha_inicio, gbc_fecha_ini);
		fecha_inicio.setColumns(10);
		fecha_inicio.setVisible(false);
		
		   	 eti_fecha_fin = new JLabel("Fecha fin de período:");
		eti_fecha_fin.setVisible(false);
		GridBagConstraints gbc_eti_fecha_fin = new GridBagConstraints();
		gbc_eti_fecha_fin.insets = new Insets(0, 0, 5, 0);
		gbc_eti_fecha_fin.gridx = 0;
		gbc_eti_fecha_fin.gridy = 7;
		panel_tarjeta.add(eti_fecha_fin, gbc_eti_fecha_fin);
		
		fecha_fin = new JTextField();
		GridBagConstraints gbc_fecha_fin = new GridBagConstraints();
		gbc_fecha_fin.insets = new Insets(0, 0, 5, 0);
		gbc_fecha_fin.fill = GridBagConstraints.HORIZONTAL;
		gbc_fecha_fin.gridx = 0;
		gbc_fecha_fin.gridy = 8;
		panel_tarjeta.add(fecha_fin, gbc_fecha_fin);
		fecha_fin.setColumns(10);
		fecha_fin.setVisible(false);
		
		separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 9;
		panel_tarjeta.add(separator, gbc_separator);
		GridBagConstraints gbc_ver_transacciones = new GridBagConstraints();
		gbc_ver_transacciones.insets = new Insets(0, 0, 5, 0);
		gbc_ver_transacciones.gridx =	0;
		gbc_ver_transacciones.gridy = 10;
		panel_tarjeta.add(ver_transacciones, gbc_ver_transacciones);
		
		GridBagConstraints gbc_otra_operacion = new GridBagConstraints();
		gbc_otra_operacion.fill = GridBagConstraints.BOTH;
		gbc_otra_operacion.insets = new Insets(0, 0, 5, 0);
		gbc_otra_operacion.gridx = 0;
		gbc_otra_operacion.gridy = 11;
		panel_tarjeta.add(otra_operacion, gbc_otra_operacion);
		
		atras = new JButton("Atrás");
		atras.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				atras.setVisible(false);
				eti_fecha_ini.setVisible(false);
				eti_fecha_fin.setVisible(false);
				fecha_inicio.setVisible(false);
				fecha_fin.setVisible(false);
				ver_transacciones.setVisible(false);
				lblFormatoDeFechas.setVisible(false);
				saldo.setVisible(true);
				ult_mov.setVisible(true);
				mov_periodo.setVisible(true);
				caja_ahorro.setVisible(false);
				monto.setVisible(false);
				ingresar_caja.setVisible(false);
				ingresar_monto.setVisible(false);
				confirmar.setVisible(false);
				transferencia.setVisible(true);
				extraccion.setVisible(true);
				otra_operacion.setVisible(true);
				tabla_Tarjeta.setVisible(false);
				fecha_inicio.setText("");
	      		fecha_fin.setText("");
	      		ingresar_monto.setText("");
	      		ingresar_caja.setText("");
	      		pack();
			}
		});
		GridBagConstraints gbc_atras = new GridBagConstraints();
		gbc_atras.fill = GridBagConstraints.BOTH;
		gbc_atras.gridx = 0;
		gbc_atras.gridy = 12;
		panel_tarjeta.add(atras, gbc_atras);
		atras.setVisible(false);
		this.pack();
		setLocationRelativeTo(null);
	}
	
	private void mntmPrestamosActionPerformed(ActionEvent evt) {

	      logInEmpleadoPrestamo vPrestamo = new logInEmpleadoPrestamo();
	      vPrestamo.setVisible(false);
		  setVisible(false);
	      vPrestamo.setVisible(true);  
	}
	
	private void mntmConsultasActionPerformed(ActionEvent evt) {
		Consultas vAdmin = new Consultas();
		vAdmin.setVisible(true);
		this.setVisible(false);
		}
}