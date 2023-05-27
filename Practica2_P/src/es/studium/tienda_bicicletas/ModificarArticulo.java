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

public class ModificarArticulo implements WindowListener, ActionListener
{
	//Ventana elegir cliente a editar en desplegable.
	Frame ventanaElegirArticulo = new Frame("Editar Articulo");
	Label lblElegir = new Label("Elegir Articulo a Editar:");
	Choice choArticulos = new Choice(); //Lista desplegable.
	
	Button btnEditar= new Button("Editar");
	
	
	//Ventana modificar.
	Frame ventanaModificarArticulo = new Frame("Editar");

	Label lblTitulo = new Label("-------- Editar Articulo --------");
	Label lblDescripcion = new Label("Descripción:");
	Label lblMarca = new Label("Marca:");
	Label lblPrecio = new Label("Precio:");

	TextField txtDescripcion = new TextField(12);
	TextField txtMarca = new TextField(13);
	TextField txtPrecio = new TextField(12);

	Button btnModificar = new Button("Modificar");
	Button btnCancelar = new Button("Cancelar");
	
	
	//Diálogo.
	Dialog dlgMensaje = new Dialog(ventanaModificarArticulo, "Mensaje", true); //"true": porque es modal.
	Label lblMensaje= new Label("Articulo Modificado");

	//Objeto conexión para utilizar el método.
	Conexion conexion = new Conexion();

	//Para que se vea en todo el método.
	String idArticulo = "";
	
	String usuario;
	
	ModificarArticulo(String u)
	{
		usuario = u;
		
		//Listener en el constructor para que luego no muestre el diálogo tantas veces repetidas.
		ventanaModificarArticulo.addWindowListener(this);
		btnModificar.addActionListener(this);
		btnCancelar.addActionListener(this);
		dlgMensaje.addWindowListener(this);
		
		//Distribución.
		ventanaElegirArticulo.setLayout(null);
		//Se añade para darle funcionalidad a la ventana.
		ventanaElegirArticulo.addWindowListener(this);
		
		ventanaElegirArticulo.setBackground(Color.LIGHT_GRAY);
		lblElegir.setBounds(98, 30, 150, 50);
		ventanaElegirArticulo.add(lblElegir);
		
		//Rellenar el Choice.
		conexion.rellenarChoiceArticulos(choArticulos); //Método le mandamos el choice para que lo rellene.
		
		choArticulos.setBounds(75, 85, 170, 40);
		ventanaElegirArticulo.add(choArticulos);
		
		btnEditar.addActionListener(this);
		btnEditar.setBounds(110, 135, 100, 30);
		ventanaElegirArticulo.add(btnEditar);

		ventanaElegirArticulo.setSize(320,200);
		ventanaElegirArticulo.setResizable(false);
		ventanaElegirArticulo.setLocationRelativeTo(null);
		ventanaElegirArticulo.setVisible(true);
	}

	
	//Interfaces.
	//Aquí funciones de la ventana windows.
	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent e)
	{
		if(ventanaModificarArticulo.isActive())
		{
			ventanaModificarArticulo.setVisible(false);
			ventanaElegirArticulo.setVisible(false);
		}
		else if(dlgMensaje.isActive())
		{
			dlgMensaje.setVisible(false);
		}
		else
		{
			ventanaElegirArticulo.setVisible(false);
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
			if(choArticulos.getSelectedIndex()!=0)
			{
				//Distribución.
				ventanaModificarArticulo.setLayout(null);
				//Se añade para darle funcionalidad a la ventana.
				
				ventanaModificarArticulo.setBackground(Color.LIGHT_GRAY);
				lblTitulo.setBounds(85, 30, 150, 50);
				lblDescripcion.setBounds(40, 70, 67, 50);
				txtDescripcion.setBounds(120, 85, 170, 25);
				
				lblMarca.setBounds(40, 120, 50, 50);
				txtMarca.setBounds(120, 135, 170, 25);
				
				lblPrecio.setBounds(40, 170, 50, 50);
				txtPrecio.setBounds(120, 185, 170, 25);
				
				ventanaModificarArticulo.add(lblTitulo);
				ventanaModificarArticulo.add(lblDescripcion);
				ventanaModificarArticulo.add(txtDescripcion);
				ventanaModificarArticulo.add(lblMarca);
				ventanaModificarArticulo.add(txtMarca);
				ventanaModificarArticulo.add(lblPrecio);
				ventanaModificarArticulo.add(txtPrecio);

				//Se añade para darle funcionalidad al botón.
				btnModificar.setBounds(55, 240, 90, 30);
				ventanaModificarArticulo.add(btnModificar);
				btnCancelar.setBounds(190, 240, 90, 30);
				ventanaModificarArticulo.add(btnCancelar);

				ventanaModificarArticulo.setSize(334,305);
				ventanaModificarArticulo.setResizable(false);
				ventanaModificarArticulo.setLocationRelativeTo(null);
				
				//Sacar el idUsuario, con el "split": el dato se guarda en tabla y se separa por el simbolo que se ponga.
				String tabla[] = choArticulos.getSelectedItem().split("-");
				
				String resultado = conexion.getDatosEdicionArticulo(tabla[0]);
				
				String datos[] = resultado.split("-");
				idArticulo = datos[0];
				txtDescripcion.setText(datos[1]);
				txtMarca.setText(datos[2]);
				txtPrecio.setText(datos[3]);
				
				ventanaModificarArticulo.setVisible(true);
			}
		}
		
		//Al pulsar el botón MODIFICAR.
		else if(e.getSource().equals(btnModificar))
		{
			dlgMensaje.setLayout(null);
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
				// Modificar.
				String descripcion = txtDescripcion.getText();
				String marca = txtMarca.getText();
				String precio = txtPrecio.getText();
								
				//Otro método de la clase conexión.
				int respuesta = conexion.modificarArticulo(descripcion, marca, precio, idArticulo, usuario); //Le pasamos nombre "usuario" para archivo .LOG
				
				if(respuesta!=0)
				{
					// Mostrar Mensaje Error.
					lblMensaje.setBounds(77, 35, 145, 50);
					lblMensaje.setText("Error Modificación");
				}
				else
				{
					lblMensaje.setBounds(74, 35, 145, 50);
					lblMensaje.setText("Artículo Modificado");
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
			ventanaModificarArticulo.setVisible(false);
		}
	}
}
