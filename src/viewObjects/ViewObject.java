package viewObjects;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.jbox2d.common.Vec2;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import utilObjects.DisplayUtils;
import modelObjects.ModelObject;

/**
 * This is a general kind of view Object. Represents common functionality that all things
 * displayed on the screen should have.
 * @author mehul
 *
 */
public abstract class ViewObject {
	protected int numVertices = 3; //Number of vertices this object will have
	protected Vertex[] circumVertices = null; //The Vertex objects for the circumference points
	private Vertex centerVertex; //The point at the middle of this object
	protected float[] color = new float[3];
	
	public ModelObject myModel = null; //The pointer to this object's model
	private Camera myCam = null;
	
	//Whether we want to render the polygon as a solid polygon or an outline
	public boolean renderAsSolid = true;
	
	private int positionPointer = 0; // The memory location for the position of this poly in GPU
	private int anglePointer = 0; // The memory location for angle of this poly in GPU
	
	private int camZoomPointer = 0; //The memory location for Camera Zoom in GPU
	private int camPosPointer = 0; //The memory location for Camera Position in GPU
	
	//GPU object bindings
	private int vaoID = 0; // VAO id for this polygon
	private int vboID = 0; //VBO for this polygon
	private int vboiID = 0; //The VBO for the indices Array
	
	//Shader variables
	private int vsID = 0; //Vertex Shader ID
	private int fsID = 0; //Fragment Shader ID
	private int pID = 0; //Program Shader ID
	
	public ViewObject(int numVertices, float[] color, ModelObject myModel, Camera myCam){
		this.numVertices = numVertices;
		this.color = color;
		this.myModel = myModel;
		this.myCam = myCam;
		
		//Make center vertex object
		centerVertex = new Vertex(0, 0, 0, 1,
				color[0], color[1], color[2], 1);
		circumVertices = new Vertex[numVertices];
		//Make circumference vertex objects
		generateCircumVertices();
		setupGPUBindings();
	}
	
	/**
	 * Generates the array of Vertex objects that defines the outline of this object
	 */
	protected abstract void generateCircumVertices();
	
	
	public void setupGPUBindings(){
		//Create the buffer objects
		setupBuffers();
		//Setup Shaders
		setupShaders();
	}
	
	
	/**
	 * Loads the vertex information into GL memory and sets the buffer ids for use
	 * later
	 */
	protected void setupBuffers(){
		
		//Make VerticesBuffer
		/*
		 * If we want to render the polygon as a solid, we include the center vertex
		 * at the beginning; else, we omit it entirely 
		 */
		FloatBuffer verticesBuffer;
		if(renderAsSolid) {
			verticesBuffer = BufferUtils.createFloatBuffer(
					(numVertices+1) * Vertex.elementCount);
			verticesBuffer.put(centerVertex.getPosition());
			verticesBuffer.put(centerVertex.getColor());
		} else {
			verticesBuffer = BufferUtils.createFloatBuffer(
					(numVertices) * Vertex.elementCount);
		}
		for(Vertex v : circumVertices){ // Buffer the rest of the vertices
			verticesBuffer.put(v.getPosition());
			verticesBuffer.put(v.getColor());
		}
		verticesBuffer.flip();
		
		//Making indices buffer
		byte[] indices;
		ByteBuffer indicesBuffer;
		if(renderAsSolid){
			indicesBuffer = BufferUtils.createByteBuffer(numVertices + 2);
			indices = new byte[numVertices + 2];
			for(byte i=0; i<=numVertices; i++)
				indices[i] = i;
			indices[numVertices+1] = 1;
		} else {
			indices = new byte[numVertices];
			indicesBuffer = BufferUtils.createByteBuffer(numVertices);
			for(byte i=0; i<numVertices; i++)
				indices[i] = i;
		}
		indicesBuffer.put(indices);
		indicesBuffer.flip();
		
		//Create a new Vertex Array Object (VAO) in memory and select it (bind it)
		vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		
		//Create a new Vertex Buffer Object in memory and select it (bind it)
		//We are creating it using GL_STREAM_DRAW to render it later
		vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STREAM_DRAW);
		
		//Put the positions in attribute list 0
		GL20.glVertexAttribPointer(0, Vertex.positionElementCount, GL11.GL_FLOAT, false, Vertex.sizeInBytes, 0);
		
		//Put the colors in attribute list 1
		GL20.glVertexAttribPointer(1, Vertex.colorElementCount, GL11.GL_FLOAT, false, Vertex.sizeInBytes, 
				Vertex.elementBytes * Vertex.positionElementCount);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // Unbind VBO
		GL30.glBindVertexArray(0); // Unbind VAO
		
		//Create a new VBO for the indices and select it (bind) - INDICES
		vboiID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0); // Unbind VBO
		
		DisplayUtils.exitOnGLError("Generating polygon buffers");
	}
	
	
	/**
	 * Loads the shader program into memory
	 */
	private void setupShaders(){
		//***Loading Shaders information into memory
		//Load the vertex shader
		vsID = DisplayUtils.loadShader("shaderData/camVertex.glsl", GL20.GL_VERTEX_SHADER);
		//Load the fragment shader
		fsID = DisplayUtils.loadShader("shaderData/fragment.glsl", GL20.GL_FRAGMENT_SHADER);
		
		//Create a new shader program that links both shaders
		pID = GL20.glCreateProgram();
		GL20.glAttachShader(pID, vsID);
		GL20.glAttachShader(pID, fsID);
		GL20.glLinkProgram(pID);
		
		//Position information will be attribute 0
		GL20.glBindAttribLocation(pID, 0, "in_Position");
		//Color information will be attribute 1
		GL20.glBindAttribLocation(pID, 1, "in_Color");
		
		//Setting up uniform variable pointers for the Polygon
		anglePointer = GL20.glGetUniformLocation(pID, "angle");
		positionPointer = GL20.glGetUniformLocation(pID, "position");
		
		//Setting up uniform variable pointers for the Camera state
		camZoomPointer = GL20.glGetUniformLocation(pID, "camZoom");
		camPosPointer = GL20.glGetUniformLocation(pID, "camPosition");
		
		//Validating the program
		GL20.glValidateProgram(pID);
		
		DisplayUtils.exitOnGLError("Setting up Shaders");
	}
	
	/**
	 * Renders this polygon.
	 */
	public synchronized void renderCycle(){
		float angle = myModel.getAngle();
		Vec2 position = myModel.getPosition();
		float[] camPos = myCam.getPosition(); 
		
		GL20.glUseProgram(pID);
		
		/* We are going to apply the camera transformations where ever we
		 * supply them to the uniform values in the GPU
		 */
		
		//rotating the vertices in GPU
		GL20.glUniform1f(anglePointer, angle);DisplayUtils.exitOnGLError("Rendering Polygon 0");
		//setting the position in GPU
		GL20.glUniform2f(positionPointer, position.x, position.y);DisplayUtils.exitOnGLError("Rendering Polygon 0.5");
		//setting the camera zoom in GPU
		GL20.glUniform1f(camZoomPointer, myCam.getZoom());DisplayUtils.exitOnGLError("Rendering Polygon 1");
		//setting the camera position in GPU
		GL20.glUniform2f(camPosPointer, camPos[0], camPos[1]);
		
		//Bind to the VAO that has all the information about the vertices
		GL30.glBindVertexArray(vaoID);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);

		//Bind to the index VBO that has all the information about the order of the vertices
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiID); 
		
		//Draw the vertices
		if(renderAsSolid)
			GL11.glDrawElements(GL11.GL_TRIANGLE_FAN, numVertices+2, GL11.GL_UNSIGNED_BYTE, 0);
		else
			GL11.glDrawElements(GL11.GL_LINE_LOOP, numVertices, GL11.GL_UNSIGNED_BYTE, 0);
		
		//Put everything back to default
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		GL20.glUseProgram(0);
		
		DisplayUtils.exitOnGLError("Rendering Polygon");
	}
	
	
	/**
	 * This is the tear down function for this object. Clears up Opengl buffers etc
	 */
	@Override
	protected void finalize(){
		tearDownGPUBindings();
	}
	
	/**
	 * Used to teardown all GPU related memory bindings. Use with caution
	 */
	public void tearDownGPUBindings(){
		tearDownShaders();
		tearDownBuffers();
		DisplayUtils.exitOnGLError("Tearing down GPU Bindings");
	}
	
	/**
	 * Used to delete this objects GPU memory buffers bindings. Use with caution.
	 */
	private void tearDownBuffers(){
		//Select the VAO
		GL30.glBindVertexArray(vaoID);
		
		//Disable the VBO index from the VAO attributes list
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		
		//Delete the vertex VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboID);

		//Delete the index VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboiID);
		
		//Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoID);
	}
	
	/**
	 * Used to unload the shader info from GPU memory
	 */
	private void tearDownShaders(){
		GL20.glUseProgram(0);
		GL20.glDetachShader(pID, vsID);
		GL20.glDetachShader(pID, fsID);
		
		GL20.glDeleteShader(vsID);
		GL20.glDeleteShader(fsID);
		GL20.glDeleteProgram(pID);
	}
}
