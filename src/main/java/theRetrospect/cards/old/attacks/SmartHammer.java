package theRetrospect.cards.old.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.ChooseFromCardGroupAction;
import theRetrospect.cards.AbstractBaseCard;

public class SmartHammer extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(SmartHammer.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public SmartHammer() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new ChooseFromCardGroupAction(
                p.discardPile.getUpgradableCards().group,
                cardStrings.EXTENDED_DESCRIPTION[0],
                1,
                false,
                true,
                card -> {
                    if (card.canUpgrade()) {
                        card.upgrade();
                    }
                    AbstractCard copy = card.makeStatEquivalentCopy();
                    copy.superFlash();
                    addToBot(new VFXAction(new ShowCardBrieflyEffect(copy, Settings.WIDTH / 2f + 30.0F * Settings.scale + AbstractCard.IMG_WIDTH, Settings.HEIGHT / 2f)));
                }
        ));
    }
}