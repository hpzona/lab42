
import java.io.Serializable;

public class Objeto implements Serializable {
	private static final long serialVersionUID = 8631675912275300021L;

    String nome, extensao, caminho;
    
   public Objeto(){        
    }
   
   public Objeto(String nome, String ext, String cmn){
        super();
        this.extensao = ext;
        this.nome = nome;
        this.caminho = cmn;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
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
}
