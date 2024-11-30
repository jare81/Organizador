/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import javax.swing.JFrame;


import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
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
    private JButton bnNombre, bnCopi, bnPaste, bnModificar;
    private JTree jTree1;
    private JList<String> jList1;
    private DefaultListModel<String> listModel;
    private JScrollPane jScrollPane1, jScrollPane2;
    private DefaultMutableTreeNode rootNode;
    private File archivoAbierto;
    private File seleccionado;
    private JTextPane textPane;

    public Administrador() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Organizador");

        setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.insets = new Insets(10, 10, 10, 10);
        File srcDir = new File(System.getProperty("user.dir"));
        rootNode = crearNodo(srcDir);
        txtBuscar = new JTextField(" ORGANIZADOR DE ARCHIVOS" );
        txtBuscar.setEditable(false);
        txtBuscar.setHorizontalAlignment(SwingConstants.CENTER);
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
                        actualizarJTree();
                        break;

                    case "Documento de Word":
                        JOptionPane.showMessageDialog(this, "Creando un documento de Word...");
                        extension = ".docx";
                        control.crearFile(fileName + extension);
                        addNodeToTree(fileName + extension, false);
                        actualizarJTree();
                        break;

                    case "Documento de texto":
                        JOptionPane.showMessageDialog(this, "Creando un documento de texto...");
                        extension = ".txt";
                        control.crearFile(fileName + extension);
                        addNodeToTree(fileName + extension, false);
                        actualizarJTree();
                        break;

                    case "Documento PDF":
                        JOptionPane.showMessageDialog(this, "Creando un documento PDF...");
                        extension = ".pdf";
                        control.crearFile(fileName + extension);
                        addNodeToTree(fileName + extension, false);
                        actualizarJTree();
                        break;

                    case "Hoja de Cálculo":
                        JOptionPane.showMessageDialog(this, "Creando una hoja de cálculo...");
                        extension = ".xlsx";
                        control.crearFile(fileName + extension);
                        addNodeToTree(fileName + extension, false);
                        actualizarJTree();
                        break;

                    default:
                        break;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
        bnNombre = new JButton("Cambiar nombre");
        bnNombre.addActionListener(e -> {
            control.cambiarNombre(seleccionado);
            actualizarJTree();

        });

        bnCopi = new JButton("Copiar");

        bnCopi.addActionListener(e -> {
            TreePath selectedPath = jTree1.getSelectionPath();
            if (selectedPath == null) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un archivo para copiar.");
                return;
            }

            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
            File selectedFile = (File) selectedNode.getUserObject();

            if (selectedFile == null || !selectedFile.exists()) {
                JOptionPane.showMessageDialog(this, "El archivo seleccionado no existe.");
                return;
            }

            if (selectedFile.isDirectory()) {
                JOptionPane.showMessageDialog(this, "Seleccione un archivo, no un directorio.");
                return;
            }

            boolean success = control.copiar(selectedFile);
            if (success) {
                JOptionPane.showMessageDialog(this, "Archivo copiado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al copiar el archivo.");
            }
        });

        bnPaste = new JButton("Pegar");
        bnPaste.addActionListener(e -> {
            TreePath selectedPath = jTree1.getSelectionPath();
            if (selectedPath == null) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un directorio de destino.");
                return;
            }

            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
            File destinationFolder = (File) selectedNode.getUserObject();

            if (destinationFolder == null || !destinationFolder.isDirectory()) {
                JOptionPane.showMessageDialog(this, "Seleccione un directorio válido.");
                return;
            }

            boolean success = control.pegar(destinationFolder);
            if (success) {
                JOptionPane.showMessageDialog(this, "Archivo pegado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al pegar el archivo.");
            }

            actualizarJTree();
        });

        bnModificar = new JButton("Modificar");

        bnModificar.addActionListener(e -> {

            control.setTextFrame(textPane);
            guardarCambios(textPane);

        });

        ordenarCombo = new JComboBox<>(new String[]{
            "Ordenar por",
            "Nombre",
            "Fecha",
            "Tamaño",
            "Tipo"
        });

        ordenarCombo.addActionListener(e -> {
            String seleccion = (String) ordenarCombo.getSelectedItem();
            if (seleccion != null && !seleccion.equals("Ordenar por")) {
                ordenarArchivos(seleccion);
            }
        });

        panelBotones.add(crearCombo);
        panelBotones.add(bnNombre);
        panelBotones.add(bnCopi);
        panelBotones.add(bnPaste);
        panelBotones.add(bnModificar);
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

        textPane = new JTextPane();
        textPane.setPreferredSize(new Dimension(400, 40));
        jScrollPane2 = new JScrollPane(textPane);

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
                seleccionado = selectedFile;

                if (selectedFile != null) {
                    if (selectedFile.isDirectory()) {
                        System.out.println("Selected Directory Path: " + selectedFile.getAbsolutePath());
                        control.modificarFile(selectedFile);
                    } else {
                        System.out.println("Selected File Path: " + selectedFile.getAbsolutePath());
                        abrirArchivo(selectedFile);
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
    }
    Comparator<File> byName = (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName());
    Comparator<File> byDate = (f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified());
    Comparator<File> bySize = (f1, f2) -> Long.compare(f1.length(), f2.length());
    Comparator<File> byType = (f1, f2) -> {
        String ext1 = getFileExtension(f1);
        String ext2 = getFileExtension(f2);
        return ext1.compareToIgnoreCase(ext2);
    };

    private String getFileExtension(File file) {
        String name = file.getName();
        int index = name.lastIndexOf('.');
        return index == -1 ? "" : name.substring(index + 1);
    }

    private void ordenarArchivos(String criterio) {
        TreePath selectedPath = jTree1.getSelectionPath();
        if (selectedPath == null) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una carpeta para ordenar.");
            return;
        }

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
        File selectedFile = (File) selectedNode.getUserObject();

        if (!selectedFile.isDirectory()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una carpeta válida.");
            return;
        }

        File[] archivos = selectedFile.listFiles();
        if (archivos == null || archivos.length == 0) {
            JOptionPane.showMessageDialog(this, "La carpeta seleccionada no contiene archivos para ordenar.");
            return;
        }

        switch (criterio) {
            case "Nombre":
                Arrays.sort(archivos, byName);
                break;
            case "Fecha":
                Arrays.sort(archivos, byDate);
                break;
            case "Tamaño":
                Arrays.sort(archivos, bySize);
                break;
            case "Tipo":
                Arrays.sort(archivos, byType);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Criterio de ordenación no válido.");
                return;
        }

        selectedNode.removeAllChildren();
        for (File archivo : archivos) {
            selectedNode.add(crearNodo(archivo));
        }

        DefaultTreeModel model = (DefaultTreeModel) jTree1.getModel();
        model.reload(selectedNode);

        System.out.println("Archivos en " + selectedFile.getName() + " ordenados por " + criterio + ":");
        for (File archivo : archivos) {
            System.out.println(archivo.getName());
        }
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

    private void actualizarJTree() {
        TreePath selectedPath = jTree1.getSelectionPath();

        rootNode = crearNodo(new File(System.getProperty("user.dir")));

        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        jTree1.setModel(model);

        if (selectedPath != null) {
            jTree1.setSelectionPath(selectedPath);
        }
    }

    public void guardarCambios(JTextPane textPane) {
        if (seleccionado == null || !seleccionado.canWrite()) {
            JOptionPane.showMessageDialog(this, "Error: No se puede escribir en un archivo nulo o protegido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!seleccionado.getName().endsWith(".txt")) {
            JOptionPane.showMessageDialog(this, "El archivo no es un archivo de texto (.txt). No se han realizado cambios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(seleccionado))) {
            String content = textPane.getText();

            bw.write(content);

            JOptionPane.showMessageDialog(this, "Archivo guardado con éxito.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirArchivo(File archivo) {

        if (archivo.isDirectory()) {
            return;
        }

        if (!archivo.getName().endsWith(".txt")) {
            JOptionPane.showMessageDialog(this, "El archivo no es un formato TXT, no se podrá editar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            textPane.setText("");

            String line;
            StringBuilder content = new StringBuilder();

            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }

            textPane.setText(content.toString());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Administrador().setVisible(true);
        });
    }
}
