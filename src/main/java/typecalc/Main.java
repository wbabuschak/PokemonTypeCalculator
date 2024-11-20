package typecalc;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        File inputfile = new File("src/main/resources/types.txt");
        ArrayList<Type> types = new ArrayList<>();

        double RESSCALAR = 1.0;
        double IMMUNSCALAR = 1.1;
        double WEAKSCALAR = 1.0;

        int i = 1;
        Type targetType = null;
        String[] instructions = {
        "'help' retuns this list",
        "'quit' closes this program",
        "'count' prints the number of types scanned",
        "'list' prints the names of types scanned",
        "'chart' prints the calculated type chart",
        "'rank1' ranks the types based on first-order matchups",
        };
        

        try (Scanner filescanner = new Scanner(inputfile)) {
            while (filescanner.hasNextLine()) {
                String line = filescanner.nextLine().trim();
                if (line.equals("")) {
                    i++;
                    continue;
                }

                String[] names = line.split(" ");
                
                for (int k = 0; k < names.length; k++) {
                    boolean typeExists = false;
                    for (int l = 0; l < types.size(); l++) {
                        if (types.get(l).displayName.equals(names[k])) {
                            typeExists = true;
                            break;
                        }
                    }
                    if (!typeExists) {
                        types.add(new Type(names[k]));
                    }
                }

                if (i % 4 == 1) {
                    targetType = null;
                    String targetName = names[0];
                    
                    for (int k = 0; k < types.size(); k++) {
                        if (types.get(k).displayName.equals(targetName)) {
                            targetType = types.get(k);
                            break;
                        }
                    }
                }

                if (targetType != null) {

                    if (i % 4 == 2) {
                        if (!line.equals("")) {
                            for (int j = 0; j < names.length; j++) {
                                for (int k = 0; k < types.size(); k++) {
                                    if (types.get(k).displayName.equals(names[j])) {
                                        targetType.weak.add(types.get(k));
                                    }
                                }
                            }
                        }

                    } if (i % 4 == 3) {
                        if (!line.equals("")) {
                            for (int j = 0; j < names.length; j++) {
                                for (int k = 0; k < types.size(); k++) {
                                    if (types.get(k).displayName.equals(names[j])) {
                                        targetType.resist.add(types.get(k));
                                    }
                                }
                            }
                        }

                    } if (i % 4 == 0) {
                        if (!line.equals("")) {
                            for (int j = 0; j < names.length; j++) {
                                for (int k = 0; k < types.size(); k++) {
                                    if (types.get(k).displayName.equals(names[j])) {
                                        targetType.immun.add(types.get(k));
                                    }
                                }
                            }
                        }
                    }
                }

                i++;
            }
            System.out.println("Program intialized! Type 'help' for commands");
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
           
        while(true){
            String input = scanner.next();
            if (input.equals("help")){
                for (int j = 0; j < instructions.length; j++){
                    System.out.println(instructions[j]);
                }
            
            } else if (input.equals("quit")){
                break;
            } else if (input.equals("count")){
                System.out.println("Program read " + types.size() + " types.");
            } else if (input.equals("list")){
                System.out.print("Types: ");
                for (int j = 0; j < types.size()-1; j++){
                    System.out.print(types.get(j).displayName + ", ");
                    
                }
                System.out.println(types.get(types.size()-1).displayName);
            } else if (input.equals("chart")){
                String[][] chart = new String[types.size()+1][types.size()+1];
                chart[0][0] = "";

                // Set labels
                for (int j = 1; j < types.size()+1; j++){
                    chart[0][j] = types.get(j-1).displayName;
                    chart[j][0] = types.get(j-1).displayName; 
                }
                
                // Set table
                for (int j = 0; j < types.size(); j++){
                    for (int k = 0; k < types.size(); k++){
                        chart[j+1][k+1] = "x1.0";
                        for (int l = 0; l < types.get(j).weak.size(); l++){
                            if (types.get(j).weak.get(l).displayName.equals(types.get(k).displayName)){
                                chart[j+1][k+1] = "x{2.0}";
                                break;
                            }
                        }
                        for (int l = 0; l < types.get(j).resist.size(); l++){
                            if (types.get(j).resist.get(l).displayName.equals(types.get(k).displayName)){
                                chart[j+1][k+1] = "x[0.5]";
                                break;
                            }
                        }
                        for (int l = 0; l < types.get(j).immun.size(); l++){
                            if (types.get(j).immun.get(l).displayName.equals(types.get(k).displayName)){
                                chart[j+1][k+1] = "x(0.0)";
                                break;
                            }
                        }
                    }
                }

                // Print chart
                for (int j = 0; j < chart.length; j++){
                    for (int k = 0; k < chart[0].length; k++){
                        System.out.printf("%8s ",chart[j][k]);
                    }
                    System.out.println();
                }
            } else if (input.equals("rank1") || input.equals("rank")){

                System.out.println("Specify 'def'ensive, 'off'ensive, 'tot'al, or 'all'\n");
                input = scanner.next();
                
            
                double[][] ranks = new double[types.size()][3];
                for (int j = 0; j < types.size(); j++){
                    ranks[j][2] = j;
                    // # resists * RESSCALAR + # immunities * IMMUNSCALAR - weaks * WEAKSCALAR
                    if (input.equals("def") || input.equals("tot") || input.equals("all")){
                        ranks[j][0] = types.get(j).resist.size() * RESSCALAR +  types.get(j).immun.size() * IMMUNSCALAR - types.get(j).weak.size();
                    }
                    if (input.equals("off") || input.equals("tot") || input.equals("all")){
                        for (int k = 0; k < types.size(); k++){
                            for (int l = 0; l < types.get(k).weak.size(); l++){
                                if (types.get(k).weak.get(l).displayName.equals(types.get(j).displayName)){
                                    ranks[j][1] += WEAKSCALAR;
                                }
                            }
                            for (int l = 0; l < types.get(k).resist.size(); l++){
                                if (types.get(k).resist.get(l).displayName.equals(types.get(j).displayName)){
                                    ranks[j][1] -= RESSCALAR;
                                }
                            }
                            for (int l = 0; l < types.get(k).immun.size(); l++){
                                if (types.get(k).immun.get(l).displayName.equals(types.get(j).displayName)){
                                    ranks[j][1] -= IMMUNSCALAR;
                                }
                            }
                        }
                    }
                    
                }
                for (int j = 0; j < ranks.length - 1; j++){
                    for (int k = 0; k < ranks.length - j - 1; k++){
                        if (ranks[k][0] + ranks[k][1] > ranks[k + 1][ 0] + ranks[k + 1][1]){
                            double[] temp = ranks[k];
                            ranks[k] = ranks[k + 1];
                            ranks[k + 1] = temp;
                        }
                    }

                }
                for (int j = types.size()-1; j > 0; j--){
                    if (input.equals("def")){
                        System.out.printf("%s: %.1f\n",types.get((int) ranks[j][2]).displayName, ranks[j][0]);
                    }
                    if (input.equals("off")){
                        System.out.printf("%s: %.1f\n",types.get((int) ranks[j][2]).displayName, ranks[j][1]);
                    }
                    if (input.equals("tot")){
                        System.out.printf("%s: %.1f\n",types.get((int) ranks[j][2]).displayName, ranks[j][0] + ranks[j][1]);
                    }
                    if (input.equals("all")){
                        System.out.println((types.size() - j) + ": " + types.get((int) ranks[j][2]).displayName);
                        System.out.printf("\tWith a defensive score of: %.1f ...\n", ranks[j][0]);
                        System.out.printf("\tWith an offensive score of: %.1f ...\n", ranks[j][1]);
                        System.out.printf("\tWith a total score of: %.1f!\n", ranks[j][0] + ranks[j][1]);
                    }
                }
            }
            
        }
        scanner.close();
    }
}