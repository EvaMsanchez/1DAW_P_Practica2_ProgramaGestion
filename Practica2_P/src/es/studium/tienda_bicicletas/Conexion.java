package es.studium.tienda_bicicletas;

import java.awt.Choice;
import java.awt.Desktop;
import java.awt.TextArea;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

public class Conexion
{
	//En esta clase todo lo relacionado a la conexión con la base de datos.
	
	//Constructor para luego crear objetos de esta clase.
	//Para conectar JAVA con MYSQL.
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/tienda_bicicletas_PG"; //Aquí nombre de la base de datos.
	String login = "eva";
	String password = "eva";
	String sentencia = ""; //Vacío para que sirva para otros elementos luego, se ve en toda la clase.
		
	Connection connection = null; //Objeto "connection", para conectarnos a la base de datos.
	Statement statement = null; //Objeto, permite ejecutar sentencias SQL.
	ResultSet rs = null; //Objeto, para guardar toda la información que nos devuelve la base de datos.
	
	//Variable usuario para coger el valor del usuario de comprobar credenciales.
	String usuario;
	
	
	Conexion()
	{
		connection = this.conectar();
	}
	
	
	//Método CONECTAR.
	public Connection conectar()
	{
		//Aquí la conexión con la base de datos.
		try
		{
			// Cargar los controladores para el acceso a la BD.
			Class.forName(driver);
			
			//Aquí se conectan.
			// Establecer la conexión con la BD Empresa.
			return(DriverManager.getConnection(url, login, password)); //Devolver un objeto de la clase conexión.
		}
		
		catch (ClassNotFoundException cnfe) 
		{
			System.out.println("Error 1-"+cnfe.getMessage());
		}
		
		catch (SQLException sqle) 
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		
		return null; //Si no conseguimos conectarnos, no devuelve una conexión nula, no se conecta.
	}
	
	
	//Método COMPROBAR CREDENCIALES.
	public int comprobarCredenciales(String u, String c)
	{
		usuario = u; //Para guardar el usuario que se haya conectado y utilizarlo en la función "apunteLog".
		
		sentencia = "SELECT*FROM usuarios WHERE nombreUsuario = '"+u+"' AND claveUsuario = SHA2('"+c+"',256);";  //"SHA2": algoritmo de encriptación.
		
		//Apunte LOG
		guardarLog(usuario, sentencia);
		
		//Hacer la consulta SELECT pero desde JAVA. 
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY); //READ_ONLY: no permite actualización del contenido.
					
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL.
			rs = statement.executeQuery(sentencia); //Donde está toda la información "rs".
			
			if(rs.next())
			{
				return rs.getInt("perfilUsuario"); //Devuelve el perfil si es 0 o 1.
			}
			else
			{
				return -1;
			}
		}
		
		catch (SQLException sqle) 
		{
			System.out.println("Error 3-"+sqle.getMessage());
		}
		
		return -1; //Por si ha habido algún otro error que no son los dos anteriores, sale -1 que sería también error.
	}

	
	//Método ALTA CLIENTE.
	public int altaCliente(String n, String dni, String tlf, String usuario)
	{
		sentencia = "INSERT INTO clientes VALUES (null, '"+n+"', '"+dni+"', '"+tlf+"');";
		
		//Apunte LOG
		guardarLog(usuario, sentencia);
		
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY); //READ_ONLY: no permite actualización del contenido.
							
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL.
			statement.executeUpdate (sentencia); //rs se quita porque no devuelve nada, es un alta, se utiliza "executeUpdate".
			return 0;
		}
	
		catch (SQLException sqle) 
		{
			System.out.println("Error 4-"+sqle.getMessage());
		}
		return 1;
	}
	

	//Método rellenar LISTADO CLIENTES.
	public void rellenarListadoClientes(TextArea areaTexto, String usuario) //TextArea porque es el tipo de areaTexto.
	{
		sentencia = "SELECT idCliente, nombreCliente, dniCliente, telefonoCliente FROM clientes;";
		
		//Apunte LOG
		guardarLog(usuario, sentencia);
		
		//Hacer la consulta SELECT pero desde JAVA. 
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
					
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL.
			
			rs = statement.executeQuery(sentencia); //donde está toda la información "rs".
			
			//Rellena el TextArea, "append": para añadir.
			while(rs.next())
			{
				areaTexto.append(" "+rs.getString("idCliente")+" \t");
				areaTexto.append(rs.getString("nombreCliente")+" \t\t   ");
				areaTexto.append(rs.getString("dniCliente")+" \t");
				areaTexto.append(rs.getString("telefonoCliente")+"\n");
			}
		}
		
		catch (SQLException sqle) 
		{
			System.out.println("Error 5-"+sqle.getMessage());
		}
	}
	
	
	//Método generar listado Clientes en ARCHIVO PDF.
	public void generarPdfClientes() 
	{
		String dest = "listadoClientes.pdf"; //nombre del archivo de destino.
		
		sentencia = "SELECT idCliente, nombreCliente, dniCliente, telefonoCliente FROM clientes;";
			
		try
		{
			//Initialize PDF writer
			PdfWriter writer = new PdfWriter(dest);
			
			//Initialize PDF document
			PdfDocument pdf = new PdfDocument(writer);
			
			// Initialize document
			Document document = new Document(pdf, PageSize.A4); //Está puesto en vertical. rotate (horizontal)
			
			document.setMargins(20, 20, 20, 20); //Márgenes.
			
			PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA); //tipo de letra.
			//PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD); //letra en negrita.
			
			//Tabla. Los números son los anchos de las columnas. 4 columnas en total.
			Table table = new Table(UnitValue.createPercentArray(new float[]{1, 4, 3, 3})).useAllAvailableWidth(); //esto es para todo el ancho disponible de la página.
			
			try
			{
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
						
				// Crear un objeto ResultSet para guardar lo obtenido
				// y ejecutar la sentencia SQL.
					
				rs = statement.executeQuery(sentencia); //donde está toda la información "rs".
				
				processClientes(table, rs, font);
			
				document.add(table);
			}
			catch (SQLException sqle) 
			{
				System.out.println("Error 16-"+sqle.getMessage());
			}
			
			//Close document
			document.close();
			
			// Open the new PDF document just created
			Desktop.getDesktop().open(new File(dest));
		}
		
		catch(IOException ioe) {}
	}
	

	public void processClientes(Table table, ResultSet rs, PdfFont font) throws SQLException 
	{
		table.addHeaderCell(new Cell().add(new Paragraph("ID").setFont(font).setBold()));
		table.addHeaderCell(new Cell().add(new Paragraph("NOMBRE").setFont(font).setBold()));
		table.addHeaderCell(new Cell().add(new Paragraph("DNI").setFont(font).setBold()));
		table.addHeaderCell(new Cell().add(new Paragraph("TELÉFONO").setFont(font).setBold()));
		
		while (rs.next()) 
		{
			/*if (isHeader) // Celdas de encabezado.
			{
				table.addHeaderCell(new Cell().add(new Paragraph(rs.getString("idCliente")).setFont(font)));
				table.addHeaderCell(new Cell().add(new Paragraph(rs.getString("nombreCliente")).setFont(font)));
				table.addHeaderCell(new Cell().add(new Paragraph(rs.getString("dniCliente")).setFont(font)));
				table.addHeaderCell(new Cell().add(new Paragraph(rs.getString("telefonoCliente")).setFont(font)));
			} 
			else*/
			table.addCell(new Cell().add(new Paragraph(rs.getString("idCliente")).setFont(font)));
			table.addCell(new Cell().add(new Paragraph(rs.getString("nombreCliente")).setFont(font)));
			table.addCell(new Cell().add(new Paragraph(rs.getString("dniCliente")).setFont(font)));
			table.addCell(new Cell().add(new Paragraph(rs.getString("telefonoCliente")).setFont(font)));
		}
	}
	
	
	//Método rellenar choice ELIMINAR CLIENTE.
	public void rellenarChoiceClientes(Choice choClientes)
	{
		sentencia = "SELECT idCliente, dniCliente FROM clientes ORDER BY 1;"; //Sentencia.
		
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL.
			rs = statement.executeQuery(sentencia);
			
			choClientes.add("Elegir cliente..."); //Para que aparezca como primera opción en la lista.
			while(rs.next())
			{
				choClientes.add(rs.getString("idCliente")+"-"+rs.getString("dniCliente"));
			}
		}
		
		catch (SQLException sqle)
		{
			System.out.println("Error 6-"+sqle.getMessage());
		}
	}


	//Método ELIMINAR CLIENTE.
	public int eliminarCliente(String idCliente, String usuario)
	{
		sentencia = "DELETE FROM clientes WHERE idCliente=" + idCliente; //Sentencia.
		
		//Apunte LOG
		guardarLog(usuario, sentencia);
		
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL.
			statement.executeUpdate(sentencia);
			return 0;
		}
	
		catch (SQLException sqle) 
		{
			System.out.println("Error 7-"+sqle.getMessage());
			return 1;
		}
	}
	
	
	//Método para OBTENER LOS DATOS DEL CLIENTE SELECCIONADO (aparezca la ventana modificar rellena).
	public String getDatosEdicionCliente(String idCliente)
	{
		String resultado = "";
		sentencia = "SELECT * FROM clientes WHERE idCliente = " + idCliente;
		
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL.
			rs = statement.executeQuery(sentencia);
			rs.next();
			resultado =(rs.getString("idCliente")+"-"+rs.getString("nombreCliente")+"-"+
					rs.getString("dniCliente")+"-"+rs.getString("telefonoCliente"));
		}
		
		catch (SQLException sqle)
		{
			System.out.println("Error 8-"+sqle.getMessage());
		}
			return resultado;
	}

		
	//Método MODIFICAR CLIENTE.
	public int modificarCliente(String nombre, String dni, String telefono, String idCliente, String usuario)
	{
		sentencia = "UPDATE clientes SET nombreCliente ='"+nombre+"', dniCliente ='"+dni
				+"', telefonoCliente = '"+telefono+"' WHERE idCliente="+idCliente+";";
		
		//Apunte LOG
		guardarLog(usuario, sentencia);
		
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL.
			statement.executeUpdate(sentencia);
			return 0;
		}
			
		catch (SQLException sqle)
		{
			System.out.println("Error 9-"+sqle.getMessage());
			return 1;
		}
	}


	//Método ALTA ARTICULO.
	public int altaArticulo(String desc, String marca, String precio, String usuario)
	{
		sentencia = "INSERT INTO articulos VALUES (null, '"+desc+"', '"+marca+"', '"+precio+"');";
		
		//Apunte LOG
		guardarLog(usuario, sentencia);
		
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
							
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL.
			statement.executeUpdate (sentencia); //Se quita "rs" porque no devuelve nada, es un alta, se utiliza "executeUpdate".
			return 0;
		}
	
		catch (SQLException sqle) 
		{
			System.out.println("Error 10-"+sqle.getMessage());
			return 1;
		}
	}
	
	
	//Método rellenar LISTADO ARTICULOS.
	public void rellenarListadoArticulos(TextArea areaTexto, String usuario) //TextArea porque es el tipo de areaTexto.
	{
		sentencia = "SELECT idArticulo, descripcionArticulo, marcaArticulo, precioArticulo FROM articulos;";
		
		//Apunte LOG
		guardarLog(usuario, sentencia);
		
		//Hacer la consulta SELECT pero desde JAVA. 
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
					
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL.
			
			rs = statement.executeQuery(sentencia); //Donde está toda la información "rs".
			
			//Rellena el TextArea, "append": para añadir.
			while(rs.next())
			{
				areaTexto.append(" "+rs.getString("idArticulo")+" \t");
				areaTexto.append(rs.getString("descripcionArticulo")+" \t\t ");
				areaTexto.append(rs.getString("marcaArticulo")+" \t         ");
				areaTexto.append(rs.getDouble("precioArticulo")+" €\n"); 
			}
		}
		
		catch (SQLException sqle) 
		{
			System.out.println("Error 11-"+sqle.getMessage());
		}
	}
	
	
	//Método generar listado Artículos en ARCHIVO PDF.
	public void generarPdfArticulos() 
	{
		String dest = "listadoArticulos.pdf"; //nombre del archivo de destino.
		
		sentencia = "SELECT idArticulo, descripcionArticulo, marcaArticulo, precioArticulo FROM articulos;";
			
		try
		{
			//Initialize PDF writer
			PdfWriter writer = new PdfWriter(dest);
			
			//Initialize PDF document
			PdfDocument pdf = new PdfDocument(writer);
			
			// Initialize document
			Document document = new Document(pdf, PageSize.A4); //Está puesto en vertical. rotate (horizontal)
			
			document.setMargins(20, 20, 20, 20); //Márgenes.
			
			PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA); //tipo de letra.
			//PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD); //letra en negrita.
			
			//Tabla. Los números son los anchos de las columnas. 4 columnas en total.
			Table table = new Table(UnitValue.createPercentArray(new float[]{1, 5, 3, 2})).useAllAvailableWidth(); //esto es para todo el ancho disponible de la página.
			
			try
			{
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
						
				// Crear un objeto ResultSet para guardar lo obtenido
				// y ejecutar la sentencia SQL.
					
				rs = statement.executeQuery(sentencia); //donde está toda la información "rs".
				
				processArticulos(table, rs, font);
			
				document.add(table);
			}
			catch (SQLException sqle) 
			{
				System.out.println("Error 16-"+sqle.getMessage());
			}
			
			//Close document
			document.close();
			
			// Open the new PDF document just created
			Desktop.getDesktop().open(new File(dest));
		}
		
		catch(IOException ioe) {}
	}
	

	public void processArticulos(Table table, ResultSet rs, PdfFont font) throws SQLException 
	{
		table.addHeaderCell(new Cell().add(new Paragraph("ID").setFont(font).setBold()));
		table.addHeaderCell(new Cell().add(new Paragraph("DESCRIPCIÓN").setFont(font).setBold()));
		table.addHeaderCell(new Cell().add(new Paragraph("MARCA").setFont(font).setBold()));
		table.addHeaderCell(new Cell().add(new Paragraph("PRECIO").setFont(font).setBold()));
		
		while (rs.next()) 
		{
			/*if (isHeader) // Celdas de encabezado.
			{
				table.addHeaderCell(new Cell().add(new Paragraph(rs.getString("idCliente")).setFont(font)));
				table.addHeaderCell(new Cell().add(new Paragraph(rs.getString("nombreCliente")).setFont(font)));
				table.addHeaderCell(new Cell().add(new Paragraph(rs.getString("dniCliente")).setFont(font)));
				table.addHeaderCell(new Cell().add(new Paragraph(rs.getString("telefonoCliente")).setFont(font)));
			} 
			else*/
			table.addCell(new Cell().add(new Paragraph(rs.getString("idArticulo")).setFont(font)));
			table.addCell(new Cell().add(new Paragraph(rs.getString("descripcionArticulo")).setFont(font)));
			table.addCell(new Cell().add(new Paragraph(rs.getString("marcaArticulo")).setFont(font)));
			table.addCell(new Cell().add(new Paragraph(rs.getString("precioArticulo")).setFont(font)));
		}
	}
	
	
	//Método rellenar choice ELIMINAR ARTICULO.
	public void rellenarChoiceArticulos(Choice choArticulos)
	{
		sentencia = "SELECT * FROM articulos ORDER BY 1;"; //Sentencia.
		
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL.
			rs = statement.executeQuery(sentencia);
			
			choArticulos.add("Elegir artículo..."); //Para que aparezca como primera opción en la lista.
			while(rs.next())
			{
				choArticulos.add(rs.getString("idArticulo")+"-"+rs.getString("descripcionArticulo")+"-"+rs.getString("marcaArticulo")+"-"+rs.getString("precioArticulo"));
			}
		}
		
		catch (SQLException sqle)
		{
			System.out.println("Error 12-"+sqle.getMessage());
		}
	}


	//Método ELIMINAR ARTICULO.
	public int eliminarArticulo(String idArticulo, String usuario)
	{
		sentencia = "DELETE FROM articulos WHERE idArticulo=" + idArticulo; //Sentencia.
		
		//Apunte LOG
		guardarLog(usuario, sentencia);
		
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL
			statement.executeUpdate(sentencia);
			return 0;
		}
	
		catch (SQLException sqle) 
		{
			System.out.println("Error 13-"+sqle.getMessage());
			return 1;
		}
	}
	
	
	//Método para OBTENER LOS DATOS DEL ARTICULO SELECCIONADO (aparezca la ventana modificar rellena).
	public String getDatosEdicionArticulo(String idArticulo)
	{
		String resultado = "";
		sentencia = "SELECT * FROM articulos WHERE idArticulo = " + idArticulo;
		
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL.
			rs = statement.executeQuery(sentencia);
			rs.next();
			resultado =(rs.getString("idArticulo")+"-"+rs.getString("descripcionArticulo")+"-"+rs.getString("marcaArticulo")+"-"
					+rs.getString("precioArticulo"));
		}
		
		catch (SQLException sqle)
		{
			System.out.println("Error 14-"+sqle.getMessage());
		}
			return resultado;
	}

		
	//Método MODIFICAR ARTICULO.
	public int modificarArticulo(String desc, String marca, String precio, String idArticulo, String usuario)
	{
		sentencia = "UPDATE articulos SET descripcionArticulo ='"+desc+"', marcaArticulo ='"+marca
				+"', precioArticulo = '"+precio+"' WHERE idArticulo="+idArticulo+";";
		
		//Apunte LOG
		guardarLog(usuario, sentencia);
		
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL.
			statement.executeUpdate(sentencia);
			return 0;
		}
			
		catch (SQLException sqle)
		{
			System.out.println("Error 15-"+sqle.getMessage());
			return 1;
		}
	}
	
	
	//Método ALTA FACTURA.
	public int altaFactura(String fecha, String fpago, String cliente, String usuario)
	{
		sentencia = "INSERT INTO facturas VALUES (null, '"+fecha+"', '"+fpago+"', null, '"+cliente+"');";
			
		//Apunte LOG
		guardarLog(usuario, sentencia);
			
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
								
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL.
			statement.executeUpdate (sentencia); //Se quita "rs" porque no devuelve nada, es un alta, se utiliza "executeUpdate".
			return 0;
		}
		
		catch (SQLException sqle) 
		{
			System.out.println("Error 17-"+sqle.getMessage());
			return 1;
		}
	}
	
	
	//Método ULTIMA FACTURA.
	public int obtenerUltimaFactura()
	{
		int resultado = 0;
		
		sentencia = "SELECT max(idFactura) AS idFactura FROM facturas;";
		
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL.
			rs = statement.executeQuery(sentencia);
			
			if (rs.next())
			{
	            resultado = rs.getInt("idFactura");
	        }
		}
		
		catch (SQLException sqle)
		{
			System.out.println("Error 18-"+sqle.getMessage());
		}
		
		return resultado;	
	}
	
	
	//Método AGREGAR ARTICULO.
	public int agregarArticulo(int cantidad, int numerof, String articulo, String usuario)
	{
		sentencia = "INSERT INTO historico VALUES (null, "+cantidad+", "+numerof+", "+articulo+");";
			
		//Apunte LOG
		//guardarLog(usuario, sentencia);
			
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
							
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL.
			statement.executeUpdate (sentencia); //Se quita "rs" porque no devuelve nada, es un alta, se utiliza "executeUpdate".
			
			return 0;
		}
			
		catch (SQLException sqle) 
		{
			System.out.println("Error 19-"+sqle.getMessage());
			return 1;
		}
	}
	
	
	//Método INTRODUCIR IMPORTE TOTAL FACTURA.
	public int introducirImporteTotal(double importeTotal, int numerof, String usuario)
	{
		sentencia = "UPDATE facturas SET importeTotalFactura ='"+importeTotal+"' WHERE idFactura="+numerof+";";
			
		//Apunte LOG
		guardarLog(usuario, sentencia);
			
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
				
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL.
			statement.executeUpdate(sentencia);
			return 0;
		}
			
		catch (SQLException sqle)
		{
			System.out.println("Error 22-"+sqle.getMessage());
			return 1;
		}
	}
	
	
	//Método rellenar LISTADO FACTURAS.
	public void rellenarListadoFacturas(TextArea areaTexto, String usuario) //TextArea porque es el tipo de areaTexto.
	{
		sentencia = "SELECT idFactura, DATE_FORMAT(fechaFactura, '%d/%m/%Y') AS fechaFactura, formaPagoFactura, importeTotalFactura, nombreCliente FROM facturas INNER JOIN clientes ON facturas.idClienteFK = clientes.idCliente;";
			
		//Apunte LOG
		guardarLog(usuario, sentencia);
			
		//Hacer la consulta SELECT pero desde JAVA. 
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
						
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL.
				
			rs = statement.executeQuery(sentencia); //Donde está toda la información "rs".
				
			//Rellena el TextArea, "append": para añadir.
			while(rs.next())
			{
				areaTexto.append(" "+rs.getString("idFactura")+" \t");
				areaTexto.append(rs.getString("fechaFactura")+" \t ");
				areaTexto.append(rs.getString("formaPagoFactura")+"\t\t");
				areaTexto.append(rs.getDouble("importeTotalFactura")+" €\t\t"); 
				areaTexto.append(rs.getString("nombreCliente")+" \n");
			}
		}
			
		catch (SQLException sqle) 
		{
			System.out.println("Error 20-"+sqle.getMessage());
		}
	}
	
	
	//Método generar listado Facturas en ARCHIVO PDF.
	public void generarPdfFacturas() 
	{
		String dest = "listadoFacturas.pdf"; //nombre del archivo de destino.
		
		sentencia = "SELECT idFactura, DATE_FORMAT(fechaFactura, '%d/%m/%Y') AS fechaFactura, formaPagoFactura, importeTotalFactura, nombreCliente FROM facturas INNER JOIN clientes ON facturas.idClienteFK = clientes.idCliente;";
			
		try
		{
			//Initialize PDF writer
			PdfWriter writer = new PdfWriter(dest);
			
			//Initialize PDF document
			PdfDocument pdf = new PdfDocument(writer);
			
			// Initialize document
			Document document = new Document(pdf, PageSize.A4); //Está puesto en vertical. rotate (horizontal)
			
			document.setMargins(20, 20, 20, 20); //Márgenes.
			
			PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA); //tipo de letra.
			//PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD); //letra en negrita.
			
			//Tabla. Los números son los anchos de las columnas. 4 columnas en total.
			Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 3, 3, 3})).useAllAvailableWidth(); //esto es para todo el ancho disponible de la página.
			
			try
			{
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
						
				// Crear un objeto ResultSet para guardar lo obtenido
				// y ejecutar la sentencia SQL.
					
				rs = statement.executeQuery(sentencia); //donde está toda la información "rs".
				
				processFacturas(table, rs, font);
			
				document.add(table);
			}
			catch (SQLException sqle) 
			{
				System.out.println("Error 21-"+sqle.getMessage());
			}
			
			//Close document
			document.close();
			
			// Open the new PDF document just created
			Desktop.getDesktop().open(new File(dest));
		}
		
		catch(IOException ioe) {}
	}
	

	public void processFacturas(Table table, ResultSet rs, PdfFont font) throws SQLException 
	{
		table.addHeaderCell(new Cell().add(new Paragraph("ID").setFont(font).setBold()));
		table.addHeaderCell(new Cell().add(new Paragraph("FECHA").setFont(font).setBold()));
		table.addHeaderCell(new Cell().add(new Paragraph("PAGO").setFont(font).setBold()));
		table.addHeaderCell(new Cell().add(new Paragraph("TOTAL FRA.").setFont(font).setBold()));
		table.addHeaderCell(new Cell().add(new Paragraph("CLIENTE").setFont(font).setBold()));
		
		while (rs.next()) 
		{
			/*if (isHeader) // Celdas de encabezado.
			{
				table.addHeaderCell(new Cell().add(new Paragraph(rs.getString("idCliente")).setFont(font)));
				table.addHeaderCell(new Cell().add(new Paragraph(rs.getString("nombreCliente")).setFont(font)));
				table.addHeaderCell(new Cell().add(new Paragraph(rs.getString("dniCliente")).setFont(font)));
				table.addHeaderCell(new Cell().add(new Paragraph(rs.getString("telefonoCliente")).setFont(font)));
			} 
			else*/
			table.addCell(new Cell().add(new Paragraph(rs.getString("idFactura")).setFont(font)));
			table.addCell(new Cell().add(new Paragraph(rs.getString("fechaFactura")).setFont(font)));
			table.addCell(new Cell().add(new Paragraph(rs.getString("formaPagoFactura")).setFont(font)));
			table.addCell(new Cell().add(new Paragraph(rs.getString("importeTotalFactura")).setFont(font)));
			table.addCell(new Cell().add(new Paragraph(rs.getString("nombreCliente")).setFont(font)));
		}
	}
	
	
	//Método APUNTE LOG.
	public void guardarLog (String usuario, String sentencia)
	{
		try
		{
			//Abrir fichero
			//True: para añadir más texto, SIN machacar el texto que existiera. False: para machacar.
			FileWriter fw = new FileWriter("movimientos.log", true); //Fichero. Si no se especifica la ruta, se guarda donde está guardado el proyecto creado.
			BufferedWriter bw = new BufferedWriter(fw); //Intermediario.
			PrintWriter salida = new PrintWriter(bw); //Escribir.
			
			
			//Muestra la fecha tal cual en este formato (Fri Apr 07 13:29:42 CEST 2023).
			Date date = new Date();
			SimpleDateFormat formateador; //Para formatear la fecha y hora a nuestro gusto.
			
			//Formatear la hora y fecha a mi gusto y salgan juntas.
			formateador = new SimpleDateFormat("[dd/MM/yyyy HH:mm:ss]");
			String fecha = formateador.format(date);
			
			
			//Gestionar fichero
			//Para escribir algo nuevo, se vuelve a escribir aquí. NOTA: ejecutar antes para que se guarde la información en el archivo.
			salida.println(fecha + " ["+ usuario +"] " + sentencia); //Todo lo que se escribe aquí se graba en el fichero.
			System.out.println("Información almacenada"); //Para comprobar que se ha almacenado correctamente.
			
			//Cerrar fichero
			//Se cierra en orden inverso de la creación.
			salida.close();
			bw.close();
			fw.close();
		}
		
		catch(IOException ioe)
		{
			System.out.println("Error en Fichero");
		}		
	}
}
