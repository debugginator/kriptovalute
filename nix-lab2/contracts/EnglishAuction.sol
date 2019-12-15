pragma solidity ^0.4.24;

import "./Auction.sol";

contract EnglishAuction is Auction {

    uint internal highestBid;
    uint internal initialPrice;
    uint internal biddingPeriod;
    uint internal lastBidTimestamp;
    uint internal minimumPriceIncrement;

    address internal highestBidder;

    constructor(
        address _sellerAddress,
        address _judgeAddress,
        Timer _timer,
        uint _initialPrice,
        uint _biddingPeriod,
        uint _minimumPriceIncrement
    ) public Auction(_sellerAddress, _judgeAddress, _timer) {
        initialPrice = _initialPrice;
        biddingPeriod = _biddingPeriod;
        minimumPriceIncrement = _minimumPriceIncrement;
        lastBidTimestamp = time();
    }

    function bid() public payable {
        uint minimalPrice = highestBid != 0 ?
            highestBid + minimumPriceIncrement :
            initialPrice;

        require(msg.value >= minimalPrice);
        require(time() < lastBidTimestamp + biddingPeriod);

        if (highestBidder != address(0)) {
            // Refund last highest bidder
            highestBidder.transfer(highestBid);
        }

        highestBid = msg.value;
        highestBidder = msg.sender;
        lastBidTimestamp = time();
    }

    /// @dev Override
    /// Function that returns highest bidder address or address(0) if
    /// auction is not yet over.
    function getHighestBidder() public view returns (address) {
        if (time() >= lastBidTimestamp + biddingPeriod) {
            Outcome _outcome = highestBidder != address(0) ? Outcome.SUCCESSFUL : Outcome.NOT_SUCCESSFUL;
            finishAuction(_outcome, highestBidder);
        }

        return highestBidderAddress;
    }
}
