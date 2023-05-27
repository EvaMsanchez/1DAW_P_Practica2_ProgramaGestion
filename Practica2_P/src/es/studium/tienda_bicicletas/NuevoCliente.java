package es.studium.tienda_bicicletas;

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

public class NuevoCliente implements WindowListener, ActionListener
{
	Frame ventanaNuevoCliente= new Frame("Nuevo Cliente");
	Dialog dlgMensaje = new Dialog(ventanaNuevoCliente, "Mensaje", true); //"True": porque es modal.

	Label lblTitulo = new Label("-------- Alta de Cliente --------");
	Label lblNombre = new Label("Nombre:");
	Label lblDni = new Label("DNI/NIF:");
	Label lblTelefono = new Label("Teléfono:");
	//Componente del diálogo.
	Label lblMensaje = new Label("Alta Correcta");

	TextField txtNombre = new TextField(12);
	TextField txtDni = new TextField(13);
	TextField txtTelefono = new TextField(12);

	Button btnAceptar = new Button("Aceptar");
	Button btnCancelar = new Button("Cancelar");
	
	//Objeto conexión para utilizar el método.
	Conexion conexion = new Conexion();
	
	String usuario;

	NuevoCliente(String u)
	{
		usuario = u;
		
		//Distribución.
		ventanaNuevoCliente.setLayout(null);
		//Se añade para darle funcionalidad a la ventana.
		ventanaNuevoCliente.addWindowListener(this);

		ventanaNuevoCliente.setBackground(Color.LIGHT_GRAY);
		lblTitulo.setBounds(80, 30, 150, 50);
		lblNombre.setBounds(40, 70, 50, 50);
		txtNombre.setBounds(105, 85, 170, 25);
		
		lblDni.setBounds(40, 120, 50, 50);
		txtDni.setBounds(105, 135, 170, 25);
		
		lblTelefono.setBounds(40, 170, 50, 50);
		txtTelefono.setBounds(105, 185, 170, 25);
		
		ventanaNuevoCliente.add(lblTitulo);
		ventanaNuevoCliente.add(lblNombre);
		ventanaNuevoCliente.add(txtNombre);
		ventanaNuevoCliente.add(lblDni);
		ventanaNuevoCliente.add(txtDni);
		ventanaNuevoCliente.add(lblTelefono);
		ventanaNuevoCliente.add(txtTelefono);
	
		//Se añade para darle funcionalidad al botón.
		btnAceptar.addActionListener(this);
		btnAceptar.setBounds(45, 240, 90, 30);
		ventanaNuevoCliente.add(btnAceptar);
		btnCancelar.addActionListener(this);
		btnCancelar.setBounds(185, 240, 90, 30);
		ventanaNuevoCliente.add(btnCancelar);

		ventanaNuevoCliente.setSize(320,305);
		ventanaNuevoCliente.setResizable(false);
		ventanaNuevoCliente.setLocationRelativeTo(null);
		ventanaNuevoCliente.setVisible(true);
	}


	//Interfaces.
	@Override
	//Aquí funciones de los botones.
	public void actionPerformed(ActionEvent e)
	{
		//Nuevo Cliente.
		//Al pulsar el botón ACEPTAR.
		if(e.getSource().equals(btnAceptar)) //Donde se produce el evento es igual al botón Aceptar.
		{
			dlgMensaje.setLayout(null);
			dlgMensaje.addWindowListener(this);
			dlgMensaje.setSize(250,100);			
			
			if(txtNombre.getText().length()==0||txtDni.getText().length()==0||txtTelefono.getText().length()==0) //"||": operador or -- "o".
			{
				lblMensaje.setBounds(56, 35, 140, 50);
				lblMensaje.setText("Los campos están vacíos");
			}
			
			else if(!txtTelefono.getText().matches("^[0-9]{9}"))
			{
				lblMensaje.setBounds(60, 35, 140, 50);
				lblMensaje.setText("El teléfono es incorrecto");
			}
			
			else if(!txtDni.getText().matches("^[0-9]{8}[a-zA-Z]{1}")&&(!txtDni.getText().matches("^[a-zA-Z]{1}[0-9]{8}")
					&&(!txtDni.getText().matches("^[a-zA-Z]{1}[0-9]{7}[a-zA-Z]{1}"))))
			{
				lblMensaje.setBounds(85, 35, 140, 50);
				lblMensaje.setText("DNI incorrecto");
			}
			
			else
			{
				//Dar de alta.
				String nombre = txtNombre.getText();
				String dni = txtDni.getText();
				String tlf = txtTelefono.getText();
								
				//Otro método de la clase conexión.
				int respuesta = conexion.altaCliente(nombre, dni, tlf, usuario); //Le pasamos nombre "usuario" para archivo .LOG
				
				if(respuesta!=0)
				{
					//Mostrar mensaje error.
					lblMensaje.setBounds(55, 35, 145, 50);
					lblMensaje.setText("Error en Alta dni duplicado");
				}
		
				else
				{
					lblMensaje.setBounds(88, 35, 145, 50);
					lblMensaje.setText("Alta Correcta");
				}
			}
			
			dlgMensaje.add(lblMensaje);
			dlgMensaje.setResizable(false);
			dlgMensaje.setLocationRelativeTo(null);
			dlgMensaje.setVisible(true);
		}

		//Al pulsar el botón CANCELAR.
		else if(e.getSource().equals(btnCancelar))
		{
			//Si se pulsa el botón Cancelar, se borran todos los campos y el cursor aparece en el nombre.
			txtNombre.setText("");
			txtDni.setText("");
			txtTelefono.setText("");
			txtNombre.requestFocus(); //Para que el cursor aparezca en el nombre.
		}
	}


	//Aquí funciones de la ventana windows.
	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent e)
	{
		if(dlgMensaje.isActive())
		{
			dlgMensaje.setVisible(false);
		}
		else
		{
			ventanaNuevoCliente.setVisible(false);
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


