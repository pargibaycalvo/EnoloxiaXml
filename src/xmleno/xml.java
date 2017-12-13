/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmleno;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author oracle
 */
public class xml {
    Connection conn;
    String ruta = "/home/oracle/NetBeansProjects/xmlEno/analisis.xml";
    XMLInputFactory fa = XMLInputFactory.newInstance();
    XMLStreamReader red;
    PreparedStatement sel;
    PreparedStatement ins;
    ResultSet rs;
    
    String datos[]=new String[4];
    String inser[]=new String[4];
    String codig[]=new String[4];
    
    public void conectBase(){
        try {
            String driver = "jdbc:oracle:thin:";
            String host = "localhost";
            String porto = "1521";
            String sid = "orcl";
            String usuario = "hr";
            String password = "hr";
            String url = driver+usuario
                    +"/"+password+"@"+host
                    +":"+porto+":"+sid;
            conn = DriverManager.getConnection(url);
            System.out.println("Base de datos operativa. Conectado");
        } catch (SQLException ex) {
            Logger.getLogger(xml.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void lectescrXml() throws XMLStreamException, FileNotFoundException, SQLException{
        
        try{
            red=fa.createXMLStreamReader(new FileInputStream(ruta));
                while(red.hasNext()){
                    int eventType = red.next();
                    switch(eventType){
                        
                        case XMLStreamReader.START_DOCUMENT:
                            break;
                        //recorremos los datos y los guardamos en el array "codig"
                        case XMLStreamReader.START_ELEMENT:
                            if(red.getAttributeCount()>0){
                                codig[1]=red.getAttributeValue(0);
                            }
                            if(red.getLocalName()=="acidez"){
                                datos[0]=red.getElementText();
                            }
                            if(red.getLocalName()=="tipodeuva"){
                                datos[1]=red.getElementText();
                            }
                            if(red.getLocalName()=="cantidade"){
                                datos[2]=red.getElementText();
                            }
                            if(red.getLocalName()=="dni"){
                                datos[3]=red.getElementText();
                            }
                            break;
                            
                        case XMLStreamReader.CHARACTERS:
                            System.out.println(red.getText());
                            break;
                        //recogemos los datos del array y los introducimos en la tabla sql que queramos
                        case XMLStreamReader.END_ELEMENT:
                            if(red.getLocalName()=="analise"){
                                sel=conn.prepareStatement("SELECT nomeu,acidezmin,acidezmax from uvas where tipo ='" +datos[1]+ "'");
                                ins=conn.prepareStatement("INSERT into copia(num,nomeuva,tratacidez,total)values(?,?,?,?)");
                                rs=sel.executeQuery();
                                rs.next();
                                
                                inser[0]=rs.getString("nomeu");
                                inser[1]=rs.getString("acidezmin");
                                inser[2]=rs.getString("acidezmax");
                                ins.setString(1, codig[1]);
                                ins.setString(2, inser[0]);
                                
                                if(Integer.parseInt(datos[0])<Integer.parseInt(inser[1])){
                                    ins.setString(3, "Baja");
                                }
                                else if(Integer.parseInt(datos[0])>Integer.parseInt(inser[2])){
                                    ins.setString(3, "Alta");
                                }
                                else{
                                    ins.setString(3, "Perfecta");
                                }
                                
                                int operacion = Integer.parseInt(datos[2])*15;
                                ins.setInt(4, operacion);
                                ins.executeUpdate();                               
                            }
                            break;
                        }
                }
                    
            } catch (FileNotFoundException ex) {
                Logger.getLogger(xml.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
}
