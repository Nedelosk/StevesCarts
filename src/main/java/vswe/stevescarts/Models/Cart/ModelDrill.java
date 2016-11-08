package vswe.stevescarts.Models.Cart;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrill;

@SideOnly(Side.CLIENT)
public class ModelDrill extends ModelCartbase {
	private ModelRenderer drillAnchor;
	private ResourceLocation resource;

	@Override
	public ResourceLocation getResource(final ModuleBase module) {
		return this.resource;
	}

	@Override
	protected int getTextureWidth() {
		return 32;
	}

	@Override
	protected int getTextureHeight() {
		return 32;
	}

	public ModelDrill(final ResourceLocation resource) {
		this.resource = resource;
		this.AddRenderer(this.drillAnchor = new ModelRenderer(this));
		this.drillAnchor.rotateAngleY = 4.712389f;
		int srcY = 0;
		for (int i = 0; i < 6; ++i) {
			final ModelRenderer drill = this.fixSize(new ModelRenderer(this, 0, srcY));
			this.drillAnchor.addChild(drill);
			drill.addBox(-3.0f + i * 0.5f, -3.0f + i * 0.5f, i, 6 - i, 6 - i, 1, 0.0f);
			drill.setRotationPoint(0.0f, 0.0f, 11.0f);
			srcY += 7 - i;
		}
	}

	@Override
	public void applyEffects(final ModuleBase module, final float yaw, final float pitch, final float roll) {
		for (final Object drill : this.drillAnchor.childModels) {
			((ModelRenderer) drill).rotateAngleZ = ((module == null) ? 0.0f : ((ModuleDrill) module).getDrillRotation());
		}
	}
}
