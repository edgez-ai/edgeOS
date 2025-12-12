package ai.edgez.edgeos.wasm;

import org.teavm.tooling.ConsoleTeaVMToolLog;
import org.teavm.tooling.TeaVMTargetType;
import org.teavm.tooling.TeaVMTool;
import org.teavm.vm.TeaVMOptimizationLevel;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Generate WebAssembly using TeaVM to compile HelloWasm.java
 * 
 * This generator uses TeaVM to compile Java code to WebAssembly,
 * rather than hardcoding the bytecode.
 * 
 * Usage:
 *     java ai.edgez.edgeos.wasm.TeaVMWasmGenerator
 *     mvn exec:java -Dexec.mainClass="ai.edgez.edgeos.wasm.TeaVMWasmGenerator"
 */
public class TeaVMWasmGenerator {
    
    public static void main(String[] args) {
        try {
            System.out.println("🔧 Compiling Java to WebAssembly using TeaVM...");
            
            Path projectRoot = Paths.get("").toAbsolutePath();

            // Output directories
            String[] outputDirs = {
                projectRoot.resolve("target/teavm-wasm").toString(),
                projectRoot.resolve("src/main/resources/wasm").toString(),
                projectRoot.resolve("target/classes/wasm").toString()
            };

            Files.createDirectories(Paths.get(outputDirs[0]));
            
            // Configure TeaVM
            TeaVMTool tool = new TeaVMTool();
            tool.setTargetType(TeaVMTargetType.WEBASSEMBLY);
            tool.setMainClass("ai.edgez.edgeos.wasm.HelloWasm");
            tool.setTargetDirectory(new File(outputDirs[0]));
            tool.setTargetFileName("classes.wasm");
            tool.setOptimizationLevel(TeaVMOptimizationLevel.SIMPLE);
            tool.setObfuscated(false);
            tool.setLog(new ConsoleTeaVMToolLog(true));
            
            // Build TeaVM classpath: project classes + everything on the current JVM classpath
            ClassLoader cl = TeaVMWasmGenerator.class.getClassLoader();
            tool.setClassLoader(cl);

            List<File> classPath = new ArrayList<>();
            Set<String> seen = new HashSet<>();

            // Ensure compiled project classes are first
            File compiledClasses = projectRoot.resolve("target/classes").toFile();
            classPath.add(compiledClasses);
            seen.add(compiledClasses.getAbsolutePath());

            // Collect URLs from the exec plugin classloader (preferred)
            if (cl instanceof URLClassLoader urlCl) {
                for (URL url : urlCl.getURLs()) {
                    File f = new File(url.getPath());
                    if (f.getPath().isEmpty()) {
                        continue;
                    }
                    String key = f.getAbsolutePath();
                    if (seen.add(key)) {
                        classPath.add(f);
                    }
                }
            }

            // Fallback to java.class.path if exec classloader is not URL-based
            if (classPath.size() == 1) { // only target/classes so far
                String javaClassPath = System.getProperty("java.class.path", "");
                String pathSeparator = System.getProperty("path.separator", ":");
                for (String entry : javaClassPath.split(java.util.regex.Pattern.quote(pathSeparator))) {
                    if (entry != null && !entry.isBlank()) {
                        File f = new File(entry);
                        String key = f.getAbsolutePath();
                        if (seen.add(key)) {
                            classPath.add(f);
                        }
                    }
                }
            }

            // Log resolved classpath for debugging missing entries
            System.out.println("📂 TeaVM classpath:");
            for (File cp : classPath) {
                System.out.println("  - " + cp.getAbsolutePath() + (cp.exists() ? "" : " (missing)"));
            }

            tool.setClassPath(classPath);
            
            // Generate WebAssembly
            tool.generate();
            
            System.out.println("✅ TeaVM compilation successful!");
            
            // Copy the generated .wasm file to resource directories
            Path sourceWasm = Paths.get(outputDirs[0], "classes.wasm");
            
            if (Files.exists(sourceWasm)) {
                byte[] wasmBytes = Files.readAllBytes(sourceWasm);
                System.out.println("📦 Generated WASM size: " + wasmBytes.length + " bytes");
                
                // Copy to resources and target/classes
                for (int i = 1; i < outputDirs.length; i++) {
                    Path targetPath = Paths.get(outputDirs[i], "hello.wasm");
                    Files.createDirectories(targetPath.getParent());
                    Files.copy(sourceWasm, targetPath, 
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("✅ Copied to " + targetPath);
                }
                
                System.out.println("📝 Module exports: hello() -> calls env.log with H, i, !");
                
            } else {
                System.err.println("❌ TeaVM did not generate classes.wasm");
                System.exit(1);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error generating WebAssembly: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
