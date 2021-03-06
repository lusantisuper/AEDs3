package menu.api;

import produtos.CelulaResposta;
import produtos.Pergunta;
import menu.backend.cruds.abstracts.PerguntaInterface;
import menu.backend.input.CustomInput;
import menu.backend.misc.CodigoDeProtocolo;
import menu.frontend.genericos.FrontEndPlus;

/**
 * Classe para gerenciar todas as funções de controle de perguntas
 * 
 * @author MysteRys337 ( Gustavo Lopes )
 */
public class PerguntasAPI {

    private final byte TAM_MIN_TITULO; // Tamanho minimo para as perguntas
    private final byte TAM_MAX_TITULO; // Tamanho maximo para as perguntas

    private final byte TAM_MIN_PERGUNTA; // Tamanho minimo para as perguntas
    private final byte TAM_MAX_PERGUNTA; // Tamanho maximo para as perguntas

    private FrontEndPlus frontEnd;

    private CustomInput customInput;

    public PerguntasAPI(byte TAM_MIN_TITULO, byte TAM_MAX_TITULO, byte TAM_MIN_PERGUNTA, byte TAM_MAX_PERGUNTA,
            FrontEndPlus frontEnd, CustomInput customInput) {

        this.TAM_MIN_TITULO = TAM_MIN_TITULO;
        this.TAM_MAX_TITULO = TAM_MAX_TITULO;

        this.TAM_MIN_PERGUNTA = TAM_MIN_PERGUNTA;
        this.TAM_MAX_PERGUNTA = TAM_MAX_PERGUNTA;

        this.frontEnd    = frontEnd;
        this.customInput = customInput;
    }

    public PerguntasAPI(FrontEndPlus frontEnd, CustomInput customInput) {

        this.TAM_MIN_TITULO = 3;
        this.TAM_MAX_TITULO = 50;

        this.TAM_MIN_PERGUNTA = 3;
        this.TAM_MAX_PERGUNTA = 120;

        this.frontEnd    = frontEnd;
        this.customInput = customInput;
    }

    /**
     * Função que serve para listar as perguntas de um usuário com base na ID do
     * usuário
     * 
     * @param idUsuario é o numero que corresponde a ID do usuário que gostaria de
     *                  ver as perguntas
     * @return um Codigo de Protocolo que representa o resultado da operação
     */
    public CelulaResposta listarPerguntas(PerguntaInterface perguntas, int idUsuario) {

        CelulaResposta resultado = new CelulaResposta();
        Pergunta[] array = perguntas.getPerguntaArray(idUsuario);

        if (array == null) {
            System.err.println("\n\n                   ¯\\_(ツ)_/¯");
            System.err.println("Ops.. parece que você não tem nenhuma pergunta...\n");

        } else {
            System.out.println(frontEnd.listar(array));
            resultado.setCdp(CodigoDeProtocolo.SUCESSO);
        }   
      
        return resultado;
    }

    /**
     * Função para criar uma nova pergunta e registrar na ID do usuario que foi
     * recebido
     * 
     * @param idUsuario é o número que corresponde a ID do usuario que quer
     *                  registrar uma pergunta
     * @return a Pergunta que foi registrada
     */
    public CelulaResposta criarPergunta(int idUsuario) {

        Pergunta novaPergunta = null;
        CodigoDeProtocolo confirmarOperacao = CodigoDeProtocolo.ERRO;

        CelulaResposta resultado = new CelulaResposta();

        novaPergunta = inserirDadosDaPergunta(idUsuario);
        if (novaPergunta == null) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }
     
        confirmarOperacao = frontEnd.verificar(novaPergunta);
        if (confirmarOperacao == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(confirmarOperacao);
            return resultado;
        }

        resultado.setPergunta(novaPergunta);
        resultado.setCdp(confirmarOperacao);

        return resultado;
    }

    /**
     * Função para alternar uma pergunta já existente para o usuário
     * 
     * @param idUsuario é o numero correspondente a ID do usuário que gostaria de
     *                  trocar a pergunta
     * @return a Pergunta que será alterada
     */
    public CelulaResposta alterarPergunta(PerguntaInterface perguntas, int idUsuario) {

        Pergunta perguntaAlterada   = null;
        int idPerguntaAlterada      = -1;
        CodigoDeProtocolo confirmar = CodigoDeProtocolo.ERRO;

        CelulaResposta resultado = new CelulaResposta();

        perguntaAlterada = escolherPergunta(perguntas, idUsuario).getPergunta();
        if (perguntaAlterada == null) {
            return resultado;
        }

        if (!perguntaAlterada.getAtiva()) {
            System.out.println("A pergunta está desativada!");
            return resultado;
        }

        idPerguntaAlterada = perguntaAlterada.getId();

        perguntaAlterada = inserirDadosDaPergunta(idUsuario);
        if(perguntaAlterada == null) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        perguntaAlterada.setId(idPerguntaAlterada);

        confirmar = frontEnd.verificar(perguntaAlterada);
        if (confirmar == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(confirmar);
            return resultado;
        }

        resultado.setPergunta(perguntaAlterada);
        resultado.setCdp(confirmar);

        return resultado;
    }

    /**
     * Função para arquivar uma pergunta do usuario
     * 
     * @param idUsuario é o numero que corresponde a ID do usuário que gostaria de
     *                  arquivar uma pergunta
     * @return a Pergunta que será registrada
     */
    public CelulaResposta arquivarPergunta(PerguntaInterface perguntas, int idUsuario) {

        Pergunta perguntaAlterada = null;
        CodigoDeProtocolo confirmarOperacao = CodigoDeProtocolo.ERRO;

        CelulaResposta resultado = new CelulaResposta();

        perguntaAlterada = escolherPergunta(perguntas, idUsuario).getPergunta();
        if (perguntaAlterada == null) {
            return resultado;
        }

        if (!perguntaAlterada.getAtiva()) {
            System.out.println("A pergunta já estava ativada!");
            return resultado;
        }

        confirmarOperacao = frontEnd.verificar(perguntaAlterada);
        if (confirmarOperacao == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(confirmarOperacao);
            return resultado;
        }

        perguntaAlterada.setAtiva(false);
        resultado.setPergunta(perguntaAlterada);
        resultado.setCdp(CodigoDeProtocolo.SUCESSO);

        return resultado;
    }

    /**
     * Função que irá permitir o usuário encontrar uma perguntas com base na
     * palavras-chave
     * 
     * @param idUsuario é o número da ID do usuário que está acessando essa função
     * @return a Pergunta que o usuário escolheu
     */
    public CelulaResposta consultarPergunta(PerguntaInterface perguntas, int idUsuario) {

        String entrada = "";

        Pergunta[] lista = null;
        CelulaResposta resultado = new CelulaResposta();

        int idPergunta = -1;

        entrada = customInput.inserir("Busque as perguntas por palavra chave separadas por espaço em branco",
                "Ex: política Brasil eleições", TAM_MIN_PERGUNTA, TAM_MAX_PERGUNTA, true);
        if (entrada.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        entrada = Pergunta.consertarPalavrasChave(entrada);

        lista = perguntas.getPerguntasPalavrasChave(entrada.split(" "), idUsuario);

        if (lista.length > 0) {

            idPergunta = frontEnd.escolher(lista);
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            if (idPergunta != -1 && idPergunta != -3) {
                resultado.setPergunta(perguntas.achar(idPergunta));
                resultado.setCdp(CodigoDeProtocolo.IRPARAPERGUNTA);

            }

        } else {
            System.out.println("Atenção! Nenhuma pergunta encontrada com as palavras-chave inserida!");
        }

        return resultado;
    }

    /**
     * Função para gerenciar a escolha de pergunta com base na ID de um usuário
     * 
     * @param idUsuario é o número que corresponde a ID do usuário que gostaria de
     *                  escolher uma de suas próprias perguntas
     * @return a Pergunta que foi propriamente escolhida
     */
    private CelulaResposta escolherPergunta(PerguntaInterface perguntas, int idUsuario) {
        int id = -1;
        Pergunta[] array = null;

        CelulaResposta resultado = new CelulaResposta();

        array = perguntas.getPerguntaArray(idUsuario);

        if (array != null) {
            id = frontEnd.escolher(array);
            if (id != -1)
                if (id == -3) {
                    resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
                } else {
                    resultado.setPergunta(perguntas.achar(id));
                }

        } else {
            System.err.println("ERRO! nenhuma pergunta encontrada!");
        }

        return resultado;
    }

    /**
     * Função genérica para inserir dados de uma pergunta
     * 
     * @param idUsuario é o numero que corresponde a ID do usuário que será dono da
     *                  pergunta inserida
     * @return a Pergunta que foi formada
     */
    private Pergunta inserirDadosDaPergunta(int idUsuario) {

        String titulo = "";
        String pergunta = "";
        String palavras_chave = "";

        Pergunta p = null;

        titulo = customInput.inserir("Insira o título da pergunta", TAM_MIN_TITULO, TAM_MAX_TITULO, true);
        if (titulo.equals("")) {
            return null;
        }
        pergunta = customInput.inserir("Insira a pergunta", TAM_MIN_PERGUNTA, TAM_MAX_PERGUNTA, true);
        if (pergunta.equals("")) {
            return null;
        }
        palavras_chave = customInput.inserir("Insira as palavras-chave dessa pergunta",
                "Exemplo: Brasil política saude", TAM_MIN_PERGUNTA, TAM_MAX_PERGUNTA, true);
        if (palavras_chave.equals("")) {
            return null;
        }

        p = new Pergunta(idUsuario, titulo, pergunta, palavras_chave);

        return p;
    }

    

}
