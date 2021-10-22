package l2r.features.achievementEngine;

import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.TutorialCloseHtml;
import l2r.gameserver.network.serverpackets.TutorialShowHtml;
import l2r.gameserver.network.serverpackets.TutorialShowQuestionMark;
import l2r.util.Files;

public class AchievementNotification
{
	public static void update(L2PcInstance pl, int type)
	{
		if (pl.getVarB("noNotification"))
		{
			return;
		}
		
		IAchievement ooo = pl.getAchievementByType(TypeConverter(type + 100));
		
		if (ooo == null)
		{
			return;
		}
		
		int points = Achievements.getInstance().getRealPoints(pl.getCounters().getPoints(TypeConverter(type + 100)), ooo.getId(), ooo.getLevel() - 1);
		
		if (ooo.isDone(points, true))
		{
			closeTutorialHTML(pl);
			return;
		}
		
		if (!pl.achivment_nf_open)
		{
			showQuestionMark(pl, type + 100);
		}
		else
		{
			showTutorialHTML(pl, type + 100);
		}
	}
	
	public static final void onTutorialQuestionMark(L2PcInstance player, int number)
	{
		showTutorialHTML(player, number);
	}
	
	public static final void showQuestionMark(L2PcInstance player, int type)
	{
		player.sendPacket(new TutorialShowQuestionMark(type));
	}
	
	private static void showTutorialHTML(L2PcInstance player, int type)
	{
		player.achivment_nf_open = true;
		String text = Files.read("data/html/achievements/Notification.htm");
		
		IAchievement ooo = player.getAchievementByType(TypeConverter(type));
		
		if (ooo == null)
		{
			closeTutorialHTML(player);
			return;
		}
		
		int points = Achievements.getInstance().getRealPoints(player.getCounters().getPoints(TypeConverter(type)), ooo.getId(), ooo.getLevel() - 1);
		
		if ((ooo.getNeedPoints() - points) == 0)
		{
			closeTutorialHTML(player);
			return;
		}
		
		text = text.replaceFirst("%fame%", "" + ooo.getFame());
		text = text.replaceAll("%desc%", ooo.getDesc().replaceAll("%need%", "" + (ooo.getNeedPoints() - points)));
		text = text.replaceAll("%name%", ooo.getName() + " " + ooo.getLevel() + " lvl");
		text = text.replaceAll("%icon%", ooo.getIcon());
		
		int bluebar = (248 * ((points * 100) / ooo.getNeedPoints())) / 100;
		if (bluebar < 0)
		{
			bluebar = 0;
		}
		
		text = text.replaceAll("%bar1%", "" + bluebar);
		text = text.replaceAll("%bar2%", "" + (248 - bluebar));
		
		player.sendPacket(new TutorialShowHtml(text));
	}
	
	public static void closeTutorialHTML(L2PcInstance player)
	{
		player.achivment_nf_open = false;
		player.sendPacket(new TutorialCloseHtml());
	}
	
	public static String TypeConverter(int type)
	{
		switch (type)
		{
			case 100:
				return "mobkill";
			case 200:
				return "";
			case 300:
				return "";
		}
		return "";
	}
}