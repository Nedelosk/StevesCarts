package vswe.stevescarts.Upgrades;

import java.util.ArrayList;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.Slots.SlotCart;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public class Blueprint extends SimpleInventoryEffect {
	public Blueprint() {
		super(1, 1);
	}

	@Override
	public Class<? extends Slot> getSlot(final int i) {
		return SlotCart.class;
	}

	@Override
	public String getName() {
		return Localization.UPGRADES.BLUEPRINT.translate();
	}

	public boolean isValidForBluePrint(final TileEntityUpgrade upgrade, final ArrayList<ModuleData> modules, final ModuleData module) {
		final ItemStack blueprint = upgrade.getStackInSlot(0);
		if (blueprint == null) {
			return false;
		}
		final NBTTagCompound info = blueprint.getTagCompound();
		if (info == null) {
			return false;
		}
		final NBTTagByteArray moduleIDTag = (NBTTagByteArray) info.getTag("Modules");
		if (moduleIDTag == null) {
			return false;
		}
		final byte[] IDs = moduleIDTag.getByteArray();
		final ArrayList<ModuleData> missing = new ArrayList<ModuleData>();
		for (final byte id : IDs) {
			final ModuleData blueprintModule = ModuleData.getList().get(id);
			final int index = modules.indexOf(blueprintModule);
			if (index != -1) {
				modules.remove(index);
			} else {
				missing.add(blueprintModule);
			}
		}
		return missing.contains(module);
	}
}
