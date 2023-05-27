package es.studium.tienda_bicicletas;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class EliminarCliente implements WindowListener, ActionListener
{
	Frame ventanaEliminarCliente = new Frame("Baja Cliente");
	Dialog dlgMensaje = new Dialog(ventanaEliminarCliente, "¿Segur@?", true); //"True": porque es modal.
	Dialog dlgMensajeFinal = new Dialog(dlgMensaje, "Mensaje", true);
	
	Label lblElegir = new Label("Elegir Cliente a Eliminar:");
	Choice choClientes = new Choice(); //Lista desplegable.
	
	//Componentes del diálogo.
	Label lblMensaje = new Label("¿Seguro de Eliminar?");
	Button btnSi= new Button("Si");
	Button btnNo= new Button("No");
	//Otro diálogo.
	Label lblMensajeFinal = new Label("Cliente Eliminado");

	Button btnEliminar= new Button("Eliminar");
	
	//Objeto conexión para utilizar el método.
	Conexion conexion = new Conexion();
	
	String usuario;

	EliminarCliente(String u)
	{
		usuario = u;
		
		//Distribución.
		ventanaEliminarCliente.setLayout(null);
		//Se añade para darle funcionalidad a la ventana.
		ventanaEliminarCliente.addWindowListener(this);
		
		ventanaEliminarCliente.setBackground(Color.LIGHT_GRAY);
		lblElegir.setBounds(94, 30, 150, 50);
		ventanaEliminarCliente.add(lblElegir);
		
		//Rellenar el Choice.
		conexion.rellenarChoiceClientes(choClientes); //Método le mandamos el choice para que lo rellene.
		
		choClientes.setBounds(75, 85, 170, 40);
		ventanaEliminarCliente.add(choClientes);
		
		btnEliminar.addActionListener(this);
		btnEliminar.setBounds(110, 135, 100, 30);
		ventanaEliminarCliente.add(btnEliminar);

		ventanaEliminarCliente.setSize(320,200);
		ventanaEliminarCliente.setResizable(false);
		ventanaEliminarCliente.setLocationRelativeTo(null);
		ventanaEliminarCliente.setVisible(true);
	}

	
	//Interfaces.
	//Aquí funciones de la ventana windows.
	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent e)
	{
		if(dlgMensajeFinal.isActive())
		{
			dlgMensajeFinal.setVisible(false);
			dlgMensaje.setVisible(false);
			ventanaEliminarCliente.setVisible(false);
		}
		else if(dlgMensaje.isActive())
		{
			dlgMensaje.setVisible(false);
		}
		else
		{
			ventanaEliminarCliente.setVisible(false);
		}
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


	//Aquí funciones de los botones.
	@Override
	public void actionPerformed(ActionEvent e)
	{
		//Al pulsar el botón ELIMINAR.
		if(e.getSource().equals(btnEliminar)) //Donde se produce el evento es igual al botón Eliminar.
		{
			if(choClientes.getSelectedIndex()!=0)
			{
				dlgMensaje.setLayout(null);
				dlgMensaje.addWindowListener(this);
				dlgMensaje.setSize(270,145);
				
				//Para que salga el todo lo que se ha elegido en el choice (id y nombre).
				lblMensaje.setBounds(35, 35, 200, 50);
				lblMensaje.setText("¿Seguro de eliminar  "+choClientes.getSelectedItem()+"?"); 
				
				dlgMensaje.add(lblMensaje);
				
				btnSi.addActionListener(this);
				btnNo.addActionListener(this);
				
				btnSi.setBounds(45, 90, 70, 25);
				btnNo.setBounds(155, 90, 70, 25);
				dlgMensaje.add(btnSi);
				dlgMensaje.add(btnNo);
				
				dlgMensaje.setResizable(false);
				dlgMensaje.setLocationRelativeTo(null);
				dlgMensaje.setVisible(true);
			}
		}
		
		//Al pulsar el botón NO.
		else if(e.getSource().equals(btnNo))
		{
			dlgMensaje.setVisible(false);
		}
		
		//Al pulsar el botón SI.
		else if(e.getSource().equals(btnSi))
		{
			//Sacar el idUsuario, con el "split": el dato se guarda en tabla y se separa por el simbolo que se ponga.
			String tabla[] = choClientes.getSelectedItem().split("-");
			
			int respuesta = conexion.eliminarCliente(tabla[0], usuario);
			
			dlgMensajeFinal.setLayout(null);
			dlgMensajeFinal.addWindowListener(this);
			dlgMensajeFinal.setSize(250,100);
			
			//Si sale todo bien.
			if(respuesta==0)
			{
				lblMensajeFinal.setBounds(77, 35, 140, 50);
				lblMensajeFinal.setText("Cliente Eliminado"); 
			}
			//Si no sale bien.
			else
			{	
				lblMensajeFinal.setBounds(65, 35, 140, 50);
				lblMensajeFinal.setText("Error al eliminar");
			}
			
			dlgMensajeFinal.add(lblMensajeFinal); //Se añade después de poner el mensaje.
			dlgMensajeFinal.setResizable(false);
			dlgMensajeFinal.setLocationRelativeTo(null);
			dlgMensajeFinal.setVisible(true);
		}
	}
}
