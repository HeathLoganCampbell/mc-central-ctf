package dev.cobblesword.ctf.data.playerdata.database;

import dev.cobblesword.ctf.data.playerdata.types.PlayerData;
import dev.cobblesword.ctf.database.AbstractRepository;
import dev.cobblesword.ctf.database.Database;
import dev.morphia.query.filters.Filters;
import org.jetbrains.annotations.NotNull;

public class PlayerDataRepository extends AbstractRepository<PlayerData>
{
    private Database database;

    public PlayerDataRepository(@NotNull  Database database) {
        super(database.getDatastore());
    }

//    public PlayerData findByUuid(String uuid) {
//        PlayerData playerData = this.database.getDatastore()
//                .find(PlayerData.class)
//                .filter(Filters.eq("uuid", uuid))
//                .first();
//        return playerData;
//    }
//
    public PlayerData findByUsername(String username) {
        PlayerData playerData = this.database.getDatastore()
                .find(PlayerData.class)
                .filter(Filters.eq("name", username))
                .first();

        return playerData;
    }


    @Override
    protected Class<PlayerData> getEntityClass() {
        return PlayerData.class;
    }
}
