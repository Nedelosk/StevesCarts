package vswe.stevescarts.TileEntities;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Containers.ContainerBase;
import vswe.stevescarts.Containers.ContainerCargo;
import vswe.stevescarts.Containers.ContainerManager;
import vswe.stevescarts.Helpers.CargoItemSelection;
import vswe.stevescarts.Helpers.ManagerTransfer;
import vswe.stevescarts.Helpers.TransferHandler;
import vswe.stevescarts.Helpers.TransferHandler.TRANSFER_TYPE;
import vswe.stevescarts.Interfaces.GuiBase;
import vswe.stevescarts.Interfaces.GuiCargo;
import vswe.stevescarts.Slots.ISlotExplosions;
import vswe.stevescarts.Slots.SlotArrow;
import vswe.stevescarts.Slots.SlotBridge;
import vswe.stevescarts.Slots.SlotBuilder;
import vswe.stevescarts.Slots.SlotCake;
import vswe.stevescarts.Slots.SlotCargo;
import vswe.stevescarts.Slots.SlotChest;
import vswe.stevescarts.Slots.SlotFertilizer;
import vswe.stevescarts.Slots.SlotFirework;
import vswe.stevescarts.Slots.SlotFuel;
import vswe.stevescarts.Slots.SlotMilker;
import vswe.stevescarts.Slots.SlotSapling;
import vswe.stevescarts.Slots.SlotSeed;
import vswe.stevescarts.Slots.SlotTorch;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
public class TileEntityCargo extends TileEntityManager
{
    
	@SideOnly(Side.CLIENT)
	@Override
	public GuiBase getGui(InventoryPlayer inv) {
		return new GuiCargo(inv, this);
	}
	
	@Override
	public ContainerBase getContainer(InventoryPlayer inv) {
		return new ContainerCargo(inv, this);		
	}
	public TileEntityCargo()
    {
		super();
    }

	public static ArrayList<CargoItemSelection> itemSelections;

	
	public static void loadSelectionSettings() {
		itemSelections = new ArrayList<CargoItemSelection>();
		itemSelections.add(new CargoItemSelection("All slots", Slot.class, new ItemStack(StevesCarts.carts, 1, 0)));
		itemSelections.add(new CargoItemSelection("Engine", SlotFuel.class, new ItemStack(StevesCarts.modules, 1, 0)));
		itemSelections.add(new CargoItemSelection("Railers", SlotBuilder.class, new ItemStack(StevesCarts.modules, 1, 10)));
		itemSelections.add(new CargoItemSelection("Storage slots", SlotChest.class, new ItemStack(Block.chest, 1)));
		itemSelections.add(new CargoItemSelection("Torches", SlotTorch.class, new ItemStack(Block.torchWood, 1)));
		itemSelections.add(new CargoItemSelection("Explosives", ISlotExplosions.class, new ItemStack(Block.tnt, 1)));
		itemSelections.add(new CargoItemSelection("Arrows", SlotArrow.class, new ItemStack(Item.arrow, 1)));
		itemSelections.add(new CargoItemSelection("Bridge material", SlotBridge.class, new ItemStack(Block.brick, 1)));
		itemSelections.add(new CargoItemSelection("Seeds", SlotSeed.class, new ItemStack(Item.seeds, 1)));
		itemSelections.add(new CargoItemSelection("Fertilizer", SlotFertilizer.class, new ItemStack(Item.dyePowder, 1, 15)));
		itemSelections.add(new CargoItemSelection(null, null, null));
		itemSelections.add(new CargoItemSelection("Saplings", SlotSapling.class, new ItemStack(Block.sapling, 1)));	
		itemSelections.add(new CargoItemSelection("Firework", SlotFirework.class, new ItemStack(Item.firework, 1)));	
		itemSelections.add(new CargoItemSelection("Buckets (for milker)", SlotMilker.class, new ItemStack(Item.bucketEmpty, 1)));
		itemSelections.add(new CargoItemSelection("Cakes", SlotCake.class, new ItemStack(Item.cake, 1)));
	}
	
	
	@Override
    public int getSizeInventory()
    {
        return 60;
    }

	@Override
    public String getInvName()
    {
        return "container.cargomanager";
    }

    public int target[] = new int[] {0, 0, 0, 0};
	public ArrayList<SlotCargo> cargoSlots;
	public int lastLayout = -1;
	
	@Override
	protected void updateLayout() {
		if (cargoSlots != null && lastLayout != layoutType) {
			for (SlotCargo slot : cargoSlots) {
				slot.updatePosition();
			}
			lastLayout = layoutType;
		}		
	}	
	
	@Override
	protected boolean isTargetValid(ManagerTransfer transfer) {
		return target[transfer.getSetting()] >= 0 && target[transfer.getSetting()] < itemSelections.size();
	}
	
	@Override
	protected void receiveClickData(int packetid, int id, int dif) {
		if (packetid == 1) {
			target[id] += dif;

			if (target[id] >= itemSelections.size())
			{
				target[id] = 0;
			}
			else if (target[id] < 0)
			{
				target[id] = itemSelections.size() - 1;
			}

			if (color[id] - 1 == getSide())
			{
				reset();
			}
			
			if (itemSelections.get(target[id]).getValidSlot() == null && dif != 0) {
				receiveClickData(packetid, id, dif);
			}
		}				
	}
	

	@Override
	public void checkGuiData(ContainerManager conManager, ICrafting crafting, boolean isNew) {
		super.checkGuiData(conManager, crafting, isNew);
	
		ContainerCargo con = (ContainerCargo)conManager;
	
		short targetShort = (short)0;
		for (int i = 0; i < 4; i++) {
			targetShort |= (target[i] & 15) << (i*4);
		}
		if (isNew || con.lastTarget != targetShort) {
			updateGuiData(con, crafting, 2, targetShort);
			con.lastTarget = targetShort;
		}	
	}
	
	@Override
	public void receiveGuiData(int id, short data) {
		if(id == 2) {
			for (int i = 0; i < 4; i++) {
				target[i] = (data & (15 << (i*4))) >> (i*4);
			}
		}else{
			super.receiveGuiData(id, data);
		}
	}

    public int getAmount(int id)
    {
        int val = getAmountId(id);

        switch (val)
        {
            case 1:
                return 1;

            case 2:
                return 3;

            case 3:
                return 8;

            case 4:
                return 16;

            case 5:
                return 32;

            case 6:
                return 64;

            case 7:
                return 1;

            case 8:
                return 2;

            case 9:
                return 3;

            case 10:
                return 5;

            default:
                return 0;
        }
    }

    //0 - MAX
    //1 - Items
    //2 - Stacks
    public int getAmountType(int id)
    {
        int val = getAmountId(id);

        if (val == 0)
        {
            return 0;
        }
        else if (val <= 6)
        {
            return 1;
        }
        else
        {
            return 2;
        }
    }
	
	@Override
	public int getAmountCount() {
		return 11;
	}
	

	@Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        setWorkload(nbttagcompound.getByte("workload"));		
        for (int i = 0; i < 4; i++)
        {
            target[i] = nbttagcompound.getByte("target" + i);
        }
		
	}
	
	@Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setByte("workload", (byte)getWorkload());
        for (int i = 0; i < 4; i++)
        {
            nbttagcompound.setByte("target" + i, (byte)target[i]);
        }		
	}
	
	@Override
	protected boolean doTransfer(ManagerTransfer transfer) {
		java.lang.Class slotCart = itemSelections.get(target[transfer.getSetting()]).getValidSlot();
		if (slotCart == null) {
			transfer.setLowestSetting(transfer.getSetting() + 1);
			return true;
		}
		java.lang.Class slotCargo = SlotCargo.class;


		IInventory fromInv;
		Container fromCont;
		java.lang.Class fromValid;
		IInventory toInv;
		Container toCont;
		java.lang.Class toValid;

		if (toCart[transfer.getSetting()])
		{
			fromInv = this;
			fromCont = new ContainerCargo(null, this);
			fromValid = slotCargo;
			toInv = transfer.getCart();
			toCont = transfer.getCart().getCon(null);
			toValid = slotCart;
		}
		else
		{
			fromInv = transfer.getCart();
			fromCont = transfer.getCart().getCon(null);
			fromValid = slotCart;
			toInv = this;
			toCont = new ContainerCargo(null, this);
			toValid = slotCargo;
		}

		latestTransferToBeUsed = transfer;
		for (int i = 0; i < fromInv.getSizeInventory(); i++)
		{
			if (TransferHandler.isSlotOfType(fromCont.getSlot(i),fromValid) && fromInv.getStackInSlot(i) != null)
			{
				ItemStack iStack = fromInv.getStackInSlot(i);
				int stacksize = iStack.stackSize;
				int maxNumber;

				if (getAmountType(transfer.getSetting()) == 1)
				{
					maxNumber = getAmount(transfer.getSetting()) - transfer.getWorkload();
				}
				else
				{
					maxNumber = -1;
				}
				
				TransferHandler.TransferItem(iStack, toInv, toCont, toValid, maxNumber, TRANSFER_TYPE.MANAGER);

				if (iStack.stackSize != stacksize)
				{
					if (getAmountType(transfer.getSetting()) == 1)
					{
						transfer.setWorkload(transfer.getWorkload() + stacksize - iStack.stackSize);
					}
					else if (getAmountType(transfer.getSetting()) == 2)
					{
						transfer.setWorkload(transfer.getWorkload() + 1);
					}

					onInventoryChanged();
					transfer.getCart().onInventoryChanged();

					if (iStack.stackSize == 0)
					{
						fromInv.setInventorySlotContents(i, null);
					}

					if (transfer.getWorkload() >= getAmount(transfer.getSetting()) && getAmountType(transfer.getSetting()) != 0)
					{
						transfer.setLowestSetting(transfer.getSetting() + 1); //this is not available anymore
					}

					return true;
				}
			}
		}
		return false;
	}
	
	@Override
    public boolean isItemValidForSlot(int slotId, ItemStack item)
    {	
		return true;
	}

	private ManagerTransfer latestTransferToBeUsed;
	public ManagerTransfer getCurrentTransferForSlots() {
		return latestTransferToBeUsed;
	}	
	
}
