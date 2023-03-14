package es.studium.Tienda_Bicicletas;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MenuPrincipal implements WindowListener, ActionListener
{
	//Men�.
	Frame ventana = new Frame("Men� Principal");
	
	//En primer lugar creamos la barra de men� (barra principal).
	MenuBar barraMenu = new MenuBar();
	
	//Ahora creamos los elementos principales del men�.
	Menu menuClientes = new Menu("Clientes");
	Menu menuArticulos = new Menu("Art�culos");
	Menu menuFacturas = new Menu("Facturas");
	
	//Y Ahora los elementos de cada opci�n del men� principal.
	// 1. Los de Clientes.
	MenuItem mniClientesNuevo = new MenuItem("Nuevo");
	MenuItem mniClientesListado = new MenuItem("Listado");
	MenuItem mniClientesBaja = new MenuItem("Baja");
	MenuItem mniClientesModificar = new MenuItem("Modificar");
	
	// 2. Los de Art�culos.
	MenuItem mniArticulosNuevo = new MenuItem("Nuevo");
	MenuItem mniArticulosListado = new MenuItem("Listado");
	MenuItem mniArticulosBaja = new MenuItem("Baja");
	MenuItem mniArticulosModificar = new MenuItem("Modificar");
	
	int perfilUsuario;
	
	public MenuPrincipal(int t)
	{
		perfilUsuario = t;
		
		ventana.setLayout (new FlowLayout());
		ventana.addWindowListener(this);
		
		// 1. De lo m�s interior (peque�o) hacia afuera.
		//Empezamos a a�adirle los elementos a Clientes.
		mniClientesNuevo.addActionListener(this);
		mniClientesListado.addActionListener(this);
		mniClientesBaja.addActionListener(this);
		mniClientesModificar.addActionListener(this);
		menuClientes.add(mniClientesNuevo);
		if(perfilUsuario==0)
		{
			menuClientes.add(mniClientesListado);
			menuClientes.addSeparator();
			menuClientes.add(mniClientesModificar);
			menuClientes.add(mniClientesBaja);
		}
		
		// 2. Seguimos con los elementos de Art�culos.
		mniArticulosNuevo.addActionListener(this);
		mniArticulosListado.addActionListener(this);
		mniArticulosBaja.addActionListener(this);
		mniArticulosModificar.addActionListener(this);
		menuArticulos.add(mniArticulosNuevo);
		if(perfilUsuario==0)
		{
			menuArticulos.add(mniArticulosListado);
			menuArticulos.addSeparator();
			menuArticulos.add(mniArticulosModificar);
			menuArticulos.add(mniArticulosBaja);
		}
					
		// 3. Por �ltimo agregamos los elementos Clientes, Articulos y Facturas a la barra.
		barraMenu.add(menuClientes);
		barraMenu.add(menuArticulos);
		barraMenu.add(menuFacturas);
		
		// 4. Establecemos la barra de men�.
		ventana.setMenuBar(barraMenu);
		
		ventana.setSize(470,300);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true); 
	}
	
	
	//Interfaces.
	@Override
	//Aqu� funciones de los men�s.
	public void actionPerformed(ActionEvent e)
	{
		//NUEVO cliente.
		if(e.getSource().equals(mniClientesNuevo))
		{
			new NuevoCliente();
		}
		//LISTADO clientes.
		else if(e.getSource().equals(mniClientesListado))
		{
			new ListadoClientes();
		}
		//ELIMINAR cliente.
		else if(e.getSource().equals(mniClientesBaja))
		{
			new EliminarCliente();
		}
		//MODIFICAR cliente.
		else if(e.getSource().equals(mniClientesModificar))
		{
			new ModificarCliente();
		}
		//NUEVO art�culo.
		else if(e.getSource().equals(mniArticulosNuevo))
		{
			new NuevoArticulo();
		}
		//LISTADO art�culos.
		else if(e.getSource().equals(mniArticulosListado))
		{
			new ListadoArticulos();
		}
		//ELIMINAR art�culo.
		else if(e.getSource().equals(mniArticulosBaja))
		{
			new EliminarArticulo();
		}
		//MODIFICAR art�culo.
		else if(e.getSource().equals(mniArticulosModificar))
		{
			new ModificarArticulo();
		}
	}
	
	
	//Aqu� funciones de la ventana windows.
	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent e)
	{
		System.exit(0); //Para cerrar la ventana con la "X". 
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
