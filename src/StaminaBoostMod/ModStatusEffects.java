package StaminaBoostMod;

import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.type.StatusEffect;

public class ModStatusEffects {
    public static StatusEffect sprint;
    public static StatusEffect  tired;
    public static StatusEffect berserk;

    public static void load() {

        sprint = new StatusEffect("sprint-buff") {{
            speedMultiplier = 1.5f;
            color = Color.valueOf("67ea00");
            effect = Fx.spawnShockwave;
        }};
        tired = new StatusEffect("tired-debuff") {{
            speedMultiplier = 0.4f;
            effect = Fx.smoke;
            color = Color.black;
            show = true;
        }};
        berserk = new StatusEffect("berserk_mode") {{
            reloadMultiplier = 2f;
            damageMultiplier = 1.5f;
            speedMultiplier = 0.9f;
            color = Color.red;
            effect = Fx.overdriven;
        }};
    }
}