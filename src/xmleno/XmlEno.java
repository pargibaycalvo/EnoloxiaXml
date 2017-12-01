/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmleno;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author oracle
 */
public class XmlEno {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws XMLStreamException, FileNotFoundException, SQLException {
        // TODO code application logic here
        
        xml lecturaescritura = new xml();
        lecturaescritura.conectBase();
        lecturaescritura.lectescrXml();
    }
    
}
