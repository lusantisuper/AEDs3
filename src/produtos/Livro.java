package produtos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import crud.Registro;

public class Livro implements Registro {
    private String nomeLivro;
    private String nomeAutor;
    private float  preco;
    private int    id = -1; 

    // Construtor de um livro vazio
    public Livro() {
        this("", "", -1f);
    }

    // Construtor de um Livro
    public Livro(String nome, String autor, float preco) {
        this.nomeLivro = nome;
        this.nomeAutor = autor;
        this.preco     = preco;
    }

    // Retorno da chave secundaria de pesquisa
    public String chaveSecundaria() {
        return this.nomeLivro;
    }

    // Set ID
    public void setId(int id) {
        this.id = id;
    }

    // Set nome do Livro
    public void setNome(String nome) {
        this.nomeLivro = nome;
    }

    // Set nome do Autor
    public void setAutor(String autor) {
        this.nomeAutor = autor;
    }

    // Set preco do Livro
    public void setPreco(float preco) {
        this.preco = preco;
    }

    // Get ID
    public int getId() {
        return this.id;
    }

    // Get nome do Livro
    public String getNome() {
        return this.nomeLivro;
    }

    // Get nome do Livro
    public String getAutor() {
        return this.nomeAutor;
    }

    // Get nome do Livro
    public float getPreco() {
        return this.preco;
    }

    // Serializar objeto
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream      data      = new DataOutputStream(byteArray);
        
        data.writeInt(this.id);
        data.writeFloat(this.preco);
        data.writeUTF(this.nomeLivro);
        data.writeUTF(this.nomeAutor);
        
        return byteArray.toByteArray();
    }

    // Desserializar objeto
    public void fromByteArray(byte[] arrayObjeto) throws IOException {
        ByteArrayInputStream byteArray = new ByteArrayInputStream(arrayObjeto);
        DataInputStream      data      = new DataInputStream(byteArray);

        this.id        = data.readInt();
        this.preco     = data.readFloat();
        this.nomeLivro = data.readUTF();
        this.nomeAutor = data.readUTF();
    }

    public String toString() {
        return "ID: "              + this.id        + "\n" +
                "Nome do livro: "  + this.nomeLivro + "\n" +
                "Nome do autor: "  + this.nomeAutor + "\n" +
                "Preço do livro: " + this.preco     + "\n";

    }
}