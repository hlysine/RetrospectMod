package theRetrospect.cards.curses;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.NecronomicurseEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;

public class Singularity extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Singularity.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;

    public Singularity() {
        super(ID, TARGET);

        this.isInnate = true;
        this.retain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void onRemoveFromMasterDeck() {
        if (AbstractDungeon.player.hasRelic("Necronomicon")) {
            AbstractDungeon.player.getRelic("Necronomicon").flash();
        }
        AbstractDungeon.effectsQueue.add(new NecronomicurseEffect(new Singularity(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
    }

    // Note: Implementation of being stuck in hand is done in SingularityPatch
}
