//package org.pundi.contract;
//
//import java.util.List;
//import java.util.Random;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author ekko
// * @version 1.0.0
// * @Description
// * @createTime 2024年05月14日 07:24:00
// */
//@Component
//@Slf4j
//public class EthRpcClientManager {
//
//  @Value("${api.infura.url}")
//  private String[] ethRpcEndpoints;
//  private List<EthereumRpcClient> rpcClientList;
//
//  @Override
//  public void afterPropertiesSet() throws Exception {
//    rpcClientList = getRpcClientList(ethRpcEndpoints);
//  }
//
//  /**
//   * get one eth rpc client random
//   *
//   * @return
//   */
//  public EthereumRpcClient getEthRpc() {
//    if (CollectionUtils.isEmpty(rpcClientList)) {
//      return null;
//    }
//    int index = new Random().nextInt(rpcClientList.size());
//    return rpcClientList.get(index);
//  }
//
//  private List<EthereumRpcClient> getRpcClientList(String[] ethRpcEndpoints) {
//
//    if (ethRpcEndpoints == null || ethRpcEndpoints.length == 0) {
//      log.warn("ethRpcEndpoints is empty");
//      return null;
//    }
//
//    List<EthereumRpcClient> list = Lists.newArrayList();
//    for (String ethRpcEndpoint : ethRpcEndpoints) {
//      EthereumRpcClient rpcClient = new EthereumRpcClient(ethRpcEndpoint);
//      list.add(rpcClient);
//    }
//
//    return list;
//  }
//}
