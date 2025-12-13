package ai.edgez.edgeos.wasm;

import org.teavm.interop.Export;

import java.util.ServiceLoader;

/** Facade that exposes static @Export entrypoints and delegates to an EdgeWasmModule impl. */
public final class WasmExports {
    private static final EdgeWasmModule _instance = resolveModule();

    private WasmExports() {
    }

    @Export(name = "main")
    public static void main() {
        _instance.main();
    }

    @Export(name = "register")
    public static void register() {
        _instance.register();
    }

    @Export(name = "unregister")
    public static void unregister() {
        _instance.unregister();
    }

    @Export(name = "event")
    public static void event() {
        _instance.event();
    }

    private static EdgeWasmModule resolveModule() {
        return ServiceLoader.load(EdgeWasmModule.class)
            .stream()
            .map(ServiceLoader.Provider::get)
            .filter(mod -> mod.getClass().isAnnotationPresent(WasmModule.class))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No EdgeWasmModule with @WasmModule found via ServiceLoader"));
    }
}
