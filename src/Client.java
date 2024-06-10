import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Parametrii invalizi!");
            System.exit(-1);
        }

        String numeServer = args[0];
        int port = Integer.valueOf(args[1]);

        try {
            System.out.println("Conectare la " + numeServer +
                    " port " + String.valueOf(port));
            Socket client = new Socket(numeServer, port);

            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            BufferedReader commandLine = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Scrie 'exit' pentru a inchide clientul!");

            while (true) {
                String mesaj = commandLine.readLine();
                if (mesaj.equalsIgnoreCase("exit")) {
                    in.close();
                    out.close();
                    client.close();
                    System.exit(0);
                } else {
                    out.writeUTF(mesaj);
                }

                System.out.println(in.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
