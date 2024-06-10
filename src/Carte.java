public class Carte {
    private String cota;
    private String titlu;
    private String autor;
    private int anAparitie;

    public Carte(){}

    public Carte(String cota, String titlu, String autor, int anAparitie) {
        this.cota = cota;
        this.titlu = titlu;
        this.autor = autor;
        this.anAparitie = anAparitie;
    }

    public String getCota() {
        return cota;
    }

    public void setCota(String cota) {
        this.cota = cota;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnAparitie() {
        return anAparitie;
    }

    public void setAnAparitie(int anAparitie) {
        this.anAparitie = anAparitie;
    }

    @Override
    public String toString() {
        return "Carte{" +
                "cota='" + cota + '\'' +
                ", titlu='" + titlu + '\'' +
                ", autor='" + autor + '\'' +
                ", anAparitie=" + anAparitie +
                '}';
    }
}
