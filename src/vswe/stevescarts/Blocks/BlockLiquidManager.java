package vswe.stevescarts.Blocks;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.TileEntities.TileEntityLiquid;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.Icon;
import net.minecraft.client.renderer.texture.IconRegister;
public class BlockLiquidManager extends BlockContainer
{


    public BlockLiquidManager(int i)
    {
        super(i, Material.rock);
		setCreativeTab(StevesCarts.tabsSC2Blocks);	
    }

	
	private Icon topIcon;
	private Icon botIcon;
	private Icon redIcon;
	private Icon blueIcon;
	private Icon greenIcon;
	private Icon yellowIcon;
	
    @SideOnly(Side.CLIENT)
	@Override
    public Icon getIcon(int side, int meta)
    {
        if (side == 0) {
			return botIcon;
		}else if(side == 1) {
			return topIcon;
		}else if(side == 2){
			return yellowIcon;
		}else if(side == 3){
			return blueIcon;
		}else if(side == 4){
			return greenIcon;
		}else{
			return redIcon;
		}
    }
	
    @SideOnly(Side.CLIENT)
	@Override
    public void registerIcons(IconRegister register)
    {
        topIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "liquid_manager" + "_top");
		botIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "liquid_manager" + "_bot");
		redIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "liquid_manager" + "_red");
		blueIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "liquid_manager" + "_blue");
		greenIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "liquid_manager" + "_green");
		yellowIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "liquid_manager" + "_yellow");
    }	

	@Override
   public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntityLiquid var7 = (TileEntityLiquid)par1World.getBlockTileEntity(par2, par3, par4);

        if (var7 != null)
        {
            for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
            {
                ItemStack var9 = var7.getStackInSlot(var8);

                if (var9 != null)
                {
                    float var10 = par1World.rand.nextFloat() * 0.8F + 0.1F;
                    float var11 = par1World.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem var14;

                    for (float var12 = par1World.rand.nextFloat() * 0.8F + 0.1F; var9.stackSize > 0; par1World.spawnEntityInWorld(var14))
                    {
                        int var13 = par1World.rand.nextInt(21) + 10;

                        if (var13 > var9.stackSize)
                        {
                            var13 = var9.stackSize;
                        }

                        var9.stackSize -= var13;
                        var14 = new EntityItem(par1World, (double)((float)par2 + var10), (double)((float)par3 + var11), (double)((float)par4 + var12), new ItemStack(var9.itemID, var13, var9.getItemDamage()));
                        float var15 = 0.05F;
                        var14.motionX = (double)((float)par1World.rand.nextGaussian() * var15);
                        var14.motionY = (double)((float)par1World.rand.nextGaussian() * var15 + 0.2F);
                        var14.motionZ = (double)((float)par1World.rand.nextGaussian() * var15);

                        if (var9.hasTagCompound())
                        {
                            var14.getEntityItem().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                        }
                    }
                }
            }
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

	

	

	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
		if (entityplayer.isSneaking()) {
			return false;
		}


        if (world.isRemote)
        {
            return true;
        }


		FMLNetworkHandler.openGui(entityplayer, StevesCarts.instance, 2, world, i, j, k);
        

        return true;
    }

	@Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityLiquid();
    }
}
