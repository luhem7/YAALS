package viewObjects;

import utilObjects.Settings;
import modelObjects.ModelObject;

public class WallView extends ViewObject {

	public WallView(int numVertices, float[] color, ModelObject myModel,
			Camera myCam) {
		super(numVertices, color, myModel, myCam);
	}

	@Override
	protected void generateCircumVertices() {
		this.renderAsSolid = false;
		for(int i=0; i<4; i++){
			circumVertices[i] = new Vertex();
			circumVertices[i].setColor(color[0], color[1], color[2]);
		}
		circumVertices[0].setPosition( -Settings.WALL_WIDTH/2, -Settings.WALL_HEIGHT/2, 0);
		circumVertices[1].setPosition( -Settings.WALL_WIDTH/2, Settings.WALL_HEIGHT/2, 0);
		circumVertices[2].setPosition( Settings.WALL_WIDTH/2, Settings.WALL_HEIGHT/2, 0);
		circumVertices[3].setPosition( Settings.WALL_WIDTH/2, -Settings.WALL_HEIGHT/2, 0);
	}

}
