# edgeOS

Uses TeaVM to create a tiny layer to build device shadow

## WebAssembly Generation

This project includes tools to generate WebAssembly modules for edge devices.

### Python Version

Generate WASM module using Python:

```bash
python3 generate_hello_wasm.py
```

### Java/Maven Version

Generate WASM module using Java and Maven:

```bash
# Automatic generation during build
mvn generate-resources

# Manual execution
mvn exec:java

# Or run directly after compilation
mvn compile
java -cp target/classes ai.edgez.edgeos.wasm.HelloWasmGenerator
```

Both implementations generate the same 65-byte WebAssembly module in `src/main/resources/wasm/hello.wasm` that:
- Imports `env.log` function from the host
- Exports a `hello()` function
- Calls `env.log` with ASCII codes for 'H', 'i', '!'

## Requirements

- **Python**: Python 3.x
- **Java/Maven**: JDK 17+ and Maven 3.6+
