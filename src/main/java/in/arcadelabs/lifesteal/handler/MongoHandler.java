package in.arcadelabs.lifesteal.handler;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import org.bson.Document;
import org.bson.UuidRepresentation;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class MongoHandler {

  @Getter(AccessLevel.NONE)
  private final LifeStealPlugin plugin;

  @Getter
  private final MongoClient client;
  private final MongoDatabase database;
  private final MongoCollection<Document> playerData;

  public MongoHandler(LifeStealPlugin plugin) {
    this.plugin = plugin;

    plugin.getLogger().info("Starting MongoDB database connection... ");

    Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);

    client = MongoClients.create(MongoClientSettings.builder()
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .applyConnectionString
                    (new ConnectionString(Objects.requireNonNull
                            (LifeStealPlugin.getLifeSteal().getConfiguration().getString("MongoDB.URI"))))
            .applicationName("lifeSteal")
            .build());

    this.database = client.getDatabase
            (Objects.requireNonNull(LifeStealPlugin.getLifeSteal().getConfiguration().getString("MongoDB.DATABASE")));
    this.playerData = database.getCollection("lifesteal_playerData");

    plugin.getLogger().info((client.getClusterDescription().hasWritableServer())
            ? "Successfully connected to the MongoDB database!" : "Connection failed to the MongoDB database");
  }
}
