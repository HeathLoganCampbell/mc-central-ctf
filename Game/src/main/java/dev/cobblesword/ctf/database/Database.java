package dev.cobblesword.ctf.database;


import dev.cobblesword.ctf.data.playerdata.types.PlayerData;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.DateStorage;
import dev.morphia.mapping.MapperOptions;
import lombok.Getter;
import org.bson.UuidRepresentation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A database wrapper class,
 * call map(?.Class) or mapPackage("package.of.my.stuff")
 *
 * then called done()
 */
public class Database
{
    private @Getter Datastore datastore;
    private MongoClient client;

    public Database(JavaPlugin plugin, String host, int port, String username, String password, String database)
    {
        try {
            String theLie = "mongodb+srv://" + username + ":" + password + "@" + host + "/" + database + "?retryWrites=true&w=majority";

            ConnectionString connectionString = new ConnectionString(theLie);

            MongoClientSettings settings = MongoClientSettings.builder()
                    .uuidRepresentation(UuidRepresentation.STANDARD)
                    .applyConnectionString(connectionString)
                    .serverApi(ServerApi.builder()
                            .version(ServerApiVersion.V1).build())
                    .build();

            client = MongoClients.create(settings);
            datastore = Morphia.createDatastore(client, database, MapperOptions.builder()
                    .discriminatorKey("")
                    .dateStorage(DateStorage.SYSTEM_DEFAULT)
                    .build());
            datastore.ensureIndexes();
            datastore.getMapper().map(PlayerData.class);
        } catch (Exception e) {
            System.out.println("[Mongo] Failed to connect to Mongo");
            e.printStackTrace();
            Bukkit.shutdown();
            throw new RuntimeException("Failed to connect to Mongo");
        }
    }

    public void map(Class<?> clazz)
    {
        datastore.getMapper().map(clazz);
    }

    public void mapPackage(String path)
    {
        datastore.getMapper().mapPackage(path);
    }

}