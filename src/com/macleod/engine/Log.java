package com.macleod.engine;

public class Log {

	public enum LogLevel {
		ALL(0), INFO(10), WARN(20), ERROR(30), NONE(40);
		
		private final int priority;
		private LogLevel(int priority) { this.priority = priority; }
	}
	
	private static LogLevel currentLogLevel = LogLevel.ALL;
	
	public static final void setLogLevel(LogLevel newLevel) { currentLogLevel = newLevel; }
	
	public static final void error(Object obj) { 
		if(currentLogLevel.priority <= LogLevel.ERROR.priority) System.err.println("[ERROR] " + obj);
	}
	public static final void error(String message, Exception e) { 
		error(message + " - " + ((e == null) ? "null" : e.getMessage()) + " - " + ((e == null) ? "null" : e.getStackTrace())); 
	}
	
	public static final void info(Object obj) { 
		if(currentLogLevel.priority <= LogLevel.INFO.priority) System.out.println("[INFO] " + obj);
	}
	
	public static final void warn(Object obj) { 
		if(currentLogLevel.priority <= LogLevel.WARN.priority) System.out.println("[WARN] " + obj);
	}
	
}
