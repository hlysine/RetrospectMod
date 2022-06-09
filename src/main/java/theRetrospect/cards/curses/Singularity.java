package theRetrospect.cards.curses;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.NecronomicurseEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;

import static theRetrospect.RetrospectMod.makeCardPath;

public class Singularity extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Singularity.class.getSimpleName());

    public static final String IMG = makeCardPath("singularity.png");

    private static final CardColor COLOR = CardColor.CURSE;
    private static final CardRarity RARITY = CardRarity.CURSE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.CURSE;

    private static final int COST = -2;

    public Singularity() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);

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

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }
}
