package combatSimulation.styles;

import java.awt.Color;

import combatSimulation.agents.BlueClan;
import combatSimulation.agents.RedClan;
import combatSimulation.agents.GreenClan;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;


public class AgentStyle2D extends DefaultStyleOGL2D {

	@Override
	public Color getColor(Object o){
		
		if (o instanceof BlueClan)
			return Color.BLUE;
		
		else if (o instanceof RedClan)
			return Color.RED;
		else if (o instanceof GreenClan)
			return Color.GREEN;
		return null;
	}
	
	@Override
	public float getScale(Object o) {
		if (o instanceof BlueClan)
			return 2f;
		
		else if (o instanceof RedClan)
			return 2f;
		else if (o instanceof GreenClan)
			return 2f;
		return 1f;
	}
}