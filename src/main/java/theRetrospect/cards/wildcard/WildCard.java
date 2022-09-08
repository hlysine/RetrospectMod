package theRetrospect.cards.wildcard;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomSavable;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;

import java.util.Arrays;
import java.util.Collections;
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

    public List<WildCardModifier> modifiers;

    public WildCard() {
        this(Collections.emptyList());
    }

    public WildCard(List<WildCardModifier> modifiers) {
        super(ID, TARGET);
        this.modifiers = modifiers.stream().map(WildCardModifier::makeCopy).collect(Collectors.toList());
        applyModifiers();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public List<String> onSave() {
        return modifiers.stream().map(WildCardModifier::getKey).collect(Collectors.toList());
    }

    @Override
    public void onLoad(List<String> object) {
        modifiers = object.stream()
                .map(WildCard::getModifierForEffect)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        applyModifiers();
    }

    @Override
    public AbstractCard makeCopy() {
        return new WildCard(this.modifiers);
    }

    private void applyModifiers() {
        for (WildCardModifier possibleEffect : possibleEffects) {
            CardModifierManager.removeModifiersById(this, possibleEffect.getKey(), true);
        }
        for (final WildCardModifier effect : modifiers) {
            effect.apply(this);
            CardModifierManager.addModifier(this, effect);
        }

        List<CardTarget> targets = modifiers.stream()
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

        List<CardType> types = modifiers.stream()
                .map(WildCardModifier::getType)
                .distinct()
                .collect(Collectors.toList());
        if (types.contains(CardType.POWER)) {
            this.type = CardType.POWER;
        } else if (types.contains(CardType.ATTACK)) {
            this.type = CardType.ATTACK;
        } else if (types.contains(CardType.SKILL)) {
            this.type = CardType.SKILL;
        } else if (types.contains(CardType.STATUS)) {
            this.type = CardType.STATUS;
            this.color = AbstractCard.CardColor.COLORLESS;
        } else {
            this.type = CardType.CURSE;
            this.color = AbstractCard.CardColor.CURSE;
        }

        ReflectionHacks.privateMethod(AbstractCard.class, "createCardImage").invoke(this);

        initializeTitle();
        initializeDescription();
        ReflectionHacks.privateMethod(AbstractCard.class, "updateTransparency").invoke(this);

        if (this.type == CardType.ATTACK) {
            this.textureImg = RetrospectMod.makeCardPath(info.getImage().split("\\.")[0] + "_attack.png");
        } else {
            this.textureImg = RetrospectMod.makeCardPath(info.getImage());
        }
        loadCardImage(textureImg);
    }

    private static WildCardModifier getModifierForEffect(String effect) {
        return possibleEffects.stream()
                .filter(x -> x.getKey().equals(effect))
                .findFirst()
                .map(WildCardModifier::makeCopy)
                .orElse(null);
    }
}
