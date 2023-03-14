package es.studium.Tienda_Bicicletas;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ListadoClientes implements WindowListener
{
	Frame ventanaListadoClientes = new Frame("Listado Clientes");

	TextArea areaTexto = new TextArea(""); //"": vacío, alto y ancho.
	
	Label lblId = new Label("Id:");
	Label lblNombre = new Label("Nombre:");
	Label lblDni = new Label("DNI:");
	Label lblTelefono = new Label("Teléfono:");

	Button btnPdf = new Button("Exportar a PDF");
	
	//Objeto conexión para utilizar el método "listadoClientes".
	Conexion conexion = new Conexion();

	ListadoClientes()
	{
		//Distribución.
		ventanaListadoClientes.setLayout(null);
		//Se añade para darle funcionalidad a la ventana.
		ventanaListadoClientes.addWindowListener(this);
		
		ventanaListadoClientes.setBackground(Color.LIGHT_GRAY);
		lblId.setBounds(40, 40, 40, 40);
		lblNombre.setBounds(85, 40, 50, 40);
		lblDni.setBounds(190, 40, 40, 40);
		lblTelefono.setBounds(275, 40, 60, 40);
		
		ventanaListadoClientes.add(lblId);
		ventanaListadoClientes.add(lblNombre);
		ventanaListadoClientes.add(lblDni);
		ventanaListadoClientes.add(lblTelefono);
		
		//Rellenar TextArea
		conexion.rellenarListadoClientes(areaTexto);
		
		areaTexto.setBounds(35, 80, 340, 150);
		ventanaListadoClientes.add(areaTexto);
		
		btnPdf.setBounds(150, 250, 110, 30);
		ventanaListadoClientes.add(btnPdf);

		ventanaListadoClientes.setSize(410,315);
		ventanaListadoClientes.setResizable(false);
		ventanaListadoClientes.setLocationRelativeTo(null);
		
		ventanaListadoClientes.setVisible(true);
	}

	
	//Interfaces.
	//Aquí funciones de la ventana windows.
	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent e)
	{
		ventanaListadoClientes.setVisible(false);
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

}
