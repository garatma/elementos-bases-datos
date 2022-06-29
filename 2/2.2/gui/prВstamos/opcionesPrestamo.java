package banco;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class opcionesPrestamo extends JFrame {
	protected Connection conexionBD = null;
	private Banco_GUI banco = null;
	private int empleadoLogueado;
	private logInEmpleadoPrestamo vPrestamo;
	private JPanel contentPane;
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu mnMen = new JMenu("Menú");
	private final JButton btnCreacPrest = new JButton("Creación de Prestamos");
	private final JButton btnPagoDeCuotas = new JButton("Pago de Cuotas");
	private final JButton btnClientesMorosos = new JButton("Clientes Morosos");
	private final JLabel lblEmpleado = new JLabel("Empleado: ");
	private final JButton btnCerrarSesion = new JButton("Cerrar Sesión");
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
					opcionesPrestamo frame = new opcionesPrestamo();
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
	public opcionesPrestamo() {
		initGUI();
	}
	private void initGUI() {
		setVisible(true);
		setResizable(false);
		setTitle("Prestamo Opciones");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new java.awt.Dimension(800, 600));
		this.setBounds(0, 0, 800, 600); 
		
		setJMenuBar(menuBar);
		
		menuBar.add(mnMen);
		
		mnMen.add(mntmConsultas);
		
		mnMen.add(mntmAtm);
		mntmPrstamos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmPrestamosActionPerformed(e);

			}
		});
		
		mntmAtm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmATMActionPerformed(e);

			}
		});
		
		mnMen.add(mntmPrstamos);
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmSalirActionPerformed(e);
			}
		});
		
		mnMen.add(mntmSalir);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		btnCreacPrest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCreacPrestActionPerformed(e);
			}
		});
		contentPane.setLayout(new BorderLayout(0, 0));
		
		contentPane.add(btnCreacPrest, BorderLayout.WEST);
		btnPagoDeCuotas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnPagoDeCuotasActionPerformed(evt);
			}
		});
		
		contentPane.add(btnPagoDeCuotas, BorderLayout.CENTER);
		btnClientesMorosos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnClientesMorososActionPerformed(evt);
			}
		});
		
		contentPane.add(btnClientesMorosos, BorderLayout.EAST);
		
		contentPane.add(lblEmpleado, BorderLayout.NORTH);
		btnCerrarSesion.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				btnLogOutActionPerformed(e);
			}
		});
		
		contentPane.add(btnCerrarSesion, BorderLayout.SOUTH);
	}
	
	private void mntmATMActionPerformed(ActionEvent evt){
		ATM atm = new ATM();
		atm.setVisible(true);
		this.setVisible(false);
	}
	
	private void btnCreacPrestActionPerformed(ActionEvent evt) {
		this.setVisible(false);
		datosClienteCrearPrestamo datCliCP = new datosClienteCrearPrestamo();
		datCliCP.setEmpleadoLogueado(this.empleadoLogueado);
		this.setVisible(false);
	}
	
	private void btnPagoDeCuotasActionPerformed(ActionEvent evt) {
		this.setVisible(false);
		datosClientePagoPrestamo dCliPP = new datosClientePagoPrestamo();
		dCliPP.setEmpleadoLogueado(this.empleadoLogueado);
		this.setVisible(false);
	}
	
	private void btnClientesMorososActionPerformed(ActionEvent evt){
		clientesMorosos cMorosos = new clientesMorosos();
		cMorosos.setEmpleadoLogueado(this.empleadoLogueado);
		this.setVisible(false);
	}
	
	private void mntmSalirActionPerformed(ActionEvent evt) {
		System.exit(0);
	}
	
	private void btnLogOutActionPerformed(ActionEvent evt) {
		desconectarBD();	
		banco = new Banco_GUI();
		this.setVisible(false);
		banco.setVisible(true);

	}
	
	private void mntmPrestamosActionPerformed(ActionEvent evt) {
		desconectarBD();
	      this.vPrestamo = new logInEmpleadoPrestamo();
	      this.vPrestamo.setVisible(false);
		  this.setVisible(false);
	      this.vPrestamo.setVisible(true);  
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
	
	
	public void setEmpleadoLogueado(int emp) {
		this.empleadoLogueado = emp;
		lblEmpleado.setText("Empleado: "+String.valueOf(emp));
	}
	
	public int getEmpleadoLogueado() {
		return empleadoLogueado;
	}

}
