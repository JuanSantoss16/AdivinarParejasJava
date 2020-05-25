package es.studium.Practica3EVALJuegoAdivinarParejas;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * @ @author Alberto Arrojo Carrasco 1º DAW - Grupo Studium, Sevilla.
 */

public class AdivinarParejas extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    JButton arrayBotones[] = new JButton[16]; // Los botones que el usuario ve
    Carta arrayCartas[] = new Carta[16]; // Array de cartas para la lógica del
					 // juego
    Carta arrayCartaTemporal = new Carta(0, "fondo.gif", 0); // Carta Temporal
    int par = 0;
    int puntos = 500;

    public JMenuBar mb;

    public JMenu menuNuevaPartida;
    public JMenuItem miNuevaPartida;

    public JMenu menuAyuda;
    public JMenuItem miAyuda;

    public JMenu menuMejoresPartidas;
    public JMenuItem miMejoresPartidas;

    public AdivinarParejas() {
	iniciarVentanaJuego();

    }

    public void iniciarVentanaJuego() { // Interfaz Gráfica

	this.getContentPane().setBackground(new Color(255, 214, 34));
	this.setSize(500, 650);
	this.setTitle("Adivinar Parejas - Los Simpsons");
	this.setLayout(null);
	this.setLocationRelativeTo(null);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setLayout(null);
	this.setResizable(false);
	this.setIconImage(new ImageIcon(getClass().getResource("rosquilla.gif")).getImage());

	mb = new JMenuBar();
	setJMenuBar(mb);

	menuNuevaPartida = new JMenu("Nueva Partida");
	mb.add(menuNuevaPartida);
	miNuevaPartida = new JMenuItem("Comenzar una nueva partida");
	miNuevaPartida.addActionListener(this);
	menuNuevaPartida.add(miNuevaPartida);

	menuMejoresPartidas = new JMenu("Mejores Partidas");
	mb.add(menuMejoresPartidas);
	miMejoresPartidas = new JMenuItem("Consultar mejores partidas");
	miMejoresPartidas.addActionListener(this);
	menuMejoresPartidas.add(miMejoresPartidas);

	menuAyuda = new JMenu("Ayuda");
	mb.add(menuAyuda);
	miAyuda = new JMenuItem("Consultar ayuda");
	miAyuda.addActionListener(this);
	menuAyuda.add(miAyuda);

	JTextField txtPuntos;
	txtPuntos = new JTextField("Puntos: 500");
	txtPuntos.setEditable(false);
	this.add(txtPuntos);
	txtPuntos.setBounds(20, 20, 100, 30);

	int contador = 0; // Variable para posicionar

	for (int i = 0; i < 4; i++) { // Tratamos las columnas
	    for (int j = 0; j < 4; j++) {// Tratamos las filas
		JButton btn = new JButton("", new ImageIcon(this.getClass().getResource("fondo.gif")));
		btn.setBounds((i + 1) * 85, (j + 1) * 85, 70, 70);
		btn.setName(contador + "");
		btn.addActionListener(this);
		arrayBotones[contador] = btn;
		contador++;
		this.add(btn);

		// Labels del menú

		JLabel labelAdivPar = new JLabel("ADIVINAR PAREJAS");
		labelAdivPar.setFont(new java.awt.Font("Simpsonfont", 1, 40));
		labelAdivPar.setBounds(40, 450, 500, 50);
		this.add(labelAdivPar);

		JLabel label2 = new JLabel("© Juego Adivinar Parejas - Alberto Arrojo Carrasco");
		label2.setFont(new java.awt.Font("Cambria", 2, 22));
		label2.setBounds(5, 525, 500, 30);
		this.add(label2);

		JLabel labelStudium = new JLabel("1º DAW - Grupo Studium");
		labelStudium.setFont(new java.awt.Font("Comic Sans MS", 2, 16));
		labelStudium.setBounds(280, 10, 500, 50);
		this.add(labelStudium);
	    }
	}
	mezclarCartas();
    }

    public void mezclarCartas() { // Método para mezclar las cartas

	int contadorMezclarCartas = 0;

	for (int i = 1; i <= 8; i++) { // De 1 a 8 IMÁGENES

	    Carta carta1 = new Carta(i, i + ".gif", contadorMezclarCartas); // 1.gif,
									    // 2.gif...

	    Carta carta2 = new Carta(i, i + ".gif", contadorMezclarCartas + 1); // Pareja
										// de
										// carta1

	    arrayCartas[contadorMezclarCartas] = carta1;
	    contadorMezclarCartas++;
	    arrayCartas[contadorMezclarCartas] = carta2;
	    contadorMezclarCartas++;
	}

	int llenar = 0; // para saber en que momento se han llenado las 16
			// cartas

	Carta cartaTemporal[] = new Carta[16];
	for (int i = 0; i < cartaTemporal.length; i++) {
	    cartaTemporal[i] = new Carta(0, "fondo.gif", -1);
	}

	while (llenar <= 15) {
	    int aleatorio = ((int) (Math.random() * 16)); // Número aleatorio
							  // rango 0-15

	    if (buscarNumero(aleatorio, cartaTemporal)) {
		cartaTemporal[llenar] = arrayCartas[aleatorio];
		cartaTemporal[llenar].btn = arrayBotones[llenar];
		llenar++;
	    }

	}
	arrayCartas = cartaTemporal;
    }

    public boolean buscarNumero(int numAleatorio, Carta c[]) {
	int contadorBuscarNumero = 0;
	for (Carta c1 : c) { // Por cada carta c1 que se encuentre dentro del
			     // array 'c'
	    if (numAleatorio == c1.posicion) {
		contadorBuscarNumero++;
	    }
	}
	return (contadorBuscarNumero < 1);
    }

    // Sonidos

    public void sonidoFallo() {

	File sf = new File(
		"C:\\Users\\Alberto\\eclipse-workspace\\Practica3EVALJuegoAdivinarParejas\\src\\es\\studium\\Practica3EVALJuegoAdivinarParejas\\fallo.wav");
	AudioFileFormat aff;
	AudioInputStream ais;
	try {
	    aff = AudioSystem.getAudioFileFormat(sf);
	    ais = AudioSystem.getAudioInputStream(sf);
	    AudioFormat af = aff.getFormat();
	    DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat(),
		    ((int) ais.getFrameLength() * af.getFrameSize()));
	    Clip ol = (Clip) AudioSystem.getLine(info);
	    ol.open(ais);
	    ol.loop(Clip.LOOP_CONTINUOUSLY);
	    // Damos tiempo para que el sonido sea escuchado
	    Thread.sleep(570);
	    ol.close();
	} catch (UnsupportedAudioFileException ee) {
	    System.out.println(ee.getMessage());
	} catch (IOException ea) {
	    System.out.println(ea.getMessage());
	} catch (LineUnavailableException LUE) {
	    System.out.println(LUE.getMessage());
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    public void sonidoSimpsons8bit() {

	File sf = new File(
		"C:\\Users\\Alberto\\eclipse-workspace\\Practica3EVALJuegoAdivinarParejas\\src\\es\\studium\\Practica3EVALJuegoAdivinarParejas\\simpsons8bit.wav");
	AudioFileFormat aff;
	AudioInputStream ais;
	try {
	    aff = AudioSystem.getAudioFileFormat(sf);
	    ais = AudioSystem.getAudioInputStream(sf);
	    AudioFormat af = aff.getFormat();
	    DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat(),
		    ((int) ais.getFrameLength() * af.getFrameSize()));
	    Clip ol = (Clip) AudioSystem.getLine(info);
	    ol.open(ais);
	    ol.loop(Clip.LOOP_CONTINUOUSLY);
	    // Damos tiempo para que el sonido sea escuchado
	    Thread.sleep(70200);
	} catch (UnsupportedAudioFileException ee) {
	    System.out.println(ee.getMessage());
	} catch (IOException ea) {
	    System.out.println(ea.getMessage());
	} catch (LineUnavailableException LUE) {
	    System.out.println(LUE.getMessage());
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    public void sonidoGanar() {

	File sf = new File(
		"C:\\Users\\Alberto\\eclipse-workspace\\Practica3EVALJuegoAdivinarParejas\\src\\es\\studium\\Practica3EVALJuegoAdivinarParejas\\ganar.wav");
	AudioFileFormat aff;
	AudioInputStream ais;
	try {
	    aff = AudioSystem.getAudioFileFormat(sf);
	    ais = AudioSystem.getAudioInputStream(sf);
	    AudioFormat af = aff.getFormat();
	    DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat(),
		    ((int) ais.getFrameLength() * af.getFrameSize()));
	    Clip ol = (Clip) AudioSystem.getLine(info);
	    ol.open(ais);
	    ol.loop(Clip.LOOP_CONTINUOUSLY);
	    // Damos tiempo para que el sonido sea escuchado
	    Thread.sleep(2700);
	    ol.close();
	} catch (UnsupportedAudioFileException ee) {
	    System.out.println(ee.getMessage());
	} catch (IOException ea) {
	    System.out.println(ea.getMessage());
	} catch (LineUnavailableException LUE) {
	    System.out.println(LUE.getMessage());
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    public void sonidoAciertaCarta() {

	File sf = new File(
		"C:\\Users\\Alberto\\eclipse-workspace\\Practica3EVALJuegoAdivinarParejas\\src\\es\\studium\\Practica3EVALJuegoAdivinarParejas\\aciertacarta.wav");
	AudioFileFormat aff;
	AudioInputStream ais;
	try {
	    aff = AudioSystem.getAudioFileFormat(sf);
	    ais = AudioSystem.getAudioInputStream(sf);
	    AudioFormat af = aff.getFormat();
	    DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat(),
		    ((int) ais.getFrameLength() * af.getFrameSize()));
	    Clip ol = (Clip) AudioSystem.getLine(info);
	    ol.open(ais);
	    ol.loop(Clip.LOOP_CONTINUOUSLY);
	    // Damos tiempo para que el sonido sea escuchado
	    Thread.sleep(780);
	    ol.close();
	} catch (UnsupportedAudioFileException ee) {
	    System.out.println(ee.getMessage());
	} catch (IOException ea) {
	    System.out.println(ea.getMessage());
	} catch (LineUnavailableException LUE) {
	    System.out.println(LUE.getMessage());
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {

	if (e.getSource() == miAyuda) {

	    try {
		Runtime.getRuntime().exec("hh.exe ayuda/ayuda.chm");
	    } catch (IOException e2) {
		e2.printStackTrace();
	    }

	} else if (e.getSource() == miNuevaPartida) {

	    mezclarCartas();

	    for (int i = 0; i < arrayBotones.length; i++) {
		tapar(i);
		puntos = 500;

	    }

	} else if (e.getSource() == miMejoresPartidas) {

	    new mejoresPartidas();

	}

	// Base del juego
	for (int i = 0; i < arrayBotones.length; i++) {

	    if (arrayCartas[i].btn == e.getSource() && arrayCartas[i].revelada == false) {
		arrayCartas[i].btn.setIcon(arrayCartas[i].img);

		if (par == 0) { // primera carta que descubre el usuario

		    arrayCartas[i].revelada = true;
		    arrayCartaTemporal = arrayCartas[i]; // se guarda en el array de cartas temporal para comparar

		    par = 1; // cuando el usuario de click en una carta, el valor de la variable "par" cambia
			     // a 1

		} else { // en caso de que el usuario haya dado click sobre la segunda carta

		    par = 0; // cuando el usuario de click en la segunda carta, el valor de la variable "par"
		    // cambia a 0

		    if (arrayCartas[i].valor == arrayCartaTemporal.valor) { // si la carta seleccionada coincide con la
			// carta temporal

			arrayCartas[i].revelada = true; // se revelan las dos cartas

			boolean bandera = true;

			for (Carta elemento : arrayCartas) { // por cada carta "elemento" que se encuentre dentro del
			    // array de cartas

			    if (elemento.revelada == false) { // si todavia no ha ganado el usuario

				bandera = false;

				break;

			    }

			}

			if (bandera) { // si todas las cartas se han revelado
			    arrayCartas[i].btn.update(arrayCartas[i].btn.getGraphics());
			    sonidoGanar();
			    Icon iconoGanador = new ImageIcon(getClass().getResource("ganador.png"));
			    JOptionPane.showMessageDialog(this, "¡Felicidades! Has conseguido " + puntos + " puntos",
				    "¡ENHORABUENA!", JOptionPane.PLAIN_MESSAGE, iconoGanador);

			    String nombreJugador = JOptionPane.showInputDialog(this,
				    "Introduce tu nombre para el récord.");
			    System.out.println(nombreJugador);

			    Connection con = conectar();

			    int respuesta = insertar(con, "puntos", nombreJugador, puntos);

			    if (respuesta == 0) {
				System.out.println("Alta de jugador correcta");
			    } else {
				System.out.println("Error en el alta de jugador");
			    }
			    desconectar(con);

			} else { // si el usuario acierta la carta
			    arrayCartas[i].btn.update(arrayCartas[i].btn.getGraphics());
			    sonidoAciertaCarta();
			    JTextField txtIntentos;
			    txtIntentos = new JTextField("Puntos: " + puntos);
			    txtIntentos.setEditable(false);
			    this.add(txtIntentos);
			    txtIntentos.setBounds(20, 20, 100, 30);
			}

		    } else {
			try { // si el usuario no acierta la carta

			    arrayCartas[i].btn.update(arrayCartas[i].btn.getGraphics());
			    sonidoFallo();
			    tapar(i);
			    puntos = puntos - 25;

			    JTextField txtIntentos;
			    txtIntentos = new JTextField("Puntos: " + puntos);
			    txtIntentos.setEditable(false);
			    this.add(txtIntentos);
			    txtIntentos.setBounds(20, 20, 100, 30);

			    if (puntos == 0) {

				JOptionPane.showMessageDialog(this, "TE HAS QUEDADO SIN PUNTOS.", "Se acabó el juego.",
					JOptionPane.PLAIN_MESSAGE);
				this.setVisible(false);
				new menuPrincipal();

			    }

			} catch (Exception e2) {
			    System.out.println(e2);
			}
		    }

		}

	    }

	}

    }

    public void tapar(int a) { // Método para volver a tapar la carta

	arrayCartas[a].btn.setIcon(new ImageIcon(this.getClass().getResource("fondo.gif"))); // la 2a carta que el
	// usuario revela, se tapa
	// con fondo.gif

	arrayCartas[Integer.valueOf(arrayCartaTemporal.btn.getName())].revelada = false; // la posicion temporal se
	// transforma a entero y se
	// oculta
	
	arrayCartas[Integer.valueOf(arrayCartaTemporal.btn.getName())].btn
		.setIcon(new ImageIcon(this.getClass().getResource("fondo.gif"))); // se tapa la carta con fondo.gif

    }

    public Connection conectar() { // Conectar a la base de datos
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
	    System.out.println("ERROR:La dirección no es válida o el usuario y clave");
	    ex.printStackTrace();
	} catch (ClassNotFoundException cnfe) {
	    System.out.println("Error 1-" + cnfe.getMessage());
	}
	return con;
    }

    public int insertar(Connection con, String tabla, String nombreJugador, int puntos) { // Insertar en la base de
	// datos
	int respuesta = 0;
	try {
	    Statement sta = con.createStatement();
	    String cadenaSQL = "INSERT INTO " + tabla + " VALUES (null, '" + nombreJugador + "', '" + puntos + "')";

	    System.out.println(cadenaSQL);
	    sta.executeUpdate(cadenaSQL);
	    sta.close();
	} catch (SQLException ex) {
	    System.out.println("ERROR:al hacer un Insert");
	    ex.printStackTrace();
	    respuesta = 1;
	}
	return respuesta;
    }

    public void desconectar(Connection con) { // Desconectar de la base de datos
	try {
	    con.close();
	} catch (Exception e) {
	}
    }

    public static void main(String[] args) {

	new AdivinarParejas().setVisible(true);

    }
}