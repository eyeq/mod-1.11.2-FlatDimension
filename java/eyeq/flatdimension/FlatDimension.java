package eyeq.flatdimension;

import eyeq.util.block.BlockUtils;
import eyeq.util.block.portal.IPortalPattern;
import eyeq.util.block.portal.UPortalPattern;
import eyeq.util.client.model.UModelCreator;
import eyeq.util.client.model.UModelLoader;
import eyeq.util.client.model.gson.BlockmodelJsonFactory;
import eyeq.util.client.model.gson.ItemmodelJsonFactory;
import eyeq.util.client.renderer.ResourceLocationFactory;
import eyeq.util.client.resource.ULanguageCreator;
import eyeq.util.client.resource.lang.LanguageResourceManager;
import eyeq.util.oredict.UOreDictionary;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import eyeq.flatdimension.block.BlockFlatPortal;
import eyeq.flatdimension.block.BlockSlimeWatery;
import eyeq.flatdimension.world.WorldProviderFlat;
import eyeq.util.item.UItemPlace;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static eyeq.flatdimension.FlatDimension.MOD_ID;

@Mod(modid = MOD_ID, version = "1.0", dependencies = "after:eyeq_util")
@Mod.EventBusSubscriber
public class FlatDimension {
    public static final String MOD_ID = "eyeq_flatdimension";

    @Mod.Instance(MOD_ID)
    public static FlatDimension instance;

    private static final ResourceLocationFactory resource = new ResourceLocationFactory(MOD_ID);

    public static DimensionType dimensionFlat;

    public static IPortalPattern flatPortalGate;

    public static Block flatPortal;
    public static Block slimeWatery;

    public static Item wandSlime;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        addRecipes();
        registerDimensions();
        if(event.getSide().isServer()) {
            return;
        }
        renderItemModels();
        createFiles();
    }

    @SubscribeEvent
    protected static void registerBlocks(RegistryEvent.Register<Block> event) {
        flatPortal = new BlockFlatPortal().setHardness(-1.0F).setLightLevel(1.0F).setUnlocalizedName("flatPortal");
        slimeWatery = new BlockSlimeWatery().setUnlocalizedName("slimeWatery");

        GameRegistry.register(flatPortal, resource.createResourceLocation("flat_portal"));
        GameRegistry.register(slimeWatery, resource.createResourceLocation("watery_slime"));
    }

    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event) {
        wandSlime = new UItemPlace(slimeWatery.getDefaultState(), true).setUnlocalizedName("wandSlime");

        GameRegistry.register(new ItemBlock(slimeWatery), slimeWatery.getRegistryName());

        GameRegistry.register(wandSlime, resource.createResourceLocation("slimy_wand"));
    }

    public static void addRecipes() {
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(slimeWatery, 4), UOreDictionary.OREDICT_SLIME_BLOCK, Items.WATER_BUCKET));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(wandSlime), " X", "Y ",
                'X', UOreDictionary.OREDICT_SLIME_BALL, 'Y', UOreDictionary.OREDICT_STICK));
    }

    public static void registerDimensions() {
        flatPortalGate = new UPortalPattern(slimeWatery, Blocks.DIRT, flatPortal);
        BlockUtils.OPEN_PORTAL_MANAGER.register(flatPortalGate);

        int id = DimensionManager.getNextFreeDimId();
        dimensionFlat = DimensionType.register("Flat", "_flat", id, WorldProviderFlat.class, false);
        DimensionManager.registerDimension(id, dimensionFlat);
    }

    @SideOnly(Side.CLIENT)
    public static void renderItemModels() {
        UModelLoader.setCustomModelResourceLocation(slimeWatery);

        UModelLoader.setCustomModelResourceLocation(wandSlime);
    }

    public static void createFiles() {
        File project = new File("../1.11.2-FlatDimension");

        LanguageResourceManager language = new LanguageResourceManager();

        language.register(LanguageResourceManager.EN_US, flatPortal, "Flat Portal");
        language.register(LanguageResourceManager.JA_JP, flatPortal, "フラットポータル");
        language.register(LanguageResourceManager.EN_US, slimeWatery, "Watery Slime");
        language.register(LanguageResourceManager.JA_JP, slimeWatery, "水気の多いスライム");

        language.register(LanguageResourceManager.EN_US, wandSlime, "Slimy Wand");
        language.register(LanguageResourceManager.JA_JP, wandSlime, "スライムの杖");

        ULanguageCreator.createLanguage(project, MOD_ID, language);

        Map<String, String> map = new HashMap<>();
        map.put("particle", "blocks/slime");
        map.put("wool", "blocks/slime");
        UModelCreator.createBlockstateJson(project, slimeWatery, BlockmodelJsonFactory.createBlockmodelJson("block/carpet", map));

        UModelCreator.createItemJson(project, wandSlime, ItemmodelJsonFactory.ItemmodelParent.HANDHELD);
    }
}
