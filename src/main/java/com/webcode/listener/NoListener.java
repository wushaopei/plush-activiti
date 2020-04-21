package com.webcode.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * @ClassName NoListener.java
 * @Description TODO
 * @Author wushaopei
 * @Date 2020年4月21日
 * @Version 1.0
 */
public class NoListener implements ExecutionListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("This is NoListener...☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
	}

}
