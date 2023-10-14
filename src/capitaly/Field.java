/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package capitaly;

/**
 * The abstract class representing a generic field on the board.
 *
 * @author rakib
 */

abstract class Field {
    
    /**
     * Abstract method that defines the action to be performed when a player lands on this field.
     *
     * @param player The player who landed on this field.
     */
    
    abstract void performAction(Player player);
}

/**
 * Class representing a property field in the game.
 */

class Property extends Field {
    int cost = 1000;
    int houseCost = 4000;
    Player owner;
    boolean hasHouse;
    
    /**
     * Implementation of performAction for a property field.
     *
     * @param player The player who landed on this field.
     */

    @Override
    void performAction(Player player) {
        if (owner == null) {
            player.decideToBuy(this);
        } else if (owner != player) {
            int rent = hasHouse ? 2000 : 500;
            player.money -= rent;
            owner.money += rent;
        } else if (!hasHouse) {
            player.decideToBuy(this);
        }
    }
}

/**
 * Class representing a service field in the game.
 */

class Service extends Field {
    int cost;
    
    /**
     * Constructor to initialize a new service field.
     *
     * @param cost The cost of the service.
     */

    Service(int cost) {
        this.cost = cost;
    }
    
    /**
     * Implementation of performAction for a service field.
     *
     * @param player The player who landed on this field.
     */

    @Override
    void performAction(Player player) {
        player.money -= cost;
    }
}

/**
 * Class representing a lucky field in the game.
 */

class Lucky extends Field {
    int amount;
    
    /**
     * Constructor to initialize a new lucky field.
     *
     * @param amount The amount to be credited to the player.
     */

    Lucky(int amount) {
        this.amount = amount;
    }
    
    /**
     * Implementation of performAction for a lucky field.
     *
     * @param player The player who landed on this field.
     */

    @Override
    void performAction(Player player) {
        player.money += amount;
    }
}