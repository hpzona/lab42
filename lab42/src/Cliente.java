import java.net.*;
import java.io.*;
import javax.swing.JOptionPane;

class Cliente {

    public static void main(String args[]) throws Exception {
        int numeroPorta = 6666;
        String numeroIP;
        boolean validou = false;

        numeroIP = JOptionPane.showInputDialog(null, "IP do servidor: \n\n Aperte \"OK\" para usar um IP padrão");
        if ("".equals(numeroIP)) {
            numeroIP = "127.0.0.1";
            JOptionPane.showMessageDialog(null, "Foi assumido o IP: 127.0.0.1");
        }

        do {
            try {
                numeroPorta = Integer.parseInt(JOptionPane.showInputDialog(null, "Porta do servidor:"));
                validou = true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Voce não digitou um numero");
                numeroPorta = Integer.parseInt(JOptionPane.showInputDialog(null, "Porta do servidor:"));
            }
        } while (validou != true);

        try {
            System.out.println("Conectando ao servidor...");
            Socket client = new Socket(numeroIP, numeroPorta);
            System.out.println("Conectado");

            String nome = JOptionPane.showInputDialog(null, "Nome do arquivo:");
            String caminho = JOptionPane.showInputDialog(null, "Caminho do arquivo:");
            String extensao = JOptionPane.showInputDialog(null, "Extensão do arquivo:");
            caminho = caminho.concat("\\");

            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            oos.writeObject(new Objeto(nome, extensao, caminho));

            ObjectInputStream in = new ObjectInputStream(client.getInputStream());

                for (File file : (File[]) in.readObject()) {
                    JOptionPane.showMessageDialog(null, "Arquivo " + file.getName() + " recebido pelo cliente");
                }
            client.close();
        } catch (UnknownHostException ExecHost) {
            System.out.println("Erro na conexão");
        } catch (IOException ioe) {
            System.out.println("Erro na conexão");
        }
    }
}
