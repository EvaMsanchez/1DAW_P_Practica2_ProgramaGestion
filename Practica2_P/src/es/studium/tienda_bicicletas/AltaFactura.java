package es.studium.tienda_bicicletas;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AltaFactura implements WindowListener, ActionListener
{
	//Ventana elegir cliente alta factura.
	Frame ventanaAltaFactura = new Frame("Alta Factura");
	Label lblFecha = new Label("Fecha:");
	TextField txtFecha = new TextField(12);
	Label lblPago = new Label("Forma de pago:");
	TextField txtPago = new TextField(12);
	Label lblCliente = new Label("Cliente:");
			
	Choice choClientes = new Choice(); //Lista desplegable.
	
	Button btnSiguiente= new Button("Siguiente");
	Button btnCancelar= new Button("Cancelar");
	
	
	//Ventana detalles factura.
	Frame ventanaDetalles = new Frame("Detalles Factura");

	Label lblNfactura = new Label("FACTURA Nº: ");
	TextField txtNumeroF = new TextField(12);
	
	Label lblArticulo = new Label("Artículo:");
	Choice choArticulos = new Choice(); //Lista desplegable.
	Label lblCantidad = new Label("Cantidad:");
	TextField txtCantidad= new TextField(12);
	Button btnAgregar= new Button("Agregar");
	
	Label lblId= new Label("Id");
	Label lblDescripcion = new Label("Descripción");
	Label lblUd = new Label("Ud.");
	Label lblPrecio = new Label("Precio");
	Label lblSubtotal = new Label("Subtotal");
	
	TextArea textArea = new TextArea(""); //"": vacío, alto y ancho.

	Label lblTotal = new Label("TOTAL:");
	TextField txtTotal = new TextField();
	
	Button btnFinalizar = new Button("Finalizar");
	
	//Diálogo.
	Dialog dlgMensaje = new Dialog(ventanaAltaFactura, "Mensaje", true); //"true": porque es modal.
	Label lblMensaje= new Label("");
	Dialog dlgMensaje1 = new Dialog(ventanaDetalles, "Mensaje", true); //"true": porque es modal.
	
	//Objeto conexión para utilizar el método.
	Conexion conexion = new Conexion();

	//Para que se vea en todo el método.
	String idCliente = "";
	
	String usuario;
	
	AltaFactura(String u)
	{
		usuario = u;
		
		//Listener en el constructor para que luego no muestre el diálogo tantas veces repetidas.
		ventanaDetalles.addWindowListener(this);
		btnAgregar.addActionListener(this);
		btnFinalizar.addActionListener(this);
		
		//Listener en el constructor para que luego no muestre el diálogo tantas veces repetidas.
		ventanaAltaFactura.addWindowListener(this);
		dlgMensaje.addWindowListener(this);
		dlgMensaje1.addWindowListener(this);
		
		//Distribución.
		ventanaAltaFactura.setLayout(null);
		
		ventanaAltaFactura.setBackground(Color.LIGHT_GRAY);
		
		lblFecha.setBounds(40, 45, 40, 50);
		txtFecha.setBounds(135, 60, 140, 25);
		lblPago.setBounds(40, 95, 85, 50);
		txtPago.setBounds(135, 110, 140, 25);
		
		ventanaAltaFactura.add(lblFecha);
		ventanaAltaFactura.add(txtFecha);
		ventanaAltaFactura.add(lblPago);
		ventanaAltaFactura.add(txtPago);
		
		lblCliente.setBounds(40, 145, 50, 50);
		ventanaAltaFactura.add(lblCliente);
		
		//Rellenar el choice Clientes.
		conexion.rellenarChoiceClientes(choClientes); //Método le mandamos el choice para que lo rellene.
		
		choClientes.setBounds(135, 160, 140, 50);
		ventanaAltaFactura.add(choClientes);
		
		btnSiguiente.addActionListener(this);
		btnSiguiente.setBounds(45, 225, 90, 30);
		ventanaAltaFactura.add(btnSiguiente);
		
		btnCancelar.addActionListener(this);
		btnCancelar.setBounds(185, 225, 90, 30);
		ventanaAltaFactura.add(btnCancelar);

		ventanaAltaFactura.setSize(320,280);
		ventanaAltaFactura.setResizable(false);
		ventanaAltaFactura.setLocationRelativeTo(null);
		ventanaAltaFactura.setVisible(true);
	}
	
		
	//Interfaces.
	//Aquí funciones de la ventana windows.
	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent e)
	{
		if(ventanaDetalles.isActive())
		{
			ventanaDetalles.setVisible(false);
			ventanaAltaFactura.setVisible(false);
		}
		else if(dlgMensaje.isActive())
		{
			dlgMensaje.setVisible(false);
		}
		else if(dlgMensaje1.isActive())
		{
			dlgMensaje1.setVisible(false);
		}
		else
		{
			ventanaAltaFactura.setVisible(false);
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
		//Al pulsar el botón SIGUIENTE.
		if(e.getSource().equals(btnSiguiente)) //Donde se produce el evento es igual al botón Editar.
		{
			dlgMensaje.setLayout(null);
			dlgMensaje.setSize(250,100);
			
			if(txtFecha.getText().length()==0||txtPago.getText().length()==0||choClientes.getSelectedIndex()==0) //"||": operador or -- "o".
			{
				lblMensaje.setBounds(56, 35, 140, 50);
				lblMensaje.setText("Los campos están vacíos");
				dlgMensaje.add(lblMensaje);
				dlgMensaje.setResizable(false);
				dlgMensaje.setLocationRelativeTo(null);
				dlgMensaje.setVisible(true);
			}
			
			else if(!txtFecha.getText().matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}"))
			{
				lblMensaje.setBounds(50, 35, 145, 50);
				lblMensaje.setText("Formato fecha dd/mm/yyyy");
				dlgMensaje.add(lblMensaje);
				dlgMensaje.setResizable(false);
				dlgMensaje.setLocationRelativeTo(null);
				dlgMensaje.setVisible(true);
			}
			
			else
			{
				//Sacar el idUsuario, con el "split": el dato se guarda en tabla y se separa por el simbolo que se ponga.
				String tabla[] = choClientes.getSelectedItem().split("-");
				
				String pago = txtPago.getText();
				String fecha = txtFecha.getText();
				
				String datos[] = fecha.split("/");
				String dia = datos[0];
				String mes = datos[1];
				String anio = datos[2];
		        
				String fechaAmericana = anio + "/" + mes + "/" + dia;
			
				//Otro método de la clase conexión.
			    int respuesta = conexion.altaFactura(fechaAmericana, pago, tabla[0], usuario);
			
			    if(respuesta!=0)
				{
					//Mostrar mensaje error.
					lblMensaje.setBounds(55, 35, 145, 50);
					lblMensaje.setText("Error en Alta");
					dlgMensaje.add(lblMensaje);
					dlgMensaje.setResizable(false);
					dlgMensaje.setLocationRelativeTo(null);
					dlgMensaje.setVisible(true);
				}
			    
				else
				{					
					ventanaAltaFactura.setVisible(false);
					//Abrir la otra interfaz de Detalles de factura.
					//Distribución.
					ventanaDetalles.setLayout(null);
					
					//Los listeners de los botones y de la ventana en el constructor.
			
					ventanaDetalles.setBackground(Color.LIGHT_GRAY);
					
					lblNfactura.setBounds(125, 35, 80, 50);
					txtNumeroF.setBounds(210, 50, 60, 25);
					
					//Otro método de la clase conexión.
					int ultimaFactura = conexion.obtenerUltimaFactura(); //Obtener último número de factura.
					
					txtNumeroF.setText(ultimaFactura+""); //Número factura en la ventana Detalles.
					txtNumeroF.setEnabled(false);
					lblArticulo.setBounds(90, 70, 50, 50);
					
					ventanaDetalles.add(lblNfactura);
					ventanaDetalles.add(txtNumeroF);
					ventanaDetalles.add(lblArticulo);
					
					//Rellenar el choice Artículos.
					conexion.rellenarChoiceArticulos(choArticulos); //Método le mandamos el choice para que lo rellene.
					
					choArticulos.setBounds(160, 85, 150, 50);
					ventanaDetalles.add(choArticulos);
					
					lblCantidad.setBounds(90, 100, 50, 50);
					txtCantidad.setBounds(160, 115, 45, 25);
					btnAgregar.setBounds(220, 110, 90, 30);
					
					ventanaDetalles.add(lblCantidad);
					ventanaDetalles.add(txtCantidad);
					ventanaDetalles.add(btnAgregar);				
					
					lblId.setBounds(23, 145, 20, 30);
					lblDescripcion.setBounds(70, 145, 90, 30);
					lblUd.setBounds(175, 145, 20, 30);
					lblPrecio.setBounds(215, 145, 60, 30);
					lblSubtotal.setBounds(280, 145, 90, 30);
					
					ventanaDetalles.add(lblId);
					ventanaDetalles.add(lblDescripcion);
					ventanaDetalles.add(lblUd);
					ventanaDetalles.add(lblPrecio);
					ventanaDetalles.add(lblSubtotal);
					
					textArea.setBounds(23, 175, 355, 140);
					ventanaDetalles.add(textArea);
					
					lblTotal.setBounds(220, 315, 50, 50);
					txtTotal.setText("0.0"); 
					txtTotal.setBounds(290, 325, 70, 25);
					txtTotal.setEnabled(false);
					ventanaDetalles.add(lblTotal);
					ventanaDetalles.add(txtTotal);
					
					btnFinalizar.setBounds(23, 345, 90, 30);
					ventanaDetalles.add(btnFinalizar);

					ventanaDetalles.setSize(400,400);
					ventanaDetalles.setResizable(false);
					ventanaDetalles.setLocationRelativeTo(null);
					ventanaDetalles.setVisible(true);
				}
			}
		}
		
		
		//Al pulsar el botón CANCELAR.
		else if(e.getSource().equals(btnCancelar))
		{
			ventanaAltaFactura.setVisible(false);
		}
		
		
		//Al pulsar el botón AGREGAR.
		else if(e.getSource().equals(btnAgregar))
		{
			dlgMensaje1.setLayout(null);
			dlgMensaje1.setSize(250,100);
			
			if(txtCantidad.getText().length()==0||choArticulos.getSelectedIndex()==0) //"||": operador or -- "o".
			{
				lblMensaje.setBounds(56, 35, 140, 50);
				lblMensaje.setText("Los campos están vacíos");
				dlgMensaje1.add(lblMensaje);
				dlgMensaje1.setResizable(false);
				dlgMensaje1.setLocationRelativeTo(null);
				dlgMensaje1.setVisible(true);
			}
			
			else
			{
				int cantidad = Integer.parseInt(txtCantidad.getText());
				int numerof = Integer.parseInt(txtNumeroF.getText());
			
				String tabla[] = choArticulos.getSelectedItem().split("-");
			
				//Otro método de la clase conexión.
				conexion.agregarArticulo(cantidad, numerof, tabla[0], usuario);		
			
				double total = 0.0;
				double cantidadCalculada = 0.0;

				if (textArea.getText().isEmpty())
				{
					textArea.append(tabla[0]+" \t");
					textArea.append(tabla[1]+" \t   ");
					textArea.append(cantidad+" \t");
					textArea.append(tabla[3]+" €\t      ");
					cantidadCalculada = cantidad*Double.parseDouble(tabla[3]);
					textArea.append(cantidadCalculada+" €");
				} 
				else
				{
					textArea.append("\n"+tabla[0]+" \t");
					textArea.append(tabla[1]+" \t   ");
					textArea.append(cantidad+" \t");
					textArea.append(tabla[3]+" €\t      ");
					cantidadCalculada = cantidad*Double.parseDouble(tabla[3]);
					textArea.append(cantidadCalculada+" €");
				}
				
				total = total + cantidadCalculada;
				txtTotal.setText((Double.parseDouble(txtTotal.getText())+total)+"");
			}
		}
		
		
		//Al pulsar el botón FINALIZAR.
		else if(e.getSource().equals(btnFinalizar))
		{
			dlgMensaje1.setLayout(null);
			dlgMensaje1.setSize(250,100);
			
			double importeTotal = (Double.parseDouble(txtTotal.getText()));
			int numerof = Integer.parseInt(txtNumeroF.getText());
			
			//Otro método de la clase conexión.
			int respuesta = conexion.introducirImporteTotal(importeTotal, numerof, usuario);	
			
			if(respuesta!=0)
			{
				//Mostrar mensaje error.
				lblMensaje.setBounds(55, 35, 145, 50);
				lblMensaje.setText("Error en Alta");
			}
			
			else
			{			
				//Mostrar mensaje OK.
				lblMensaje.setBounds(88, 35, 145, 50);
				lblMensaje.setText("Alta Correcta");
				
				ventanaDetalles.setVisible(false);
			}
			
			dlgMensaje1.add(lblMensaje);
			dlgMensaje1.setResizable(false);
			dlgMensaje1.setLocationRelativeTo(null);
			dlgMensaje1.setVisible(true);
		}
	}	
}
