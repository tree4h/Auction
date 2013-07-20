package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.validation.NotNull;

import controllers.DateFactory;

import play.db.ebean.Model;

@Entity
public class Auction extends Model {
	@Id
    public Long id;
    @CreatedTimestamp
    public Date createDate;
    @Version
    public Date updateDate;
       
    @NotNull
    private Date start;
    @NotNull
    private Date end;
    @NotNull
    private int minBidPrice = 0;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    private List<Bid> bids = new LinkedList<Bid>();
    @NotNull
    private AuctionStatus status;
    //@NotNull
    //private AuctionState state;
    
    private DateFactory df = new DateFactory();
    
	public Auction(Date d1, Date d2,Member m,Item i) {
		start = d1;
		end = d2;
		member = m;
		item = i;
		status = AuctionStatus.開始待ち;
		//this.setState(new AuctionState01());
	}
	
	//public void setState(AuctionState newState) {
	//	this.state = newState;
	//}
	
	public void bid(Bid bid) {
		if(bid.getDate().before(start)) {
			bid.setState(BidStatus.失敗);
		}
		else if(bid.getDate().after(end)) {
			bid.setState(BidStatus.失敗);
		}
		else if(bid.getPrice() <= this.getMaxBidPrice()) {
			bid.setState(BidStatus.失敗);
		}
		else if(bid.getBidder().equals(member)) {
			bid.setState(BidStatus.失敗);
		}
		else {
			bid.setState(BidStatus.成功);
		}
		bids.add(bid);
		//save();
	}

	public AuctionStatus getStatus() {
		//Date current = df.newDate();
		if(status == AuctionStatus.不成立) {
			return status;
		}
		if(status == AuctionStatus.落札) {
			return status;
		}
		
		Date current = new Date();
		if(current.before(start)) {
			return status;
		}
		if(current.after(end)) {
			this.finishAuction();
			//save();
			return status;
		}
		status = AuctionStatus.出品中;
		//save();//きもい
		return status;
	}
	
	public int getAvailableTime() {
		long d1 = (new Date()).getTime();
		long d2 = end.getTime();
		long diffMinute = (d2-d1)/(1000*60);
		int result = (int)diffMinute;
		if(result < 0) {
			return 0;
		}
		return result;
	}

	public int getMaxBidPrice() {
		if(this.isSuccessful()) {
			int i = this.getMaxBidIndex();
			return bids.get(i).getPrice();
		}
		else {
			return minBidPrice;
		}
	}

	public String getStartDate() {
		return DateFactory.format(start);
	}
	
	public String getEndDate() {
		return DateFactory.format(end);
	}

	public String getItemName() {
		return item.getName();
	}

	public String getExhibitorName() {
		return member.getName();
	}

	public List<Bid> getBids() {
		return bids;
	}

	/*
	public Bid getSuccessfulBid() {
		for(int i=0; i<bids.size(); i++) {
			Bid bid = bids.get(i);
			if(bid.getState().equals(BidState.落札.toString())) {
				return bid;
			}
		}
		return false;
	}
	*/

	private void finishAuction() {
		if(this.isSuccessful()) {
			status = AuctionStatus.落札;
			int i = this.getMaxBidIndex();
			bids.get(i).setState(BidStatus.落札);
		}
		else {
			status = AuctionStatus.不成立;
		}
	}
	
	private boolean isSuccessful() {
		for(int i=0; i<bids.size(); i++) {
//			String target = bids.get(i).getState();
//			if(target.equals(BidState.成功.toString()) || target.equals(BidState.落札.toString()) ) {
//				return true;
//			}
			BidStatus bidState = bids.get(i).getStatus();
			if(bidState == BidStatus.成功 || bidState == BidStatus.落札 ) {
				return true;
			}
		}
		return false;
	}

	private int getMaxBidIndex() {
		int maxPrice = this.minBidPrice;
		int max = 0;
		for(int i=0; i<bids.size(); i++) {
			Bid target = bids.get(i);
			int price = target.getPrice();
			if(target.getStatus() == BidStatus.成功 || target.getStatus() == BidStatus.落札) {
				if(maxPrice < price) {
					maxPrice = price;
					max = i;
				}
			}
		}
		return max;
	}

}
