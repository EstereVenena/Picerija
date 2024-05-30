package picerija;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class Main {
    private static final String ORDER_FILE = "picas_pasutijumi.txt"; 
    private static List<PicasPasutijums> pasutijumi = new ArrayList<>(); 
    public static void main(String[] args) {
        loadOrders();
        while (true) {
           
            String[] options = {"Jauns Pasutijums", "Skatit Pasutijumus", "Iziet"};
            int choice = JOptionPane.showOptionDialog(null, "Izveleties opciju:", "Picas Pasutijumu Sistema",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

           
            if (choice == 0) {
                createNewOrder(); 
            } else if (choice == 1) {
                viewOrders(); 
            } else {
                saveOrders();
                break;
            }
        }
    }

    private static void createNewOrder() {
     
        String vards = JOptionPane.showInputDialog("Ievadiet savu vardu:");
        if (vards == null || vards.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vards ir obligats.");
            return;
        }

        String telefons;
        while (true) {
            String tel = JOptionPane.showInputDialog("Ievadiet savu telefona numuru:");
            if (tel == null) {
                return;
            }
            if (tel.matches("\\d{8}")) {
                telefons = "+371" + tel;
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Telefona numuram jabut no 8 cipariem!");
            }
        }

       
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        String[] izmeri = {"Maza 10€", "Vidēja 12€", "Liela 14€"};
        JComboBox<String> izmeraIzvele = new JComboBox<>(izmeri);
        panel.add(new JLabel("Izveleties picas izmeru:"));
        panel.add(izmeraIzvele);

        String[] piedevuOpcijas = {"Siers +0.5€", "Pepperoni +0.5€", "Sampinjoni +0.5€", "Sīpoli +0.5€", "Bekons +0.5€"};
        JCheckBox[] piedevuIzvelnes = new JCheckBox[piedevuOpcijas.length];
        panel.add(new JLabel("Izveleties piedevas:"));
        for (int i = 0; i < piedevuOpcijas.length; i++) {
            piedevuIzvelnes[i] = new JCheckBox(piedevuOpcijas[i]);
            panel.add(piedevuIzvelnes[i]);
        }

        String[] mercesOpcijas = {"Tomatu", "Kiploku", "Barbecue"};
        JComboBox<String> mercesIzvele = new JComboBox<>(mercesOpcijas);
        panel.add(new JLabel("Izveleties merci:"));
        panel.add(mercesIzvele);

        int rezultats = JOptionPane.showConfirmDialog(null, panel, "Picas Pasutijums", JOptionPane.OK_CANCEL_OPTION);
        if (rezultats != JOptionPane.OK_OPTION) {
            return;
        }

        String izmers = (String) izmeraIzvele.getSelectedItem();
        List<String> piedevas = new ArrayList<>();
        for (JCheckBox checkbox : piedevuIzvelnes) {
            if (checkbox.isSelected()) {
                piedevas.add(checkbox.getText());
            }
        }
        String merce = (String) mercesIzvele.getSelectedItem();

       
        int piegadesOpcija = JOptionPane.showConfirmDialog(null, "Vai velaties piegadi? +5€", "Piegades Opcija", JOptionPane.YES_NO_OPTION);
        boolean piegade = (piegadesOpcija == JOptionPane.YES_OPTION);

        String adrese = "";
        if (piegade) {
            adrese = JOptionPane.showInputDialog("Ievadiet savu adresi (Ielas nosaukums, Majas numurs, Dzivokla numurs):");
            if (adrese == null || adrese.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Adrese ir obligata piegadei.");
                return;
            }
        }


        double cena = 10.0;
        switch (izmers) {
            case "Vidēja":
                cena += 2.0;
                break;
            case "Liela":
                cena += 4.0;
                break;
        }
        cena += piedevas.size() * 0.5;
        if (piegade) {
            cena += 5.0;
        }

       
        PicasPasutijums pasutijums = new PicasPasutijums(vards, adrese, telefons, izmers, piedevas, merce, piegade, cena);
        pasutijumi.add(pasutijums);

      
        JOptionPane.showMessageDialog(null, "Jūsu pasūtījums ir veikts!\n" + pasutijums);
    }

    private static void viewOrders() {
        StringBuilder sb = new StringBuilder();
        for (PicasPasutijums pasutijums : pasutijumi) {
            sb.append(pasutijums).append("\n");
        }

        String[] options = {"Dzest visus pasutijumus", "Aizvērt"};
        int choice = JOptionPane.showOptionDialog(null, sb.toString(), "Pasutijumu saraksts",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if (choice == 0) {
            pasutijumi.clear();
            JOptionPane.showMessageDialog(null, "Visi pasutijumi ir dzesti.");
        }
    }

    private static void loadOrders() {
        try (BufferedReader br = new BufferedReader(new FileReader(ORDER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String vards = line.split(":")[1].trim();
                String adrese = br.readLine().split(":")[1].trim();
                String telefons = br.readLine().split(":")[1].trim();
                String izmers = br.readLine().split(":")[1].trim();
                String[] piedevas = br.readLine().split(":")[1].trim().split(", ");
                String merce = br.readLine().split(":")[1].trim();
                boolean piegade = br.readLine().split(":")[1].trim().equals("Ja");
                double cena = Double.parseDouble(br.readLine().split(":")[1].trim());

                
                List<String> piedevuSaraksts = new ArrayList<>();
                for (String piedeva : piedevas) {
                    if (!piedeva.isEmpty()) {
                        piedevuSaraksts.add(piedeva);
                    }
                }
               pasutijumi.add(new PicasPasutijums(vards, adrese, telefons, izmers, piedevuSaraksts, merce, piegade, cena));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Kļūda ielādējot iepriekšējos pasūtījumus: " + e.getMessage(), "Kļūda", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void saveOrders() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ORDER_FILE))) {
            for (PicasPasutijums pasutijums : pasutijumi) {
                bw.write(pasutijums.toString());
                bw.write("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Kļūda saglabājot pasūtījumus: " + e.getMessage(), "Kļūda", JOptionPane.ERROR_MESSAGE);
        }
    }
}