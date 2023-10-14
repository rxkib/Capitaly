package capitaly;

import java.io.*;
import java.util.*;

/**
 * The main class that drives the Capitaly game logic.
 *
 * @author rakib
 */

public class Capitaly {
    
    
    /**
     * The entry point for the program.
     *
     * @param args Command line arguments.
     * @throws IOException if an error occurs while reading files.
     */

    public static void main(String[] args) throws IOException {
        runTestCases();
        System.out.println("Running main game logic with Input.txt and Dice.txt");
        runGame("Input.txt", "Dice.txt");
    }
    
    /**
     * Runs a series of test cases.
     */

    public static void runTestCases() {
        System.out.println("Running test cases...");
        runGame("ValidInput.txt", "ValidDice.txt");
        runGame("InvalidInput.txt", "ValidDice.txt");
        runGame("EmptyInput.txt", "ValidDice.txt");
        runGame("Input.txt", "InvalidDice.txt");
        runGame("Input.txt", "EmptyDice.txt");
        System.out.println("Completed test cases.");
    }
    
    /**
     * Executes the main game logic based on input and dice files.
     *
     * @param inputFileName The name of the input file containing the game setup.
     * @param diceFileName The name of the dice file containing the dice rolls.
     */
    
    public static void runGame(String inputFileName, String diceFileName) {
        
        // Creating lists to hold fields, players, and dice rolls
        List<Field> fields = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        List<Integer> diceRolls = new ArrayList<>();
        
        // Reading field and player data from input file
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            int numFields = Integer.parseInt(reader.readLine());
            for (int i = 0; i < numFields; i++) {
                String line = reader.readLine();
                String[] parts = line.split(" ");
                if (parts.length < 1) {
                    throw new IllegalArgumentException("Missing arguments for field: " + line);
                }

                if ("Property".equals(parts[0])) {
                    fields.add(new Property());
                } else if ("Service".equals(parts[0])) {
                    fields.add(new Service(Integer.parseInt(parts[1])));
                } else if ("Lucky".equals(parts[0])) {
                    fields.add(new Lucky(Integer.parseInt(parts[1])));
                } else {
                    throw new IllegalArgumentException("Invalid field type: " + parts[0]);
                }
            }

            int numPlayers = Integer.parseInt(reader.readLine());
            for (int i = 0; i < numPlayers; i++) {
                String line = reader.readLine();
                String[] parts = line.split(" ");
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Missing arguments for player: " + line);
                }

                if ("Careful".equals(parts[1])) {
                    players.add(new CarefulPlayer(parts[0]));
                } else if ("Tactical".equals(parts[1])) {
                    players.add(new TacticalPlayer(parts[0]));
                } else if ("Greedy".equals(parts[1])) {
                    players.add(new GreedyPlayer(parts[0]));
                } else {
                    throw new IllegalArgumentException("Invalid player type: " + parts[1]);
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading Input file: " + e.getMessage());
            return;
        }
        
        // Reading dice rolls from dice file
        try (BufferedReader reader = new BufferedReader(new FileReader(diceFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int roll = Integer.parseInt(line);
                if (roll < 1 || roll > 6) {
                    throw new IllegalArgumentException("Invalid dice roll: " + roll);
                }
                diceRolls.add(roll);
            }
        } catch (Exception e) {
            System.out.println("Error reading Dice file: " + e.getMessage());
            return;
        }
        // Check for empty lists
        if(fields.isEmpty()) {
            System.out.println("No fields found in input file. Exiting...");
            return;
        }

        if(players.isEmpty()) {
            System.out.println("No players found in input file. Exiting...");
            return;
        }

        if(diceRolls.isEmpty()) {
            System.out.println("No dice rolls found in dice file. Exiting...");
            return;
        }
        
        // Initializing game state
        List<Player> losers = new ArrayList<>();
        Map<Player, Integer> positions = new HashMap<>();
        for (Player player : players) {
            positions.put(player, 0);
        }

        // Main game loop
        int currentPlayerIndex = 0;
        int diceIndex = 0;

        while (losers.size() < 2) {
            Player currentPlayer = players.get(currentPlayerIndex);
            if (!losers.contains(currentPlayer)) {
                int roll = diceRolls.get(diceIndex % diceRolls.size()); 
                //The % diceRolls.size() ensures it cycles back to the first roll when reaching the end of the list.
                int newPosition = (positions.get(currentPlayer) + roll) % fields.size();
                //The % fields.size() makes sure that if the player passes the last field, they loop back to the start.
                positions.put(currentPlayer, newPosition);
                Field currentField = fields.get(newPosition);

                currentField.performAction(currentPlayer);

                if (currentPlayer.money < 0 && !losers.contains(currentPlayer)) {
                    losers.add(currentPlayer);
                    for (Field field : fields) {
                        if (field instanceof Property) {
                            Property property = (Property) field;
                            if (property.owner == currentPlayer) {
                                property.owner = null;
                                property.hasHouse = false;
                            }
                        }
                    }
                }
            }
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            diceIndex = (diceIndex + 1) % diceRolls.size();
        }

        System.out.println("Second player to lose is: " + losers.get(1).name);
    }
}
