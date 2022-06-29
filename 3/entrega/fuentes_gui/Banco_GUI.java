package banco;

import java.sql.Connection;



import java.awt.EventQueue;



import javax.swing.JFrame;

import javax.swing.JPanel;

import javax.swing.border.EmptyBorder;

import banco.logInEmpleadoPrestamo;
import quick.dbtable.DBTable;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;


import java.awt.Color;
import java.awt.event.ActionListener;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Banco_GUI extends JFrame {
	private logInEmpleadoPrestamo vPrestamo;
	protected Connection conexionBD = null;

	DBTable tablaTasaPrestamo;


	protected int seleccionado = -1;


	private JPanel contentPane;
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu mnMen = new JMenu("Menú");
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
					Banco_GUI frame = new Banco_GUI();
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
	public Banco_GUI() {
		initGUI();
	}
	private void initGUI() {
		setResizable(false);
		setTitle("Gestion Banco");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		

		
		setJMenuBar(menuBar);
		
		menuBar.add(mnMen);
		mntmConsultas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mntmConsultasActionPerformed(evt);
			}
		});
		
		mnMen.add(mntmConsultas);
		mntmAtm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mntmATMActionPerformed(evt);
				
			}
		});
		
		mnMen.add(mntmAtm);
		mntmPrstamos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmPrestamosActionPerformed(e);
				
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
		contentPane.setForeground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
	
	private void mntmConsultasActionPerformed(ActionEvent evt) {
		Consultas vAdmin = new Consultas();
		vAdmin.setVisible(true);
		this.setVisible(false);
	}
	
	private void mntmSalirActionPerformed(ActionEvent evt) {
		System.exit(0);
	}
	
	private void mntmATMActionPerformed(ActionEvent evt) {
		ATM atm = new ATM();
		atm.setVisible(true);
		this.setVisible(false);
		atm.setLocationRelativeTo(null); 

	}
	
	private void mntmPrestamosActionPerformed(ActionEvent e) {
	      this.vPrestamo = new logInEmpleadoPrestamo();
	      this.vPrestamo.setVisible(false);
		  this.setVisible(false);
	      this.vPrestamo.setVisible(true);  
		
	}
}
