package StaminaBoostMod;

import arc.*;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.util.*;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.EventType.*;
import mindustry.gen.Unit;
import mindustry.mod.*;
import mindustry.ui.Bar;

public class StaminaBoostMain extends Mod {
    private static final float STAMINA_DECREASE_RATE = 0.05f;
    private static final float STAMINA_RECOVERY_RATE = 0.01f;

    public static float staminaLeft = 1.0f;
    private static boolean lowStamina = false;

    public StaminaBoostMain(){
        Log.info("Loaded StaminaBoostMain constructor.");

        Events.on(ClientLoadEvent.class, e -> {
            Table myTable = new Table();
            myTable.bottom();
            myTable.setFillParent(true);

            Bar staminaBar = new Bar(
                    () -> lowStamina ? "OverCharge" : "Stamina",
                    () -> blinkColor(),
                    () -> staminaLeft
            );

            myTable.add(staminaBar).width(200f).height(25f).padBottom(60f);
            myTable.visible(() -> Vars.ui.hudfrag.shown);
            Vars.ui.hudGroup.addChild(myTable);

            Events.run(EventType.Trigger.update, () -> update());
        });
    }


    @Override
    public void loadContent(){
        // Isto é obrigatório para o jogo reconhecer os teus efeitos novos
        ModStatusEffects.load();
        Log.info("Loaded Effects constructor.");
    }

    private Color blinkColor(){
        Color base = new Color(Color.red).lerp(Color.green, staminaLeft);
        if(lowStamina){
            float s = Mathf.absin(Time.time, 6f, 1f);
            return base.lerp(Color.white, s);
        }
        return base;
    }

    private void update(){
        if(Vars.player == null || Vars.player.unit() == null) return;

        Unit unit = Vars.player.unit();
        boolean isShiftPressed = Core.input.keyDown(KeyCode.shiftLeft);
        boolean isMousePressed = Core.input.keyDown(KeyCode.mouseLeft);
        float delta = Time.delta / 60f;

        if(isShiftPressed && !isMousePressed){
            staminaDec(delta);
            if(!lowStamina){
                unit.apply(ModStatusEffects.sprint, 10f);
            }
            if(staminaLeft <= 0.01f){
                lowStamina = true;
            }
        }
        else{
            staminaInc(delta);
        }
        if(lowStamina){
            unit.apply(ModStatusEffects.tired, 10f);
        }
    }
    private void staminaInc(float delta){
        float stamina = staminaLeft + STAMINA_RECOVERY_RATE * delta;
        staminaLeft = Math.min(stamina, 1.0f);

        if(staminaLeft > 0.3f)
            lowStamina = false;
    }

    private void staminaDec(float delta){
        float stamina = staminaLeft - STAMINA_DECREASE_RATE * delta;
        staminaLeft = Math.max(stamina, 0.0f);
    }
}