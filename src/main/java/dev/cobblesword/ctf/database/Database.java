package dev.cobblesword.ctf.database;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.cobblesword.ctf.data.playerdata.database.PlayerDataRepository;
import dev.cobblesword.ctf.data.playerdata.types.PlayerData;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.DateStorage;
import dev.morphia.mapping.MapperOptions;
import lombok.Getter;
import org.bson.UuidRepresentation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * A database wrapper class,
 * call map(?.Class) or mapPackage("package.of.my.stuff")
 *
 * then called done()
 */
public class Database
{
    private MongoClient mongo;
    private Morphia morphia;
    private @Getter Datastore datastore;

    private String databaseName;

    private MongoClient client;

//    public static void main(String[] args) {
//        Database database = new Database(null, "capturetheflag.efkvpik.mongodb.net", 27017, "databaseUser", "c5Hgq1oW8WuntJC6", "CaptureTheFlag");
//
//        PlayerData playerData = new PlayerData();
//        playerData.setName("sprick");
//        playerData.setUuid(UUID.randomUUID());
//
//        new PlayerDataRepository(database).save(playerData);
//        System.out.println("saved");
//    }

    public Database(JavaPlugin plugin, String host, int port, String username, String password, String database)
    {
        this.databaseName = database;

        try {
            String theLie = "mongodb+srv://" + username + ":" + password + "@" + host + "/" + database + "?retryWrites=true&w=majority";

            ConnectionString connectionString = new ConnectionString(theLie); //Mongo connection string object

            MongoClientSettings settings = MongoClientSettings.builder() //Settings building
                    .uuidRepresentation(UuidRepresentation.STANDARD) //How to store UUIDs?
                    .applyConnectionString(connectionString) //Where we are connecting
                    .serverApi(ServerApi.builder() //Serverapi building
                            .version(ServerApiVersion.V1).build()) //What the serverapi version is
                    .build();

            client = MongoClients.create(settings); //Actually start the connection
            datastore = Morphia.createDatastore(client, database, MapperOptions.builder()
                    .discriminatorKey("") //Removes discriminator keys
                    .dateStorage(DateStorage.SYSTEM_DEFAULT) //Set dateStorage format
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

    /**
     * Called after you have made all your mappings
     */
    public void done()
    {
        datastore.ensureIndexes();
    }
}