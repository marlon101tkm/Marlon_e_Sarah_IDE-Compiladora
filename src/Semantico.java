
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import jdk.nashorn.internal.ir.BreakNode;

public class Semantico implements Constants {

    Stack<Integer> pilhaEscopo = new Stack(), pilhaEscAux = new Stack();
    Stack<Integer> pilhaExp = new Stack();
    Stack<String> pilhaRotulos = new Stack();
    Stack<String> pilhaFimPara = new Stack();
    List<TabelaDeSimbolos> tabSimb = new ArrayList<>();
    String nome = "", nomeAtrib = "", nomeVet = "", tipo = "", warning = "", nome_func = "", src = ".text\n                JMP\t _PRINCIPAL \n", operador = "", opAnt = "", codTempLopPara = "",
            rotSe = "", rotFim = "", rotIni = "", nomeLopPara = "", nome_call = "";
    boolean inicializada = false, parametros = false, vet = false, ref = false, isRetornavel = false,
            func = false, seFunc = false, flagOP = false, isNot = false, expIndexVet = false, isParam = false, seRetornou = false;
    SemanticTable sTb = new SemanticTable();
    int escopo = 0, tipo_id = -1, tipo_exp = -1, tipo1 = -1, tipo2 = -1, operacao = -1, resultAtrib = -1, ordemParam = -1, posicao = 1,
            indexExp = 0, indexVetIndex = 0, indexExpVet = 0, escopo_temp = -1, qtd_param = 0, qtd_chama_param = 0, tipo_param = -1,
            escopo_inter_func = 0, contRot = 0, indeExpParam = 0;

    public List<TabelaDeSimbolos> getTabSimb() {
        return tabSimb;
    }

    public void gera_cod(String le, String ld) {
        src += "                " + le + "\t" + ld + "\n";
    }

    public void gera_cod_rot(String le, String ld) {
        src += le + " " + ld + "\n";
    }

    public void gera_cod_nome_func(String nome) {
        src += nome + " : \n";
    }

    public void gera_codVar(Token token) {

        if (!flagOP) {
            gera_cod("LD", token.getLexeme());
        } else {
            if (operador.equals("+")) {
                gera_cod("ADD", token.getLexeme());
            }

            if (operador.equals("-")) {
                gera_cod("SUB", token.getLexeme());
            }

            if (operador.equals("&")) {
                gera_cod("AND", token.getLexeme());
            }
            if (operador.equals("|")) {
                gera_cod("OR", token.getLexeme());
            }
            if (operador.equals("~")) {
                if (buscaSeVet(nome, BuscaEscopoVar(nome))) {

                    // gera_cod("NOT", token.getLexeme() + ":");
                    if (indexExpVet > 0) {
                        //  gera_cod("STO", "1003");

                        // gera_cod("LD", "1002");
                        if (opAnt.equals("+")) {
                            gera_cod("ADD", "1003");
                        } else if (opAnt.equals("-")) {
                            gera_cod("SUB", "1003");

                        }
                    }
                } else {
                    // if (!buscaSeVet(nome, BuscaEscopoVar(nome))) {
                    gera_cod("LD", token.getLexeme());
                    gera_cod("NOT", token.getLexeme() + ":");
                    if (indexExp > 0) {
                        //gera_cod("NOT", token.getLexeme() + ":");
                        gera_cod("STO", "1003");

                        gera_cod("LD", "1002");

                        if (opAnt.equals("+")) {
                            gera_cod("ADD", "1003");
                        } else if (opAnt.equals("-")) {
                            gera_cod("SUB", "1003");

                        }
                    }

                }

                if (operador.equals("^")) {
                    gera_cod("XOR", token.getLexeme());
                }
                flagOP = false;

            }
        }
    }

    public void gera_codInteiro(Token token) {

        if (!vet) {

            if (!flagOP) {
                gera_cod("LDI", token.getLexeme());
            } else {
                if (operador.equals("+")) {
                    gera_cod("ADDI", token.getLexeme());
                }

                if (operador.equals("-")) {
                    gera_cod("SUBI", token.getLexeme());
                }
                if (operador.equals("&")) {
                    gera_cod("ANDI", token.getLexeme());
                }
                if (operador.equals("|")) {
                    gera_cod("ORI", token.getLexeme());
                }
                if (operador.equals("^")) {
                    gera_cod("XORI", token.getLexeme());
                }

                if (operador.equals(">>")) {
                    gera_cod("SRL", token.getLexeme());
                }
                if (operador.equals("<<")) {
                    gera_cod("SLL", token.getLexeme());
                }

                if (operador.equals("~")) {
                    if (opAnt.equals("+")) {
                        gera_cod("ADDI", token.getLexeme());
                    } else if (opAnt.equals("-")) {
                        gera_cod("SUBI", token.getLexeme());
                    } else {
                        gera_cod("LDI", token.getLexeme());
                    }

                }
                flagOP = false;

            }
        }
    }

    public void gera_desvio(String rotulo) {
        if (operador.equals(">")) {
            gera_cod("BLE", rotulo);
        }
        if (operador.equals("<")) {
            gera_cod("BGE", rotulo);
        }
        if (operador.equals(">=")) {
            gera_cod("BLT", rotulo);
        }
        if (operador.equals("<=")) {
            gera_cod("BGT", rotulo);
        }
        if (operador.equals("==")) {
            gera_cod("BNE", rotulo);
        }
        if (operador.equals("!=")) {
            gera_cod("BEQ", rotulo);
        }

    }

    public void gera_desvioFazEnq(String rotulo) {
        if (operador.equals(">")) {
            gera_cod("BGT", rotulo);
        }
        if (operador.equals("<")) {
            gera_cod("BLT", rotulo);
        }
        if (operador.equals(">=")) {
            gera_cod("BGE", rotulo);
        }
        if (operador.equals("<=")) {
            gera_cod("BLE", rotulo);
        }
        if (operador.equals("==")) {
            gera_cod("BEQ", rotulo);
        }
        if (operador.equals("!=")) {
            gera_cod("BNE", rotulo);
        }

    }

    public String getSrc() {
        return src;
    }

    public String carregaVariavel() {
        String pontoData = ".data\n";
        for (TabelaDeSimbolos it : tabSimb) {

            if (!it.isFunc() && !it.isVet() && it.getTipo().equals("inteiro")) {
                pontoData += "" + it.getNome() + " : " + "0\n";

            }

            if (!it.isFunc() && it.isVet() && it.getTipo().equals("inteiro") && !it.isParametros()) {
                pontoData += "" + it.getNome() + " : ";
                for (int i = 0; i < it.getQtdEspacoVet(); i++) {
                    if (i == it.getQtdEspacoVet() - 1) {
                        pontoData += "0\n";
                    } else {
                        pontoData += "0,";
                    }

                }
            }
        }

        return pontoData;

    }

    public void insereQtdVet(String nome, int qtdvet) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (!it.isFunc() && it.isVet() && it.getNome().equals(nome) && !it.isParametros()) {
                it.setQtdEspacoVet(qtdvet);

            }
        }

    }

    public boolean buscaNomeTabela(String id, int escop) {
//        System.err.println(tabSimb.isEmpty());
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.getNome().equals(id) && it.getEscopo() == escop) {

                return true;
            }
        }

        return false;
    }

    public int BuscaEscopoVar(String id) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (!it.isFunc() && it.getNome().equals(id)) {

                return it.getEscopo();
            }
        }
        return -1;
    }

    public boolean buscaNomeTabelaVar(String id, int escop) {
//        System.err.println(tabSimb.isEmpty());
        for (TabelaDeSimbolos it : tabSimb) {

            if (!it.isFunc() && it.getNome().equals(id) && it.getEscopo() == escop) {

                return true;
            }
        }

        return false;
    }

    public boolean buscaNomeEscoposMaiores(String id) {

        while (!pilhaEscopo.isEmpty()) {
            pilhaEscAux.push(pilhaEscopo.pop());
            if (buscaNomeTabelaVar(id, pilhaEscAux.peek())) {
                while (!pilhaEscAux.isEmpty()) {
                    pilhaEscopo.push(pilhaEscAux.pop());
                }
                return true;
            }
        }

        while (!pilhaEscAux.isEmpty()) {
            pilhaEscopo.push(pilhaEscAux.pop());
        }

        return false;
    }

    public String buscaNomePeloEscopoInterno(int escopo) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.isFunc() && it.getEscopoInternoFunc() == escopo) {
                return it.getNome();
            }
        }

        return "";
    }

    public boolean buscaSeFuncTabela(String id, int escop) {
//        System.err.println(tabSimb.isEmpty());
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.getNome().equals(id) && it.getEscopo() == escop && it.isFunc()) {
                //System.out.println("achou");
                //System.out.println(it.getNome());
                return true;
            }
        }
        // System.out.println("n achou");
        return false;
    }

    public void insereEscopoInternoFuncParam(String id, int escopo, int escopoInter) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.getNome().equals(id) && it.getEscopo() == escopo) {
                it.setEscopoInternoFunc(escopoInter);
            }
        }
    }

    public int buscaEscopoInternoFunc(String id, int escopo) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.getNome().equals(id) && it.getEscopo() == escopo && it.isFunc()) {
                return it.getEscopoInternoFunc();
            }
        }

        return -1;
    }

    public int buscaEscopoFunc(String id) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.getNome().equals(id) && it.isFunc()) {
                return it.getEscopo();
            }
        }
        return -1;
    }

    public boolean buscaSeInicializada(String id, int escopo) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (!it.isFunc() && it.getNome().equals(id) && it.getEscopo() == escopo && !it.isInicializada()) {
                return true;
            }
        }

        return false;
    }

    public boolean buscaSeVet(String id, int escopo) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.isVet() && !it.isFunc() && it.getNome().equals(id) && it.getEscopo() == escopo && !it.isInicializada()) {
                return true;
            }
        }

        return false;
    }

    public String buscaNomeParam(String id, int contPar) {
        int escop = buscaEscopoInternoFunc(id, buscaEscopoFunc(id));
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.isParametros() && !it.isFunc() && it.getPos() == contPar
                    && it.getEscopo() == escop) {
                return it.getNome();
            }
        }

        return "";
    }

    public boolean buscaSeParam(String id, int escopo) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.isParametros() && !it.isFunc() && it.getNome().equals(id) && it.getEscopo() == escopo && !it.isInicializada()) {
                return true;
            }
        }

        return false;
    }

    public boolean buscaSeInicializadaEscoposMaiores(String id) {

        while (!pilhaEscopo.isEmpty()) {
            pilhaEscAux.push(pilhaEscopo.pop());
            if (buscaSeInicializada(id, pilhaEscAux.peek())) {
                while (!pilhaEscAux.isEmpty()) {
                    pilhaEscopo.push(pilhaEscAux.pop());
                }
                return true;
            }
        }

        while (!pilhaEscAux.isEmpty()) {
            pilhaEscopo.push(pilhaEscAux.pop());
        }

        return false;
    }

    public void insereQtdParam(String id, int escopo, int qtd_param) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.getNome().equals(id) && it.getEscopo() == escopo) {
                it.setQtd_param(qtd_param);
            }
        }
    }

    public String buscaTipoTabela(String id) {
        for (TabelaDeSimbolos it : tabSimb) {
            if (it.getNome().equals(id)) {

                return it.getTipo();
            }
        }

        return "";
    }

    public String buscaTipoParam(int posi, int escopo) {
        for (TabelaDeSimbolos it : tabSimb) {
            if (it.getPos() == posi && it.getEscopo() == escopo && it.isParametros()) {

                return it.getTipo();
            }
        }
        return "";
    }

    public boolean buscaIniTabela(String id, int escopo) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.getNome().equals(id) && it.getEscopo() == escopo) {
                System.out.println("Nome: " + it.getNome() + "  Escopo: " + it.getEscopo());
                return it.isInicializada();
            }
        }
        return false;
    }

    public void inserePosParam(String id, int escopo, int posi) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.getNome().equals(id) && it.getEscopo() == escopo) {
                it.setPos(posi);
            }
        }
    }

    public void insereParam(String id, int escopo) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.getNome().equals(id) && it.getEscopo() == escopo) {
                it.setParametros(true);
            }
        }
    }

    public void insereInicializar(String id, int escopo) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.getNome().equals(id) && it.getEscopo() == escopo) {

                it.setInicializada(true);
            }
        }
    }

    public void insereUsada(String id, int escopo) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.getNome().equals(id) && it.getEscopo() == escopo) {
                it.setUsada(true);
            }
        }
    }

    public boolean buscaNomeFun(String id) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.getNome().equals(id)) {
                return it.isFunc();
            }
        }
        return false;
    }

    public int buscaQtdFunc(String id) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.getNome().equals(id) && it.isFunc()) {
                return it.getQtd_param();
            }
        }
        return 0;
    }

    public void seVarNaoUsada(int escopo) {
        for (TabelaDeSimbolos it : tabSimb) {

            if (it.getEscopo() == escopo && !it.isFunc() && !it.isParametros() && !it.isUsada()) {

                warning += "WARNING: Varivel " + it.getNome() + " declarada e nao utilizada \n";
            }
        }
    }

    public String tipoNumToString(int num) {

        switch (num) {
            case 0:
                return "inteiro";
            case 1:
                return "decimal";
            case 2:
                return "letra";
            case 3:
                return "texto";
            case 4:
                return "logico";
            case 5:
                return "vazio";
        }
        return "";

    }

    public int tipoStringToNum(String tipo) {

        switch (tipo) {
            case "inteiro":
                return 0;
            case "decimal":
                return 1;
            case "letra":
                return 2;
            case "texto":
                return 3;
            case "logico":
                return 4;
            case "vazio":
                return 5;
        }
        return -1;

    }

    public void trataExp() throws SemanticError {

        tipo2 = pilhaExp.pop();
        operacao = pilhaExp.pop();
        tipo1 = pilhaExp.pop();
        tipo_exp = sTb.resultType(tipo1, tipo2, operacao);
        if (tipo_exp == sTb.ERR) {
            throw new SemanticError("ERRO: Tipos incompativeis");
        } else {

            pilhaExp.push(tipo_exp);
        }

    }

    public boolean testeAltFlagOpInteiro() {

        if (isParam) {

            return indeExpParam == 0;
        }

        if (expIndexVet) {

            return indexVetIndex == 0;
        }

        return indexExp == 0;

    }

    public boolean testeAltFlagOpVar() {

        if (isParam) {

            return indeExpParam == 0 && !isNot;
        }

        if (expIndexVet) {

            return indexVetIndex == 0 && !isNot;
        }

        return indexExp == 0 && !isNot;

    }

    public void executeAction(int action, Token token) throws SemanticError {

        TabelaDeSimbolos elemento = new TabelaDeSimbolos();

        switch (action) {
            case 1:
                tipo = token.getLexeme();
                break;
            case 2:

                nome = nome_func + "_" + token.getLexeme();
                if (tipo.equals("vazio")) {
                    throw new SemanticError("ERRO: variavel não pode ser declarada como vazia");
                }
                if (buscaNomeTabela(nome, escopo)) {
                    throw new SemanticError("ERRO: variavel já declarada");
                } else {
                    elemento.setNome(nome);
                    elemento.setTipo(tipo);
                    tipo_id = tipoStringToNum(tipo);
                    pilhaExp.push(tipo_id);

                    elemento.setEscopo(pilhaEscopo.peek());

                    //System.out.println(elemento.getTipo());
                    tabSimb.add(elemento);
                    nomeAtrib = nome;
                }
                break;
            case 3:
                nome = nome_func + "_" + token.getLexeme();
                if (!buscaNomeEscoposMaiores(nome)) {
                    throw new SemanticError("ERRO: Variavel: " + nome + " Não Declarada");
                } else {

                    tipo_id = tipoStringToNum(buscaTipoTabela(nome));
                    pilhaExp.push(tipo_id);
                    nomeAtrib = nome;

                }
                break;
            case 4:
                pilhaExp.push(sTb.INT);

//                if ((indexVetIndex == 0 && expIndexVet) | (indexExp == 0 && !expIndexVet)| (indeExpParam==0 && isParam) ) {
                if (testeAltFlagOpInteiro()) {
                    flagOP = false;
                }

                gera_codInteiro(token);

//                if ((indexVetIndex == 0 && expIndexVet) | (indexExp == 0 && !expIndexVet) | (indeExpParam==0 && isParam) ) {
                if (testeAltFlagOpInteiro()) {
                    flagOP = true;
                }

                vet = false;
                break;
            case 5:
                pilhaExp.push(sTb.FLO);
                break;
            case 6:
                pilhaExp.push(sTb.BOO);

                break;
            case 7:
                pilhaExp.push(sTb.CHA);
                break;
            case 8:
                pilhaExp.push(sTb.STR);
                break;
            case 9:
                tipo_exp = pilhaExp.pop();
                indexExp = 0;
                break;
            case 10:
                escopo_temp = pilhaEscopo.peek();
                escopo++;
                if (buscaSeFuncTabela(nome_func, escopo_temp)) {
                    insereEscopoInternoFuncParam(nome, escopo_temp, escopo);
                }
                pilhaEscopo.push(escopo);
                break;
            case 11:
                seVarNaoUsada(pilhaEscopo.peek());
                pilhaEscopo.pop();
                break;
            case 12:
                tipo_id = pilhaExp.pop();
                resultAtrib = sTb.atribType(tipo_id, tipo_exp);
                if (resultAtrib == sTb.ERR) {
                    throw new SemanticError("ERRO: Tipos incompativeis");
                }
                if (resultAtrib == sTb.WAR) {
                    warning += "WARNING: Posivel perda de precisao  na atribuição de tipo " + tipoNumToString(tipo_exp) + " para tipo " + tipoNumToString(tipo_id) + "\n";
                }
                if (buscaSeVet(nomeAtrib, BuscaEscopoVar(nomeAtrib))) {

                    gera_cod("STO", "1002");
                    gera_cod("LD", "1000");
                    gera_cod("STO", "$indr");
                    gera_cod("LD", "1002");
                    gera_cod("STOV", nomeAtrib);

                } else {

                    insereInicializar(nome, BuscaEscopoVar(nome));
                    gera_cod("STO", nomeAtrib);
                }
                break;
            case 13:
                trataExp();
                break;

            case 14:
                pilhaExp.push(sTb.LOG);
                flagOP = true;
                operador = token.getLexeme();

                break;
            case 15:
                pilhaExp.push(sTb.REL);
                flagOP = true;
                operador = token.getLexeme();
                gera_cod("STO", "1000");
                break;
            case 16:
                pilhaExp.push(sTb.SUM);

                flagOP = true;
                operador = token.getLexeme();

                if (expIndexVet) {
                    indexVetIndex++;
                } else if (isParam) {
                    indeExpParam++;
                } else {
                    opAnt = operador;
                    indexExp++;
                }
                break;
            case 17:
                pilhaExp.push(sTb.SUB);
                flagOP = true;
                operador = token.getLexeme();
                opAnt = operador;
                if (expIndexVet) {
                    indexVetIndex++;
                } else if (isParam) {

                    indeExpParam++;
                } else {
                    indexExp++;
                }
                break;
            case 18:
                pilhaExp.push(sTb.MUL);
                break;
            case 19:
                pilhaExp.push(sTb.DIV);
                break;
            case 20:
                nome = "_" + token.getLexeme();
                //System.out.println(nome);
                if (buscaNomeTabela(nome, pilhaEscopo.peek())) {
                    throw new SemanticError("ERRO: Função já declarada");
                } else {
                    nome_func = nome;
                    elemento.setNome(nome);
                    elemento.setTipo(tipo);
                    elemento.setFunc(true);

                    escopo_temp = pilhaEscopo.peek();
                    elemento.setEscopo(escopo_temp);

                    //System.out.println(elemento.getTipo());
                    tabSimb.add(elemento);
                    gera_cod_nome_func(nome_func);

                    if (tipo.equals("vazio") || nome_func.equals("_PRINCIPAL")) {
                        isRetornavel = false;
                    } else {
                        isRetornavel = true;
                    }
                }
                break;
            case 21:
                nome = token.getLexeme();
                //System.out.println(nome);
                if (buscaNomeTabela(nome, escopo)) {
                    throw new SemanticError("ERRO: variavel já declarada");
                } else {
                    elemento.setNome(nome);
                    elemento.setTipo(tipo);
                    elemento.setVet(true);
                    tipo_id = tipoStringToNum(tipo);

                    elemento.setEscopo(pilhaEscopo.peek());

                    //System.out.println(elemento.getTipo());
                    tabSimb.add(elemento);
                    nomeAtrib = nome;
                    vet = true;
                }
                break;
            case 22:
                trataExp();
                break;
            case 23:
                trataExp();
                break;
            case 24:
                trataExp();
                break;
            case 25:
                trataExp();
                break;
            case 26:
                trataExp();
                gera_cod("STO", "1001");
                gera_cod("LD", "1000");
                gera_cod("SUB", "1001");
                break;
            case 27:
                trataExp();
                break;
            case 28:
                trataExp();
                break;
            case 29:
                trataExp();
                break;
            case 30:
                nome = nome_func + "_" + token.getLexeme();
                if (!buscaNomeEscoposMaiores(nome)) {
                    throw new SemanticError("ERRO: Variavel: " + nome + " Não Declarada");
                } else {
                    if (buscaSeInicializadaEscoposMaiores(nome)) {
                        warning += "WARNING: Varivel " + nome + " não inicializada, pode conter lixo de memoria \n";
                    }

                    if (buscaSeVet(nome, BuscaEscopoVar(nome))) {
                        nomeVet = nome;

                        if (indexExp > 0 && !isNot) {
                            gera_cod("STO", "1002");

                        }
                        tipo_id = tipoStringToNum(buscaTipoTabela(nome));
                        pilhaExp.push(tipo_id);

                        insereUsada(nome, BuscaEscopoVar(nome));
                        indexExpVet = indexExp;

                    } else {

                        tipo_id = tipoStringToNum(buscaTipoTabela(nome));
                        pilhaExp.push(tipo_id);

                        insereUsada(nome, BuscaEscopoVar(nome));

//                        if (((expIndexVet && indexVetIndex == 0) | indexExp == 0 | indeExpParam==0) && !isNot) {
                    if(testeAltFlagOpVar()){
                        flagOP = false;
                    }
                    gera_codVar(new Token(0, nome, 0));
//                    if (((expIndexVet && indexVetIndex == 0) | indexExp == 0 | indeExpParam == 0) && !isNot) {
                    if(testeAltFlagOpVar()){
                        flagOP = true;
                    }

                    if (buscaSeParam(nome, BuscaEscopoVar(nome))) {

                        gera_cod(nome_call, buscaNomeParam(tipo, contRot));
                    }

                }

        }
        break;
    

case 31:
                if (!buscaNomeTabela(nome, escopo)) {
                    throw new SemanticError("ERRO: Variavel: " + nome + " Não Declarada");
                }
                insereParam(nome, escopo);
                inserePosParam(nome, escopo, posicao);
                break;
            case 32:
                posicao++;
              
                break;
            case 33:
                insereQtdParam(nome_func, escopo_temp, posicao);
                posicao = 1;
                break;
            case 34:
                pilhaExp.push(tipo_exp);
                break;
            case 35:
                qtd_chama_param++;
                tipo_exp = pilhaExp.pop();

                tipo_param = tipoStringToNum(buscaTipoParam(qtd_chama_param, buscaEscopoInternoFunc(nome_call, buscaEscopoFunc(nome_call))));

                resultAtrib = sTb.atribType(tipo_param, tipo_exp);
                if (resultAtrib == sTb.ERR) {
                    throw new SemanticError("ERRO: Tipo Incompativel com o Parametro da funcao");
                }

                gera_cod("STO", buscaNomeParam(nome_call, qtd_chama_param));
                indeExpParam= 0;
                break;
            case 36:
                nome = "_" + token.getLexeme();
                if (!buscaNomeFun(nome)) {
                    throw new SemanticError("ERRO: Função: " + nome + " Não existe");
                } else {
                    if (buscaTipoTabela(nome).equals("vazio")) {
                        throw new SemanticError("ERRO: Funcao: " + nome + " não pode estar uma expresão");
                    }
                    nome_call = nome;
//                    nome_func = nome;
                    escopo_temp = buscaEscopoFunc(nome);
                    escopo_inter_func = buscaEscopoInternoFunc(nome_func, escopo_temp);
                    tipo_id = tipoStringToNum(buscaTipoTabela(nome));
                    pilhaExp.push(tipo_id);
                    insereUsada(nome, escopo);
                    
                    if (indexExp > 0) {
                        gera_cod("STO", "1002");
                    }

                }
                break;
            case 37:
                qtd_param = buscaQtdFunc(nome_call);
                if (qtd_param != qtd_chama_param) {
                    throw new SemanticError("ERRO: Funcao: " + nome_call + " possui " + qtd_param + " parametros e foram informados " + qtd_chama_param + " parametro(s)");
                }
                break;
            case 38:
                if (qtd_chama_param != buscaQtdFunc(nome_call)) {
                    throw new SemanticError("ERRO: Funcao: " + nome_call + " possui " + buscaQtdFunc(nome_call) + " parametros e foram informados Parametros alem do necessario");
                }
                indeExpParam = 0;
                posicao = 1;
                qtd_chama_param = 0;
                isParam = false;
                gera_cod("CALL", nome_call);
                if (indexExp > 0) {
                    gera_cod("STO", "1003");
                    gera_cod("LD", "1002");
                    operador = opAnt;
                    gera_codVar(new Token(0, "1003", 0));

                }
                break;
            case 39:
                tipo_exp = pilhaExp.pop();
                resultAtrib = sTb.atribType(sTb.BOO, tipo_exp);
                if (resultAtrib == sTb.ERR) {
                    throw new SemanticError("ERRO: Resultado da Expressão não é do tipo LOGICO");
                }
                break;

            case 40:

                nome = token.getLexeme();

                if (tipo.equals("vazio")) {
                    throw new SemanticError("ERRO: variavel não pode ser declarada como vazia");
                }
                if (buscaNomeTabela(nome, escopo)) {
                    throw new SemanticError("ERRO: variavel já declarada");
                }

                elemento.setNome(nome);
                elemento.setTipo(tipo);
                tipo_id = tipoStringToNum(tipo);
                elemento.setParametros(true);
                elemento.setEscopo(pilhaEscopo.peek());
                elemento.setRef(true);

                tabSimb.add(elemento);

                break;
            case 41:
                nome = token.getLexeme();
                if (buscaSeFuncTabela(nome, pilhaEscopo.peek())) {
                    throw new SemanticError("ERRO: Entrada de dados somente aceita variaveis");
                }
                gera_cod("LD", "$in_port");
                if (!buscaSeVet(nome, BuscaEscopoVar(nome))) {
                    gera_cod("STO", nome);
                }

                break;
            case 42: 
                tipo_exp = pilhaExp.pop();
                resultAtrib = sTb.atribType(tipoStringToNum(buscaTipoTabela(buscaNomePeloEscopoInterno(pilhaEscopo.peek()))), tipo_exp);
                if (resultAtrib == sTb.ERR) {
                    throw new SemanticError("ERRO: Retorno é do tipo: " + tipoNumToString(tipo_exp) + " e funcao: "
                            + buscaNomePeloEscopoInterno(pilhaEscopo.peek()) + " possui tipo: " + buscaTipoTabela(buscaNomePeloEscopoInterno(pilhaEscopo.peek())));
                }
                seRetornou = true;
                break;
            case 43:
                if (token.getLexeme().equals("!")) {
                    pilhaExp.push(sTb.BOO);

                } else if (token.getLexeme().equals("~")) {
                    if (indexExp > 0) {

                        gera_cod("STO", "1002");

                        opAnt = operador;
                    }

                    pilhaExp.push(sTb.INT);
                    isNot = true;
                }
                pilhaExp.push(sTb.NEGACAO);

                flagOP = true;
                operador = token.getLexeme();

                break;
            case 44:
                pilhaExp.push(sTb.NEGATIVO);
                pilhaExp.push(sTb.INT);

                break;
            case 45:
                pilhaExp.push(sTb.SUF);
                pilhaExp.push(sTb.INT);

                break;

            case 46:
                trataExp();
                break;
            case 47:
                trataExp();
                break;

            case 48:
                gera_cod("STO", "$out_port");
                break;
            case 49:
                insereQtdVet(nome, Integer.parseInt(token.getLexeme()));
                break;
            case 50:
                if (indexExpVet == 0) {
                    gera_cod("STO", "$indr");
                    gera_cod("LDV", nomeVet);
                    if (isNot) {
                        gera_cod("NOT", nomeVet + ":");

                    }

                } else {
                    String aux = operador;
                    gera_cod("STO", "$indr");
                    gera_cod("LDV", nomeVet);
                    if (isNot) {
                        gera_cod("NOT", nomeVet + ":");
                        gera_cod("STO", "1003");
                        gera_cod("LD", "1002");
                    } else {
                        gera_cod("STO", "1003");
                        gera_cod("LD", "1002");
                    }
                    // gera_cod("STO", "1003");
                    //gera_cod("LD", "1002");

                    //if (!operador.equals("~")) {
                    flagOP = true;

                    // }
                    gera_codVar(new Token(0, "1003", 0));
                    //if (!operador.equals("~")) {
                    flagOP = false;
                    if (isNot) {
                        isNot = false;
                    }

                    // }
                }
                indexVetIndex = 0;
                break;
            case 51:
                gera_cod("STO", "1000");
                break;

            case 52:
                expIndexVet = true;
                break;
            case 53:
                expIndexVet = false;
                break;
            case 54:
                contRot++;
                rotSe = "SE" + contRot;
                pilhaRotulos.push(rotSe);
                gera_desvio(rotSe);
                break;
            case 55:
                rotFim = pilhaRotulos.pop();
                gera_cod_rot(rotFim, ":");
                break;
            case 56:
                rotSe = pilhaRotulos.pop();
                rotFim = "FIM_SE" + contRot;
                gera_cod("JMP", rotFim);
                pilhaRotulos.push(rotFim);
                gera_cod_rot(rotSe, ":");
                break;

            case 57:
                contRot++;
                rotIni = "Ini_" + token.getLexeme() + "_" + contRot;
                pilhaRotulos.push(rotIni);
                gera_cod_rot(rotIni, ":");
                break;
            case 58:
                if (rotIni.contains("Para")) {
                    rotFim = "FimPara" + contRot;
                } else {
                    rotFim = "FimEnq" + contRot;
                }
                pilhaRotulos.push(rotFim);
                gera_desvio(rotFim);
                break;
            case 59:
                rotFim = pilhaRotulos.pop();
                rotIni = pilhaRotulos.pop();
                gera_cod("JMP", rotIni);
                gera_cod_rot(rotFim, ":");
                break;
            case 60:
                rotIni = pilhaRotulos.pop();
                gera_desvioFazEnq(rotIni);

                break;
            case 61:
                nomeLopPara = nome;
                contRot++;
                rotIni = "Ini" + "Para" + contRot;
                pilhaRotulos.push(rotIni);
                gera_cod_rot(rotIni, ":");
                break;
            case 62:
                codTempLopPara = "                LD   " + nomeLopPara + "\n";
                if (token.getLexeme().equals("++")) {
                    codTempLopPara += "                ADDI   1\n";
                }
                if (token.getLexeme().equals("--")) {
                    codTempLopPara += "                SUBI   1\n";
                }
                codTempLopPara += "                STO   " + nomeLopPara + "\n";

                pilhaFimPara.push(codTempLopPara);
                break;
            case 63:
                src += pilhaFimPara.pop();
                rotFim = pilhaRotulos.pop();
                rotIni = pilhaRotulos.pop();
                gera_cod("JMP", rotIni);
                gera_cod_rot(rotFim, ":");
                break;
            case 64:
                nome = token.getLexeme();
                if (buscaSeFuncTabela(nome, pilhaEscopo.peek())) {
                    throw new SemanticError("ERRO: Entrada de dados somente aceita variaveis");
                }
                break;
            case 65:
                gera_cod("STO", "$indr");
                gera_cod("LD", "$in_port");
                gera_cod("STOV", nome);
                break;
            case 66:
                isParam = true;
                break;
            case 67:
                if (!nome_func.equals("_PRINCIPAL")) {
                    if (!buscaTipoTabela(nome_func).equals("vazio") && !seRetornou) {
                        throw new SemanticError("ERRO:  função " + nome_func + " precisa de Retorno ");
                    }

                    if (buscaTipoTabela(nome_func).equals("vazio") && seRetornou) {
                        throw new SemanticError("ERRO: função " + nome_func + " não possui retorno ");
                    }
                    seRetornou = false;
                    gera_cod("RETURN", "0");
                }

                break;
            case 68:
                 nome = "_" + token.getLexeme();
                if (!buscaNomeFun(nome)) {
                    throw new SemanticError("ERRO: Função: " + nome + " Não existe");
                } else {
                    if (!buscaTipoTabela(nome).equals("vazio")) {
                        throw new SemanticError("ERRO: Funcao: " + nome + " não pode estar fora de uma expresão");
                    }
                    nome_call = nome;
//                    nome_func = nome;
                    escopo_temp = buscaEscopoFunc(nome);
                    escopo_inter_func = buscaEscopoInternoFunc(nome_func, escopo_temp);
                    tipo_id = tipoStringToNum(buscaTipoTabela(nome));
                    pilhaExp.push(tipo_id);
                    insereUsada(nome, escopo);
                }
              break;
            
        }

    }
}
