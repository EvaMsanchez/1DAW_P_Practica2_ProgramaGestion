package es.studium.tienda_bicicletas;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ListadoFacturas implements WindowListener, ActionListener
{
	Frame ventanaListadoFacturas = new Frame("Listado Facturas");

	TextArea areaTexto = new TextArea(""); //"": vacío, alto y ancho.
	
	Label lblId = new Label("Id:");
	Label lblFecha = new Label("Fecha:");
	Label lblPago = new Label("Pago:");
	Label lblTotal = new Label("Total Fra.:");
	Label lblCliente = new Label("Cliente:");

	Button btnPdf = new Button("Exportar a PDF");
	
	//Objeto conexión para utilizar el método "listadoClientes".
	Conexion conexion = new Conexion();
	
	String usuario;

	ListadoFacturas(String u)
	{
		usuario = u;
		
		//Distribución.
		ventanaListadoFacturas.setLayout(null);
		//Se añade para darle funcionalidad a la ventana.
		ventanaListadoFacturas.addWindowListener(this);
		
		ventanaListadoFacturas.setBackground(Color.LIGHT_GRAY);
		lblId.setBounds(40, 40, 40, 40);
		lblFecha.setBounds(85, 40, 50, 40);
		lblPago.setBounds(184, 40, 40, 40);
		lblTotal.setBounds(273, 40, 60, 40);
		lblCliente.setBounds(370, 40, 50, 40);
		
		ventanaListadoFacturas.add(lblId);
		ventanaListadoFacturas.add(lblFecha);
		ventanaListadoFacturas.add(lblPago);
		ventanaListadoFacturas.add(lblTotal);
		ventanaListadoFacturas.add(lblCliente);
		
		//Rellenar TextArea
		conexion.rellenarListadoFacturas(areaTexto, usuario); //Le pasamos nombre "usuario" para archivo .LOG
		
		areaTexto.setBounds(35, 80, 430, 150);
		ventanaListadoFacturas.add(areaTexto);
		
		btnPdf.setBounds(195, 250, 110, 30);
		btnPdf.addActionListener(this);
		ventanaListadoFacturas.add(btnPdf);

		ventanaListadoFacturas.setSize(500,315);
		ventanaListadoFacturas.setResizable(false);
		ventanaListadoFacturas.setLocationRelativeTo(null);
		
		ventanaListadoFacturas.setVisible(true);
	}

	
	//Interfaces.
	//Aquí funciones de la ventana windows.
	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent e)
	{
		ventanaListadoFacturas.setVisible(false);
	}
	@Override
	public void windowClosed(WindowEvent e){}
	@Override
	public void windowIconified(WindowEvent e){}
	@Override
	public void windowDeiconified(WindowEvent e){}
	@Override
	public void windowActivated(WindowEvent e){}
	@Override
	public void windowDeactivated(WindowEvent e){}


	@Override
	public void actionPerformed(ActionEvent e)
	{
		//Al pulsar el botón PDF.
		if(e.getSource().equals(btnPdf)) //Donde se produce el evento es igual al botón PDF.
		{
			conexion.generarPdfFacturas();
		}
	}
	
	
	/*Main.
			public static void main(String[] args)
			{
				new ListadoFacturas();
			}*/
}
