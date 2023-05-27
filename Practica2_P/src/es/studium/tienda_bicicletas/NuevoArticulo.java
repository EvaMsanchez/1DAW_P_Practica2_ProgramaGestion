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

public class NuevoArticulo implements WindowListener, ActionListener
{
	Frame ventanaNuevoArticulo= new Frame("Nuevo Artículo");
	Dialog dlgMensaje = new Dialog(ventanaNuevoArticulo, "Mensaje", true); //"True": porque es modal.

	Label lblTitulo = new Label("-------- Alta de Artículo --------");
	Label lblDescripcion = new Label("Descripción:");
	Label lblMarca = new Label("Marca:");
	Label lblPrecio = new Label("Precio:");
	//Componente del diálogo.
	Label lblMensaje = new Label("Alta Correcta");

	TextField txtDescripcion = new TextField(12);
	TextField txtMarca = new TextField(13);
	TextField txtPrecio = new TextField(12);

	Button btnAceptar = new Button("Aceptar");
	Button btnCancelar = new Button("Cancelar");
	
	//Objeto conexión para utilizar el método.
	Conexion conexion = new Conexion();
	
	String usuario;

	NuevoArticulo(String u)
	{
		usuario = u;
		
		//Distribución.
		ventanaNuevoArticulo.setLayout(null);
		//Se añade para darle funcionalidad a la ventana.
		ventanaNuevoArticulo.addWindowListener(this);

		ventanaNuevoArticulo.setBackground(Color.LIGHT_GRAY);
		lblTitulo.setBounds(92, 30, 150, 50);
		lblDescripcion.setBounds(40, 70, 67, 50);
		txtDescripcion.setBounds(120, 85, 170, 25);
		
		lblMarca.setBounds(40, 120, 50, 50);
		txtMarca.setBounds(120, 135, 170, 25);
		
		lblPrecio.setBounds(40, 170, 50, 50);
		txtPrecio.setBounds(120, 185, 170, 25);
		
		ventanaNuevoArticulo.add(lblTitulo);
		ventanaNuevoArticulo.add(lblDescripcion);
		ventanaNuevoArticulo.add(txtDescripcion);
		ventanaNuevoArticulo.add(lblMarca);
		ventanaNuevoArticulo.add(txtMarca);
		ventanaNuevoArticulo.add(lblPrecio);
		ventanaNuevoArticulo.add(txtPrecio);
	
		//Se añade para darle funcionalidad al botón.
		btnAceptar.addActionListener(this);
		btnAceptar.setBounds(55, 240, 90, 30);
		ventanaNuevoArticulo.add(btnAceptar);
		btnCancelar.addActionListener(this);
		btnCancelar.setBounds(190, 240, 90, 30);
		ventanaNuevoArticulo.add(btnCancelar);

		ventanaNuevoArticulo.setSize(334,305);
		ventanaNuevoArticulo.setResizable(false);
		ventanaNuevoArticulo.setLocationRelativeTo(null);
		ventanaNuevoArticulo.setVisible(true);
	}


	//Interfaces.
	@Override
	//Aquí funciones de los botones.
	public void actionPerformed(ActionEvent e)
	{
		//Nuevo Artículo.
		//Al pulsar el botón ACEPTAR.
		if(e.getSource().equals(btnAceptar)) //Donde se produce el evento es igual al botón Aceptar.
		{
			dlgMensaje.setLayout(null);
			dlgMensaje.addWindowListener(this);
			dlgMensaje.setSize(250,100);
			
			if(txtDescripcion.getText().length()==0||txtMarca.getText().length()==0||txtPrecio.getText().length()==0) //"||": operador or -- "o".
			{
				lblMensaje.setBounds(56, 35, 140, 50);
				lblMensaje.setText("Los campos están vacíos");
			}
			
			else if(!txtPrecio.getText().matches("^[0-9]{1,5}(.[0-9]{1,2})?"))
			{
				lblMensaje.setBounds(59, 35, 150, 50);
				lblMensaje.setText("Formato precio no válido");
			}
			
			else if(txtPrecio.getText().matches("^[0-9]{1,5},[0-9]{1,2}"))
			{
				lblMensaje.setBounds(50, 35, 150, 50);
				lblMensaje.setText("Formato precio no válido ' . '");
			}
			
			else
			{
				//Dar de alta.
				String descripcion = txtDescripcion.getText();
				String marca = txtMarca.getText();
				String precio = txtPrecio.getText();
								
				//Otro método de la clase conexión.
				int respuesta = conexion.altaArticulo(descripcion, marca, precio, usuario); //Le pasamos nombre "usuario" para archivo .LOG

				if(respuesta!=0)
				{
					//Mostrar mensaje error.
					lblMensaje.setBounds(88, 35, 145, 50);
					lblMensaje.setText("Error en Alta");
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
			txtDescripcion.setText("");
			txtMarca.setText("");
			txtPrecio.setText("");
			txtDescripcion.requestFocus(); //Para que el cursor aparezca en el nombre.
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
			ventanaNuevoArticulo.setVisible(false);
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
