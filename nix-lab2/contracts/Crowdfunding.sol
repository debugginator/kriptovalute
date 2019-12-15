pragma solidity ^0.4.24;

import "./Timer.sol";

/// This contract represents the most simple crowdfunding campaign.
/// This contract does not protect investors from not receiving goods
/// they were promised from the crowdfunding owner. This kind of contract
/// might be suitable for campaigns that do not promise anything to the
/// investors except that they will start working on the project.
/// (e.g. almost all blockchain spinoffs.)
contract Crowdfunding {

    address private owner;

    Timer private timer;

    uint256 public goal;

    uint256 public endTimestamp;

    mapping (address => uint256) public investments;

    constructor(
        address _owner,
        Timer _timer,
        uint256 _goal,
        uint256 _endTimestamp
    ) public {
        owner = _owner == 0 ? msg.sender : _owner;
        timer = _timer;
        goal = _goal;
        endTimestamp = _endTimestamp;
    }

    function invest() public payable {
        require(timer.getTime() < endTimestamp);
        require(msg.value > 0);

        investments[msg.sender] += msg.value;
    }

    function claimFunds() public {
        require(timer.getTime() >= endTimestamp);
        require(address(this).balance >= goal);
        require(msg.sender == owner);

        msg.sender.transfer(address(this).balance);
    }

    function refund() public {
        require(timer.getTime() >= endTimestamp);
        require(address(this).balance < goal);

        uint256 amount = investments[msg.sender];
        investments[msg.sender] = 0;
        msg.sender.transfer(amount);
    }
}
