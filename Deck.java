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


package edu.wit.scds.ds.lists.app.card_game.standard_cards.pile ;

import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Orientation.FACE_DOWN ;

import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Card ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Suit ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.card.CardBase ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.support.Orientation ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.support.Persistence ;


/**
 * Representation of a standard deck of cards containing
 * <ul>
 * <li>4 suits: Clubs, Diamonds, Hearts, Spades
 * <li>13 ranks: Ace, 2..9, Jack, Queen, King
 * <li>2 Jokers
 * </ul>
 * <p>
 * this is the source of all cards available to the game
 *
 * @author Dave Rosenberg
 *
 * @version 1.0 2025-06-26 Initial implementation (extracted from {@code Deck})
 * @version 1.1 2025-07-13 make the number of playing cards per deck available
 * @version 1.2 2025-11-19 add support for a template collection of
 *     {@code Card}s
 */
public final class Deck extends Pile
    {

    /*
     * utility constants
     */
    /** by default, cards added to this deck will be turned face down */
    protected final static Orientation DEFAULT_CARD_ORIENTATION = FACE_DOWN ;

    /** number of Jokers contained in each deck */
    private final static int DEFAULT_JOKER_COUNT = 2 ;
    
    /** number of playing cards in a deck */
    public final static int NUMBER_OF_PLAYING_CARDS_PER_DECK = 4 * // suits
                                                               13 ; // ranks

    
    /*
     * static fields
     */
    
    /** template deck contains all playing cards and the default number of jokers */
    private final static Deck templateCards ;
    static
        {

        // instantiate a template deck
        templateCards = new Deck( Persistence.TEMPLATE );
        
        }   // end static initializer
    
    
    /*
     * data fields
     */
    // none


    /*
     * constructors
     */


    /**
     * Initialize a deck of cards with default number of Jokers and default card orientation - the
     * cards are in the order as specified by {@link #createPlayingCards()}
     */
    public Deck()
        {

        super() ;
        
        populateDeck() ;

        }   // end no-arg constructor


    /**
     * Initialize a deck of cards including a specified number of Jokers and default card
     * orientation<br>
     * the cards are in sorted order
     *
     * @param numberOfJokers
     *     the desired number of jokers to add to the deck
     */
    public Deck( final int numberOfJokers )
        {

        // instantiate the deck with default card orientation and specified number of jokers
        this( DEFAULT_CARD_ORIENTATION, numberOfJokers ) ;

        }  // end 1-arg constructor w/ # of jokers


    /**
     * Initialize a deck of cards including a default number of Jokers and specified card
     * orientation<br>
     * the cards are in sorted order
     *
     * @param initialOrientation
     *     indicate face up or face down
     */
    public Deck( final Orientation initialOrientation )
        {

        // instantiate the deck with specified card orientation and default number of jokers
        this( initialOrientation, DEFAULT_JOKER_COUNT ) ;

        }  // end 1-arg constructor w/ card orientation


    /**
     * Initialize a deck of cards including a specified number of Jokers and card orientation<br>
     * the cards are in sorted order
     *
     * @param initialOrientation
     *     indicate face up or face down
     * @param numberOfJokers
     *     the desired number of jokers to add to the deck
     */
    public Deck( final Orientation initialOrientation,
                 final int numberOfJokers )
        {

        // initialize the pile
        super() ;

        // populate it as a deck
        populateDeck( initialOrientation, numberOfJokers, Persistence.PERMANENT ) ;

        }  // end full/2-arg constructor
    

    /**
     * Initialize a deck of cards with specified persistence, default number of
     * Jokers, and default card orientation<br>
     * the cards are in the order as specified by {@link #createPlayingCards()}
     * <p>
     * This constructor provides a reliable mechanism to instantiate the template
     * deck
     * 
     * @param persistence
     *     usually {@code Persistence.TEMPLATE}
     */
    private Deck( final Persistence persistence )
        {

        // initialize the pile
        super() ;

        // populate it as a deck
        populateDeck( DEFAULT_CARD_ORIENTATION, DEFAULT_JOKER_COUNT, persistence ) ;

        }   // end private template constructor


    /*
     * public methods
     */
    // none


    /*
     * private utility methods
     */


    /**
     * Add the specified number of Joker cards to the deck
     *
     * @param numberOfJokers
     *     the desired number of Jokers to add to the deck
     */
    private void createJokers( int numberOfJokers )
        {

        while ( numberOfJokers-- > 0 )
            {
            super.cards.add( new Card( Rank.JOKER ) ) ;
            }

        }  // end createJokers()


    /**
     * Instantiate all the playing cards in a new deck
     * <ul>
     * <li>cards are generated by rank within suit
     * <li>jokers are excluded
     * </ul>
     */
    private void createPlayingCards()
        {

        // generate all the cards in the deck
        for ( final Suit suit : Suit.values() )
            {

            // skip placeholder suit
            if ( suit == Suit.NA )
                {
                continue ;
                }

            for ( final Rank rank : Rank.values() )
                {

                // skip non-playing card(s) - Joker
                if ( rank == Rank.JOKER )
                    {
                    continue ;
                    }

                // build a card and save it
                super.cards.add( new Card( rank, suit ) ) ;
                }   // end inner for

            }   // end outer for

        }  // end createPlayingCards()

    
    /**
     * build a deck of cards using the template cards
     */
    private void populateDeck()
        {

        for ( CardBase aCardBase : Deck.templateCards )
            {
            if ( aCardBase instanceof Card aCard )
                {
                final Card newCard = new Card( aCard, Persistence.PERMANENT ) ;
                this.addToBottom( newCard ) ;
                }
            }
        
        }   // end no-arg cloning populate


    /**
     * Initialize a deck of cards including a specified number of Jokers and card orientation<br>
     * the cards are in sorted order
     *
     * @param initialOrientation
     *     indicate face up or face down
     * @param numberOfJokers
     *     the desired number of jokers to add to the deck
     * @param persistence
     *     controls the cards' persistence (permanent or template) 
     */
    private void populateDeck( final Orientation initialOrientation,
                               final int numberOfJokers,
                               final Persistence persistence )
        {

        // prepare to create the cards
        final Orientation savedOrientation = CardBase.setDefaultOrientation( initialOrientation ) ;

        // if these are the 'real' cards - mark them as permanent
        // if they're the template cards - mark them as template
        final Persistence savedPersistence = CardBase.setDefaultPersistence( persistence ) ;

        // populate it with all the playing cards
        createPlayingCards() ;

        // add jokers, if any
        createJokers( numberOfJokers ) ;

        // set the default card state back to its prior state
        CardBase.setDefaultPersistence( savedPersistence ) ;
        CardBase.setDefaultOrientation( savedOrientation ) ;

        }  // end full, 3-arg populateDeck()
    
    
    /**
     * ensure that this deck contains the all the cards it had when instantiated
     * and nothing else
     * 
     * @throws IllegalStateException
     *     if any cards are missing or contains any stray cards
     */
    public void validateDeck() throws IllegalStateException
        {
        
        // utility piles of missing cards and remaining cards
        final Pile missingCards = new Pile() { /* temporary */ } ;
        missingCards.setDefaultOrientation( Orientation.AS_IS ) ;
        missingCards.setAcceptablePersistence( Persistence.UNRESTRICTED ) ;
        
        final Pile remainingCards ;
        
        // move all cards from this deck to a temporary collection
        final Pile temporaryDeck = this.removeAllCards() ;
        
        // iterate over the template moving corresponding cards from
        // the temporary collection back into this deck
        for ( CardBase templateCard : Deck.templateCards )
            {
            CardBase realCard = temporaryDeck.removeCard( templateCard ) ;
            
            // if a card is missing, capture it otherwise put it back in our list
            if ( realCard == null )
                {
                missingCards.addToBottom( templateCard ) ;
                }
            else
                {
                this.addToBottom( realCard ) ;
                }
            }
        
        // after that loop, if there are any cards left in the temporary
        // collection collect them
        remainingCards = temporaryDeck.removeAllCards() ;

        // if there are any collected problems, report them via exception
        if ( ( missingCards.cardCount() != 0 ) || ( remainingCards.cardCount() != 0 ) )
            {
            missingCards.revealAll() ;
            remainingCards.revealAll() ;
            
            CardBase.disableDecoration() ;
            
            // failed validation - report it
            throw new IllegalStateException( String.format( "Deck validation failed; missing: %s; strays: %s",
                                                            missingCards.cardCount() == 0
                                                                    ? "none"
                                                                    : missingCards,
                                                            remainingCards.cardCount() == 0
                                                                    ? "none"
                                                                    : remainingCards ) ) ;
            }

        }   // end validateDeck()


    /*
     * for testing/debugging
     */


    /**
     * (optional) test driver
     *
     * @param args
     *     -unused-
     */
    public static void main( final String[] args )
        {
        
        Deck.templateCards.revealAll() ;
        System.out.printf( "template:%n%s%n%n", Deck.templateCards ) ;
        Deck.templateCards.hideAll() ;
        
        Deck testDeck = new Deck() ;
        System.out.printf( "testDeck:%n%s%n%n", testDeck ) ;
        testDeck.revealAll() ;
        System.out.printf( "testDeck:%n%s%n%n", testDeck ) ;

        Card testCard = new Card( Rank.ACE, Suit.DIAMONDS ).reveal() ;
        System.out.printf( "testCard: %s%n", testCard ) ;
        
        try
            {
            System.out.printf( "add the %s to the deck (won't end well!)%n", testCard ) ;
            testDeck.addToTop( testCard ) ;
            }
        catch ( IllegalArgumentException e )
            {
            System.out.printf( "blew up: %s%n", e.getMessage() ) ;
            }
        
        
        System.out.printf( "validating deck...%n" ) ;
        testDeck.validateDeck() ;
        System.out.printf( "...ok%n" ) ;

        try
            {
            System.out.printf( "%nmessing up deck... removed %s...%n", testDeck.removeTopCard().reveal() ) ;
            System.out.printf( "revalidating deck...%n" ) ;
            testDeck.validateDeck() ;
            System.out.printf( "...ok%n" ) ;
            }
        catch ( IllegalStateException e )
            {
            System.out.printf( "blew up: %s%n", e.getMessage() ) ;
            }
        
        }   // end main()


    public static Deck createStandardDeck() {
        // Create a new Deck with zero jokers and default orientation
        Deck standardDeck = new Deck(FACE_DOWN, 0);

       
        return standardDeck;
    }
    

    }   // end class Deck
