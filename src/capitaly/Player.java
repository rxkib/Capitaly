/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package capitaly;

/**
 * The abstract class representing a generic player in the game.
 * 
 * @author rakib
 */

abstract class Player {
    String name;
    int money = 10000;
    boolean skipNextBuy = false;
    
    /**
     * Constructor to initialize a new player.
     *
     * @param name The name of the player.
     */

    Player(String name) {
        this.name = name;
    }
    
    /**
     * Abstract method that decides whether or not to buy a property.
     *
     * @param property The property the player has landed on.
     */

    abstract void decideToBuy(Property property);
}

/**
 * Class representing a greedy player in the game.
 */
class GreedyPlayer extends Player {
    
    /**
     * Constructor to initialize a new greedy player.
     *
     * @param name The name of the player.
     */
    GreedyPlayer(String name) {
        super(name);
    }
    
    /**
     * Implementation of decideToBuy for a greedy player.
     *
     * @param property The property the player has landed on.
     */

    @Override
    void decideToBuy(Property property) {
        if (property.owner == null && money >= property.cost) {
            money -= property.cost;
            property.owner = this;
        } else if (property.owner == this && !property.hasHouse && money >= property.houseCost) {
            money -= property.houseCost;
            property.hasHouse = true;
        }
    }
}

/**
 * Class representing a careful player in the game.
 */

class CarefulPlayer extends Player {
    /**
     * Constructor to initialize a new careful player.
     *
     * @param name The name of the player.
     */
    CarefulPlayer(String name) {
        super(name);
    }
    
    /**
     * Implementation of decideToBuy for a careful player.
     *
     * @param property The property the player has landed on.
     */

    @Override
    void decideToBuy(Property property) {
        int maxSpend = money / 2;
        if (property.owner == null && maxSpend >= property.cost) {
            money -= property.cost;
            property.owner = this;
        } else if (property.owner == this && !property.hasHouse && maxSpend >= property.houseCost) {
            money -= property.houseCost;
            property.hasHouse = true;
        }
    }
}

/**
 * Class representing a tactical player in the game.
 */

class TacticalPlayer extends Player {
    
    /**
     * Constructor to initialize a new tactical player.
     *
     * @param name The name of the player.
     */
    
    TacticalPlayer(String name) {
        super(name);
    }
    
    /**
     * Implementation of decideToBuy for a tactical player.
     *
     * @param property The property the player has landed on.
     */

    @Override
    void decideToBuy(Property property) {
        if (skipNextBuy) {
            skipNextBuy = false;
            return;
        }

        if (property.owner == null && money >= property.cost) {
            money -= property.cost;
            property.owner = this;
        } else if (property.owner == this && !property.hasHouse && money >= property.houseCost) {
            money -= property.houseCost;
            property.hasHouse = true;
        }

        skipNextBuy = true;
    }
}
