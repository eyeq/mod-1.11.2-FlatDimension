package eyeq.flatdimension.world;

import eyeq.flatdimension.FlatDimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldType;
import net.minecraft.world.border.WorldBorder;
import eyeq.util.world.UWorldProvider;

public class WorldProviderFlat extends UWorldProvider {
    @Override
    public DimensionType getDimensionType() {
        return FlatDimension.dimensionFlat;
    }

    @Override
    public WorldType getTerrainType() {
        return WorldType.FLAT;
    }

    @Override
    public WorldBorder createWorldBorder() {
        return new WorldBorder() {
            @Override
            public double getCenterX() {
                return super.getCenterX() / 64.0;
            }

            @Override
            public double getCenterZ() {
                return super.getCenterZ() / 64.0;
            }
        };
    }
}
