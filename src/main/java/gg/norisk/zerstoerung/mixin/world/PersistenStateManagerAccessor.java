package gg.norisk.zerstoerung.mixin.world;

import net.minecraft.world.PersistentStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.io.File;

@Mixin(PersistentStateManager.class)
public interface PersistenStateManagerAccessor {
    @Accessor("directory")
    File getDirectory();
}
