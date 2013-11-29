package vswe.stevescarts.Arcade;

import net.minecraft.client.Minecraft;
import vswe.stevescarts.Arcade.TrackOrientation.DIRECTION;


public class TrackEnderHandler extends Track {
	
	private boolean isSpawner;
	public TrackEnderHandler(int x, int y, TrackOrientation orientation, boolean isSpawner) {
		super(x, y, orientation);
		this.isSpawner = isSpawner;
	}
	
	
	@Override
	public void travel(ArcadeTracks game, Cart cart) {
		if (isSpawner) {
			game.getEnderman().setAlive(true);
			game.getEnderman().setDirection(DIRECTION.RIGHT);
			game.getEnderman().setX(cart.getX() + 5);
			game.getEnderman().setY(cart.getY());
			Minecraft.getMinecraft().sndManager.playSoundFX("mob.endermen.portal", 1, 1);		 
		}else if (game.getEnderman().isAlive()){
			game.getEnderman().setAlive(false);
			Minecraft.getMinecraft().sndManager.playSoundFX("mob.endermen.portal", 1, 1);		 
		}
		
	}		
	
	@Override
	public Track copy() {
		return new TrackEnderHandler(getX(), getY(), getOrientation(), isSpawner);
	}		
	
}
