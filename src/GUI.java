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
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JCheckBox;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import javax.swing.ListSelectionModel;

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
	private long seed;
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
		sl_panel1.putConstraint(SpringLayout.NORTH, btnRandomisation, 650, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, btnRandomisation, 170, SpringLayout.WEST, panel1);
		panel1.add(btnRandomisation);
		btnRandomisation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(excelPath == null){
					Point p = frmTer.getLocation();
					Popup.pop(p,"Veuillez selectionner une base de donnée (.xls)");
				}
				else if(templates.size() == 0){
					Point p = frmTer.getLocation();
					Popup.pop(p,"Veuillez selectionner un template de CV (.doc)");
				}
				else if(templatesLM.size() == 0){
					Point p = frmTer.getLocation();
					Popup.pop(p,"Veuillez selectionner un template de LM (.doc)");
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
					Popup.pop(p,"Veuillez choisir le nombre de CV par annonce.");
				} else{
					System.out.println("pre test");
					testing = new Test();
					testing.init(templates, outputFolder, excelPath);
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
		sl_panel1.putConstraint(SpringLayout.WEST, textFieldOutput, 10, SpringLayout.EAST, lblDossierDeDestination);
		sl_panel1.putConstraint(SpringLayout.EAST, textFieldOutput, 210, SpringLayout.EAST, lblDossierDeDestination);
		panel1.add(textFieldOutput);
		textFieldOutput.setEditable(false);
		textFieldOutput.setColumns(10);
		
		
		textFieldTemplate = new JTextField();
		sl_panel1.putConstraint(SpringLayout.EAST, lblTemplatesDeCv, -9, SpringLayout.WEST, textFieldTemplate);
		sl_panel1.putConstraint(SpringLayout.EAST, textFieldTemplate, 370, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, textFieldTemplate, 170, SpringLayout.WEST, panel1);
		panel1.add(textFieldTemplate);
		textFieldTemplate.setEditable(false);
		textFieldTemplate.setColumns(10);
		
		
		textFieldExcel = new JTextField();
		sl_panel1.putConstraint(SpringLayout.NORTH, textFieldTemplate, 22, SpringLayout.SOUTH, textFieldExcel);
		sl_panel1.putConstraint(SpringLayout.EAST, lblFichierExcelxsl, -17, SpringLayout.WEST, textFieldExcel);
		sl_panel1.putConstraint(SpringLayout.EAST, textFieldExcel, 370, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, textFieldExcel, 170, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.NORTH, textFieldExcel, 30, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, textFieldExcel, 58, SpringLayout.NORTH, panel1);
		panel1.add(textFieldExcel);
		textFieldExcel.setEditable(false);
		textFieldExcel.setColumns(10);
		
		
		JButton btnParcourirTemplate = new JButton("Parcourir");
		sl_panel1.putConstraint(SpringLayout.NORTH, btnParcourirTemplate, -6, SpringLayout.NORTH, lblTemplatesDeCv);
		sl_panel1.putConstraint(SpringLayout.WEST, btnParcourirTemplate, 6, SpringLayout.EAST, textFieldTemplate);
		panel1.add(btnParcourirTemplate);
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
		
		
		JButton btnParcourirExcel = new JButton("Parcourir");
		sl_panel1.putConstraint(SpringLayout.NORTH, btnParcourirExcel, -6, SpringLayout.NORTH, lblFichierExcelxsl);
		sl_panel1.putConstraint(SpringLayout.WEST, btnParcourirExcel, 6, SpringLayout.EAST, textFieldExcel);
		sl_panel1.putConstraint(SpringLayout.EAST, btnParcourirExcel, 101, SpringLayout.EAST, textFieldExcel);
		panel1.add(btnParcourirExcel);
		btnParcourirExcel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Explorer explorer = new Explorer("UNIQUE_FILE");
				excelPath = explorer.getFilepath();
				
				textFieldExcel.setText(excelPath);
			}
		});
		
		
		JButton btnParcourirOutput = new JButton("Parcourir");
		sl_panel1.putConstraint(SpringLayout.NORTH, btnParcourirOutput, -6, SpringLayout.NORTH, lblDossierDeDestination);
		sl_panel1.putConstraint(SpringLayout.WEST, btnParcourirOutput, 6, SpringLayout.EAST, textFieldOutput);
		panel1.add(btnParcourirOutput);
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
		
		
		JScrollPane scrollPanelListeTemplate = new JScrollPane();
		sl_panel1.putConstraint(SpringLayout.SOUTH, scrollPanelListeTemplate, -248, SpringLayout.SOUTH, panel1);
		sl_panel1.putConstraint(SpringLayout.NORTH, textFieldOutput, 21, SpringLayout.SOUTH, scrollPanelListeTemplate);
		sl_panel1.putConstraint(SpringLayout.NORTH, lblDossierDeDestination, 27, SpringLayout.SOUTH, scrollPanelListeTemplate);
		sl_panel1.putConstraint(SpringLayout.EAST, scrollPanelListeTemplate, 444, SpringLayout.WEST, lblFichierExcelxsl);
		sl_panel1.putConstraint(SpringLayout.WEST, scrollPanelListeTemplate, -6, SpringLayout.WEST, lblFichierExcelxsl);
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
		sl_panel1.putConstraint(SpringLayout.SOUTH, lblListeDesTemplates, -569, SpringLayout.SOUTH, panel1);
		sl_panel1.putConstraint(SpringLayout.NORTH, scrollPanelListeTemplate, 6, SpringLayout.SOUTH, lblListeDesTemplates);
		sl_panel1.putConstraint(SpringLayout.NORTH, lblListeDesTemplates, 25, SpringLayout.SOUTH, lblTemplatesDeCv);
		sl_panel1.putConstraint(SpringLayout.WEST, lblListeDesTemplates, 0, SpringLayout.WEST, lblFichierExcelxsl);
		sl_panel1.putConstraint(SpringLayout.EAST, lblListeDesTemplates, -850, SpringLayout.EAST, panel1);
		panel1.add(lblListeDesTemplates);
		
		
		JLabel lblNombreDannonces = new JLabel("Nombre d'annonces :");
		sl_panel1.putConstraint(SpringLayout.SOUTH, lblDossierDeDestination, -34, SpringLayout.NORTH, lblNombreDannonces);
		sl_panel1.putConstraint(SpringLayout.NORTH, lblNombreDannonces, 540, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, lblNombreDannonces, 212, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, lblNombreDannonces, 81, SpringLayout.WEST, panel1);
		panel1.add(lblNombreDannonces);
		
		
		JLabel lblNombreDeCv = new JLabel("Nombre de CV par annonce :");
		sl_panel1.putConstraint(SpringLayout.NORTH, lblNombreDeCv, 580, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, lblNombreDeCv, 44, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, lblNombreDeCv, -115, SpringLayout.SOUTH, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, lblNombreDannonces, -24, SpringLayout.NORTH, lblNombreDeCv);
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
		sl_panel1.putConstraint(SpringLayout.EAST, formattedTextFieldNbAnnonces, 106, SpringLayout.EAST, lblNombreDannonces);
		sl_panel1.putConstraint(SpringLayout.NORTH, formattedTextFieldNbAnnonces, 534, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, formattedTextFieldNbAnnonces, 6, SpringLayout.EAST, lblNombreDannonces);
		panel1.add(formattedTextFieldNbAnnonces);
		
		JFormattedTextField formattedTextFieldNbCvAnnonce = new JFormattedTextField(formatter);
		sl_panel1.putConstraint(SpringLayout.NORTH, formattedTextFieldNbCvAnnonce, 574, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, lblNombreDeCv, -4, SpringLayout.WEST, formattedTextFieldNbCvAnnonce);
		sl_panel1.putConstraint(SpringLayout.WEST, formattedTextFieldNbCvAnnonce, 218, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, formattedTextFieldNbCvAnnonce, 318, SpringLayout.WEST, panel1);
		panel1.add(formattedTextFieldNbCvAnnonce);
		
		
		JButton btnValiderNbAnnonces = new JButton("Valider");
		sl_panel1.putConstraint(SpringLayout.NORTH, btnValiderNbAnnonces, -6, SpringLayout.NORTH, lblNombreDannonces);
		sl_panel1.putConstraint(SpringLayout.WEST, btnValiderNbAnnonces, 6, SpringLayout.EAST, formattedTextFieldNbAnnonces);
		btnValiderNbAnnonces.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(formattedTextFieldNbAnnonces.getValue() != null){
					nombreAnnonces = (int)formattedTextFieldNbAnnonces.getValue();
					formattedTextFieldNbAnnonces.setEnabled(false);
				}
			}
		});
		panel1.add(btnValiderNbAnnonces);
		
		
		JButton btnValiderNbCvAnnonce = new JButton("Valider");
		sl_panel1.putConstraint(SpringLayout.WEST, btnValiderNbCvAnnonce, 324, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.NORTH, btnValiderNbCvAnnonce, -6, SpringLayout.NORTH, lblNombreDeCv);
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
		sl_panel1.putConstraint(SpringLayout.NORTH, btnResetNbAnnonces, -6, SpringLayout.NORTH, lblNombreDannonces);
		sl_panel1.putConstraint(SpringLayout.WEST, btnResetNbAnnonces, 6, SpringLayout.EAST, btnValiderNbAnnonces);
		btnResetNbAnnonces.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				formattedTextFieldNbAnnonces.setText("");
				formattedTextFieldNbAnnonces.setEnabled(true);
				nombreAnnonces = -1;
			}
		});
		panel1.add(btnResetNbAnnonces);
		
		
		JButton btnResetNbCvAnnonce = new JButton("Reset");
		sl_panel1.putConstraint(SpringLayout.NORTH, btnResetNbCvAnnonce, -6, SpringLayout.NORTH, lblNombreDeCv);
		sl_panel1.putConstraint(SpringLayout.WEST, btnResetNbCvAnnonce, 6, SpringLayout.EAST, btnValiderNbCvAnnonce);
		btnResetNbCvAnnonce.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				formattedTextFieldNbCvAnnonce.setText("");
				formattedTextFieldNbCvAnnonce.setEnabled(true);
				nombreCvParAnnonce = -1;
			}
		});
		panel1.add(btnResetNbCvAnnonce);
		
		
		JScrollPane scrollPanelListeLM = new JScrollPane();
		sl_panel1.putConstraint(SpringLayout.WEST, scrollPanelListeLM, 54, SpringLayout.EAST, scrollPanelListeTemplate);
		sl_panel1.putConstraint(SpringLayout.EAST, scrollPanelListeLM, 504, SpringLayout.EAST, scrollPanelListeTemplate);
		panel1.add(scrollPanelListeLM);
		
		
		JPanel panelOptions = new JPanel();
		panelOptions.setVisible(false);
		sl_panel1.putConstraint(SpringLayout.EAST, btnParcourirOutput, -52, SpringLayout.WEST, panelOptions);
		sl_panel1.putConstraint(SpringLayout.NORTH, panelOptions, 506, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, panelOptions, -45, SpringLayout.SOUTH, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, panelOptions, 567, SpringLayout.EAST, btnResetNbAnnonces);
		sl_panel1.putConstraint(SpringLayout.WEST, panelOptions, 67, SpringLayout.EAST, btnResetNbAnnonces);
		panelOptions.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel1.add(panelOptions);
		SpringLayout sl_panelOptions = new SpringLayout();
		panelOptions.setLayout(sl_panelOptions);
		
		JLabel lblSeedDeGnration = new JLabel("Seed de génération :");
		sl_panelOptions.putConstraint(SpringLayout.NORTH, lblSeedDeGnration, 22, SpringLayout.NORTH, panelOptions);
		sl_panelOptions.putConstraint(SpringLayout.WEST, lblSeedDeGnration, 26, SpringLayout.WEST, panelOptions);
		panelOptions.add(lblSeedDeGnration);
		
		
		/*CheckBox pour la liaison entre les tableaus de CV et de LM*/
		JCheckBox chckbxLiaisonCV_LM = new JCheckBox("Liaison CV - LM ");
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
		
		JCheckBox chckbxMmeQualitPour = new JCheckBox("Même qualité pour les CV et LM d'une même annonce");
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
		sl_panelOptions.putConstraint(SpringLayout.EAST, chckbxMmeQualitPour, -8, SpringLayout.EAST, panelOptions);
		sl_panelOptions.putConstraint(SpringLayout.WEST, chckbxLiaisonCV_LM, 0, SpringLayout.WEST, chckbxMmeQualitPour);
		sl_panelOptions.putConstraint(SpringLayout.SOUTH, chckbxLiaisonCV_LM, -6, SpringLayout.NORTH, chckbxMmeQualitPour);
		sl_panelOptions.putConstraint(SpringLayout.SOUTH, chckbxMmeQualitPour, -37, SpringLayout.SOUTH, panelOptions);
		panelOptions.add(chckbxMmeQualitPour);
		
		JFormattedTextField formattedTextFieldSeed = new JFormattedTextField(formatter);
		sl_panelOptions.putConstraint(SpringLayout.NORTH, formattedTextFieldSeed, -6, SpringLayout.NORTH, lblSeedDeGnration);
		sl_panelOptions.putConstraint(SpringLayout.WEST, formattedTextFieldSeed, 6, SpringLayout.EAST, lblSeedDeGnration);
		sl_panelOptions.putConstraint(SpringLayout.EAST, formattedTextFieldSeed, 106, SpringLayout.EAST, lblSeedDeGnration);
		panelOptions.add(formattedTextFieldSeed);
		
		JButton btnValiderSeed = new JButton("Valider");
		btnValiderSeed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(formattedTextFieldSeed.getValue() != null){
					seed = (long)(int)formattedTextFieldSeed.getValue();
					formattedTextFieldSeed.setEnabled(false);
				}
			}
		});
		sl_panelOptions.putConstraint(SpringLayout.NORTH, btnValiderSeed, 0, SpringLayout.NORTH, formattedTextFieldSeed);
		sl_panelOptions.putConstraint(SpringLayout.WEST, btnValiderSeed, 6, SpringLayout.EAST, formattedTextFieldSeed);
		panelOptions.add(btnValiderSeed);
		
		JButton btnResetSeed = new JButton("Reset");
		btnResetSeed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				formattedTextFieldSeed.setText("");
				formattedTextFieldSeed.setEnabled(true);
				seed = -1;
			}
		});
		sl_panelOptions.putConstraint(SpringLayout.NORTH, btnResetSeed, 0, SpringLayout.NORTH, formattedTextFieldSeed);
		sl_panelOptions.putConstraint(SpringLayout.WEST, btnResetSeed, 6, SpringLayout.EAST, btnValiderSeed);
		panelOptions.add(btnResetSeed);
		
		
		JLabel lblOptions = new JLabel("<html><u>Options +__________</u></html>");
		lblOptions.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(lblOptions.getText().compareTo("<html><u>Options +__________</u></html>") == 0){
					panelOptions.setVisible(true);
					lblOptions.setText("<html>Options -</html>");
				}
				else{
					panelOptions.setVisible(false);
					lblOptions.setText("<html><u>Options +__________</u></html>");
				}
			}
		});
		sl_panel1.putConstraint(SpringLayout.SOUTH, scrollPanelListeLM, -21, SpringLayout.NORTH, lblOptions);
		sl_panel1.putConstraint(SpringLayout.NORTH, lblOptions, 484, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, lblOptions, 52, SpringLayout.EAST, btnParcourirOutput);
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
				System.out.println("olol:"+tableLM.getSelectedRow());
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
		
		
		JLabel lblTemplatesDeLm = new JLabel("Templates de LM (.doc) :");
		sl_panel1.putConstraint(SpringLayout.SOUTH, lblTemplatesDeLm, -47, SpringLayout.NORTH, scrollPanelListeLM);
		sl_panel1.putConstraint(SpringLayout.EAST, btnParcourirTemplate, -54, SpringLayout.WEST, lblTemplatesDeLm);
		sl_panel1.putConstraint(SpringLayout.WEST, lblTemplatesDeLm, 525, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, lblTemplatesDeLm, -343, SpringLayout.EAST, panel1);
		panel1.add(lblTemplatesDeLm);
		
		
		textFieldTemplateLM = new JTextField();
		sl_panel1.putConstraint(SpringLayout.WEST, textFieldTemplateLM, 671, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, textFieldTemplateLM, -41, SpringLayout.NORTH, scrollPanelListeLM);
		sl_panel1.putConstraint(SpringLayout.EAST, textFieldTemplateLM, 871, SpringLayout.WEST, panel1);
		panel1.add(textFieldTemplateLM);
		textFieldTemplateLM.setColumns(10);
		
		
		JButton btnParcourirTemplateLM = new JButton("Parcourir");
		btnParcourirTemplateLM.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Explorer explorer = new Explorer("MULTIPLE_FILES");
				
				templateNamesLM = explorer.getFilenames();
				templatePathsLM = explorer.getFilepaths();
				
				if(templateNamesLM != null){
					textFieldTemplateLM.setText(templatePathsLM[templateNamesLM.length-1]);
				
					for(int i=0; i<templateNamesLM.length; i++)
						addListeLM(templateNamesLM[i],templatePathsLM[i]);
				}
			}
		});
		sl_panel1.putConstraint(SpringLayout.NORTH, btnParcourirTemplateLM, 80, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, btnParcourirTemplateLM, 6, SpringLayout.EAST, textFieldTemplateLM);
		panel1.add(btnParcourirTemplateLM);
		
		
		JButton btnMonterLM = new JButton("Monter");
		btnMonterLM.setMnemonic(KeyEvent.VK_E);
		btnMonterLM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("avant:"+templatesLM+"    - SIZE:"+templatesLM.size()+"  caseSelectTableauLM:"+caseSelectTableauLM);
				//TODO monter
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
		
		
		sl_panel1.putConstraint(SpringLayout.NORTH, btnMonterLM, 204, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, btnMonterLM, 6, SpringLayout.EAST, scrollPanelListeLM);
		panel1.add(btnMonterLM);
		
		
		JButton btnDescendreLM = new JButton("Descendre");
		btnDescendreLM.setMnemonic(KeyEvent.VK_D);
		btnDescendreLM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("avant:"+templatesLM+"    - SIZE:"+templatesLM.size()+"  caseSelectTableauLM:"+caseSelectTableauLM);
				//TODO monter
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
		sl_panel1.putConstraint(SpringLayout.NORTH, btnDescendreLM, 23, SpringLayout.SOUTH, btnMonterLM);
		sl_panel1.putConstraint(SpringLayout.WEST, btnDescendreLM, 6, SpringLayout.EAST, scrollPanelListeLM);
		panel1.add(btnDescendreLM);
		
		
		JLabel lblListeDesLettres = new JLabel("Liste des lettres de motivation :");
		sl_panel1.putConstraint(SpringLayout.NORTH, scrollPanelListeLM, 6, SpringLayout.SOUTH, lblListeDesLettres);
		sl_panel1.putConstraint(SpringLayout.NORTH, lblListeDesLettres, 0, SpringLayout.NORTH, lblListeDesTemplates);
		sl_panel1.putConstraint(SpringLayout.WEST, lblListeDesLettres, 295, SpringLayout.EAST, lblListeDesTemplates);
		panel1.add(lblListeDesLettres);
		

		
		

		
		
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
		if(table.getRowCount() == 0){
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
}
