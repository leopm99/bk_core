package l2r.features.achievementEngine;

import java.util.HashMap;

import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.util.Files;

public class IAchievement
{
	public int _id;
	public int _level;
	public String _name;
	public int _cat;
	public String _icon;
	public String _desc;
	public int _needPoints;
	public String _type;
	public int _fame;
	
	public HashMap<Integer, Long> _rewards = new HashMap<>();
	
	public boolean maybeDone(int PlayerPoints)
	{
		if (isDone(PlayerPoints, false))
		{
			return false;
		}
		
		if (PlayerPoints == _needPoints)
		{
			return true;
		}
		return false;
	}
	
	public boolean isDone(int PlayerPoints, boolean lygu)
	{
		if (!lygu)
		{
			if (PlayerPoints > _needPoints)
			{
				return true;
			}
		}
		else if (PlayerPoints >= _needPoints)
		{
			return true;
		}
		return false;
	}
	
	public String getNotDoneHtml(L2PcInstance pl, int playerPoints)
	{
		String oneAchivment = Files.read("data/html/achievements/oneAchievement.htm");
		
		int bluebar = (24 * ((playerPoints * 100) / _needPoints)) / 100;
		if (bluebar < 0)
		{
			bluebar = 0;
		}
		
		if (bluebar > 24)
		{
			fix(pl, playerPoints);
			pl.sendMessage("Applying fix for achievments.");
			return "";
		}
		
		oneAchivment = oneAchivment.replaceFirst("%fame%", "" + _fame);
		oneAchivment = oneAchivment.replaceAll("%bar1%", "" + bluebar);
		oneAchivment = oneAchivment.replaceAll("%bar2%", "" + (24 - bluebar));
		
		oneAchivment = oneAchivment.replaceFirst("%cap1%", bluebar > 0 ? "Gauge_DF_MP_Left" : "Gauge_DF_Exp_bg_Left");
		oneAchivment = oneAchivment.replaceFirst("%cap2%", "Gauge_DF_Exp_bg_Right");
		
		oneAchivment = oneAchivment.replaceFirst("%desc%", _desc.replaceAll("%need%", "" + (_needPoints - playerPoints)));
		
		oneAchivment = oneAchivment.replaceFirst("%bg%", (_id % 2) == 0 ? "090908" : "0f100f");
		oneAchivment = oneAchivment.replaceFirst("%icon%", _icon);
		oneAchivment = oneAchivment.replaceFirst("%name%", _name + " " + _level + "lvl");
		return oneAchivment;
	}
	
	private void fix(L2PcInstance pl, int playerPoints)
	{
		try
		{
			IAchievement arc = Achievements.getInstance().GetAchivment(_id, pl.getAchivmentLevelbyId(_id) + 1);
			if ((arc != null) && (playerPoints > arc.getNeedPoints()))
			{
				Achievements.getInstance().reward(pl, arc);
				fix(pl, playerPoints);
			}
		}
		catch (Exception e)
		{
			// just in case
		}
	}
	
	public String getDoneHtml()
	{
		String oneAchivment = Files.read("data/html/achievements/oneAchievement.htm");
		
		oneAchivment = oneAchivment.replaceFirst("%fame%", "" + _fame);
		oneAchivment = oneAchivment.replaceAll("%bar1%", "24");
		oneAchivment = oneAchivment.replaceAll("%bar2%", "0");
		
		oneAchivment = oneAchivment.replaceFirst("%cap1%", "Gauge_DF_MP_Left");
		oneAchivment = oneAchivment.replaceFirst("%cap2%", "Gauge_DF_MP_Right");
		
		oneAchivment = oneAchivment.replaceFirst("%desc%", "Done.");
		
		oneAchivment = oneAchivment.replaceFirst("%bg%", (_id % 2) == 0 ? "090908" : "0f100f");
		oneAchivment = oneAchivment.replaceFirst("%icon%", _icon);
		oneAchivment = oneAchivment.replaceFirst("%name%", _name + " " + _level + "lvl");
		return oneAchivment;
	}
	
	public HashMap<Integer, Long> getRewards()
	{
		return _rewards;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public String getDesc()
	{
		return _desc;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public int getLevel()
	{
		return _level;
	}
	
	public void addReward(int itemid, long itemcount)
	{
		_rewards.put(itemid, itemcount);
	}
	
	public String getType()
	{
		return _type;
	}
	
	public int getNeedPoints()
	{
		return _needPoints;
	}
	
	public int getCat()
	{
		return _cat;
	}
	
	public String getIcon()
	{
		return _icon;
	}
	
	public int getFame()
	{
		return _fame;
	}
}