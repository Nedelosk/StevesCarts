package vswe.stevesvehicles.module.common.addon.mobdetector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityVillager;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.Localization;

public class ModuleVillager extends ModuleEntityDetector {
	public ModuleVillager(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	public String getName() {
		return Localization.MODULES.ADDONS.DETECTOR_VILLAGERS.translate();
	}
	public boolean isValidTarget(Entity target) {
		return
		(
			target instanceof EntityGolem
			||
			target instanceof EntityVillager
		)
		;
	}
}