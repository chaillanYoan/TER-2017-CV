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
		JButton btnGnrerLesCvs = new JButton("Générer les CVs");
		sl_panel1.putConstraint(SpringLayout.SOUTH, lblDossierDeDestination, -44, SpringLayout.NORTH, btnGnrerLesCvs);
		sl_panel1.putConstraint(SpringLayout.WEST, btnGnrerLesCvs, 70, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, btnGnrerLesCvs, -33, SpringLayout.SOUTH, panel1);
		panel1.add(btnGnrerLesCvs);
		
		
		textFieldOutput = new JTextField();
		sl_panel1.putConstraint(SpringLayout.WEST, textFieldOutput, 171, SpringLayout.WEST, panel1);
		sl_panel1.putConstraint(SpringLayout.SOUTH, textFieldOutput, -37, SpringLayout.NORTH, btnGnrerLesCvs);
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
			);
		/*Action de bouton supprimer du tableau*/
		@SuppressWarnings("serial")
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
		
		table = new JTable(model);
		table.setBounds(377, 118, 480, 300);
		table.setRowHeight(30);
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
		
		JButton btnNext = new JButton("NEXT");
		sl_panel1.putConstraint(SpringLayout.WEST, btnNext, -219, SpringLayout.EAST, panel1);
		sl_panel1.putConstraint(SpringLayout.EAST, btnNext, -160, SpringLayout.EAST, panel1);
		btnNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout.show(frmTer.getContentPane(),"PANEL2");
			}
		});
		sl_panel1.putConstraint(SpringLayout.SOUTH, btnNext, 0, SpringLayout.SOUTH, btnGnrerLesCvs);
		panel1.add(btnNext);
		
		
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
		
		
		/*Bouton pour générer les CVs*/
		btnGnrerLesCvs.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(excelPath == null){
					Point p = frmTer.getLocation();
					Popup.pop(p,"Veuillez selectionner un fichier excel (.xls)");
				}
				else if(templates.size() == 0){
					Point p = frmTer.getLocation();
					Popup.pop(p,"Veuillez selectionner un template de CV (.doc).");
				}
				else if(outputFolder == null){
					Point p = frmTer.getLocation();
					Popup.pop(p,"Veuillez selectionner un dossier de sortie.");
				}
				else{
					Test testing = new Test();
					testing.init(templates, outputFolder, excelPath);
					try {
						testing.generate();
					} catch (EncryptedDocumentException | InvalidFormatException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		
		


		
		
		/**
		 * PANEL 2 : 
		 * -show the table the shuffled table
		 */
		
		JPanel panel2 = new JPanel();
		frmTer.getContentPane().add(panel2, "PANEL2");
		panel2.setLayout(new SpringLayout());
		
		

		
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
}
