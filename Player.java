/* @formatter:off
 *
 * © David M Rosenberg
 *
 * Topic: List App ~ Card Game
 * 
 * Usage restrictions:
 * 
 * You may use this code for exploration, experimentation, and furthering your
 * learning for this course. You may not use this code for any other
 * assignments, in my course or elsewhere, without explicit permission, in
 * advance, from myself (and the instructor of any other course).
 * 
 * Further, you may not post (including in a public repository such as on github)
 * nor otherwise share this code with anyone other than current students in my 
 * sections of this course.
 * 
 * Violation of these usage restrictions will be considered a violation of
 * Wentworth Institute of Technology's Academic Honesty Policy.  Unauthorized posting
 * or use of this code may also be considered copyright infringement and may subject
 * the poster and/or the owners/operators of said websites to legal and/or financial
 * penalties.  My students are permitted to store this code in a private repository
 * or other private cloud-based storage.
 *
 * Do not modify or remove this notice.
 *
 * @formatter:on
 */


package edu.wit.scds.ds.lists.app.card_game.your_game.game ;

import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank.JOKER ;

import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Card ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Card.CompareOn ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Suit ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Deck ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Pile ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.support.NoCardsException ;
import edu.wit.scds.ds.lists.app.card_game.your_game.pile.Hand ;
import edu.wit.scds.ds.lists.app.card_game.your_game.pile.Meld ;
import edu.wit.scds.ds.lists.app.card_game.your_game.pile.Stock ;
import edu.wit.scds.ds.lists.app.card_game.your_game.pile.DiscardPile;


import java.io.File ;
import java.io.FileNotFoundException ;
import java.util.ArrayList ;
import java.util.List ;
import java.util.Random ;
import java.util.Scanner ;

/**
 * Representation of a player
 * <p>
 * NOTE: You will modify this code
 *
 * @author Dave Rosenberg
 *
 * @version 1.0 2021-12-08 Initial implementation
 * @version 2.0 2025-06-28 track changes to other classes
 * @version 2.1 2025-11-04 track changes to other classes
 * 
 * @author Jonathan Wu
 * 
 * @version 3.0 2025-11-03 modifications for your game
 */
public final class Player
    {

    /*
     * data fields
     */


    /** the cards that are in-play */
    private final Hand hand ;

    /** groups of cards collected during play */
   

    /** player's name */
    public final String name ;


    /*
     * constructor(s)
     */


    /**
     * initialize a player
     *
     * @param playerName
     *     the player's name
     */
    public Player( final String playerName )
        {

        this.name = playerName ;

        this.hand = new Hand() ;

        

        }   // end constructor


    /*
     * public methods
     */
    public Hand getHand() {
        return this.hand;
    }


    /**
     * Add a dealt card to our hand
     *
     * @param dealt
     *     the card we're dealt
     */
    public void dealtACard( final Card dealt )
        {

        this.hand.addToBottom( dealt ) ;
        this.hand.sort() ;

        }  // end dealtACard()


   

    /**
     * Remove an unspecified card from our hand
     *
     * @return any card currently in the hand
     *
     * @throws NoCardsException
     *     if the hand is empty
     */
    public Card playACard() throws NoCardsException
        {

        return this.hand.removeCardAt( new Random().nextInt( 0,
                                                             this.hand.cardCount() ) ) ;

        }  // end playACard()


    /**
     * Remove a specified card from our hand
     *
     * @param cardToThrow
     *     the card to remove
     *
     * @return the specified card or null if not in the hand
     *
     * @since 2.0
     */
    public Card playACard( final Card cardToThrow )
        {

        return this.hand.removeCard( cardToThrow ) ;

        }  // end playACard()


    /**
     * Remove a specified card from our hand
     *
     * @param rank
     *     the rank of the card to remove
     * @param suit
     *     the suit of the card to remove
     *
     * @return the specified card or null if not in the hand
     */
    public Card playACard( final Rank rank,
                           final Suit suit )
        {

        return playACard( new Card( rank, suit ) ) ;

        }  // end playACard()


    /**
     * text describing the contents of the player's hand
     * <p>
     * note that cards' orientation is unchanged
     *
     * @return a string containing the cards in the player's hand
     */
    public String revealHand()
        {

        if ( this.hand.cardCount() == 0 )
            {
            return "empty" ;
            }

        return this.hand.revealAll().toString() ;

        }   // end revealHand()


  

    /**
     * Remove all cards from our hand and our collected cards
     *
     * @return a pile with all the cards we have - order and orientation may be inconsistent
     *
     * @since 2.0
     */
    public Pile turnInAllCards()
        {

        // local temporary class (pile) to hold our cards
       final Pile allCards = new Pile() {};
       
        // we may have cards in our hand 

        allCards.moveCardsToBottom( this.hand );
        this.hand.clear();

      

        return allCards ;

        }  // end turnInAllCards()


   

    /*
     * utility methods
     */



    @Override
    public String toString() {
        return String.format("%nPlayer: %s%n  Hand (%d cards): %s%n",
                             this.name,
                             this.hand.cardCount(),
                             this.hand.cardCount() == 0 ? "empty" : this.revealHand());
    }
    
    
    
    
    /**
     * Optional helper: attempt to draw until a matching card is found.
     */
    public boolean takeTurn(final Stock stock, final DiscardPile discard) {
        while (!stock.isEmpty()) {
            try {
                final Card flipped = stock.drawTopCard();
                // find a match in hand (by rank or suit)
                Card match = null;
                for (final Object o : this.hand) {
                    // hand is iterable of CardBase; cast to Card
                    final Card c = (Card) o;
                    if (c.rank == flipped.rank || c.suit == flipped.suit) {
                        match = c;
                        break;
                    }
                }
                if (match != null) {
                    this.hand.removeCard(match);
                    discard.addCard(flipped.reveal());
                    System.out.printf("%s matched %s and discarded the pair!%n", name, flipped);
                    return true;
                } else {
                    this.hand.addToTop(flipped);
                    System.out.printf("%s drew %s (no match)%n", name, flipped);
                }
            } catch (final NoCardsException e) {
                break;
            }
        }
        System.out.printf("%s: Stock is empty! Turn ends.%n", name);
        return false;
    }


    /*
     * testing/debugging
     */


    /**
     * (optional) test driver
     *
     * @param args
     *     -unused-
     */
    public static void main( final String[] args )
        {
        }   // end main()


    public String getName() {
        return this.name;
    }

    } 