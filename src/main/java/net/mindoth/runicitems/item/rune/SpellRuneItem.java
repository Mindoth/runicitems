package net.mindoth.runicitems.item.rune;

import net.mindoth.runicitems.spell.abstractspell.AbstractSpell;

public class SpellRuneItem extends RuneItem {

    public SpellRuneItem(Properties pProperties, AbstractSpell spell) {
        super(pProperties);
        this.spell = spell;
    }

    public AbstractSpell spell;
}
