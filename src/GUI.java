import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.UIManager;
import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.JFormattedTextField;

public class GUI {
	
	private CardLayout cardLayout;
	private JFrame frmTer;
	
	private JTextField textFieldOutput;
	private JTextField textFieldTemplate;
	private JTextField textFieldExcel;
	
	private DefaultTableModel model;
	private JTable table;
	
	private String outputFolder, excelPath;
	private String[] templateNames, templatePaths;
	private ArrayList<Template> templates = new ArrayList<Template>();
	
	private int nombreAnnonces = -1, nombreCvParAnnonce = -1;
	
	private DefaultTableModel modelRandom;
	private JTable tableRandom;
	
	private JButton gotoP2;
	
	private Test testing;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmTer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		cardLayout = new CardLayout(0, 0);
		frmTer = new JFrame();
		frmTer.setTitle("Ultra CV-tron 2000");
		frmTer.setBounds(100, 100, 550, 750);
		frmTer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTer.getContentPane().setLayout(cardLayout);
		
		
		/**
		 * PANEL 1 : 
		 * -select files and output folder
		 * -generate shuffled table
		 * 
		 * >next panel will show the table
		 */
		JPanel panel1 = new JPanel();
		panel1.setName("PANEL1");
		frmTer.getContentPane().add(panel1, "PANEL1");
		SpringLayout sl_panel1 = new SpringLayout();
		panel1.setLayout(sl_panel1);
		
		
		JLabel lblFichierExcelxsl = new JLabel("Base de donnée (.xls) :");
		sl_panel1.putConstraint(SpringLayout.NORTH, lblFichierExcelxsl, 35, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, lblFichierExcelxsl, 51, SpringLayout.NORTH, panel1);
		panel1.add(lblFichierExcelxsl);
		
		
		JLabel lblDossierDeDestination = new JLabel("Dossier de destination :");
		panel1.add(lblDossierDeDestination);
		
		
		JLabel lblTemplatesDeCv = new JLabel("Templates de CV (.doc) :");
		sl_panel1.putConstraint(SpringLayout.EAST, lblDossierDeDestination, 0, SpringLayout.EAST, lblTemplatesDeCv);
		sl_panel1.putConstraint(SpringLayout.NORTH, lblTemplatesDeCv, 34, SpringLayout.SOUTH, lblFichierExcelxsl);
		panel1.add(lblTemplatesDeCv);
		JButton btnRandomisation = new JButton("Randomisation");
		sl_panel1.putConstraint(SpringLayout.WEST, btnRandomisation, 70, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, btnRandomisation, -33, SpringLayout.SOUTH, panel1);
		panel1.add(btnRandomisation);
		
		
		textFieldOutput = new JTextField();
		sl_panel1.putConstraint(SpringLayout.WEST, textFieldOutput, 171, SpringLayout.WEST, panel1);
		panel1.add(textFieldOutput);
		textFieldOutput.setEditable(false);
		textFieldOutput.setColumns(10);
		
		
		textFieldTemplate = new JTextField();
		sl_panel1.putConstraint(SpringLayout.EAST, textFieldOutput, 0, SpringLayout.EAST, textFieldTemplate);
		sl_panel1.putConstraint(SpringLayout.EAST, lblTemplatesDeCv, -9, SpringLayout.WEST, textFieldTemplate);
		sl_panel1.putConstraint(SpringLayout.WEST, textFieldTemplate, 170, SpringLayout.WEST, panel1);
		panel1.add(textFieldTemplate);
		textFieldTemplate.setEditable(false);
		textFieldTemplate.setColumns(10);
		
		
		textFieldExcel = new JTextField();
		sl_panel1.putConstraint(SpringLayout.WEST, textFieldExcel, 170, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.NORTH, textFieldTemplate, 22, SpringLayout.SOUTH, textFieldExcel);
		sl_panel1.putConstraint(SpringLayout.EAST, textFieldTemplate, 0, SpringLayout.EAST, textFieldExcel);
		sl_panel1.putConstraint(SpringLayout.EAST, lblFichierExcelxsl, -17, SpringLayout.WEST, textFieldExcel);
		sl_panel1.putConstraint(SpringLayout.NORTH, textFieldExcel, 30, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, textFieldExcel, 58, SpringLayout.NORTH, panel1);
		panel1.add(textFieldExcel);
		textFieldExcel.setEditable(false);
		textFieldExcel.setColumns(10);
		
		
		JButton btnParcourirTemplate = new JButton("Parcourir");
		sl_panel1.putConstraint(SpringLayout.NORTH, btnParcourirTemplate, -6, SpringLayout.NORTH, lblTemplatesDeCv);
		sl_panel1.putConstraint(SpringLayout.WEST, btnParcourirTemplate, 6, SpringLayout.EAST, textFieldTemplate);
		sl_panel1.putConstraint(SpringLayout.EAST, btnParcourirTemplate, -69, SpringLayout.EAST, panel1);
		panel1.add(btnParcourirTemplate);
		
		
		JButton btnParcourirExcel = new JButton("Parcourir");
		sl_panel1.putConstraint(SpringLayout.EAST, textFieldExcel, -6, SpringLayout.WEST, btnParcourirExcel);
		sl_panel1.putConstraint(SpringLayout.WEST, btnParcourirExcel, -164, SpringLayout.EAST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, btnParcourirExcel, -69, SpringLayout.EAST, panel1);
		sl_panel1.putConstraint(SpringLayout.NORTH, btnParcourirExcel, -7, SpringLayout.NORTH, lblFichierExcelxsl);
		panel1.add(btnParcourirExcel);
		
		
		JButton btnParcourirOutput = new JButton("Parcourir");
		sl_panel1.putConstraint(SpringLayout.WEST, btnParcourirOutput, 6, SpringLayout.EAST, textFieldOutput);
		sl_panel1.putConstraint(SpringLayout.EAST, btnParcourirOutput, -69, SpringLayout.EAST, panel1);
		panel1.add(btnParcourirOutput);
		
		
		JScrollPane scrollPanelListeTemplate = new JScrollPane();
		sl_panel1.putConstraint(SpringLayout.NORTH, textFieldOutput, 21, SpringLayout.SOUTH, scrollPanelListeTemplate);
		sl_panel1.putConstraint(SpringLayout.NORTH, lblDossierDeDestination, 27, SpringLayout.SOUTH, scrollPanelListeTemplate);
		sl_panel1.putConstraint(SpringLayout.SOUTH, scrollPanelListeTemplate, -248, SpringLayout.SOUTH, panel1);
		sl_panel1.putConstraint(SpringLayout.NORTH, scrollPanelListeTemplate, 148, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, scrollPanelListeTemplate, 46, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, scrollPanelListeTemplate, -38, SpringLayout.EAST, panel1);
		panel1.add(scrollPanelListeTemplate);
		
		/*Tableau des templates*/
		model = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Fichier", "Chemin", ""
				}
			){
				public boolean isCellEditable(int row, int column) {
					if(column > 1)
						return true;
					else
						return false;
				}
			};
		table = new JTable();
		table.setModel(model);
		table.setBounds(377, 118, 480, 300);
		table.getColumnModel().getColumn(2).setMaxWidth(150);
		table.getColumnModel().getColumn(2).setMinWidth(100);
		table.setRowHeight(30);
		/*Action de bouton supprimer du tableau*/
		Action delete = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        ((DefaultTableModel)table.getModel()).removeRow(modelRow);
		        templates.remove(modelRow);
		    }
		};
		@SuppressWarnings("unused")
		ButtonColumn buttonColumn = new ButtonColumn(table, delete, 2);
		scrollPanelListeTemplate.setViewportView(table);
		
		
		JLabel lblListeDesTemplates = new JLabel("Liste des templates :");
		sl_panel1.putConstraint(SpringLayout.NORTH, lblListeDesTemplates, 25, SpringLayout.SOUTH, lblTemplatesDeCv);
		sl_panel1.putConstraint(SpringLayout.WEST, lblListeDesTemplates, 6, SpringLayout.WEST, scrollPanelListeTemplate);
		sl_panel1.putConstraint(SpringLayout.SOUTH, lblListeDesTemplates, -6, SpringLayout.NORTH, scrollPanelListeTemplate);
		sl_panel1.putConstraint(SpringLayout.EAST, lblListeDesTemplates, 0, SpringLayout.EAST, btnRandomisation);
		panel1.add(lblListeDesTemplates);
		
		
		gotoP2 = new JButton("Afficher le résultat");
		sl_panel1.putConstraint(SpringLayout.NORTH, btnParcourirOutput, -166, SpringLayout.NORTH, gotoP2);
		sl_panel1.putConstraint(SpringLayout.SOUTH, btnParcourirOutput, -138, SpringLayout.NORTH, gotoP2);
		sl_panel1.putConstraint(SpringLayout.WEST, gotoP2, -219, SpringLayout.EAST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, gotoP2, -69, SpringLayout.EAST, panel1);
		gotoP2.setEnabled(false);
		sl_panel1.putConstraint(SpringLayout.NORTH, gotoP2, 0, SpringLayout.NORTH, btnRandomisation);
		gotoP2.setName("btnNext");
		gotoP2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(gotoP2.isEnabled())
					cardLayout.show(frmTer.getContentPane(),"PANEL2");
			}
		});
		panel1.add(gotoP2);
		
		
		JLabel lblNombreDannonces = new JLabel("Nombre d'annonces :");
		sl_panel1.putConstraint(SpringLayout.SOUTH, lblDossierDeDestination, -34, SpringLayout.NORTH, lblNombreDannonces);
		sl_panel1.putConstraint(SpringLayout.WEST, lblNombreDannonces, 156, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, lblNombreDannonces, 274, SpringLayout.WEST, panel1);
		panel1.add(lblNombreDannonces);
		
		
		JLabel lblNombreDeCv = new JLabel("Nombre de CV par annonce :");
		sl_panel1.putConstraint(SpringLayout.NORTH, lblNombreDannonces, -40, SpringLayout.NORTH, lblNombreDeCv);
		sl_panel1.putConstraint(SpringLayout.SOUTH, lblNombreDannonces, -24, SpringLayout.NORTH, lblNombreDeCv);
		sl_panel1.putConstraint(SpringLayout.WEST, lblNombreDeCv, 115, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, lblNombreDeCv, 274, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.NORTH, lblNombreDeCv, -70, SpringLayout.NORTH, btnRandomisation);
		sl_panel1.putConstraint(SpringLayout.SOUTH, lblNombreDeCv, -54, SpringLayout.NORTH, btnRandomisation);
		panel1.add(lblNombreDeCv);
		
		
		
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format){
			/*
			 * text.compareTo("") return null : permet d'autoriser la valeur "" ce qui permet de vider completement le champ
			 * sinon lorsqu'on rentre "1234" mais que l'on veut le supprimer par la suite il restera "1" qui sera impossible 
			 * a supprimer car "" n'est pas une valeur valide de base.
			 */
			@Override
			public Object stringToValue(String text) throws ParseException{
				if(text.compareTo("") == 0)
					return null;
				else
					return super.stringToValue(text);
			}
		};
		formatter.setValueClass(Integer.class);
	    formatter.setMinimum(0);
	    formatter.setMaximum(Integer.MAX_VALUE);
	    formatter.setAllowsInvalid(false);
	    // If you want the value to be committed on each keystroke instead of focus lost
	    formatter.setCommitsOnValidEdit(true);
	    
		JFormattedTextField formattedTextFieldNbAnnonces = new JFormattedTextField(formatter);
		sl_panel1.putConstraint(SpringLayout.SOUTH, textFieldOutput, -22, SpringLayout.NORTH, formattedTextFieldNbAnnonces);
		sl_panel1.putConstraint(SpringLayout.WEST, formattedTextFieldNbAnnonces, 6, SpringLayout.EAST, lblNombreDannonces);
		sl_panel1.putConstraint(SpringLayout.EAST, formattedTextFieldNbAnnonces, -184, SpringLayout.EAST, panel1);
		panel1.add(formattedTextFieldNbAnnonces);
		
		JFormattedTextField formattedTextFieldNbCvAnnonce = new JFormattedTextField(formatter);
		sl_panel1.putConstraint(SpringLayout.WEST, formattedTextFieldNbCvAnnonce, 6, SpringLayout.EAST, lblNombreDeCv);
		sl_panel1.putConstraint(SpringLayout.EAST, formattedTextFieldNbCvAnnonce, -184, SpringLayout.EAST, panel1);
		sl_panel1.putConstraint(SpringLayout.NORTH, formattedTextFieldNbAnnonces, -40, SpringLayout.NORTH, formattedTextFieldNbCvAnnonce);
		sl_panel1.putConstraint(SpringLayout.SOUTH, formattedTextFieldNbAnnonces, -12, SpringLayout.NORTH, formattedTextFieldNbCvAnnonce);
		sl_panel1.putConstraint(SpringLayout.SOUTH, formattedTextFieldNbCvAnnonce, -48, SpringLayout.NORTH, gotoP2);
		panel1.add(formattedTextFieldNbCvAnnonce);
		
		
		JButton btnValiderNbAnnonces = new JButton("Valider");
		btnValiderNbAnnonces.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(formattedTextFieldNbAnnonces.getValue() != null){
					nombreAnnonces = (int)formattedTextFieldNbAnnonces.getValue();
					formattedTextFieldNbAnnonces.setEnabled(false);
				}
			}
		});
		sl_panel1.putConstraint(SpringLayout.NORTH, btnValiderNbAnnonces, -6, SpringLayout.NORTH, lblNombreDannonces);
		sl_panel1.putConstraint(SpringLayout.WEST, btnValiderNbAnnonces, 6, SpringLayout.EAST, formattedTextFieldNbAnnonces);
		panel1.add(btnValiderNbAnnonces);
		
		
		JButton btnValiderNbCvAnnonce = new JButton("Valider");
		btnValiderNbCvAnnonce.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(formattedTextFieldNbCvAnnonce.getValue() != null){
					nombreCvParAnnonce = (int)formattedTextFieldNbCvAnnonce.getValue();
					formattedTextFieldNbCvAnnonce.setEnabled(false);
				}
			}
		});
		sl_panel1.putConstraint(SpringLayout.NORTH, btnValiderNbCvAnnonce, -6, SpringLayout.NORTH, lblNombreDeCv);
		sl_panel1.putConstraint(SpringLayout.WEST, btnValiderNbCvAnnonce, 6, SpringLayout.EAST, formattedTextFieldNbCvAnnonce);
		panel1.add(btnValiderNbCvAnnonce);
		
		
		JButton btnResetNbAnnonces = new JButton("Reset");
		btnResetNbAnnonces.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				formattedTextFieldNbAnnonces.setText("");
				formattedTextFieldNbAnnonces.setEnabled(true);
				nombreAnnonces = -1;
			}
		});
		sl_panel1.putConstraint(SpringLayout.NORTH, btnResetNbAnnonces, -6, SpringLayout.NORTH, lblNombreDannonces);
		sl_panel1.putConstraint(SpringLayout.WEST, btnResetNbAnnonces, 6, SpringLayout.EAST, btnValiderNbAnnonces);
		panel1.add(btnResetNbAnnonces);
		
		
		JButton btnResetNbCvAnnonce = new JButton("Reset");
		btnResetNbCvAnnonce.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				formattedTextFieldNbCvAnnonce.setText("");
				formattedTextFieldNbCvAnnonce.setEnabled(true);
				nombreCvParAnnonce = -1;
			}
		});
		sl_panel1.putConstraint(SpringLayout.NORTH, btnResetNbCvAnnonce, -6, SpringLayout.NORTH, lblNombreDeCv);
		sl_panel1.putConstraint(SpringLayout.WEST, btnResetNbCvAnnonce, 6, SpringLayout.EAST, btnValiderNbCvAnnonce);
		panel1.add(btnResetNbCvAnnonce);
		

		
		btnParcourirOutput.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String folder = new String();
				Explorer explorer = new Explorer("DIRECTORY");
				folder = explorer.getFolder();
				outputFolder = folder;
						
				textFieldOutput.setText(outputFolder);
			}
		});
		
		
		/*Bouton parcourir pour le Excel .xls*/
		btnParcourirExcel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Explorer explorer = new Explorer("UNIQUE_FILE");
				excelPath = explorer.getFilepath();
				
				textFieldExcel.setText(excelPath);
			}
		});
		
		
		/*Bouton parcourir pour les templates de CV*/
		btnParcourirTemplate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Explorer explorer = new Explorer("MULTIPLE_FILES");
				
				templateNames = explorer.getFilenames();
				templatePaths = explorer.getFilepaths();
				
				if(templateNames != null){
					textFieldTemplate.setText(templatePaths[templateNames.length-1]);
				
					for(int i=0; i<templateNames.length; i++)
						addListeCV(templateNames[i],templatePaths[i]);
				}
			}
		});
		
		
		/*Bouton pour Randomisations*/
		btnRandomisation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("CVCreator.outputapth : "+CVCreator.createOutputPath(1, "C:\\AYOYO\\TER\\GIT\\TER-2017-CV\\zoutput", "C:\\AYOYO\\TER\\GIT\\TER-2017-CV\\zinput\\1Prénom Nom.doc","chaillan","YOAN"));
				/*TODO mettre les verif dans une fonction a part qui renvoie un boolean*/
				if(excelPath == null){
					Point p = frmTer.getLocation();
					Popup.pop(p,"Veuillez selectionner une base de donnée (.xls)");
				}
				else if(templates.size() == 0){
					Point p = frmTer.getLocation();
					Popup.pop(p,"Veuillez selectionner un template de CV (.doc)");
				}
				else if(outputFolder == null){
					Point p = frmTer.getLocation();
					Popup.pop(p,"Veuillez selectionner un dossier de sortie.");
				}
				else if(nombreAnnonces < 0){
					Point p = frmTer.getLocation();
					Popup.pop(p,"Veuillez choisir un nombre d'annonces.");
				}
				else if(nombreCvParAnnonce < 0){
					Point p = frmTer.getLocation();
					Popup.pop(p,"Veuillez le nombre de CV par annonce.");
				}
				else{
					testing = new Test();
					testing.init(templates, outputFolder, excelPath);
					String[][] t = null;
					try {
						t = testing.generate();
					} catch (EncryptedDocumentException | InvalidFormatException | IOException e1) {
						//Auto-generated catch block
						e1.printStackTrace();
					}
					createTableRandom(t);
					gotoP2.setEnabled(true);
				}
			}
		});
		
		
		


		
		
		/**
		 * PANEL 2 : 
		 * -show the table the shuffled table
		 */
		
		JPanel panel2 = new JPanel();
		frmTer.getContentPane().add(panel2, "PANEL2");
		SpringLayout sl_panel2 = new SpringLayout();
		panel2.setLayout(sl_panel2);
		
		
		JLabel lblExcelRandomis = new JLabel("Résultat du mélange :");
		sl_panel2.putConstraint(SpringLayout.NORTH, lblExcelRandomis, 63, SpringLayout.NORTH, panel2);
		sl_panel2.putConstraint(SpringLayout.WEST, lblExcelRandomis, 30, SpringLayout.WEST, panel2);
		sl_panel2.putConstraint(SpringLayout.SOUTH, lblExcelRandomis, 79, SpringLayout.NORTH, panel2);
		sl_panel2.putConstraint(SpringLayout.EAST, lblExcelRandomis, 160, SpringLayout.WEST, panel2);
		panel2.add(lblExcelRandomis);
		
		
		JButton btnPrcdent = new JButton("Précédent");
		btnPrcdent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout.show(frmTer.getContentPane(),"PANEL1");
			}
		});
		sl_panel2.putConstraint(SpringLayout.NORTH, btnPrcdent, -77, SpringLayout.SOUTH, panel2);
		sl_panel2.putConstraint(SpringLayout.WEST, btnPrcdent, 30, SpringLayout.WEST, panel2);
		sl_panel2.putConstraint(SpringLayout.SOUTH, btnPrcdent, -37, SpringLayout.SOUTH, panel2);
		sl_panel2.putConstraint(SpringLayout.EAST, btnPrcdent, 145, SpringLayout.WEST, panel2);
		panel2.add(btnPrcdent);
		
		
		JButton btnGnrer = new JButton("Générer les CV");
		btnGnrer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//TODO gestion création des CV
				System.out.println("Et là les CVs sont créés");
				try {
					testing.create(nombreAnnonces, nombreCvParAnnonce);
				} catch (IOException e1) {
					// Auto-generated catch block
					e1.printStackTrace();
				}
			
			}
		});
		sl_panel2.putConstraint(SpringLayout.NORTH, btnGnrer, -40, SpringLayout.SOUTH, btnPrcdent);
		sl_panel2.putConstraint(SpringLayout.WEST, btnGnrer, -147, SpringLayout.EAST, panel2);
		sl_panel2.putConstraint(SpringLayout.SOUTH, btnGnrer, 0, SpringLayout.SOUTH, btnPrcdent);
		sl_panel2.putConstraint(SpringLayout.EAST, btnGnrer, -32, SpringLayout.EAST, panel2);
		panel2.add(btnGnrer);
		
		
		JScrollPane scrollPane = new JScrollPane();
		sl_panel2.putConstraint(SpringLayout.NORTH, scrollPane, 6, SpringLayout.SOUTH, lblExcelRandomis);
		sl_panel2.putConstraint(SpringLayout.WEST, scrollPane, 30, SpringLayout.WEST, panel2);
		sl_panel2.putConstraint(SpringLayout.SOUTH, scrollPane, -119, SpringLayout.NORTH, btnPrcdent);
		sl_panel2.putConstraint(SpringLayout.EAST, scrollPane, -32, SpringLayout.EAST, panel2);
		panel2.add(scrollPane);
		
		
		tableRandom = new JTable();
		scrollPane.setViewportView(tableRandom);
		
		

		
	}
	
	
	/**Fonction qui ajoute les templates au tableau**/
	public void addListeCV(String name, String path){
		
		/*Ajout a l'arrayList*/
		Template t = new Template(name,path);
		templates.add(t);
		
		/*Ajout au GUI*/
		if(table.getRowCount() == 0){
			model.addRow(new Object[]{name, path, "Supprimer"});
		}
		else{
			for(int i=0; i<table.getRowCount(); i++){
				//System.out.println("i="+i+" cell="+table.getValueAt(i, 0));
					
				if(table.getValueAt(i, 0) == null){
					table.setValueAt(name, i, 0);
					table.setValueAt(path, i, 1);
					table.setValueAt("Supprimer", i, 2);
					break;
				}
				else if(table.getValueAt(i, 0) != null){
					//System.out.println("new row");
					model.addRow(new Object[]{name, path, "Supprimer"});
					break;
				}
			}
		}
		//System.out.println("");
	}
	
	/**Fonction qui crée le tableau du resultat de la randomisation**/
	@SuppressWarnings("serial")
	public void createTableRandom(String [][] t){
		String tValues[][] = new String[(t.length-1)][t[0].length];
		String tTitles[] = t[0];
		
		for(int i=1; i<t.length; i++){
			for(int j=0; j<t[0].length; j++){
				tValues[i-1][j] = t[i][j];
			}
		}
		
		modelRandom = new DefaultTableModel(tValues,tTitles){
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		tableRandom.setModel(modelRandom);
	}
}
