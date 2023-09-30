package dev.cobblesword.ctf.game.team;

import dev.cobblesword.libraries.common.Reflection;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TeamPrefix
{
    private static final Class PACKET_CLASS = PacketPlayOutScoreboardTeam.class;

    private static final Field TEAM_NAME = Reflection.getField(PACKET_CLASS, "a");
    private static final Field DISPLAY_NAME = Reflection.getField(PACKET_CLASS, "b");
    private static final Field PREFIX = Reflection.getField(PACKET_CLASS, "c");
    private static final Field SUFFIX = Reflection.getField(PACKET_CLASS, "d");
    private static final Field NAME_TAG_VISIBILITY = Reflection.getField(PACKET_CLASS, "e");
    private static final Field MEMBERS = Reflection.getField(PACKET_CLASS, "g");
    private static final Field TEAM_MODE = Reflection.getField(PACKET_CLASS, "h");
    private static final Field OPTIONS = Reflection.getField(PACKET_CLASS, "i");

    public static void createTeamWithPlayer(String prefix, String teamName, List<Player> playersInTeam, List<Player> viewers)
    {
        // Create Team Packet
        PacketPlayOutScoreboardTeam createTeamPacket = new PacketPlayOutScoreboardTeam();
        setField(createTeamPacket, TEAM_NAME, teamName);
        setField(createTeamPacket, DISPLAY_NAME, teamName);
        setField(createTeamPacket, PREFIX, prefix);
        setField(createTeamPacket, SUFFIX, "");
        setField(createTeamPacket, NAME_TAG_VISIBILITY, "always");
        setField(createTeamPacket, OPTIONS, 0);
        setField(createTeamPacket, TEAM_MODE, TeamMode.CREATE.getMode());

        // Add Player to Team Packet
        PacketPlayOutScoreboardTeam addPlayerPacket = new PacketPlayOutScoreboardTeam();
        setField(addPlayerPacket, TEAM_NAME, teamName);
        setField(addPlayerPacket, TEAM_MODE, TeamMode.ADD_PLAYER.getMode());
        List<String> teamMembers = new ArrayList<>();
        for (Player player : playersInTeam) {
            teamMembers.add(player.getName());
        }
        setField(addPlayerPacket, MEMBERS, teamMembers);

        for (Player viewer : viewers) {
            CraftPlayer craftPlayer = (CraftPlayer) viewer;
            craftPlayer.getHandle().playerConnection.sendPacket(createTeamPacket);
            craftPlayer.getHandle().playerConnection.sendPacket(addPlayerPacket);
        }
    }

    public static void removeTeamWithPlayer(String teamName, Collection<? extends Player> viewers)
    {
        // Create Team Packet
        PacketPlayOutScoreboardTeam createTeamPacket = new PacketPlayOutScoreboardTeam();
        setField(createTeamPacket, TEAM_NAME, teamName);
        setField(createTeamPacket, TEAM_MODE, TeamMode.REMOVE.getMode());

        for (Player viewer : viewers) {
            CraftPlayer craftPlayer = (CraftPlayer) viewer;
            craftPlayer.getHandle().playerConnection.sendPacket(createTeamPacket);
        }
    }

    private static void setField(Object obj, Field field, Object value) {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace(); // Logging the exception is important for debugging
        }
    }

    public enum TeamMode {

        CREATE(0),
        REMOVE(1),
        UPDATE(2),
        ADD_PLAYER(3),
        REMOVE_PLAYER(4);

        private final int mode;

        TeamMode(final int mode) {
            this.mode = mode;
        }

        public final int getMode() {
            return this.mode;
        }

    }
}
