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

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.UIManager;
import java.awt.CardLayout;

import javax.swing.JPanel;

public class GUI {
	
	private CardLayout cardLayout;
	private JFrame frmTer;
	
	private JTextField textFieldOutput;
	private JTextField textFieldTemplate;
	private JTextField textFieldExcel;
	
	private DefaultTableModel model;
	private JTable table;
	
	private String templateName, templatePath, outputFolder, excelPath;
	private ArrayList<Template> templates = new ArrayList<Template>();
	
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
		frmTer.setTitle("TER 2017");
		frmTer.setBounds(100, 100, 550, 720);
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
		
		
		JLabel lblFichierExcelxsl = new JLabel("Fichier excel (.xls) :");
		sl_panel1.putConstraint(SpringLayout.NORTH, lblFichierExcelxsl, 35, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, lblFichierExcelxsl, 51, SpringLayout.NORTH, panel1);
		panel1.add(lblFichierExcelxsl);
		
		
		JLabel lblDossierDeDestination = new JLabel("Dossier de destination :");
		panel1.add(lblDossierDeDestination);
		
		
		JLabel lblTemplatesDeCv = new JLabel("Templates de CV (.doc) :");
		sl_panel1.putConstraint(SpringLayout.NORTH, lblTemplatesDeCv, 34, SpringLayout.SOUTH, lblFichierExcelxsl);
		panel1.add(lblTemplatesDeCv);
		JButton btnRandomisation = new JButton("Randomisation");
		sl_panel1.putConstraint(SpringLayout.SOUTH, lblDossierDeDestination, -44, SpringLayout.NORTH, btnRandomisation);
		sl_panel1.putConstraint(SpringLayout.WEST, btnRandomisation, 70, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, btnRandomisation, -33, SpringLayout.SOUTH, panel1);
		panel1.add(btnRandomisation);
		
		
		textFieldOutput = new JTextField();
		sl_panel1.putConstraint(SpringLayout.WEST, textFieldOutput, 171, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, textFieldOutput, -37, SpringLayout.NORTH, btnRandomisation);
		sl_panel1.putConstraint(SpringLayout.EAST, lblDossierDeDestination, -12, SpringLayout.WEST, textFieldOutput);
		panel1.add(textFieldOutput);
		textFieldOutput.setEditable(false);
		textFieldOutput.setColumns(10);
		
		
		textFieldTemplate = new JTextField();
		sl_panel1.putConstraint(SpringLayout.WEST, textFieldTemplate, 170, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, lblTemplatesDeCv, -9, SpringLayout.WEST, textFieldTemplate);
		panel1.add(textFieldTemplate);
		textFieldTemplate.setEditable(false);
		textFieldTemplate.setColumns(10);
		
		
		textFieldExcel = new JTextField();
		sl_panel1.putConstraint(SpringLayout.WEST, textFieldExcel, 170, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.NORTH, textFieldTemplate, 22, SpringLayout.SOUTH, textFieldExcel);
		sl_panel1.putConstraint(SpringLayout.EAST, lblFichierExcelxsl, -17, SpringLayout.WEST, textFieldExcel);
		sl_panel1.putConstraint(SpringLayout.NORTH, textFieldExcel, 30, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, textFieldExcel, 58, SpringLayout.NORTH, panel1);
		panel1.add(textFieldExcel);
		textFieldExcel.setEditable(false);
		textFieldExcel.setColumns(10);
		
		
		JButton btnParcourirTemplate = new JButton("Parcourir");
		sl_panel1.putConstraint(SpringLayout.EAST, textFieldTemplate, -23, SpringLayout.WEST, btnParcourirTemplate);
		panel1.add(btnParcourirTemplate);
		
		
		JButton btnParcourirExcel = new JButton("Parcourir");
		sl_panel1.putConstraint(SpringLayout.NORTH, btnParcourirTemplate, 22, SpringLayout.SOUTH, btnParcourirExcel);
		sl_panel1.putConstraint(SpringLayout.WEST, btnParcourirTemplate, -95, SpringLayout.EAST, btnParcourirExcel);
		sl_panel1.putConstraint(SpringLayout.EAST, btnParcourirTemplate, 0, SpringLayout.EAST, btnParcourirExcel);
		sl_panel1.putConstraint(SpringLayout.EAST, textFieldExcel, -23, SpringLayout.WEST, btnParcourirExcel);
		sl_panel1.putConstraint(SpringLayout.EAST, btnParcourirExcel, -124, SpringLayout.EAST, panel1);
		sl_panel1.putConstraint(SpringLayout.NORTH, btnParcourirExcel, 30, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, btnParcourirExcel, 58, SpringLayout.NORTH, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, btnParcourirExcel, -219, SpringLayout.EAST, panel1);
		panel1.add(btnParcourirExcel);
		
		
		JButton btnParcourirOutput = new JButton("Parcourir");
		sl_panel1.putConstraint(SpringLayout.EAST, textFieldOutput, -17, SpringLayout.WEST, btnParcourirOutput);
		sl_panel1.putConstraint(SpringLayout.EAST, btnParcourirOutput, -129, SpringLayout.EAST, panel1);
		sl_panel1.putConstraint(SpringLayout.NORTH, btnParcourirOutput, -126, SpringLayout.SOUTH, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, btnParcourirOutput, -98, SpringLayout.SOUTH, panel1);
		sl_panel1.putConstraint(SpringLayout.WEST, btnParcourirOutput, -224, SpringLayout.EAST, panel1);
		panel1.add(btnParcourirOutput);
		
		
		JScrollPane scrollPanelListeTemplate = new JScrollPane();
		sl_panel1.putConstraint(SpringLayout.WEST, scrollPanelListeTemplate, 45, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, scrollPanelListeTemplate, -39, SpringLayout.NORTH, textFieldOutput);
		sl_panel1.putConstraint(SpringLayout.EAST, scrollPanelListeTemplate, -39, SpringLayout.EAST, panel1);
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
		
		
		JLabel lblListeDesTemplates = new JLabel("Liste des templates");
		sl_panel1.putConstraint(SpringLayout.NORTH, scrollPanelListeTemplate, 4, SpringLayout.SOUTH, lblListeDesTemplates);
		sl_panel1.putConstraint(SpringLayout.NORTH, lblListeDesTemplates, 45, SpringLayout.SOUTH, lblTemplatesDeCv);
		sl_panel1.putConstraint(SpringLayout.SOUTH, lblListeDesTemplates, 61, SpringLayout.SOUTH, lblTemplatesDeCv);
		sl_panel1.putConstraint(SpringLayout.WEST, lblListeDesTemplates, 49, SpringLayout.WEST, panel1);
		panel1.add(lblListeDesTemplates);
		
		
		JButton btnAjouter = new JButton("Ajouter");
		sl_panel1.putConstraint(SpringLayout.NORTH, btnAjouter, 0, SpringLayout.NORTH, textFieldTemplate);
		sl_panel1.putConstraint(SpringLayout.WEST, btnAjouter, 6, SpringLayout.EAST, btnParcourirTemplate);
		sl_panel1.putConstraint(SpringLayout.EAST, btnAjouter, -23, SpringLayout.EAST, panel1);
		panel1.add(btnAjouter);
		
		
		gotoP2 = new JButton("Afficher random");
		gotoP2.setEnabled(false);
		sl_panel1.putConstraint(SpringLayout.NORTH, gotoP2, 0, SpringLayout.NORTH, btnRandomisation);
		sl_panel1.putConstraint(SpringLayout.WEST, gotoP2, 0, SpringLayout.WEST, btnParcourirTemplate);
		sl_panel1.putConstraint(SpringLayout.EAST, gotoP2, -69, SpringLayout.EAST, panel1);
		gotoP2.setName("btnNext");
		gotoP2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(gotoP2.isEnabled())
					cardLayout.show(frmTer.getContentPane(),"PANEL2");
			}
		});
		panel1.add(gotoP2);
		
		
		btnAjouter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addListeCV(templateName,templatePath);
			}
		});
		
		
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
				Explorer explorer = new Explorer();
				excelPath = explorer.getFilepath();
				
				textFieldExcel.setText(excelPath);
			}
		});
		
		
		/*Bouton parcourir pour les templates de CV*/
		btnParcourirTemplate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Explorer explorer = new Explorer();
				templateName = explorer.getFilename();
				templatePath = explorer.getFilepath();
				
				
				textFieldTemplate.setText(templatePath);
				
				//System.out.println("nom fichier récupéré = "+templateName);
				//System.out.println("chemin fichier récupéré = "+templatePath);
			}
		});
		
		
		/*Bouton pour Randomisations*/
		btnRandomisation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				/*TODO mettre les verif dans une fonction a part qui renvoie un boolean*/
				if(excelPath == null){
					Point p = frmTer.getLocation();
					Popup.pop(p,"Veuillez selectionner un fichier excel (.xls)");
				}
				else if(templates.size() == 0){
					Point p = frmTer.getLocation();
					Popup.pop(p,"Veuillez selectionner un template de CV (.doc)");
				}
				else if(outputFolder == null){
					Point p = frmTer.getLocation();
					Popup.pop(p,"Veuillez selectionner un dossier de sortie.");
				}
				else{
					testing = new Test();
					testing.init(templates, outputFolder, excelPath);
					String[][] t = null;
					try {
						t = testing.generate();
					} catch (EncryptedDocumentException | InvalidFormatException | IOException e1) {
						//TODOAuto-generated catch block
						e1.printStackTrace();
					}
					createTableRandom(t);
					Point p = frmTer.getLocation();
					Popup.pop(p,"TODO : choix nb offres et choix nb CVs/offre");
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
		
		
		JLabel lblExcelRandomis = new JLabel("Excel randomisé");
		sl_panel2.putConstraint(SpringLayout.NORTH, lblExcelRandomis, 63, SpringLayout.NORTH, panel2);
		sl_panel2.putConstraint(SpringLayout.WEST, lblExcelRandomis, 30, SpringLayout.WEST, panel2);
		sl_panel2.putConstraint(SpringLayout.SOUTH, lblExcelRandomis, 79, SpringLayout.NORTH, panel2);
		sl_panel2.putConstraint(SpringLayout.EAST, lblExcelRandomis, 145, SpringLayout.WEST, panel2);
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
		
		
		JButton btnGnrer = new JButton("Générer");
		btnGnrer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//TODO decommenter 
				System.out.println("Et là les CVs sont créés");
				try {
					testing.create();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
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
