package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timelineActions.TriggerTimelineAction;
import theRetrospect.cards.AbstractRetrospectCard;
import theRetrospect.util.CallbackUtils;
import theRetrospect.util.CardUtils;

import java.util.concurrent.atomic.AtomicInteger;

import static theRetrospect.RetrospectMod.makeCardPath;

public class TemporalAnomaly extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(TemporalAnomaly.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("temporal_anomaly.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = -1;

    public TemporalAnomaly() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = CardUtils.calculateXCostEffect(this);

        if (this.upgraded)
            effect += 1;

        if (effect > 0) {
            if (!this.freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount);
            }

            AtomicInteger remainingEffect = new AtomicInteger(effect);
            CallbackUtils.ForLoop(
                    () -> remainingEffect.get() > 0,
                    remainingEffect::decrementAndGet,
                    next -> addToBot(new TriggerTimelineAction(null, true, new AbstractGameAction() {
                        @Override
                        public void update() {
                            next.run();
                            this.isDone = true;
                        }
                    }))
            );
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}