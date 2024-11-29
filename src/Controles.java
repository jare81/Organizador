/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.JOptionPane;

/**
 *
 * @author Laura Sabillon
 */public class Controles {

    private File directory = null;

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

    boolean copiar(String destino) {
        if (directory == null || !directory.isDirectory()) {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado un directorio válido.");
            return false;
        }

        // Assuming 'file' points to a file to copy
        File file = new File(directory, "fileToCopy.txt");  // example file name
        if (file.exists() && file.isFile()) {
            File destFile = new File(destino, file.getName());
            try {
                Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println("No existe archivo.");
            return false;
        }
    }
 
    void pegar() {

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
