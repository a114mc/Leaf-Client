/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package net.nonemc.leaf.launch.data.modernui.clickgui.fonts.util;

public final class SneakyThrowing {

    private SneakyThrowing() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static RuntimeException sneakyThrow(Throwable throwable) {
        return sneakyThrow0(throwable);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> T sneakyThrow0(Throwable throwable) throws T {
        throw (T) throwable;
    }
}
