package StaminaBoostMod;

import arc.*;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.util.*;
import arc.math.Angles;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.EventType.*;
import mindustry.gen.Unit;
import mindustry.content.Fx;
import mindustry.mod.*;
import mindustry.ui.Bar;

public class StaminaBoostMain extends Mod {
    private static final float STAMINA_DECREASE_RATE = 0.04f;
    private static final float STAMINA_RECOVERY_RATE = 0.05f;
    public static float staminaLeft = 1.0f;
    private static boolean lowStamina = false;

    private static boolean berserkActive = false;

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

        ModStatusEffects.load();
        Log.info("Loaded Effects constructor.");
    }
    private Color blinkColor(){
        Color c = Color.red.cpy().lerp(Color.green, staminaLeft);
        if(lowStamina){
            // piscar um bocado mais caótico, não precisa ser perfeito
            float t = (Mathf.sin(Time.time / 5f) + 1f) / 2f;
            c.lerp(Color.white, t * 0.8f);
        }

        return c;
    }


    private void update(){
        if(Vars.player == null || Vars.player.unit() == null) return;
        Unit unit = Vars.player.unit();
        boolean isShiftPressed = Core.input.keyDown(KeyCode.shiftLeft);
        boolean isMousePressed = Core.input.keyDown(KeyCode.mouseLeft);
        float delta = Time.delta / 60f;
        if(Core.input.keyTap(KeyCode.altLeft) && staminaLeft > 0.4f){
            staminaLeft -= 0.4f;
            float dashAngle = unit.vel.len() > 0.1f ? unit.vel.angle() : unit.rotation;
            float dashForce = 10f * unit.mass();
            unit.impulse(Angles.trnsx(dashAngle, dashForce), Angles.trnsy(dashAngle, dashForce));
            Fx.circleColorSpark.at(unit.x, unit.y, 0.5f, Color.valueOf("ff8c03"));

        }
        if(Core.input.keyTap(KeyCode.r)){
            berserkActive = !berserkActive;
            if(berserkActive){
                unit.apply(ModStatusEffects.berserk, 60f);
            }
        }
        if(berserkActive){
            if(staminaLeft > 0.0f && !lowStamina){
                staminaDec(delta * 2.0f);
                unit.apply(ModStatusEffects.berserk, 10f);
                if(Mathf.chance(0.3)){
                    Fx.steam.at(unit.x, unit.y, 0, Color.orange);
                }
            } else {
                berserkActive = false;
                lowStamina = true;
            }
        }
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