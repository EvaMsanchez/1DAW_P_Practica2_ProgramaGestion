package es.studium.tienda_bicicletas;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

public class MenuPrincipal implements WindowListener, ActionListener
{
	//Menú.
	Frame ventana = new Frame("Menú Principal");
	
	//En primer lugar creamos la barra de menú (barra principal).
	MenuBar barraMenu = new MenuBar();
	
	//Ahora creamos los elementos principales del menú.
	Menu menuClientes = new Menu("Clientes");
	Menu menuArticulos = new Menu("Artículos");
	Menu menuFacturas = new Menu("Facturas");
	Menu menuAyuda = new Menu("Ayuda");
	
	//Y Ahora los elementos de cada opción del menú principal.
	// 1. Los de Clientes.
	MenuItem mniClientesNuevo = new MenuItem("Nuevo");
	MenuItem mniClientesListado = new MenuItem("Listado");
	MenuItem mniClientesBaja = new MenuItem("Baja");
	MenuItem mniClientesModificar = new MenuItem("Modificar");
	
	// 2. Los de Artículos.
	MenuItem mniArticulosNuevo = new MenuItem("Nuevo");
	MenuItem mniArticulosListado = new MenuItem("Listado");
	MenuItem mniArticulosBaja = new MenuItem("Baja");
	MenuItem mniArticulosModificar = new MenuItem("Modificar");
	
	// 3. Los de Facturas.
	MenuItem mniFacturasAlta = new MenuItem("Alta");
	MenuItem mniFacturasListado = new MenuItem("Listado");
	
	// 4. El de Ayuda.
	MenuItem mniAyuda = new MenuItem("Acerca de");
	
	
	int perfilUsuario;
	String usuario;
	
	public MenuPrincipal(int t, String u)
	{
		perfilUsuario = t;
		usuario = u;
		
		ventana.setLayout (new FlowLayout());
		ventana.addWindowListener(this);
		
		// 1. De lo más interior (pequeño) hacia afuera.
		//Empezamos a añadirle los elementos a Clientes.
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
		
		// 2. Seguimos con los elementos de Artículos.
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
		
		// 3. Seguimos con los elementos de Facturas.
		mniFacturasAlta.addActionListener(this);
		mniFacturasListado.addActionListener(this);
		menuFacturas.add(mniFacturasAlta);
		if(perfilUsuario==0)
		{
			menuFacturas.add(mniFacturasListado);
		}
		
		// 4. Continuamos con elemento de Ayuda.
		mniAyuda.addActionListener(this);
		menuAyuda.add(mniAyuda);
		
		// 5. Por último agregamos los elementos Clientes, Articulos, Facturas y Ayuda a la barra.
		barraMenu.add(menuClientes);
		barraMenu.add(menuArticulos);
		barraMenu.add(menuFacturas);
		barraMenu.add(menuAyuda);
		
		// 6. Establecemos la barra de menú.
		ventana.setMenuBar(barraMenu);
		
		ventana.setSize(470,300);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true); 
	}
	
	
	//Interfaces.
	@Override
	//Aquí funciones de los menús.
	public void actionPerformed(ActionEvent e)
	{
		//NUEVO cliente.
		if(e.getSource().equals(mniClientesNuevo))
		{
			new NuevoCliente(usuario);
		}
		//LISTADO clientes.
		else if(e.getSource().equals(mniClientesListado))
		{
			new ListadoClientes(usuario);
		}
		//ELIMINAR cliente.
		else if(e.getSource().equals(mniClientesBaja))
		{
			new EliminarCliente(usuario);
		}
		//MODIFICAR cliente.
		else if(e.getSource().equals(mniClientesModificar))
		{
			new ModificarCliente(usuario);
		}
		//NUEVO artículo.
		else if(e.getSource().equals(mniArticulosNuevo))
		{
			new NuevoArticulo(usuario);
		}
		//LISTADO artículos.
		else if(e.getSource().equals(mniArticulosListado))
		{
			new ListadoArticulos(usuario);
		}
		//ELIMINAR artículo.
		else if(e.getSource().equals(mniArticulosBaja))
		{
			new EliminarArticulo(usuario);
		}
		//MODIFICAR artículo.
		else if(e.getSource().equals(mniArticulosModificar))
		{
			new ModificarArticulo(usuario);
		}
		//ALTA factura.
		else if(e.getSource().equals(mniFacturasAlta))
		{
			new AltaFactura(usuario);
		}
		//LISTADO facturas.
		else if(e.getSource().equals(mniFacturasListado))
		{
			new ListadoFacturas(usuario);
		}
		//AYUDA.
		else if(e.getSource().equals(mniAyuda))
		{
			try 
			{
				Runtime.getRuntime().exec("hh.exe C:\\Users\\EVA\\Desktop\\DAW\\1º CURSO\\P\\PRÁCTICAS\\tema 4-5-6-7\\Práctica2P_ProgramaGestión_Eva\\ayuda\\ayuda.chm");
			}
			catch (IOException ee) 
			{
				ee.printStackTrace();
			}
		}
	}
	
	
	//Aquí funciones de la ventana windows.
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
