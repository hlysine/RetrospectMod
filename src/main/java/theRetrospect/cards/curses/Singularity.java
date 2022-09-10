package theRetrospect.cards.curses;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.NecronomicurseEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.relics.BottledSingularity;

public class Singularity extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Singularity.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;

    public Singularity() {
        super(ID, TARGET);

        this.isInnate = true;
        this.selfRetain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.dontTriggerOnUseCard) {
            addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, this.magicNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void onRemoveFromMasterDeck() {
        if (AbstractDungeon.player.hasRelic(BottledSingularity.ID)) {
            AbstractDungeon.player.getRelic(BottledSingularity.ID).flash();
        }
        AbstractDungeon.effectsQueue.add(new NecronomicurseEffect(new Singularity(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        this.dontTriggerOnUseCard = true;
        this.returnToHand = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }

    // Note: Implementation of being stuck in hand is done in SingularityPatch
}
