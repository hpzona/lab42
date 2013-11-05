import java.net.*;
import java.io.*;
import java.util.*;

public class FTPServidor {

    public static void main(String args[]) throws Exception {
        int numeroPorta = 6666;
        boolean validou = false;

        BufferedReader valorDigitado;
        valorDigitado = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Defina a Porta para o servidor: ");
        do {
            try {
                numeroPorta = Integer.parseInt(valorDigitado.readLine());
                validou = true;
            } catch (NumberFormatException e) {
                System.out.println("Voce não digitou um numero.");
                System.out.print("Porta para o servidor: ");
            }
        } while (validou != true);

        try {
            ServerSocket soc = new ServerSocket(numeroPorta);
            System.out.println("Servidor FTP iniciado na porta " + numeroPorta);
            while (true) {
                System.out.println("Esperando cliente conectar...");
                TranferenciaObjeto t = new TranferenciaObjeto(soc.accept());
            }
        } catch (UnknownHostException ExecHost) {
            System.out.println("Erro na conexão");
        } catch (IOException ioe) {
            System.out.println("Erro na conexão");
        }
    }

}


class TranferenciaObjeto extends Thread {

    Socket ClientSoc;
    Objeto objetoRequisicao;

    DataInputStream din;
    DataOutputStream dout;
    
    ObjectInputStream objInputStream;
    ObjectOutputStream objOutputStream;

    TranferenciaObjeto(Socket soc) {
        try {
            ClientSoc = soc;
            din = new DataInputStream(ClientSoc.getInputStream());
            dout = new DataOutputStream(ClientSoc.getOutputStream());
            
            objOutputStream = new ObjectOutputStream(ClientSoc.getOutputStream());
            objInputStream = new ObjectInputStream(ClientSoc.getInputStream());
            
            System.out.println("FTP Client Connected ...");
            start();

        } catch (Exception ex) {
        }
    }

    void SendFile() throws Exception {
           
        String filename = din.readUTF();
        File f = new File(filename);
        if (!f.exists()) {
            dout.writeUTF("Arquivo nao encontrado");
            return;
        } else {
            dout.writeUTF("PRONTO");
            FileInputStream fin = new FileInputStream(f);
            int ch;
            do {
                ch = fin.read();
                dout.writeUTF(String.valueOf(ch));
            } while (ch != -1);
            fin.close();
            dout.writeUTF("Arquivo recebedio com sucesso");
        }
    }

    void ReceiveFile() throws Exception {
        objetoRequisicao = (Objeto)objInputStream.readObject();
        
        String filename = objetoRequisicao.getNome();
        if (filename.compareTo("Arquivo nao encontrado") == 0) {
            return;
        }
        File f = new File(filename);
        String opcao;

        if (f.exists()) {
            dout.writeUTF("Este arquivo ja existe");
            opcao = din.readUTF();
        } else {
            dout.writeUTF("SendFile");
            opcao = "S";
        }

        if (opcao.compareTo("S") == 0) {
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
            dout.writeUTF("File Send Successfully");
        } else {
            return;
        }

    }

    public void run() {
        while (true) {
            try {
                System.out.println("Waiting for Command ...");
                String Command = din.readUTF();
                if (Command.compareTo("GET") == 0) {
                    System.out.println("\tGET Command Received ...");
                    SendFile();
                    continue;
                } else if (Command.compareTo("SEND") == 0) {
                    System.out.println("\tSEND Command Receiced ...");
                    ReceiveFile();
                    continue;
                } else if (Command.compareTo("DISCONNECT") == 0) {
                    System.out.println("\tDisconnect Command Received ...");
                    System.exit(1);
                }
            } catch (Exception ex) {
            }
        }
    }
}
