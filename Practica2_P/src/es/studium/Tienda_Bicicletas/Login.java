package es.studium.Tienda_Bicicletas;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Login implements WindowListener, ActionListener
{
	Frame ventanaLogin = new Frame("Login");
	Dialog dlgMensaje = new Dialog(ventanaLogin, "Error", true); //"True": porque es modal.
	
	Label lblUsuario = new Label("Usuario:");
	Label lblClave = new Label("Clave:");
	Label lblMensaje = new Label("Credenciales incorrectas");
	
	TextField txtUsuario = new TextField(10);
	TextField txtClave = new TextField(11);
	
	Button btnAcceder = new Button("Acceder");
	
	//Objeto conexión para utilizar el método "comprobarCredenciales".
	Conexion conexion = new Conexion();
	
	int perfilUsuario;
	
	Login()
	{
		//Se añade para darle funcionalidad a la ventana.
		ventanaLogin.addWindowListener(this);
		//Distribución.
		ventanaLogin.setLayout(null);
		
		ventanaLogin.setBackground(Color.LIGHT_GRAY);
		lblUsuario.setBounds(40, 50, 50, 50);
		txtUsuario.setBounds(105, 65, 160, 25);
		lblClave.setBounds(40, 100, 50, 50);
		txtClave.setBounds(105, 115, 160, 25);
		
		ventanaLogin.add(lblUsuario);
		ventanaLogin.add(txtUsuario);
		ventanaLogin.add(lblClave);
		txtClave.setEchoChar('*'); //Para ocultar la contraseña y salga como "*".
		ventanaLogin.add(txtClave);
		
		//Se añade para darle funcionalidad al botón.
		btnAcceder.addActionListener(this);
		btnAcceder.setBounds(105, 170, 100, 30);
		ventanaLogin.add(btnAcceder);
		
		ventanaLogin.setSize(305,235);
		ventanaLogin.setResizable(false);
		ventanaLogin.setLocationRelativeTo(null);
		ventanaLogin.setVisible(true);
	}
	
	
	//Main.
	public static void main(String[] args)
	{
		new Login();
	}

	
	//Interfaces.
	@Override
	//Aquí funciones de los botones.
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(btnAcceder)) //Donde se produce el evento es igual al botón Acceder.
		{
			String usuario = txtUsuario.getText();
			String clave = txtClave.getText();
			
			//Credenciales correctas.
			perfilUsuario = conexion.comprobarCredenciales(usuario, clave);
			if(perfilUsuario!=-1)
			{
				new MenuPrincipal(perfilUsuario);
				ventanaLogin.setVisible(false);
			}
			
			//Credenciales incorrectas
			else
			{
				//Para que aparezca el diálogo.
				dlgMensaje.setLayout(null);
				dlgMensaje.addWindowListener(this);
				
				lblMensaje.setBounds(58, 35, 140, 50);
				dlgMensaje.add(lblMensaje);
				dlgMensaje.setSize(250,100);
				dlgMensaje.setResizable(false);
				dlgMensaje.setLocationRelativeTo(null);
				dlgMensaje.setVisible(true);
			}
		}
	}
			
	
	//Aquí funciones de la ventana window.
	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent ee)
	{
		if(dlgMensaje.isActive())
		{
			dlgMensaje.setVisible(false);
		}
		else
		{
			System.exit(0); //Para cerrar la ventana con la "X". 
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
}
