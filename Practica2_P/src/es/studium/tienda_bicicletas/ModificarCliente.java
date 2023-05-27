package es.studium.tienda_bicicletas;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ModificarCliente implements WindowListener, ActionListener
{
	//Ventana elegir cliente a editar en desplegable.
	Frame ventanaElegirCliente = new Frame("Editar Cliente");
	Label lblElegir = new Label("Elegir Cliente a Editar:");
	Choice choClientes = new Choice(); //Lista desplegable.
	
	Button btnEditar= new Button("Editar");
	
	
	//Ventana modificar.
	Frame ventanaModificarCliente = new Frame("Editar");

	Label lblTitulo = new Label("-------- Editar Cliente --------");
	Label lblNombre = new Label("Nombre:");
	Label lblDni = new Label("DNI/NIF:");
	Label lblTelefono = new Label("Teléfono:");

	TextField txtNombre = new TextField(12);
	TextField txtDni = new TextField(13);
	TextField txtTelefono = new TextField(12);

	Button btnModificar = new Button("Modificar");
	Button btnCancelar = new Button("Cancelar");
	
	
	//Diálogo.
	Dialog dlgMensaje = new Dialog(ventanaModificarCliente, "Mensaje", true); //"true": porque es modal.
	Label lblMensaje= new Label("Cliente Modificado");

	//Objeto conexión para utilizar el método.
	Conexion conexion = new Conexion();

	//Para que se vea en todo el método.
	String idCliente = "";
	
	String usuario;
	
	ModificarCliente(String u)
	{
		usuario = u;
		
		//Listener en el constructor para que luego no muestre el diálogo tantas veces repetidas.
		ventanaModificarCliente.addWindowListener(this);
		btnModificar.addActionListener(this);
		btnCancelar.addActionListener(this);
		dlgMensaje.addWindowListener(this);
		
		//Distribución.
		ventanaElegirCliente.setLayout(null);
		//Se añade para darle funcionalidad a la ventana.
		ventanaElegirCliente.addWindowListener(this);
		
		ventanaElegirCliente.setBackground(Color.LIGHT_GRAY);
		lblElegir.setBounds(99, 30, 150, 50);
		ventanaElegirCliente.add(lblElegir);
		
		//Rellenar el Choice.
		conexion.rellenarChoiceClientes(choClientes); //Método le mandamos el choice para que lo rellene.
		
		choClientes.setBounds(75, 85, 170, 40);
		ventanaElegirCliente.add(choClientes);
		
		btnEditar.addActionListener(this);
		btnEditar.setBounds(110, 135, 100, 30);
		ventanaElegirCliente.add(btnEditar);

		ventanaElegirCliente.setSize(320,200);
		ventanaElegirCliente.setResizable(false);
		ventanaElegirCliente.setLocationRelativeTo(null);
		ventanaElegirCliente.setVisible(true);
	}

	
	//Interfaces.
	//Aquí funciones de la ventana windows.
	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent e)
	{
		if(ventanaModificarCliente.isActive())
		{
			ventanaModificarCliente.setVisible(false);
			ventanaElegirCliente.setVisible(false);
		}
		else if(dlgMensaje.isActive())
		{
			dlgMensaje.setVisible(false);
		}
		else
		{
			ventanaElegirCliente.setVisible(false);
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
		//Al pulsar el botón EDITAR.
		if(e.getSource().equals(btnEditar)) //Donde se produce el evento es igual al botón Editar.
		{
			if(choClientes.getSelectedIndex()!=0)
			{
				//Distribución.
				ventanaModificarCliente.setLayout(null);
				//Se añade para darle funcionalidad a la ventana.
		
				
				ventanaModificarCliente.setBackground(Color.LIGHT_GRAY);
				lblTitulo.setBounds(87, 30, 150, 50);
				lblNombre.setBounds(40, 70, 50, 50);
				txtNombre.setBounds(105, 85, 170, 25);
				
				lblDni.setBounds(40, 120, 50, 50);
				txtDni.setBounds(105, 135, 170, 25);
				
				lblTelefono.setBounds(40, 170, 50, 50);
				txtTelefono.setBounds(105, 185, 170, 25);

				ventanaModificarCliente.add(lblTitulo);
				ventanaModificarCliente.add(lblNombre);
				ventanaModificarCliente.add(txtNombre);
				ventanaModificarCliente.add(lblDni);
				ventanaModificarCliente.add(txtDni);
				ventanaModificarCliente.add(lblTelefono);
				ventanaModificarCliente.add(txtTelefono);

				//Se añade para darle funcionalidad al botón.
				
				btnModificar.setBounds(48, 240, 90, 30);
				ventanaModificarCliente.add(btnModificar);
			
				btnCancelar.setBounds(185, 240, 90, 30);
				ventanaModificarCliente.add(btnCancelar);

				ventanaModificarCliente.setSize(320,305);
				ventanaModificarCliente.setResizable(false);
				ventanaModificarCliente.setLocationRelativeTo(null);
				
				//Sacar el idUsuario, con el "split": el dato se guarda en tabla y se separa por el simbolo que se ponga.
				String tabla[] = choClientes.getSelectedItem().split("-");
				
				String resultado = conexion.getDatosEdicionCliente(tabla[0]);
				
				String datos[] = resultado.split("-");
				idCliente = datos[0];
				txtNombre.setText(datos[1]);
				txtDni.setText(datos[2]);
				txtTelefono.setText(datos[3]);
				
				ventanaModificarCliente.setVisible(true);
			}
		}
		
		//Al pulsar el botón MODIFICAR.
		else if(e.getSource().equals(btnModificar))
		{
			dlgMensaje.setLayout(null);
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
				// Modificar.
				String nombre = txtNombre.getText();
				String dni = txtDni.getText();
				String telefono = txtTelefono.getText();
								
				//Otro método de la clase conexión.
				int respuesta = conexion.modificarCliente(nombre, dni, telefono, idCliente, usuario); //Le pasamos nombre "usuario" para archivo .LOG
				
				if(respuesta!=0)
				{
					// Mostrar Mensaje Error.
					lblMensaje.setBounds(26, 35, 197, 50);
					lblMensaje.setText("Error en Modificación el dni ya existe");
				}
				
				else
				{
					lblMensaje.setBounds(75, 35, 145, 50);
					lblMensaje.setText("Cliente Modificado");
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
			ventanaModificarCliente.setVisible(false);
		}
	}
}
