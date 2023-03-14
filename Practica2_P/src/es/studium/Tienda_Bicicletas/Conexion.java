package es.studium.Tienda_Bicicletas;

import java.awt.Choice;
import java.awt.TextArea;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
		sentencia = "SELECT*FROM usuarios WHERE nombreUsuario = '"+u+"' AND claveUsuario = SHA2('"+c+"',256);";  //"SHA2": algoritmo de encriptación.
		
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
	public int altaCliente(String n, String dni, String tlf)
	{
		sentencia = "INSERT INTO clientes VALUES (null, '"+n+"', '"+dni+"', '"+tlf+"');";
		
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
	public void rellenarListadoClientes(TextArea areaTexto) //TextArea porque es el tipo de areaTexto.
	{
		sentencia = "SELECT idCliente, nombreCliente, dniCliente, telefonoCliente FROM clientes;";
		
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
	public int eliminarCliente(String idCliente)
	{
		sentencia = "DELETE FROM clientes WHERE idCliente=" + idCliente; //Sentencia.
		
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
	public int modificarCliente(String nombre, String dni, String telefono, String idCliente)
	{
		sentencia = "UPDATE clientes SET nombreCliente ='"+nombre+"', dniCliente ='"+dni
				+"', telefonoCliente = '"+telefono+"' WHERE idCliente="+idCliente+";";
		
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
	public int altaArticulo(String desc, String marca, String precio)
	{
		sentencia = "INSERT INTO articulos VALUES (null, '"+desc+"', '"+marca+"', '"+precio+"');";
		
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
	public void rellenarListadoArticulos(TextArea areaTexto) //TextArea porque es el tipo de areaTexto.
	{
		sentencia = "SELECT idArticulo, descripcionArticulo, marcaArticulo, precioArticulo FROM articulos;";
		
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
				choArticulos.add(rs.getString("idArticulo")+"-"+rs.getString("descripcionArticulo")+"-"+rs.getString("marcaArticulo"));
			}
		}
		
		catch (SQLException sqle)
		{
			System.out.println("Error 12-"+sqle.getMessage());
		}
	}


	//Método ELIMINAR ARTICULO.
	public int eliminarArticulo(String idArticulo)
	{
		sentencia = "DELETE FROM articulos WHERE idArticulo=" + idArticulo; //Sentencia.
		
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
	public int modificarArticulo(String desc, String marca, String precio, String idArticulo)
	{
		sentencia = "UPDATE articulos SET descripcionArticulo ='"+desc+"', marcaArticulo ='"+marca
				+"', precioArticulo = '"+precio+"' WHERE idArticulo="+idArticulo+";";
		
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
}
