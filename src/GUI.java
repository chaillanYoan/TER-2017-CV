import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.UIManager;

public class GUI {

	private JFrame frmTer;
	private JTextField textFieldOutput;
	private JTextField textFieldTemplate;
	private JTextField textFieldExcel;
	private DefaultTableModel model;
	private JTable table;
	private String templateName, templatePath, templateOutput;

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
		frmTer = new JFrame();
		frmTer.setTitle("TER 2017");
		frmTer.setResizable(false);
		frmTer.setBounds(100, 100, 550, 720);
		frmTer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frmTer.getContentPane().setLayout(springLayout);
		
		
		JLabel lblFichierExcelxsl = new JLabel("Fichier excel (.xsl) :");
		frmTer.getContentPane().add(lblFichierExcelxsl);
		
		
		JLabel lblTemplatesDeCv = new JLabel("Templates de CV (.doc) :");
		springLayout.putConstraint(SpringLayout.SOUTH, lblFichierExcelxsl, -36, SpringLayout.NORTH, lblTemplatesDeCv);
		springLayout.putConstraint(SpringLayout.EAST, lblFichierExcelxsl, 0, SpringLayout.EAST, lblTemplatesDeCv);
		springLayout.putConstraint(SpringLayout.NORTH, lblTemplatesDeCv, 130, SpringLayout.NORTH, frmTer.getContentPane());
		frmTer.getContentPane().add(lblTemplatesDeCv);
		
		
		JLabel lblDossierDeDestination = new JLabel("Dossier de destination :");
		springLayout.putConstraint(SpringLayout.EAST, lblTemplatesDeCv, 0, SpringLayout.EAST, lblDossierDeDestination);
		springLayout.putConstraint(SpringLayout.WEST, lblDossierDeDestination, 20, SpringLayout.WEST, frmTer.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblDossierDeDestination, -167, SpringLayout.SOUTH, frmTer.getContentPane());
		frmTer.getContentPane().add(lblDossierDeDestination);
		
		
		JButton btnGnrerLesCvs = new JButton("Générer les CVs");
		springLayout.putConstraint(SpringLayout.NORTH, btnGnrerLesCvs, 593, SpringLayout.NORTH, frmTer.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnGnrerLesCvs, 163, SpringLayout.WEST, frmTer.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, btnGnrerLesCvs, -47, SpringLayout.SOUTH, frmTer.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnGnrerLesCvs, -185, SpringLayout.EAST, frmTer.getContentPane());
		frmTer.getContentPane().add(btnGnrerLesCvs);
		
		
		textFieldOutput = new JTextField();
		textFieldOutput.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, textFieldOutput, -3, SpringLayout.NORTH, lblDossierDeDestination);
		springLayout.putConstraint(SpringLayout.WEST, textFieldOutput, 6, SpringLayout.EAST, lblDossierDeDestination);
		springLayout.putConstraint(SpringLayout.EAST, textFieldOutput, 176, SpringLayout.EAST, lblDossierDeDestination);
		frmTer.getContentPane().add(textFieldOutput);
		textFieldOutput.setColumns(10);
		
		
		textFieldTemplate = new JTextField();
		textFieldTemplate.setEditable(false);
		springLayout.putConstraint(SpringLayout.WEST, textFieldTemplate, 6, SpringLayout.EAST, lblTemplatesDeCv);
		springLayout.putConstraint(SpringLayout.EAST, textFieldTemplate, -230, SpringLayout.EAST, frmTer.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, textFieldTemplate, -3, SpringLayout.NORTH, lblTemplatesDeCv);
		frmTer.getContentPane().add(textFieldTemplate);
		textFieldTemplate.setColumns(10);
		
		
		textFieldExcel = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, textFieldExcel, 6, SpringLayout.EAST, lblFichierExcelxsl);
		springLayout.putConstraint(SpringLayout.EAST, textFieldExcel, 0, SpringLayout.EAST, textFieldTemplate);
		textFieldExcel.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, textFieldExcel, -3, SpringLayout.NORTH, lblFichierExcelxsl);
		frmTer.getContentPane().add(textFieldExcel);
		textFieldExcel.setColumns(10);
		
		
		/*Bouton parcourir pour les templates de CV*/
		JButton btnParcourirTemplate = new JButton("Parcourir");
		springLayout.putConstraint(SpringLayout.NORTH, btnParcourirTemplate, -6, SpringLayout.NORTH, lblTemplatesDeCv);
		springLayout.putConstraint(SpringLayout.WEST, btnParcourirTemplate, 16, SpringLayout.EAST, textFieldTemplate);
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
		frmTer.getContentPane().add(btnParcourirTemplate);
		
		
		/*Bouton parcourir pour le Excel .xls*/
		JButton btnParcourirExcel = new JButton("Parcourir");
		btnParcourirExcel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String path = new String();
				Explorer explorer = new Explorer();
				path = explorer.getFilepath();
				
				textFieldExcel.setText(path);
			}
		});
		springLayout.putConstraint(SpringLayout.EAST, btnParcourirTemplate, 0, SpringLayout.EAST, btnParcourirExcel);
		springLayout.putConstraint(SpringLayout.WEST, btnParcourirExcel, 16, SpringLayout.EAST, textFieldExcel);
		springLayout.putConstraint(SpringLayout.EAST, btnParcourirExcel, -114, SpringLayout.EAST, frmTer.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, btnParcourirExcel, -4, SpringLayout.NORTH, lblFichierExcelxsl);
		frmTer.getContentPane().add(btnParcourirExcel);
		
		
		JButton btnParcourirOutput = new JButton("Parcourir");
		btnParcourirOutput.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String folder = new String();
				Explorer explorer = new Explorer("DIRECTORY");
				folder = explorer.getFilepath();
				templateOutput = folder;
						
				textFieldOutput.setText(folder);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnParcourirOutput, 0, SpringLayout.NORTH, textFieldOutput);
		springLayout.putConstraint(SpringLayout.WEST, btnParcourirOutput, 0, SpringLayout.WEST, btnParcourirTemplate);
		springLayout.putConstraint(SpringLayout.EAST, btnParcourirOutput, -114, SpringLayout.EAST, frmTer.getContentPane());
		frmTer.getContentPane().add(btnParcourirOutput);
		
		
		JScrollPane scrollPanelListeTemplate = new JScrollPane();
		springLayout.putConstraint(SpringLayout.WEST, scrollPanelListeTemplate, 31, SpringLayout.WEST, frmTer.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPanelListeTemplate, -222, SpringLayout.SOUTH, frmTer.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPanelListeTemplate, -33, SpringLayout.EAST, frmTer.getContentPane());
		frmTer.getContentPane().add(scrollPanelListeTemplate);
		
		
		JLabel lblListeDesTemplates = new JLabel("Liste des templates");
		springLayout.putConstraint(SpringLayout.NORTH, scrollPanelListeTemplate, 6, SpringLayout.SOUTH, lblListeDesTemplates);
		
		
		/*Tableau des templates*/
		model = new DefaultTableModel(
				new Object[][] {
					{null, null, null}
				},
				new String[] {
					"Fichier", "Chemin", ""
				}
			);
		table = new JTable(model);
		
		/*width and height*/
		table.setBounds(377, 118, 480, 300);
		int tableWidth = table.getWidth();
		table.getColumnModel().getColumn(0).setMinWidth(Math.round(tableWidth*25/100));
		table.getColumnModel().getColumn(1).setMinWidth(Math.round(tableWidth*50/100));
		table.getColumnModel().getColumn(2).setMinWidth(Math.round(tableWidth*25/100));
		table.setRowHeight(25);
		scrollPanelListeTemplate.setViewportView(table);
		
		
		springLayout.putConstraint(SpringLayout.SOUTH, lblListeDesTemplates, -488, SpringLayout.SOUTH, frmTer.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblListeDesTemplates, 106, SpringLayout.WEST, frmTer.getContentPane());
		frmTer.getContentPane().add(lblListeDesTemplates);
		
		
		JButton btnAjouter = new JButton("Ajouter");
		btnAjouter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addListeCV(templateName,templatePath);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnAjouter, -6, SpringLayout.NORTH, lblTemplatesDeCv);
		springLayout.putConstraint(SpringLayout.WEST, btnAjouter, 6, SpringLayout.EAST, btnParcourirTemplate);
		frmTer.getContentPane().add(btnAjouter);
	}
	
	
	/**Fonction qui ajoute les templates au tableau**/
	public void addListeCV(String name, String path){
			
		for(int i=0; i<table.getRowCount(); i++){
			//System.out.println("i="+i+" cell="+table.getValueAt(i, 0));
				
			if(table.getValueAt(i, 0) == null){
				table.setValueAt(name, i, 0);
				table.setValueAt(path, i, 1);
				break;
			}
			else if(table.getValueAt(i, 0) != null){
				//System.out.println("new row");
				model.addRow(new Object[]{name, path});
				break;
			}
		}
		//System.out.println("");
	}
	
	
}
