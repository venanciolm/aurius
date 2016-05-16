package com.farmafene.aurius.mngt;

public interface IThreadManagement {

	long getCpuTime(long threadId);

	long getContentionTime(long threadId);

	long getBlockedTime(long threadId);

	long getWaitedTime(long threadId);

	StackTraceElement[] getStackTrace(long threadId);

}