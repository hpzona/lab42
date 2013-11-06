
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Servidor extends Thread {

    ServerSocket sct;

    public Servidor(int porta) throws IOException {
        sct = new ServerSocket(porta);
    }

    public static void main(String args[]) throws Exception {
        int numeroPorta = 0000;
        boolean validou = false;
        do {
            try {
                numeroPorta = Integer.parseInt(JOptionPane.showInputDialog("Defina a Porta para o servidor: "));
                validou = true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Voce não digitou um numero.");
                numeroPorta = Integer.parseInt(JOptionPane.showInputDialog("Defina a Porta para o servidor: "));
            }
        } while (validou != true);
        try {
            Thread t = new Servidor(numeroPorta);
            t.start();
        } catch (IOException e) {
        }
    }

    private static File[] procurarObj(Object infos) {
        final Objeto obj = (Objeto) infos;
        File dir = new File(obj.getCaminho());

        File[] arquivos = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(obj.getNome())
                        & name.endsWith(obj.getExtensao());
            }
        });

        if (arquivos != null) {
            return arquivos;
        }
        return null;

    }

    public void run() {
        while (true) {
            try {
                System.out.println("Aguardando Cliente.. ");
                Socket server = sct.accept();
                System.out.println("Conectado");

                ObjectInputStream in = new ObjectInputStream(server.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());

                oos.writeObject(procurarObj(in.readObject()));
            } catch (UnknownHostException ExecHost) {
                System.out.println("Erro na conexão");
            } catch (IOException ioe) {
                System.out.println("Erro na conexão");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
