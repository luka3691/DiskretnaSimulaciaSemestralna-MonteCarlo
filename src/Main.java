import MonteCarlo.Rozdelenia.Exponencialne;
import MonteCarlo.Rozdelenia.SpojiteEmpiricke;
import MonteCarlo.Rozdelenia.Triangularne;
import MonteCarlo.Stanok;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Stanok stanok = new Stanok(10000, 5, 5);
        stanok.simuluj();
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        /*
        Stanok stanok = new Stanok(100000);
        stanok.simuluj();
        */
        /*
        List<Double> pravdepodobnosti = Arrays.asList( 0.1, 0.35, 0.2, 0.15, 0.15, 0.05);
        List<Double[]> hranice = Arrays.asList(new Double[]{0.1, 0.3}, new Double[]{0.3, 0.8}, new Double[]{0.8, 1.2}, new Double[]{1.2, 2.5}, new Double[]{2.5, 3.8}, new Double[]{3.8, 4.8});

        Triangularne rozdelenie = new Triangularne(60, 480, 120);
        try {
            FileWriter myWriter = new FileWriter("filename.txt");
            for (int i = 0; i < 100000; i++) {
                myWriter.write(String.valueOf(rozdelenie.sample()) + '\n');

            }
            myWriter.close();

        } catch (IOException e) {
            System.out.println("Error.");
            e.printStackTrace();
        }

         */
    }
}