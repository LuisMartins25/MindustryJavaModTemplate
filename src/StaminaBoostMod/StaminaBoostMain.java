package StaminaBoostMod;

import arc.*;
import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import arc.util.*;
import mindustry.Vars;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.ui.Bar;

public class StaminaBoostMain extends Mod {

    public static float staminaLeft = 1.0f;
    public StaminaBoostMain(){
        Log.info("Loaded StaminaBoostMain constructor.");

        Events.on(ClientLoadEvent.class, e -> {
            Table myTable = new Table();
            myTable.bottom();
            myTable.setFillParent(true);
            Bar staminaBar = new Bar("Stamina", Color.orange, () -> staminaLeft);
            myTable.add(staminaBar).width(200f).height(25f).padBottom(60f);
            myTable.visible(() -> Vars.ui.hudfrag.shown);
            Vars.ui.hudGroup.addChild(myTable);
        });
    }
    @Override
    public void loadContent(){
        Log.info("Loading content.");
    }
}