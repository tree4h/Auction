package models;

import java.math.BigDecimal;
import java.util.Date;
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
public class Bid extends Model {
	@Id
    public Long id;
    @CreatedTimestamp
    public Date createDate;
    @Version
    public Date updateDate;
       
    @NotNull
    private Date bidDate;
    @NotNull
    private int bidPrice;
    @NotNull
    private BidStatus status;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "auction_id")
    private Auction auction;
    
    private DateFactory df = new DateFactory();
    
	public Bid(Member m, int p) {
		member = m;
		bidPrice = p;
		bidDate = new Date();
		status = BidStatus.入札前;
	}
	
	public Member getBidder() {
		return member;
	}

	public String getBidderName() {
		return member.getName();
	}
	
	public Date getDate() {
		return bidDate;
	}

	public String getDateString() {
		return DateFactory.format(bidDate);
	}

	public int getPrice() {
		return bidPrice;
	}
	
	public void setState(BidStatus bs) {
		status = bs;
	}

	public BidStatus getStatus() {
		return status;
	}

	public Long getAuction() {
		return auction.id;
	}
}
