package theRetrospect.cards.old.skills;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;

public class AssetProcurement extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(AssetProcurement.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;

    public AssetProcurement() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new MakeTempCardInHandAction(AbstractDungeon.returnTrulyRandomColorlessCardInCombat()));
    }
}
