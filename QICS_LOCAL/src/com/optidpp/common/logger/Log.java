// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) ansi

package com.optidpp.common.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log
{

	public Log()
	{
	}

	public static void Debug()
	{
		Debug((new StringBuilder()).append("####################").append(Thread.currentThread().getStackTrace()[2].getClassName()).append(".").append(Thread.currentThread().getStackTrace()[2].getMethodName()).toString());
	}

	public static void DebugStart()
	{
		Debug((new StringBuilder()).append("####################").append(Thread.currentThread().getStackTrace()[2].getClassName()).append(".").append(Thread.currentThread().getStackTrace()[2].getMethodName()).append(" Start").toString());
	}

	public static void DebugEnd()
	{
		Debug((new StringBuilder()).append("####################").append(Thread.currentThread().getStackTrace()[2].getClassName()).append(".").append(Thread.currentThread().getStackTrace()[2].getMethodName()).append(" End").toString());
	}

	public static void Debug(String s)
	{
		//System.out.println(s);
		if(log.isDebugEnabled())
			log.debug(s);
	}

	public static void Info(String s)
	{
		//System.out.println(s);
		if(log.isDebugEnabled())
			log.info(s);
	}

	public static void Warn(String s)
	{
		//System.out.println(s);
		log.warn(s);
	}

	public static void Error(String s)
	{
		//System.out.println(s);
		log.error(s);
	}

	private static final Logger log = LoggerFactory.getLogger(Log.class);

}
