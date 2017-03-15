package Base;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public abstract class HelloBase {

	// The window handle
	private long window;

	double secsPerUpdate = 1.0 / 30.0;
	float loopSlot = 1f / 60;
	
	double previous = getTime();
	double steps = 0.0;
	
	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init(512, 512);
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init(int width, int height) {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(width, height, "Hello World!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		
		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		GLFWWindowSizeCallback windowSizeCallback;
		
		// Setup resize callback
		glfwSetWindowSizeCallback(window, windowSizeCallback = new GLFWWindowSizeCallback() {
		@Override
		public void invoke(long window, int width, int height) {
			glfwSetWindowSize(window, width, height);
			
		//Window.this.width = width;
		//Window.this.height = height;
		//Window.this.setResized(true);
		}
		});

		
		// Make the window visible
		glfwShowWindow(window);
		
		
	}

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		init();
		
		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		previous = getTime();
		while ( !glfwWindowShouldClose(window) ) {
			
			// To have fixed step game loop.
			double loopStartTime = getTime();
			double elapsed = loopStartTime - previous;
			previous = loopStartTime;
			steps += elapsed;

			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			
			
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
			
			handleInput();
			
			
			while (steps >= secsPerUpdate) {
				updateGameState();
				steps -= secsPerUpdate;
				System.err.println("Update Game State");
			}

			

			render();
			System.err.println("Render Scene");
			
			glfwSwapBuffers(window); // swap the color buffers

			sync(getTime());
			
			System.err.println("Steps = "+steps);
		}
	}

	/**
	 * @param time
	 */
	private void sync(double loopStartTime)
	{

		double endTime = loopStartTime + loopSlot;
		while (getTime() < endTime)
		{
			try
			{
				Thread.sleep(1);
			} catch (InterruptedException ie)
			{
			}
		}
	}

	

	/**
	 * @return
	 */
	protected long getTime()
	{
		return System.currentTimeMillis();
	}

	/**
	 * 
	 */
	protected abstract void updateGameState();
	protected abstract void handleInput();
	protected abstract void render();
	protected abstract void destroy();
	
	public abstract void init();
	
/*	public int loadShader(String filename, int type)
	{
		StringBuilder shaderSource = new StringBuilder();
		int shaderID = 0;

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null)
			{
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e)
		{
			System.err.println("Could not read file.");
			e.printStackTrace();
			System.exit(-1);
		}

		shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);

		String s = GL20.glGetShaderInfoLog(shaderID, 1000);

		if (!s.isEmpty())
		{
			System.err.println("Error compiling " + filename);
			System.err.println(s);
			return -1;
		}
		
		return shaderID;
	}*/
	

}