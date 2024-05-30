package picerija;

import java.io.Serializable;
import java.util.List;

public class PicasPasutijums implements Serializable {
    private String vards;
    private String adrese;
    private String telefons;
    private String izmers;
    private List<String> piedevas;
    private String merce;
    private boolean piegade;
    private double cena;

    public PicasPasutijums(String vards, String adrese, String telefons, String izmers, List<String> piedevas, String merce, boolean piegade, double cena) {
        this.vards = vards;
        this.adrese = adrese;
        this.telefons = telefons;
        this.izmers = izmers;
        this.piedevas = piedevas;
        this.merce = merce;
        this.piegade = piegade;
        this.cena = cena;
    }

    @Override
    public String toString() {
        return "Vards: " + vards +
                "\nAdrese: " + adrese +
                "\nTelefons: " + telefons +
                "\nIzmers: " + izmers +
                "\nPiedevas: " + String.join(", ", piedevas) +
                "\nMerce: " + merce +
                "\nPiegade: " + (piegade ? "Ja" : "Ne") +
                "\nCena: " + cena +
        		"\n--------------------------------------------";
    }
}