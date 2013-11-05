
import java.net.*;
import java.io.*;
import static java.sql.Types.NULL;
import java.util.*;

class FTPCliente {

    public static void main(String args[]) throws Exception {
        int numeroPorta = 6666;
        String numeroIP;
        boolean validou = false;

        BufferedReader bufferRead;
        bufferRead = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("IP do servidor: ");
        numeroIP = bufferRead.readLine();
        if ("".equals(numeroIP)) {
            numeroIP = "127.0.0.1";
            System.out.println("Foi assumido o IP: 127.0.0.1");
        }

        System.out.print("Porta do servidor: ");
        do {
            try {
                numeroPorta = Integer.parseInt(bufferRead.readLine());
                validou = true;
            } catch (NumberFormatException e) {
                System.out.println("Voce não digitou um numero");
                System.out.print("Porta do servidor: ");
            }
        } while (validou != true);

        try {
            Socket soc = new Socket(numeroIP, numeroPorta);
            TransferenciaCliente t = new TransferenciaCliente(soc);
            t.displayMenu();
        } catch (UnknownHostException ExecHost) {
            System.out.println("Erro na conexão");
        } catch (IOException ioe) {
            System.out.println("Erro na conexão");
        }
    }
}

class TransferenciaCliente {

    Socket ClientSoc;
    Objeto objetoRequisicao;

    FileInputStream din;
    DataOutputStream dout;
    ObjectInputStream objInputStream;
    ObjectOutputStream objOutputStream;
    BufferedReader bufferRead;
        
    TransferenciaCliente(Socket soc) {
        try {
            ClientSoc = soc; 
            objetoRequisicao = new Objeto();
            din = new FileInputStream(ClientSoc.getInputStream().toString());
            dout = new DataOutputStream(ClientSoc.getOutputStream());
            bufferRead = new BufferedReader(new InputStreamReader(System.in));
            objOutputStream = new ObjectOutputStream(ClientSoc.getOutputStream());
            objInputStream = new ObjectInputStream(ClientSoc.getInputStream()); 
        } catch (Exception ex) {
        }
    }
    
    void EnviarObjeto(Objeto objeto) throws IOException {
         objOutputStream.writeObject(objeto);    
         //objOutputStream.close();
    }

    Object ReceberObjeto() throws ClassNotFoundException, IOException {
        return objInputStream.readObject();
    }
    
    void EnviarArquivo() throws Exception {

        String filename;
        System.out.print("Digite o nome do arquivo: ");
        filename = bufferRead.readLine();

        File f = new File(filename);
        if (!f.exists()) {
            System.out.println("Arquivo inexistente...");
            objetoRequisicao.setInformacao("Arquivo nao encontrado");
            EnviarObjeto(objetoRequisicao);
            return;
        }
        
        objetoRequisicao.setNome(filename);
        EnviarObjeto(objetoRequisicao);

        Object msgFromServer = objInputStream.readObject();
        objetoRequisicao = (Objeto)msgFromServer;
        
        /*if (objetoRequisicao.getInformacao().compareTo("Este arquivo ja existe") == 0) {
            String opcao;
            System.out.println("Este arquivo ja existe. Deseja sobrescrever (S/N)?");
            opcao = bufferRead.readLine();
            if (opcao == "S") {
                dout.writeUTF("S");
            } else {
                dout.writeUTF("N");
                return;
            }
        }*/

        System.out.println("Enviando arquivo...");
        FileInputStream fin = new FileInputStream(f);
        int ch;
        do {
            ch = fin.read();
            dout.writeUTF(String.valueOf(ch));
        } while (ch != -1);
        fin.close();
        //System.out.println(din.readUTF());

    }

    /*void PesquisarArquivoNoServidor() throws Exception {
        String fileName;
        System.out.print("Digite o nome do arquivo: ");
        fileName = bufferRead.readLine();
        objetoRequisicao.setNome(fileName);
        dout.writeUTF(fileName);
        String msgFromServer = din.readUTF();

        if (msgFromServer.compareTo("Arquivo nao encontrado") == 0) {
            System.out.println("Arquivo nao encontrado no servidor.");
            return;
        } else if (msgFromServer.compareTo("PRONTO") == 0) {
            System.out.println("Recebendo arquivo...");
            File f = new File(fileName);
            if (f.exists()) {
                String opcao;
                System.out.println("Este arquivo ja existe. Deseja sobrescrever (S/N)?");
                opcao = bufferRead.readLine();
                if (opcao == "N") {
                    dout.flush();
                    return;
                }
            }
            FileOutputStream fout = new FileOutputStream(f);
            int ch;
            String temp;
            do {
                temp = din.readUTF();
                ch = Integer.parseInt(temp);
                if (ch != -1) {
                    fout.write(ch);
                }
            } while (ch != -1);
            fout.close();
            System.out.println(din.readUTF());
        }
    }*/

    public void displayMenu() throws Exception {
        while (true) {
            System.out.println("[ MENU ]");
            System.out.println("1. Enviar arquivo");
            System.out.println("2. Receber arquivo");
            System.out.println("3. Pesquisar por nome");
            System.out.println("4. Pesquisar por extensão");
            System.out.println("5. Pesquisar por tipo");
            System.out.println("6. Pesquisar por conteúdo");
            System.out.println("7. Sair");
            System.out.print("\nDigite sua opção: ");
            int opcao;
            opcao = Integer.parseInt(bufferRead.readLine());
            if (opcao == 1) {
                //dout.writeUTF("SEND");
                EnviarArquivo();
            } else if (opcao == 2) {
                //dout.writeUTF("GET");
                //PesquisarArquivoNoServidor();
            } else {
                //dout.writeUTF("DISCONNECT");
                System.exit(1);
            }
        }
    }
}
