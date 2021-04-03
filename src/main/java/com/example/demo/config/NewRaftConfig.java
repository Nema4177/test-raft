package com.example.demo.config;

import com.alipay.sofa.jraft.JRaftUtils;
import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.NodeManager;
import com.alipay.sofa.jraft.RaftGroupService;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.entity.Task;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.util.Endpoint;
import com.example.demo.closure.RaftClosure;
import com.example.demo.statemachine.DemoStateMachine;

import java.nio.ByteBuffer;

public class NewRaftConfig {

    public static void main(String[] args) {
        String groupId = "jraft";
        Endpoint addr = JRaftUtils.getEndPoint("localhost:8081");
        PeerId peer = JRaftUtils.getPeerId("localhost:8081");
        PeerId peer2 = JRaftUtils.getPeerId("localhost:8080");
        Configuration conf = JRaftUtils.getConfiguration("localhost:8080,localhost:8081");

        NodeManager.getInstance().addAddress(peer.getEndpoint());
//        NodeManager.getInstance().addAddress(peer2.getEndpoint());

//        RpcServer rpcServer = RaftRpcServerFactory.createAndStartRaftRpcServer(peer.getEndpoint());


        //Node options
        NodeOptions nodeOptions = getNodeOptions(conf);//new NodeOptions();

        RaftGroupService cluster = new RaftGroupService(groupId, peer, nodeOptions);
        Node node = cluster.start();

        //Create a closure
        RaftClosure done = new RaftClosure();

        // create a simple task
        Task task = new Task();
        task.setData(ByteBuffer.wrap("hello".getBytes()));
        task.setDone(done);

        Task task2 = new Task();
        task2.setData(ByteBuffer.wrap("hello2".getBytes()));
        task2.setDone(done);

        Task task3 = new Task();
        task3.setData(ByteBuffer.wrap("hello3".getBytes()));
        task3.setDone(done);

        Task task4 = new Task();
        task4.setData(ByteBuffer.wrap("hello4".getBytes()));
        task4.setDone(done);

//        node.snapshot(done);

        System.out.println("Leader ID: " +node.getLeaderId());
        while(node.getLeaderId() == null){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Leader ID(A): " +node.getLeaderId());
        node.apply(task);
        node.apply(task2);
        node.apply(task3);
        node.apply(task4);
    }

    private static NodeOptions getNodeOptions(Configuration conf) {
        NodeOptions nodeOptions = new NodeOptions();
        nodeOptions.setElectionTimeoutMs(1000);
        nodeOptions.setSnapshotLogIndexMargin(3600);
        nodeOptions.setInitialConf(conf);
        nodeOptions.setLogUri("logs");
        nodeOptions.setRaftMetaUri("rafts");
        nodeOptions.setSnapshotUri("snaps");
        nodeOptions.setFsm(new DemoStateMachine());
        return nodeOptions;
    }

}
