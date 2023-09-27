package dev.cobblesword.ctf.data.playerdata.customevents;

import dev.cobblesword.ctf.data.playerdata.types.PlayerData;
import dev.cobblesword.libraries.common.EventBase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerDataEvent extends EventBase
{
    private PlayerData playerData;
}