package theRetrospect.cards.wildcard;

import basemod.abstracts.CustomSavable;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WildCard extends AbstractBaseCard implements CustomSavable<List<String>> {

    public static final String ID = RetrospectMod.makeID(WildCard.class.getSimpleName());

    public static final List<WildCardModifier> possibleEffects = Arrays.asList(
            new AttackModifier(),
            new DefendModifier(),
            new DexterityModifier(),
            new DrawModifier(),
            new EnergyModifier(),
            new StrengthModifier(),
            new VulnerableModifier(),
            new WeakModifier()
    );

    private static final CardTarget TARGET = CardTarget.SELF;

    private List<WildCardModifier> effects;

    public WildCard() {
        this(Arrays.asList(new AttackModifier(),
                new DefendModifier(),
                new DexterityModifier(),
                new DrawModifier(),
                new EnergyModifier(),
                new StrengthModifier(),
                new VulnerableModifier(),
                new WeakModifier()));
    }

    public WildCard(List<WildCardModifier> effects) {
        super(ID, TARGET);
        this.effects = effects.stream().map(WildCardModifier::makeCopy).collect(Collectors.toList());
        applyModifiers();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public List<String> onSave() {
        return effects.stream().map(WildCardModifier::getKey).collect(Collectors.toList());
    }

    @Override
    public void onLoad(List<String> object) {
        effects = object.stream()
                .map(WildCard::getModifierForEffect)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        applyModifiers();
    }

    @Override
    public AbstractCard makeCopy() {
        return new WildCard(this.effects);
    }

    private void applyModifiers() {
        CardModifierManager.removeAllModifiers(this, false);
        for (final WildCardModifier effect : effects) {
            effect.apply(this);
            CardModifierManager.addModifier(this, effect);
        }

        List<CardTarget> targets = effects.stream()
                .map(WildCardModifier::getTarget)
                .distinct()
                .collect(Collectors.toList());
        if (targets.contains(CardTarget.SELF_AND_ENEMY)) {
            this.target = CardTarget.SELF_AND_ENEMY;
        } else if (targets.contains(CardTarget.ENEMY)) {
            if (targets.contains(CardTarget.SELF) || targets.contains(CardTarget.ALL))
                this.target = CardTarget.SELF_AND_ENEMY;
            else
                this.target = CardTarget.ENEMY;
        } else if (targets.contains(CardTarget.ALL)) {
            this.target = CardTarget.ALL;
        } else if (targets.contains(CardTarget.SELF) && targets.contains(CardTarget.ALL_ENEMY)) {
            this.target = CardTarget.ALL;
        } else if (targets.contains(CardTarget.SELF)) {
            this.target = CardTarget.SELF;
        } else if (targets.contains(CardTarget.ALL_ENEMY)) {
            this.target = CardTarget.ALL_ENEMY;
        } else {
            this.target = CardTarget.NONE;
        }
    }

    @Override
    public boolean canUpgrade() {
        return this.timesUpgraded < effects.size();
    }

    @Override
    public void upgrade() {
        if (this.timesUpgraded < effects.size()) {
            effects.get(this.timesUpgraded).upgrade(this);
            this.timesUpgraded++;
            this.upgraded = true;
            if (effects.size() == 1)
                this.name = cardStrings.NAME + "+";
            else
                this.name = cardStrings.NAME + "+" + this.timesUpgraded;
            initializeTitle();
            initializeDescription();
        }
    }

    private static WildCardModifier getModifierForEffect(String effect) {
        return possibleEffects.stream()
                .filter(x -> x.getKey().equals(effect))
                .findFirst()
                .map(WildCardModifier::makeCopy)
                .orElse(null);
    }
}
