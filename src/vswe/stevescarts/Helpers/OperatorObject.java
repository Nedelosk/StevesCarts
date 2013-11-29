package vswe.stevescarts.Helpers;

import java.util.Collection;
import java.util.HashMap;

import net.minecraft.tileentity.TileEntity;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.TileEntities.TileEntityDetector;

import com.jcraft.jorbis.Block;

public class OperatorObject {

	private static HashMap<Byte, OperatorObject> allOperators;
		
	static {
		allOperators = new HashMap<Byte, OperatorObject>();
		HashMap<Byte, OperatorObject> operators = new HashMap<Byte, OperatorObject>();
		
		new OperatorObject(operators, 0, "OUTPUT", 1) {
			public boolean inTab() {
				return false;
			}
			
			public boolean evaluate(TileEntityDetector detector, MinecartModular cart, int depth,  LogicObject A, LogicObject B) {
				return A.evaluateLogicTree(detector, cart, depth); 
			}			
		};
		
		new OperatorObject(operators, 1, "AND", 2) {
			public boolean evaluate(TileEntityDetector detector, MinecartModular cart, int depth,  LogicObject A, LogicObject B) {
				return A.evaluateLogicTree(detector, cart, depth) && B.evaluateLogicTree(detector, cart, depth); 
			}			
		};
		
		new OperatorObject(operators, 2, "OR", 2) {
			public boolean evaluate(TileEntityDetector detector, MinecartModular cart, int depth,  LogicObject A, LogicObject B) {
				return A.evaluateLogicTree(detector, cart, depth) || B.evaluateLogicTree(detector, cart, depth); 
			}	
		};
		new OperatorObject(operators, 3, "NOT", 1) {
			public boolean isChildValid(OperatorObject child) {
				return getID() != child.ID;
			}
			
			public boolean evaluate(TileEntityDetector detector, MinecartModular cart, int depth,  LogicObject A, LogicObject B) {
				return !A.evaluateLogicTree(detector, cart, depth); 
			}		
		};
		
		new OperatorObject(operators, 4, "XOR", 2) {
			public boolean evaluate(TileEntityDetector detector, MinecartModular cart, int depth,  LogicObject A, LogicObject B) {
				return A.evaluateLogicTree(detector, cart, depth) != B.evaluateLogicTree(detector, cart, depth); 
			}		
		};
		new OperatorObjectRedirector(operators, 5, "Top Unit", 0, 1, 0);
		new OperatorObjectRedirector(operators, 6, "Bottom Unit", 0, -1, 0);
		new OperatorObjectRedirector(operators, 7, "North Unit", 0, 0, -1);
		new OperatorObjectRedirector(operators, 8, "West Unit", -1, 0, 0);
		new OperatorObjectRedirector(operators, 9, "South Unit", 0, 0, 1);
		new OperatorObjectRedirector(operators, 10, "East Unit", 1, 0, 0);

		//Note that IDs are also used by the specific types, the next ID here shouldn't be 11, that one is already in use.
		
		for (DetectorType type : DetectorType.values()) {
			type.initOperators(new  HashMap<Byte, OperatorObject>(operators));
		}
	}
	
	
	public static Collection<OperatorObject> getOperatorList(int meta) {
		return DetectorType.getTypeFromMeta(meta).getOperators().values();
	}
	
	public static HashMap<Byte, OperatorObject> getAllOperators() {
		return allOperators;
	}	
	
	public static class OperatorObjectRedirector extends OperatorObject {
		private int x;
		private int y;
		private int z;
		public OperatorObjectRedirector(HashMap<Byte, OperatorObject> operators, int ID, String name, int x, int y, int z) {
			super(operators, ID, name, 0);
			
			this.x = x;
			this.y = y;
			this.z = z;			
		}
		
		@Override
		public boolean evaluate(TileEntityDetector detector, MinecartModular cart, int depth, LogicObject A, LogicObject B) {
			int x = this.x + detector.xCoord;
			int y = this.y + detector.yCoord;
			int z = this.z + detector.zCoord;
			
			TileEntity tileentity = detector.worldObj.getBlockTileEntity(x, y, z);
			if (tileentity != null && tileentity instanceof TileEntityDetector) {
				return ((TileEntityDetector)tileentity).evaluate(cart, depth);
			}else{
				return false;
			}
		}
	}
	
	public static class OperatorObjectRedstone extends OperatorObject {
		private int x;
		private int y;
		private int z;
		public OperatorObjectRedstone(HashMap<Byte, OperatorObject> operators, int ID, String name, int x, int y, int z) {
			super(operators, ID, name, 0);
			
			this.x = x;
			this.y = y;
			this.z = z;			
		}
		
		@Override
		public boolean evaluate(TileEntityDetector detector, MinecartModular cart, int depth, LogicObject A, LogicObject B) {
			int x = this.x + detector.xCoord;
			int y = this.y + detector.yCoord;
			int z = this.z + detector.zCoord;
			
			
			if (this.x == 0 && this.y == 0 && this.z == 0) {
				return detector.worldObj.isBlockIndirectlyGettingPowered(x, y, z);
			}else{
				int direction;
				if (this.y > 0) {
					direction = 0;
				}else if(this.y < 0) {
					direction = 1;
				}else if(this.x > 0) {
					direction = 4;
				}else if(this.x < 0) {
					direction = 5;
				}else if(this.z > 0) {
					direction = 2;
				}else{
					direction = 3;
				}
				
		        return detector.worldObj.getIndirectPowerLevelTo(x, y, z, direction) > 0;				
			}
		}
	}	
	
	private byte ID;
	private String name;
	private int childs;
	
	
	public OperatorObject(HashMap<Byte, OperatorObject> operators, int ID, String name, int childs) {
		this.ID = (byte)ID;
		this.name = name;
		this.childs = childs;
		
		operators.put(this.ID, this);
		allOperators.put(this.ID, this);
	}
	
	public byte getID() {
		return ID;
	}
	
	public String getName() {
		return name;
	}
	
	public int getChildCount() {
		return childs;
	}
	
	public boolean inTab() {
		return true;
	}
	
	public boolean isChildValid(OperatorObject child) {
		return true;
	}
	
	public boolean evaluate(TileEntityDetector detector, MinecartModular cart, int depth, LogicObject A, LogicObject B) {
		return false;
	}


	
}
