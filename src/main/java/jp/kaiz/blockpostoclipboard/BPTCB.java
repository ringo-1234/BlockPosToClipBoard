package jp.kaiz.blockpostoclipboard;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = BPTCB.MODID, version = BPTCB.VERSION, name = BPTCB.NAME)
@Mod.EventBusSubscriber(modid = BPTCB.MODID)
public class BPTCB extends Item {

    public static final String MODID = "bptcb";
    public static final String NAME = "BlockPosToClipBoard";
    public static final String VERSION = "1.0";

    public static final Item INSTANCE = new BPTCB();

    private boolean commaMode;

    public BPTCB() {
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setRegistryName("item_bptcb");
        this.setUnlocalizedName(BPTCB.MODID + ".item_bptcb");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (worldIn.isRemote) {
            this.commaMode ^= true;
            playerIn.sendMessage(new TextComponentString(String.format("CommaMode=%s", this.commaMode)));
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            String posStr = String.format(this.commaMode ? "%s, %s, %s" : "%s %s %s", pos.getX(), pos.getY(), pos.getZ());
            player.sendMessage(new TextComponentString(String.format("Copied! (%s)", posStr)));
            GuiScreen.setClipboardString(posStr);
        }
        return EnumActionResult.SUCCESS;
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(INSTANCE);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(INSTANCE, 0, new ModelResourceLocation(INSTANCE.getRegistryName(), "inventory"));
    }
}
