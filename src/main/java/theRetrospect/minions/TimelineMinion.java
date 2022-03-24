package theRetrospect.minions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import theRetrospect.RetrospectMod;
import theRetrospect.powers.TimerPower;

import java.util.List;

public class TimelineMinion extends AbstractMinionWithCards {

    public static final String ID = RetrospectMod.makeID(TimelineMinion.class.getSimpleName());
    private static final CharacterStrings minionStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    public static final String IMG = "theRetrospectResources/images/char/retrospectCharacter/Spriter/object1.png";
    public static final String NAME = minionStrings.NAMES[0];

    public TimelineMinion(List<AbstractCard> cards, int offsetX, int offsetY) {
        super(NAME, ID, 20, 0, 0, 240, 240, IMG, offsetX, offsetY);
        setCards(cards);
        addPower(new TimerPower(this, 2));
    }
}
