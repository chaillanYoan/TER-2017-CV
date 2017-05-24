import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.ToolTipManager;
import javax.swing.JLabel;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
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
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JCheckBox;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import javax.swing.ListSelectionModel;
import java.awt.Font;

public class GUI {
	
	private CardLayout cardLayout;
	private JFrame frmTer;
	
	private JTextField textFieldOutput;
	private JTextField textFieldTemplate;
	private JTextField textFieldTemplateLM;
	private JTextField textFieldExcel;
	
	private DefaultTableModel model;
	private JTable table;
	private DefaultTableModel modelLM;
	private JTable tableLM;
	
	private String outputFolder, excelPath;
	private String[] templateNames, templatePaths, templateNamesLM, templatePathsLM;
	private ArrayList<Template> templates = new ArrayList<Template>();
	private ArrayList<Template> templatesLM = new ArrayList<Template>();
	
	private int nombreAnnonces = -1, nombreCvParAnnonce = -1;
	private long seed = -1;
	private boolean liaisonCV_LM = false, annonceMemeQualite = false;
	
	private DefaultTableModel modelRandom;
	private JTable tableRandom;
	
	private Test testing;
	
	private int caseSelectTableauLM;
	
	
	

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
		frmTer.setTitle("Ultra CV-tron 3000");
		frmTer.setBounds(100, 100, 1100, 750);
		frmTer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTer.getContentPane().setLayout(cardLayout);
		
		
		/**
		 * PANEL 1 : 
		 * -select files and output folder
		 * -generate shuffled table
		 * 
		 * >next panel will show the table
		 */
		
		ToolTipManager.sharedInstance().setInitialDelay(100);
		ToolTipManager.sharedInstance().setDismissDelay(10000);
		
		JPanel panel1 = new JPanel();
		panel1.setName("PANEL1");
		frmTer.getContentPane().add(panel1, "PANEL1");
		SpringLayout sl_panel1 = new SpringLayout();
		panel1.setLayout(sl_panel1);
		
		
		JLabel lblFichierExcelxsl = new JLabel("<html>\r\nBase de donnée (.xls)<sup>?</sup> : \r\n</html>");
		lblFichierExcelxsl.setToolTipText("<html>\r\nFichier contenant les données nécessaires à la création des CV et Lettre de Motivation :<br>\r\n<ul>\r\n<li>Nom</li>\r\n<li>Prenom</li>\r\n<li>Adresse</li>\r\n<li>Email</li>\r\n<li>Téléphone</li>\r\n<li>...</li>\r\n</u>\r\n</html>");
		panel1.add(lblFichierExcelxsl);
		
		
		JLabel lblDossierDeDestination = new JLabel("<html>\r\nDossier de destination<sup>?</sup> :\r\n</html>");
		sl_panel1.putConstraint(SpringLayout.WEST, lblDossierDeDestination, 0, SpringLayout.WEST, lblFichierExcelxsl);
		lblDossierDeDestination.setToolTipText("Dossier où seront créés CV et Lettres de motivation");
		panel1.add(lblDossierDeDestination);
		
		
		JLabel lblTemplatesDeCv = new JLabel("<html>\r\nTemplates de CV (.doc)<sup>?</sup> :\r\n</html>");
		sl_panel1.putConstraint(SpringLayout.EAST, lblTemplatesDeCv, 0, SpringLayout.EAST, lblFichierExcelxsl);
		lblTemplatesDeCv.setToolTipText("Template de base des CV au format word (.doc)");
		panel1.add(lblTemplatesDeCv);
		
		
		JButton btnRandomisation = new JButton("<html><center>\r\nGénération<br>\r\ndes résultats</center>\r\n</html>");
		sl_panel1.putConstraint(SpringLayout.NORTH, btnRandomisation, -61, SpringLayout.SOUTH, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, btnRandomisation, 170, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, btnRandomisation, 310, SpringLayout.WEST, panel1);
		panel1.add(btnRandomisation);
		btnRandomisation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Point p = frmTer.getLocation();
				
				if(excelPath == null){
					Popup.pop(p,"Veuillez selectionner une base de donnée (.xls)");
				}
				else if(templates.size() == 0){
					Popup.pop(p,"Veuillez selectionner un template de CV (.doc)");
				}
				else if(templatesLM.size() == 0){
					Popup.pop(p,"Veuillez selectionner un template de LM (.doc)");
				}
				else if(outputFolder == null){
					Popup.pop(p,"Veuillez selectionner un dossier de sortie.");
				}
				else if(nombreAnnonces < 0){
					Popup.pop(p,"Veuillez choisir un nombre d'annonces.");
				}
				else if(nombreCvParAnnonce < 0){
					Popup.pop(p,"Veuillez choisir le nombre de CV par annonce.");
				} 
				else if(annonceMemeQualite && nombreCvParAnnonce > nombreMemeQualite(templates)){
					Popup.pop(p,"Pas assez de templates de CV de même qualité.");
				}
				else if(annonceMemeQualite && nombreCvParAnnonce > nombreMemeQualite(templatesLM)){
					Popup.pop(p,"Pas assez de templates de LM de même qualité.");
				}
				else{
					System.out.println("======pre test=====");
					System.out.println("======+annonceMemeQualite liste templates : "+templates);
		        	System.out.println("======+annonceMemeQualite liste templates LM: "+templatesLM);
		        	
		        	if(liaisonCV_LM){
		        		//TODO liaison cv-lm : meme taille pour les 2 listes
		        		
		        		//on ajoute les template des LM dans le template des CV pour garder une trace de la liaison entre eux
		        		System.out.println("templates.size():"+templates.size()+" templatesLM.size():"+templatesLM.size());
		        		for(int i=0; i<templates.size(); i++){
		        			System.out.println("i="+i);
		        			templates.get(i).setLinkedLM(templatesLM.get(i));
		        		}
		        	}
					
					testing = new Test();
					testing.init(templates, templatesLM, outputFolder, excelPath, liaisonCV_LM, annonceMemeQualite, seed);
					String[][] t = null;
					try {
						t = testing.generate(nombreAnnonces,nombreCvParAnnonce);
					} catch (EncryptedDocumentException | InvalidFormatException | IOException e1) {
						//Auto-generated catch block
						e1.printStackTrace();
					}
					createTableRandom(t);
					cardLayout.show(frmTer.getContentPane(),"PANEL2");
				}
			}
		});
		
		
		textFieldOutput = new JTextField();
		sl_panel1.putConstraint(SpringLayout.NORTH, lblDossierDeDestination, 0, SpringLayout.NORTH, textFieldOutput);
		textFieldOutput.setBackground(Color.decode("#d15050"));
		sl_panel1.putConstraint(SpringLayout.WEST, textFieldOutput, 167, SpringLayout.WEST, panel1);
		panel1.add(textFieldOutput);
		textFieldOutput.setEditable(false);
		textFieldOutput.setColumns(10);
		
		
		textFieldTemplate = new JTextField();
		sl_panel1.putConstraint(SpringLayout.NORTH, lblTemplatesDeCv, 1, SpringLayout.NORTH, textFieldTemplate);
		textFieldTemplate.setBackground(Color.decode("#d15050"));
		sl_panel1.putConstraint(SpringLayout.WEST, textFieldTemplate, 170, SpringLayout.WEST, panel1);
		panel1.add(textFieldTemplate);
		textFieldTemplate.setEditable(false);
		textFieldTemplate.setColumns(10);
		
		
		textFieldExcel = new JTextField();
		sl_panel1.putConstraint(SpringLayout.WEST, textFieldExcel, 170, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, lblFichierExcelxsl, -9, SpringLayout.WEST, textFieldExcel);
		sl_panel1.putConstraint(SpringLayout.SOUTH, textFieldExcel, -27, SpringLayout.NORTH, textFieldTemplate);
		sl_panel1.putConstraint(SpringLayout.NORTH, lblFichierExcelxsl, 1, SpringLayout.NORTH, textFieldExcel);
		textFieldExcel.setBackground(Color.decode("#d15050"));
		panel1.add(textFieldExcel);
		textFieldExcel.setEditable(false);
		textFieldExcel.setColumns(10);
		
		
		JButton btnParcourirTemplate = new JButton("Parcourir");
		sl_panel1.putConstraint(SpringLayout.WEST, btnParcourirTemplate, 376, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, textFieldTemplate, -6, SpringLayout.WEST, btnParcourirTemplate);
		panel1.add(btnParcourirTemplate);
		btnParcourirTemplate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Explorer explorer = new Explorer("MULTIPLE_FILES");
				
				templateNames = explorer.getFilenames();
				templatePaths = explorer.getFilepaths();
				
				if(templateNames != null){
					textFieldTemplate.setText(templatePaths[templateNames.length-1]);
					textFieldTemplate.setBackground(Color.decode("#5dc35d"));
					
					for(int i=0; i<templateNames.length; i++)
						addListeCV(templateNames[i],templatePaths[i]);
				}
			}
		});
		
		
		JButton btnParcourirExcel = new JButton("Parcourir");
		sl_panel1.putConstraint(SpringLayout.NORTH, btnParcourirTemplate, 27, SpringLayout.SOUTH, btnParcourirExcel);
		sl_panel1.putConstraint(SpringLayout.WEST, btnParcourirExcel, 376, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, textFieldExcel, -6, SpringLayout.WEST, btnParcourirExcel);
		sl_panel1.putConstraint(SpringLayout.EAST, btnParcourirExcel, 0, SpringLayout.EAST, btnParcourirTemplate);
		panel1.add(btnParcourirExcel);
		btnParcourirExcel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Explorer explorer = new Explorer("UNIQUE_FILE");
				excelPath = explorer.getFilepath();
				
				if(excelPath != null){
					textFieldExcel.setText(excelPath);
					textFieldExcel.setBackground(Color.decode("#5dc35d"));
				}
			}
		});
		
		
		JButton btnParcourirOutput = new JButton("Parcourir");
		sl_panel1.putConstraint(SpringLayout.EAST, textFieldOutput, -9, SpringLayout.WEST, btnParcourirOutput);
		sl_panel1.putConstraint(SpringLayout.WEST, btnParcourirOutput, 376, SpringLayout.WEST, panel1);
		panel1.add(btnParcourirOutput);
		btnParcourirOutput.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String folder = new String();
				Explorer explorer = new Explorer("DIRECTORY");
				folder = explorer.getFolder();
				outputFolder = folder;
						
				if(outputFolder != null){
					textFieldOutput.setText(outputFolder);
					textFieldOutput.setBackground(Color.decode("#5dc35d"));
				}
			}
		});
		
		
		JScrollPane scrollPanelListeTemplate = new JScrollPane();
		sl_panel1.putConstraint(SpringLayout.NORTH, textFieldOutput, 14, SpringLayout.SOUTH, scrollPanelListeTemplate);
		sl_panel1.putConstraint(SpringLayout.EAST, scrollPanelListeTemplate, 471, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, scrollPanelListeTemplate, -211, SpringLayout.SOUTH, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, scrollPanelListeTemplate, 21, SpringLayout.WEST, panel1);
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
		sl_panel1.putConstraint(SpringLayout.SOUTH, textFieldTemplate, -26, SpringLayout.NORTH, lblListeDesTemplates);
		sl_panel1.putConstraint(SpringLayout.NORTH, scrollPanelListeTemplate, 6, SpringLayout.SOUTH, lblListeDesTemplates);
		sl_panel1.putConstraint(SpringLayout.WEST, lblListeDesTemplates, 27, SpringLayout.WEST, panel1);
		panel1.add(lblListeDesTemplates);
		
		
		JLabel lblNombreDannonces = new JLabel("<html>\r\nNombre d'annonces<sup>?</sup> :\r\n</html>");
		sl_panel1.putConstraint(SpringLayout.NORTH, lblNombreDannonces, 30, SpringLayout.SOUTH, textFieldOutput);
		sl_panel1.putConstraint(SpringLayout.WEST, lblNombreDannonces, 66, SpringLayout.WEST, panel1);
		lblNombreDannonces.setToolTipText("Chaque annonce aura un mélange aléatoire de la base de donnée différent");
		panel1.add(lblNombreDannonces);
		
		
		JLabel lblNombreDeCv = new JLabel("<html>\r\nNombre de CV par annonce<sup>?</sup> :\r\n</html>");
		sl_panel1.putConstraint(SpringLayout.SOUTH, lblNombreDannonces, -11, SpringLayout.NORTH, lblNombreDeCv);
		sl_panel1.putConstraint(SpringLayout.WEST, lblNombreDeCv, 30, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, lblNombreDeCv, 0, SpringLayout.EAST, lblNombreDannonces);
		lblNombreDeCv.setToolTipText("Nombre de CV par annonce");
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
		sl_panel1.putConstraint(SpringLayout.EAST, lblNombreDannonces, -6, SpringLayout.WEST, formattedTextFieldNbAnnonces);
		sl_panel1.putConstraint(SpringLayout.SOUTH, textFieldOutput, -27, SpringLayout.NORTH, formattedTextFieldNbAnnonces);
		formattedTextFieldNbAnnonces.setBackground(Color.decode("#d15050"));
		sl_panel1.putConstraint(SpringLayout.WEST, formattedTextFieldNbAnnonces, 218, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, formattedTextFieldNbAnnonces, 318, SpringLayout.WEST, panel1);
		panel1.add(formattedTextFieldNbAnnonces);
		
		JFormattedTextField formattedTextFieldNbCvAnnonce = new JFormattedTextField(formatter);
		sl_panel1.putConstraint(SpringLayout.NORTH, lblNombreDeCv, 0, SpringLayout.NORTH, formattedTextFieldNbCvAnnonce);
		sl_panel1.putConstraint(SpringLayout.NORTH, formattedTextFieldNbAnnonces, -35, SpringLayout.NORTH, formattedTextFieldNbCvAnnonce);
		sl_panel1.putConstraint(SpringLayout.SOUTH, formattedTextFieldNbAnnonces, -7, SpringLayout.NORTH, formattedTextFieldNbCvAnnonce);
		sl_panel1.putConstraint(SpringLayout.NORTH, formattedTextFieldNbCvAnnonce, -46, SpringLayout.NORTH, btnRandomisation);
		sl_panel1.putConstraint(SpringLayout.SOUTH, formattedTextFieldNbCvAnnonce, -18, SpringLayout.NORTH, btnRandomisation);
		formattedTextFieldNbCvAnnonce.setBackground(Color.decode("#d15050"));
		sl_panel1.putConstraint(SpringLayout.EAST, lblNombreDeCv, -6, SpringLayout.WEST, formattedTextFieldNbCvAnnonce);
		sl_panel1.putConstraint(SpringLayout.WEST, formattedTextFieldNbCvAnnonce, 218, SpringLayout.WEST, panel1);
		panel1.add(formattedTextFieldNbCvAnnonce);
		
		
		JButton btnValiderNbAnnonces = new JButton("Valider");
		sl_panel1.putConstraint(SpringLayout.NORTH, btnParcourirOutput, -55, SpringLayout.NORTH, btnValiderNbAnnonces);
		sl_panel1.putConstraint(SpringLayout.SOUTH, btnParcourirOutput, -27, SpringLayout.NORTH, btnValiderNbAnnonces);
		sl_panel1.putConstraint(SpringLayout.WEST, btnValiderNbAnnonces, 324, SpringLayout.WEST, panel1);
		btnValiderNbAnnonces.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(formattedTextFieldNbAnnonces.getValue() != null){
					nombreAnnonces = (int)formattedTextFieldNbAnnonces.getValue();
					formattedTextFieldNbAnnonces.setEnabled(false);

					formattedTextFieldNbAnnonces.setBorder(new LineBorder(Color.decode("#5dc35d")));
				}
			}
		});
		panel1.add(btnValiderNbAnnonces);
		
		
		JButton btnValiderNbCvAnnonce = new JButton("Valider");
		sl_panel1.putConstraint(SpringLayout.NORTH, btnValiderNbAnnonces, -35, SpringLayout.NORTH, btnValiderNbCvAnnonce);
		sl_panel1.putConstraint(SpringLayout.SOUTH, btnValiderNbAnnonces, -7, SpringLayout.NORTH, btnValiderNbCvAnnonce);
		sl_panel1.putConstraint(SpringLayout.EAST, formattedTextFieldNbCvAnnonce, -6, SpringLayout.WEST, btnValiderNbCvAnnonce);
		sl_panel1.putConstraint(SpringLayout.NORTH, btnValiderNbCvAnnonce, -107, SpringLayout.SOUTH, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, btnValiderNbCvAnnonce, 324, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, btnValiderNbCvAnnonce, -79, SpringLayout.SOUTH, panel1);
		btnValiderNbCvAnnonce.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(formattedTextFieldNbCvAnnonce.getValue() != null){
					if(excelPath == null){
						Point p = frmTer.getLocation();
						Popup.pop(p,"Veuillez selectionner une base de donnée (.xls) avant de remplir ce champ.");
						formattedTextFieldNbCvAnnonce.setText("");
					}
					else if((int)formattedTextFieldNbCvAnnonce.getValue() > templates.size()){
						Point p = frmTer.getLocation();
						Popup.pop(p,"Vous n'avez pas ajouter assez de templates de CV.");
					} 
					else if((int)formattedTextFieldNbCvAnnonce.getValue() > templatesLM.size()){
						Point p = frmTer.getLocation();
						Popup.pop(p,"Vous n'avez pas ajouter assez de templates de LM.");
					}
					else
						try {
							if((int)formattedTextFieldNbCvAnnonce.getValue() > ExcelParser.nombrePersonnes(excelPath) ){
								Point p = frmTer.getLocation();
								Popup.pop(p,"Pas assez de personnes dans la base de donnée.");
							}
							else{
								nombreCvParAnnonce = (int)formattedTextFieldNbCvAnnonce.getValue();
								formattedTextFieldNbCvAnnonce.setEnabled(false);
								formattedTextFieldNbCvAnnonce.setBorder(new LineBorder(Color.decode("#5dc35d")));
							}
						} catch (EncryptedDocumentException | InvalidFormatException | IOException e1) {
							// Auto-generated catch block
							e1.printStackTrace();
						}
				}
			}
		});
		panel1.add(btnValiderNbCvAnnonce);
		
		
		JButton btnResetNbAnnonces = new JButton("Reset");
		sl_panel1.putConstraint(SpringLayout.WEST, btnResetNbAnnonces, 6, SpringLayout.EAST, btnValiderNbAnnonces);
		btnResetNbAnnonces.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				formattedTextFieldNbAnnonces.setText("");
				formattedTextFieldNbAnnonces.setEnabled(true);

				formattedTextFieldNbAnnonces.setBorder(new JFormattedTextField().getBorder());
				nombreAnnonces = -1;
			}
		});
		panel1.add(btnResetNbAnnonces);
		
		
		JButton btnResetNbCvAnnonce = new JButton("Reset");
		sl_panel1.putConstraint(SpringLayout.NORTH, btnResetNbAnnonces, -35, SpringLayout.NORTH, btnResetNbCvAnnonce);
		sl_panel1.putConstraint(SpringLayout.SOUTH, btnResetNbAnnonces, -7, SpringLayout.NORTH, btnResetNbCvAnnonce);
		sl_panel1.putConstraint(SpringLayout.NORTH, btnResetNbCvAnnonce, -107, SpringLayout.SOUTH, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, btnResetNbCvAnnonce, 6, SpringLayout.EAST, btnValiderNbCvAnnonce);
		sl_panel1.putConstraint(SpringLayout.SOUTH, btnResetNbCvAnnonce, -79, SpringLayout.SOUTH, panel1);
		btnResetNbCvAnnonce.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				formattedTextFieldNbCvAnnonce.setText("");
				formattedTextFieldNbCvAnnonce.setEnabled(true);
				formattedTextFieldNbCvAnnonce.setBorder(new JFormattedTextField().getBorder());
				nombreCvParAnnonce = -1;
			}
		});
		panel1.add(btnResetNbCvAnnonce);
		
		
		JScrollPane scrollPanelListeLM = new JScrollPane();
		sl_panel1.putConstraint(SpringLayout.WEST, scrollPanelListeLM, 524, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, scrollPanelListeLM, -110, SpringLayout.EAST, panel1);
		panel1.add(scrollPanelListeLM);
		
		
		JPanel panelOptions = new JPanel();
		sl_panel1.putConstraint(SpringLayout.SOUTH, btnRandomisation, 0, SpringLayout.SOUTH, panelOptions);
		sl_panel1.putConstraint(SpringLayout.EAST, btnParcourirOutput, -53, SpringLayout.WEST, panelOptions);
		sl_panel1.putConstraint(SpringLayout.NORTH, panelOptions, -181, SpringLayout.SOUTH, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, panelOptions, 524, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, panelOptions, -21, SpringLayout.SOUTH, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, panelOptions, -60, SpringLayout.EAST, panel1);
		panelOptions.setVisible(false);
		panelOptions.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel1.add(panelOptions);
		SpringLayout sl_panelOptions = new SpringLayout();
		panelOptions.setLayout(sl_panelOptions);
		
		JLabel lblSeedDeGnration = new JLabel("<html>\r\nGraine de génération aléatoire<sup>?</sup> :\r\n</html>");
		lblSeedDeGnration.setToolTipText("Seed utilisée pour le mélange aléatoire. Si le champ est vide une seed sera utilisée au hasard.");
		panelOptions.add(lblSeedDeGnration);
		
		
		/*CheckBox pour la liaison entre les tableaus de CV et de LM*/
		JCheckBox chckbxLiaisonCV_LM = new JCheckBox("<html>\r\nLiaison entre les tableaux de CV et LM<sup>?</sup>\r\n</html>");
		sl_panelOptions.putConstraint(SpringLayout.NORTH, chckbxLiaisonCV_LM, 32, SpringLayout.SOUTH, lblSeedDeGnration);
		sl_panelOptions.putConstraint(SpringLayout.WEST, chckbxLiaisonCV_LM, 30, SpringLayout.WEST, panelOptions);
		chckbxLiaisonCV_LM.setToolTipText("<html>\r\nLie  les CV et LM des 2 tableaux : le CV et la LM d'une même ligne seront toujours créés ensembles.<br>\r\n<br>\r\nle CV dans la première ligne de la liste sera créé avec la LM dans la première ligne de la liste,<br>\r\nle CV dans la deuxième ligne de la liste sera créé avec la LM dans la deuxième ligne de la liste,<br>\r\n...\r\n<ul>\r\n<li>Coché : CV et LM liés </li>\r\n<li>Décoché : CV et LM <b>non liés</b> </li>\r\n<ul>\r\n</html>");
		chckbxLiaisonCV_LM.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SPACE){
					liaisonCV_LM = !liaisonCV_LM;
					System.out.println("liaison:"+liaisonCV_LM);
				}
			}
		});
		chckbxLiaisonCV_LM.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				liaisonCV_LM = !liaisonCV_LM;
				System.out.println("liaison:"+liaisonCV_LM);
			}
		});
		panelOptions.add(chckbxLiaisonCV_LM);
		
		JCheckBox chckbxMmeQualitPour = new JCheckBox("<html>\r\nMême type pour les CV et LM d'une même annonce<sup>?</sup>\r\n</html>");
		sl_panelOptions.putConstraint(SpringLayout.NORTH, chckbxMmeQualitPour, 22, SpringLayout.SOUTH, chckbxLiaisonCV_LM);
		sl_panelOptions.putConstraint(SpringLayout.WEST, chckbxMmeQualitPour, 30, SpringLayout.WEST, panelOptions);
		chckbxMmeQualitPour.setToolTipText("<html>\r\nSi vous utilisez des templates de différents type,<br>\r\ncochez cette case si vous voulez que tout les CV et LM d'une même annonce soient du même type.\r\n</html>");
		chckbxMmeQualitPour.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SPACE){
					annonceMemeQualite = !annonceMemeQualite;
					System.out.println("annonceMemeQualite:"+annonceMemeQualite);
				}
			}
		});
		chckbxMmeQualitPour.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				annonceMemeQualite = !annonceMemeQualite;
				System.out.println("annonceMemeQualite:"+annonceMemeQualite);
			}
		});
		panelOptions.add(chckbxMmeQualitPour);
		
		JFormattedTextField formattedTextFieldSeed = new JFormattedTextField(formatter);
		sl_panelOptions.putConstraint(SpringLayout.NORTH, formattedTextFieldSeed, 16, SpringLayout.NORTH, panelOptions);
		sl_panelOptions.putConstraint(SpringLayout.WEST, formattedTextFieldSeed, 211, SpringLayout.WEST, panelOptions);
		sl_panelOptions.putConstraint(SpringLayout.NORTH, lblSeedDeGnration, 1, SpringLayout.NORTH, formattedTextFieldSeed);
		sl_panelOptions.putConstraint(SpringLayout.EAST, lblSeedDeGnration, -6, SpringLayout.WEST, formattedTextFieldSeed);
		panelOptions.add(formattedTextFieldSeed);
		
		JButton btnValiderSeed = new JButton("Valider");
		sl_panelOptions.putConstraint(SpringLayout.WEST, btnValiderSeed, 317, SpringLayout.WEST, panelOptions);
		sl_panelOptions.putConstraint(SpringLayout.EAST, formattedTextFieldSeed, -6, SpringLayout.WEST, btnValiderSeed);
		btnValiderSeed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(formattedTextFieldSeed.getValue() != null){
					seed = (long)(int)formattedTextFieldSeed.getValue();
					formattedTextFieldSeed.setEnabled(false);
					formattedTextFieldSeed.setBorder(new LineBorder(Color.decode("#5dc35d")));
				}
			}
		});
		sl_panelOptions.putConstraint(SpringLayout.NORTH, btnValiderSeed, 0, SpringLayout.NORTH, formattedTextFieldSeed);
		panelOptions.add(btnValiderSeed);
		
		JButton btnResetSeed = new JButton("Reset");
		btnResetSeed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				formattedTextFieldSeed.setText("");
				formattedTextFieldSeed.setEnabled(true);
				formattedTextFieldSeed.setBorder(new JFormattedTextField().getBorder());
				seed = -1;
			}
		});
		sl_panelOptions.putConstraint(SpringLayout.NORTH, btnResetSeed, 0, SpringLayout.NORTH, formattedTextFieldSeed);
		sl_panelOptions.putConstraint(SpringLayout.WEST, btnResetSeed, 6, SpringLayout.EAST, btnValiderSeed);
		panelOptions.add(btnResetSeed);
		
		
		JLabel lblOptions = new JLabel("<html><u>Options +__________</u></html>");
		sl_panel1.putConstraint(SpringLayout.SOUTH, scrollPanelListeLM, -8, SpringLayout.NORTH, lblOptions);
		sl_panel1.putConstraint(SpringLayout.WEST, lblOptions, 54, SpringLayout.EAST, btnParcourirOutput);
		sl_panel1.putConstraint(SpringLayout.SOUTH, lblOptions, -187, SpringLayout.SOUTH, panel1);
		lblOptions.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblOptions.setToolTipText("Choix additionels");
		lblOptions.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(lblOptions.getText().compareTo("<html><u>Options +__________</u></html>") == 0){
					panelOptions.setVisible(true);
					lblOptions.setText("<html>Options - </html>");
					lblOptions.setBorder(BorderFactory.createDashedBorder(new Color(0, 0, 0),5,3));
				}
				else{
					panelOptions.setVisible(false);
					lblOptions.setText("<html><u>Options +__________</u></html>");
					lblOptions.setBorder(new LineBorder(new Color(0, 0, 0)));
				}
			}
		});
		panel1.add(lblOptions);
		
		
		/*tableau des LM*/
		modelLM = new DefaultTableModel(
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
		
		tableLM = new JTable();
		tableLM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableLM.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				caseSelectTableauLM = tableLM.getSelectedRow();
			}
		});
		tableLM.setModel(modelLM);
		tableLM.getColumnModel().getColumn(2).setMaxWidth(150);
		tableLM.getColumnModel().getColumn(2).setMinWidth(100);
		tableLM.setRowHeight(30);
		/*Action de bouton supprimer du tableau*/
		Action deleteLM = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        ((DefaultTableModel)table.getModel()).removeRow(modelRow);
		        templatesLM.remove(modelRow);
		    }
		};
		@SuppressWarnings("unused")
		ButtonColumn buttonColumnLM = new ButtonColumn(tableLM, deleteLM, 2);
		
		scrollPanelListeLM.setViewportView(tableLM);
		
		
		JLabel lblTemplatesDeLm = new JLabel("<html>\r\nTemplates de LM (.doc)<sup>?</sup> :\r\n</html>");
		sl_panel1.putConstraint(SpringLayout.EAST, btnParcourirTemplate, -53, SpringLayout.WEST, lblTemplatesDeLm);
		sl_panel1.putConstraint(SpringLayout.NORTH, lblTemplatesDeLm, 0, SpringLayout.NORTH, lblTemplatesDeCv);
		sl_panel1.putConstraint(SpringLayout.WEST, lblTemplatesDeLm, 524, SpringLayout.WEST, panel1);
		lblTemplatesDeLm.setToolTipText("Template de base des lettres de motivation au format word (.doc)");
		panel1.add(lblTemplatesDeLm);
		
		
		textFieldTemplateLM = new JTextField();
		sl_panel1.putConstraint(SpringLayout.EAST, lblTemplatesDeLm, -16, SpringLayout.WEST, textFieldTemplateLM);
		textFieldTemplateLM.setBackground(Color.decode("#d15050"));
		sl_panel1.putConstraint(SpringLayout.WEST, textFieldTemplateLM, 686, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, textFieldTemplateLM, -201, SpringLayout.EAST, panel1);
		panel1.add(textFieldTemplateLM);
		textFieldTemplateLM.setColumns(10);
		
		
		JButton btnParcourirTemplateLM = new JButton("Parcourir");
		sl_panel1.putConstraint(SpringLayout.WEST, btnParcourirTemplateLM, 6, SpringLayout.EAST, textFieldTemplateLM);
		sl_panel1.putConstraint(SpringLayout.SOUTH, btnParcourirTemplateLM, -46, SpringLayout.NORTH, scrollPanelListeLM);
		btnParcourirTemplateLM.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Explorer explorer = new Explorer("MULTIPLE_FILES");
				
				templateNamesLM = explorer.getFilenames();
				templatePathsLM = explorer.getFilepaths();
				
				if(templateNamesLM != null){
					textFieldTemplateLM.setText(templatePathsLM[templateNamesLM.length-1]);
					textFieldTemplateLM.setBackground(Color.decode("#5dc35d"));
					
					for(int i=0; i<templateNamesLM.length; i++)
						addListeLM(templateNamesLM[i],templatePathsLM[i]);
				}
			}
		});
		panel1.add(btnParcourirTemplateLM);
		
		
		JButton btnMonterLM = new JButton("Monter");
		sl_panel1.putConstraint(SpringLayout.WEST, btnMonterLM, 6, SpringLayout.EAST, scrollPanelListeLM);
		btnMonterLM.setToolTipText("Monte la lettre de motivation sélectionnée d'une ligne. (Raccourcis : ALT+E)");
		btnMonterLM.setMnemonic(KeyEvent.VK_E);
		btnMonterLM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("avant:"+templatesLM+"    - SIZE:"+templatesLM.size()+"  caseSelectTableauLM:"+caseSelectTableauLM);
				if(caseSelectTableauLM > 0){
					/*changement de l'arraylist*/
					Template t1 = templatesLM.get(caseSelectTableauLM);
					templatesLM.set(caseSelectTableauLM, templatesLM.get(caseSelectTableauLM-1));
					templatesLM.set(caseSelectTableauLM-1, t1);
					
					/*changement de la Jtable*/
					
					modelLM.moveRow(caseSelectTableauLM, caseSelectTableauLM, caseSelectTableauLM-1);
					//modelLM.addRow(new Object[]{name, path, "Supprimer"});
				}
				
				if(caseSelectTableauLM > 0){
					tableLM.changeSelection(caseSelectTableauLM-1, caseSelectTableauLM-1, false, false);
					caseSelectTableauLM--;
				}
				System.out.println("apres:"+templatesLM+"    - SIZE:"+templatesLM.size()+"  caseSelectTableauLM:"+caseSelectTableauLM+"\n");
			}
		});
		panel1.add(btnMonterLM);
		
		
		JButton btnDescendreLM = new JButton("Descendre");
		sl_panel1.putConstraint(SpringLayout.NORTH, btnDescendreLM, 300, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, btnDescendreLM, 6, SpringLayout.EAST, scrollPanelListeLM);
		sl_panel1.putConstraint(SpringLayout.SOUTH, btnMonterLM, -22, SpringLayout.NORTH, btnDescendreLM);
		btnDescendreLM.setToolTipText("Descend la lettre de motivation sélectionnée d'une ligne. (Raccourcis : ALT+D)");
		btnDescendreLM.setMnemonic(KeyEvent.VK_D);
		btnDescendreLM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("avant:"+templatesLM+"    - SIZE:"+templatesLM.size()+"  caseSelectTableauLM:"+caseSelectTableauLM);
				if(caseSelectTableauLM < templatesLM.size()-1 && caseSelectTableauLM >= 0){
					/*changement de l'arraylist*/
					Template t1 = templatesLM.get(caseSelectTableauLM);
					templatesLM.set(caseSelectTableauLM, templatesLM.get(caseSelectTableauLM+1));
					templatesLM.set(caseSelectTableauLM+1, t1);
					
					/*changement de la Jtable*/
					
					modelLM.moveRow(caseSelectTableauLM, caseSelectTableauLM, caseSelectTableauLM+1);
					//modelLM.addRow(new Object[]{name, path, "Supprimer"});
				}
				
				if(caseSelectTableauLM < templatesLM.size()-1){
					tableLM.changeSelection(caseSelectTableauLM+1, caseSelectTableauLM+1, false, false);
					caseSelectTableauLM++;
				}
				
				System.out.println("apres:"+templatesLM+"    - SIZE:"+templatesLM.size()+"  caseSelectTableauLM:"+caseSelectTableauLM+"\n");
			}
		});
		panel1.add(btnDescendreLM);
		
		
		JLabel lblListeDesLettres = new JLabel("Liste des lettres de motivation :");
		sl_panel1.putConstraint(SpringLayout.SOUTH, textFieldTemplateLM, -26, SpringLayout.NORTH, lblListeDesLettres);
		sl_panel1.putConstraint(SpringLayout.SOUTH, lblListeDesTemplates, 16, SpringLayout.NORTH, lblListeDesLettres);
		sl_panel1.putConstraint(SpringLayout.NORTH, scrollPanelListeLM, 4, SpringLayout.SOUTH, lblListeDesLettres);
		sl_panel1.putConstraint(SpringLayout.WEST, lblListeDesLettres, 527, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, lblListeDesTemplates, -293, SpringLayout.WEST, lblListeDesLettres);
		sl_panel1.putConstraint(SpringLayout.NORTH, lblListeDesTemplates, 0, SpringLayout.NORTH, lblListeDesLettres);
		sl_panel1.putConstraint(SpringLayout.NORTH, lblListeDesLettres, 163, SpringLayout.NORTH, panel1);
		panel1.add(lblListeDesLettres);
		
		JLabel lblPage1Title = new JLabel("<html>\r\n<b>Données</b> -> Résultat\r\n<html>");
		sl_panel1.putConstraint(SpringLayout.NORTH, btnParcourirExcel, 25, SpringLayout.SOUTH, lblPage1Title);
		sl_panel1.putConstraint(SpringLayout.NORTH, lblPage1Title, 10, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, lblPage1Title, 450, SpringLayout.WEST, panel1);
		lblPage1Title.setFont(new Font("SansSerif", Font.PLAIN, 14));
		panel1.add(lblPage1Title);
		

		
		

		
		
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
		
		
		JButton btnGnrer = new JButton("<html>\r\n<center>\r\nGénérer<br>\r\nles CV\r\n</center>\r\n</html>");
		btnGnrer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Et là les CVs sont créés");
				try {
					testing.create(nombreAnnonces, nombreCvParAnnonce,seed);
					Point p = frmTer.getLocation();
					Popup.pop(p,"Création des CV et LM terminée !");
				} catch (IOException e1) {
					//Auto-generated catch block
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
		
		JLabel lblPage2Title = new JLabel("<html>\r\nDonnées -> <b>Résultat</b>\r\n<html>");
		lblPage2Title.setFont(new Font("SansSerif", Font.PLAIN, 14));
		sl_panel2.putConstraint(SpringLayout.NORTH, lblPage2Title, 10, SpringLayout.NORTH, panel2);
		sl_panel2.putConstraint(SpringLayout.WEST, lblPage2Title, 450, SpringLayout.WEST, panel2);
		panel2.add(lblPage2Title);
		
		JButton btnGnrer_1 = new JButton("<html>\r\n<center>\r\nGénérer<br>\r\nles PDF\r\n</center>\r\n</html>");
		btnGnrer_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				testing.createPdf();
			}
		});
		sl_panel2.putConstraint(SpringLayout.NORTH, btnGnrer_1, 0, SpringLayout.NORTH, btnPrcdent);
		sl_panel2.putConstraint(SpringLayout.WEST, btnGnrer_1, -158, SpringLayout.WEST, btnGnrer);
		sl_panel2.putConstraint(SpringLayout.SOUTH, btnGnrer_1, 0, SpringLayout.SOUTH, btnPrcdent);
		sl_panel2.putConstraint(SpringLayout.EAST, btnGnrer_1, -43, SpringLayout.WEST, btnGnrer);
		panel2.add(btnGnrer_1);
		/*TODO bouton caché car convertion en pdf non fonctionnelle*/
		btnGnrer_1.setVisible(false);
		
		

		
	}
	
	
	/**Fonction qui ajoute les templates de CV au tableau**/
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
	
	/**Fonction qui ajoute les templates de LM au tableau**/
	public void addListeLM(String name, String path){
		/*Ajout a l'arrayList*/
		Template t = new Template(name,path);
		templatesLM.add(t);
		
		/*Ajout au GUI*/
		if(tableLM.getRowCount() == 0){
			modelLM.addRow(new Object[]{name, path, "Supprimer"});
		}
		else{
			for(int i=0; i<tableLM.getRowCount(); i++){
				//System.out.println("i="+i+" cell="+table.getValueAt(i, 0));
					
				if(tableLM.getValueAt(i, 0) == null){
					tableLM.setValueAt(name, i, 0);
					tableLM.setValueAt(path, i, 1);
					tableLM.setValueAt("Supprimer", i, 2);
					break;
				}
				else if(tableLM.getValueAt(i, 0) != null){
					//System.out.println("new row");
					modelLM.addRow(new Object[]{name, path, "Supprimer"});
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
	
	/**Fonction qui renvoie le nombre maximum de templates de même type**/
	public int nombreMemeQualite(ArrayList<Template> tabTemplates){
		int max = 0;
		int cptTab[] = new int[10];
		
		for(int i=0; i<tabTemplates.size(); i++){
			if(Character.isDigit(tabTemplates.get(i).getFilename().charAt(0))){
				String s = ""+tabTemplates.get(i).getFilename().charAt(0);
				cptTab[Integer.parseInt(s)] ++;
			}
			else
				cptTab[0] ++;
		}
		
		for(int i=0; i<cptTab.length; i++){
			System.out.println("type "+i+" : "+cptTab[i]);
			if(cptTab[i] > max)
				max = cptTab[i];
		}
		
		System.out.println("MAX="+max);
		return max;
	}
}
