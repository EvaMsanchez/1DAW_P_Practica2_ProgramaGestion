package es.studium.Tienda_Bicicletas;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ListadoArticulos implements WindowListener
{
	Frame ventanaListadoArticulos = new Frame("Listado Articulos");

	TextArea areaTexto = new TextArea(""); //"": vacío, alto y ancho.
	
	Label lblId = new Label("Id:");
	Label lblDescripcion = new Label("Descripción:");
	Label lblMarca = new Label("Marca:");
	Label lblPrecio = new Label("Precio:");

	Button btnPdf = new Button("Exportar a PDF");
	
	//Objeto conexión para utilizar el método "listadoArticulos".
	Conexion conexion = new Conexion();

	ListadoArticulos()
	{
		//Distribución.
		ventanaListadoArticulos.setLayout(null);
		//Se añade para darle funcionalidad a la ventana.
		ventanaListadoArticulos.addWindowListener(this);
		
		ventanaListadoArticulos.setBackground(Color.LIGHT_GRAY);
		lblId.setBounds(40, 40, 40, 40);
		lblDescripcion.setBounds(85, 40, 68, 40);
		lblMarca.setBounds(231, 40, 40, 40);
		lblPrecio.setBounds(307, 40, 60, 40);
		
		ventanaListadoArticulos.add(lblId);
		ventanaListadoArticulos.add(lblDescripcion);
		ventanaListadoArticulos.add(lblMarca);
		ventanaListadoArticulos.add(lblPrecio);

		//Rellenar TextArea
		conexion.rellenarListadoArticulos(areaTexto);
		
		areaTexto.setBounds(35, 80, 340, 150);
		ventanaListadoArticulos.add(areaTexto);
		
		btnPdf.setBounds(150, 250, 110, 30);
		ventanaListadoArticulos.add(btnPdf);

		ventanaListadoArticulos.setSize(410,315);
		ventanaListadoArticulos.setResizable(false);
		ventanaListadoArticulos.setLocationRelativeTo(null);
		
		ventanaListadoArticulos.setVisible(true);
	}

	
	//Interfaces.
	//Aquí funciones de la ventana windows.
	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent e)
	{
		ventanaListadoArticulos.setVisible(false);
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
