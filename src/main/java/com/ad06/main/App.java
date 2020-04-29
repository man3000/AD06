/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ad06.main;

import com.ad06.util.DatosConexion;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mongodb.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author Manuel
 */
public class App {

    //Directorio donde se encuentra el proyecto
    static String dir = System.getProperty("user.dir");

    //Separador de archivos de OS de ejecución
    static String sep = File.separator;

    //Ruta del archivo config.json relativa al directorio de ejecución del proyecto
    static final File configJson = new File(dir + sep + "src" + sep + "main" + sep + "java" + sep + "com" + sep + "ad06" + sep + "util" + sep + "config.json");

    public String dbURI;

    public DatosConexion datosConexion;
    
    private DB database;

    public App() {
        datosConexion = new DatosConexion();
    }

    

    public static void main(String[] args) {

        App app = new App();
        app.loadJsonConfigData(configJson);

        //System.out.println("La existencia del usuario es " + app.userExists("manu"));
        
        Login login = new Login(app);
        login.setVisible(true);
    }
    
    private void loadJsonConfigData(File f) {
        File file = f;
        if (file.exists()) {
            try {

                Gson gson2 = new Gson();
                BufferedReader input = new BufferedReader(new FileReader(file));

                StringBuilder injson = new StringBuilder("");

                String s;
                while ((s = input.readLine()) != null) {
                    injson.append(s);
                }
                this.datosConexion = gson2.fromJson(injson.toString(), DatosConexion.class);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (JsonSyntaxException e) {

                JOptionPane.showMessageDialog(null, "Error al procesar el archivo config.json", "Error", JOptionPane.CANCEL_OPTION);
                System.exit(0);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encuentra el archivo provincias.json", "Error", JOptionPane.CANCEL_OPTION);
            System.exit(0);

        }

        this.dbURI = "mongodb://" + datosConexion.address + ":" + datosConexion.port;

        MongoClient mongoClient = new MongoClient(new MongoClientURI(this.dbURI));
        
        database = mongoClient.getDB(datosConexion.dbname);
        
        System.out.println(datosConexion.address);
        System.out.println(datosConexion.port);
        System.out.println(datosConexion.dbname);
        System.out.println(datosConexion.username);
        System.out.println(datosConexion.password);

    }

    public boolean userExists(String user) {
        //Conexión con MongoDB
        

        //Coleccion alumno. Hai que creala fora
        DBCollection colUsuario = database.getCollection("usuario");

        DBObject query = new BasicDBObject("username", user);
        DBCursor cursor = colUsuario.find(query);

        return cursor.count() != 0;
    }
    
    public boolean authenticateUser(){
        
        return true;
    }
}
