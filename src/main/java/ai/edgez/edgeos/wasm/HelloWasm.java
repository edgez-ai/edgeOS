package ai.edgez.edgeos.wasm;

import org.teavm.interop.Import;

/**
 * Simple WebAssembly module that calls env.log with "Hi!"
 * This class will be compiled to WebAssembly by TeaVM
 */
public class HelloWasm {
    
    /**
     * Import the log function from the host environment
     * This will be available as env.log in the WebAssembly module
     */
    @Import(name = "log", module = "env")
    public static native void log(int value);
    
    /**
     * Export the hello function
     * This will call log with ASCII codes for 'H', 'i', '!'
     */
    public static void hello() {
        log(72);   // 'H'
        log(105);  // 'i'
        log(33);   // '!'
    }
    
    /**
     * Main entry point for TeaVM compilation
     */
    public static void main(String[] args) {
        hello();
    }
}
