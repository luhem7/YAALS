package viewObjects;

/**
 * This class contains all the information necessary for a single vertex.
 * This vertex stores information using floats. It also has 4 attributes
 * each for location and color.
 * @author mehul
 *
 */
public class Vertex {
	
	//Vertex data
	private float[] position = new float[] {0f, 0f, 0f, 1f};
	private float[] color = new float[] {1f, 1f, 1f, 1f};
	
	//The number of elements in a vertex
	public static final int elementCount = 8;
	//The number of bytes an element has
	public static final int elementBytes = 4;
	//The size of a vertex in bytes:
	public static final int sizeInBytes = elementBytes*elementCount;
	//Number of elements used for position
	public static final int positionElementCount = 4;
	//Number of elements used for color
	public static final int colorElementCount = 4;
	
	public Vertex(){
		//Uses defaults as defined above
	}
	
	public Vertex(float x, float y, float z, float w,
			float r, float g, float b, float a){
		this.position = new float[] {x, y, z, w};
		this.color = new float[] {r, g, b, a};
	}

	public float[] getPosition() {
		return position;
	}

	public void setPosition(float x, float y, float z) {
		this.setPosition(x, y, z, 1f);
	}
	
	public void setPosition(float x, float y, float z, float w) {
		this.position = new float[] {x, y, z, w};
	}

	public float[] getColor() {
		return color;
	}
	
	public void setColor(float r, float g, float b) {
		this.setColor(r, g, b, 1f);
	}
	
	public void setColor(float[] newColor) {
		this.setColor(newColor[0], newColor[1], newColor[2], 1f);
	}

	public void setColor(float r, float g, float b, float a) {
		this.color = new float[] {r, g, b, a};
	}
}
