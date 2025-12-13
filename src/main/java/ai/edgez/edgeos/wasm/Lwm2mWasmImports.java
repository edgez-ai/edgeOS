package ai.edgez.edgeos.wasm;

import org.teavm.interop.Import;

/**
 * TeaVM WebAssembly imports provided by the host (edge-server).
 *
 * Names/modules here must match the host's import resolver.
 */
public final class Lwm2mWasmImports {
    private Lwm2mWasmImports() {
    }

    /**
     * Read by numeric ids.
     *
     * Signature supported by host: (objId, instId, resId, outPtr, outCap) -> i32(bytesWritten)
     */
    @Import(name = "read", module = "env")
    public static native int read(int objectId, int objectInstanceId, int resourceId, int outPtr, int outCap);

    /**
     * Write by numeric ids.
     *
     * Signature supported by host: (objId, instId, resId, payloadPtr, payloadLen) -> i32(status)
     */
    @Import(name = "write", module = "env")
    public static native int write(int objectId, int objectInstanceId, int resourceId, int payloadPtr, int payloadLen);

    /**
     * Observe by numeric ids.
     *
     * Signature supported by host: (objId, instId, resId) -> i32(status)
     */
    @Import(name = "observe", module = "env")
    public static native int observe(int objectId, int objectInstanceId, int resourceId);

    /**
     * Cancel observation by numeric ids.
     *
     * Signature supported by host: (objId, instId, resId) -> i32(status)
     */
    @Import(name = "cancelObserve", module = "env")
    public static native int cancelObserve(int objectId, int objectInstanceId, int resourceId);

    // Optional pointer/len variants (host supports these too) if you later want string paths.

    @Import(name = "read", module = "env")
    public static native int readPath(int pathPtr, int pathLen, int outPtr, int outCap);

    @Import(name = "write", module = "env")
    public static native int writePath(int pathPtr, int pathLen, int payloadPtr, int payloadLen);

    @Import(name = "observe", module = "env")
    public static native int observePath(int pathPtr, int pathLen);

    @Import(name = "cancelObserve", module = "env")
    public static native int cancelObservePath(int pathPtr, int pathLen);
}
