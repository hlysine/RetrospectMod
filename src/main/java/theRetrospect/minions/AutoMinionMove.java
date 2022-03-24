package theRetrospect.minions;

import com.badlogic.gdx.graphics.Texture;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import kobting.friendlyminions.monsters.MinionMove;

public class AutoMinionMove extends MinionMove {
    public Runnable moveActions;

    public AutoMinionMove(String ID, AbstractFriendlyMonster owner, Texture moveImage, String moveDescription, Runnable moveActions) {
        super(ID, owner, moveImage, moveDescription, moveActions);
        this.moveActions = moveActions;
    }

    @Override
    protected void onClick() {
        // super intentionally not called
    }
}
