package menu.sistema;

/**
 * Classe para armazenar todos os comandos para as API processar além de gerenciar seus respectivos resultados de resposta ao usuário
 * @author MysteRys337(Gustavo Lopes)
 */
public enum CodigoDeProtocolo {

    //Todos os códigos no programa

    //Operações do sistema
    ACESSOAOSISTEMA         ("1"),
    CRIARNOVOUSUARIO        ("2"),
    CRIARSENHATEMPORARIA    ("3"),

    //Inicio
    CONSULTARPERGUNTAS      ("21"),
    OLHARNOTIFICACOES       ("31"),
    NOVASENHA               ("41"),

    //Perguntas
    LISTARPERGUNTAS         ("12"),
    NOVAPERGUNTA            ("22"),
    ALTERARPERGUNTA         ("32"),
    ARQUIVARPERGUNTA        ("42"),

    //Respostas
    LISTARRESPOSTAS         ("14"),
    INCLUIRRESPOSTA         ("24"),
    ALTERARRESPOSTA         ("34"),
    ARQUIVARRESPOSTA        ("44"),
    
    //Possíveis resultados
    SUCESSO                 ("111"),
    ERRO                    ("000"),
    NULL                    ("-1"),
    MUDARUSUARIO            ("222"),
    OPERACAOCANCELADA       ("-3");

    private String codigo;

    CodigoDeProtocolo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return this.codigo;
    }

    /**
     * Recebe um codigo de protocolo e coloca na tela uma mensagem que corresponde ao resultado de uma operação realizada pelo usuário
     * @param cdp é o Codigo de Protocolo recebido pelo usuário
     */
    public static void verificarCodigo(CodigoDeProtocolo cdp) {
        
        switch(cdp) {
            case ERRO:
                System.out.println("Operação terminou com erro!");
                break;

            case SUCESSO:
                System.out.println("Operação terminou com sucesso!");
                break;

            case MUDARUSUARIO:
                System.out.println("Seja bem vindo usuário!");
                break;

            case OPERACAOCANCELADA:
                System.out.println("Operação cancelada pelo usuário");
                break;

            default:
                System.out.println("Erro! Operação desconhecida");

        }
        Sistema.esperarUsuario();
    }
}
