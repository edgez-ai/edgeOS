package ai.edgez.edgeos.wasm;

/**
 * Interface describing wasm lifecycle hooks expected by the host.
 */
public interface EdgeWasmModule {
    void main();
    void register();
    void unregister();
    void event();
}
