package in.arcadelabs.lifesteal.profile;

public interface ProfileStorage {

    void load(Profile profile);

    void save(Profile profile);

    void disconnect();

}
