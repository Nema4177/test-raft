package com.example.demo.statemachine;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.core.StateMachineAdapter;

public class DemoStateMachine extends StateMachineAdapter {
    private AtomicLong   leaderTerm = new AtomicLong(-1);
    
    @Override
    public void onApply(Iterator iter) {
        while(iter.hasNext()){
        	ByteBuffer data = iter.getData(); // Get the current task data
        	Closure done = iter.done();  // Get the closure callback of the current task
        	long index = iter.getIndex();  // Get the unique log number of the task, which monotonically increases, and is automatically assigned by JRaft
        	long term = iter.getTerm(); 
           // Apply a task to state machine
            iter.next();
        }
    }

    @Override
    public void onLeaderStart(long term) {
        // Save the leader term
        this.leaderTerm.set(term);
        super.onLeaderStart(term);
    }
    
}