package banco;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import quick.dbtable.DBTable;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;


@SuppressWarnings("serial")
public class Consultas extends JFrame {
	private JPanel contentPane;
	private JPanel panel_pass;
	private JPanel panel_principal;
	private JPasswordField field_pass;
	private JLabel label_consulta;
	private JPanel panel_medio;
	private JLabel label_tabla;
	private JButton boton_GO;
	private JScrollPane scroll_consulta;
	private JTextArea area_consulta;
	private JButton boton_limpiar_consulta;
	private boolean primera = true;
	private java.sql.Connection conexion;
	private DBTable db_tabla;
	private int cant_tablas = 0;
	private JPanel panel_lista;
	private JPanel panel_atributos;
	private JLabel lblTabla;
	private JLabel label_atributos;
	private JList <Object> lista_tablas;
	private JList <Object> lista_argumentos;
	private JMenuBar menuBar;
	private JMenu mnMenu;
	private JMenuItem mntmConsultas;
	private JMenuItem mntmAtm;
	private JMenuItem mntmPrstamos;
	private JMenuItem mntmSalir;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Consultas frame = new Consultas();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Consultas() {
		setTitle("Base de datos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 809, 188);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnMenu = new JMenu("Men\u00FA");
		menuBar.add(mnMenu);
		
		mntmConsultas = new JMenuItem("Consultas");
		mnMenu.add(mntmConsultas);
		
		mntmAtm = new JMenuItem("ATM");
		mntmAtm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmATMActionPerformed(e);
			}
		});
		mnMenu.add(mntmAtm);
		
		mntmPrstamos = new JMenuItem("Pr\u00E9stamos");
		mntmPrstamos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmPrestamosActionPerformed(e);
			}
		});
		mnMenu.add(mntmPrstamos);
		
		mntmSalir = new JMenuItem("Salir");
		mnMenu.add(mntmSalir);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		panel_principal = new JPanel();
		panel_principal.setVisible(false);
		contentPane.add(panel_principal);
		GridBagLayout gbl_panel_principal = new GridBagLayout();
		gbl_panel_principal.columnWidths = new int[]{560, 0};
		gbl_panel_principal.rowHeights = new int[]{25, 135, 0, 0, 0};
		gbl_panel_principal.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_principal.rowWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		panel_principal.setLayout(gbl_panel_principal);
		
		label_consulta = new JLabel("Consulta:");
		GridBagConstraints gbc_label_consulta = new GridBagConstraints();
		gbc_label_consulta.anchor = GridBagConstraints.WEST;
		gbc_label_consulta.insets = new Insets(0, 0, 5, 0);
		gbc_label_consulta.gridx = 0;
		gbc_label_consulta.gridy = 0;
		panel_principal.add(label_consulta, gbc_label_consulta);
		
		scroll_consulta = new JScrollPane();
		GridBagConstraints gbc_scroll_consulta = new GridBagConstraints();
		gbc_scroll_consulta.insets = new Insets(0, 0, 5, 0);
		gbc_scroll_consulta.fill = GridBagConstraints.BOTH;
		gbc_scroll_consulta.gridx = 0;
		gbc_scroll_consulta.gridy = 1;
		panel_principal.add(scroll_consulta, gbc_scroll_consulta);
		
		area_consulta = new JTextArea();
		area_consulta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if ( primera ) {
					area_consulta.setEnabled(true);
					area_consulta.setText("");
					primera = false;
				}
			}
		});
		scroll_consulta.setViewportView(area_consulta);
		area_consulta.setText("Ingrese una consulta");
		area_consulta.setEnabled(false);
		
		
		panel_medio = new JPanel();
		GridBagConstraints gbc_panel_medio = new GridBagConstraints();
		gbc_panel_medio.insets = new Insets(0, 0, 5, 0);
		gbc_panel_medio.fill = GridBagConstraints.BOTH;
		gbc_panel_medio.gridx = 0;
		gbc_panel_medio.gridy = 2;
		panel_principal.add(panel_medio, gbc_panel_medio);
		GridBagLayout gbl_panel_medio = new GridBagLayout();
		gbl_panel_medio.columnWidths = new int[]{440, 0, 82, 0, 0};
		gbl_panel_medio.rowHeights = new int[]{0, 0};
		gbl_panel_medio.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_medio.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_medio.setLayout(gbl_panel_medio);
		
		label_tabla = new JLabel("Resultado en tabla:");
		GridBagConstraints gbc_label_tabla = new GridBagConstraints();
		gbc_label_tabla.anchor = GridBagConstraints.WEST;
		gbc_label_tabla.insets = new Insets(0, 0, 0, 5);
		gbc_label_tabla.gridx = 0;
		gbc_label_tabla.gridy = 0;
		panel_medio.add(label_tabla, gbc_label_tabla);
		
		boton_limpiar_consulta = new JButton("Limpiar consulta");
		boton_limpiar_consulta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				area_consulta.setText("");
			}
		});
		GridBagConstraints gbc_boton_limpiar_consulta = new GridBagConstraints();
		gbc_boton_limpiar_consulta.insets = new Insets(0, 0, 0, 5);
		gbc_boton_limpiar_consulta.gridx = 2;
		gbc_boton_limpiar_consulta.gridy = 0;
		panel_medio.add(boton_limpiar_consulta, gbc_boton_limpiar_consulta);
		
		boton_GO = new JButton("GO");
		boton_GO.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				realizar_consulta();
			}
		});
		GridBagConstraints gbc_boton_GO = new GridBagConstraints();
		gbc_boton_GO.gridx = 3;
		gbc_boton_GO.gridy = 0;
		panel_medio.add(boton_GO, gbc_boton_GO);

    	db_tabla = new DBTable();
    	db_tabla.setSortEnabled(false);
    	db_tabla.setEditable(false);
    	db_tabla.removeColumn(db_tabla.getColumn(0));
    	db_tabla.removeColumn(db_tabla.getColumn(0));
    	db_tabla.removeColumn(db_tabla.getColumn(0));
    	db_tabla.removeColumn(db_tabla.getColumn(0));
		GridBagConstraints gbc_db_table = new GridBagConstraints();
		gbc_db_table.fill = GridBagConstraints.BOTH;
		gbc_db_table.gridx = 0;
		gbc_db_table.gridy = 3;
		panel_principal.add(db_tabla, gbc_db_table);
		
		panel_pass = new JPanel();
		contentPane.add(panel_pass);
		GridBagLayout gbl_panel_pass = new GridBagLayout();
		gbl_panel_pass.columnWidths = new int[]{118, 128, 0, 0};
		gbl_panel_pass.rowHeights = new int[]{0, 0};
		gbl_panel_pass.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_pass.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_pass.setLayout(gbl_panel_pass);
		
		JLabel label_pass = new JLabel("Ingrese la contrase\u00F1a");
		GridBagConstraints gbc_label_pass = new GridBagConstraints();
		gbc_label_pass.insets = new Insets(0, 0, 0, 5);
		gbc_label_pass.gridx = 0;
		gbc_label_pass.gridy = 0;
		panel_pass.add(label_pass, gbc_label_pass);
		
		field_pass = new JPasswordField();
		field_pass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				establecer_conexion(field_pass.getPassword());
			}
		});
		GridBagConstraints gbc_field_pass = new GridBagConstraints();
		gbc_field_pass.fill = GridBagConstraints.HORIZONTAL;
		gbc_field_pass.insets = new Insets(0, 0, 0, 5);
		gbc_field_pass.gridx = 1;
		gbc_field_pass.gridy = 0;
		panel_pass.add(field_pass, gbc_field_pass);
		
		JButton boton_pass = new JButton("OK");
		boton_pass.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				establecer_conexion(field_pass.getPassword());
			}
		});
		GridBagConstraints gbc_boton_pass = new GridBagConstraints();
		gbc_boton_pass.gridx = 2;
		gbc_boton_pass.gridy = 0;
		panel_pass.add(boton_pass, gbc_boton_pass);
		
		panel_lista = new JPanel();
		panel_lista.setVisible(false);
		contentPane.add(panel_lista);
		GridBagLayout gbl_panel_lista = new GridBagLayout();
		gbl_panel_lista.columnWidths = new int[]{116, 0};
		gbl_panel_lista.rowHeights = new int[]{0, 25, 0};
		gbl_panel_lista.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_lista.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel_lista.setLayout(gbl_panel_lista);
		
		lblTabla = new JLabel("Tabla:");
		GridBagConstraints gbc_lblTabla = new GridBagConstraints();
		gbc_lblTabla.insets = new Insets(0, 0, 5, 0);
		gbc_lblTabla.gridx = 0;
		gbc_lblTabla.gridy = 0;
		panel_lista.add(lblTabla, gbc_lblTabla);
		
		lista_tablas = new JList<Object>();
		lista_tablas.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) incializar_lista_atributos(lista_tablas.getSelectedValue().toString());
			}
		});
		GridBagConstraints gbc_lista_tablas = new GridBagConstraints();
		gbc_lista_tablas.fill = GridBagConstraints.BOTH;
		gbc_lista_tablas.gridx = 0;
		gbc_lista_tablas.gridy = 1;
		panel_lista.add(lista_tablas, gbc_lista_tablas);
		
		panel_atributos = new JPanel();
		panel_atributos.setVisible(false);
		contentPane.add(panel_atributos);
		GridBagLayout gbl_panel_atributos = new GridBagLayout();
		gbl_panel_atributos.columnWidths = new int[]{105, 0};
		gbl_panel_atributos.rowHeights = new int[]{0, 34, 0};
		gbl_panel_atributos.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_atributos.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel_atributos.setLayout(gbl_panel_atributos);
		
		label_atributos = new JLabel("Atributos:");
		GridBagConstraints gbc_label_atributos = new GridBagConstraints();
		gbc_label_atributos.insets = new Insets(0, 0, 5, 0);
		gbc_label_atributos.gridx = 0;
		gbc_label_atributos.gridy = 0;
		panel_atributos.add(label_atributos, gbc_label_atributos);
		
		lista_argumentos = new JList<Object>();
		GridBagConstraints gbc_lista_argumentos = new GridBagConstraints();
		gbc_lista_argumentos.fill = GridBagConstraints.BOTH;
		gbc_lista_argumentos.gridx = 0;
		gbc_lista_argumentos.gridy = 1;
		panel_atributos.add(lista_argumentos, gbc_lista_argumentos);
		this.pack();
	}

	protected void incializar_lista_atributos(String tabla) {
		java.sql.Statement statem;
		try {
			statem = conexion.createStatement();
			java.sql.ResultSet res = statem.executeQuery("describe "+tabla);
			int filas = 0;
			while ( res.next() ) filas ++;
			Object [] atributos = new String[filas];
			res.absolute(0);
			int i = 0;
			while ( res.next() ) {
				atributos[i++] = res.getString(1);
				res.absolute(i);
			}
			lista_argumentos.setListData(atributos);
			panel_atributos.setVisible(true);
			statem.close();
		} catch (SQLException e) {
			cartel("Error al mostrar los datos de la tabla seleccionada.\nDetalle: "+ e.getMessage() + "\nCódigo de error MySQL: "+ e.getErrorCode() +"\nCódigo de error SQL: "+ e.getSQLState(),"Error",JOptionPane.ERROR_MESSAGE);
		}
		this.pack();
	}

	protected void realizar_consulta() {
		String consulta = area_consulta.getText().trim();
			try {
				
				java.sql.Statement statem = conexion.createStatement(); 
				boolean tipo_consulta = statem.execute(consulta);
				
				if ( tipo_consulta ) {
					db_tabla.setSelectSql(consulta);
					db_tabla.createColumnModelFromQuery();    	  
					db_tabla.refresh();
					boolean error=false;
					for (int i=0; !error && i<db_tabla.getRowCount(); i++) {
						for (int j = 0; !error && j < db_tabla.getColumnCount(); j++) {
								
								if	 (db_tabla.getColumn(j).getType()==Types.TIME)   {
									 Date nuevaFecha = new Date();
					                 Calendar cal = Calendar.getInstance(); 
					                 cal.setTime((Date)db_tabla.getValueAt(i,j)); 
					                 cal.add(Calendar.HOUR, 3);
					                 nuevaFecha = cal.getTime();
					                 db_tabla.setValueAt(nuevaFecha, i, j);
					                 db_tabla.getColumn(j).setDateFormat("hh:mm:ss");
					  	       	 }
					    		 if	 (db_tabla.getColumn(j).getType()==Types.DATE) {
					    			 Date nuevaFecha = new Date();
					                 Calendar cal = Calendar.getInstance(); 
					                 cal.setTime((Date)db_tabla.getValueAt(i,j)); 
					                 
					                 cal.add(Calendar.DATE, 1);
					                 nuevaFecha = cal.getTime();
					                 db_tabla.setValueAt(nuevaFecha, i, j);
					                 db_tabla.getColumn(j).setDateFormat("dd/MM/YYYY");
					    		}   	     	  
			    	
						}
					}
				}
				else {
					iniciar_lista();
					cartel("La consulta ingresada se realizó con éxito.","Consulta exitosa",JOptionPane.INFORMATION_MESSAGE);
				}
				statem.close();
			}
			catch ( SQLException e ) {
				cartel("Error al ejecutar la consulta.\nDetalle: "+ e.getMessage() + "\nCódigo de error MySQL: "+ e.getErrorCode() +"\nCódigo de error SQL: "+ e.getSQLState(),"Error",JOptionPane.ERROR_MESSAGE);
			}
			this.pack();
	}
	
	private void mntmPrestamosActionPerformed(ActionEvent evt) {
		logInEmpleadoPrestamo vPrestamo = new logInEmpleadoPrestamo();
		vPrestamo.setVisible(true);
		this.setVisible(false);
	}
	
	private void mntmATMActionPerformed(ActionEvent evt) {
		ATM atm = new ATM();
		atm.setVisible(true);
		this.setVisible(false);
	}

	protected void establecer_conexion(char[] pass_arr) {
		String clave = "";
		int i = 0;
		while ( i < pass_arr.length ) {
			clave += pass_arr[i];
			pass_arr[i++] = 0;
		}
		try {
	        String driver ="com.mysql.cj.jdbc.Driver";
	        String url = "jdbc:mysql://localhost:3306/banco?serverTimezone=UTC";
			db_tabla.connectDatabase(driver, url, "admin", clave);
			conexion = db_tabla.getConnection();
			iniciar_lista();
			panel_pass.setVisible(false);
			panel_lista.setVisible(true);
			panel_principal.setVisible(true);
			this.setTitle("Base de datos del banco - Usuario admin");
			cartel("Conexión exitosa establecida con la base de datos.","Ingreso",JOptionPane.INFORMATION_MESSAGE);
		}
		catch( java.sql.SQLException e) {
			cartel("Error al establecer la conexión con la base de datos.\nDetalle: "+e.getMessage()+"\nCódigo de error de MySQL: "+ e.getErrorCode() + "\nCódigo de error de SQL: "+e.getSQLState(),"Error",JOptionPane.ERROR_MESSAGE);
		}
		catch (ClassNotFoundException e) {
			cartel("Error al establecer la conexión con la base de datos.\nDetalle: "+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
		this.pack();
	}
	
	protected void cartel(String mensaje, String titulo, int tipo_mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, titulo, tipo_mensaje);
	}

	protected void iniciar_lista() {
		java.sql.Statement statem;
		try {
			lista_tablas.removeAll();
			statem = conexion.createStatement();
			java.sql.ResultSet res = statem.executeQuery("show tables from banco;");
			while ( res.next() ) cant_tablas ++;
			Object [] tablas = new String[cant_tablas];
			res.absolute(0);
			int i = 0;
			while ( res.next() ) tablas[i++] = res.getString(1);
			lista_tablas.setListData(tablas);
			statem.close();
		} catch (SQLException e) {
			cartel("Error al establecer la conexión con la base de datos.\nDetalle: "+e.getMessage()+"\nCódigo de error de MySQL: "+ e.getErrorCode() + "\nCódigo de error de SQL: "+e.getSQLState(),"Error",JOptionPane.ERROR_MESSAGE);
		}
	}
}
