package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.ShuffleDiscardPileAction;
import theRetrospect.cards.AbstractBaseCard;

public class RewriteHistory extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(RewriteHistory.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public RewriteHistory() {
        super(ID, TARGET);

        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = p.discardPile.size();
        if (count > 0) {
            if (m != null) {
                addToBot(new SFXAction("MONSTER_COLLECTOR_DEBUFF"));
                AbstractDungeon.effectsQueue.add(new CollectorCurseEffect(m.hb.cX, m.hb.cY));
            }
            addToBot(new WaitAction(0.5f));
            for (int i = 0; i < count; i++) {
                addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL, true));
            }
        }
        addToBot(new ShuffleDiscardPileAction());
    }
}
