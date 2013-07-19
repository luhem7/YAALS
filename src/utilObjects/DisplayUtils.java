package utilObjects;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;

public class DisplayUtils {
	/* Opens the file and stores the contents of that file as a shader program.
	 * Needs the type of shader we are loading as.
	 * Returns the int that corresponds to the shader object in memory
	 */
	public static int loadShader(String filename, int type) {
		StringBuilder shaderSource = new StringBuilder();
		int shaderID = 0;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file.");
			e.printStackTrace();
			System.exit(-1);
		}
		
		shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Could not compile shader.");
			System.exit(-1);
		}
		
		exitOnGLError("Loading / Compiling Shaders");
		
		return shaderID;
	}
	
	/**
	 * Helper function to detect and output GL errors
	 * @param errorMessage
	 */
	public static void exitOnGLError(String errorMessage) {
		int errorValue = GL11.glGetError();
		
		if(errorValue == GL11.GL_NO_ERROR)
			return;
		
		while(errorValue != GL11.GL_NO_ERROR){
			System.out.println("Error => "+GLU.gluErrorString(errorValue)+
					" during "+ errorMessage);
			errorValue = GL11.glGetError();
		}
		
		if(Display.isCreated())
			Display.destroy();
		
		System.exit(-1);
	}
}
