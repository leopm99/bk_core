package l2r.features.achievementEngine;

import java.util.ArrayList;

import l2r.util.Files;

public class IAchievementCategory
{
	public ArrayList<IAchievement> _achievements = new ArrayList<>();
	int _id;
	public String _html;
	public String _name;
	public String _icon;
	public String _desc;
	
	public String getHtml(int totalPlayerLevel)
	{
		int bluebar = 0;
		
		if (totalPlayerLevel > 0)
		{
			bluebar = calculteBar(24, totalPlayerLevel);
		}
		
		String temp = Files.read("data/html/achievements/AchievementsCat.htm");
		temp = temp.replaceFirst("%bg%", (getId() % 2) == 0 ? "090908" : "0f100f");
		temp = temp.replaceFirst("%desc%", getDesc());
		temp = temp.replaceFirst("%icon%", getIcon());
		temp = temp.replaceFirst("%name%", getName());
		temp = temp.replaceFirst("%id%", "" + getId());
		
		temp = temp.replaceFirst("%caps1%", bluebar > 0 ? "Gauge_DF_MP_Left" : "Gauge_DF_Exp_bg_Left");
		temp = temp.replaceFirst("%caps2%", bluebar >= 24 ? "Gauge_DF_MP_Right" : "Gauge_DF_Exp_bg_Right");
		
		temp = temp.replaceAll("%bar1%", "" + bluebar);
		temp = temp.replaceAll("%bar2%", "" + (24 - bluebar));
		return temp;
	}
	
	public int calculteBar(int barmax, int totalPlayerLevel)
	{
		int c = (barmax * ((totalPlayerLevel * 100) / _achievements.size())) / 100;
		
		if (c >= barmax)
		{
			return barmax;
		}
		return c;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public ArrayList<IAchievement> getAchievements()
	{
		return _achievements;
	}
	
	public String getDesc()
	{
		return _desc;
	}
	
	public String getIcon()
	{
		return _icon;
	}
	
	public String getName()
	{
		return _name;
	}
}