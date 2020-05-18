package es.studium.Practica3EVALJuegoAdivinarParejas;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class menuPrincipal extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	JButton btnNuevaPartida = new JButton("Nueva Partida");
	JButton btnMejoresPartidas = new JButton("Mejores Partidas");
	JButton btnAyuda = new JButton("Ayuda...");

	JLabel labelHomerPensando = new JLabel();

	public menuPrincipal() { // Creamos el menú principal

		this.setIconImage(new ImageIcon(getClass().getResource("rosquilla.gif"))
				.getImage());
		JLabel labelAdivPar = new JLabel("ADIVINAR PAREJAS LOS SIMPSONS");
		labelAdivPar.setFont(new java.awt.Font("Simpsonfont", 1, 20));
		labelAdivPar.setBounds(13, 400, 400, 30);
		this.add(labelAdivPar);

		JPanel menuPrincipal = new JPanel();

		setTitle("Menú Principal");
		setSize(400, 500);
		setLocationRelativeTo(null);

		// Añadimos los botones al menú principal

		menuPrincipal.add(btnNuevaPartida);
		btnNuevaPartida.addActionListener(this);

		menuPrincipal.add(btnMejoresPartidas);
		btnMejoresPartidas.addActionListener(this);

		menuPrincipal.add(btnAyuda);
		btnAyuda.addActionListener(this);
		labelHomerPensando.setIcon(new ImageIcon(
				"C:\\Users\\Alberto\\eclipse-workspace\\Practica3EVALJuegoAdivinarParejas\\src\\es\\studium\\Practica3EVALJuegoAdivinarParejas\\homerPensando.png"));
		menuPrincipal.add(labelHomerPensando);
		this.getContentPane().add(menuPrincipal)
				.setBackground(new Color(255, 214, 34));
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public void actionPerformed(ActionEvent e) {
		if (btnNuevaPartida == e.getSource()) {
			this.setVisible(false);
			new AdivinarParejas().setVisible(true);

		} else {
			if (btnMejoresPartidas == e.getSource()) {

				new mejoresPartidas();

			} else {
				if (btnAyuda == e.getSource()) {
					try {
						Runtime.getRuntime().exec("hh.exe ayuda/ayuda.chm");
					} catch (IOException e2) {
						e2.printStackTrace();
					}

				}
			}
		}

	}

	public static void main(String[] args) {

		new menuPrincipal();
		new AdivinarParejas().sonidoSimpsons8bit();

	}

}
