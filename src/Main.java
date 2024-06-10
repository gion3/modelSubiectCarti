import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception{
        List<Carte> carti = new ArrayList<>();
        carti = citireTxt("carti.txt");
        System.out.println("----------------- cerinta 1 -----------------------");
        List<Carte> cerinta1 = carti.stream()
                .filter(carte -> carte.getAnAparitie() < 1940)
                .sorted((c1,c2)->c1.getTitlu().compareTo(c2.getTitlu()))
                .collect(Collectors.toList());
        cerinta1.forEach(carte -> System.out.println(carte.getCota() + "\t" + carte.getTitlu() + "\t" + carte.getAnAparitie()));
        System.out.println("------------------------ cerinta 2 ----------------------");
        List<Imprumut> imprumuturi = new ArrayList<>();
        imprumuturi = citireDB("biblioteca.db");
        Set<String> cititori = new HashSet<String>();
        cititori = imprumuturi.stream().map(Imprumut::getNumeCititor).collect(Collectors.toSet());
        cititori.forEach(System.out::println);
        System.out.println("------------------------ cerinta 3 ---------------------------");
        scriereTxt("imprumuturi.txt");
        System.out.println("-------------------- cerinta 3 cu MAP ---------------------------");
        Map<String, Integer> imprumuturiMap = new HashMap<>();
        imprumuturiMap = imprumuturi.stream()
                .collect(Collectors.groupingBy(imprumut -> imprumut.getNumeCititor(),Collectors.summingInt(imprumut -> imprumut.getNrZileImprumut())));

        List<Map.Entry<String, Integer>> sortedList = imprumuturiMap.entrySet()
                .stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
                .collect(Collectors.toList());
        sortedList.forEach(System.out::println);
        //scriere json
        scriereJSON("carti.json", carti);
        //scriere in db
        List<Imprumut> imprumuturi2 = new ArrayList<>();
        Imprumut i1 = new Imprumut("Ionel Ionescu", "COTAMEALACHE",69);
        Imprumut i2 = new Imprumut("Fane Vancica", "COTA20",420);
        Imprumut i3 = new Imprumut("MAMATATA", "FARACOTA",1);
        imprumuturi2.add(i1);
        imprumuturi2.add(i2);
        imprumuturi2.add(i3);

        scriereDB("biblioteca.db",imprumuturi2);
    }
    public static void scriereJSON(String filePath, List<Carte> carti) throws IOException {
        JSONArray jsonCarti = new JSONArray();

        for(Carte c : carti){
            JSONObject jsonCarte = new JSONObject();
            jsonCarte.put("Cota carte", c.getCota());
            jsonCarte.put("Titlu",c.getTitlu());
            jsonCarte.put("Autor", c.getAutor());
            jsonCarte.put("Anul aparitiei", c.getAnAparitie());
            jsonCarti.put(jsonCarte);
        }

        String jsonString = jsonCarti.toString();
        Files.writeString(Paths.get(filePath),jsonString, StandardOpenOption.CREATE);
    }

    public static void scriereDB(String dbPath, List<Imprumut> imprumuturi) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        try(PreparedStatement st = con.prepareStatement("INSERT INTO imprumuturi VALUES (?,?,?)")){
            for(Imprumut i : imprumuturi){
                st.setString(1,i.getNumeCititor());
                st.setString(2,i.getCotaCarte());
                st.setInt(3,i.getNrZileImprumut());
                st.addBatch();
            }
            st.executeBatch();
        }
        con.close();
    }
    public static void scriereTxt(String filePath) throws Exception{
        try(Connection con = DriverManager.getConnection("jdbc:sqlite:biblioteca.db")){
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT \"nume student\", \"cota carte\", SUM(\"zile imprumut\") " +
                    "as total FROM IMPRUMUTURI " +
                    "GROUP BY \"nume student\" " +
                    "ORDER BY \"total\" DESC");
            List<Imprumut> rez = new ArrayList<>();
            while (rs.next()){
                Imprumut i = new Imprumut();
                i.setNumeCititor(rs.getString(1));
                i.setCotaCarte(rs.getString(2));
                i.setNrZileImprumut(rs.getInt(3));

                rez.add(i);
            }
            try(FileWriter writer = new FileWriter(filePath)) {
                writer.write("Nume Student                 - Total zile imprumut\n");
                for (Imprumut i : rez) {
                    writer.write(i.getNumeCititor() + "                      " + i.getNrZileImprumut() + "\n");
                }
            }
        }
    }
    public static List<Imprumut> citireDB(String dbPath) throws Exception{
        List<Imprumut> rez = new ArrayList<>();
        String path = "jdbc:sqlite:" + dbPath;
        try(Connection con = DriverManager.getConnection(path)){
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM IMPRUMUTURI");
            while(rs.next()){
                Imprumut i = new Imprumut();
                i.setNumeCititor(rs.getString(1));
                i.setCotaCarte(rs.getString(2));
                i.setNrZileImprumut(rs.getInt(3));
                rez.add(i);
            }
        }
        return rez;
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