package ai.edgez.examples.wasm;

import org.teavm.interop.Export;
import org.teavm.interop.Import;

import ai.edgez.edgeos.wasm.EdgeWasmModule;
import ai.edgez.edgeos.wasm.Lwm2mWasmImports;
import ai.edgez.edgeos.wasm.WasmModule;

/**
 * Simple WebAssembly module that calls env.log with "Hi!" and uses LwM2M imports.
 */
@WasmModule
public class HelloWasm implements EdgeWasmModule {

    private static final HelloWasm INSTANCE = new HelloWasm();

    @Import(name = "log", module = "env")
    public static native void log(int value);

    @Override
    public void main() {
        log(77); // 'M'
    }

    @Override
    public void register() {
        Lwm2mWasmImports.observe(3, 0, 0);
        log(82); // 'R'
    }

    @Override
    public void unregister() {
        Lwm2mWasmImports.cancelObserve(3, 0, 0);
        log(85); // 'U'
    }

    @Override
    public void event() {
        Lwm2mWasmImports.read(3, 0, 0, 0, 0);
        log(69); // 'E'
    }

}
