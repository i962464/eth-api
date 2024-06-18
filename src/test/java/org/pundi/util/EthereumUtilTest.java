package org.pundi.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.concurrent.ExecutionException;

import org.bitcoinj.wallet.UnreadableWalletException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月11日 10:51:00
 */
@SpringBootTest
class EthereumUtilTest {

  private static BigDecimal DECIMAL = BigDecimal.valueOf(Math.pow(10, 18));


  @Test
  public void createAccountTest() {
    EthereumUtil.createAccount("123");
    System.out.println("success");
  }

  @Test
  public void createHDWalletTest() {
    String xPub = "xpub661MyMwAqRbcEiGE2skVXZy8zdQxEyPvpqRUWrznW3uQZ7WBvDePU9z6XfKrHjy9xcxFE6vPFAkBJwKSebSidKJgtbeznmsZDRdzYTRa5A8";
    System.out.println(" 1 -> " + EthereumUtil.deriveAddressFromExtendPubKey(xPub, 1));
    System.out.println(" 2 -> " + EthereumUtil.deriveAddressFromExtendPubKey(xPub, 2));
    System.out.println(" 3 -> " + EthereumUtil.deriveAddressFromExtendPubKey(xPub, 3));
    System.out.println(" 4 -> " + EthereumUtil.deriveAddressFromExtendPubKey(xPub, 4));
    System.out.println(" 5 -> " + EthereumUtil.deriveAddressFromExtendPubKey(xPub, 5));
    System.out.println(" 10 -> " + EthereumUtil.deriveAddressFromExtendPubKey(xPub, 10));
    System.out.println(" 15 -> " + EthereumUtil.deriveAddressFromExtendPubKey(xPub, 15));
    System.out.println(" 20 -> " + EthereumUtil.deriveAddressFromExtendPubKey(xPub, 20));
    System.out.println(" 421 -> " + EthereumUtil.deriveAddressFromExtendPubKey(xPub, 10000324));

  }

  @Test
  public void getLastBlock() throws IOException, ExecutionException, InterruptedException {
    BigInteger lastBlock = EthereumUtil.getLastBlock();
    System.out.println(lastBlock);
  }


  @Test
  public void getBalanceTest() throws IOException {
    EthereumUtil.setWeb3RpcEndpoint("https://sepolia.infura.io/v3/7cecab00c2b8496cbf7a51fcd05fb5ec");
    BigInteger balance = EthereumUtil.getBalance("0xA964d0d6936580d7d2D797DE5a71c1429D848248");
    BigDecimal divide = new BigDecimal(balance).divide(DECIMAL, 8, RoundingMode.DOWN);
    System.out.println(divide);
  }

  @Test
  public void getBalanceByTokenTest() throws IOException, ExecutionException, InterruptedException {
//    System.setProperty("http.proxyHost", "127.0.0.1");
//    System.setProperty("http.proxyPort", "7890");
    EthereumUtil.setWeb3RpcEndpoint("https://testnet-fx-json-web3.functionx.io:8545");
    BigInteger balance = EthereumUtil.getBalance("0xB25Fdff8D86C85eb5E9b455b71487CE76f086DfF", "0xc8B4d3e67238e38B20d38908646fF6F4F48De5EC");
//    BigDecimal divide = new BigDecimal(balance).divide(DECIMAL, 8, RoundingMode.DOWN);
    System.out.println(balance);
  }

  @Test
  public void ethTransfer() throws IOException, ExecutionException, InterruptedException {

    String hash = EthereumUtil.ethTransfer(
        "0x8cE4092e890c5E21d1596156EDc73Ab00242B20d",
        "0xB25Fdff8D86C85eb5E9b455b71487CE76f086DfF", new BigInteger("1000000000000000"),
        "11e807ffd2af91ad19a093c9613f116139848b6bf10f9fb8f2f0f138f7b44ec4");
    System.out.println(hash);
  }

  @Test
  public void tokenTransfer() throws IOException, ExecutionException, InterruptedException {

    String hash = EthereumUtil.tokenTransfer(
        "0x8cE4092e890c5E21d1596156EDc73Ab00242B20d",
        "0xf47c629fbe70498d221f2eed7c8aad67137c7189",
        "0xEDC0d17D2804D6937dd83b993C7A668059bBF78D",
        new BigInteger("1000000000000000000000"),
        "11e807ffd2af91ad19a093c9613f116139848b6bf10f9fb8f2f0f138f7b44ec4");
    System.out.println(hash);
  }

  @Test
  public void getCoinName() throws Exception {
    String coinName = EthereumUtil.getCoinName("0x67550Df3290415611F6C140c81Cd770Ff1742cb9");
    String tokenSymbol = EthereumUtil.getTokenSymbol("0x67550Df3290415611F6C140c81Cd770Ff1742cb9");
    System.out.println(coinName);
    System.out.println(tokenSymbol);
  }
}