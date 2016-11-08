package vswe.stevescarts.Slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Helpers.TransferHandler;
import vswe.stevescarts.Modules.Workers.Tools.ModuleTool;

public class SlotRepair extends SlotBase implements ISpecialItemTransferValidator {
	private ModuleTool tool;

	public SlotRepair(final ModuleTool tool, final IInventory iinventory, final int i, final int j, final int k) {
		super(iinventory, i, j, k);
		this.tool = tool;
	}

	@Override
	public boolean isItemValidForTransfer(final ItemStack item, final TransferHandler.TRANSFER_TYPE type) {
		return false;
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		return this.tool.isValidRepairMaterial(itemstack);
	}
}
