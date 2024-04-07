import MonteCarlo.ISimDelegate;
import MonteCarlo.SimJadro;
import MonteCarlo.Stanok;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUIapp implements ISimDelegate {
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
    int pocetPokladni;
    int pocetObsluz ;

    private boolean pomalyBeh;

    public GUIapp() {
        JFrame frame = new JFrame("JTable Demo");
        tableOdber.setModel(odberModel);
        tablePokladne.setModel(pokladneModel);
        pocetObsluzField.setText("13");
        pocetPokladField.setText("4");
        pocetReplikField.setText("25000");
        pauzaButton.setEnabled(false);
        stopButton.setEnabled(false);
        pomalyBeh = false;

        štartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer.parseInt(pocetObsluzField.getText());
                    Integer.parseInt(pocetPokladField.getText());
                    Integer.parseInt(pocetReplikField.getText());
                    startSimulation();
                    štartButton.setEnabled(false);
                }
                catch (NumberFormatException i) {
                    //Nebolo zadane cislo
                }
            }
        });
        pauzaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer.parseInt(pocetObsluzField.getText());
                    Integer.parseInt(pocetPokladField.getText());
                    Integer.parseInt(pocetReplikField.getText());
                    startSimulation();

                }
                catch (NumberFormatException i) {
                    //Nebolo zadane cislo
                }
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer.parseInt(pocetObsluzField.getText());
                    Integer.parseInt(pocetPokladField.getText());
                    Integer.parseInt(pocetReplikField.getText());
                    startSimulation();

                }
                catch (NumberFormatException i) {
                    //Nebolo zadane cislo
                }
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
        int pocetReplikacii = Integer.parseInt(pocetReplikField.getText());
        pocetPokladni = Integer.parseInt(pocetPokladField.getText());
        pocetObsluz = Integer.parseInt(pocetObsluzField.getText());
        Stanok stanok = new Stanok(pocetReplikacii,pocetObsluz, pocetPokladni);
        Thread simulatcia1 = new Thread(stanok::simuluj);
        stanok.registerDelegate(this);
        for (int i = 0; i < pocetPokladni; i++) {
            pokladneModel.addRow(new Object[]{i, stanok.getPokladne().getPokladne()[i], 0 ,0.0});
        }
        for (int i = 0; i < stanok.getObsluzneMiesta().getOnlineObsluzne().length; i++) {
            odberModel.addRow(new Object[]{i, "ONLINE", stanok.getObsluzneMiesta().getOnlineObsluzne()[i], 0.0});
        }
        for (int i = 0; i < stanok.getObsluzneMiesta().getNormalneObsluzne().length; i++) {
            odberModel.addRow(new Object[]{i+stanok.getObsluzneMiesta().getOnlineObsluzne().length, "NORMALNE",stanok.getObsluzneMiesta().getNormalneObsluzne()[i], 0.0});
        }
//odstarovanie simulacie pre kazdu strategiu

        simulatcia1.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUIapp::new);
    }

    @Override
    public void refresh(SimJadro simJadro) {
        Stanok sim = (Stanok) simJadro;
        if (sim.isSlowRequested() != pomalyBeh) {
            sim.setSlowRequested(pomalyBeh);
        }
        if (pomalyBeh) {
            casLabel.setText((int)sim.getSimCas()/60 + ":" + (int)sim.getSimCas()%60);
            pocetLudiPredAutomatom.setText(String.valueOf(sim.getOsobyQueue().size()));
            for (int i = 0; i < pocetPokladni; i++) {
                pokladneModel.setValueAt(sim.getPokladne().getPokladne()[i], i, 1);
                pokladneModel.setValueAt(sim.getPokladne().getRady()[i].size(), i, 2);
                //pridat vytazenie
            }
            for (int i = 0; i < sim.getObsluzneMiesta().getOnlineObsluzne().length; i++) {
                odberModel.setValueAt(sim.getObsluzneMiesta().getOnlineObsluzne()[i], i, 2);
            }
            for (int i = 0; i < sim.getObsluzneMiesta().getNormalneObsluzne().length; i++) {
                odberModel.setValueAt(sim.getObsluzneMiesta().getNormalneObsluzne()[i], i+sim.getObsluzneMiesta().getOnlineObsluzne().length, 2);
            }
            obsadenyAutomatLabel.setText(String.valueOf(sim.getAutomatIsEmpty()));
            pocetOnlineZakaznikovRad.setText(String.valueOf(sim.getObsluzneMiesta().getOnlineQueue().size()));
            pocetOstatnychZakaznikovRad.setText(String.valueOf(sim.getObsluzneMiesta().getOsobyQueue().size()));
            try {
                Thread.sleep(rychlostSlider.getValue() ); // Sleep for 5 milliseconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve interrupt status
                // Handle the InterruptedException if required
                // For example, log it or rethrow it
            }
        }

    }
    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) prepinaciPanel.getLayout();
        cardLayout.show(prepinaciPanel, panelName);
    }
}
