package StaminaBoostMod;

import arc.graphics.Color;
import mindustry.type.StatusEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class StaminaBoostMainTest {

    private StaminaBoostMain staminaMod;

    @BeforeEach
    void setUp() throws Exception {

        // Initialize the Mod class
        staminaMod = new StaminaBoostMain();


        // Reset the public static state before every test
        StaminaBoostMain.staminaLeft = 1.0f;

        ModStatusEffects.sprint = new StatusEffect("sprint-buff");
        ModStatusEffects.tired = new StatusEffect("tired-debuff");
        ModStatusEffects.berserk = new StatusEffect("berserk_mode");


        // Reset the private static 'berserkActive' field to false
        Field berserkField = StaminaBoostMain.class.getDeclaredField("berserkActive");
        berserkField.setAccessible(true);
        berserkField.set(null, false);

    }

    // Stamina Tests

    // Test  stamina decrease method
    @Test
    void testStaminaDecrease() throws Exception {
        float start = StaminaBoostMain.staminaLeft;
        staminaMod.staminaDec(0.1f);

        // Assert
        assertTrue(StaminaBoostMain.staminaLeft < start, "Stamina should decrease.");
        assertTrue(StaminaBoostMain.staminaLeft >= 0f, "Stamina should not go below 0.");
    }

    // Test  stamina increase method
    @Test
    void testStaminaIncrease() throws Exception {
        float start = StaminaBoostMain.staminaLeft-0.5f;
        staminaMod.staminaInc(0.1f);

        // Assert
        assertTrue(StaminaBoostMain.staminaLeft > start, "Stamina should increase.");
        assertTrue(StaminaBoostMain.staminaLeft <= 1f, "Stamina should not exceed 1.");
    }

    // Test  stamina not exceed 1
    @Test
    void testStaminaOne() throws Exception {
        StaminaBoostMain.staminaLeft = 0.95f;
        staminaMod.staminaInc(10f);

        // Assert
        assertEquals(1.0f, StaminaBoostMain.staminaLeft, 0.0001);
    }

    // Test  stamina not go below 0
    @Test
    void testStaminaZero() throws Exception {
        staminaMod.staminaDec(999f);

        // Assert
        assertEquals(0f, StaminaBoostMain.staminaLeft, 0.0001);
    }

    @Test
    void blinkColorChange() throws Exception{
        var method = StaminaBoostMain.class.getDeclaredMethod("blinkColor");
        method.setAccessible(true);
        // Color with full stamina
        StaminaBoostMain.staminaLeft = 1f;
        Color full = (Color) method.invoke(staminaMod);
        // Color with empty stamina
        StaminaBoostMain.staminaLeft = 0f;
        Color empty = (Color) method.invoke(staminaMod);

        // Assert
        assertNotEquals(full.toString(), empty.toString(),
                "Color should be different depending on stamina.");
    }

    // Test berserk deactivate at 0 stamina
    @Test
    void testBerserkStopsInStaminaZero() {
        StaminaBoostMain.berserkActive = true;

        StaminaBoostMain.staminaLeft = 0.01f;

        staminaMod.staminaDec(0.1f * 2f);

        // Assert
        assertEquals(0f, StaminaBoostMain.staminaLeft, 0.0001);
        assertFalse(StaminaBoostMain.berserkActive,
                "Berserk should auto-deactivate at 0 stamina.");
    }

}