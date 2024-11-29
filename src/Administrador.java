/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import javax.swing.JFrame;

/**
 *
 * @author Laura Sabillon
 */
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class Administrador extends JFrame {

    private Controles control = new Controles();

    private JTextField txtBuscar;
    private JPanel panelBotones;
    private JComboBox<String> crearCombo, ordenarCombo;
    private JButton bnNombre, bnCopi, bnPaste;
    private JTree jTree1;
    private JList<String> jList1;
    private DefaultListModel<String> listModel;
    private JScrollPane jScrollPane1, jScrollPane2;
    private DefaultMutableTreeNode rootNode;

    public Administrador() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Organizador");

        setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.insets = new Insets(10, 10, 10, 10);
        File srcDir = new File(System.getProperty("user.dir"));
        rootNode = crearNodo(srcDir);
        txtBuscar = new JTextField("Buscar");
        grid.gridx = 0;
        grid.gridy = 0;
        grid.gridwidth = 2;
        grid.fill = GridBagConstraints.HORIZONTAL;
        txtBuscar.setPreferredSize(new Dimension(800, 40));
        add(txtBuscar, grid);

        panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        crearCombo = new JComboBox<>(new String[]{
            "Crear",
            "Carpeta",
            "Documento de Word",
            "Documento de texto",
            "Documento PDF",
            "Hoja de Cálculo"
        });

        crearCombo.addActionListener(e -> {
            String seleccion = (String) crearCombo.getSelectedItem();
            String extension = "";

            String fileName = JOptionPane.showInputDialog("Introduce el nombre del archivo (sin extensión):");
            if (fileName == null || fileName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre de archivo no válido.");
                return;
            }
            try {
                switch (seleccion) {
                    case "Carpeta":
                        JOptionPane.showMessageDialog(this, "Creando una carpeta...");
                        control.crearFolder(fileName);
                        addNodeToTree(fileName, true);
                        break;

                    case "Documento de Word":
                        JOptionPane.showMessageDialog(this, "Creando un documento de Word...");
                        extension = ".docx";
                        control.crearFile(fileName + extension);
                        addNodeToTree(fileName + extension, false);
                        break;

                    case "Documento de texto":
                        JOptionPane.showMessageDialog(this, "Creando un documento de texto...");
                        extension = ".txt";
                        control.crearFile(fileName + extension);
                        addNodeToTree(fileName + extension, false);
                        break;

                    case "Documento PDF":
                        JOptionPane.showMessageDialog(this, "Creando un documento PDF...");
                        extension = ".pdf";
                        control.crearFile(fileName + extension);
                        addNodeToTree(fileName + extension, false);
                        break;

                    case "Hoja de Cálculo":
                        JOptionPane.showMessageDialog(this, "Creando una hoja de cálculo...");
                        extension = ".xlsx";
                        control.crearFile(fileName + extension);
                        addNodeToTree(fileName + extension, false);
                        break;

                    default:
                        break;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
        bnNombre = new JButton("Cambiar nombre");
        bnCopi = new JButton("Copiar");
        bnPaste = new JButton("Pegar");

        ordenarCombo = new JComboBox<>(new String[]{
            "Ordenar por",
            "Nombre",
            "Fecha",
            "Tamaño",
            "Tipo"
        });

        ordenarCombo.addActionListener(e -> {
            String seleccion = (String) ordenarCombo.getSelectedItem();
            switch (seleccion) {
                case "Nombre":
                    JOptionPane.showMessageDialog(this, "Ordenando por nombre...");
                    // Lógica para ordenar por nombre
                    break;
                case "Fecha":
                    JOptionPane.showMessageDialog(this, "Ordenando por fecha...");
                    // Lógica para ordenar por fecha
                    break;
                case "Tamaño":
                    JOptionPane.showMessageDialog(this, "Ordenando por tamaño...");
                    // Lógica para ordenar por tamaño
                    break;
                case "Tipo":
                    JOptionPane.showMessageDialog(this, "Ordenando por tipo...");
                    // Lógica para ordenar por tipo
                    break;
                default:
                    // Opción "Ordenar por" o inválida
                    break;
            }
        });

        panelBotones.add(crearCombo);
        panelBotones.add(bnNombre);
        panelBotones.add(bnCopi);
        panelBotones.add(bnPaste);
        panelBotones.add(ordenarCombo);

        grid.gridx = 0;
        grid.gridy = 1;
        grid.gridwidth = 2;
        grid.fill = GridBagConstraints.HORIZONTAL;
        add(panelBotones, grid);

        JPanel panelVista = new JPanel(new GridLayout(1, 2, 10, 0));
        jTree1 = new JTree(new DefaultTreeModel(rootNode));
        jTree1.setPreferredSize(new Dimension(400, 40));
        jScrollPane1 = new JScrollPane(jTree1);

        jList1 = new JList<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"});
        jList1.setPreferredSize(new Dimension(400, 40));
        jScrollPane2 = new JScrollPane(jList1);

        panelVista.add(jScrollPane1);
        panelVista.add(jScrollPane2);

        grid.gridx = 0;
        grid.gridy = 2;
        grid.gridwidth = 2;
        grid.fill = GridBagConstraints.BOTH;
        grid.weightx = 1.0;
        grid.weighty = 1.0;
        add(panelVista, grid);

        setSize(950, 600);
        setLocationRelativeTo(null);
        jTree1.addTreeSelectionListener(e -> {
            TreePath selectedPath = e.getPath();
            if (selectedPath != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
                File selectedFile = (File) node.getUserObject();

                if (selectedFile != null) {
                    if (selectedFile.isDirectory()) {
                        System.out.println("Selected Directory Path: " + selectedFile.getAbsolutePath());
                        control.modificarFile(selectedFile);
                    } else {
                        JOptionPane.showMessageDialog(this, "Por favor seleccione un directorio.");
                    }
                }
            }
        });
        jTree1.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

                if (value instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                    Object userObject = node.getUserObject();

                    if (userObject instanceof File) {
                        File file = (File) userObject;

                        setText(file.getName());

                        if (file.isDirectory()) {
                            setIcon(UIManager.getIcon("FileView.directoryIcon"));
                        } else {
                            setIcon(UIManager.getIcon("FileView.fileIcon"));
                        }
                    }
                }

                return c;
            }
        });
        Comparator<File> byName = (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName());
        Comparator<File> byDate = (f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified());
        Comparator<File> bySize = (f1, f2) -> Long.compare(f1.length(), f2.length());
        Comparator<File> byType = (f1, f2) -> {
            String ext1 = getFileExtension(f1);
            String ext2 = getFileExtension(f2);
            return ext1.compareToIgnoreCase(ext2);
        };

    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int index = name.lastIndexOf('.');
        return index == -1 ? "" : name.substring(index + 1);
    }

    private void addNodeToTree(String name, boolean isDirectory) {
        File newFile = new File(System.getProperty("user.dir"), name);
        DefaultMutableTreeNode parentNode = rootNode;

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(name);
        newNode.setUserObject(newFile);

        if (isDirectory) {
            parentNode.add(newNode);

            DefaultMutableTreeNode emptyNode = new DefaultMutableTreeNode("...");
            newNode.add(emptyNode);
        } else {
            parentNode.add(newNode);
        }

        DefaultTreeModel model = (DefaultTreeModel) jTree1.getModel();
        model.reload();
    }

    private DefaultMutableTreeNode crearNodo(File file) {
        DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(file);

        if (file.isDirectory()) {
            File[] archivos = file.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    nodo.add(crearNodo(archivo));
                }
            }
        }

        return nodo;
    }
    
     private void actualizarJTree(File dir) {
        rootNode = crearNodo(dir); 
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        jTree1.setModel(treeModel); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Administrador().setVisible(true);
        });
    }
}
