import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;

@SuppressWarnings("serial")
public class Explorer extends JFrame {
	private String filename;
	private String filepath;
	private String folder;

	private JPanel contentPane;

	/**
	 * Launch the application. 
	 * 
	 * new Explorer() > explorer for files
	 * new Explorer("DIRECTORY") > explorer for directories
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if(args.length > 0){
						Explorer frame = new Explorer("DIRECTORY");
						frame.setVisible(true);
					}
					else{
						Explorer frame = new Explorer();
						frame.setVisible(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	/*Constructeur pour une recherche de fichier*/
	public Explorer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 607, 447);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JFileChooser fileChooser = new JFileChooser();
		contentPane.add(fileChooser, BorderLayout.CENTER);
		int retour=fileChooser.showOpenDialog(contentPane);
		if(retour==JFileChooser.APPROVE_OPTION){
		   // un fichier a été choisi (sortie par OK)
			filename = fileChooser.getSelectedFile().getName();
			
		   // chemin absolu du fichier choisi
			filepath = fileChooser.getSelectedFile().getAbsolutePath();
			
			//chemin du dossier
			folder = fileChooser.getCurrentDirectory().getAbsolutePath();
		}
		else{// pas de fichier choisi
			//System.out.println("Pas de fichier selectionné");
		};
	}
	
	
	/*Constructeur pour une recherche de dossier*/
	public Explorer(String s){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 607, 447);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		contentPane.add(fileChooser, BorderLayout.CENTER);
		int retour=fileChooser.showOpenDialog(contentPane);
		if(retour==JFileChooser.APPROVE_OPTION){
		   
			System.out.println("getCurrentDirectory(): "+  fileChooser.getCurrentDirectory());
			
			//chemin du dossier
			folder = fileChooser.getCurrentDirectory().getAbsolutePath();
		}
		else{// pas de fichier choisi
			//System.out.println("Pas de fichier selectionné");
		};
	}
	
	
	
	

	public String getFilename() {
		return filename;
	}

	public String getFilepath() {
		return filepath;
	}
	
	public String getFolder() {
		return folder;
	}
	
	
	

}
