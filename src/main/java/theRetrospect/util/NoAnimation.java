package theRetrospect.util;

import basemod.animations.AbstractAnimation;

public class NoAnimation extends AbstractAnimation {
    @Override
    public Type type() {
        return Type.NONE;
    }
}
