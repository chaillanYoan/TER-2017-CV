import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JButton;
import java.awt.Point;

import javax.swing.UIDefaults;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.UIManager;
import javax.swing.JTextPane;

@SuppressWarnings("serial")
public class Popup extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void pop(Point p, String s) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Popup frame = new Popup(p,s);
					
					frame.setVisible(true);
					frame.setAlwaysOnTop(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Popup(Point p, String s) {
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds((int)p.getX()+150, (int)p.getY()+250, 300, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnOk = new JButton("OK");
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				exit();
			}
		});
		btnOk.setBounds(95, 127, 89, 23);
		contentPane.add(btnOk);
		
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setForeground(Color.BLACK);
		textPane.setBounds(6, 33, 282, 73);
		textPane.setText(s);
		
		
		StyledDocument doc = textPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		//change background color, mais comme dirait Queen : "it's a kind of magic" https://www.youtube.com/watch?v=0p_1QSUsbsM
		//http://stackoverflow.com/questions/15228336/changing-the-look-and-feel-changes-the-color-of-jtextpane
		Color bgColor = new Color(214,217,223);
		UIDefaults defaults = new UIDefaults();
		defaults.put("EditorPane[Enabled].backgroundPainter", bgColor);
		textPane.putClientProperty("Nimbus.Overrides", defaults);
		textPane.putClientProperty("Nimbus.Overrides.InheritDefaults", false);
		textPane.setBackground(bgColor);
		
		
		contentPane.add(textPane);
	}
	
	public void exit(){
		//this.dispose();
		this.setVisible(false);
	}
}
