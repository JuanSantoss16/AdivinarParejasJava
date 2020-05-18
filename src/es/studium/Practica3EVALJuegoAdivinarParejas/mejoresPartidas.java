package es.studium.Practica3EVALJuegoAdivinarParejas;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class mejoresPartidas extends JFrame {

	private static final long serialVersionUID = 1L;
	JTextArea txtPuntuaciones;

	public mejoresPartidas() {
		this.setIconImage(new ImageIcon(getClass().getResource("rosquilla.gif"))
				.getImage());
		JLabel labelTop10 = new JLabel("Top 10");
		labelTop10.setFont(new java.awt.Font("Simpsonfont", 1, 30));
		labelTop10.setBounds(300, 135, 500, 30);
		this.add(labelTop10);

		JPanel mejoresPartidas = new JPanel();

		txtPuntuaciones = new JTextArea();
		txtPuntuaciones.setBounds(10, 50, 400, 300);
		add(txtPuntuaciones);
		setTitle("Mejores partidas");
		setSize(450, 350);
		setLocationRelativeTo(null);
		setResizable(false);
		this.add(mejoresPartidas);
		this.getContentPane().add(mejoresPartidas)
				.setBackground(new Color(255, 214, 34));
		setVisible(true);

		txtPuntuaciones.setEditable(false);
		txtPuntuaciones.setOpaque(false);
		txtPuntuaciones.setFont(new java.awt.Font("Cambria", 1, 15));

		setDefaultCloseOperation(HIDE_ON_CLOSE);

		Connection con = conectar();

		rellenarTextArea(con, txtPuntuaciones);

		desconectar(con);
	}

	public Connection conectar() { // Método para conectar a la base de datos
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/adivinarparejas?autoReconnect=true&useSSL=false";
		String user = "root";
		String password = "Studium2019;";
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			if (con != null) {
				System.out.println("Conectado a la base de datos");
			}
		} catch (SQLException ex) {
			System.out.println(
					"ERROR:La dirección no es válida o el usuario y clave");
			ex.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Error 1-" + cnfe.getMessage());
		}
		return con;
	}

	public void rellenarTextArea(Connection con, JTextArea t) { // Rellenamos el
																// JTextArea
		String sqlSelect = "SELECT nombreJugador, puntosJugador FROM puntos ORDER BY puntosJugador desc LIMIT 10";
		try {

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlSelect);
			while (rs.next()) {
				if (t.getText().length() == 0) {
					t.setText("- " + rs.getString("nombreJugador") + " ->  "
							+ rs.getString("puntosJugador") + " puntos");
				} else {
					t.setText(t.getText() + "\n" + "- "
							+ rs.getString("nombreJugador") + " ->  "
							+ rs.getString("puntosJugador") + " puntos");
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {

			System.out.println("ERROR:al consultar");
			ex.printStackTrace();
		}
	}

	public void desconectar(Connection con) { // Método para desconectar la base
												// de datos
		try {
			con.close();
		} catch (Exception e) {
		}
	}

}
