package StaminaBoostMod;

import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.type.StatusEffect;

public class ModStatusEffects {
    // Definimos como public static para acessar de qualquer lugar
    public static StatusEffect sprint, tired;

    public static void load() {
        // Definição do efeito de corrida
        sprint = new StatusEffect("sprint-buff") {{
            speedMultiplier = 1.5f;
            color = Color.valueOf("76ff03");
            effect = Fx.spawnShockwave;
            show = false;
        }};

        // Definição do efeito de cansaço
        tired = new StatusEffect("tired-debuff") {{
            speedMultiplier = 0.5f;
            effect = Fx.smoke;
            color = Color.red;
            show = true;
        }};
    }
}