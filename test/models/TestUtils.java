package models;

import java.util.Calendar;
import java.util.Date;

public class TestUtils {
	public static Auction auction(Date start, Date end, Member member, Item item) {
		return new Auction(start, end, member, item);
	}
	public static Bid bid(Member member, int price) {
		return new Bid(member, price);
	}

	//開催中のAuctionを返す
	public static Auction auction() {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.add(Calendar.DATE, -1);
		end.add(Calendar.DATE, 1);
		return auction(start.getTime(), end.getTime(), new Member("moriki"), new Item("item"));
	}
	//開催前のAuctionを返す
	public static Auction beforeAuction() {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.add(Calendar.DATE, 1);
		end.add(Calendar.DATE, 2);
		return auction(start.getTime(), end.getTime(), new Member("moriki"), new Item("item"));
	}
	//開催終了のAuctionを返す
	public static Auction afterAuction() {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.add(Calendar.DATE, -2);
		end.add(Calendar.DATE, -1);
		return auction(start.getTime(), end.getTime(), new Member("moriki"), new Item("item"));
	}
	//現在時刻から指定期間（秒）のAuctionを返す
	public static Auction auction(int second) {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		end.add(Calendar.SECOND, second);
		return auction(start.getTime(), end.getTime(), new Member("moriki"), new Item("item"));
	}
	
	public static Auction auction(Date start, Date end) {
		return auction(start, end, new Member("moriki"), new Item("item"));
	}

	//開催中のAuctionを返す
	public static Auction auction(Member member) {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.add(Calendar.DATE, -1);
		end.add(Calendar.DATE, 1);
		return auction(start.getTime(), end.getTime(), member, new Item("item"));
	}


	public static Bid bid() {
		return bid(new Member("moriki"), 1);
	}

	public static Bid bid(int price) {
		return bid(new Member("moriki"), price);
	}

	public static Bid bid(Member member) {
		return bid(member, 1);
	}
}
