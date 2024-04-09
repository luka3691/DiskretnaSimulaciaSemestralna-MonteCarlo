import MonteCarlo.ISimDelegate;
import MonteCarlo.SimJadro;
import MonteCarlo.Predajna;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


public class GUIapp implements ISimDelegate {
    private AtomicBoolean isStopped;
    private AtomicBoolean isPaused;
    private JPanel panel1;
    private JTextField pocetObsluzField;
    private JTextField pocetPokladField;
    private JTextField pocetReplikField;
    private JRadioButton normálnyBehRadioButton;
    private JRadioButton zrýchlenýBehRadioButton;
    private JButton štartButton;
    private JButton pauzaButton;
    private JButton stopButton;
    private JLabel pocetObsluzLabel;
    private JPanel normalnyBeh;
    private JPanel zrychlenyBeh;
    private JTable tableZakaznici;
    private JTable tableOdber;
    private DefaultTableModel odberModel = new DefaultTableModel(new String[]{"ID", "Typ pokladne", "Je volna?", "Vytazenie"}, 0);
    private DefaultTableModel pokladneModel = new DefaultTableModel(new String[]{"ID", "Je volna?", "V rade pred" , "Vytazenie"}, 0);
    private DefaultTableModel zakazniciModel = new DefaultTableModel(new String[]{"ID", "Typ zákazníka", "Stav"}, 0);
    private JTable tablePokladne;
    private JLabel stavyZakazLabel;
    private JLabel stavyOdberLabel;
    private JLabel stavyPokladLabel;
    private JLabel pocetPokladLabel;
    private JLabel PocetReplikLabel;
    private JSlider rychlostSlider;
    private JLabel pocetLudiPredAutomatom;
    private JLabel pocetOnlineZakaznikovRad;
    private JLabel pocetOstatnychZakaznikovRad;
    private JLabel casLabel;
    private JPanel prepinaciPanel;
    private JLabel obsadenyAutomatLabel;
    private JCheckBox porovananieCheckbox;
    private JLabel cisloReplikacieLabel;
    private JLabel priemerZakaznikovLabel;
    private JLabel priemerCasVSystemeLabel;
    private JLabel priemerCasOdchodLabel;
    private JLabel intervalSpolahlivostiLabel;
    private JLabel casCakaniaPredAutomatomLabel;
    private JLabel priemerDlzkaFrontuPredAutomatomLabel;
    private JLabel vytazeneiAutomatuLabel;
    private JLabel vytazenieObsluznychLabel;
    private JLabel vytazeniePokladniLabel;
    private JLabel dlzkyRadovPriPokladniachLabel;
    private JLabel dlzkyRadovPredObsluznymiLabel;
    private JLabel priemerObsluzenychLabel;
    private JLabel vytazenostAutomatuLabel;
    private SimJadro pausedSim;
    int pocetPokladni;
    int pocetObsluz ;
    int replikacieCounter;

    private boolean pomalyBeh;

    public GUIapp() {
        JFrame frame = new JFrame("Simulácia");
        tableOdber.setModel(odberModel);
        tablePokladne.setModel(pokladneModel);
        tableZakaznici.setModel(zakazniciModel);
        pocetObsluzField.setText("13");
        pocetPokladField.setText("4");
        pocetReplikField.setText("25000");
        pauzaButton.setEnabled(false);
        stopButton.setEnabled(false);
        pomalyBeh = false;
        isStopped = new AtomicBoolean(false);
        isPaused = new AtomicBoolean(false);

        štartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer.parseInt(pocetObsluzField.getText());
                    Integer.parseInt(pocetPokladField.getText());
                    Integer.parseInt(pocetReplikField.getText());
                    startSimulation();
                    štartButton.setEnabled(false);
                    pauzaButton.setEnabled(true);
                    stopButton.setEnabled(true);
                }
                catch (NumberFormatException i) {
                    //Nebolo zadane cislo
                }
            }
        });
        pauzaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    if (isPaused.get()) {
                        unpause();
                    } else {
                        isPaused.set(true);
                    }
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                porovananieCheckbox.setEnabled(true);
                štartButton.setEnabled(true);
                pauzaButton.setEnabled(false);
                stopButton.setEnabled(false);
                isStopped.set(true);
            }
        });
        zrýchlenýBehRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (zrýchlenýBehRadioButton.isSelected()) {
                    zrychliBeh();
                } else  {
                    spomalBeh();
                }
            }
        });

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel1,BorderLayout.PAGE_START);
        switchPanel("normalBehCard");


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);

        frame.setVisible(true);

    }

    private void zrychliBeh() {
        pomalyBeh = false;
        switchPanel("zrychlenyBehCard");
    }
    private void spomalBeh() {
        pomalyBeh = true;
        switchPanel("normalBehCard");
    }

    private void startSimulation() {
        isPaused.set(false);
        isStopped.set(false);
        replikacieCounter =1000;
        pokladneModel.setRowCount(0);
        odberModel.setRowCount(0);
        zakazniciModel.setRowCount(0);
        int pocetReplikacii = Integer.parseInt(pocetReplikField.getText());
        pocetPokladni = Integer.parseInt(pocetPokladField.getText());
        pocetObsluz = Integer.parseInt(pocetObsluzField.getText());
        Predajna predajna = new Predajna(pocetReplikacii,pocetObsluz, pocetPokladni);
        Thread simulatcia1 = new Thread(predajna::simuluj);
        predajna.registerDelegate(this);
        for (int i = 0; i < pocetPokladni; i++) {
            pokladneModel.addRow(new Object[]{i, predajna.getPokladne().getPokladne()[i], 0 ,0.0});
        }
        for (int i = 0; i < predajna.getObsluzneMiesta().getOnlineObsluzne().length; i++) {
            odberModel.addRow(new Object[]{i, "ONLINE", predajna.getObsluzneMiesta().getOnlineObsluzne()[i], 0.0});
        }
        for (int i = 0; i < predajna.getObsluzneMiesta().getNormalneObsluzne().length; i++) {
            odberModel.addRow(new Object[]{i+ predajna.getObsluzneMiesta().getOnlineObsluzne().length, "NORMALNE", predajna.getObsluzneMiesta().getNormalneObsluzne()[i], 0.0});
        }
//odstarovanie simulacie pre kazdu strategiu
        if (porovananieCheckbox.isSelected()) {
            GUIPorovnanie porovanie = new GUIPorovnanie(pocetObsluz, pocetReplikacii);
            porovanie.startSimulation();
        }
        simulatcia1.start();

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUIapp::new);
    }

    @Override
    public void refresh(SimJadro simJadro) {
        Predajna sim = (Predajna) simJadro;
        if (isStopped.get()) {
            sim.setStopRequested(true);

        }
        if (isPaused.get()) {
            sim.setPaused(true);
            pausedSim = sim;
        }
        if (sim.isSlowRequested() != pomalyBeh) {
            sim.setSlowRequested(pomalyBeh);
        }
        if (pomalyBeh) {
            if(!sim.getStavyOsob().isEmpty()) {
                zakazniciModel.addRow(new Object[]{sim.getStavyOsob().get(0), sim.getStavyOsob().get(1), sim.getStavyOsob().get(2)});
                sim.getStavyOsob().clear();
            }
            casLabel.setText((int)sim.getSimCas()/60 + ":" + (int)sim.getSimCas()%60 + ":" + (int)(sim.getSimCas()*60%60));
            pocetLudiPredAutomatom.setText(String.valueOf(sim.getOsobyQueue().size()));
            for (int i = 0; i < pocetPokladni; i++) {
                pokladneModel.setValueAt(sim.getPokladne().getPokladne()[i], i, 1);
                pokladneModel.setValueAt(sim.getPokladne().getRady()[i].size(), i, 2);
                pokladneModel.setValueAt(Math.round(sim.getPriemerVytazenostPokladni().get(i).getVytazenie(sim.getSimCas() - sim.getZaciatokCasu())*100) + "%", i ,3);
                //pridat vytazenie
            }
            for (int i = 0; i < sim.getObsluzneMiesta().getOnlineObsluzne().length; i++) {
                odberModel.setValueAt(sim.getObsluzneMiesta().getOnlineObsluzne()[i], i, 2);
                odberModel.setValueAt(Math.round(sim.getPriemerVytazenostObsluznychOnline().get(i).getVytazenie(sim.getSimCas() - sim.getZaciatokCasu()) * 100) + "%", i, 3);
            }
            for (int i = 0; i < sim.getObsluzneMiesta().getNormalneObsluzne().length; i++) {
                odberModel.setValueAt(sim.getObsluzneMiesta().getNormalneObsluzne()[i], i+sim.getObsluzneMiesta().getOnlineObsluzne().length, 2);
                odberModel.setValueAt(Math.round(sim.getPriemerVytazenostObsluznychOstatne().get(i).getVytazenie(sim.getSimCas() - sim.getZaciatokCasu()) * 100) + "%", i+sim.getObsluzneMiesta().getOnlineObsluzne().length, 3);

            }
            obsadenyAutomatLabel.setText(String.valueOf(sim.getAutomatIsEmpty()));
            vytazenostAutomatuLabel.setText(Math.round(sim.getPriemerVytazenieAutomatu().getVytazenie(sim.getSimCas() - sim.getZaciatokCasu())*100.0) + "%");
            pocetOnlineZakaznikovRad.setText(String.valueOf(sim.getObsluzneMiesta().getOnlineQueue().size()));
            pocetOstatnychZakaznikovRad.setText(String.valueOf(sim.getObsluzneMiesta().getOsobyQueue().size()));
            try {
                Thread.sleep(Math.round(rychlostSlider.getValue()) );
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            cisloReplikacieLabel.setText(String.valueOf(sim.getCisloReplikacie()));
            priemerZakaznikovLabel.setText(String.valueOf(Math.round(sim.getPriemerPocetLudiCelkovy().vypocitaj() * 1000.0) / 1000.0));
            double casVSyteme = Math.round(sim.getPriemerCasVObchodeCelkovy().vypocitaj() * 1000.0) / 1000.0;
            priemerCasVSystemeLabel.setText((int)casVSyteme%60 + ":" + (int)(casVSyteme*60%60));
            double casOdchodu = sim.getPriemerPoslednyOdchod().vypocitaj();
            priemerCasOdchodLabel.setText(String.valueOf((int)casOdchodu/60 + ":" + (int)casOdchodu%60 + ":" + (int)(casOdchodu*60%60)));
            intervalSpolahlivostiLabel.setText(String.valueOf((int)Math.floor(sim.getPriemerCasVObchodeCelkovy().getIntervalSpolahlivosti()[0])) + ":" + String.valueOf((int)(sim.getPriemerCasVObchodeCelkovy().getIntervalSpolahlivosti()[0]*60%60)) + ";" + String.valueOf((int)Math.floor(sim.getPriemerCasVObchodeCelkovy().getIntervalSpolahlivosti()[1])) + ":" + String.valueOf((int)(sim.getPriemerCasVObchodeCelkovy().getIntervalSpolahlivosti()[1]*60%60)));
            double casCakaniaVRade = sim.getPriemerCakanieVRadePredAutomatomCalkovy().vypocitaj();
            casCakaniaPredAutomatomLabel.setText((int)casCakaniaVRade + ":" + (int)(casCakaniaVRade*60%60));
            priemerDlzkaFrontuPredAutomatomLabel.setText(String.valueOf(Math.round(sim.getPriemerDlzkaRaduCelkovy().vypocitaj() * 1000.0) / 1000.0));
            vytazeneiAutomatuLabel.setText(Math.round(sim.getPriemerVytazenieAutomatuCelkove().vypocitaj()*100* 1000.0) / 1000.0 + "%");
            ArrayList<String> vytazenieObsluznych = new ArrayList<>();

            for (int i = 0; i < sim.getObsluzneMiesta().getOnlineObsluzne().length; i++) {
                vytazenieObsluznych.add(Math.round(sim.getPriemerVytazenostObsluznychOnlineCelkove().get(i).vypocitaj()*100) + "%");
            }

            for (int i = 0; i < sim.getObsluzneMiesta().getNormalneObsluzne().length; i++) {
                vytazenieObsluznych.add(Math.round(sim.getPriemerVytazenostObsluznychOstatneCelkove().get(i).vypocitaj()*100) + "%");
            }
            vytazenieObsluznychLabel.setText(vytazenieObsluznych.toString());
            ArrayList<String> vytazeniePokladni = new ArrayList<>();
            ArrayList<String> dlzkyRadovPriPokladniach = new ArrayList<>();
            for (int i = 0; i < pocetPokladni; i++) {
                vytazeniePokladni.add(Math.round(sim.getPriemerVytazenostPokladniCelkove().get(i).vypocitaj()*100) + "%");
                dlzkyRadovPriPokladniach.add(String.valueOf(Math.round(sim.getPriemerDlzkaRadovPriPokladniachCelkove().get(i).vypocitaj()*1000.0)/1000.0));
            }
            vytazeniePokladniLabel.setText(vytazeniePokladni.toString());
            dlzkyRadovPriPokladniachLabel.setText(dlzkyRadovPriPokladniach.toString());
            priemerObsluzenychLabel.setText(String.valueOf(Math.round(sim.getPocetObsluzenychZakaznikovCelkove().vypocitaj()*1000.0)/1000.0));
            dlzkyRadovPredObsluznymiLabel.setText("Normálne: " + Math.round(sim.getPriemerDlzkaRaduPredObsluzNormalCelkove().vypocitaj()*1000.0)/1000.0 + ", Online: " + Math.round(sim.getPriemerDlzkaRaduPredObsluzOnlineCelkove().vypocitaj()*1000.0)/1000.0);
        }

    }
    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) prepinaciPanel.getLayout();
        cardLayout.show(prepinaciPanel, panelName);
    }

    private void unpause() {
        pausedSim.setPaused(false);
        isPaused.set(false);
    }
}
