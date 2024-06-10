import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServerThread extends Thread{
    private Socket client = null;
    private static int nrClienti = 0;

    static List<Carte> carti = new ArrayList<>();
    public ServerThread(Socket socket) {
        super("ServerThread");
        this.client = socket;
        this.setName(this.getName() + "-" + ++nrClienti);
    }

    @Override
    public void run() {
        try (DataInputStream in = new DataInputStream(client.getInputStream());
             DataOutputStream out = new DataOutputStream(client.getOutputStream())){

            System.out.println("Conexiune acceptata de la client " +
                    client.getRemoteSocketAddress() + "; " + this.getName());

            String mesajClient = null;

            while (true) {
                carti = citireTxt("carti.txt");

                mesajClient = in.readUTF();

                System.out.println(mesajClient);

                String finalMesajClient = mesajClient;
                Optional<Carte> cautata = carti.stream().filter(c -> c.getCota().equals(finalMesajClient)).findFirst();

                out.writeUTF(cautata.get().getTitlu() + ", " + cautata.get().getAutor() + ", " + cautata.get().getAnAparitie() + "\n");
            }

        } catch (EOFException eof) {
            System.out.println("Deconectare de la client " +
                    "(" + this.getName() + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<Carte> citireTxt(String filePath) throws FileNotFoundException {
        List<Carte> rez = new ArrayList<>();
        String line;
        try(BufferedReader in = new BufferedReader(new FileReader(filePath))){
            while ((line = in.readLine()) != null){
                Carte c = new Carte();
                String[] tokens = line.split("\t");
                c.setCota(tokens[0]);
                c.setTitlu(tokens[1]);
                c.setAutor(tokens[2]);
                c.setAnAparitie(Integer.parseInt(tokens[3]));
                rez.add(c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rez;
    }
}
