# hello-wasm example

This example shows a minimal WASM module that depends on `edgeOS` and exports the expected hooks.

## Build
```bash
mvn clean package
```

## Artifact
- `target/hello-wasm-0.0.1-SNAPSHOT.jar`
- WASM generation is not built-in here; consume via edgeOS tooling or your own pipeline.

## Coordinates
Use `ai.edgez.examples:hello-wasm:0.0.1-SNAPSHOT` as a sample module. It depends on `ai.edgez:edgeOS`.
