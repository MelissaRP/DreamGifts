/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Usuario
 */
public class Conexion {
    
      private Connection con;
    
    
    public  Connection ConexionBD(){
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost/mydb","root","");
            
        }catch(Exception e){
            System.out.println("no conetado: "+e);
     }
        return con;
    }//fin metodo
    
          
  
    /*
    public static void main(String[] args){
        
        Conexion conectar = new Conexion();
        Connection con;
        DefaultTableModel modelo;
        
        List<Clientes> datos = new ArrayList<>();
        String sql="select * from `cliente`";
        
        try{
            con=conectar.ConexionBD();
            PreparedStatement pst = con.prepareStatement("select * from cliente");
            ResultSet rs = pst.executeQuery();
            
            
            while (rs.next()){
                Clientes cli = new Clientes();
                cli.setRut(rs.getInt(1));
                cli.setDv(rs.getString(2));
                cli.setNombre(rs.getString(3));
                cli.setCelular(rs.getInt(4));
                cli.setTelefono(rs.getInt(5));
                cli.setFecha_nac(rs.getString(6));
                cli.setEmail(rs.getString(7));
                cli.setRedsocial(rs.getString(8));
                datos.add(cli);
            }
        }catch(Exception e){
            
        }
        
        
           System.out.println("1.- "+datos);
       
        
    }
    */
    
    
    
}
