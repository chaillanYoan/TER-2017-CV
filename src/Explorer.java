import java.awt.BorderLayout;
//import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;

@SuppressWarnings("serial")
public class Explorer extends JFrame {
	private String filename;
	private String filepath;
	
	private String filenames[];
	private String filepaths[];
	private String folder;

	private JPanel contentPane;

	/**
	 * Launch the application. 
	 * 
	 * new Explorer() > explorer for files
	 * new Explorer("DIRECTORY") > explorer for directories
	 * 
	 * fonction main inutile si utilisé depuis GUI
	 
	//main utilisé seulement en stand-alone
	public static void main(String[] args) {
		System.out.println("sdsd");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if(args.length > 0){
						Explorer frame = new Explorer(args[0]);
						frame.setVisible(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/
	
	/**
	 * Constructor of Explorer
	 * @param	type {@code String} the type of explorer : 	"UNIQUE_FILE" or "MULTIPLE_FILES" or "DIRECORY"
	 */
	public Explorer(String type){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 607, 447);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		

		
		if(type.compareTo("UNIQUE_FILE") == 0){
			/*UNIQUE FILE*/
			JFileChooser fileChooser = new JFileChooser();
			File workingDirectory = new File(System.getProperty("user.dir"));
			fileChooser.setCurrentDirectory(workingDirectory);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Document excel (.xls)","xls");
			fileChooser.setFileFilter(filter);
			contentPane.add(fileChooser, BorderLayout.CENTER);
			
			int retour = fileChooser.showOpenDialog(contentPane);
			if(retour == JFileChooser.APPROVE_OPTION){
				//verification de l'extension
				if(fileChooser.getSelectedFile().getName().contains(".xls") == true){
					filename = fileChooser.getSelectedFile().getName();
					filepath = fileChooser.getSelectedFile().getAbsolutePath();
				}
			}
			else{
				// pas de fichier choisi
				//System.out.println("Pas de fichier selectionné");
			};
		}
		else if(type.compareTo("MULTIPLE_FILES") == 0){
			/*MULTIPLE FILES*/
			JFileChooser fileChooser = new JFileChooser();
			File workingDirectory = new File(System.getProperty("user.dir"));
			fileChooser.setCurrentDirectory(workingDirectory);
			fileChooser.setMultiSelectionEnabled(true);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Document word (.doc)","doc");
			fileChooser.setFileFilter(filter);
			contentPane.add(fileChooser, BorderLayout.CENTER);
			
			int retour = fileChooser.showOpenDialog(contentPane);
			if(retour == JFileChooser.APPROVE_OPTION){
				File[] files = fileChooser.getSelectedFiles();
				int cpt = 0;
				
				for(int i=0; i<files.length; i++){
					if(files[i].getName().contains(".doc") == true)
						cpt++;
				}
				
				
				filenames = new String[cpt];
				filepaths = new String[cpt];
				
				cpt = 0;
				for(int i=0; i<files.length; i++){
					if(files[i].getName().contains(".doc") == true){
						filenames[cpt] = files[i].getName();
						filepaths[cpt] = files[i].getAbsolutePath();
						cpt++;
					}
				}
				
			}
			else{
				// pas de fichier choisi
				//System.out.println("Pas de fichier selectionné");
			};
		}
		else if(type.compareTo("DIRECTORY") == 0){
			/*DIRECTORY*/
			
			JFileChooser fileChooser = new JFileChooser();
			File workingDirectory = new File(System.getProperty("user.dir"));
			fileChooser.setCurrentDirectory(workingDirectory);
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setAcceptAllFileFilterUsed(false);
			contentPane.add(fileChooser, BorderLayout.CENTER);
			
			int retour=fileChooser.showOpenDialog(contentPane);
			if(retour==JFileChooser.APPROVE_OPTION){
			   
				//System.out.println("getCurrentDirectory(): "+  fileChooser.getCurrentDirectory());
				
				//chemin du dossier
				folder = fileChooser.getSelectedFile().getAbsolutePath();
			}
			else{// pas de fichier choisi
				//System.out.println("Pas de fichier selectionné");
			};
		}
		else{
			
		}
	}
	
	
	
	
	public String getFilename() {
		return filename;
	}

	public String getFilepath() {
		return filepath;
	}
	
	public String[] getFilenames() {
		return filenames;
	}

	public String[] getFilepaths() {
		return filepaths;
	}
	
	public String getFolder() {
		return folder;
	}
	
	
	

}
