package vista;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

public class Vista extends javax.swing.JFrame {

    Conexion con = new Conexion();
    Connection cn = con.ConexionBD();
    DefaultTableModel modelo, modelo2, modeloCat, modeloComuna, modeloBanco, modeloCatVenta, modeloRRSS, modeloArticulo, modeloPack, modeloPack2;
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    int id;

    public Vista() {
        initComponents();
        setLocationRelativeTo(null);
        listarCliente();
        listarComuna();
        listarBanco();
        listarProveedores();
        listarCat();
        listaRRSS();
        listaArticulos();
        listarCatVenta();
        listarUsuario();
        listarPack();
        listar2Pack();
        llenarComboBoxPackVenta();

    }

    //////////////// Cliente //////////////////////////////
    //Metodo Listar Cliente
    void listarCliente() {

        try {
            PreparedStatement pst = cn.prepareStatement("SELECT c.rut, c.dv, c.nombre, c.direccion, c.telefono, c.celular, c.fecha_nac, c.email, r.nombre, c.estado FROM cliente as c, rrss as r WHERE c.RRSS_id_rrss = r.id_rrss");
            ResultSet rs = pst.executeQuery();

            Object[] clientes = new Object[10];

            modelo = (DefaultTableModel) TablaClientes.getModel();

            while (rs.next()) {
                clientes[0] = rs.getInt("c.rut");
                clientes[1] = rs.getString("c.dv");
                clientes[2] = rs.getString("c.nombre");
                clientes[3] = rs.getString("c.direccion");
                clientes[4] = rs.getInt("c.telefono");
                clientes[5] = rs.getInt("c.celular");
                clientes[6] = rs.getString("c.fecha_nac");
                clientes[7] = rs.getString("c.email");
                clientes[8] = rs.getString("r.nombre");
                clientes[9] = rs.getString("c.estado");

                modelo.addRow(clientes);
            }
            TablaClientes.setModel(modelo);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al Conectar  " + e);
        }

    }//Cierre Listar Cliente

    /*public void addCheckBox(int column, JTable table) {
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }

    public boolean IsSelected(int row, int column, JTable table) {
        return table.getValueAt(row, column) != null;
    }*/
    //metodo para Guardar Clientes
    void GuardarCliente() {

        int idRrss = 0;
        try {
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
            String nom_rrss = cbRedSocialCliente.getSelectedItem().toString();

            PreparedStatement pst1 = cn.prepareStatement("select id_rrss from rrss where nombre = '" + nom_rrss + "'");
            ResultSet rs = pst1.executeQuery();
            while (rs.next()) {
                idRrss = rs.getInt("id_rrss");
            }

            PreparedStatement pst2 = cn.prepareStatement("insert into cliente values(?,?,?,?,?,?,?,?,?,?)");

            pst2.setInt(1, Integer.parseInt(txtRutCliente.getText()));
            pst2.setString(2, txtDvCliente.getText().toUpperCase());
            pst2.setString(3, txtNombreCliente.getText().toUpperCase());
            pst2.setString(4, txtDireccionCliente.getText().toUpperCase());
            pst2.setInt(5, Integer.parseInt(txtTelefonoCliente.getText()));
            pst2.setInt(6, Integer.parseInt(txtCelularCliente.getText()));
            pst2.setString(7, ((JTextField) FechaNacimientoCliente.getDateEditor().getUiComponent()).getText());
            pst2.setString(8, txtEmailCliente.getText().trim());
            pst2.setInt(9, idRrss);
            pst2.setString(10, cbEstadoCliente.getSelectedItem().toString());

            pst2.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Guardo el Cliente Correctamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);
            limpiartabla();// limpiar la Tabla 
            limpiarCliente();// limpiar campos de textos

        }// cierre del bloque try
        catch (Exception ex) {

            // mensaje error al grabar
            JOptionPane.showMessageDialog(null, "Error al intentar guardar al Cliente" + ex, "AVISO", JOptionPane.ERROR_MESSAGE);
            limpiarUsuario();
            limpiartabla2();
            ex.printStackTrace();
        }// cierre del catch

    }// Cierre Guardar Clientes

    //Metodo Moficar Clientes
    void modificarCliente() {
        int idRrss = 0;

        try {

            int rut = Integer.parseInt(txtRutCliente.getText());
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
            String nom_rrss = cbRedSocialCliente.getSelectedItem().toString();
            PreparedStatement pst1 = cn.prepareStatement("select id_rrss from rrss where nombre = '" + nom_rrss + "'");
            ResultSet rs = pst1.executeQuery();
            while (rs.next()) {
                idRrss = rs.getInt("id_rrss");
            }

            PreparedStatement pst = cn.prepareStatement("update cliente set rut=?,dv=?,nombre=?,direccion=?,telefono=?,celular=?,fecha_nac=?,email=?,RRSS_id_rrss=?,estado=? where rut=" + rut);

            pst.setInt(1, Integer.parseInt(txtRutCliente.getText().trim()));
            pst.setString(2, txtDvCliente.getText().trim());
            pst.setString(3, txtNombreCliente.getText().trim());
            pst.setString(4, txtDireccionCliente.getText().trim());
            pst.setInt(5, Integer.parseInt(txtTelefonoCliente.getText().trim()));
            pst.setInt(6, Integer.parseInt(txtCelularCliente.getText().trim()));
            pst.setString(7, ((JTextField) FechaNacimientoCliente.getDateEditor().getUiComponent()).getText());
            pst.setString(8, txtEmailCliente.getText().trim());
            pst.setInt(9, idRrss);
            pst.setString(10, cbEstadoCliente.getSelectedItem().toString());

            pst.executeUpdate();
            limpiarCliente();

            JOptionPane.showMessageDialog(null, "Datos del usuario actualizados", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            limpiartabla();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar " + e);
            limpiarCliente();
            limpiartabla();
            e.printStackTrace();
        }
    }// Cierre Modificar Cliente

     /* declarar la siguiente variable en la clase*/
    DefaultTableModel dm;

    /* Método filtro para buscar por nombre y rut*/
    public void filtro(String consulta, JTable jtableBuscar){
        dm = (DefaultTableModel) jtableBuscar.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dm);
        jtableBuscar.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(consulta));
}
    
    
    protected void buscarTabla(int rut, String nombre) {
        /*modelo = (DefaultTableModel) TablaClientes.getModel();
        Object[] clientes = new Object[10];
        String where=" where 1=1 ";
        //Si el nombre no esta vacio
        if(rut==0){
            where=where+" and rut='"+rut+"' ";
        }
        //Si el puesto no esta vacio
        if(nombre.isEmpty()==false){
            where=where+" and nombre='"+nombre+"' ";
        }
       
        //"select * from cliente "+where+" ;"
        // select * from cliente where nombre like '%"+valor+"%'
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT c.rut, c.dv, c.nombre, c.direccion, c.telefono, c.celular, c.fecha_nac, c.email, r.nombre, c.estado FROM cliente as c, rrss as r WHERE c.RRSS_id_rrss = r.id_rrss and rut like '%\"+rut+\"%' or nombre like '%\"+nombre+\"%'");
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                clientes[0] = rs.getInt("c.rut");
                clientes[1] = rs.getString("c.dv");
                clientes[2] = rs.getString("c.nombre");
                clientes[3] = rs.getString("c.direccion");
                clientes[4] = rs.getInt("c.telefono");
                clientes[5] = rs.getInt("c.celular");
                clientes[6] = rs.getString("c.fecha_nac");
                clientes[7] = rs.getString("c.email");
                clientes[8] = rs.getString("r.nombre");
                clientes[9] = rs.getString("c.estado");
                
                modelo.addRow(clientes);
            }
            TablaClientes.setModel(modelo);

        } catch (Exception e) {
        }*/
    }

    //Limpiar la tabla de  Cliente
    public void limpiartabla() {
        DefaultTableModel model1 = (DefaultTableModel) TablaClientes.getModel();
        while (TablaClientes.getRowCount() > 0) {
            model1.removeRow(0);
        }//Fin de limpiar las filas 
    }// Cierre de Limpiar tabla Cliente

    // Metodo Limpiar Cliente
    void limpiarCliente() {

        txtRutCliente.setText("");
        txtDvCliente.setText("");
        txtNombreCliente.setText("");
        txtDireccionCliente.setText("");
        txtTelefonoCliente.setText("");
        txtCelularCliente.setText("");
        FechaNacimientoCliente.setDate(null);
        txtEmailCliente.setText("");
        cbRedSocialCliente.setSelectedIndex(0);
        cbEstadoRRSS.setSelectedIndex(0);

    }// Cierre Metodo Limpiar Cliente
    ////////////////////////FIN CLIENTE/////////////////////////////////////

    ////////////////////////////// Comuna //////////////////////////////
    //Metodo Listar Comuna
    void listarComuna() {

        try {
            PreparedStatement pst = cn.prepareStatement("select * from comuna");
            ResultSet rs = pst.executeQuery();

            Object[] usuario = new Object[2];

            modeloComuna = (DefaultTableModel) Tabla_Comuna.getModel();

            while (rs.next()) {
                usuario[0] = rs.getInt("cod_comuna");
                usuario[1] = rs.getString("nombre");

                modeloComuna.addRow(usuario);
            }
            Tabla_Comuna.setModel(modeloComuna);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al Conectar  " + e);
        }

    }//cierre listar Comuna

    //Metodo Guardar Comuna    
    void guardarComuna() {
        try {
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
            PreparedStatement pst = cn.prepareStatement("insert into comuna values(?,?)");
            int id = 0;
            pst.setInt(1, id);
            pst.setString(2, txtNombreComuna.getText().trim());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Guardo La Comuna Correctamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);
            limpiartablaComuna();// Limpia la tabla 
            limpiarComuna();// limpiar campos de textos

        }// cierre del bloque try
        catch (Exception ex) {

            // mensaje error al grabar
            JOptionPane.showMessageDialog(null, "Error al intentar guardad La Comuna   // " + ex, "AVISO", JOptionPane.ERROR_MESSAGE);
            limpiarComuna();
            limpiartablaComuna();
            ex.printStackTrace();
        }// cierre del catch
    }// Cierre guardar usuario

    //Metodo modificar Comuna
    void modificarComuna() {

        try {

            String id1 = txtCodigoComuna.getText();
            int id2 = Integer.parseInt(id1);
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
            PreparedStatement pst = cn.prepareStatement("update comuna set nombre=? where cod_comuna=" + id2);

            pst.setString(1, txtNombreComuna.getText().trim());

            pst.executeUpdate();
            limpiarComuna();

            JOptionPane.showMessageDialog(null, "Datos de la comuna actualizados  ", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            limpiartablaComuna();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar  //  " + e);
            limpiarComuna();
        }
    }// Cierre Modifica Comuna

    //Eliminar Comuna
    void elimianrComuna() {
        int filaselecionado = Tabla_Comuna.getSelectedRow();
        if (filaselecionado == -1) {
            JOptionPane.showMessageDialog(null, "Debe selecionar Fila");
        } else {
            try {
                String id1 = txtCodigoComuna.getText();
                int id2 = Integer.parseInt(id1);
                String URL_bd = "jdbc:mysql://localhost/mydb";
                String usuario = "root";// este usuario es por default de mysql
                String contraseña = "";// depende de como entre a la consola de mysql
                Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
                PreparedStatement pst = cn.prepareStatement("delete from comuna where cod_comuna=" + "'" + id2 + "'");
                int validar = JOptionPane.showConfirmDialog(null, "¿Estas seguro que deseas eliminar el registro?  ", "PREGUNTA", JOptionPane.YES_NO_OPTION);
                if (validar == 0) {
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Registro eliminado ");
                    limpiarComuna();
                    limpiartablaComuna();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar registro  //  " + e);
                limpiarComuna();
                limpiartablaComuna();
            }

        }
    }// cierre Eliminar Comuna

    // Metodo Limpiar Comuna 
    public void limpiarComuna() {
        txtCodigoComuna.setText("");
        txtNombreComuna.setText("");

    }// Cierre limpiar 

    //Metodo Limpiar la tabla de USUARIO
    public void limpiartablaComuna() {
        DefaultTableModel model1 = (DefaultTableModel) Tabla_Comuna.getModel();
        while (Tabla_Comuna.getRowCount() > 0) {
            model1.removeRow(0);
        }//Fin de limpiar las filas 
    }// Cierre de Limpiar tabla Usuario

    ///////////////////////FIN COMUNA         ///////////////////////////
    //////////////// Proveedores //////////////////////////////
    //Metodo Listar Proveedores
    void listarProveedores() {

        try {
            PreparedStatement pst = cn.prepareStatement("SELECT P.rut, P.razon_social, P.nombre_contacto, P.fono, P.email, D.calle, D.nro, C.nombre, D.complemento, p.estado FROM proveedor as P,direccion as D, comuna as C where P.DIRECCION_cod_direccion = D.cod_direccion and D.COMUNA_cod_comuna = C.cod_comuna and P.DIRECCION_COMUNA_cod_comuna = C.cod_comuna ");
            ResultSet rs = pst.executeQuery();

            Object[] proveedor = new Object[10];

            modelo = (DefaultTableModel) Tabla_Provedores1.getModel();

            while (rs.next()) {
                proveedor[0] = rs.getString("P.rut");
                proveedor[1] = rs.getString("P.razon_social");
                proveedor[2] = rs.getString("P.nombre_contacto");
                proveedor[3] = rs.getInt("P.fono");
                proveedor[4] = rs.getString("P.email");
                proveedor[5] = rs.getString("D.calle");
                proveedor[6] = rs.getInt("D.nro");
                proveedor[7] = rs.getString("C.nombre");
                proveedor[8] = rs.getString("D.complemento");
                proveedor[9] = rs.getString("P.estado");

                modelo.addRow(proveedor);
            }
            Tabla_Provedores1.setModel(modelo);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al Conectar  " + e);
        }

    }//Cierre Listar Proveedores

    // Metodo Guardar Proveedor
    void GuardarProveedor() {
        int id = 0;
        int codComuna = 0;
        int codDire = 0;
        String comuna = cbxComunaProveedor.getSelectedItem().toString();
        String calle = txtDireccionProveedor.getText().trim();
        int nro = Integer.parseInt(txtNumDireProveedor.getText().trim());
        try {
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);

            // ingresamos la comuna
            PreparedStatement pst1 = cn.prepareStatement("insert into comuna values(?,?)");

            pst1.setInt(1, id);
            pst1.setString(2, cbxComunaProveedor.getSelectedItem().toString());
            pst1.executeUpdate();

            // buscamos el codigo de comuna
            PreparedStatement pst = cn.prepareStatement("select cod_comuna from comuna where nombre ='" + comuna + "'");
            ResultSet rs1 = pst.executeQuery();
            if (rs1.next()) {
                codComuna = rs1.getInt("cod_comuna");
            }

            // Ingresamos la direccion 
            PreparedStatement pst2 = cn.prepareStatement("insert into direccion values(?,?,?,?,?)");
            pst2.setInt(1, id);
            pst2.setString(2, txtDireccionProveedor.getText().trim());
            pst2.setInt(3, Integer.parseInt(txtNumDireProveedor.getText().trim().toString()));
            pst2.setString(4, txtComplementoProveedor.getText().trim());
            pst2.setInt(5, codComuna);
            pst2.executeUpdate();

            // buscamos el codigo de la direccion
            PreparedStatement pst4 = cn.prepareStatement("select cod_direccion from direccion where calle ='" + calle + "' and nro ='" + nro + "' ");
            ResultSet rs2 = pst4.executeQuery();
            if (rs2.next()) {
                codDire = rs2.getInt("cod_direccion");
            }

            // Ingresamos los datos de Proveedores
            PreparedStatement pst3 = cn.prepareStatement("insert into proveedor values(?,?,?,?,?,?,?,?)");

            pst3.setString(1, txtRutProveedor.getText().trim());
            pst3.setString(2, txtRazonSocialProveedor.getText().trim());
            pst3.setString(3, txtNombreProveedor.getText().trim());
            pst3.setInt(4, Integer.parseInt(txtTelefonoProveedor.getText().trim()));
            pst3.setString(5, txtEmailProveedor.getText().trim());
            pst3.setInt(6, codDire);
            pst3.setInt(7, codComuna);
            pst3.setString(8, cbEstadoProveedor.getSelectedItem().toString());
            pst3.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Guardo el Usuario Correctamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);
            limpiarTablaProveedor();
            limpiarProvedores();// limpiar campos de textos

        }// cierre del bloque try
        catch (Exception ex) {

            // mensaje error al grabar
            JOptionPane.showMessageDialog(null, "Error al intentar guardar al Usuario" + ex, "AVISO", JOptionPane.ERROR_MESSAGE);
            limpiarProvedores();
            limpiarTablaProveedor();
            ex.printStackTrace();
        }// cierre del catch

    }// Cierre Metodo Guardar Proveedor

    // metodo para Editar Proveedores
    void editarPorveedor() {
        int id = 0;
        int codComuna = 0;
        int codDire = 0;

        try {

            String comuna = cbxComunaProveedor.getSelectedItem().toString();
            String calle = txtDireccionProveedor.getText().trim();
            int nro = Integer.parseInt(txtNumDireProveedor.getText().trim());

            String rut = (txtRutProveedor.getText());
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);

            // buscamos el codigo de comuna
            PreparedStatement pst4 = cn.prepareStatement("select cod_comuna from comuna where nombre ='" + comuna + "'");
            ResultSet rs1 = pst4.executeQuery();
            if (rs1.next()) {
                codComuna = rs1.getInt("cod_comuna");
            }

            // buscamos el codigo de la direccion
            PreparedStatement pst5 = cn.prepareStatement("select cod_direccion from direccion where calle ='" + calle + "' and nro ='" + nro + "' ");
            ResultSet rs2 = pst5.executeQuery();
            if (rs2.next()) {
                codDire = rs2.getInt("cod_direccion");
            }

            PreparedStatement pst1 = cn.prepareStatement("update comuna set nombre=? where nombre='" + comuna + "' ");
            pst1.setString(1, cbxComunaProveedor.getSelectedItem().toString());
            pst1.executeUpdate();

            PreparedStatement pst2 = cn.prepareStatement("update direccion set calle=?, nro=?,complemento=? where calle ='" + calle + "' and nro ='" + nro + "' ");

            pst2.setString(1, txtDireccionProveedor.getText().trim());
            pst2.setInt(2, Integer.parseInt(txtNumDireProveedor.getText().trim().toString()));
            pst2.setString(3, txtComplementoProveedor.getText().trim());

            pst2.executeUpdate();

            PreparedStatement pst3 = cn.prepareStatement("update proveedor set rut=?,razon_social=?,nombre_contacto=?,fono=?,email=?, estado=? where rut='" + rut + "'");

            pst3.setString(1, txtRutProveedor.getText().trim());
            pst3.setString(2, txtRazonSocialProveedor.getText().trim());
            pst3.setString(3, txtNombreProveedor.getText().trim());
            pst3.setString(4, txtTelefonoProveedor.getText().trim());
            pst3.setString(5, txtEmailProveedor.getText().trim());
            pst3.setString(6, cbEstadoProveedor.getSelectedItem().toString());

            pst3.executeUpdate();

            limpiarCliente();

            JOptionPane.showMessageDialog(null, "Datos del Proveedor actualizados", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            limpiarTablaProveedor();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar ///" + e);
            limpiarProvedores();
            limpiarTablaProveedor();
            e.printStackTrace();
        }

    }// Cierre del metodo Editar Proveedores

    //Limpiar la tabla de  Proveedor
    public void limpiarTablaProveedor() {
        DefaultTableModel model2 = (DefaultTableModel) Tabla_Provedores1.getModel();
        while (Tabla_Provedores1.getRowCount() > 0) {
            model2.removeRow(0);
        }//Fin de limpiar las filas 
    }// Cierre de Limpiar tabla Cliente

    // Metodo Limpiar Proveedores
    void limpiarProvedores() {
        txtRutProveedor.setText("");
        txtRazonSocialProveedor.setText("");
        txtNombreProveedor.setText("");
        txtTelefonoProveedor.setText("");
        txtEmailProveedor.setText("");
        txtDireccionProveedor.setText("");
        txtNumDireProveedor.setText("");
        cbxComunaProveedor.setSelectedIndex(0);
        txtComplementoProveedor.setText("");
        cbEstadoProveedor.setSelectedIndex(0);
    }

    /////////////      Fin de Proveedores             /////////////////////
    ///////////////////////////////// BANCO //////////////////////////////
    //Metodo Listar Banco
    void listarBanco() {

        try {
            PreparedStatement pst = cn.prepareStatement("select * from banco");
            ResultSet rs = pst.executeQuery();

            Object[] banco = new Object[3];

            modeloBanco = (DefaultTableModel) Tabla_Banco.getModel();

            while (rs.next()) {
                banco[0] = rs.getInt("id_banco");
                banco[1] = rs.getString("descripcion_Banco");
                banco[2] = rs.getString("estado_banco");

                modeloBanco.addRow(banco);
            }
            Tabla_Banco.setModel(modeloBanco);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al Conectar //  " + e);
        }

    }//cierre listar Banco

    //Metodo Guardar Banco    
    void guardarBanco() {
        try {
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
            PreparedStatement pst = cn.prepareStatement("insert into banco values(?,?,?)");
            int id = 0;
            pst.setInt(1, id);
            pst.setString(2, txtNombreBanco.getText().trim());
            pst.setString(3, cbEstadoBanco.getSelectedItem().toString());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Guardo El Banco Correctamente  ", "AVISO", JOptionPane.INFORMATION_MESSAGE);
            limpiartablaBanco();// Limpia la tabla 
            limpiarBanco();// limpiar campos de textos

        }// cierre del bloque try
        catch (Exception ex) {

            // mensaje error al grabar
            JOptionPane.showMessageDialog(null, "Error al intentar guardad El Banco  // " + ex, "AVISO", JOptionPane.ERROR_MESSAGE);
            limpiarBanco();
            limpiartablaBanco();
            ex.printStackTrace();
        }// cierre del catch
    }// Cierre guardar Banco

    //Metodo modificar Banco
    void modificarBanco() {

        try {

            String id1 = txtCodigoBanco.getText();
            int id2 = Integer.parseInt(id1);
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
            PreparedStatement pst = cn.prepareStatement("update banco set descripcion_Banco=?, estado_banco=? where id_banco=" + id2);

            pst.setString(1, txtNombreBanco.getText().trim());
            pst.setString(2, cbEstadoBanco.getSelectedItem().toString());

            pst.executeUpdate();
            limpiarBanco();

            JOptionPane.showMessageDialog(null, "Datos del Banco actualizados  ", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            limpiartablaBanco();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar  //  " + e);
            limpiarBanco();
        }
    }// Cierre Modifica Banco

    //Eliminar Banco
    void elimianrBanco() {
        int filaselecionado = Tabla_Banco.getSelectedRow();
        if (filaselecionado == -1) {
            JOptionPane.showMessageDialog(null, "Debe selecionar Fila");
        } else {
            try {
                String id1 = txtCodigoBanco.getText();
                int id2 = Integer.parseInt(id1);
                String URL_bd = "jdbc:mysql://localhost/mydb";
                String usuario = "root";// este usuario es por default de mysql
                String contraseña = "";// depende de como entre a la consola de mysql
                Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
                PreparedStatement pst = cn.prepareStatement("delete from banco where id_banco=" + "'" + id2 + "'");
                int validar = JOptionPane.showConfirmDialog(null, "¿Estas seguro que deseas eliminar el registro?  ", "PREGUNTA", JOptionPane.YES_NO_OPTION);
                if (validar == 0) {
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Registro eliminado ");
                    limpiarComuna();
                    limpiartablaComuna();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar registro  //  " + e);
                limpiarComuna();
            }

        }
    }// cierre Eliminar Banco

    // Metodo Limpiar Banco 
    public void limpiarBanco() {
        txtCodigoBanco.setText("");
        txtNombreBanco.setText("");
        cbEstadoBanco.setSelectedIndex(0);

    }// Cierre limpiar 

    //Metodo Limpiar la tabla de Banco
    public void limpiartablaBanco() {
        DefaultTableModel model1 = (DefaultTableModel) Tabla_Banco.getModel();
        while (Tabla_Banco.getRowCount() > 0) {
            model1.removeRow(0);
        }//Fin de limpiar las filas 
    }// Cierre de Limpiar tabla Usuario

    ////////////////////        Fin BANCO               /////////////////////
    /////////////////////         Categoria             //////////////////
    //listar Categoria
    void listarCat() {

        try {
            PreparedStatement pst = cn.prepareStatement("select * from categoria");
            ResultSet rs = pst.executeQuery();

            Object[] categ = new Object[3];

            modeloCat = (DefaultTableModel) Tabla_CategoriaArt.getModel();

            while (rs.next()) {
                categ[0] = rs.getInt("id");
                categ[1] = rs.getString("nombre_categoria");
                categ[2] = rs.getString("estado");

                modeloCat.addRow(categ);
            }
            Tabla_CategoriaArt.setModel(modeloCat);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al Conectar  " + e);
        }
    }

    //metodo Guardar Cateoria
    void guardarCatVenta() {
        try {
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
            PreparedStatement pst = cn.prepareStatement("insert into estado_venta values(?,?,?)");
            int id = 0;
            pst.setInt(1, id);
            pst.setString(2, txtCatVenta.getText().trim());
            pst.setString(3, cbEstadoCatVenta.getSelectedItem().toString());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Guardo Correctamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);
            limpiarTablaCatVenta();// limpia la tabla 
            limpiarCatVenta();// limpiar campos de textos

        }// cierre del bloque try
        catch (Exception ex) {

            // mensaje error al grabar
            JOptionPane.showMessageDialog(null, "Error al intentar guardar " + ex, "AVISO", JOptionPane.ERROR_MESSAGE);
            limpiarTablaCatVenta();// limpia la tabla 
            limpiarCatVenta();// limpiar campos de textos
            ex.printStackTrace();
        }// cierre del catch
    }//cierre metodo Guardar Cateoria

    //metodo Modificar Categoria Venta
    void ModificarCatVenta() {
        try {
            String id1 = txtCodigoCatVenta.getText();
            int id2 = Integer.parseInt(id1);
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
            PreparedStatement pst = cn.prepareStatement("update estado_venta set descripcion=?, estado=? where id=" + id2);

            pst.setString(1, txtCatVenta.getText().trim());
            pst.setString(2, cbEstadoCatVenta.getSelectedItem().toString());

            pst.executeUpdate();
            limpiarCatVenta();

            JOptionPane.showMessageDialog(null, "Datos de la categoria actualizados", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            limpiarTablaCatVenta();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar " + e);
            limpiarCatVenta();
        }
    }// cierre Modificar Categoria Venta

    //Eliminar Categoria Venta
    void elimianrCatVenta() {
        int filaselecionado = Tabla_CatVenta.getSelectedRow();
        if (filaselecionado == -1) {
            JOptionPane.showMessageDialog(null, "Debe selecionar Fila");
        } else {
            try {
                String id1 = txtCodigoCatVenta.getText();
                int id2 = Integer.parseInt(id1);
                String URL_bd = "jdbc:mysql://localhost/mydb";
                String usuario = "root";// este usuario es por default de mysql
                String contraseña = "";// depende de como entre a la consola de mysql
                Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
                PreparedStatement pst = cn.prepareStatement("delete from estado_venta where id=" + "'" + id2 + "'");
                int validar = JOptionPane.showConfirmDialog(null, "¿Estas seguro que deseas eliminar el registro?  ", "PREGUNTA", JOptionPane.YES_NO_OPTION);
                if (validar == 0) {
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Registro eliminado ");
                    limpiarCategoria();
                    limpiarTablaCat();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar registro  //  " + e);
                limpiarCategoria();
            }

        }
    }// cierre Eliminar Categoria Venta

    //Limpiar la tabla de  Categoria
    public void limpiarTablaCatVenta() {
        DefaultTableModel model2 = (DefaultTableModel) Tabla_CatVenta.getModel();
        while (Tabla_CatVenta.getRowCount() > 0) {
            model2.removeRow(0);
        }//Fin de limpiar las filas 
    }// Cierre de Limpiar tabla Categoria

    // Metodo Limpiar Categoria
    void limpiarCatVenta() {
        txtCodigoCatVenta.setText("");
        txtCatVenta.setText("");
        cbEstadoCatVenta.setSelectedIndex(0);

    }

    /////////////////////    Fin Categoria Art        /////////////////////////
    /////////////////////         Categoria Venta             //////////////////
    //listar Categoria Venta
    void listarCatVenta() {

        try {
            PreparedStatement pst = cn.prepareStatement("select * from estado_venta");
            ResultSet rs = pst.executeQuery();

            Object[] categ = new Object[3];

            modeloCatVenta = (DefaultTableModel) Tabla_CatVenta.getModel();

            while (rs.next()) {
                categ[0] = rs.getInt("id");
                categ[1] = rs.getString("descripcion");
                categ[2] = rs.getString("estado");

                modeloCatVenta.addRow(categ);
            }
            Tabla_CatVenta.setModel(modeloCatVenta);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al Conectar  " + e);
        }
    }

    //metodo Guardar Cateoria
    void guardarCategoria() {
        try {
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
            PreparedStatement pst = cn.prepareStatement("insert into estado_venta values(?,?,?)");
            int id = 0;
            pst.setInt(1, id);
            pst.setString(2, txtCatVenta.getText().trim());
            pst.setString(3, cbEstadoCatVenta.getSelectedItem().toString());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Guardo Correctamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);
            limpiarTablaCatVenta();// limpia la tabla 
            limpiarCatVenta();// limpiar campos de textos

        }// cierre del bloque try
        catch (Exception ex) {

            // mensaje error al grabar
            JOptionPane.showMessageDialog(null, "Error al intentar guardar " + ex, "AVISO", JOptionPane.ERROR_MESSAGE);
            limpiarTablaCatVenta();// limpia la tabla 
            limpiarCatVenta();// limpiar campos de textos
            ex.printStackTrace();
        }// cierre del catch
    }//cierre metodo Guardar Cateoria

    //metodo Editar Categoria
    void editarCategoria() {
        try {
            String id1 = txtCodigoCategoria.getText();
            int id2 = Integer.parseInt(id1);
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
            PreparedStatement pst = cn.prepareStatement("update estado_venta set descripcion=?, estado=? where id=" + id2);

            pst.setString(1, txtCatVenta.getText().trim());
            pst.setString(2, cbEstadoCatVenta.getSelectedItem().toString());

            pst.executeUpdate();
            limpiarCatVenta();

            JOptionPane.showMessageDialog(null, "Datos de la categoria actualizados", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            limpiarTablaCatVenta();;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar " + e);
            limpiarCatVenta();
        }
    }// cierre metodo Editar Categoria

    //Eliminar Categoria Art
    void elimianrCategoria() {
        int filaselecionado = Tabla_CategoriaArt.getSelectedRow();
        if (filaselecionado == -1) {
            JOptionPane.showMessageDialog(null, "Debe selecionar Fila");
        } else {
            try {
                String id1 = txtCodigoCatVenta.getText();
                int id2 = Integer.parseInt(id1);
                String URL_bd = "jdbc:mysql://localhost/mydb";
                String usuario = "root";// este usuario es por default de mysql
                String contraseña = "";// depende de como entre a la consola de mysql
                Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
                PreparedStatement pst = cn.prepareStatement("delete from estado_venta where id=" + "'" + id2 + "'");
                int validar = JOptionPane.showConfirmDialog(null, "¿Estas seguro que deseas eliminar el registro?  ", "PREGUNTA", JOptionPane.YES_NO_OPTION);
                if (validar == 0) {
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Registro eliminado ");
                    limpiarCategoria();
                    limpiarTablaCat();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar registro  //  " + e);
                limpiarCategoria();
            }

        }
    }// cierre Eliminar Categoria

    //Limpiar la tabla de  Categoria
    public void limpiarTablaCat() {
        DefaultTableModel model2 = (DefaultTableModel) Tabla_CatVenta.getModel();
        while (Tabla_CatVenta.getRowCount() > 0) {
            model2.removeRow(0);
        }//Fin de limpiar las filas 
    }// Cierre de Limpiar tabla Categoria

    // Metodo Limpiar Categoria
    void limpiarCategoria() {
        txtCodigoCatVenta.setText("");
        txtCatVenta.setText("");
        cbEstadoCatVenta.setSelectedIndex(0);

    }

    /////////////////////    Fin Categoria Venta        //////////////////////
    /////////////////////         RRSS             //////////////////
    //listar Categoria Venta
    void listaRRSS() {

        try {
            PreparedStatement pst = cn.prepareStatement("select * from rrss");
            ResultSet rs = pst.executeQuery();

            Object[] categ = new Object[3];

            modeloRRSS = (DefaultTableModel) Tabla_RRSS.getModel();

            while (rs.next()) {
                categ[0] = rs.getInt("id_rrss");
                categ[1] = rs.getString("nombre");
                categ[2] = rs.getString("estado");

                modeloRRSS.addRow(categ);
            }
            Tabla_RRSS.setModel(modeloRRSS);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al Conectar  " + e);
        }
    }

    //metodo Guardar RRSS
    void guardarRRSS() {
        try {
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
            PreparedStatement pst = cn.prepareStatement("insert into rrss values(?,?,?)");
            int id = 0;
            pst.setInt(1, id);
            pst.setString(2, txtNombreRRSS.getText().trim());
            pst.setString(3, cbEstadoRRSS.getSelectedItem().toString());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Guardo Correctamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);
            limpiarTablaRRSS();// limpia la tabla 
            limpiarRRSS();// limpiar campos de textos

        }// cierre del bloque try
        catch (Exception ex) {

            // mensaje error al grabar
            JOptionPane.showMessageDialog(null, "Error al intentar guardar " + ex, "AVISO", JOptionPane.ERROR_MESSAGE);
            limpiarTablaRRSS();// limpia la tabla 
            limpiarRRSS();// limpiar campos de textos
            ex.printStackTrace();
        }// cierre del catch
    }//cierre metodo Guardar RRSS

    //metodo Editar RRSS
    void modificarRRSS() {
        try {
            String id1 = txtCodigoRRSS.getText();
            int id2 = Integer.parseInt(id1);
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
            PreparedStatement pst = cn.prepareStatement("update rrss set nombre=?, estado=? where id_rrss=" + id2);

            pst.setString(1, txtNombreRRSS.getText().trim());
            pst.setString(2, cbEstadoRRSS.getSelectedItem().toString());

            pst.executeUpdate();
            limpiarRRSS();

            JOptionPane.showMessageDialog(null, "Datos de la categoria actualizados", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            limpiarTablaRRSS();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar " + e);
            limpiarRRSS();
        }
    }// cierre metodo Editar RRSS

    //Eliminar RRSS
    void elimianrRRSS() {
        int filaselecionado = Tabla_RRSS.getSelectedRow();
        if (filaselecionado == -1) {
            JOptionPane.showMessageDialog(null, "Debe selecionar Fila");
        } else {
            try {
                String id1 = txtCodigoRRSS.getText();
                int id2 = Integer.parseInt(id1);
                String URL_bd = "jdbc:mysql://localhost/mydb";
                String usuario = "root";// este usuario es por default de mysql
                String contraseña = "";// depende de como entre a la consola de mysql
                Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
                PreparedStatement pst = cn.prepareStatement("delete from rrss where id_rrss=" + "'" + id2 + "'");
                int validar = JOptionPane.showConfirmDialog(null, "¿Estas seguro que deseas eliminar el registro?  ", "PREGUNTA", JOptionPane.YES_NO_OPTION);
                if (validar == 0) {
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Registro eliminado ");
                    limpiarRRSS();
                    limpiarTablaRRSS();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar registro  //  " + e);
                limpiarRRSS();
            }

        }
    }// cierre Eliminar Categoria

    //Limpiar la tabla de  Categoria
    public void limpiarTablaRRSS() {
        DefaultTableModel model2 = (DefaultTableModel) Tabla_RRSS.getModel();
        while (Tabla_RRSS.getRowCount() > 0) {
            model2.removeRow(0);
        }//Fin de limpiar las filas 
    }// Cierre de Limpiar tabla Categoria

    // Metodo Limpiar Categoria
    void limpiarRRSS() {
        txtCodigoRRSS.setText("");
        txtNombreRRSS.setText("");
        cbEstadoRRSS.setSelectedIndex(0);

    }

    /////////////////////    Fin RRSS        //////////////////////
    /////////////////////         Articulos             //////////////////
    //Metodo listar Articulo
    void listaArticulos() {

        try {

            PreparedStatement pst = cn.prepareStatement("SELECT A.cod_articulo, A.nombre, A.fecha_vencimiento, A.stock, A.estado, C.nombre_categoria FROM categoria as C, articulo as A where C.id = A.CATEGORIA_id ");
            ResultSet rs = pst.executeQuery();

            Object[] categ = new Object[6];

            modeloArticulo = (DefaultTableModel) Tabla_Articulos.getModel();

            while (rs.next()) {
                categ[0] = rs.getInt("A.cod_articulo");
                categ[1] = rs.getString("A.nombre");
                categ[2] = rs.getString("C.nombre_categoria");
                categ[3] = rs.getInt("A.stock");
                categ[4] = rs.getString("A.fecha_vencimiento");
                categ[5] = rs.getString("A.estado");

                modeloArticulo.addRow(categ);
            }
            Tabla_Articulos.setModel(modeloArticulo);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al Conectar  " + e);
        }

    }    // cierre metodo listar Articulo

    //metodo Guardar RRSS
    void guardarArticulos() {

        int id_cat = 0;
        String nombrecat = cbxArticulo.getSelectedItem().toString();

        try {
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);

            // buscamos el codigo de categoria
            PreparedStatement pst1 = cn.prepareStatement("select id from categoria where nombre_categoria ='" + nombrecat + "'");
            ResultSet rs1 = pst1.executeQuery();
            if (rs1.next()) {
                id_cat = rs1.getInt("id");
            }

            PreparedStatement pst = cn.prepareStatement("insert into articulo values(?,?,?,?,?,?)");
            int id = 0;
            pst.setInt(1, id);
            pst.setString(2, txtNombreArticulo.getText().trim());
            pst.setString(3, ((JTextField) fechaVencimientoArt.getDateEditor().getUiComponent()).getText());
            pst.setString(4, txtArtStock.getText().trim());
            pst.setString(5, cbEstadoArt.getSelectedItem().toString());
            pst.setInt(6, id_cat);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Guardo Correctamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);
            limpiarTablaArticulos();// limpia la tabla 
            limpiarArticulos();// limpiar campos de textos

        }// cierre del bloque try
        catch (Exception ex) {

            // mensaje error al grabar
            JOptionPane.showMessageDialog(null, "Error al intentar guardar " + ex, "AVISO", JOptionPane.ERROR_MESSAGE);
            limpiarTablaArticulos();// limpia la tabla 
            limpiarArticulos();// limpiar campos de textos
            ex.printStackTrace();
        }// cierre del catch

    }//cierre metodo Guardar RRSS

    //metodo Modificar Articulo
    void modificarArticulos() {

        int id_cat = 0;
        String nombrecat = cbxArticulo.getSelectedItem().toString();

        try {
            String id1 = txtCodArticulo.getText();
            int id2 = Integer.parseInt(id1);
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);

            // buscamos el codigo de categoria
            PreparedStatement pst1 = cn.prepareStatement("select id from categoria where nombre_categoria ='" + nombrecat + "'");
            ResultSet rs1 = pst1.executeQuery();
            if (rs1.next()) {
                id_cat = rs1.getInt("id");
            }

            PreparedStatement pst = cn.prepareStatement("update articulo set nombre=?, fecha_vencimiento=?, stock=?, estado=?, CATEGORIA_id=? where cod_articulo=" + id2);

            pst.setString(1, txtNombreArticulo.getText().trim());
            pst.setString(2, ((JTextField) fechaVencimientoArt.getDateEditor().getUiComponent()).getText());
            pst.setString(3, txtArtStock.getText().trim());
            pst.setString(4, cbEstadoArt.getSelectedItem().toString());
            pst.setInt(5, id_cat);

            pst.executeUpdate();
            limpiarArticulos();

            JOptionPane.showMessageDialog(null, "Datos del Articulo se a actualizado", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            limpiarTablaArticulos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar " + e);
            limpiarArticulos();
        }
    }// cierre metodo modificar Articulo

    //Eliminar Articulo
    void elimianrArticulos() {
        int filaselecionado = Tabla_Articulos.getSelectedRow();
        if (filaselecionado == -1) {
            JOptionPane.showMessageDialog(null, "Debe selecionar Fila");
        } else {
            try {
                String id1 = txtCodArticulo.getText();
                int id2 = Integer.parseInt(id1);
                String URL_bd = "jdbc:mysql://localhost/mydb";
                String usuario = "root";// este usuario es por default de mysql
                String contraseña = "";// depende de como entre a la consola de mysql
                Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
                PreparedStatement pst = cn.prepareStatement("delete from articulo where cod_articulo=" + "'" + id2 + "'");
                int validar = JOptionPane.showConfirmDialog(null, "¿Estas seguro que deseas eliminar el registro?  ", "PREGUNTA", JOptionPane.YES_NO_OPTION);
                if (validar == 0) {
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Registro eliminado ");
                    limpiarRRSS();
                    limpiarTablaRRSS();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar registro  //  " + e);
                limpiarRRSS();
            }

        }
    }// cierre Eliminar Articulo

    //Limpiar la tabla de  Articulo
    public void limpiarTablaArticulos() {
        DefaultTableModel model2 = (DefaultTableModel) Tabla_Articulos.getModel();
        while (Tabla_Articulos.getRowCount() > 0) {
            model2.removeRow(0);
        }//Fin de limpiar las filas 
    }// Cierre de Limpiar tabla Categoria

    // Metodo Limpiar Articulo
    void limpiarArticulos() {
        txtCodArticulo.setText("");
        txtNombreArticulo.setText("");
        cbxArticulo.setSelectedIndex(0);
        txtArtStock.setText("");
        fechaVencimientoArt.setDate(null);
        cbEstadoArt.setSelectedIndex(0);

    }

    /////////////////////    Fin Articulos        //////////////////////
    /////////////////////////////////////////////////////////////////
    //                   USUARIO                         //
    //LISTAR
    void listarUsuario() {

        try {
            PreparedStatement pst = cn.prepareStatement("select * from usuario");
            ResultSet rs = pst.executeQuery();

            Object[] usuario = new Object[5];

            modelo2 = (DefaultTableModel) tablaUsuario.getModel();

            while (rs.next()) {
                usuario[0] = rs.getInt("id");
                usuario[1] = rs.getString("nombre");
                usuario[2] = rs.getString("clave");
                usuario[3] = rs.getString("categoria");
                usuario[4] = rs.getString("estado");

                modelo2.addRow(usuario);
            }
            tablaUsuario.setModel(modelo2);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al Conectar  " + e);
        }

    }//cierre listar usuario

    //Guardar USUARIO    
    void guardarUsuario() {
        try {
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
            PreparedStatement pst = cn.prepareStatement("insert into usuario values(?,?,?,?,?)");
            int id = 0;
            pst.setInt(1, id);
            pst.setString(2, txtUsuario.getText().trim());
            pst.setString(3, txtClave.getText().trim());
            pst.setString(4, txtDepartamento.getText().trim());
            pst.setString(5, cbEstadoUsuario.getSelectedItem().toString());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se Guardo el Usuario Correctamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);
            limpiartabla2();
            limpiarUsuario();// limpiar campos de textos

        }// cierre del bloque try
        catch (Exception ex) {

            // mensaje error al grabar
            JOptionPane.showMessageDialog(null, "Error al intentar guardar al Usuario" + ex, "AVISO", JOptionPane.ERROR_MESSAGE);
            limpiarUsuario();
            limpiartabla2();
            ex.printStackTrace();
        }// cierre del catch
    }// Cierre guardar usuario

    //modificar USUARIO
    void modificarUsuario() {

        try {

            String id1 = txtIdUsuario.getText();
            int id2 = Integer.parseInt(id1);
            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
            PreparedStatement pst = cn.prepareStatement("update usuario set nombre=?,clave=?,categoria=?,estado=? where id=" + id2);

            pst.setString(1, txtUsuario.getText().trim());
            pst.setString(2, txtClave.getText().trim());
            pst.setString(3, txtDepartamento.getText().trim());
            pst.setString(4, cbEstadoUsuario.getSelectedItem().toString());

            pst.executeUpdate();
            limpiarUsuario();

            JOptionPane.showMessageDialog(null, "Datos del usuario actualizados", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            limpiartabla2();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar " + e);
        }
    }// Cierre Modifica usuario

    //Eliminar Usuario
    void elimianrUsuario() {
        int filaselecionado = tablaUsuario.getSelectedRow();
        if (filaselecionado == -1) {
            JOptionPane.showMessageDialog(null, "Debe selecionar Fila");
        } else {
            try {
                String id1 = txtIdUsuario.getText();
                int id2 = Integer.parseInt(id1);
                String URL_bd = "jdbc:mysql://localhost/mydb";
                String usuario = "root";// este usuario es por default de mysql
                String contraseña = "";// depende de como entre a la consola de mysql
                Connection cn = DriverManager.getConnection(URL_bd, usuario, contraseña);
                PreparedStatement pst = cn.prepareStatement("delete from usuario where id=" + "'" + id2 + "'");
                int validar = JOptionPane.showConfirmDialog(null, "¿Estas seguro que deseas eliminar el registro?", "PREGUNTA", JOptionPane.YES_NO_OPTION);
                if (validar == 0) {
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Registro eliminado ");
                    limpiarUsuario();
                    limpiartabla2();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar registro " + e);
            }

        }
    }// cierre Eliminar Usuario

    //Buscar Usuario
    /* void buscar() {
        try {

            String URL_bd = "jdbc:mysql://localhost/mydb";
            String usuario = "root";// este usuario es por default de mysql
            String contraseña = "";// depende de como entre a la consola de mysql
            Connection conexion = DriverManager.getConnection(URL_bd, usuario, contraseña);
            PreparedStatement pst = conexion.prepareStatement("select * from usuario where id = ? or nombre=?");
            pst.setString(1, txtbuscarUsuario.getText().trim());

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                txtIdUsuario.setText(rs.getString("id"));
                txtUsuario.setText(rs.getString("nombre"));
                txtClave.setText(rs.getString("clave"));
                txtDepartamento.setText(rs.getString("categoria"));

            } else {
                JOptionPane.showMessageDialog(null, "No encontrado");
                int a = JOptionPane.showConfirmDialog(null, "¿Deseas agregar un nuevo registro?", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                if (a == 0) {
                    avilitarCampos();
                    jTextRut.setEditable(false);
                    jTextDv.setEditable(false);
                    jTextNombre.requestFocus();
                } else {
                    jTextRut.requestFocus();
                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "erro al realizar la busqueda" + e);
        }
    } // Cierre Buscar Usuario */
    public void limpiarUsuario() {
        txtUsuario.setText("");
        txtClave.setText("");
        txtDepartamento.setText("");
        cbEstadoUsuario.setSelectedIndex(0);

    }// Cierre limpiar 

    //Limpiar la tabla de USUARIO
    public void limpiartabla2() {
        DefaultTableModel model1 = (DefaultTableModel) tablaUsuario.getModel();
        while (tablaUsuario.getRowCount() > 0) {
            model1.removeRow(0);
        }//Fin de limpiar las filas 
    }// Cierre de Limpiar tabla Usuario

    ////////////////////////FIN DE USUARIO//////////////////////////
    ///////////////////           PACK          /////////////////////////////
    //listar Pack
    void listarPack() {
        try {
            PreparedStatement pst = cn.prepareStatement("select * from pack");
            ResultSet rs = pst.executeQuery();

            Object[] categ = new Object[5];

            modeloPack = (DefaultTableModel) Tabla_PackListar.getModel();

            while (rs.next()) {
                categ[0] = rs.getInt("cod_pack");
                categ[1] = rs.getString("nombre");
                categ[2] = rs.getInt("precio");
                categ[3] = rs.getString("stock_pack");
                categ[4] = rs.getString("estado");

                modeloPack.addRow(categ);
            }
            Tabla_PackListar.setModel(modeloPack);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al Conectar  " + e);
        }

    }//cerrar listar Pack

    //listar2 Pack
    void listar2Pack() {

        try {
            PreparedStatement pst = cn.prepareStatement("select nombre, stock from articulo");
            ResultSet rs = pst.executeQuery();

            Object[] categ = new Object[2];

            modeloPack2 = (DefaultTableModel) tabla_Pack1.getModel();

            while (rs.next()) {
                categ[0] = rs.getString("nombre");
                categ[1] = rs.getInt("stock");

                modeloPack2.addRow(categ);
            }
            tabla_Pack1.setModel(modeloPack2);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al Conectar  " + e);
        }

    }//cerrar listar2 Pack

    //metodo agregar artivculos al pack
    void agregarArtPack() {
        //declaracion de variables
        int cantidad = 0, stock_Art1 = 0, resta = 0, idstock = 0;
        String nombreArt = "";
        cantidad = Integer.parseInt(txtUnidades.getText());
        int fila = tabla_Pack1.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "No Seleccionado");
            limpiarTablaPack2();
        } else if (fila >= 0) {
            nombreArt = (String) tabla_Pack1.getValueAt(fila, 0).toString();
            stock_Art1 = Integer.parseInt((String) tabla_Pack1.getValueAt(fila, 1).toString());
            JOptionPane.showMessageDialog(null, "datos " + nombreArt + " " + stock_Art1);

            if (stock_Art1 == 0) {
                JOptionPane.showConfirmDialog(null, "No hay Stock Suficiente", "CANCELAR", JOptionPane.YES_NO_CANCEL_OPTION);

            } else if (stock_Art1 > 0) {
                resta = stock_Art1 - cantidad;

                JOptionPane.showMessageDialog(null, "la resta es " + resta);

                try {

                    // buscamos el codigo de articulo
                    PreparedStatement pst1 = cn.prepareStatement("select cod_articulo from articulo where nombre ='" + nombreArt + "'");
                    ResultSet rs1 = pst1.executeQuery();
                    if (rs1.next()) {
                        idstock = rs1.getInt("cod_articulo");
                    }

                    // actualiza la cantidad restando el stock al articulo
                    PreparedStatement pst = cn.prepareStatement("update articulo set stock=? where cod_articulo=" + idstock);
                    pst.setInt(1, resta);
                    pst.executeUpdate();

                    //le colocamos contenido a Tabla donde se guardara los Art
                    DefaultTableModel modelo2 = (DefaultTableModel) Tabla_Pack2.getModel();
                    Object[] row = new Object[2];
                    row[0] = nombreArt;
                    row[1] = cantidad;
                    modelo2.addRow(row);

                    limpiarTablaPack2();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error al modificar " + e);
                    limpiarTablaPack2();
                }

            } // cierre else   

        }
    }// cerrar Metoido Articulos al pack

    //metodo Quitamos articulo al pack
    void quitarArtPack() {
        //declaracion de variables
        int cantidad = 0, stock_Art1 = 0, resta = 0, idstock = 0;
        String nombreArt = "";
        cantidad = Integer.parseInt(txtUnidades.getText());
        int fila = Tabla_Pack2.getSelectedRow();
        int stoc = 0;

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "No Seleccionado");
        } else if (fila >= 0) {
            nombreArt = (String) Tabla_Pack2.getValueAt(fila, 0).toString();
            stock_Art1 = Integer.parseInt((String) Tabla_Pack2.getValueAt(fila, 1).toString());
            JOptionPane.showMessageDialog(null, "datos: " + nombreArt + " " + stock_Art1);

            if (stock_Art1 == 0) {
                JOptionPane.showConfirmDialog(null, "No hay Stock Suficiente", "CANCELAR", JOptionPane.YES_NO_CANCEL_OPTION);

            } else if (stock_Art1 > 0) {

                try {

                    // buscamos el codigo de articulo
                    PreparedStatement pst1 = cn.prepareStatement("select cod_articulo,stock from articulo where nombre ='" + nombreArt + "'");
                    ResultSet rs1 = pst1.executeQuery();
                    if (rs1.next()) {
                        idstock = rs1.getInt("cod_articulo");
                        stoc = rs1.getInt("stock");
                    }

                    resta = stock_Art1 + stoc;

                    // actualiza la cantidad restando el stock al articulo
                    PreparedStatement pst = cn.prepareStatement("update articulo set stock=? where cod_articulo=" + idstock);
                    pst.setInt(1, resta);
                    pst.executeUpdate();

                    //le colocamos contenido a Tabla donde se guardara los Art
                    DefaultTableModel modelo2 = (DefaultTableModel) Tabla_Pack2.getModel();
                    modelo2.removeRow(fila);
                    limpiarTablaPack2();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error al modificar " + e);
                    limpiarTablaPack2();
                }

            } // cierre else   

        }
    }// cerrar Quitar Articulos al pack  

    // guardar Pack
    void guardarPack() {

        String nombreAr = "";
        String nombrePPack = txtNombrePack.getText();
        boolean esta = false;
        int idpack = 0;
        int stoc = 0;
        int cantidad = 1;
        int idArticulo = 0;
        int stockP = 0;

        DefaultTableModel modelox = (DefaultTableModel) Tabla_PackListar.getModel();

        if (nombrePPack != null) {

            for (int i = 0; i < modelox.getRowCount(); i++) {
                nombreAr = Tabla_PackListar.getValueAt(i, 1).toString();
                if (nombreAr == nombrePPack) {
                    cantidad = (Integer.parseInt(Tabla_PackListar.getValueAt(i, 3).toString())) + 1;
                }
            }//cierre for 

            if (nombreAr == nombrePPack) {

                try {
                    //Actualizar el Stock
                    PreparedStatement pst = cn.prepareStatement("update stock_pack set pack where nombre='" + nombrePPack + "'");
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        pst.setInt(1, cantidad);
                    }

                    JOptionPane.showMessageDialog(null, "Se Guardo Correctamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);
                    limpiarTablaPack(); // limpia la tabla 
                    limpiarPack();// limpiar campos de textos
                    limpiarTablaPack3();

                } catch (Exception ex) {

                    // mensaje error al grabar
                    JOptionPane.showMessageDialog(null, "Error al intentar guardar " + ex, "AVISO", JOptionPane.ERROR_MESSAGE);
                    limpiarTablaPack(); // limpia la tabla 
                    limpiarPack();// limpiar campos de textos
                    limpiarTablaPack3();

                }// cierre del catch

            } else {

                try {

                    PreparedStatement pst = cn.prepareStatement("insert into pack values(?,?,?,?,?)");
                    int id = 0;
                    pst.setInt(1, id);
                    pst.setString(2, txtNombrePack.getText().trim());
                    pst.setInt(3, Integer.parseInt(txtPrecioPack.getText().trim()));
                    pst.setInt(4, cantidad);
                    pst.setString(5, cbEstadoRRSS.getSelectedItem().toString());

                    pst.executeUpdate();

                    PreparedStatement pst1 = cn.prepareStatement("select cod_pack from pack where nombre='" + nombrePPack + "'");
                    ResultSet rs1 = pst1.executeQuery();
                    if (rs1.next()) {
                        idpack = rs1.getInt("cod_pack");

                    }

                    DefaultTableModel modelox2 = (DefaultTableModel) Tabla_Pack2.getModel();

                    for (int i = 0; i < modelox2.getRowCount(); i++) {
                        String nombArticulo = Tabla_Pack2.getValueAt(i, 0).toString();
                        int CantArt1 = Integer.parseInt(Tabla_Pack2.getValueAt(i, 1).toString());

                        PreparedStatement pst3 = cn.prepareStatement("select cod_articulo from articulo where nombre='" + nombArticulo + "'");
                        ResultSet rs3 = pst3.executeQuery();
                        if (rs3.next()) {
                            idArticulo = rs3.getInt("cod_articulo");

                        }

                        PreparedStatement pst2 = cn.prepareStatement("insert into pack_has_articulo values(?,?,?)");

                        pst2.setInt(1, idpack);
                        pst2.setInt(2, idArticulo);
                        pst2.setInt(3, CantArt1);

                        pst2.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(null, "Se Guardo Correctamente", "AVISO", JOptionPane.INFORMATION_MESSAGE);
                    limpiarTablaPack(); // limpia la tabla 
                    limpiarPack();// limpiar campos de textos
                    limpiarTablaPack3();

                }// cierre del bloque try
                catch (Exception ex) {

                    // mensaje error al grabar
                    JOptionPane.showMessageDialog(null, "Error al intentar guardar " + ex, "AVISO", JOptionPane.ERROR_MESSAGE);
                    limpiarTablaPack(); // limpia la tabla 
                    limpiarPack();// limpiar campos de textos
                    limpiarTablaPack3();
                }// cierre del catch
            }//else     
        } // Cierre If

    }//cerrar guardar Pack

    // Modificar Pack
    void modificarPack() {
        
        try {

            String id1 = txtCodigoPack.getText();
            int id2 = Integer.parseInt(id1);
            
            PreparedStatement pst = cn.prepareStatement("update pack set nombre=?,precio=?,estado=? where cod_pack=" + id2);

            pst.setString(1, txtNombrePack.getText().trim());
            pst.setString(2, txtPrecioPack.getText().trim());
            pst.setString(4, cbEstadoPack.getSelectedItem().toString());

            pst.executeUpdate();
            
            
           /* DefaultTableModel modelox2 = (DefaultTableModel) Tabla_Pack2.getModel();
            
            PreparedStatement pst2 = cn.prepareStatement("update pack_has_articulo  set cantidad=? where PACK_cod_pack =" + id2);

            pst2.setString(1, txtNombrePack.getText().trim());
            pst2.setString(2, txtPrecioPack.getText().trim());
            pst2.setString(4, cbEstadoPack.getSelectedItem().toString());

            pst2.executeUpdate();*/
            
            
            
            
            limpiarPack();

            JOptionPane.showMessageDialog(null, "Datos del usuario actualizados", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            limpiarTablaPack();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar " + e);
        }
        
        

    }//cerrar Modificar Pack

    //limpiar caja textos
    void limpiarPack() {
        txtCodigoPack.setText("");
        txtNombrePack.setText("");
        txtCantidadPack.setText("");
        txtPrecioPack.setText("");
        cbEstadoPack.setSelectedIndex(0);

    }//cerrar limpiar caja textos

    void limpiarTablaPack3() {
        DefaultTableModel model1 = (DefaultTableModel) Tabla_Pack2.getModel();
        while (Tabla_Pack2.getRowCount() > 0) {
            model1.removeRow(0);
        }
    }

    void limpiarTablaPack2() {
        DefaultTableModel model1 = (DefaultTableModel) tabla_Pack1.getModel();
        while (tabla_Pack1.getRowCount() > 0) {
            model1.removeRow(0);
        }
    }

    // limpiar Tabla Pack
    void limpiarTablaPack() {
        DefaultTableModel model1 = (DefaultTableModel) Tabla_PackListar.getModel();
        while (Tabla_PackListar.getRowCount() > 0) {
            model1.removeRow(0);
        }//Fin de limpiar las filas 

    }//cerrar limpiar Tabla Pack

    ///////////////////////// FIN DE PACK  //////////////////////////////
    //
    //////////////////////  FIN PACK  ///////////////////////////
    //
    ///////////////////// VENTAS  ///////////////////////////////
    
    void llenarComboBoxPackVenta(){
        
        cbListaPack.removeAllItems();
        
        try {
            PreparedStatement pst = cn.prepareStatement("select nombre from pack");
            ResultSet rs = pst.executeQuery();

            ArrayList<String> lista = new ArrayList<String>();
            
            

            while (rs.next()) {
                lista.add(rs.getString("nombre"));
            }
            
            for(int i=0 ; i < lista.size(); i++){

            cbListaPack.addItem(lista.get(i));

           }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al Conectar  " + e);
        }
        
         
        
    }
    
    
    
    
    
    
    
    
    /////////////////////  Fin VENTAS  ////////////////////////////
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jFrame1 = new javax.swing.JFrame();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jFrame2 = new javax.swing.JFrame();
        jFrame3 = new javax.swing.JFrame();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu9 = new javax.swing.JMenu();
        jMenu10 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem2 = new javax.swing.JCheckBoxMenuItem();
        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu13 = new javax.swing.JMenu();
        jMenu14 = new javax.swing.JMenu();
        jMenuBar4 = new javax.swing.JMenuBar();
        jMenu15 = new javax.swing.JMenu();
        jMenu16 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jPopupMenu3 = new javax.swing.JPopupMenu();
        jFrame4 = new javax.swing.JFrame();
        jPopupMenu4 = new javax.swing.JPopupMenu();
        jMenuBar5 = new javax.swing.JMenuBar();
        jMenu17 = new javax.swing.JMenu();
        jMenu18 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jPopupMenu5 = new javax.swing.JPopupMenu();
        menuBar1 = new java.awt.MenuBar();
        menu1 = new java.awt.Menu();
        menu2 = new java.awt.Menu();
        jPopupMenu6 = new javax.swing.JPopupMenu();
        jPopupMenu7 = new javax.swing.JPopupMenu();
        jPopupMenu8 = new javax.swing.JPopupMenu();
        jFrame5 = new javax.swing.JFrame();
        jFrame6 = new javax.swing.JFrame();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        Titulo_Prov = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla_Provedores = new javax.swing.JTable();
        jTextField11 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jSeparator56 = new javax.swing.JSeparator();
        dreamGifts = new javax.swing.JLabel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        ventas = new javax.swing.JTabbedPane();
        jPanel20 = new javax.swing.JPanel();
        Titulo_Prov10 = new javax.swing.JLabel();
        jSeparator24 = new javax.swing.JSeparator();
        jLabel50 = new javax.swing.JLabel();
        txtNumeroPedido = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        txtNombreClienteVenta = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        txtEmailClienteVenta = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        txtRutVentaCliente = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        txttelefonClienteVenta = new javax.swing.JTextField();
        jButton58 = new javax.swing.JButton();
        jButton61 = new javax.swing.JButton();
        Titulo_Prov11 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        txtNombreDetinatario = new javax.swing.JTextField();
        jTextField55 = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jButton66 = new javax.swing.JButton();
        jButton67 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel63 = new javax.swing.JLabel();
        cbListaPack = new javax.swing.JComboBox<>();
        jLabel64 = new javax.swing.JLabel();
        jTextField58 = new javax.swing.JTextField();
        jComboBox5 = new javax.swing.JComboBox<>();
        jComboBox6 = new javax.swing.JComboBox<>();
        jComboBox7 = new javax.swing.JComboBox<>();
        jLabel65 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        cbFechaEntregaDestinatario = new com.toedter.calendar.JDateChooser();
        jPanel22 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator26 = new javax.swing.JSeparator();
        jSeparator27 = new javax.swing.JSeparator();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField7 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jLabel93 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        FechaNacimientoCliente1 = new com.toedter.calendar.JDateChooser();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jSeparator28 = new javax.swing.JSeparator();
        jSeparator29 = new javax.swing.JSeparator();
        jSeparator30 = new javax.swing.JSeparator();
        jLabel94 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel96 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jLabel97 = new javax.swing.JLabel();
        jSeparator31 = new javax.swing.JSeparator();
        jSeparator32 = new javax.swing.JSeparator();
        jSeparator33 = new javax.swing.JSeparator();
        compras = new javax.swing.JTabbedPane();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jScrollPane19 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jSeparator34 = new javax.swing.JSeparator();
        jSeparator35 = new javax.swing.JSeparator();
        jSeparator36 = new javax.swing.JSeparator();
        jLabel98 = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jButton56 = new javax.swing.JButton();
        jButton57 = new javax.swing.JButton();
        jButton59 = new javax.swing.JButton();
        jButton60 = new javax.swing.JButton();
        jTextField19 = new javax.swing.JTextField();
        jTextField21 = new javax.swing.JTextField();
        FechaNacimientoCliente2 = new com.toedter.calendar.JDateChooser();
        jPanel26 = new javax.swing.JPanel();
        jSeparator37 = new javax.swing.JSeparator();
        jSeparator38 = new javax.swing.JSeparator();
        jSeparator39 = new javax.swing.JSeparator();
        jSeparator40 = new javax.swing.JSeparator();
        jSeparator41 = new javax.swing.JSeparator();
        jSeparator42 = new javax.swing.JSeparator();
        jScrollPane20 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jButton69 = new javax.swing.JButton();
        jButton70 = new javax.swing.JButton();
        jButton71 = new javax.swing.JButton();
        jButton72 = new javax.swing.JButton();
        jButton73 = new javax.swing.JButton();
        jButton74 = new javax.swing.JButton();
        jLabel103 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        jLabel106 = new javax.swing.JLabel();
        jLabel107 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jTextField22 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        FechaNacimientoCliente3 = new com.toedter.calendar.JDateChooser();
        jLabel108 = new javax.swing.JLabel();
        jLabel109 = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jLabel111 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel112 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jLabel113 = new javax.swing.JLabel();
        jTextField25 = new javax.swing.JTextField();
        jLabel114 = new javax.swing.JLabel();
        FechaNacimientoCliente4 = new com.toedter.calendar.JDateChooser();
        jLabel115 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jSeparator43 = new javax.swing.JSeparator();
        jLabel116 = new javax.swing.JLabel();
        jSeparator44 = new javax.swing.JSeparator();
        jComboBox8 = new javax.swing.JComboBox<>();
        jLabel117 = new javax.swing.JLabel();
        jTextField26 = new javax.swing.JTextField();
        jLabel118 = new javax.swing.JLabel();
        jLabel119 = new javax.swing.JLabel();
        jTextField29 = new javax.swing.JTextField();
        jLabel120 = new javax.swing.JLabel();
        FechaNacimientoCliente5 = new com.toedter.calendar.JDateChooser();
        jButton75 = new javax.swing.JButton();
        jButton76 = new javax.swing.JButton();
        jSeparator45 = new javax.swing.JSeparator();
        jScrollPane21 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jButton77 = new javax.swing.JButton();
        jButton78 = new javax.swing.JButton();
        jLabel121 = new javax.swing.JLabel();
        jScrollPane22 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable();
        jLabel122 = new javax.swing.JLabel();
        informes = new javax.swing.JTabbedPane();
        jPanel28 = new javax.swing.JPanel();
        jSeparator46 = new javax.swing.JSeparator();
        jSeparator47 = new javax.swing.JSeparator();
        jSeparator48 = new javax.swing.JSeparator();
        jScrollPane23 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable();
        FechaNacimientoCliente6 = new com.toedter.calendar.JDateChooser();
        FechaNacimientoCliente7 = new com.toedter.calendar.JDateChooser();
        jLabel123 = new javax.swing.JLabel();
        jLabel124 = new javax.swing.JLabel();
        jLabel125 = new javax.swing.JLabel();
        jLabel126 = new javax.swing.JLabel();
        jLabel127 = new javax.swing.JLabel();
        jTextField30 = new javax.swing.JTextField();
        jTextField31 = new javax.swing.JTextField();
        jLabel128 = new javax.swing.JLabel();
        jButton79 = new javax.swing.JButton();
        jLabel129 = new javax.swing.JLabel();
        jTextField32 = new javax.swing.JTextField();
        jLabel130 = new javax.swing.JLabel();
        jButton80 = new javax.swing.JButton();
        jPanel29 = new javax.swing.JPanel();
        jSeparator49 = new javax.swing.JSeparator();
        jLabel131 = new javax.swing.JLabel();
        jSeparator50 = new javax.swing.JSeparator();
        jLabel132 = new javax.swing.JLabel();
        FechaNacimientoCliente8 = new com.toedter.calendar.JDateChooser();
        jLabel133 = new javax.swing.JLabel();
        FechaNacimientoCliente9 = new com.toedter.calendar.JDateChooser();
        jButton81 = new javax.swing.JButton();
        jSeparator51 = new javax.swing.JSeparator();
        jLabel134 = new javax.swing.JLabel();
        jScrollPane24 = new javax.swing.JScrollPane();
        jTable11 = new javax.swing.JTable();
        jButton82 = new javax.swing.JButton();
        jLabel136 = new javax.swing.JLabel();
        FechaNacimientoCliente10 = new com.toedter.calendar.JDateChooser();
        jLabel135 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        jLabel138 = new javax.swing.JLabel();
        jComboBox9 = new javax.swing.JComboBox<>();
        jComboBox10 = new javax.swing.JComboBox<>();
        jPanel30 = new javax.swing.JPanel();
        jSeparator52 = new javax.swing.JSeparator();
        jLabel139 = new javax.swing.JLabel();
        jSeparator53 = new javax.swing.JSeparator();
        jLabel140 = new javax.swing.JLabel();
        jLabel141 = new javax.swing.JLabel();
        FechaNacimientoCliente11 = new com.toedter.calendar.JDateChooser();
        FechaNacimientoCliente12 = new com.toedter.calendar.JDateChooser();
        jLabel142 = new javax.swing.JLabel();
        jTextField33 = new javax.swing.JTextField();
        jLabel143 = new javax.swing.JLabel();
        jLabel144 = new javax.swing.JLabel();
        jTextField35 = new javax.swing.JTextField();
        jButton83 = new javax.swing.JButton();
        jSeparator54 = new javax.swing.JSeparator();
        jLabel145 = new javax.swing.JLabel();
        jScrollPane25 = new javax.swing.JScrollPane();
        jTable12 = new javax.swing.JTable();
        jButton84 = new javax.swing.JButton();
        jPanel31 = new javax.swing.JPanel();
        jSeparator55 = new javax.swing.JSeparator();
        jLabel146 = new javax.swing.JLabel();
        jLabel147 = new javax.swing.JLabel();
        jLabel148 = new javax.swing.JLabel();
        FechaNacimientoCliente13 = new com.toedter.calendar.JDateChooser();
        FechaNacimientoCliente14 = new com.toedter.calendar.JDateChooser();
        jLabel149 = new javax.swing.JLabel();
        jTextField36 = new javax.swing.JTextField();
        jLabel150 = new javax.swing.JLabel();
        jLabel151 = new javax.swing.JLabel();
        jTextField38 = new javax.swing.JTextField();
        jButton85 = new javax.swing.JButton();
        jSeparator57 = new javax.swing.JSeparator();
        jLabel152 = new javax.swing.JLabel();
        jScrollPane26 = new javax.swing.JScrollPane();
        jTable13 = new javax.swing.JTable();
        jButton86 = new javax.swing.JButton();
        maestros = new javax.swing.JTabbedPane();
        clientes = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        Titulo_Prov1 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        txtNombreCliente = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtTelefonoCliente = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtEmailCliente = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtRutCliente = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtCelularCliente = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel25 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TablaClientes = new javax.swing.JTable();
        txtBuscar = new javax.swing.JTextField();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        txtDvCliente = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        cbRedSocialCliente = new javax.swing.JComboBox<>();
        FechaNacimientoCliente = new com.toedter.calendar.JDateChooser();
        cbEstadoCliente = new javax.swing.JComboBox<>();
        jLabel30 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        txtDireccionCliente = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        Titulo_Prov12 = new javax.swing.JLabel();
        jSeparator25 = new javax.swing.JSeparator();
        jLabel56 = new javax.swing.JLabel();
        txtRutProveedor = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        txtNombreProveedor = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        txtDireccionProveedor = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        txtRazonSocialProveedor = new javax.swing.JTextField();
        jLabel68 = new javax.swing.JLabel();
        txtTelefonoProveedor = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        txtEmailProveedor = new javax.swing.JTextField();
        jButton62 = new javax.swing.JButton();
        jButton63 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel70 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tabla_Provedores1 = new javax.swing.JTable();
        txtBuscarProv = new javax.swing.JTextField();
        jButton64 = new javax.swing.JButton();
        jButton65 = new javax.swing.JButton();
        jButton68 = new javax.swing.JButton();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        txtNumDireProveedor = new javax.swing.JTextField();
        jLabel73 = new javax.swing.JLabel();
        txtComplementoProveedor = new javax.swing.JTextField();
        cbxComunaProveedor = new javax.swing.JComboBox<>();
        jLabel77 = new javax.swing.JLabel();
        cbEstadoProveedor = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        Titulo_Prov2 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        txtNombreArticulo = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        Tabla_Articulos = new javax.swing.JTable();
        txtBuscarArticulo = new javax.swing.JTextField();
        jButton23 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        btnEliminarArticulo = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        txtCodArticulo = new javax.swing.JTextField();
        cbxArticulo = new javax.swing.JComboBox<>();
        fechaVencimientoArt = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        txtArtStock = new javax.swing.JTextField();
        cbEstadoArt = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        Titulo_Prov3 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel17 = new javax.swing.JLabel();
        txtNombrePack = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jButton24 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JSeparator();
        jLabel38 = new javax.swing.JLabel();
        txtBuscarPack = new javax.swing.JTextField();
        txtPrecioPack = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtUnidades = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jScrollPane15 = new javax.swing.JScrollPane();
        tabla_Pack1 = new javax.swing.JTable();
        jScrollPane16 = new javax.swing.JScrollPane();
        Tabla_Pack2 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        Tabla_PackListar = new javax.swing.JTable();
        jButton29 = new javax.swing.JButton();
        jLabel82 = new javax.swing.JLabel();
        cbEstadoPack = new javax.swing.JComboBox<>();
        jLabel83 = new javax.swing.JLabel();
        txtCodigoPack = new javax.swing.JTextField();
        jLabel84 = new javax.swing.JLabel();
        txtCantidadPack = new javax.swing.JTextField();
        jLabel153 = new javax.swing.JLabel();
        txtlimpiarArtlista = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        Titulo_Prov4 = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        txtNombreRRSS = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jSeparator13 = new javax.swing.JSeparator();
        jLabel39 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        Tabla_RRSS = new javax.swing.JTable();
        txtBuscarRRSS = new javax.swing.JTextField();
        jButton33 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        txtCodigoRRSS = new javax.swing.JTextField();
        cbEstadoRRSS = new javax.swing.JComboBox<>();
        jLabel31 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        Titulo_Prov5 = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        jLabel19 = new javax.swing.JLabel();
        txtNombreCategoria = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jSeparator15 = new javax.swing.JSeparator();
        jLabel40 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        Tabla_CategoriaArt = new javax.swing.JTable();
        txtBuscarCat = new javax.swing.JTextField();
        jButton38 = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        txtCodigoCategoria = new javax.swing.JTextField();
        cbEstadoCategoria = new javax.swing.JComboBox<>();
        jLabel79 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        Titulo_Prov6 = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JSeparator();
        jLabel20 = new javax.swing.JLabel();
        txtNombreComuna = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jButton41 = new javax.swing.JButton();
        jButton42 = new javax.swing.JButton();
        jSeparator17 = new javax.swing.JSeparator();
        jLabel42 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        Tabla_Comuna = new javax.swing.JTable();
        txtBuscarComuna = new javax.swing.JTextField();
        jButton43 = new javax.swing.JButton();
        jButton44 = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        txtCodigoComuna = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        Titulo_Prov7 = new javax.swing.JLabel();
        jSeparator18 = new javax.swing.JSeparator();
        jLabel33 = new javax.swing.JLabel();
        txtNombreBanco = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jButton46 = new javax.swing.JButton();
        jButton47 = new javax.swing.JButton();
        jSeparator19 = new javax.swing.JSeparator();
        jLabel44 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        Tabla_Banco = new javax.swing.JTable();
        txtBuscarBanco = new javax.swing.JTextField();
        jButton48 = new javax.swing.JButton();
        jButton49 = new javax.swing.JButton();
        jButton50 = new javax.swing.JButton();
        txtCodigoBanco = new javax.swing.JTextField();
        cbEstadoBanco = new javax.swing.JComboBox<>();
        jLabel78 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        Titulo_Prov8 = new javax.swing.JLabel();
        jSeparator20 = new javax.swing.JSeparator();
        jLabel45 = new javax.swing.JLabel();
        txtCatVenta = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jButton51 = new javax.swing.JButton();
        jButton52 = new javax.swing.JButton();
        jSeparator21 = new javax.swing.JSeparator();
        jLabel47 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        Tabla_CatVenta = new javax.swing.JTable();
        txtBuscarCatVent = new javax.swing.JTextField();
        jButton53 = new javax.swing.JButton();
        jButton54 = new javax.swing.JButton();
        jButton55 = new javax.swing.JButton();
        txtCodigoCatVenta = new javax.swing.JTextField();
        cbEstadoCatVenta = new javax.swing.JComboBox<>();
        jLabel80 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        Titulo_Prov9 = new javax.swing.JLabel();
        jSeparator22 = new javax.swing.JSeparator();
        jLabel12 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        txtClave = new javax.swing.JTextField();
        txtDepartamento = new javax.swing.JTextField();
        btnCancelarUsuario = new javax.swing.JButton();
        btnGuardarUsuario = new javax.swing.JButton();
        jSeparator23 = new javax.swing.JSeparator();
        jLabel53 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tablaUsuario = new javax.swing.JTable();
        txtbuscarUsuario = new javax.swing.JTextField();
        btnbuscarUsuario = new javax.swing.JButton();
        btnEditarUsuario = new javax.swing.JButton();
        btnDesactivarUsuario = new javax.swing.JButton();
        jLabel74 = new javax.swing.JLabel();
        txtIdUsuario = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        cbEstadoUsuario = new javax.swing.JComboBox<>();
        jLabel85 = new javax.swing.JLabel();

        jMenu1.setText("jMenu1");

        jMenu2.setText("jMenu2");

        jMenu3.setText("jMenu3");

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jMenu4.setText("jMenu4");

        jMenu5.setText("jMenu5");

        jMenu6.setText("jMenu6");

        javax.swing.GroupLayout jFrame2Layout = new javax.swing.GroupLayout(jFrame2.getContentPane());
        jFrame2.getContentPane().setLayout(jFrame2Layout);
        jFrame2Layout.setHorizontalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame2Layout.setVerticalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jFrame3Layout = new javax.swing.GroupLayout(jFrame3.getContentPane());
        jFrame3.getContentPane().setLayout(jFrame3Layout);
        jFrame3Layout.setHorizontalGroup(
            jFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame3Layout.setVerticalGroup(
            jFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jMenu9.setText("File");
        jMenuBar2.add(jMenu9);

        jMenu10.setText("Edit");
        jMenuBar2.add(jMenu10);

        jMenuItem1.setText("jMenuItem1");

        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("jRadioButtonMenuItem1");

        jCheckBox1.setText("jCheckBox1");

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        jCheckBoxMenuItem2.setSelected(true);
        jCheckBoxMenuItem2.setText("jCheckBoxMenuItem2");

        jMenu13.setText("File");
        jMenuBar3.add(jMenu13);

        jMenu14.setText("Edit");
        jMenuBar3.add(jMenu14);

        jMenu15.setText("File");
        jMenuBar4.add(jMenu15);

        jMenu16.setText("Edit");
        jMenuBar4.add(jMenu16);

        jMenuItem2.setText("jMenuItem2");

        javax.swing.GroupLayout jFrame4Layout = new javax.swing.GroupLayout(jFrame4.getContentPane());
        jFrame4.getContentPane().setLayout(jFrame4Layout);
        jFrame4Layout.setHorizontalGroup(
            jFrame4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame4Layout.setVerticalGroup(
            jFrame4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jMenu17.setText("File");
        jMenuBar5.add(jMenu17);

        jMenu18.setText("Edit");
        jMenuBar5.add(jMenu18);

        jMenuItem3.setText("jMenuItem3");

        menu1.setLabel("File");
        menuBar1.add(menu1);

        menu2.setLabel("Edit");
        menuBar1.add(menu2);

        javax.swing.GroupLayout jFrame5Layout = new javax.swing.GroupLayout(jFrame5.getContentPane());
        jFrame5.getContentPane().setLayout(jFrame5Layout);
        jFrame5Layout.setHorizontalGroup(
            jFrame5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame5Layout.setVerticalGroup(
            jFrame5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jFrame6Layout = new javax.swing.GroupLayout(jFrame6.getContentPane());
        jFrame6.getContentPane().setLayout(jFrame6Layout);
        jFrame6Layout.setHorizontalGroup(
            jFrame6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame6Layout.setVerticalGroup(
            jFrame6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jMenuItem4.setText("jMenuItem4");

        jMenu11.setText("jMenu11");

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        Titulo_Prov.setText("Proveedores");

        jLabel5.setText("Rut Proveedor");

        jTextField4.setText("70.123.345-6");
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel6.setText("Nombre Contacto");

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jLabel7.setText("Direccion");

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jLabel8.setText("Razón Social");

        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jLabel9.setText("Teléfono");

        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });

        jLabel10.setText("E-Mail");

        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });

        jButton6.setText("Cancelar");

        jButton7.setText("Guardar");

        jLabel11.setText("PROVEEDORES");

        Tabla_Provedores.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tabla_Provedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Razón Social", "Nombre Contacto", "Teléfono", "E-Mail", "Acción"
            }
        ));
        Tabla_Provedores.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(Tabla_Provedores);
        Tabla_Provedores.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jTextField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField11ActionPerformed(evt);
            }
        });

        jButton8.setText("Buscar");

        jButton12.setText("Comprar");

        jButton13.setText("Editar");

        jButton14.setText("Desactivar");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jSeparator3)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Titulo_Prov)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(35, 35, 35)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(51, 51, 51)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField10)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jButton6)
                                                .addGap(26, 26, 26)
                                                .addComponent(jButton7)
                                                .addGap(0, 105, Short.MAX_VALUE)))))))
                        .addContainerGap(22, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(356, 356, 356)
                        .addComponent(jLabel11)
                        .addGap(129, 129, 129)
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton8)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton12)
                .addGap(18, 18, 18)
                .addComponent(jButton13)
                .addGap(18, 18, 18)
                .addComponent(jButton14)
                .addGap(54, 54, 54))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addGap(76, 76, 76)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Titulo_Prov)
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton12)
                    .addComponent(jButton13)
                    .addComponent(jButton14))
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(460, Short.MAX_VALUE)))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dreamGifts.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        dreamGifts.setText("Dream Gifts");
        getContentPane().add(dreamGifts, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 11, -1, -1));

        Titulo_Prov10.setText("Datos Cliente Solicitante ");

        jLabel50.setText("Número Pedido");

        txtNumeroPedido.setEditable(false);
        txtNumeroPedido.setEnabled(false);
        txtNumeroPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroPedidoActionPerformed(evt);
            }
        });

        jLabel51.setText("Nombre Cliente");

        txtNombreClienteVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreClienteVentaActionPerformed(evt);
            }
        });

        jLabel52.setText("E-Mail");

        txtEmailClienteVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailClienteVentaActionPerformed(evt);
            }
        });

        jLabel54.setText("Rut");

        txtRutVentaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRutVentaClienteActionPerformed(evt);
            }
        });

        jLabel55.setText("Teléfono");

        txttelefonClienteVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttelefonClienteVentaActionPerformed(evt);
            }
        });

        jButton58.setText("Cancelar");

        jButton61.setText("Guardar");

        Titulo_Prov11.setText("Datos Destinatario ");

        jLabel58.setText("Nombre Destinatario");

        jLabel59.setText("Fecha de Entrega");

        jLabel60.setText("Dirección");

        txtNombreDetinatario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreDetinatarioActionPerformed(evt);
            }
        });

        jTextField55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField55ActionPerformed(evt);
            }
        });

        jLabel61.setText("Pack");

        jLabel62.setText("Hora Inicio Entrega");

        jButton66.setText("Cancelar");

        jButton67.setText("Guardar");

        jLabel63.setText("Comuna");

        cbListaPack.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pack-01 - Cumpleaños", "Pack-02 - Dia del Niño", "Pack-03 - Dia del Padre" }));

        jLabel64.setText("Saludo");

        jTextField58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField58ActionPerformed(evt);
            }
        });

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Las Condes", "Santiago", "San Ramón", "Maipú" }));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", " " }));

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00" }));

        jLabel65.setText("Hora Fin Entrega");

        jLabel1.setText("Sub - Total");

        jLabel2.setText("Envios");

        jLabel3.setText("TOTAL");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        cbFechaEntregaDestinatario.setToolTipText("");
        cbFechaEntregaDestinatario.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNombreDetinatario, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20))
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel20Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField55, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel20Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addComponent(jTextField58, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel20Layout.createSequentialGroup()
                                .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbFechaEntregaDestinatario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(113, 113, 113)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cbListaPack, 0, 165, Short.MAX_VALUE)
                                    .addComponent(jComboBox6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel20Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(46, 46, 46)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
                                    .addGroup(jPanel20Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(47, 47, 47)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
                                    .addGroup(jPanel20Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(26, 26, 26)
                                        .addComponent(jTextField1))
                                    .addComponent(jComboBox7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                                .addComponent(jButton66)
                                .addGap(18, 18, 18)
                                .addComponent(jButton67)
                                .addGap(44, 44, 44))))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel20Layout.createSequentialGroup()
                                .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtEmailClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(62, 62, 62))
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtNombreClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(txtNumeroPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(131, 131, 131)))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addGap(88, 88, 88)
                                .addComponent(jButton58)
                                .addGap(26, 26, 26)
                                .addComponent(jButton61))
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel20Layout.createSequentialGroup()
                                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtRutVentaCliente))
                                .addGroup(jPanel20Layout.createSequentialGroup()
                                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txttelefonClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel20Layout.createSequentialGroup()
                            .addComponent(Titulo_Prov11)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSeparator1))
                        .addGroup(jPanel20Layout.createSequentialGroup()
                            .addComponent(Titulo_Prov10)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSeparator24, javax.swing.GroupLayout.PREFERRED_SIZE, 675, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(302, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Titulo_Prov10)
                    .addComponent(jSeparator24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(txtNumeroPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54)
                    .addComponent(txtRutVentaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(txtNombreClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55)
                    .addComponent(txttelefonClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(txtEmailClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton58)
                    .addComponent(jButton61))
                .addGap(10, 10, 10)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Titulo_Prov11)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(txtNombreDetinatario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61)
                    .addComponent(cbListaPack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel59)
                        .addComponent(jLabel62)
                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cbFechaEntregaDestinatario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel60)
                    .addComponent(jTextField55, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel64)
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton66)
                            .addComponent(jButton67))
                        .addGap(54, 54, 54))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField58, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        ventas.addTab("Ventas", jPanel20);

        jLabel86.setText("Confirmar Pago Cliente");

        jLabel87.setText("Rut");

        jLabel88.setText("Numero de Pedido");

        jLabel89.setText("Banco");

        jLabel90.setText("Fecha ");

        jLabel91.setText("Codigo de Transaccion");

        jLabel92.setText("Nombre Cliente");

        jButton2.setText("Cancelar");

        jButton3.setText("Confirmar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(jTable1);

        jTextField15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField15ActionPerformed(evt);
            }
        });

        jLabel93.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel93.setText("Ventas Pendientes de Pago");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        FechaNacimientoCliente1.setToolTipText("");
        FechaNacimientoCliente1.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 1053, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(59, 59, 59)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel22Layout.createSequentialGroup()
                                        .addComponent(jLabel88)
                                        .addGap(41, 41, 41)
                                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel87)
                                            .addComponent(jLabel89))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextField12, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel91)
                                    .addComponent(jLabel92)
                                    .addComponent(jLabel90))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField13, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                                    .addComponent(jTextField14, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                                    .addComponent(FechaNacimientoCliente1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGap(0, 36, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel93)
                .addGap(144, 144, 144)
                .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(196, 196, 196))
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator27)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jLabel86)
                        .addGap(43, 43, 43)
                        .addComponent(jSeparator26))))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jSeparator26, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel86)))
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel88)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel87))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel92))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel90)
                                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(FechaNacimientoCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel91)
                            .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel89)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(29, 29, 29)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(18, 18, 18)
                .addComponent(jSeparator27, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel93))
                .addGap(17, 17, 17)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
        );

        ventas.addTab("Confirmacion", jPanel22);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane8.setViewportView(jTable2);

        jLabel94.setText("Despacho");

        jLabel95.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel95.setText("Lista Destino para Despacho por Día");

        jButton4.setText("Descargar");

        jButton5.setText("Imprimir");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jSeparator28, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel94, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(jSeparator29, javax.swing.GroupLayout.DEFAULT_SIZE, 914, Short.MAX_VALUE))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 1057, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(jSeparator30, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                        .addComponent(jLabel95)
                        .addGap(156, 156, 156)
                        .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(167, 167, 167))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addGap(130, 130, 130)
                        .addComponent(jButton5)
                        .addGap(305, 305, 305))))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator29, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator28, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel94)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel95))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jSeparator30, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addGap(123, 123, 123))
        );

        ventas.addTab("Lista Destino", jPanel23);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane14.setViewportView(jTable3);

        jLabel96.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel96.setText("Actualizacion Estado Despacho ");

        jButton11.setText("Descargar");

        jButton17.setText("Imprimir");

        jLabel97.setText("Despacho");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jSeparator32, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel97, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(jSeparator31, javax.swing.GroupLayout.DEFAULT_SIZE, 914, Short.MAX_VALUE))
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 1057, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(jSeparator33, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel96)
                        .addGap(156, 156, 156)
                        .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(167, 167, 167))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                        .addComponent(jButton11)
                        .addGap(130, 130, 130)
                        .addComponent(jButton17)
                        .addGap(305, 305, 305))))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator31, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator32, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel97)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel96))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jSeparator33, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton11)
                    .addComponent(jButton17))
                .addGap(123, 123, 123))
        );

        ventas.addTab("Despachado", jPanel24);

        jTabbedPane4.addTab("Ventas", ventas);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane17.setViewportView(jTable4);
        if (jTable4.getColumnModel().getColumnCount() > 0) {
            jTable4.getColumnModel().getColumn(1).setHeaderValue("Title 2");
            jTable4.getColumnModel().getColumn(2).setHeaderValue("Title 3");
            jTable4.getColumnModel().getColumn(3).setResizable(false);
            jTable4.getColumnModel().getColumn(3).setHeaderValue("Title 4");
        }

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1"
            }
        ));
        jScrollPane18.setViewportView(jTable5);

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1"
            }
        ));
        jScrollPane19.setViewportView(jTable6);

        jLabel98.setText("Solicitud de pedido");

        jLabel99.setText("Numero Pedido");

        jLabel100.setText("Fecha de Pedido");

        jLabel101.setText("Unidad");

        jLabel102.setText("jLabel98");

        jButton20.setText("Cancelar");

        jButton26.setText("Guardar");

        jButton30.setText(">>");

        jButton56.setText("<<");

        jButton57.setText("Editar");

        jButton59.setText("Ver");

        jButton60.setText("Generar OC");

        jTextField21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField21ActionPerformed(evt);
            }
        });

        FechaNacimientoCliente2.setToolTipText("");
        FechaNacimientoCliente2.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator36)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator34, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel98)
                        .addGap(49, 49, 49)
                        .addComponent(jSeparator35))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 1064, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jLabel99)
                                .addGap(14, 14, 14)
                                .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(77, 77, 77)
                                .addComponent(jLabel100)
                                .addGap(18, 18, 18)
                                .addComponent(FechaNacimientoCliente2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addGap(286, 286, 286)
                                .addComponent(jLabel102))
                            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel25Layout.createSequentialGroup()
                                    .addGap(493, 493, 493)
                                    .addComponent(jButton20)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton26))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel25Layout.createSequentialGroup()
                                    .addGap(19, 19, 19)
                                    .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel25Layout.createSequentialGroup()
                                            .addGap(81, 81, 81)
                                            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jButton56)
                                                .addComponent(jButton30)))
                                        .addGroup(jPanel25Layout.createSequentialGroup()
                                            .addGap(35, 35, 35)
                                            .addComponent(jLabel101)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(63, 63, 63)
                                    .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 26, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton57)
                .addGap(64, 64, 64)
                .addComponent(jButton59)
                .addGap(72, 72, 72)
                .addComponent(jButton60)
                .addGap(202, 202, 202))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator34, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator35, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel98))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel99)
                        .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(FechaNacimientoCliente2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel100)))
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                            .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(18, 18, 18))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel101)
                            .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jButton30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                        .addComponent(jButton56)
                        .addGap(40, 40, 40)))
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton20)
                    .addComponent(jButton26))
                .addGap(15, 15, 15)
                .addComponent(jSeparator36, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel102)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton57)
                    .addComponent(jButton60)
                    .addComponent(jButton59))
                .addGap(7, 7, 7))
        );

        compras.addTab("Solicitud Pedido", jPanel25);

        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Articulo", "Cantidad", "Valor"
            }
        ));
        jScrollPane20.setViewportView(jTable7);

        jButton69.setText("Cancelar");

        jButton70.setText("Guardar");

        jButton71.setText("Cancelar");
        jButton71.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton71ActionPerformed(evt);
            }
        });

        jButton72.setText("Agregar Articulo");

        jButton73.setText("Editar");

        jButton74.setText("Eliminar");
        jButton74.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton74ActionPerformed(evt);
            }
        });

        jLabel103.setText("Registro de Factura de Proveedores");

        jLabel104.setText("Numero de Factura");

        jLabel105.setText("Rut");

        jLabel106.setText("Fecha de Recepcion");

        jLabel107.setText("Proveedor Razon Social");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        FechaNacimientoCliente3.setToolTipText("");
        FechaNacimientoCliente3.setDateFormatString("yyyy-MM-dd");

        jLabel108.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel108.setText("Registro de Detalle de Factura");

        jLabel109.setText("Registro de Detalle de Factura de Proveedor");

        jLabel110.setText("Codigo");

        jTextField23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField23ActionPerformed(evt);
            }
        });

        jLabel111.setText("Articulo");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel112.setText("Cantidad");

        jTextField24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField24ActionPerformed(evt);
            }
        });

        jLabel113.setText("Valor");

        jTextField25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField25ActionPerformed(evt);
            }
        });

        jLabel114.setText("Vencimiento");

        FechaNacimientoCliente4.setToolTipText("");
        FechaNacimientoCliente4.setDateFormatString("yyyy-MM-dd");

        jLabel115.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel115.setText("Detalle de Factura");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGap(371, 371, 371)
                        .addComponent(jLabel107)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGap(350, 350, 350)
                        .addComponent(jLabel108)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addComponent(jLabel106)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(FechaNacimientoCliente3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel26Layout.createSequentialGroup()
                                    .addComponent(jLabel104)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel26Layout.createSequentialGroup()
                                    .addComponent(jSeparator37, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel103))))
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSeparator38, javax.swing.GroupLayout.PREFERRED_SIZE, 826, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17))
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addGap(66, 66, 66)
                                .addComponent(jButton69)
                                .addGap(79, 79, 79)
                                .addComponent(jButton70)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator39)
                            .addComponent(jScrollPane20)
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addComponent(jSeparator42, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel109)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator40, javax.swing.GroupLayout.PREFERRED_SIZE, 825, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel105)
                                    .addComponent(jSeparator41, javax.swing.GroupLayout.PREFERRED_SIZE, 1084, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton72))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel26Layout.createSequentialGroup()
                                .addComponent(jLabel110)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel111)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel26Layout.createSequentialGroup()
                                        .addComponent(jButton71)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanel26Layout.createSequentialGroup()
                                        .addComponent(jLabel112)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField24)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel113)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel114)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(FechaNacimientoCliente4, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(218, 218, 218))))
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(395, 395, 395)
                .addComponent(jLabel115)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton73, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addComponent(jButton74, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(455, 455, 455))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator37, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator38, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel103))
                .addGap(18, 18, 18)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel104)
                    .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel107)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel105)
                    .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel106)
                    .addComponent(FechaNacimientoCliente3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton69)
                        .addComponent(jButton70)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator39, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel108)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator42, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSeparator40, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel109))
                .addGap(18, 18, 18)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel110)
                        .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel111)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel112)
                        .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel113)
                        .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel114))
                    .addComponent(FechaNacimientoCliente4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton71, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton72))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator41, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel115)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton74)
                    .addComponent(jButton73))
                .addGap(29, 29, 29))
        );

        compras.addTab("Registro Compra", jPanel26);

        jLabel116.setText("Revision de Factura Inventariadas");

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel117.setText("Proveedor Razon Social");

        jLabel118.setText("Numero de Factura");

        jLabel119.setText("Rut");

        jLabel120.setText("Fecha de Recepcion");

        FechaNacimientoCliente5.setToolTipText("");
        FechaNacimientoCliente5.setDateFormatString("yyyy-MM-dd");

        jButton75.setText("Cancelar");

        jButton76.setText("Buscar");

        jTable8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Articulo", "Cantidad", "Valor"
            }
        ));
        jScrollPane21.setViewportView(jTable8);

        jButton77.setText("Editar");

        jButton78.setText("Ver");
        jButton78.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton78ActionPerformed(evt);
            }
        });

        jLabel121.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel121.setText("Factura de Compra Inventariada");

        jTable9.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numero de Factura", "Fecha de Recepcion", "Razon Social", "Total"
            }
        ));
        jScrollPane22.setViewportView(jTable9);

        jLabel122.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel122.setText("Detalle de Factura");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addGap(361, 361, 361)
                                .addComponent(jLabel117)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel119))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel27Layout.createSequentialGroup()
                                        .addComponent(jLabel120)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(FechaNacimientoCliente5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel27Layout.createSequentialGroup()
                                            .addComponent(jLabel118)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel27Layout.createSequentialGroup()
                                            .addComponent(jSeparator43, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel116))))
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel27Layout.createSequentialGroup()
                                        .addGap(66, 66, 66)
                                        .addComponent(jButton75)
                                        .addGap(79, 79, 79)
                                        .addComponent(jButton76)
                                        .addGap(548, 548, 548))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSeparator44, javax.swing.GroupLayout.PREFERRED_SIZE, 764, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(69, 69, 69))))
                            .addComponent(jSeparator45, javax.swing.GroupLayout.PREFERRED_SIZE, 1075, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 1094, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGap(109, 109, 109)
                        .addComponent(jLabel122))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGap(118, 118, 118)
                        .addComponent(jLabel121)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton77, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(72, 72, 72)
                    .addComponent(jButton78, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(450, 450, 450)))
            .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel27Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 1094, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(50, Short.MAX_VALUE)))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator43, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator44, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel116))
                .addGap(18, 18, 18)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel118)
                    .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel117)
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel119)
                    .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel120)
                    .addComponent(FechaNacimientoCliente5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton75)
                        .addComponent(jButton76)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator45, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel121)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 194, Short.MAX_VALUE)
                .addComponent(jLabel122)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
            .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel27Layout.createSequentialGroup()
                    .addGap(322, 322, 322)
                    .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton78)
                        .addComponent(jButton77))
                    .addContainerGap(209, Short.MAX_VALUE)))
            .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                    .addContainerGap(252, Short.MAX_VALUE)
                    .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(203, 203, 203)))
        );

        compras.addTab("Revision de Facturas", jPanel27);

        jTabbedPane4.addTab("Compras", compras);

        jTable10.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numero Pedido", "Rut Cliente", "Nombre Cliente", "Fecha Compra", "Fecha Entrega", "Pack Comprado", "Monto"
            }
        ));
        jScrollPane23.setViewportView(jTable10);

        FechaNacimientoCliente6.setToolTipText("");
        FechaNacimientoCliente6.setDateFormatString("yyyy-MM-dd");

        FechaNacimientoCliente7.setToolTipText("");
        FechaNacimientoCliente7.setDateFormatString("yyyy-MM-dd");

        jLabel123.setText("Busqueda de Venta");

        jLabel124.setText("Busqueda Rango de Fecha Venta");

        jLabel125.setText("Desde");

        jLabel126.setText("Hasta");

        jLabel127.setText("Busqueda por Rut");

        jLabel128.setText("-");

        jButton79.setText("BUSCAR");

        jLabel129.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel129.setText("Detalle de Ventas Realizada");

        jLabel130.setText("Buscar");

        jButton80.setText("Descargar");
        jButton80.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton80ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane23, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator48)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jSeparator46, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel123)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator47))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                        .addGap(0, 105, Short.MAX_VALUE)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel124)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel127)
                                    .addComponent(jLabel125))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel28Layout.createSequentialGroup()
                                        .addComponent(FechaNacimientoCliente6, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel126))
                                    .addGroup(jPanel28Layout.createSequentialGroup()
                                        .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel128, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton79, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(FechaNacimientoCliente7, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))
                        .addGap(479, 479, 479)))
                .addContainerGap())
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(188, 188, 188)
                .addComponent(jLabel129)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel130)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(174, 174, 174))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton80, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator46, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel123)
                    .addComponent(jSeparator47, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(jLabel124)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(FechaNacimientoCliente7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(FechaNacimientoCliente6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel126, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel125))
                .addGap(27, 27, 27)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel127)
                    .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel128))
                .addGap(10, 10, 10)
                .addComponent(jButton79)
                .addGap(18, 18, 18)
                .addComponent(jSeparator48, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel129)
                    .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel130))
                .addGap(14, 14, 14)
                .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton80)
                .addContainerGap(75, Short.MAX_VALUE))
        );

        informes.addTab("Informe de Venta", jPanel28);

        jLabel131.setText("Informe Inventario");

        jLabel132.setText("Busqueda Rango de Fecha Venta");

        FechaNacimientoCliente8.setToolTipText("");
        FechaNacimientoCliente8.setDateFormatString("yyyy-MM-dd");

        jLabel133.setText("Hasta");

        FechaNacimientoCliente9.setToolTipText("");
        FechaNacimientoCliente9.setDateFormatString("yyyy-MM-dd");

        jButton81.setText("BUSCAR");

        jLabel134.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel134.setText("Detalle de Inventario");

        jTable11.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo de Articulo", "Nombre Articulo", "Stock", "Fecha Vencimiento", "Valor del Producto", "Categoria", "Rut Proveedor"
            }
        ));
        jScrollPane24.setViewportView(jTable11);

        jButton82.setText("Descargar");
        jButton82.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton82ActionPerformed(evt);
            }
        });

        jLabel136.setText("Desde");

        FechaNacimientoCliente10.setToolTipText("");
        FechaNacimientoCliente10.setDateFormatString("yyyy-MM-dd");

        jLabel135.setText("Categoria");

        jLabel137.setText("Fecha de Vencimiento");

        jLabel138.setText("Proveedor");

        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator49, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jLabel131)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator50)
                .addGap(4, 4, 4))
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel136)
                            .addComponent(jLabel133))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(FechaNacimientoCliente9, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FechaNacimientoCliente8, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel137)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(FechaNacimientoCliente10, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addGap(132, 132, 132)
                                .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel138)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel132)
                        .addGap(68, 68, 68)
                        .addComponent(jLabel135)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addContainerGap(519, Short.MAX_VALUE)
                .addComponent(jButton81, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(422, 422, 422))
            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel29Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane24, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jSeparator51)
                        .addGroup(jPanel29Layout.createSequentialGroup()
                            .addGap(188, 188, 188)
                            .addComponent(jLabel134)
                            .addGap(326, 710, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jButton82, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(89, 89, 89)))
                    .addContainerGap()))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator50, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator49, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel131))
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(FechaNacimientoCliente10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel132)
                            .addComponent(jLabel135))
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(FechaNacimientoCliente8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel136)))
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jLabel137)))
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel133)
                                    .addComponent(FechaNacimientoCliente9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel138)
                                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(18, 18, 18)
                .addComponent(jButton81)
                .addContainerGap())
            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel29Layout.createSequentialGroup()
                    .addGap(196, 196, 196)
                    .addComponent(jSeparator51, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel134)
                    .addGap(16, 16, 16)
                    .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jButton82)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        informes.addTab("Informe de Inventario", jPanel29);

        jLabel139.setText("Busqueda de Venta");

        jLabel140.setText("Busqueda Rango de Fecha Venta");

        jLabel141.setText("Desde");

        FechaNacimientoCliente11.setToolTipText("");
        FechaNacimientoCliente11.setDateFormatString("yyyy-MM-dd");

        FechaNacimientoCliente12.setToolTipText("");
        FechaNacimientoCliente12.setDateFormatString("yyyy-MM-dd");

        jLabel142.setText("Hasta");

        jLabel143.setText("Busqueda por Rut");

        jLabel144.setText("-");

        jButton83.setText("BUSCAR");

        jLabel145.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel145.setText("Detalle de Ventas Realizada");

        jTable12.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo Pack", "Pack", "Fecha de Registro", "Cliente", "Estado", "Comuna"
            }
        ));
        jScrollPane25.setViewportView(jTable12);

        jButton84.setText("Descargar");
        jButton84.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton84ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1130, Short.MAX_VALUE)
            .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel30Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane25, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jSeparator54)
                        .addGroup(jPanel30Layout.createSequentialGroup()
                            .addComponent(jSeparator52, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel139)
                            .addGap(18, 18, 18)
                            .addComponent(jSeparator53))
                        .addGroup(jPanel30Layout.createSequentialGroup()
                            .addGap(188, 188, 188)
                            .addComponent(jLabel145)
                            .addGap(672, 672, 672))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel140)
                                        .addGroup(jPanel30Layout.createSequentialGroup()
                                            .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel143)
                                                .addComponent(jLabel141))
                                            .addGap(18, 18, 18)
                                            .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel30Layout.createSequentialGroup()
                                                    .addComponent(FechaNacimientoCliente11, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(jLabel142))
                                                .addGroup(jPanel30Layout.createSequentialGroup()
                                                    .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jLabel144, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButton83, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(FechaNacimientoCliente12, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))
                                    .addGap(479, 479, 479))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                                    .addComponent(jButton84, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(89, 89, 89)))))
                    .addContainerGap()))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 554, Short.MAX_VALUE)
            .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel30Layout.createSequentialGroup()
                    .addGap(32, 32, 32)
                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jSeparator52, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel139)
                        .addComponent(jSeparator53, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(7, 7, 7)
                    .addComponent(jLabel140)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(FechaNacimientoCliente12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FechaNacimientoCliente11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel142, javax.swing.GroupLayout.Alignment.LEADING))
                        .addComponent(jLabel141))
                    .addGap(27, 27, 27)
                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel143)
                        .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel144))
                    .addGap(10, 10, 10)
                    .addComponent(jButton83)
                    .addGap(18, 18, 18)
                    .addComponent(jSeparator54, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel145)
                    .addGap(14, 14, 14)
                    .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jButton84)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        informes.addTab("Informe de Cliente", jPanel30);

        jLabel146.setText("Busqueda de Venta");

        jLabel147.setText("Busqueda Rango de Fecha Venta");

        jLabel148.setText("Desde");

        FechaNacimientoCliente13.setToolTipText("");
        FechaNacimientoCliente13.setDateFormatString("yyyy-MM-dd");

        FechaNacimientoCliente14.setToolTipText("");
        FechaNacimientoCliente14.setDateFormatString("yyyy-MM-dd");

        jLabel149.setText("Hasta");

        jLabel150.setText("Busqueda por Rut");

        jLabel151.setText("-");

        jButton85.setText("BUSCAR");

        jLabel152.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel152.setText("Detalle de Devoluciones y Cambios");

        jTable13.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Registro de ventas", "Pack", "Destinatario", "Fecha Entrega", "Comuna", "Hora de Entrega", "Devolucion"
            }
        ));
        jScrollPane26.setViewportView(jTable13);

        jButton86.setText("Descargar");
        jButton86.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton86ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1110, Short.MAX_VALUE)
            .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel31Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator57)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                            .addGap(0, 105, Short.MAX_VALUE)
                            .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel147)
                                        .addGroup(jPanel31Layout.createSequentialGroup()
                                            .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel150)
                                                .addComponent(jLabel148))
                                            .addGap(18, 18, 18)
                                            .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel31Layout.createSequentialGroup()
                                                    .addComponent(FechaNacimientoCliente13, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(jLabel149))
                                                .addGroup(jPanel31Layout.createSequentialGroup()
                                                    .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jLabel151, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButton85, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(FechaNacimientoCliente14, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))
                                    .addGap(479, 479, 479))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                                    .addComponent(jButton86, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(89, 89, 89))))
                        .addComponent(jScrollPane26)
                        .addGroup(jPanel31Layout.createSequentialGroup()
                            .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel31Layout.createSequentialGroup()
                                    .addComponent(jSeparator55, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel146))
                                .addGroup(jPanel31Layout.createSequentialGroup()
                                    .addGap(188, 188, 188)
                                    .addComponent(jLabel152)))
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 554, Short.MAX_VALUE)
            .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel31Layout.createSequentialGroup()
                    .addGap(32, 32, 32)
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jSeparator55, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel146))
                    .addGap(7, 7, 7)
                    .addComponent(jLabel147)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(FechaNacimientoCliente14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FechaNacimientoCliente13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel149, javax.swing.GroupLayout.Alignment.LEADING))
                        .addComponent(jLabel148))
                    .addGap(27, 27, 27)
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel150)
                        .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel151))
                    .addGap(10, 10, 10)
                    .addComponent(jButton85)
                    .addGap(18, 18, 18)
                    .addComponent(jSeparator57, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel152)
                    .addGap(14, 14, 14)
                    .addComponent(jScrollPane26, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jButton86)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        informes.addTab("Informe Dev. y Cambio", jPanel31);

        jTabbedPane4.addTab("Informes", informes);

        maestros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                maestrosMouseClicked(evt);
            }
        });

        Titulo_Prov1.setText("Clientes");

        jLabel13.setText("Nombre Cliente");

        txtNombreCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreClienteActionPerformed(evt);
            }
        });

        jLabel14.setText("Teléfono");

        txtTelefonoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoClienteActionPerformed(evt);
            }
        });

        jLabel21.setText("E- Mail");

        txtEmailCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailClienteActionPerformed(evt);
            }
        });

        jLabel22.setText("Rut");

        txtRutCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRutClienteActionPerformed(evt);
            }
        });

        jLabel23.setText("F. Nacimiento");

        jLabel24.setText("celular");

        txtCelularCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCelularClienteActionPerformed(evt);
            }
        });

        jButton15.setText("Cancelar");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setText("Guardar");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel25.setText("CLIENTES");

        TablaClientes.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        TablaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "rut", "dv", "Nombre", "Direccion", "Telefono", "Celular", "Fecha de Nacimiento", "Email", "Red Social", "Estado"
            }
        ));
        TablaClientes.setColumnSelectionAllowed(true);
        TablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaClientesMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TablaClientes);
        TablaClientes.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarKeyTyped(evt);
            }
        });

        jButton18.setText("Venta");

        jButton19.setText("Editar");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jLabel26.setText("-");

        txtDvCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDvClienteActionPerformed(evt);
            }
        });

        jLabel27.setText("Red Social");

        cbRedSocialCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "WhatsApp", "Instagram", "FaceBook", "Twitter", " " }));
        cbRedSocialCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRedSocialClienteActionPerformed(evt);
            }
        });

        FechaNacimientoCliente.setToolTipText("");
        FechaNacimientoCliente.setDateFormatString("yyyy-MM-dd");

        cbEstadoCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Desactivado" }));
        cbEstadoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEstadoClienteActionPerformed(evt);
            }
        });

        jLabel30.setText("Estado");

        jLabel75.setText("Dirección");

        txtDireccionCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionClienteActionPerformed(evt);
            }
        });

        jLabel76.setText("Buscar");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(524, 524, 524)
                .addComponent(jButton18)
                .addGap(47, 47, 47)
                .addComponent(jButton19)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Titulo_Prov1)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(332, 332, 332)
                                        .addComponent(jLabel25)
                                        .addGap(303, 303, 303)
                                        .addComponent(jLabel76)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1042, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 14, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel30)
                                        .addGap(22, 22, 22))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel75, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                                        .addGap(11, 11, 11)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtRutCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addComponent(cbEstadoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(37, 37, 37)
                                        .addComponent(jButton15)
                                        .addGap(26, 26, 26)
                                        .addComponent(jButton16))
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(cbRedSocialCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addComponent(txtDvCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(18, 18, 18)
                                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(FechaNacimientoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                                                .addComponent(txtTelefonoCliente))))))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtEmailCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCelularCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addGap(76, 76, 76)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Titulo_Prov1)
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(txtRutCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(txtDvCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel75)
                        .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23))
                    .addComponent(FechaNacimientoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(txtEmailCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCelularCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel27)
                        .addComponent(cbRedSocialCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton15)
                    .addComponent(jButton16)
                    .addComponent(cbEstadoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel76))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton18)
                    .addComponent(jButton19))
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(435, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout clientesLayout = new javax.swing.GroupLayout(clientes);
        clientes.setLayout(clientesLayout);
        clientesLayout.setHorizontalGroup(
            clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        clientesLayout.setVerticalGroup(
            clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        maestros.addTab("Clientes", clientes);

        Titulo_Prov12.setText("Proveedores");

        jLabel56.setText("Rut Proveedor");

        txtRutProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRutProveedorActionPerformed(evt);
            }
        });

        jLabel57.setText("Nombre Contacto");

        txtNombreProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreProveedorActionPerformed(evt);
            }
        });

        jLabel66.setText("Direccion");

        txtDireccionProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionProveedorActionPerformed(evt);
            }
        });

        jLabel67.setText("Razón Social");

        txtRazonSocialProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRazonSocialProveedorActionPerformed(evt);
            }
        });

        jLabel68.setText("Teléfono");

        txtTelefonoProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoProveedorActionPerformed(evt);
            }
        });

        jLabel69.setText("E-Mail");

        txtEmailProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailProveedorActionPerformed(evt);
            }
        });

        jButton62.setText("Cancelar");
        jButton62.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton62ActionPerformed(evt);
            }
        });

        jButton63.setText("Guardar");
        jButton63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton63ActionPerformed(evt);
            }
        });

        jLabel70.setText("PROVEEDORES");

        Tabla_Provedores1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tabla_Provedores1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "RutProve", "Razón Social", "Nombre Contacto", "Teléfono", "E-Mail", "Direccion", "Numero", "Comuna", "Complemento", "Estado"
            }
        ));
        Tabla_Provedores1.setColumnSelectionAllowed(true);
        Tabla_Provedores1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabla_Provedores1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(Tabla_Provedores1);
        Tabla_Provedores1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        txtBuscarProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarProvActionPerformed(evt);
            }
        });
        txtBuscarProv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarProvKeyReleased(evt);
            }
        });

        jButton64.setText("Buscar");

        jButton65.setText("Comprar");

        jButton68.setText("Editar");
        jButton68.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton68ActionPerformed(evt);
            }
        });

        jLabel71.setText("Comuna");

        jLabel72.setText("numero direccion");

        txtNumDireProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumDireProveedorActionPerformed(evt);
            }
        });

        jLabel73.setText("complemento");

        txtComplementoProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtComplementoProveedorActionPerformed(evt);
            }
        });

        cbxComunaProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Santiago", "Cerrillos", "Cerro Navia", "Conchali", "El Bosque", "Estacion Central", "Huechuraba", "Independencia", "La Cisterna", "La Florida", "La Granja", "La Pintana", "La Reina", "Las Condes", "Lo Barnechea", "Lo Espejo", "Lo Prado", "Macul", "Maipu", "Ñuñoa", "Padre Hurtado", "Pedro Aguirre Cerda", "Peñalolen", "Pirque", "Providencia", "Pudahuel", "Puente Alto", "Quilicura", "Quinta Normal", "Recoleta", "Renca", "San Bernardo", "San Joaquin", "San Jose de Maipo", "San Miguel", "San Ramon", " " }));
        cbxComunaProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxComunaProveedorActionPerformed(evt);
            }
        });

        jLabel77.setText("Estado");

        cbEstadoProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Desactivado" }));
        cbEstadoProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEstadoProveedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addComponent(jButton65)
                        .addGap(18, 18, 18)
                        .addComponent(jButton68)
                        .addGap(155, 155, 155))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addComponent(jButton62)
                        .addGap(405, 405, 405))))
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(356, 356, 356)
                        .addComponent(jLabel70)
                        .addGap(129, 129, 129)
                        .addComponent(txtBuscarProv, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton64)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator4)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel21Layout.createSequentialGroup()
                                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel21Layout.createSequentialGroup()
                                                .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(33, 33, 33)
                                                .addComponent(txtRutProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel21Layout.createSequentialGroup()
                                                .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtComplementoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanel21Layout.createSequentialGroup()
                                        .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtNumDireProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtRazonSocialProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(304, 304, 304))
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton63)
                                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(Titulo_Prov12)
                                        .addGroup(jPanel21Layout.createSequentialGroup()
                                            .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtNombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtTelefonoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel21Layout.createSequentialGroup()
                                            .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtDireccionProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtEmailProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel21Layout.createSequentialGroup()
                                            .addGap(393, 393, 393)
                                            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel77))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(cbEstadoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(cbxComunaProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                    .addGap(76, 76, 76)
                    .addComponent(jSeparator25, javax.swing.GroupLayout.DEFAULT_SIZE, 1376, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Titulo_Prov12)
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel67, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtRutProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel56)
                        .addComponent(txtRazonSocialProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(txtNombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefonoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel68))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(txtDireccionProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel69)
                    .addComponent(txtEmailProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel72)
                    .addComponent(txtNumDireProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel71)
                    .addComponent(cbxComunaProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtComplementoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel77)
                            .addComponent(cbEstadoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton62)
                            .addComponent(jButton63))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton64)
                    .addComponent(jLabel70))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton65)
                    .addComponent(jButton68))
                .addContainerGap())
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel21Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jSeparator25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(530, Short.MAX_VALUE)))
        );

        maestros.addTab("Proveedores", jPanel21);

        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
        });

        Titulo_Prov2.setText("Artículos");

        jLabel15.setText("Nombre Artículo");

        txtNombreArticulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreArticuloActionPerformed(evt);
            }
        });

        jLabel29.setText("Categoria Artículo");

        jButton21.setText("Cancelar");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton22.setText("Guardar");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jLabel32.setText("ARTICULOS");

        Tabla_Articulos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tabla_Articulos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo Art", "Nombre Art", "Categoria", "Stock", "Fech Vencimiento", "Estado   "
            }
        ));
        Tabla_Articulos.setColumnSelectionAllowed(true);
        Tabla_Articulos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabla_ArticulosMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(Tabla_Articulos);
        Tabla_Articulos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        txtBuscarArticulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarArticuloActionPerformed(evt);
            }
        });

        jButton23.setText("Buscar");

        jButton25.setText("Editar");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        btnEliminarArticulo.setText("Eliminar");
        btnEliminarArticulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarArticuloActionPerformed(evt);
            }
        });

        jLabel34.setText("Codigo Artículo");

        txtCodArticulo.setEditable(false);
        txtCodArticulo.setDisabledTextColor(new java.awt.Color(255, 255, 204));
        txtCodArticulo.setEnabled(false);
        txtCodArticulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodArticuloActionPerformed(evt);
            }
        });

        cbxArticulo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Accesorio", "Bebestible", "Comestible", " " }));

        fechaVencimientoArt.setToolTipText("");
        fechaVencimientoArt.setDateFormatString("yyyy-MM-dd");

        jLabel16.setText("Fecha de Vencimiento");

        jLabel81.setText("Stock");

        cbEstadoArt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Desactivado" }));
        cbEstadoArt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEstadoArtActionPerformed(evt);
            }
        });

        jLabel28.setText("Estado");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(356, 356, 356)
                        .addComponent(jLabel32)
                        .addGap(129, 129, 129)
                        .addComponent(txtBuscarArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton23)
                        .addGap(0, 46, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton25)
                .addGap(18, 18, 18)
                .addComponent(btnEliminarArticulo)
                .addGap(54, 54, 54))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator9)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(Titulo_Prov2)
                                .addGap(0, 765, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel81)
                            .addComponent(jLabel28))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNombreArticulo, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                            .addComponent(txtArtStock)
                            .addComponent(cbEstadoArt, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(139, 139, 139)
                                .addComponent(jButton21)
                                .addGap(26, 26, 26)
                                .addComponent(jButton22)
                                .addContainerGap(127, Short.MAX_VALUE))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cbxArticulo, 0, 156, Short.MAX_VALUE)
                                    .addComponent(txtCodArticulo)
                                    .addComponent(fechaVencimientoArt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                    .addGap(76, 76, 76)
                    .addComponent(jSeparator8, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Titulo_Prov2)
                .addGap(20, 20, 20)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtNombreArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(cbxArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCodArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34)
                    .addComponent(txtArtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel81))
                .addGap(15, 15, 15)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(fechaVencimientoArt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(cbEstadoArt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel28)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton21)
                    .addComponent(jButton22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton23)
                    .addComponent(jLabel32))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton25)
                    .addComponent(btnEliminarArticulo))
                .addContainerGap())
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(435, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(274, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        maestros.addTab("Artículos", jPanel4);

        Titulo_Prov3.setText("Packs");

        jLabel17.setText("Nombre Pack");

        txtNombrePack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombrePackActionPerformed(evt);
            }
        });

        jLabel35.setText("Precio Pack ");

        jButton24.setText("Cancelar");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jButton27.setText("Crear PACK");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jLabel38.setText("TABLA  PACKS");

        txtBuscarPack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarPackActionPerformed(evt);
            }
        });
        txtBuscarPack.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarPackKeyReleased(evt);
            }
        });

        txtPrecioPack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioPackActionPerformed(evt);
            }
        });

        jLabel4.setText("Unidades");

        txtUnidades.setText("1");

        jButton9.setText(">>");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("<<");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        tabla_Pack1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Articulo", "cantidad"
            }
        ));
        jScrollPane15.setViewportView(tabla_Pack1);

        Tabla_Pack2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Articulo", "cantidad"
            }
        ));
        jScrollPane16.setViewportView(Tabla_Pack2);

        Tabla_PackListar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tabla_PackListar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cdigo Pack", "Nombre Pack", "Precio", "Stock", "Estado"
            }
        ));
        Tabla_PackListar.setColumnSelectionAllowed(true);
        Tabla_PackListar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabla_PackListarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Tabla_PackListarMouseEntered(evt);
            }
        });
        jScrollPane5.setViewportView(Tabla_PackListar);
        Tabla_PackListar.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jButton29.setText("Editar");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jLabel82.setText("Estado");

        cbEstadoPack.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Desactivado" }));
        cbEstadoPack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEstadoPackActionPerformed(evt);
            }
        });

        jLabel83.setText("Codigo ");

        txtCodigoPack.setEditable(false);
        txtCodigoPack.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtCodigoPack.setForeground(new java.awt.Color(0, 0, 204));
        txtCodigoPack.setEnabled(false);
        txtCodigoPack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoPackActionPerformed(evt);
            }
        });

        jLabel84.setText("Cantidad");

        txtCantidadPack.setEditable(false);
        txtCantidadPack.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCantidadPack.setForeground(new java.awt.Color(51, 153, 0));
        txtCantidadPack.setEnabled(false);
        txtCantidadPack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadPackActionPerformed(evt);
            }
        });

        jLabel153.setText("buscar");

        txtlimpiarArtlista.setText("limpiar Caja Art");
        txtlimpiarArtlista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtlimpiarArtlistaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator11)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(317, 317, 317)
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel153)
                        .addGap(18, 18, 18)
                        .addComponent(txtBuscarPack, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(181, 181, 181))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 823, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(Titulo_Prov3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel13Layout.createSequentialGroup()
                                                .addGap(49, 49, 49)
                                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, Short.MAX_VALUE)
                                                .addComponent(txtUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(44, 44, 44))
                                            .addGroup(jPanel13Layout.createSequentialGroup()
                                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                                        .addGap(83, 83, 83)
                                                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                                        .addGap(81, 81, 81)
                                                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel83))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtNombrePack, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtCodigoPack, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel84, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtPrecioPack, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                            .addComponent(txtCantidadPack))
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel82)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addComponent(jButton24)
                                        .addGap(26, 26, 26)
                                        .addComponent(jButton27))
                                    .addComponent(cbEstadoPack, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(375, 375, 375)
                .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtlimpiarArtlista)
                .addGap(327, 327, 327))
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                    .addContainerGap(89, Short.MAX_VALUE)
                    .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 785, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(Titulo_Prov3)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(146, 146, 146)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton24)
                            .addComponent(jButton27))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel83)
                            .addComponent(txtCodigoPack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel84)
                            .addComponent(txtCantidadPack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(txtNombrePack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35)
                            .addComponent(txtPrecioPack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel82)
                            .addComponent(cbEstadoPack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton10)))
                .addGap(14, 14, 14)
                .addComponent(txtlimpiarArtlista)
                .addGap(18, 18, 18)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarPack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38)
                    .addComponent(jLabel153))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(jButton29)
                .addContainerGap())
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(508, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(216, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        maestros.addTab("Packs", jPanel5);

        Titulo_Prov4.setText("Redes Sociales");

        jLabel18.setText("Nombre RRSS");

        txtNombreRRSS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreRRSSActionPerformed(evt);
            }
        });

        jLabel36.setText("Codigo RRSS ");

        jButton31.setText("Cancelar");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        jButton32.setText("Guardar");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        jLabel39.setText("REDES  SOCIALES");

        Tabla_RRSS.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tabla_RRSS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cdigo", "Nombre", "Estado"
            }
        ));
        Tabla_RRSS.setColumnSelectionAllowed(true);
        Tabla_RRSS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabla_RRSSMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(Tabla_RRSS);
        Tabla_RRSS.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        txtBuscarRRSS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarRRSSActionPerformed(evt);
            }
        });
        txtBuscarRRSS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarRRSSKeyReleased(evt);
            }
        });

        jButton33.setText("Buscar");

        jButton34.setText("Editar");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        jButton35.setText("Eliminar");
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        txtCodigoRRSS.setEditable(false);
        txtCodigoRRSS.setEnabled(false);
        txtCodigoRRSS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoRRSSActionPerformed(evt);
            }
        });

        cbEstadoRRSS.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Desactivado" }));
        cbEstadoRRSS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEstadoRRSSActionPerformed(evt);
            }
        });

        jLabel31.setText("Estado");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator13)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Titulo_Prov4)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel31))
                                .addGap(35, 35, 35)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbEstadoRRSS, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel14Layout.createSequentialGroup()
                                        .addComponent(txtNombreRRSS, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(108, 108, 108)
                                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel14Layout.createSequentialGroup()
                                                .addComponent(jLabel36)
                                                .addGap(31, 31, 31)
                                                .addComponent(txtCodigoRRSS, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel14Layout.createSequentialGroup()
                                                .addGap(88, 88, 88)
                                                .addComponent(jButton31)
                                                .addGap(8, 8, 8)
                                                .addComponent(jButton32)))))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane6))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(356, 356, 356)
                        .addComponent(jLabel39)
                        .addGap(129, 129, 129)
                        .addComponent(txtBuscarRRSS, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton33)
                        .addGap(0, 16, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton34)
                .addGap(18, 18, 18)
                .addComponent(jButton35)
                .addGap(54, 54, 54))
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                    .addContainerGap(95, Short.MAX_VALUE)
                    .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 719, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Titulo_Prov4)
                .addGap(26, 26, 26)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtNombreRRSS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36)
                    .addComponent(txtCodigoRRSS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbEstadoRRSS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addGap(36, 36, 36)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton31)
                    .addComponent(jButton32))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarRRSS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton33)
                    .addComponent(jLabel39))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton34)
                    .addComponent(jButton35))
                .addContainerGap())
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(435, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(274, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        maestros.addTab("RRSS", jPanel6);

        Titulo_Prov5.setText("Categoria Articulos");

        jLabel19.setText("Categoria Artículo");

        txtNombreCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreCategoriaActionPerformed(evt);
            }
        });

        jLabel37.setText("Codigo Categoria ");

        jButton36.setText("Cancelar");
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        jButton37.setText("Guardar");
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });

        jLabel40.setText("Lista Categoria");

        Tabla_CategoriaArt.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tabla_CategoriaArt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre", "Estado"
            }
        ));
        Tabla_CategoriaArt.setColumnSelectionAllowed(true);
        Tabla_CategoriaArt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabla_CategoriaArtMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(Tabla_CategoriaArt);
        Tabla_CategoriaArt.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        txtBuscarCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarCatActionPerformed(evt);
            }
        });
        txtBuscarCat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarCatKeyReleased(evt);
            }
        });

        jButton38.setText("Buscar");

        jButton39.setText("Editar");
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });

        jButton40.setText("Eliminar");
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });

        txtCodigoCategoria.setEditable(false);
        txtCodigoCategoria.setEnabled(false);
        txtCodigoCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoCategoriaActionPerformed(evt);
            }
        });

        cbEstadoCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Desactivado" }));
        cbEstadoCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEstadoCategoriaActionPerformed(evt);
            }
        });

        jLabel79.setText("Estado");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator15)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Titulo_Prov5)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel79))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbEstadoCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel15Layout.createSequentialGroup()
                                        .addComponent(txtNombreCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(108, 108, 108)
                                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel15Layout.createSequentialGroup()
                                                .addComponent(jButton36)
                                                .addGap(8, 8, 8)
                                                .addComponent(jButton37))
                                            .addGroup(jPanel15Layout.createSequentialGroup()
                                                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtCodigoCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane9))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(356, 356, 356)
                        .addComponent(jLabel40)
                        .addGap(129, 129, 129)
                        .addComponent(txtBuscarCat, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton38)
                        .addGap(0, 31, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton39)
                .addGap(18, 18, 18)
                .addComponent(jButton40)
                .addGap(54, 54, 54))
            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                    .addContainerGap(115, Short.MAX_VALUE)
                    .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 701, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Titulo_Prov5)
                .addGap(26, 26, 26)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtNombreCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37)
                    .addComponent(txtCodigoCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbEstadoCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel79))
                .addGap(50, 50, 50)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton36)
                    .addComponent(jButton37))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton38)
                    .addComponent(jLabel40))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton39)
                    .addComponent(jButton40))
                .addContainerGap())
            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel15Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(435, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(274, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        maestros.addTab("Categorías Artículos", jPanel7);

        Titulo_Prov6.setText("Comunas");

        jLabel20.setText("Nombre Comuna");

        txtNombreComuna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreComunaActionPerformed(evt);
            }
        });

        jLabel41.setText("Código Comuna ");

        jButton41.setText("Cancelar");
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41ActionPerformed(evt);
            }
        });

        jButton42.setText("Guardar");
        jButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton42ActionPerformed(evt);
            }
        });

        jLabel42.setText("COMUNAS");

        Tabla_Comuna.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tabla_Comuna.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código Comuna", "Nombre Comuna"
            }
        ));
        Tabla_Comuna.setColumnSelectionAllowed(true);
        Tabla_Comuna.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabla_ComunaMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(Tabla_Comuna);
        Tabla_Comuna.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        txtBuscarComuna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarComunaActionPerformed(evt);
            }
        });
        txtBuscarComuna.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarComunaKeyReleased(evt);
            }
        });

        jButton43.setText("Buscar");

        jButton44.setText("Actualizar");
        jButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44ActionPerformed(evt);
            }
        });

        jButton45.setText("Eliminar");
        jButton45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton45ActionPerformed(evt);
            }
        });

        txtCodigoComuna.setEditable(false);
        txtCodigoComuna.setEnabled(false);
        txtCodigoComuna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoComunaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator17)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtNombreComuna, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(108, 108, 108)
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel16Layout.createSequentialGroup()
                                        .addComponent(jButton41)
                                        .addGap(8, 8, 8)
                                        .addComponent(jButton42))
                                    .addGroup(jPanel16Layout.createSequentialGroup()
                                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtCodigoComuna, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(Titulo_Prov6))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane10))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(356, 356, 356)
                        .addComponent(jLabel42)
                        .addGap(129, 129, 129)
                        .addComponent(txtBuscarComuna, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton43)
                        .addGap(0, 53, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton44)
                .addGap(18, 18, 18)
                .addComponent(jButton45)
                .addGap(54, 54, 54))
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                    .addContainerGap(115, Short.MAX_VALUE)
                    .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, 701, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Titulo_Prov6)
                .addGap(26, 26, 26)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtNombreComuna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41)
                    .addComponent(txtCodigoComuna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(88, 88, 88)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton41)
                    .addComponent(jButton42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator17, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarComuna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton43)
                    .addComponent(jLabel42))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton44)
                    .addComponent(jButton45))
                .addContainerGap())
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel16Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(435, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(274, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        maestros.addTab("Comunas", jPanel8);

        Titulo_Prov7.setText("Bancos");

        jLabel33.setText("Nombre Banco");

        txtNombreBanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreBancoActionPerformed(evt);
            }
        });

        jLabel43.setText("Código Banco");

        jButton46.setText("Cancelar");
        jButton46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton46ActionPerformed(evt);
            }
        });

        jButton47.setText("Guardar");
        jButton47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton47ActionPerformed(evt);
            }
        });

        jLabel44.setText("BANCOS");

        Tabla_Banco.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tabla_Banco.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código Banco", "Nombre Banco", "Estado"
            }
        ));
        Tabla_Banco.setColumnSelectionAllowed(true);
        Tabla_Banco.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabla_BancoMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(Tabla_Banco);
        Tabla_Banco.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        txtBuscarBanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarBancoActionPerformed(evt);
            }
        });
        txtBuscarBanco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarBancoKeyReleased(evt);
            }
        });

        jButton48.setText("Buscar");

        jButton49.setText("Editar");
        jButton49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton49ActionPerformed(evt);
            }
        });

        jButton50.setText("Eliminar");
        jButton50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton50ActionPerformed(evt);
            }
        });

        txtCodigoBanco.setEditable(false);
        txtCodigoBanco.setEnabled(false);
        txtCodigoBanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoBancoActionPerformed(evt);
            }
        });

        cbEstadoBanco.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Desactivado" }));
        cbEstadoBanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEstadoBancoActionPerformed(evt);
            }
        });

        jLabel78.setText("Estado");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator19)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Titulo_Prov7)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel78))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbEstadoBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addComponent(txtNombreBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(108, 108, 108)
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel17Layout.createSequentialGroup()
                                                .addComponent(jButton46)
                                                .addGap(8, 8, 8)
                                                .addComponent(jButton47))
                                            .addGroup(jPanel17Layout.createSequentialGroup()
                                                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtCodigoBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane11))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(356, 356, 356)
                        .addComponent(jLabel44)
                        .addGap(129, 129, 129)
                        .addComponent(txtBuscarBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton48)
                        .addGap(0, 62, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton49)
                .addGap(18, 18, 18)
                .addComponent(jButton50)
                .addGap(54, 54, 54))
            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                    .addContainerGap(115, Short.MAX_VALUE)
                    .addComponent(jSeparator18, javax.swing.GroupLayout.PREFERRED_SIZE, 701, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Titulo_Prov7)
                .addGap(26, 26, 26)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(txtNombreBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43)
                    .addComponent(txtCodigoBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbEstadoBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel78))
                .addGap(57, 57, 57)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton46)
                    .addComponent(jButton47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator19, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton48)
                    .addComponent(jLabel44))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton49)
                    .addComponent(jButton50))
                .addContainerGap())
            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel17Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jSeparator18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(435, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(274, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        maestros.addTab("Bancos", jPanel9);

        Titulo_Prov8.setText("Categoria Ventas");

        jLabel45.setText("Descripcion Categoria Venta");

        txtCatVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCatVentaActionPerformed(evt);
            }
        });

        jLabel46.setText("Código Categoria Ventas");

        jButton51.setText("Cancelar");
        jButton51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton51ActionPerformed(evt);
            }
        });

        jButton52.setText("Guardar");
        jButton52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton52ActionPerformed(evt);
            }
        });

        jLabel47.setText("CATEGORIA VENTAS REGISTRADAS");

        Tabla_CatVenta.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tabla_CatVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código ", "Descripcion Categoria Ventas", "Estado"
            }
        ));
        Tabla_CatVenta.setColumnSelectionAllowed(true);
        Tabla_CatVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabla_CatVentaMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(Tabla_CatVenta);
        Tabla_CatVenta.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        txtBuscarCatVent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarCatVentActionPerformed(evt);
            }
        });
        txtBuscarCatVent.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarCatVentKeyReleased(evt);
            }
        });

        jButton53.setText("Buscar");

        jButton54.setText("Editar");
        jButton54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton54ActionPerformed(evt);
            }
        });

        jButton55.setText("Eliminar");
        jButton55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton55ActionPerformed(evt);
            }
        });

        txtCodigoCatVenta.setEditable(false);
        txtCodigoCatVenta.setEnabled(false);
        txtCodigoCatVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoCatVentaActionPerformed(evt);
            }
        });

        cbEstadoCatVenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Desactivado" }));
        cbEstadoCatVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEstadoCatVentaActionPerformed(evt);
            }
        });

        jLabel80.setText("Estado");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator21)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Titulo_Prov8)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel18Layout.createSequentialGroup()
                                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                                        .addComponent(jLabel80)
                                        .addGap(28, 28, 28)))
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel18Layout.createSequentialGroup()
                                        .addComponent(txtCatVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(56, 56, 56)
                                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel18Layout.createSequentialGroup()
                                                .addComponent(jButton51)
                                                .addGap(8, 8, 8)
                                                .addComponent(jButton52))
                                            .addGroup(jPanel18Layout.createSequentialGroup()
                                                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtCodigoCatVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addComponent(cbEstadoCatVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 56, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane12))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(224, 224, 224)
                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(129, 129, 129)
                        .addComponent(txtBuscarCatVent, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton53)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton54)
                .addGap(18, 18, 18)
                .addComponent(jButton55)
                .addGap(54, 54, 54))
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                    .addContainerGap(115, Short.MAX_VALUE)
                    .addComponent(jSeparator20, javax.swing.GroupLayout.PREFERRED_SIZE, 701, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Titulo_Prov8)
                .addGap(26, 26, 26)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(txtCatVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46)
                    .addComponent(txtCodigoCatVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbEstadoCatVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel80))
                .addGap(57, 57, 57)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton51)
                    .addComponent(jButton52))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator21, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarCatVent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton53)
                    .addComponent(jLabel47))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton54)
                    .addComponent(jButton55))
                .addContainerGap())
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel18Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jSeparator20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(435, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(274, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        maestros.addTab("Categoría Ventas", jPanel10);

        Titulo_Prov9.setText("Usuario");

        jLabel12.setText("Nombre Usuario");

        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });

        jLabel48.setText("Ingrese Clave");

        txtClave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClaveActionPerformed(evt);
            }
        });

        txtDepartamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDepartamentoActionPerformed(evt);
            }
        });

        btnCancelarUsuario.setText("Cancelar");
        btnCancelarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarUsuarioActionPerformed(evt);
            }
        });

        btnGuardarUsuario.setText("Guardar");
        btnGuardarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarUsuarioActionPerformed(evt);
            }
        });

        jLabel53.setText("USUARIOS REGISTRADOS");

        tablaUsuario.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tablaUsuario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nombre Usuario", "Clave", "Departamento", "Estado"
            }
        ));
        tablaUsuario.setColumnSelectionAllowed(true);
        tablaUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaUsuarioMouseClicked(evt);
            }
        });
        jScrollPane13.setViewportView(tablaUsuario);
        tablaUsuario.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        txtbuscarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtbuscarUsuarioActionPerformed(evt);
            }
        });
        txtbuscarUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtbuscarUsuarioKeyReleased(evt);
            }
        });

        btnbuscarUsuario.setText("Buscar");
        btnbuscarUsuario.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                btnbuscarUsuarioStateChanged(evt);
            }
        });
        btnbuscarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarUsuarioActionPerformed(evt);
            }
        });

        btnEditarUsuario.setText("Editar");
        btnEditarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarUsuarioActionPerformed(evt);
            }
        });

        btnDesactivarUsuario.setText("Eliminar");
        btnDesactivarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesactivarUsuarioActionPerformed(evt);
            }
        });

        jLabel74.setText("Departamento");

        txtIdUsuario.setEditable(false);
        txtIdUsuario.setEnabled(false);

        jLabel49.setText("ID");

        cbEstadoUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Desactivado" }));
        cbEstadoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEstadoUsuarioActionPerformed(evt);
            }
        });

        jLabel85.setText("Estado");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator23)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel19Layout.createSequentialGroup()
                                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel19Layout.createSequentialGroup()
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(35, 35, 35)
                                        .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel19Layout.createSequentialGroup()
                                        .addComponent(jLabel74)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtDepartamento, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel19Layout.createSequentialGroup()
                                        .addGap(287, 287, 287)
                                        .addComponent(btnCancelarUsuario)
                                        .addGap(26, 26, 26)
                                        .addComponent(btnGuardarUsuario))
                                    .addGroup(jPanel19Layout.createSequentialGroup()
                                        .addGap(90, 90, 90)
                                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel49)
                                            .addComponent(jLabel85))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtIdUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cbEstadoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(Titulo_Prov9))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane13))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGap(258, 258, 258)
                        .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(129, 129, 129)
                        .addComponent(txtbuscarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnbuscarUsuario)
                        .addGap(0, 30, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnEditarUsuario)
                .addGap(18, 18, 18)
                .addComponent(btnDesactivarUsuario)
                .addGap(54, 54, 54))
            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                    .addGap(76, 76, 76)
                    .addComponent(jSeparator22, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Titulo_Prov9)
                .addGap(20, 20, 20)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49))
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbEstadoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel85))
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDepartamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel74))
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelarUsuario)
                    .addComponent(btnGuardarUsuario))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator23, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtbuscarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbuscarUsuario)
                    .addComponent(jLabel53))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditarUsuario)
                    .addComponent(btnDesactivarUsuario))
                .addContainerGap())
            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel19Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jSeparator22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(435, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(274, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        maestros.addTab("Usuarios", jPanel11);

        jTabbedPane4.addTab("Maestros", maestros);

        getContentPane().add(jTabbedPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 1120, 610));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField11ActionPerformed

    private void txtNombreClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreClienteActionPerformed

    private void txtTelefonoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoClienteActionPerformed

    private void txtEmailClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailClienteActionPerformed

    private void txtRutClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRutClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutClienteActionPerformed

    private void txtCelularClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCelularClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCelularClienteActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void txtDvClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDvClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDvClienteActionPerformed

    private void cbRedSocialClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRedSocialClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbRedSocialClienteActionPerformed

    private void txtNombreArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreArticuloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreArticuloActionPerformed

    private void txtBuscarArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarArticuloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarArticuloActionPerformed

    private void txtCodArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodArticuloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodArticuloActionPerformed

    private void txtNombrePackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombrePackActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombrePackActionPerformed

    private void txtBuscarPackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarPackActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarPackActionPerformed

    private void txtPrecioPackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioPackActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioPackActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        agregarArtPack();
        listar2Pack();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        quitarArtPack();
        listar2Pack();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void txtNombreRRSSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreRRSSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreRRSSActionPerformed

    private void txtBuscarRRSSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarRRSSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarRRSSActionPerformed

    private void txtCodigoRRSSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoRRSSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoRRSSActionPerformed

    private void txtNombreCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreCategoriaActionPerformed

    private void txtBuscarCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarCatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarCatActionPerformed

    private void txtCodigoCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoCategoriaActionPerformed

    private void txtNombreComunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreComunaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreComunaActionPerformed

    private void txtBuscarComunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarComunaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarComunaActionPerformed

    private void txtCodigoComunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoComunaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoComunaActionPerformed

    private void txtNombreBancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreBancoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreBancoActionPerformed

    private void txtBuscarBancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarBancoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarBancoActionPerformed

    private void txtCodigoBancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoBancoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoBancoActionPerformed

    private void txtCatVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCatVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCatVentaActionPerformed

    private void txtBuscarCatVentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarCatVentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarCatVentActionPerformed

    private void txtCodigoCatVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoCatVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoCatVentaActionPerformed

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void txtClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClaveActionPerformed

    private void txtDepartamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDepartamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDepartamentoActionPerformed

    private void txtbuscarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtbuscarUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtbuscarUsuarioActionPerformed

    private void txtNumeroPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroPedidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroPedidoActionPerformed

    private void txtNombreClienteVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreClienteVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreClienteVentaActionPerformed

    private void txtEmailClienteVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailClienteVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailClienteVentaActionPerformed

    private void txtRutVentaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRutVentaClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutVentaClienteActionPerformed

    private void txttelefonClienteVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttelefonClienteVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttelefonClienteVentaActionPerformed

    private void txtNombreDetinatarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDetinatarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDetinatarioActionPerformed

    private void jTextField55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField55ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField55ActionPerformed

    private void jTextField58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField58ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField58ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void txtRutProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRutProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutProveedorActionPerformed

    private void txtNombreProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreProveedorActionPerformed

    private void txtDireccionProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionProveedorActionPerformed

    private void txtRazonSocialProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRazonSocialProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRazonSocialProveedorActionPerformed

    private void txtTelefonoProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoProveedorActionPerformed

    private void txtEmailProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailProveedorActionPerformed

    private void txtBuscarProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarProvActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarProvActionPerformed

    private void txtNumDireProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumDireProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumDireProveedorActionPerformed

    private void txtComplementoProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtComplementoProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtComplementoProveedorActionPerformed

    private void cbxComunaProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxComunaProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxComunaProveedorActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        GuardarCliente();
        listarCliente();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        limpiarCliente();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void btnGuardarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarUsuarioActionPerformed
        // TODO add your handling code here:
        guardarUsuario();
        listarUsuario();
    }//GEN-LAST:event_btnGuardarUsuarioActionPerformed

    private void btnCancelarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarUsuarioActionPerformed
        limpiarUsuario();
    }//GEN-LAST:event_btnCancelarUsuarioActionPerformed

    private void tablaUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaUsuarioMouseClicked

        int fila = tablaUsuario.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Usuario no Seleccionado");
        } else {
            id = Integer.parseInt((String) tablaUsuario.getValueAt(fila, 0).toString());
            String usu = (String) tablaUsuario.getValueAt(fila, 1);
            String cla = (String) tablaUsuario.getValueAt(fila, 2);
            String cat = (String) tablaUsuario.getValueAt(fila, 3);
            txtIdUsuario.setText("" + id);
            txtUsuario.setText(usu);
            txtClave.setText(cla);
            txtDepartamento.setText(cat);

        }
    }//GEN-LAST:event_tablaUsuarioMouseClicked

    private void btnEditarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarUsuarioActionPerformed
        // TODO add your handling code here:
        modificarUsuario();

        listarUsuario();
    }//GEN-LAST:event_btnEditarUsuarioActionPerformed

    private void btnDesactivarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesactivarUsuarioActionPerformed
        elimianrUsuario();

        listarUsuario();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDesactivarUsuarioActionPerformed

    private void btnbuscarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarUsuarioActionPerformed
        String valor = (txtbuscarUsuario.getText().trim()).toString();
        if (valor.isEmpty()) {
            tablaUsuario.clearSelection();
        } else {
            for (int i = 0; i < tablaUsuario.getRowCount(); i++) {
                if (tablaUsuario.getValueAt(i, 0).equals(Integer.parseInt(valor)) || tablaUsuario.getValueAt(i, 1).equals(valor)) {
                    tablaUsuario.changeSelection(i, 0, false, false);
                }
            }
        }
    }//GEN-LAST:event_btnbuscarUsuarioActionPerformed

    private void btnbuscarUsuarioStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_btnbuscarUsuarioStateChanged
        /*String valor = txtbuscarUsuario.getText().trim();
        if(valor.isEmpty()){
            tablaUsuario.clearSelection();
        }else{
        for (int i = 0; i < tablaUsuario.getRowCount(); i++) {
            if (tablaUsuario.getValueAt(i, 0).equals(valor) || tablaUsuario.getValueAt(i, 1).equals(valor)) {
                 tablaUsuario.changeSelection(i, 0, false, false);
            }
        }
        }*/
    }//GEN-LAST:event_btnbuscarUsuarioStateChanged

    private void TablaClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaClientesMouseClicked
        // TODO add your handling code here:
        //Selecciona la fila
        int fila = TablaClientes.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Cliente no Seleccionado");
        } else {
            String rut = ((String) TablaClientes.getValueAt(fila, 0).toString());
            String dv = (String) TablaClientes.getValueAt(fila, 1);
            String Nombre = (String) TablaClientes.getValueAt(fila, 2);
            String Direccion = (String) TablaClientes.getValueAt(fila, 3);
            String telefono = ((String) TablaClientes.getValueAt(fila, 4).toString());
            String celular = ((String) TablaClientes.getValueAt(fila, 5).toString());
            String Fecha_Nacimiento = (String) TablaClientes.getValueAt(fila, 6);
            String Email = (String) TablaClientes.getValueAt(fila, 7);
            String redSocial = (String) TablaClientes.getValueAt(fila, 8);
            String EstadoCli = (String) TablaClientes.getValueAt(fila, 9);

            txtRutCliente.setText(rut);
            txtDvCliente.setText(dv);
            txtNombreCliente.setText(Nombre);
            txtDireccionCliente.setText(Direccion);
            txtTelefonoCliente.setText(telefono);
            txtCelularCliente.setText(celular);
            ((JTextField) FechaNacimientoCliente.getDateEditor().getUiComponent()).setText(Fecha_Nacimiento);
            txtEmailCliente.setText(Email);
            cbRedSocialCliente.setSelectedItem(redSocial);
            cbEstadoCliente.setSelectedItem(EstadoCli);
        }
    }//GEN-LAST:event_TablaClientesMouseClicked

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        modificarCliente();
        listarCliente();
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton62ActionPerformed

        limpiarProvedores();
    }//GEN-LAST:event_jButton62ActionPerformed

    private void Tabla_Provedores1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla_Provedores1MouseClicked

        int fila = Tabla_Provedores1.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Proveedor no Seleccionado");
        } else {
            String rutP = (String) Tabla_Provedores1.getValueAt(fila, 0);
            String rsP = (String) Tabla_Provedores1.getValueAt(fila, 1);
            String nomP = (String) Tabla_Provedores1.getValueAt(fila, 2);
            String telP = (String) Tabla_Provedores1.getValueAt(fila, 3).toString();
            String emaP = (String) Tabla_Provedores1.getValueAt(fila, 4);
            String dirP = (String) Tabla_Provedores1.getValueAt(fila, 5);
            String numP = (String) Tabla_Provedores1.getValueAt(fila, 6).toString();
            String comP = (String) Tabla_Provedores1.getValueAt(fila, 7);
            String compP = (String) Tabla_Provedores1.getValueAt(fila, 8);
            String estadoP = (String) Tabla_Provedores1.getValueAt(fila, 9);

            txtRutProveedor.setText(rutP);
            txtRazonSocialProveedor.setText(rsP);
            txtNombreProveedor.setText(nomP);
            txtTelefonoProveedor.setText(telP);
            txtEmailProveedor.setText(emaP);
            txtDireccionProveedor.setText(dirP);
            txtNumDireProveedor.setText(numP);
            cbxComunaProveedor.setSelectedItem(comP);
            txtComplementoProveedor.setText(compP);
            cbEstadoProveedor.setSelectedItem(estadoP);

        }
    }//GEN-LAST:event_Tabla_Provedores1MouseClicked

    private void jButton63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton63ActionPerformed

        GuardarProveedor();
        listarProveedores();
    }//GEN-LAST:event_jButton63ActionPerformed

    private void jButton68ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton68ActionPerformed
        editarPorveedor();
        listarProveedores();
    }//GEN-LAST:event_jButton68ActionPerformed

    private void Tabla_CategoriaArtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla_CategoriaArtMouseClicked
        int fila = Tabla_CategoriaArt.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Categoria no Seleccionado");
        } else {
            id = Integer.parseInt((String) Tabla_CategoriaArt.getValueAt(fila, 0).toString());
            String nom = (String) Tabla_CategoriaArt.getValueAt(fila, 1);
            String esta = (String) Tabla_CategoriaArt.getValueAt(fila, 2);

            txtCodigoCategoria.setText("" + id);
            txtNombreCategoria.setText(nom);
            cbEstadoCategoria.setSelectedItem(esta);

        }
    }//GEN-LAST:event_Tabla_CategoriaArtMouseClicked

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        guardarCategoria();
        listarCat();
    }//GEN-LAST:event_jButton37ActionPerformed

    private void cbEstadoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEstadoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbEstadoClienteActionPerformed

    private void cbEstadoRRSSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEstadoRRSSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbEstadoRRSSActionPerformed

    private void txtDireccionClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionClienteActionPerformed

    private void txtBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarKeyTyped

    private void Tabla_ComunaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla_ComunaMouseClicked
        int fila = Tabla_Comuna.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Comuna no Seleccionado");
        } else {
            id = Integer.parseInt((String) Tabla_Comuna.getValueAt(fila, 0).toString());
            String usu = (String) Tabla_Comuna.getValueAt(fila, 1);

            txtCodigoComuna.setText("" + id);
            txtNombreComuna.setText(usu);

        }
    }//GEN-LAST:event_Tabla_ComunaMouseClicked

    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed
        guardarComuna();
        listarComuna();
    }//GEN-LAST:event_jButton42ActionPerformed

    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
        modificarComuna();
        listarComuna();
    }//GEN-LAST:event_jButton44ActionPerformed

    private void jButton45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton45ActionPerformed
        elimianrComuna();
        listarComuna();
    }//GEN-LAST:event_jButton45ActionPerformed

    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
        limpiarComuna();
    }//GEN-LAST:event_jButton41ActionPerformed

    private void cbEstadoProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEstadoProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbEstadoProveedorActionPerformed

    private void jButton50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton50ActionPerformed
        elimianrBanco();
        listarBanco();
    }//GEN-LAST:event_jButton50ActionPerformed

    private void cbEstadoBancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEstadoBancoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbEstadoBancoActionPerformed

    private void Tabla_BancoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla_BancoMouseClicked

        int fila = Tabla_Banco.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "No Seleccionado");
        } else {
            id = Integer.parseInt((String) Tabla_Banco.getValueAt(fila, 0).toString());
            String usu = (String) Tabla_Banco.getValueAt(fila, 1);
            String esta = (String) Tabla_Banco.getValueAt(fila, 2);

            txtCodigoBanco.setText("" + id);
            txtNombreBanco.setText(usu);
            cbEstadoBanco.setSelectedItem(esta);

        }
    }//GEN-LAST:event_Tabla_BancoMouseClicked

    private void jButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton46ActionPerformed
        limpiarBanco();
    }//GEN-LAST:event_jButton46ActionPerformed

    private void jButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton47ActionPerformed
        guardarBanco();
        listarBanco();
    }//GEN-LAST:event_jButton47ActionPerformed

    private void jButton49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton49ActionPerformed
        modificarBanco();
        listarBanco();
    }//GEN-LAST:event_jButton49ActionPerformed

    private void cbEstadoCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEstadoCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbEstadoCategoriaActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        elimianrCategoria();
        listarCat();
    }//GEN-LAST:event_jButton40ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        limpiarCategoria();
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
        editarCategoria();
        listarCat();
    }//GEN-LAST:event_jButton39ActionPerformed

    private void cbEstadoCatVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEstadoCatVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbEstadoCatVentaActionPerformed

    private void Tabla_CatVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla_CatVentaMouseClicked
        int fila = Tabla_CatVenta.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "No Seleccionado");
        } else {
            id = Integer.parseInt((String) Tabla_CatVenta.getValueAt(fila, 0).toString());
            String descrip = (String) Tabla_CatVenta.getValueAt(fila, 1);
            String esta = (String) Tabla_CatVenta.getValueAt(fila, 2);

            txtCodigoCatVenta.setText("" + id);
            txtCatVenta.setText(descrip);
            cbEstadoCatVenta.setSelectedItem(esta);

        }
    }//GEN-LAST:event_Tabla_CatVentaMouseClicked

    private void jButton51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton51ActionPerformed
        limpiarCatVenta();
    }//GEN-LAST:event_jButton51ActionPerformed

    private void jButton52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton52ActionPerformed
        guardarCatVenta();
        listarCatVenta();
    }//GEN-LAST:event_jButton52ActionPerformed

    private void jButton54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton54ActionPerformed
        ModificarCatVenta();
        listarCatVenta();
    }//GEN-LAST:event_jButton54ActionPerformed

    private void jButton55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton55ActionPerformed
        elimianrCatVenta();
        listarCatVenta();
    }//GEN-LAST:event_jButton55ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        limpiarRRSS();
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        guardarRRSS();
        listaRRSS();
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        modificarRRSS();
        listaRRSS();
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        elimianrRRSS();
        listaRRSS();
    }//GEN-LAST:event_jButton35ActionPerformed

    private void Tabla_RRSSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla_RRSSMouseClicked
        int fila = Tabla_RRSS.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "No Seleccionado");
        } else {
            id = Integer.parseInt((String) Tabla_RRSS.getValueAt(fila, 0).toString());
            String usu = (String) Tabla_RRSS.getValueAt(fila, 1);
            String esta = (String) Tabla_RRSS.getValueAt(fila, 2);

            txtCodigoRRSS.setText("" + id);
            txtNombreRRSS.setText(usu);
            cbEstadoRRSS.setSelectedItem(esta);

        }
    }//GEN-LAST:event_Tabla_RRSSMouseClicked

    private void cbEstadoArtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEstadoArtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbEstadoArtActionPerformed

    private void Tabla_ArticulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla_ArticulosMouseClicked
        int fila = Tabla_Articulos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "No Seleccionado");
        } else {
            String cod_Art = (String) Tabla_Articulos.getValueAt(fila, 0).toString();
            String nombre = (String) Tabla_Articulos.getValueAt(fila, 1);
            String categoria = (String) Tabla_Articulos.getValueAt(fila, 2);
            String stock = (String) Tabla_Articulos.getValueAt(fila, 3).toString();
            String fecha = (String) Tabla_Articulos.getValueAt(fila, 4);
            String estado = (String) Tabla_Articulos.getValueAt(fila, 5);

            txtCodArticulo.setText(cod_Art);
            txtNombreArticulo.setText(nombre);
            cbxArticulo.setSelectedItem(categoria);
            txtArtStock.setText(stock);
            ((JTextField) fechaVencimientoArt.getDateEditor().getUiComponent()).setText(fecha);
            cbEstadoArt.setSelectedItem(estado);

        }
    }//GEN-LAST:event_Tabla_ArticulosMouseClicked

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        limpiarArticulos();
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        guardarArticulos();
        listaArticulos();
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        modificarArticulos();
        listaArticulos();
    }//GEN-LAST:event_jButton25ActionPerformed

    private void btnEliminarArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarArticuloActionPerformed
        elimianrArticulos();
        listaArticulos();
    }//GEN-LAST:event_btnEliminarArticuloActionPerformed

    private void cbEstadoPackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEstadoPackActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbEstadoPackActionPerformed

    private void txtCodigoPackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoPackActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoPackActionPerformed

    private void txtCantidadPackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadPackActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadPackActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        limpiarPack();
    }//GEN-LAST:event_jButton24ActionPerformed

    private void cbEstadoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEstadoUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbEstadoUsuarioActionPerformed

    private void jTextField15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField21ActionPerformed

    private void jTextField23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField23ActionPerformed

    private void jTextField24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField24ActionPerformed

    private void jTextField25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField25ActionPerformed

    private void jButton71ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton71ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton71ActionPerformed

    private void jButton74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton74ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton74ActionPerformed

    private void jButton78ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton78ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton78ActionPerformed

    private void jButton80ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton80ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton80ActionPerformed

    private void jButton82ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton82ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton82ActionPerformed

    private void jButton84ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton84ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton84ActionPerformed

    private void jButton86ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton86ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton86ActionPerformed

    private void maestrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maestrosMouseClicked
        limpiarTablaPack2();
        listar2Pack();

        limpiarTablaArticulos();
        listaArticulos();
    }//GEN-LAST:event_maestrosMouseClicked

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked

    }//GEN-LAST:event_jPanel4MouseClicked

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        guardarPack();
        listarPack();
    }//GEN-LAST:event_jButton27ActionPerformed

    private void Tabla_PackListarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla_PackListarMouseClicked
        int fila = Tabla_PackListar.getSelectedRow();
        int codigArt = 0;

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "No Seleccionado");
        } else {
            String cod_Pack = (String) Tabla_PackListar.getValueAt(fila, 0).toString();
            String nombrePack = (String) Tabla_PackListar.getValueAt(fila, 1);
            String precio = (String) Tabla_PackListar.getValueAt(fila, 2).toString();
            String stock = (String) Tabla_PackListar.getValueAt(fila, 3).toString();
            String estado = (String) Tabla_PackListar.getValueAt(fila, 4);

            txtCodigoPack.setText(cod_Pack);
            txtNombrePack.setText(nombrePack);
            txtPrecioPack.setText(precio);
            txtCantidadPack.setText(stock);
            cbEstadoPack.setSelectedItem(estado);
            
            limpiarTablaPack3();

            try {
                int codigoPack = Integer.parseInt(cod_Pack);
               
                PreparedStatement pst1 = cn.prepareStatement("select pa.ARTICULO_cod_articulo from pack as p, pack_has_articulo as pa  where PACK_cod_pack ='" + codigoPack + "'  ");
                ResultSet rs1 = pst1.executeQuery();
                while (rs1.next()) {
                    codigArt = rs1.getInt("pa.ARTICULO_cod_articulo");
                }

                PreparedStatement pst = cn.prepareStatement("select a.nombre,pa.cantidad from pack_has_articulo as pa, articulo as a, pack as p where pa.PACK_cod_pack = '"+codigoPack+"' and pa.ARTICULO_cod_articulo = a.cod_articulo GROUP by a.nombre" );
                ResultSet rs = pst.executeQuery();

                Object[] categ = new Object[2];

                modeloPack = (DefaultTableModel) Tabla_Pack2.getModel();

                while (rs.next()) {
                    categ[0] = rs.getString("a.nombre");
                    categ[1] = rs.getInt("pa.cantidad");

                    modeloPack.addRow(categ);
                }
                Tabla_Pack2.setModel(modeloPack);
               

            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, "Error al Conectar  " + e);
                limpiarTablaPack3();
            }

        }
    }//GEN-LAST:event_Tabla_PackListarMouseClicked

    private void Tabla_PackListarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla_PackListarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_Tabla_PackListarMouseEntered

    private void txtlimpiarArtlistaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtlimpiarArtlistaActionPerformed
        limpiarTablaPack3();
    }//GEN-LAST:event_txtlimpiarArtlistaActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        modificarPack();
        limpiarPack();
        limpiarTablaPack();
        listarPack();
    }//GEN-LAST:event_jButton29ActionPerformed

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        // TODO add your handling code here:
        filtro(txtBuscar.getText().toUpperCase(), TablaClientes);
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void txtBuscarProvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProvKeyReleased
        // TODO add your handling code here:
        filtro(txtBuscarProv.getText().toUpperCase(), Tabla_Provedores1);
    }//GEN-LAST:event_txtBuscarProvKeyReleased

    private void txtBuscarPackKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarPackKeyReleased
        // TODO add your handling code here:
        filtro(txtBuscarPack.getText().toUpperCase(), Tabla_PackListar);
    }//GEN-LAST:event_txtBuscarPackKeyReleased

    private void txtBuscarRRSSKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarRRSSKeyReleased
        // TODO add your handling code here:
        filtro(txtBuscarRRSS.getText().toUpperCase(), Tabla_RRSS);
    }//GEN-LAST:event_txtBuscarRRSSKeyReleased

    private void txtBuscarCatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarCatKeyReleased
        // TODO add your handling code here:
        filtro(txtBuscarCat.getText().toUpperCase(), Tabla_CategoriaArt);
    }//GEN-LAST:event_txtBuscarCatKeyReleased

    private void txtBuscarComunaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarComunaKeyReleased
        // TODO add your handling code here:
        filtro(txtBuscarComuna.getText().toUpperCase(), Tabla_Comuna);
        
    }//GEN-LAST:event_txtBuscarComunaKeyReleased

    private void txtBuscarBancoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarBancoKeyReleased
        // TODO add your handling code here:
        filtro(txtBuscarBanco.getText().toUpperCase(), Tabla_Banco);
        
    }//GEN-LAST:event_txtBuscarBancoKeyReleased

    private void txtBuscarCatVentKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarCatVentKeyReleased
        // TODO add your handling code here:
        filtro(txtBuscarCatVent.getText().toUpperCase(), Tabla_CatVenta);
        
    }//GEN-LAST:event_txtBuscarCatVentKeyReleased

    private void txtbuscarUsuarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtbuscarUsuarioKeyReleased
        // TODO add your handling code here:
        filtro(txtbuscarUsuario.getText().toUpperCase(), tablaUsuario);
        
    }//GEN-LAST:event_txtbuscarUsuarioKeyReleased

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Vista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Vista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Vista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Vista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Vista().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public com.toedter.calendar.JDateChooser FechaNacimientoCliente;
    public com.toedter.calendar.JDateChooser FechaNacimientoCliente1;
    public com.toedter.calendar.JDateChooser FechaNacimientoCliente10;
    public com.toedter.calendar.JDateChooser FechaNacimientoCliente11;
    public com.toedter.calendar.JDateChooser FechaNacimientoCliente12;
    public com.toedter.calendar.JDateChooser FechaNacimientoCliente13;
    public com.toedter.calendar.JDateChooser FechaNacimientoCliente14;
    public com.toedter.calendar.JDateChooser FechaNacimientoCliente2;
    public com.toedter.calendar.JDateChooser FechaNacimientoCliente3;
    public com.toedter.calendar.JDateChooser FechaNacimientoCliente4;
    public com.toedter.calendar.JDateChooser FechaNacimientoCliente5;
    public com.toedter.calendar.JDateChooser FechaNacimientoCliente6;
    public com.toedter.calendar.JDateChooser FechaNacimientoCliente7;
    public com.toedter.calendar.JDateChooser FechaNacimientoCliente8;
    public com.toedter.calendar.JDateChooser FechaNacimientoCliente9;
    public javax.swing.JTable TablaClientes;
    private javax.swing.JTable Tabla_Articulos;
    private javax.swing.JTable Tabla_Banco;
    private javax.swing.JTable Tabla_CatVenta;
    private javax.swing.JTable Tabla_CategoriaArt;
    private javax.swing.JTable Tabla_Comuna;
    private javax.swing.JTable Tabla_Pack2;
    private javax.swing.JTable Tabla_PackListar;
    private javax.swing.JTable Tabla_Provedores;
    private javax.swing.JTable Tabla_Provedores1;
    private javax.swing.JTable Tabla_RRSS;
    private javax.swing.JLabel Titulo_Prov;
    private javax.swing.JLabel Titulo_Prov1;
    private javax.swing.JLabel Titulo_Prov10;
    private javax.swing.JLabel Titulo_Prov11;
    private javax.swing.JLabel Titulo_Prov12;
    private javax.swing.JLabel Titulo_Prov2;
    private javax.swing.JLabel Titulo_Prov3;
    private javax.swing.JLabel Titulo_Prov4;
    private javax.swing.JLabel Titulo_Prov5;
    private javax.swing.JLabel Titulo_Prov6;
    private javax.swing.JLabel Titulo_Prov7;
    private javax.swing.JLabel Titulo_Prov8;
    private javax.swing.JLabel Titulo_Prov9;
    private javax.swing.JButton btnCancelarUsuario;
    private javax.swing.JButton btnDesactivarUsuario;
    private javax.swing.JButton btnEditarUsuario;
    private javax.swing.JButton btnEliminarArticulo;
    private javax.swing.JButton btnGuardarUsuario;
    private javax.swing.JButton btnbuscarUsuario;
    public javax.swing.JComboBox<String> cbEstadoArt;
    public javax.swing.JComboBox<String> cbEstadoBanco;
    public javax.swing.JComboBox<String> cbEstadoCatVenta;
    public javax.swing.JComboBox<String> cbEstadoCategoria;
    public javax.swing.JComboBox<String> cbEstadoCliente;
    public javax.swing.JComboBox<String> cbEstadoPack;
    public javax.swing.JComboBox<String> cbEstadoProveedor;
    public javax.swing.JComboBox<String> cbEstadoRRSS;
    public javax.swing.JComboBox<String> cbEstadoUsuario;
    public com.toedter.calendar.JDateChooser cbFechaEntregaDestinatario;
    private javax.swing.JComboBox<String> cbListaPack;
    public javax.swing.JComboBox<String> cbRedSocialCliente;
    private javax.swing.JComboBox<String> cbxArticulo;
    public static javax.swing.JComboBox<String> cbxComunaProveedor;
    private javax.swing.JPanel clientes;
    private javax.swing.JTabbedPane compras;
    private javax.swing.JLabel dreamGifts;
    public com.toedter.calendar.JDateChooser fechaVencimientoArt;
    private javax.swing.JTabbedPane informes;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    public javax.swing.JButton jButton15;
    public javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    public javax.swing.JButton jButton18;
    public javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton48;
    private javax.swing.JButton jButton49;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton50;
    private javax.swing.JButton jButton51;
    private javax.swing.JButton jButton52;
    private javax.swing.JButton jButton53;
    private javax.swing.JButton jButton54;
    private javax.swing.JButton jButton55;
    private javax.swing.JButton jButton56;
    private javax.swing.JButton jButton57;
    private javax.swing.JButton jButton58;
    private javax.swing.JButton jButton59;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton60;
    private javax.swing.JButton jButton61;
    public static javax.swing.JButton jButton62;
    public static javax.swing.JButton jButton63;
    public static javax.swing.JButton jButton64;
    private javax.swing.JButton jButton65;
    private javax.swing.JButton jButton66;
    private javax.swing.JButton jButton67;
    private javax.swing.JButton jButton68;
    private javax.swing.JButton jButton69;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton70;
    private javax.swing.JButton jButton71;
    private javax.swing.JButton jButton72;
    private javax.swing.JButton jButton73;
    private javax.swing.JButton jButton74;
    private javax.swing.JButton jButton75;
    private javax.swing.JButton jButton76;
    private javax.swing.JButton jButton77;
    private javax.swing.JButton jButton78;
    private javax.swing.JButton jButton79;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton80;
    private javax.swing.JButton jButton81;
    private javax.swing.JButton jButton82;
    private javax.swing.JButton jButton83;
    private javax.swing.JButton jButton84;
    private javax.swing.JButton jButton85;
    private javax.swing.JButton jButton86;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox10;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JComboBox<String> jComboBox9;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JFrame jFrame2;
    private javax.swing.JFrame jFrame3;
    private javax.swing.JFrame jFrame4;
    private javax.swing.JFrame jFrame5;
    private javax.swing.JFrame jFrame6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    public javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel152;
    private javax.swing.JLabel jLabel153;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu16;
    private javax.swing.JMenu jMenu17;
    private javax.swing.JMenu jMenu18;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuBar jMenuBar3;
    private javax.swing.JMenuBar jMenuBar4;
    private javax.swing.JMenuBar jMenuBar5;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    public javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu3;
    private javax.swing.JPopupMenu jPopupMenu4;
    private javax.swing.JPopupMenu jPopupMenu5;
    private javax.swing.JPopupMenu jPopupMenu6;
    private javax.swing.JPopupMenu jPopupMenu7;
    private javax.swing.JPopupMenu jPopupMenu8;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator25;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JSeparator jSeparator28;
    private javax.swing.JSeparator jSeparator29;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator30;
    private javax.swing.JSeparator jSeparator31;
    private javax.swing.JSeparator jSeparator32;
    private javax.swing.JSeparator jSeparator33;
    private javax.swing.JSeparator jSeparator34;
    private javax.swing.JSeparator jSeparator35;
    private javax.swing.JSeparator jSeparator36;
    private javax.swing.JSeparator jSeparator37;
    private javax.swing.JSeparator jSeparator38;
    private javax.swing.JSeparator jSeparator39;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator40;
    private javax.swing.JSeparator jSeparator41;
    private javax.swing.JSeparator jSeparator42;
    private javax.swing.JSeparator jSeparator43;
    private javax.swing.JSeparator jSeparator44;
    private javax.swing.JSeparator jSeparator45;
    private javax.swing.JSeparator jSeparator46;
    private javax.swing.JSeparator jSeparator47;
    private javax.swing.JSeparator jSeparator48;
    private javax.swing.JSeparator jSeparator49;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator50;
    private javax.swing.JSeparator jSeparator51;
    private javax.swing.JSeparator jSeparator52;
    private javax.swing.JSeparator jSeparator53;
    private javax.swing.JSeparator jSeparator54;
    private javax.swing.JSeparator jSeparator55;
    private javax.swing.JSeparator jSeparator56;
    private javax.swing.JSeparator jSeparator57;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable10;
    private javax.swing.JTable jTable11;
    private javax.swing.JTable jTable12;
    private javax.swing.JTable jTable13;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTable8;
    private javax.swing.JTable jTable9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField55;
    private javax.swing.JTextField jTextField58;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTabbedPane maestros;
    private java.awt.Menu menu1;
    private java.awt.Menu menu2;
    private java.awt.MenuBar menuBar1;
    private javax.swing.JTable tablaUsuario;
    private javax.swing.JTable tabla_Pack1;
    private javax.swing.JTextField txtArtStock;
    public javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtBuscarArticulo;
    private javax.swing.JTextField txtBuscarBanco;
    private javax.swing.JTextField txtBuscarCat;
    private javax.swing.JTextField txtBuscarCatVent;
    private javax.swing.JTextField txtBuscarComuna;
    private javax.swing.JTextField txtBuscarPack;
    public static javax.swing.JTextField txtBuscarProv;
    private javax.swing.JTextField txtBuscarRRSS;
    private javax.swing.JTextField txtCantidadPack;
    private javax.swing.JTextField txtCatVenta;
    public javax.swing.JTextField txtCelularCliente;
    private javax.swing.JTextField txtClave;
    private javax.swing.JTextField txtCodArticulo;
    private javax.swing.JTextField txtCodigoBanco;
    private javax.swing.JTextField txtCodigoCatVenta;
    private javax.swing.JTextField txtCodigoCategoria;
    private javax.swing.JTextField txtCodigoComuna;
    private javax.swing.JTextField txtCodigoPack;
    private javax.swing.JTextField txtCodigoRRSS;
    public static javax.swing.JTextField txtComplementoProveedor;
    private javax.swing.JTextField txtDepartamento;
    public javax.swing.JTextField txtDireccionCliente;
    public static javax.swing.JTextField txtDireccionProveedor;
    public javax.swing.JTextField txtDvCliente;
    public javax.swing.JTextField txtEmailCliente;
    private javax.swing.JTextField txtEmailClienteVenta;
    public static javax.swing.JTextField txtEmailProveedor;
    private javax.swing.JTextField txtIdUsuario;
    private javax.swing.JTextField txtNombreArticulo;
    private javax.swing.JTextField txtNombreBanco;
    private javax.swing.JTextField txtNombreCategoria;
    public javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNombreClienteVenta;
    private javax.swing.JTextField txtNombreComuna;
    private javax.swing.JTextField txtNombreDetinatario;
    private javax.swing.JTextField txtNombrePack;
    public static javax.swing.JTextField txtNombreProveedor;
    private javax.swing.JTextField txtNombreRRSS;
    public static javax.swing.JTextField txtNumDireProveedor;
    private javax.swing.JTextField txtNumeroPedido;
    private javax.swing.JTextField txtPrecioPack;
    public static javax.swing.JTextField txtRazonSocialProveedor;
    public javax.swing.JTextField txtRutCliente;
    public static javax.swing.JTextField txtRutProveedor;
    private javax.swing.JTextField txtRutVentaCliente;
    public javax.swing.JTextField txtTelefonoCliente;
    public static javax.swing.JTextField txtTelefonoProveedor;
    private javax.swing.JTextField txtUnidades;
    private javax.swing.JTextField txtUsuario;
    private javax.swing.JTextField txtbuscarUsuario;
    private javax.swing.JButton txtlimpiarArtlista;
    private javax.swing.JTextField txttelefonClienteVenta;
    private javax.swing.JTabbedPane ventas;
    // End of variables declaration//GEN-END:variables
}
