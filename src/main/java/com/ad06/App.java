/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ad06;

import com.mongodb.*;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Manuel
 */
public class App {
    
    public static void main(String[] args) {
        //Conexión con MongoDB
        String host = "192.168.56.102";
        String port = "27017";
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://"+host+":"+port));

        //Collemos a base de datos que queremos. Hai que creala fora
        String dbName = "minitwitter";
        DB database = mongoClient.getDB(dbName);

        //Coleccion alumno. Hai que creala fora
        DBCollection colUsuario= database.getCollection("usuario");

        //Creamos o documento
        List<String> follows = Arrays.asList("username1", "username2", "username3");
        DBObject alumno = new BasicDBObject()
                .append("nome", "Manuel Varela")
                .append("username", "manu")
                .append("password", "abc123.")
                .append("follows", follows);

        //Insertamolo documento
        colUsuario.insert(alumno);
        System.out.println("Inserción realizada con éxito");
    }
    
}
