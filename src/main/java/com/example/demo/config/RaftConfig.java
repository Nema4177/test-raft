package com.example.demo.config;

import java.nio.ByteBuffer;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.JRaftUtils;
import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.NodeManager;
import com.alipay.sofa.jraft.RaftGroupService;
import com.alipay.sofa.jraft.RaftServiceFactory;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.core.IteratorImpl;
import com.alipay.sofa.jraft.core.NodeImpl;
import com.alipay.sofa.jraft.core.ReplicatorGroupImpl;
import com.alipay.sofa.jraft.entity.NodeId;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.entity.Task;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.option.RaftOptions;
import com.alipay.sofa.jraft.rpc.CliClientService;
import com.alipay.sofa.jraft.rpc.RaftRpcServerFactory;
import com.alipay.sofa.jraft.rpc.RpcClient;
import com.alipay.sofa.jraft.rpc.RpcServer;
import com.alipay.sofa.jraft.rpc.impl.BoltRpcServer;
import com.alipay.sofa.jraft.rpc.impl.BoltRaftRpcFactory;
import com.alipay.sofa.jraft.rpc.impl.BoltRpcClient;
import com.alipay.sofa.jraft.util.Endpoint;
import com.example.demo.closure.RaftClosure;
import com.example.demo.statemachine.DemoStateMachine;
import com.alipay.sofa.jraft.option.ReplicatorGroupOptions;

@Component
public class RaftConfig {
	
	//@PostConstruct
	//void init() {
	public static void main(String args[]) {
		
		  String groupId = "jraft";
//		  Endpoint addr = new Endpoint("localhost", 8080);
//		  String s = addr.toString(); // 结果为 localhost:8080
//		  PeerId peer = new PeerId();
//		  boolean success = peer.parse(s);
		Endpoint addr = JRaftUtils.getEndPoint("localhost:8080");
		  PeerId serverId = JRaftUtils.getPeerId("localhost:8080");
		  Configuration conf = JRaftUtils.getConfiguration("localhost:8081,localhost:8080");
		  
//		  //IteratorImpl it = new IteratorImpl();
		  Closure done = new RaftClosure();
		  Task task = new Task();
		  task.setData(ByteBuffer.wrap("Hello! This is a sample message to test raft..".getBytes()));
		  task.setDone(done);
		  
		  NodeOptions opts = new NodeOptions();
		  opts.setElectionTimeoutMs(1000);
		  opts.setLogUri("logs");
		  opts.setRaftMetaUri("raftMeta");
		  opts.setSnapshotUri("snapshots");
		  opts.setFsm(new DemoStateMachine());
		  opts.setInitialConf(conf);
		  NodeImpl node = (NodeImpl) RaftServiceFactory.createRaftNode(groupId, serverId);
		  NodeId nodeId = node.getNodeId();
		  NodeManager.getInstance().addAddress(serverId.getEndpoint());
		  BoltRpcServer rpcServer = (BoltRpcServer) RaftRpcServerFactory.createAndStartRaftRpcServer(serverId.getEndpoint());
		  RaftGroupService cluster = new RaftGroupService(groupId, serverId, opts);
		  //RpcServer rpcServer2 = cluster.getRpcServer();
		  //Node node2 = cluster.start();
		  
		  BoltRaftRpcFactory boltRaftRpcFactory = new BoltRaftRpcFactory();
		  RpcClient boltRpcClient =  boltRaftRpcFactory.createRpcClient();
		  //boltRpcClient.registerConnectEventListener(null);
		  //boltRpcClient.invokeSync(addr, s, 0)
		  ReplicatorGroupOptions  replicatorGroupOptions = new ReplicatorGroupOptions();
		  RaftOptions raftOptions = new RaftOptions();
		  replicatorGroupOptions.setRaftOptions(raftOptions);
		  replicatorGroupOptions.setNode(node);
		  ReplicatorGroupImpl replicatorGroupImpl = new ReplicatorGroupImpl();
		  replicatorGroupImpl.init(nodeId, replicatorGroupOptions);
		  
		  boltRpcClient.registerConnectEventListener(replicatorGroupImpl);
		  
		  
	}

}
