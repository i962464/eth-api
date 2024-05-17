##  创建用户
##  根据用户ID创建地址，分为两种
    * 第一种，直接随机生成地址，然后保存生成地址的私钥
    * 第二种，根据助记词生成地址，根据助记词派生地址
    * 第三种，根据xpub推导地址，未完成
##  发送交易，参照EthereumUtil
###  ERC20Token
    ```  
    public static String tokenTransfer(String from, String to, String contractAddress, BigInteger value, String prvKey)
      throws ExecutionException, InterruptedException, IOException {

    // 构建建议
    RawTransaction rawTx = buildTokenRawTx(from, to, contractAddress, value);
    // 构建私钥
    Credentials credentials = Credentials.create(prvKey);
    // 签名Transaction
    byte[] signMessage = TransactionEncoder.signMessage(rawTx, credentials);
    String hexValue = Numeric.toHexString(signMessage);
    // 发送交易
    EthSendTransaction ethSendTransaction = getWeb3j().ethSendRawTransaction(hexValue).sendAsync().get();

    return ethSendTransaction.getTransactionHash();
}
```
###   ETH
    ```
      public static String ethTransfer(String from, String to, BigInteger value, String prvKey)
      throws ExecutionException, InterruptedException, IOException {

    // 创建RawTransaction交易对象
    RawTransaction rawTransaction = buildEthRawTx(from, to, value);
    // 构建私钥
    Credentials credentials = Credentials.create(prvKey);
    // 签名Transaction
    byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
    String hexValue = Numeric.toHexString(signMessage);
    // 发送交易
    EthSendTransaction ethSendTransaction = getWeb3j().ethSendRawTransaction(hexValue).sendAsync().get();

    return ethSendTransaction.getTransactionHash();
}
```
## 查询链上交易数据
### 步骤
    * 设置开始区块
    * web3j 获取当前区块ethGetBlockReceipts
    * 根据logs解析ETH交易还是ERC20交易
    * 如果是ETH交易，根据transactionHash 获取EthTransaction
    * 如果是ERC20交易，根据logs中topics解析topic[0]="0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef" 并且from & to 地址都不为0的交易
    * 保存tx_record记录

## 监听以太坊地址资产变动
    * 同上，监听transfer交易的地址， 则认为资产有变动
## 如何做到给定任意地址查询出地址下的所有以太坊ERC20资产
    * 同上，根据区块获取交易结果
    * EthBlock ethBlock = EthereumUtil.getWeb3j().ethGetBlockByNumber().send();
    List<TransactionResult> transactions = ethBlock.getBlock().getTransactions();
    * 过滤交易结果中的value等于0的记录
    * 根据用户地址， 判断from， to 则为Erc20 token 地址
    * 根据EthereumUtil#getTokenSymbol 获取token简称
    * 根据EthereumUtil#getCoinName 获取token名称


##  充值归集(这里是伪代码，没有提供签名服务，使用的依旧是EthereumUtil工具类接口)
### 步骤
    * 使用上面同步区块的tx_record记录，过滤to地址为user列表中的地址作为充值记录
    * 根据最新区块确认数-交易所在区块 + 1 >12 判断交易是否确认
    * 根据user的index 推导私钥
    * 判断当前充值地址是否存在足够的gas
    * 如果不存在，调用**钱包服务**转手续费给充值地址
    * 调用离线服务构建交易
    * 调用离线服务签名，并缓存到redis
    * 获取交易的签名列表，广播交易
    * 获取广播交易hash，更新归集状态
### 问题
    * 问题一：假如用户只充了ERC20的token进来， 没有ETH作为手续费。那需要父钱包做一次转账， 然后用户的地址再做一次转账。
    * 问题二：如果给派生地址转了0.1个手续费，但是真正用到的却是0.08个， 这样会留一部分手续费在派生地址
## 统计以太坊每⽇新增了哪些地址
## 如何解析以太坊合约内部交易
    * https://github.com/hyperledger/web3j-cli/releases 下载web3j-cli-shadow-1.4.2
    * 解压， 进入bin目录
    * 创建abi文件
    * 执行命令生成java代码 ./web3j generate solidity -a ../../contract/HToken.abi -p com.example -o ../../src
    * 根据[ContractUtil]获取生成的文件对象， 即可以调用合约方法
    * 解析TransactionReceipt