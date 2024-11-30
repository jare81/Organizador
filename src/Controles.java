/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.rtf.RTFEditorKit;

public class Controles {

    private File directory = null;
    private RTFEditorKit rtfEditorKit = new RTFEditorKit(); 
    private JTextPane textPane;
    private File fileToCopy;
    
    public void setTextFrame(JTextPane frame) {
        this.textPane = frame;
    }

    void modificarFile(File dir) {
        this.directory = dir;
    }

    boolean crearFile(String nombre) throws IOException {
        if (directory == null || !directory.isDirectory()) {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado un directorio válido.");
            return false;
        }

        File newFile = new File(directory, nombre);

        if (newFile.exists()) {
            JOptionPane.showMessageDialog(null, "Ya existe este archivo.");
            return false;
        }

        return newFile.createNewFile();
    }

    boolean crearFolder(String folderName) {
        if (directory == null || !directory.isDirectory()) {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado un directorio válido.");
            return false;
        }

        File newFolder = new File(directory, folderName);

        if (newFolder.exists()) {
            JOptionPane.showMessageDialog(null, "La carpeta ya existe.");
            return false;
        }

        return newFolder.mkdir(); 
    }

      boolean copiar(File fileToCopy) {
        if (fileToCopy.exists() && fileToCopy.isFile()) {
            this.fileToCopy = fileToCopy;
            return true;
        } else {
            System.out.println("No existe archivo.");
            return false;
        }
    }

    public boolean pegar(File destinationFolder) {
        if (fileToCopy == null) {
            JOptionPane.showMessageDialog(null, "No hay un archivo copiado para pegar.");
            return false;
        }

        if (destinationFolder == null || !destinationFolder.isDirectory()) {
            JOptionPane.showMessageDialog(null, "Seleccione un directorio válido.");
            return false;
        }

        File destFile = new File(destinationFolder, fileToCopy.getName());
        try {
            Files.copy(fileToCopy.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            JOptionPane.showMessageDialog(null, "Archivo pegado correctamente.");
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Hubo un error al pegar el archivo.");
            e.printStackTrace();
            return false;
        }
    }

    
     public void guardarFile() {
        try (FileWriter writer = new FileWriter(directory)) {
            rtfEditorKit.write(writer, textPane.getDocument(), 0, textPane.getDocument().getLength());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

        void cambiarNombre(File archivo) {
            String extensionOriginal = "";

            int i = archivo.getName().lastIndexOf('.');
            if (i > 0 && i < archivo.getName().length() - 1) {
                extensionOriginal = archivo.getName().substring(i); 
            }

            String nuevoNombre = JOptionPane.showInputDialog(
                null,
                "Ingrese el nuevo nombre (sin cambiar la extensión):",
                archivo.getName().replace(extensionOriginal, "") 
            );

            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                if (!nuevoNombre.endsWith(extensionOriginal)) {
                    nuevoNombre += extensionOriginal;
                }

                File archivoRenombrado = new File(archivo.getParent(), nuevoNombre);

                boolean exito = archivo.renameTo(archivoRenombrado);

                if (exito) {
                    JOptionPane.showMessageDialog(null, "El archivo ha sido renombrado exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo renombrar el archivo.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.");
            }
        }

}
