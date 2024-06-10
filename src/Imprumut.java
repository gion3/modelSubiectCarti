public class Imprumut {
    private String numeCititor;
    private String cotaCarte;
    private int nrZileImprumut;

    public Imprumut(){}

    public Imprumut(String numeCititor, String cotaCarte, int nrZileImprumut) {
        this.numeCititor = numeCititor;
        this.cotaCarte = cotaCarte;
        this.nrZileImprumut = nrZileImprumut;
    }

    public String getNumeCititor() {
        return numeCititor;
    }

    public void setNumeCititor(String numeCititor) {
        this.numeCititor = numeCititor;
    }

    public String getCotaCarte() {
        return cotaCarte;
    }

    public void setCotaCarte(String cotaCarte) {
        this.cotaCarte = cotaCarte;
    }

    public int getNrZileImprumut() {
        return nrZileImprumut;
    }

    public void setNrZileImprumut(int nrZileImprumut) {
        this.nrZileImprumut = nrZileImprumut;
    }

    @Override
    public String toString() {
        return "Imprumut{" +
                "numeCititor='" + numeCititor + '\'' +
                ", cotaCarte='" + cotaCarte + '\'' +
                ", nrZileImprumut=" + nrZileImprumut +
                '}';
    }
}
