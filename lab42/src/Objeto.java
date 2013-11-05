
import java.io.Serializable;

public class Objeto implements Serializable{

    String nome, extensao, data, informacao;
    Integer tamanho;
    
    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getExtensao() {
        return extensao;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getTamanho() {
        return tamanho;
    }

    public void setTamanho(Integer tamanho) {
        this.tamanho = tamanho;
    }
    
    public Objeto(){
        this.data = "";
        this.extensao = "";
        this.informacao = "";
        this.nome = "";
        this.tamanho = null;
    }
}
