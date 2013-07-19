package viewObjects;

import utilObjects.Settings;
import modelObjects.ModelObject;

public class CreatureView extends ViewObject {

	public CreatureView(ModelObject myModel, Camera myCam) {
		super(Settings.CREATURE_VERTICES, Settings.CREATURE_BODY_COLOR, myModel, myCam);
	}

	@Override
	protected void generateCircumVertices() {
		float radius = Settings.CREATURE_VIEW_SIZE;
		
		double theta = 2 * 3.1415926 / (numVertices);
		double cos_precalc = Math.cos(theta);//precalculate the sine and cosine
		double sine_precalc = Math.sin(theta);//precalculate the sine and cosine
		double t;

		double x = radius;//we start at angle = 0 
		double y = 0; 
	    
		for(int i = 0; i < numVertices; i++) {
			circumVertices[i] = new Vertex();
			circumVertices[i].setColor(Settings.CREATURE_BODY_COLOR);
			circumVertices[i].setPosition( (float)x, (float)y, 0);
	        
			//apply the rotation matrix
			t = x;
			x = cos_precalc * x - sine_precalc * y;
			y = sine_precalc * t + cos_precalc * y;
		}
		
		//Setting the leading vertex to a different color
		circumVertices[0].setColor(Settings.CREATURE_HEAD_COLOR);
		
		this.renderAsSolid = false;
	}

}
