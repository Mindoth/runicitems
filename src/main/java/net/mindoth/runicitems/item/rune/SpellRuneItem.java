package net.mindoth.runicitems.item.rune;

import net.mindoth.runicitems.spells.AbstractSpell;

public class SpellRuneItem extends RuneItem {

    public SpellRuneItem(Properties pProperties, int drain, AbstractSpell spell) {
        super(pProperties, drain);
        this.spell = spell;
    }

    public AbstractSpell spell;
}
