/**
 * Copyright (c) 2016 Dr. Chris Senior.
 * See LICENSE.txt for licensing terms
 */
package com.asteroid.duck.osgi.log;

/**
 * A dumb, simple logging API - that has 0 dependencies : since it's used by classloaders - it's just easier this way :-)
 */
public class Logger {
    private static final Logger LOG = Logger.getLogger(Logger.class);

    /** logging level enum */
    private enum Level {
        TRACE(0), DEBUG(5), INFO(10), EXCEPTION(15), ERROR(20), NONE(25);

        private final int level;

        private Level(int order) {
            this.level = order;
        }

        public boolean includes(Level other) {
            return this.level <= other.level;
        }

        /**
         * Parse logging level from string (match enum name - ignore case)
         *
         * @param s the string to parse
         *
         * @return the level (or NONE as default)
         */
        public static Level parse(String s) {
            if (s != null && s.length() > 0) {
                for (Level l : values()) {
                    if (l.name().equalsIgnoreCase(s)) {
                        return l;
                    }
                }
            }
            return NONE;
        }
    }

    /** System property prefix for controlling logging levels */
    private static final String LOGGING_OPT = "osgi-unit.log";
    /** the level of this logger */
    private final Level level;
    /** the name of this logger */
    private final String name;

    /** create with a name */
    public Logger(String name) {
        this.name = name;
        level = getLevel(name);
    }

    /**
     * Load the level from system properties.
     * Uses osgi-unit.log.&lt;name&gt; to locate a Level
     * Failing that uses osgi-unit.log.default
     * Failing that uses Level.NONE
     *
     * @param name the name of the logger
     *
     * @return the level to use for this logger
     */
    private static Level getLevel(final String name) {
        Level level = Level.parse(System.getProperty(LOGGING_OPT + ".default"));
        String levelString = System.getProperty(LOGGING_OPT + "." + name);
        if (levelString != null && levelString.length() > 0) {
            level = Level.parse(levelString);
        }
        return level;
    }

    public static Logger getLogger(Class<?> klass) {
        Logger logger = new Logger(klass.getName());
        return logger;
    }

    public static Logger getLogger(String name) {
        return new Logger(name);
    }

    public boolean isDebugEnabled() {
        return level.includes(Level.DEBUG);
    }

    public boolean isTraceEnabled() {
        return level.includes(Level.TRACE);
    }

    public void debug(final String s) {
        if (level.includes(Level.DEBUG)) {
            System.out.println("[DEBUG] " + name + "::" + s);
        }
    }

    public void error(final String s, final Throwable e) {
        if (level.includes(Level.ERROR)) {
            System.err.println("[ERROR] " + name + "::" + s + " - " + e.getMessage());
        }
        if (level.includes(Level.EXCEPTION)) {
            e.printStackTrace(System.err);
        }
    }

    public void info(final String s) {
        if (level.includes(Level.INFO)) {
            System.out.println("[INFO] " + name + "::" + s);
        }
    }

    public void trace(final String s) {
        if (level.includes(Level.TRACE)) {
            System.out.println("[TRACE] " + name + "::" + s);
        }
    }
}
