package sk.uniba.fmph.dcs.terra_futura;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Pile with up to 4 visible cards, hidden deck and discard pile.
 * Index 1 = newest visible, index 4 = oldest.
 */
public final class Pile {
    private static final int MAX_VISIBLE_CARDS = 4;

    // visibleCards[0] = newest, last index = oldest
    private final List<Card> visibleCards = new ArrayList<>();
    private final List<Card> hiddenCards = new ArrayList<>();
    private final List<Card> discardPile = new ArrayList<>();

    private final Random random;

    public Pile(final List<Card> initialVisibleCards,
                final List<Card> initialHiddenCards,
                final long seed) {

        if (initialVisibleCards == null || initialHiddenCards == null) {
            throw new IllegalArgumentException("Initial card lists must be non-null");
        }
        if (initialVisibleCards.size() != MAX_VISIBLE_CARDS) {
            throw new IllegalArgumentException("There must be exactly " + MAX_VISIBLE_CARDS + " visible cards");
        }

        this.visibleCards.addAll(initialVisibleCards);
        this.hiddenCards.addAll(initialHiddenCards);
        this.random = new Random(seed);
    }

    public Pile(final List<Card> initialVisibleCards,
                final List<Card> initialHiddenCards) {
        this(initialVisibleCards, initialHiddenCards, System.currentTimeMillis());
    }

    // 1-based index: 1 = newest, 4 = oldest
    public Optional<Card> getCard(final int index) {
        final int idx = index - 1;
        if (idx < 0 || idx >= visibleCards.size()) {
            return Optional.empty();
        }
        return Optional.ofNullable(visibleCards.get(idx));
    }

    public void takeCard(final int index) {
        final int idx = index - 1;
        if (idx < 0 || idx >= visibleCards.size()) {
            throw new IllegalArgumentException("Index " + index + " out of range");
        }

        visibleCards.remove(idx);
        refillVisibleIfPossible();
    }

    public void removeLastCard() {
        if (!canDiscardLastCard()) {
            throw new IllegalStateException("Cannot discard last card: no hidden or discard cards available");
        }
        final int oldestIndex = visibleCards.size() - 1;
        if (oldestIndex < 0) {
            throw new IllegalStateException("No visible cards to discard");
        }
        final Card oldest = visibleCards.remove(oldestIndex);
        discardPile.add(oldest);

        refillVisibleIfPossible();
    }

    private boolean canDiscardLastCard() {
        return !hiddenCards.isEmpty() || !discardPile.isEmpty();
    }

    private void refillVisibleIfPossible() {
        if (visibleCards.size() >= MAX_VISIBLE_CARDS) {
            return;
        }
        if (hiddenCards.isEmpty() && discardPile.isEmpty()) {
            return;
        }
        final Card drawn = drawFromHiddenOrDiscard();
        visibleCards.add(0, drawn);
    }

    private Card drawFromHiddenOrDiscard() {
        if (hiddenCards.isEmpty()) {
            if (discardPile.isEmpty()) {
                throw new IllegalStateException("No cards available to draw");
            }
            hiddenCards.addAll(discardPile);
            discardPile.clear();
            Collections.shuffle(hiddenCards, random);
        }
        final int index = random.nextInt(hiddenCards.size());
        return hiddenCards.remove(index);
    }

    public String state() {
        final JSONObject result = new JSONObject();
        result.put("visible_count", visibleCards.size());
        result.put("hidden_count", hiddenCards.size());
        result.put("discard_count", discardPile.size());

        final JSONArray visibleArray = new JSONArray();
        for (Card card : visibleCards) {
            if (card == null) {
                visibleArray.put(JSONObject.NULL);
            } else {
                visibleArray.put(new JSONObject(card.state()));
            }
        }
        result.put("visible_cards", visibleArray);

        return result.toString();
    }
}
