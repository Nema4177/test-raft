package com.example.demo.closure;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Status;

public class RaftClosure implements Closure{

	@Override
	public void run(Status status) {
			
		System.out.println("Task completed with status"+status.getCode());
	}

}
