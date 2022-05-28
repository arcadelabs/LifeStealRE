package in.arcadelabs.lifesteal.profile;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class ProfileHandler {

    private LifeStealPlugin plugin;
    private final Map<UUID, Profile> profileMap = new HashMap<>();

    public ProfileHandler(LifeStealPlugin plugin) {
        this.plugin = plugin;
    }

    public Profile createProfile(UUID uuid) throws IOException {
        final Profile profile = new Profile(uuid);

        plugin.getProfileStorage().load(profile);
        profileMap.put(uuid, profile);

        return profile;
    }
    public void handleRemove(Profile profile) throws IOException {
        plugin.getProfileStorage().save(profile);
        profileMap.remove(profile.getUniqueID());
    }
    public Profile getProfile(UUID uuid) {
        return profileMap.getOrDefault(uuid, null);
    }
}
