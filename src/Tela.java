/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Win10
 */
public class Tela extends javax.swing.JFrame {

    /**
     * Creates new form Tela
     * 
     * 
     * 
     */
    
     CodigoGerado gerado  = new CodigoGerado();
    
    String codData="",codExe="";
    
    public void carregaTabSimb(Semantico sem){
        
        DefaultTableModel val = (DefaultTableModel) tabela.getModel();
        val.setRowCount(0);
        for (TabelaDeSimbolos it : sem.tabSimb) {
                val.addRow(new String[]{
                    it.getNome(),
                    it.getTipo(),
                    it.booleanToString(it.isInicializada()),
                    it.booleanToString(it.isUsada()),
                    Integer.toString(it.getEscopo()),
                    it.booleanToString(it.isParametros()),
                    Integer.toString(it.getPos()),
                    it.booleanToString(it.isVet()),
                    it.booleanToString(it.isRef()),
                    it.booleanToString(it.isFunc())
                });
            }
        
    }
    
    
    public Tela() {
        initComponents();
        painelConsole.setEditable(false);
        gerado.setVisible(false);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        entradaCompila = new javax.swing.JTextArea();
        btnCompilar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        painelConsole = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabela = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        btnMenuAbrir = new javax.swing.JMenu();
        btnMenuSalva = new javax.swing.JMenuItem();
        btnMenuAbre = new javax.swing.JMenuItem();
        codGerado = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        entradaCompila.setColumns(20);
        entradaCompila.setRows(5);
        jScrollPane1.setViewportView(entradaCompila);

        btnCompilar.setText("Compilar");
        btnCompilar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnCompilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompilarActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportView(painelConsole);

        tabela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "tipo", "ini", "usada", "escopo", "param", "Pos", "vet", "ref", "func"
            }
        ));
        jScrollPane2.setViewportView(tabela);

        btnMenuAbrir.setText("Arquivo");

        btnMenuSalva.setText("Salvar");
        btnMenuSalva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuSalvaActionPerformed(evt);
            }
        });
        btnMenuAbrir.add(btnMenuSalva);

        btnMenuAbre.setText("Abrir");
        btnMenuAbre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuAbreActionPerformed(evt);
            }
        });
        btnMenuAbrir.add(btnMenuAbre);

        codGerado.setText("Gerar Codigo");
        codGerado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codGeradoActionPerformed(evt);
            }
        });
        btnMenuAbrir.add(codGerado);

        jMenuBar1.add(btnMenuAbrir);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnCompilar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCompilar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCompilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompilarActionPerformed
        painelConsole.setText("");
        String codigo = entradaCompila.getText();
        Lexico lex = new Lexico();
        Sintatico sint = new Sintatico();
        Semantico sem = new Semantico();
        sem.pilhaEscopo.push(0);
        lex.setInput(codigo);
        try {
            sint.parse(lex, sem);
            carregaTabSimb(sem);
            painelConsole.setText(sem.warning+"Sucesso");
           codData = sem.carregaVariavel();
           codExe = sem.src;

        } catch (LexicalError ex) {
            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
            painelConsole.setText("Erro");

        } catch (SyntaticError ex) {
            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
            painelConsole.setText("Erro");

        } catch (SemanticError ex) {
            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
           //System.out.println(ex.getMessage());
            painelConsole.setText(ex.getMessage());

        }
    }//GEN-LAST:event_btnCompilarActionPerformed

    private void btnMenuSalvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuSalvaActionPerformed
        JFileChooser file = new JFileChooser();
        file.setFileSelectionMode(JFileChooser.FILES_ONLY);
        file.showSaveDialog(this);
        File f = file.getSelectedFile();
        try {
            FileWriter fw = new FileWriter(f);
            String codigo = entradaCompila.getText();
            fw.write(codigo);
            fw.close();

        } catch (IOException ex) {
            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnMenuSalvaActionPerformed

    private void btnMenuAbreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuAbreActionPerformed
        JFileChooser file = new JFileChooser();
        file.setFileSelectionMode(JFileChooser.FILES_ONLY);
        file.showSaveDialog(this);
        File f = file.getSelectedFile();
        Path caminho = Paths.get(f.getName());
        byte[] texto;
        try {
            texto = Files.readAllBytes(caminho);
            String leitura = new String(texto);
            entradaCompila.setText(leitura);
        } catch (IOException ex) {
            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnMenuAbreActionPerformed

    private void codGeradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codGeradoActionPerformed
        String codFinal;
        codFinal = codData+codExe+"                HLT \t 0"; 
        gerado.setCaixaTexto(codFinal);
        gerado.setVisible(true);
        
        
        
    }//GEN-LAST:event_codGeradoActionPerformed

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
            java.util.logging.Logger.getLogger(Tela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tela().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCompilar;
    private javax.swing.JMenuItem btnMenuAbre;
    private javax.swing.JMenu btnMenuAbrir;
    private javax.swing.JMenuItem btnMenuSalva;
    private javax.swing.JMenuItem codGerado;
    private javax.swing.JTextArea entradaCompila;
    private javax.swing.JButton jButton1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextPane painelConsole;
    private javax.swing.JTable tabela;
    // End of variables declaration//GEN-END:variables
}
