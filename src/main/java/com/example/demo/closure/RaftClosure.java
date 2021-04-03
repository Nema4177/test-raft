package com.example.demo.closure;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.closure.TaskClosure;

public class RaftClosure implements Closure {

	@Override
	public void run(Status status) {
			
		System.out.println("Task completed with status"+status.getCode());
		System.out.println("Task completed with "+status.getErrorMsg());
		System.out.println("Task completed with "+status.getRaftError());
	}

//	@Override
//	public void onCommitted() {
//		System.out.println("Task onCommitted");
//	}

}
