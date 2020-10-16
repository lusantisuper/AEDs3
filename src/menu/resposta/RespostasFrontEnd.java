package menu.resposta;

import menu.sistema.*;
import menu.sistema.abstracts.frontend.RegistroVisual;
import menu.sistema.abstracts.frontend.RegistroVisualResposta;
import menu.sistema.abstracts.frontend.RegistroVisualplus;
import menu.sistema.abstracts.frontend.RespostaFrontEndInterface;
import menu.sistema.controle.CodigoDeProtocolo;
import menu.sistema.graficos.*;
import menu.sistema.input.Input;
import produtos.Resposta;

public class RespostasFrontEnd implements RespostaFrontEndInterface{

		//Atributos
        private ANSILibrary   destaqueData;
        private Input         input;
        private ASCIInterface graficos;

        public RespostasFrontEnd(ANSILibrary destaqueData,ASCIInterface graficos,Input input) {
            this.destaqueData = destaqueData;
            this.input        = input;
            this.graficos     = graficos;
        }

        /**
         * Função para retornar todas as respostas em um array em formato String
         * @param array é o array de respostas que foi enviado
         * @return a String correspondente a listagem das respostas
         */
        public String listar(RegistroVisualplus[] array) {

            String  resp     = "";
            byte    contador = 1;

            resp += graficos.caixa(3,"Respostas");
           
            for (RegistroVisualplus i : array) {
                if(i.getAtiva() == false) 
                    resp += "\n(Arquivada)";

                resp += "\n" + destaqueData.imprimir(contador + ".") + "\n";
                resp += i.imprimir() + "\n";
                contador++;
                
            }
    
            return resp;
        }

        /**
         * Função para retornar todas as respostas em um array em formato String
         * @param array é o array de respostas que foi enviado
         * @return a String correspondente a listagem das respostas
         */
        public String listarGeral(RegistroVisualResposta[] array) {

            String  resp     = "";
            String  nome     = "";
            
            byte    contador = 1;

            resp += graficos.caixa(3,"Respostas");

           
            for (RegistroVisualResposta i : array) {
                if(i.getAtiva() == false) 
                    continue;

                nome = APIControle.acharUsuario(i.getIdUsuario()).getNome();

                resp += "\n" + destaqueData.imprimir(contador + ".") + "\n";
                resp += i.imprimir(nome) + "\n";
                contador++;
                
            }
    
            return resp;
        }

        /**
         * Função para retornar todas as perguntas em um array em formato String de forma simplificada
         * @param array é o array de perguntas que foi enviado
         * @return a String correspondente a listagem das perguntas
         */
        public String listarSimplificado(RegistroVisualplus[] array) {

            String  resp     = "";
            byte    contador = 1;

            resp += graficos.caixa(3,"PERGUNTAS");

            for (RegistroVisualplus i : array) {
                if(i.getAtiva() == false) {
                    resp += "\n(Arquivada)";
                }

                resp += "\n" + destaqueData.imprimir(contador + ".") + "\n";
                resp += i.imprimirSimplificado();
                contador++;
                
            }
    
            return resp;
        }

        /**
         * Função para fazer a confirmação com o usuário se essa é a resposta que será registrada/alterada/arquivada
         * @param r é a resposta que foi recebida seja qual for a operação
         * @return um codigo de protocolo referente ao resultado da verificacao
         */
        public CodigoDeProtocolo verificar(RegistroVisual r) {

            String              confirmar   = "";
            CodigoDeProtocolo   sucesso     = CodigoDeProtocolo.ERRO;

            System.out.println(graficos.caixa("Vamos conferir a sua pergunta") + "\n");
            System.out.print(r.imprimir() + 
                            "\nEssa é a sua pergunta?(s/n) : ");

            confirmar = input.lerString();

            graficos.limparTela();

            if(confirmar.length() == 0 || confirmar.toLowerCase().equals("s")) {

                sucesso = CodigoDeProtocolo.SUCESSO;
            } else {
                sucesso = CodigoDeProtocolo.OPERACAOCANCELADA;
                System.out.println("Processo cancelado!\nVoltando para o menu...\n");
            }

            return sucesso;
        }

        /**
         * Função intermediaria para escolher uma resposta em um determinado array
         * @param array que é o array de respostas na qual o usuário terá que fazer uma escolha
         * @return o Id da resposta que o usuário escolheu
         */
        public int escolherResposta(Resposta[] array) {

            byte     entrada          = -1;
            int      indexSelecionado = -1;
    
            System.out.print(listar(array) + 
                            "\nEscolha uma das respostas: \nObs: Pressione \'0\' para voltar ao menu\n-> ");
    
            entrada = input.lerByte();
            graficos.limparTela();     
    
            if (array.length > entrada - 1 && entrada - 1 >= 0) {

                    indexSelecionado = array[entrada -1].getId();
            }
            else {
                if ( entrada != 0 )
                    System.err.println("ERRO! Entrada inválida!");
                else
                    indexSelecionado = -3; //Sinal para o programa que o usuário cancelou o processo
            }
            
            return indexSelecionado; 
        }

}