/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Win10
 */
public class TabelaDeSimbolos {
    private String nome = "",tipo = "";
    private boolean inicializada,parametros,vet,ref,func,usada;
    int escopo = 0 , pos=0, escopoInternoFunc=-1, qtd_param = 0,qtdEspacoVet=-1;
    public TabelaDeSimbolos() {
        
    }

    public int getQtdEspacoVet() {
        return qtdEspacoVet;
    }

    public void setQtdEspacoVet(int qtdEspacoVet) {
        this.qtdEspacoVet = qtdEspacoVet;
    }

   
  

    public String booleanToString(boolean val){
        if(val){
           return "T";
        }
        
        return "F";
    } 

    public int getQtd_param() {
        return qtd_param;
    }

    public void setQtd_param(int qtd_param) {
        this.qtd_param = qtd_param;
    }
    
    
    
    public int getEscopoInternoFunc() {
        return escopoInternoFunc;
    }

    public void setEscopoInternoFunc(int escopoInternoFunc) {
        this.escopoInternoFunc = escopoInternoFunc;
    }
    
    
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isInicializada() {
        return inicializada;
    }

    public void setInicializada(boolean inicializada) {
        this.inicializada = inicializada;
    }

    public boolean isParametros() {
        return parametros;
    }

    public void setParametros(boolean parametros) {
        this.parametros = parametros;
    }

    public boolean isVet() {
        return vet;
    }

    public void setVet(boolean vet) {
        this.vet = vet;
    }

    public boolean isRef() {
        return ref;
    }

    public void setRef(boolean ref) {
        this.ref = ref;
    }

    public boolean isFunc() {
        return func;
    }

    public void setFunc(boolean func) {
        this.func = func;
    }

    public boolean isUsada() {
        return usada;
    }

    public void setUsada(boolean usada) {
        this.usada = usada;
    }

    public int getEscopo() {
        return escopo;
    }

    public void setEscopo(int escopo) {
        this.escopo = escopo;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    

   
}
