package com.kneelawk.glowvid.core.impl;

public class GVOS {
    public enum OSType {
        WINDOWS,
        LINUX,
        MACOSX,
        OTHER
    }

    public enum ArchType {
        X86_64,
        X86_32,
        ARM_64,
        ARM_32,
        OTHER
    }

    public static final String OS_NAME = System.getProperty("os.name");
    public static final String OS_ARCH = System.getProperty("os.arch");
    public static final String OS_VERSION = System.getProperty("os.version");

    public static final boolean OS_LINUX = OS_NAME.toLowerCase().startsWith("linux");
    public static final boolean OS_MAC = OS_NAME.toLowerCase().startsWith("mac");
    public static final boolean OS_WINDOWS = OS_NAME.toLowerCase().startsWith("windows");
    public static final OSType OS =
        OS_LINUX ? OSType.LINUX : OS_MAC ? OSType.MACOSX : OS_WINDOWS ? OSType.WINDOWS : OSType.OTHER;

    public static final boolean ARCH_X86_64 = OS_ARCH.equalsIgnoreCase("x86_64") || OS_ARCH.equalsIgnoreCase("amd64");
    public static final boolean ARCH_X86_32 =
        OS_ARCH.equalsIgnoreCase("x86") || OS_ARCH.equalsIgnoreCase("i386") || OS_ARCH.equalsIgnoreCase("i686") ||
            OS_ARCH.equalsIgnoreCase("i586") || OS_ARCH.equalsIgnoreCase("i486") || OS_ARCH.equalsIgnoreCase("amd32");
    public static final boolean ARCH_ARM_64 = OS_ARCH.equalsIgnoreCase("aarch64") || OS_ARCH.equalsIgnoreCase("arm64");
    public static final boolean ARCH_ARM_32 =
        OS_ARCH.equalsIgnoreCase("arm") || OS_ARCH.toLowerCase().startsWith("armv");
    public static final ArchType ARCH = ARCH_X86_64 ? ArchType.X86_64 :
        ARCH_X86_32 ? ArchType.X86_32 : ARCH_ARM_64 ? ArchType.ARM_64 : ARCH_ARM_32 ? ArchType.ARM_32 : ArchType.OTHER;

    public static void print() {
        GVCLog.LOG.info("[GlowVid] os.name: '{}'", OS_NAME);
        GVCLog.LOG.info("[GlowVid] os.arch: '{}'", OS_ARCH);
        GVCLog.LOG.info("[GlowVid] os.version: '{}'", OS_VERSION);
        GVCLog.LOG.info("[GlowVid] Detected OS: {}", OS);
        GVCLog.LOG.info("[GlowVid] Detected Arch: {}", ARCH);

        if (OS == OSType.OTHER) {
            GVCLog.LOG.error(
                "[GlowVid] Unknown Operating System encountered: '{}'. Please submit an issue to https://github.com/Kneelawk/GlowVid/issues",
                OS_NAME);
        }

        if (!ARCH_X86_64 && !ARCH_ARM_64) {
            GVCLog.LOG.warn(
                "[GlowVid] Prebuilt FFmpeg builds are only available for 64-bit x86 and arm architectures. GlowVid will rely on system installations of FFmpeg.");
        }
    }
}
