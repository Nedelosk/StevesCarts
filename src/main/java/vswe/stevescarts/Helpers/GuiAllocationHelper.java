package vswe.stevescarts.Helpers;

import java.util.ArrayList;

import vswe.stevescarts.Modules.ModuleBase;

public class GuiAllocationHelper {
	public int width;
	public int maxHeight;
	public ArrayList<ModuleBase> modules;

	public GuiAllocationHelper() {
		this.modules = new ArrayList<ModuleBase>();
	}
}
