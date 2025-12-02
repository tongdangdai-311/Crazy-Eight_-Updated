package edu.wit.scds.ds.lists.app.card_game.your_game.game;

import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Card;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Suit;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Deck;
import edu.wit.scds.ds.lists.app.card_game.your_game.pile.DiscardPile;
import edu.wit.scds.ds.lists.app.card_game.your_game.pile.Stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Crazy Eights – TODO//// write down all the rules
 */
public final class CrazyEight {

    private final List<Player> players = new ArrayList<>();
    private final Stock stock = new Stock();
    private final DiscardPile discardPile = new DiscardPile();
    private final Scanner scanner = new Scanner(System.in);

    private int currentPlayerIndex = 0;
    private boolean clockwise = true;
    private Suit currentSuit;           // suit that must be matched
    private int pendingDraw = 0;         // +2 / +5 stack

    public static void main(final String[] args) {
        new CrazyEight().run();
    }

    private void run() {
        welcome();
        setup();
        playGame();
        tearDown();
    }

    private void welcome() {
        System.out.println("""
      CRAZYEIGHT, PLAY RIGHT AWAY!!!!
            """);
    }

    private void setup() {
        System.out.print("How many players (2+)? ");
        int n = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 1; i <= n; i++) {
            System.out.print("Player " + i + " name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = "Player" + i;
            players.add(new Player(name));
        }

        final Deck deck = Deck.createStandardDeck();
        deck.shuffle();
        stock.moveCardsToBottom(deck);

        // deal 7 cards each
        for (int i = 0; i < 7; i++) {
            for (final Player p : players) {
                try {
                    p.dealtACard(stock.drawTopCard().hide());
                } catch (final Exception e) {
                    // unexpected — if stock ran out, stop dealing
                    System.err.println("Stock depleted while dealing: " + e);
                }
            }
        }

        // put first non-special card onto discard
        Card first = null;
        while (true) {
            try {
                first = stock.drawTopCard();
                
            } catch (final Exception e) {
                System.err.println("Failed to draw starting card: " + e);
                break;
            }
            if (first == null) break;
            if (first.rank == Rank.TWO || first.rank == Rank.ACE ||
                    (first.rank == Rank.QUEEN && first.suit == Suit.SPADES)) {
                // put aside temporarily on bottom of stock so not lost
                stock.addToBottom(first); // if addToBottom exists; if not, we can addToTop
                continue;
            } else {
                break;
            }
        }

        if (first != null) {
            discardPile.addCard(first.reveal());
            currentSuit = first.suit;
            System.out.println("\nStarting card: " + first.reveal() + "\n");
        } else {
            System.out.println("No starting card available.");
        }
    }

    private void playGame() {
        while (true) {
            final Player current = players.get(currentPlayerIndex);

            // Win check
            if (current.getHand().cardCount() == 0) {
                System.out.printf("\n%s WINS THE GAME!!! Empty hand!\n", current.getName());
                break;
            }

            System.out.printf("\n=== %s's turn (%d cards) ===\n", current.getName(), current.getHand().cardCount());
            final Card topCard = discardPile.getTopCard();
            System.out.println("Top card: " + topCard.reveal() +
                    (currentSuit != topCard.suit ? " (must match: " + currentSuit + ")" : ""));

            // === Handle +2 / +5 stack ===
            if (pendingDraw > 0) {
                System.out.printf("%s must draw %d cards!\n", current.getName(), pendingDraw);
                for (int i = 0; i < pendingDraw && !stock.isEmpty(); i++) {
                    try {
                        current.dealtACard(stock.drawTopCard());
                    } catch (final Exception e) {
                        // if stock empties unexpectedly
                        break;
                    }
                }
                pendingDraw = 0;
                nextPlayer();
                continue;
            }

            System.out.println("Your hand: " + current.revealHand());

            // === Must draw until can play ===
            if (!current.getHand().canPlay(topCard, currentSuit)) {
                current.getHand().drawUntilCanPlay(stock, topCard, currentSuit);
                nextPlayer();
                continue;
            }

            // === Choose card ===
            final int maxIndex = current.getHand().cardCount() - 1;
            int choice = -1;
            
            while (true) {
                System.out.print("Choose card index (0-" + maxIndex + "): ");
                final String line = scanner.nextLine().trim();
                try {
                    choice = Integer.parseInt(line);
                    if (choice >= 0 && choice <= maxIndex) 
                        {
                        Card playedCard = (Card) current.getHand().getCardAt( choice );
                        
                        if(isValidPlay(playedCard, topCard, currentSuit))
                            {
                            break;
                            }
                        }
                } catch (final NumberFormatException e) {
                    // try again
                    System.out.println("Invalid choice. Try again.");
                }
            }

            final Card played = current.getHand().removeCardAt(choice).reveal();
            discardPile.addCard(played);

            System.out.println(current.getName() + " plays → " + played.reveal());

            // === SPECIAL CARD EFFECTS ===
            if (played.rank == Rank.EIGHT) {
                System.out.print("Declare suit [H/D/C/S]: ");
                final char c = Character.toUpperCase(scanner.nextLine().trim().charAt(0));
                currentSuit = switch (c) {
                    case 'H' -> Suit.HEARTS;
                    case 'D' -> Suit.DIAMONDS;
                    case 'C' -> Suit.CLUBS;
                    default -> Suit.SPADES;
                };
                System.out.println("→ Next suit: " + currentSuit);
            } else if (played.rank == Rank.TWO) {
                pendingDraw += 2;
            } else if (played.rank == Rank.QUEEN && played.suit == Suit.SPADES) {
                pendingDraw += 5;
            } else if (played.rank == Rank.JACK) {
                if (players.size() == 2) nextPlayer();
                else {
                    clockwise = !clockwise;
                    System.out.println("Direction reversed!");
                }
            } else if (played.rank == Rank.ACE) {
                System.out.println(current.getName() + " gets another turn!");
                // Ace = play again (do not advance currentPlayerIndex)
                continue;
            } else {
                currentSuit = played.suit;
            }

            nextPlayer();
        }
    }

    private void nextPlayer() {
        currentPlayerIndex = clockwise
                ? (currentPlayerIndex + 1) % players.size()
                : (currentPlayerIndex - 1 + players.size()) % players.size();
    }

    private void tearDown() {
        System.out.println("\nReturning all cards...");
        for (final Player p : players) stock.moveCardsToBottom(p.turnInAllCards());
        stock.moveCardsToBottom(discardPile);
        System.out.printf("All %d cards safely returned.\nThanks for playing!\n", stock.cardCount());
    }
    
    private boolean isValidPlay( Card c,
                                Card topCard,
                                Suit currentSuit )
        {

        if ( c.rank == Rank.EIGHT )
            return true ;

        if ( c.rank == Rank.QUEEN && c.suit == Suit.SPADES )
            {
            return topCard.suit == Suit.SPADES || topCard.rank == Rank.QUEEN ;
            }

        return c.suit == currentSuit ||
               c.rank == topCard.rank ;

        }
    
}
