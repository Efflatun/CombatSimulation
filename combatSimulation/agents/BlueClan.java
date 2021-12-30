package combatSimulation.agents;


import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class BlueClan extends SimpleAgent {
	public BlueClan (double energy){
		this.setEnergy(energy);               // assign the offspring energy
		this.setHeading(RandomHelper.nextDoubleFromTo(0,360));   // randomize the heading from 0-360 degrees
	}
	// This constructor is used to create initial clans from the context creator
	public BlueClan(){
		// Get the value of the Clan gain from food from the environment parameters
		Parameters p = RunEnvironment.getInstance().getParameters();
		double gain = (Double)p.getValue("clangainfromfood");

		this.setEnergy(RandomHelper.nextDouble() * 2 * gain);    // set the initial energy
		this.setHeading(RandomHelper.nextDoubleFromTo(0,360));   // randomize the heading from 0-360 degrees
	}
	public void step() {
		// Get the context in which the Clan resides.
		Context context = ContextUtils.getContext(this);

		// Move the Clan
		move();

		// Reduce the Clan's energy by one unit
		this.setEnergy(this.getEnergy() - 1);

		
		// Get the patch grid from the context
		Grid patch = (Grid) context.getProjection("Simple Grid");

		// Get the agnet's current patch
		GridPoint point = patch.getLocation(this);

		int x = point.getX();   // The x-ccordinate of the sheep's current patch
		int y = point.getY();   // The y-ccordinate of the sheep's current patch

		// Get the clan gain from foodsupply from the user parameters
		Parameters p = RunEnvironment.getInstance().getParameters();
		//sheep=clan
		double foodgain = (Double)p.getValue("clangainfromfood");
		// Find other clans at the patch and eat it if one exists
		Parameters p1 = RunEnvironment.getInstance().getParameters();
		double clashgain = (Double)p1.getValue("clangainfromfight");
	
		GreenClan greenclan = null;
		for (Object o: patch.getObjectsAt(x,y)){
		if (o instanceof GreenClan)
		greenclan = (GreenClan)o;
		}
		// If there is a greenclan on the patch then eat it
		if (greenclan != null && greenclan.getEnergy()<this.getEnergy()){
		greenclan.die();              // kill the greenclan
		this.setEnergy(this.getEnergy() + clashgain);   // increment the wolf's energy
		}
		
		RedClan redclan= null;
		for (Object o: patch.getObjectsAt(x,y)){
		if (o instanceof RedClan)
		redclan = (RedClan)o;
		}
		// If there is a redclan on the patch then eat it
		if (redclan != null && redclan.getEnergy()<this.getEnergy()){
		redclan.die();              // kill the sheep
		this.setEnergy(this.getEnergy() + clashgain);   // increment the wolf's energy
		}
		// Find the FoodSupply at the patch and eat it if it is alive
		FoodSupply fs = null;                   
		for (Object o : patch.getObjectsAt(x,y)){
			if (o instanceof FoodSupply)
				fs = (FoodSupply)o;
		}
		// If there is a FoodSupply and it is alive, then eat it
		if (fs != null && fs.isAlive()){
			fs.consume();                    // eat the grass
			this.setEnergy(this.getEnergy() + foodgain);  // increment the sheep's energy
		}

		// Reproduce the Clan 
		// Get the reproduction rate from the user parameters
		double rate = (Double)p.getValue("Clanreproduce");

		//	Spawn a new Clan if a random draw on [0,100) < reproduction rate
		if (100 * RandomHelper.nextDouble() < rate){
			this.setEnergy(this.getEnergy() / 2);      // divide the parent's energy in half
			BlueClan blueClan = new BlueClan(this.getEnergy());    // create a new Clan offspring and assing its energy
			context.add(blueClan);	                       // add the offspring to the root context
		}

		// Death
		if (this.getEnergy() < 0)
			die();
	}
	public int isClan() {
		return 1;
	}
}
