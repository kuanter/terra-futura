package sk.uniba.fmph.dcs.terra_futura;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;

public final class SelectReward {

    private Integer player; // if null = no selection pending
    private Card targetCard;
    private final List<Resource> options = new ArrayList<>();

    public void setReward(final int playerId,
                          final Card card,
                          final Resource[] rewardOptions) {
        if (player != null) {
            throw new IllegalStateException("Reward already pending");
        }
        this.player = playerId;
        this.targetCard = card;
        options.clear();
        options.addAll(Arrays.asList(rewardOptions));
    }

    public Optional<Integer> player() {
        return Optional.ofNullable(player);
    }

    public boolean canSelectReward(final Resource resource) {
        return player != null && options.contains(resource);
    }

    public void selectReward(final Resource resource) {
        if (!canSelectReward(resource)) {
            throw new IllegalStateException("Illegal reward selection");
        }
        targetCard.putResources(List.of(resource));
        player = null;
        targetCard = null;
        options.clear();
    }

    public String state() {
        JSONObject json = new JSONObject();
        json.put("player", player == null ? JSONObject.NULL : player.intValue());
        JSONArray arr = new JSONArray();
        for (Resource r : options) {
            arr.put(r.name());
        }
        json.put("options", arr);
        return json.toString();
    }
}
