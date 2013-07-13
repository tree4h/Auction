package models;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static models.TestUtils.*;

import javax.swing.Timer;

import org.junit.Ignore;
import org.junit.Test;
//import play.test.*;

public class AuctionTest {
	private final static int MIN_BID_PRICE = 0;
	
	@Test
	public void testBidでオークション開催中は入札できること() {
		Auction auction = auction();
		Bid bid = bid();
		auction.bid(bid);
		assertThat(auction.getBids().size(), is(1));
		assertThat(auction.getBids().get(0).getStatus(), is(BidStatus.成功));
	}
	@Test
	public void testBidでオークション開始前は入札できないこと() {
		Auction auction = beforeAuction();
		Bid bid = bid();
		auction.bid(bid);
		assertThat(auction.getBids().size(), is(1));
		assertThat(auction.getBids().get(0).getStatus(), is(BidStatus.失敗));
	}
	@Test
	public void testBidでオークション終了後は入札できないこと() {
		Auction auction = afterAuction();
		Bid bid = bid();
		auction.bid(bid);
		assertThat(auction.getBids().size(), is(1));
		assertThat(auction.getBids().get(0).getStatus(), is(BidStatus.失敗));
		//オークション終了後（落札状態）は入札できないこと
		//オークション終了後（不成立状態）は入札できないこと
	}
	@Test
	public void testBidで初回入札額が最低入札額を超えるとき入札できること() {
		Auction auction = auction();
		Bid bid1 = bid(this.MIN_BID_PRICE+1);
		auction.bid(bid1);
		assertThat(auction.getBids().size(), is(1));
		assertThat(auction.getBids().get(0).getStatus(), is(BidStatus.成功));
	}
	@Test
	public void testBidで初回入札額が最低入札額のとき入札できないこと() {
		Auction auction = auction();
		Bid bid1 = bid(this.MIN_BID_PRICE);
		auction.bid(bid1);
		assertThat(auction.getBids().size(), is(1));
		assertThat(auction.getBids().get(0).getStatus(), is(BidStatus.失敗));
	}
	@Test
	public void testBidで入札額が前回入札額を超えないとき入札できないこと() {
		Auction auction = auction();
		Bid bid1 = bid(1);
		auction.bid(bid1);
		assertThat(auction.getBids().size(), is(1));
		assertThat(auction.getBids().get(0).getStatus(), is(BidStatus.成功));

		Bid bid2 = bid(1);
		auction.bid(bid2);
		//初回
		assertThat(auction.getBids().size(), is(2));
		assertThat(auction.getBids().get(1).getStatus(), is(BidStatus.失敗));
	}
	@Test
	public void testBidで入札額が前回入札額を超えたとき入札できること() {
		Auction auction = auction();
		Bid bid1 = bid(1);
		auction.bid(bid1);
		assertThat(auction.getBids().size(), is(1));
		assertThat(auction.getBids().get(0).getStatus(), is(BidStatus.成功));

		Bid bid2 = bid(2);
		auction.bid(bid2);
		//初回
		assertThat(auction.getBids().size(), is(2));
		assertThat(auction.getBids().get(1).getStatus(), is(BidStatus.成功));
	}
	@Test
	public void testBidで出品者は入札できないこと() {
		Member member1 = new Member("moriki");
		Member member2 = new Member("hosozawa");
		Auction auction = auction(member1);
		Bid bid1 = bid(member1);
		auction.bid(bid1);
		assertThat(auction.getBids().size(), is(1));
		assertThat(auction.getBids().get(0).getStatus(), is(BidStatus.失敗));
		
		Bid bid2 = bid(member2);
		auction.bid(bid2);
		assertThat(auction.getBids().size(), is(2));
		assertThat(auction.getBids().get(1).getStatus(), is(BidStatus.成功));
	}

	@Test
	public void testGetStateでオークション開始前は開始待ちとなること() {
		Auction auction = beforeAuction();
		assertThat(auction.getStatus(), is(AuctionStatus.開始待ち));
	}
	@Test
	public void testGetStateでオークション出品期間中は出品中となること() {
		Auction auction = auction();
		assertThat(auction.getStatus(), is(AuctionStatus.出品中));
	}
	@Test
	public void testGetStateでオークション出品期間終了後で有効な入札がないとき不成立となること() {
		Auction auction = afterAuction();
		assertThat(auction.getStatus(), is(AuctionStatus.不成立));
	}
	@Test
	public void testGetStateでオークション出品期間終了後で有効な入札があるとき落札となること() throws InterruptedException {
		Auction auction = auction(1);
		Bid bid = bid();
		auction.bid(bid);
		assertThat(auction.getBids().size(), is(1));
		assertThat(auction.getBids().get(0).getStatus(), is(BidStatus.成功));
		Thread.sleep(1000);//テストとしてはまずいか？
		assertThat(auction.getStatus(), is(AuctionStatus.落札));
	}
	@Test
	public void testGetAvailableTimeで出品中のとき残りの残り時間1分を返すこと() {
		Auction auction = auction(60);
		assertThat(auction.getAvailableTime(),is(1));
	}
	@Test
	public void testGetAvailableTimeで出品中のとき残りの残り時間0分を返すこと() {
		Auction auction = auction(59);
		assertThat(auction.getAvailableTime(),is(0));
	}
	@Test
	public void testGetAvailableTimeで終了時刻を過ぎたら残り時間0分を返すこと() throws InterruptedException {
		Auction auction = auction(1);
		//Thread.sleep(61000);//テストとしてはまずいか？
		assertThat(auction.getAvailableTime(),is(0));
	}
	@Test
	public void testGetMaxBidPriceで現在の最高価格を返すこと() {
		Auction auction = auction();
		Bid bid1 = bid(100);
		auction.bid(bid1);
		assertThat(auction.getBids().size(), is(1));
		assertThat(auction.getBids().get(0).getStatus(), is(BidStatus.成功));
		assertThat(auction.getMaxBidPrice(), is(100));
		Bid bid2 = bid(200);
		auction.bid(bid2);
		assertThat(auction.getBids().size(), is(2));
		assertThat(auction.getBids().get(1).getStatus(), is(BidStatus.成功));
		assertThat(auction.getMaxBidPrice(), is(200));
	}
}
