package l2r.features.achievementEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilderFactory;

import l2r.features.achievementEngine.voice.AchievementVcmd;
import l2r.gameserver.handler.VoicedCommandHandler;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.MagicSkillUse;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.util.Files;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Achievements
{
	// id-max
	public Map<Integer, Integer> _achivmentsLevels = new ConcurrentHashMap<>();
	public Set<IAchievementCategory> _achivmentsCat = ConcurrentHashMap.newKeySet();
	private final String FullPage = Files.read("data/html/achievements/inAchievements.htm");
	private static Achievements _instance;
	
	public Achievements()
	{
		load();
	}
	
	public void usebypass(L2PcInstance player, String bypass, String[] cm)
	{
		if (bypass.startsWith("_bbs_achivments_cat"))
		{
			generatePage(player, Integer.parseInt(cm[1]));
		}
		else if (bypass.equals("_bbs_achivments_close"))
		{
			AchievementNotification.closeTutorialHTML(player);
		}
		else
		{
			generatePagr(player);
		}
	}
	
	public void generatePagr(L2PcInstance player)
	{
		if (player == null)
		{
			return;
		}
		
		NpcHtmlMessage msg = new NpcHtmlMessage();
		String achivments = Files.read("data/html/achievements/Achievements.htm");
		String ac = "";
		
		for (IAchievementCategory a : _achivmentsCat)
		{
			ac += a.getHtml(player.getAchivmentsLevel(a.getId()));
		}
		
		msg.setHtml(achivments.toString());
		msg.replace("%categories%", ac);
		player.sendPacket(msg);
	}
	
	public void generatePage(L2PcInstance player, int category)
	{
		if (player == null)
		{
			return;
		}
		
		NpcHtmlMessage msg = new NpcHtmlMessage();
		boolean done;
		String achivmentsNotDone = "";
		String achivmentsDone = "";
		int playerPoints = 0;
		String html;
		
		IAchievementCategory cat = getCatById(category);
		
		if (cat == null)
		{
			return;
		}
		
		for (Entry<Integer, Integer> arc : player.getAchivments(category).entrySet())
		{
			int aId = arc.getKey();
			int aLevel = arc.getValue();
			IAchievement a = GetAchivment(aId, aLevel + 1);
			
			if (a == null)
			{
				return;
			}
			
			playerPoints = getRealPoints(player.getCounters().getPoints(a.getType()), aId, aLevel);
			
			if (getMaxLevel(aId) != aLevel)
			{
				done = false;
				html = a.getNotDoneHtml(player, playerPoints);
			}
			else
			{
				done = true;
				html = a.getDoneHtml();
			}
			
			if (!done)
			{
				achivmentsNotDone += html;
			}
			else
			{
				achivmentsDone += html;
			}
		}
		
		int barup = cat.calculteBar(248, player.getAchivmentsLevel(category));
		String fp = FullPage;
		fp = fp.replaceAll("%bar1up%", "" + barup);
		fp = fp.replaceAll("%bar2up%", "" + (248 - barup));
		
		fp = fp.replaceFirst("%caps1%", barup > 0 ? "Gauge_DF_Large_MP_Left" : "Gauge_DF_Large_Exp_bg_Left");
		
		fp = fp.replaceFirst("%caps2%", barup >= 248 ? "Gauge_DF_Large_MP_Right" : "Gauge_DF_Large_Exp_bg_Right");
		
		fp = fp.replaceFirst("%achivmentsNotDone%", achivmentsNotDone);
		fp = fp.replaceFirst("%achivmentsDone%", achivmentsDone);
		fp = fp.replaceFirst("%catname%", cat.getName());
		fp = fp.replaceFirst("%catDesc%", cat.getDesc());
		fp = fp.replaceFirst("%catIcon%", cat.getIcon());
		
		msg.setHtml(fp.toString());
		player.sendPacket(msg);
	}
	
	public void checkAchievements(L2PcInstance player)
	{
		if (player.isBussy())
		{
			return;
		}
		
		for (Entry<Integer, Integer> arco : player.getAchivments().entrySet())
		{
			if (getMaxLevel(arco.getKey()) == arco.getValue())
			{
				continue;
			}
			
			IAchievement arc = GetAchivment(arco.getKey(), arco.getValue() + 1);
			if (arc.maybeDone(getRealPoints(player.getCounters().getPoints(arc._type), arco.getKey(), arco.getValue())))
			{
				reward(player, arc);
			}
		}
	}
	
	public void reward(L2PcInstance player, IAchievement arc)
	{
		player.setBussy(true);
		String messageToSend = "You complete '" + arc.getName() + "' Achievement.";
		player.sendMessageS(messageToSend, 5);
		player.getAchivments().put(arc.getId(), arc.getLevel());
		for (Entry<Integer, Long> item : arc.getRewards().entrySet())
		{
			player.addItem("Achievement", item.getKey(), item.getValue(), player, true);
		}
		player.setFame(player.getFame() + arc._fame);
		player.broadcastPacket(new MagicSkillUse(player, player, 2527, 1, 0, 500));
		player.getCounters().onAchivmentDone();
		player.setBussy(false);
	}
	
	public int getRealPoints(int allpoints, int id, int level)
	{
		if (allpoints == 0)
		{
			return 0;
		}
		
		int result = 0;
		for (int i = level; i > 0; i--)
		{
			IAchievement a = GetAchivment(id, i);
			if (a != null)
			{
				result += a.getNeedPoints();
			}
		}
		return allpoints - result;
	}
	
	public IAchievementCategory getCatById(int id)
	{
		for (IAchievementCategory ach : _achivmentsCat)
		{
			if (ach.getId() == id)
			{
				return ach;
			}
		}
		return null;
	}
	
	public IAchievement GetAchivment(int id, int level)
	{
		for (IAchievementCategory cat : _achivmentsCat)
		{
			for (IAchievement ipo : cat.getAchievements())
			{
				if ((ipo.getId() == id) && (ipo.getLevel() == level))
				{
					return ipo;
				}
			}
		}
		return GetAchivment(id, getMaxLevel(id));
	}
	
	public IAchievement GetAchivment(int id)
	{
		for (IAchievementCategory cat : _achivmentsCat)
		{
			for (IAchievement ipo : cat.getAchievements())
			{
				if (ipo.getId() == id)
				{
					return ipo;
				}
			}
		}
		return GetAchivment(id, getMaxLevel(id));
	}
	
	public IAchievement GetAchivmentWithNull(int id, int level)
	{
		for (IAchievementCategory cat : _achivmentsCat)
		{
			for (IAchievement ipo : cat.getAchievements())
			{
				if ((ipo.getId() == id) && (ipo.getLevel() == level))
				{
					return ipo;
				}
			}
		}
		return null;
	}
	
	public int getMaxLevel(int id)
	{
		return _achivmentsLevels.get(id);
	}
	
	public void load()
	{
		VoicedCommandHandler.getInstance().registerHandler(new AchievementVcmd());
		_achivmentsLevels.clear();
		_achivmentsCat.clear();
		try
		{
			ArrayList<IAchievement> _tempachivments = new ArrayList<>();
			HashMap<Integer, Integer> _tempRewards = new HashMap<>();
			
			IAchievementCategory cat = new IAchievementCategory();
			int lastlevel = 0;
			int lastid = 0;
			int lastcat = 0;
			double mp = 0;
			
			File file = new File("data/xml/sunrise/achievement.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			Document doc = factory.newDocumentBuilder().parse(file);
			for (Node g = doc.getFirstChild(); g != null; g = g.getNextSibling())
			{
				for (Node z = g.getFirstChild(); z != null; z = z.getNextSibling())
				{
					if (z.getNodeName().equals("categories"))
					{
						for (Node i = z.getFirstChild(); i != null; i = i.getNextSibling())
						{
							for (Node o = i.getFirstChild(); o != null; o = o.getNextSibling())
							{
								if ("kitekat".equalsIgnoreCase(i.getNodeName()))
								{
									if ("cat".equalsIgnoreCase(o.getNodeName()))
									{
										cat._id = Integer.valueOf(o.getAttributes().getNamedItem("id").getNodeValue());
										cat._name = String.valueOf(o.getAttributes().getNamedItem("name").getNodeValue());
										cat._icon = String.valueOf(o.getAttributes().getNamedItem("icon").getNodeValue());
										cat._desc = String.valueOf(o.getAttributes().getNamedItem("desc").getNodeValue());
										_achivmentsCat.add(cat);
									}
								}
								cat = new IAchievementCategory();
							}
						}
					}
					else if (z.getNodeName().equals("achievement"))
					{
						for (Node i = z.getFirstChild(); i != null; i = i.getNextSibling())
						{
							int _id = Integer.valueOf(z.getAttributes().getNamedItem("id").getNodeValue());
							int _cat = Integer.valueOf(z.getAttributes().getNamedItem("cat").getNodeValue());
							String _desc = String.valueOf(z.getAttributes().getNamedItem("desc").getNodeValue());
							String _type = String.valueOf(z.getAttributes().getNamedItem("type").getNodeValue());
							
							for (Node o = i.getFirstChild(); o != null; o = o.getNextSibling())
							{
								if ("levels".equalsIgnoreCase(i.getNodeName()))
								{
									if ("lvl".equalsIgnoreCase(o.getNodeName()))
									{
										IAchievement achivment = new IAchievement();
										achivment._id = _id;
										achivment._cat = _cat;
										achivment._type = _type;
										achivment._desc = _desc;
										achivment._level = Integer.valueOf(o.getAttributes().getNamedItem("id").getNodeValue());
										achivment._needPoints = Integer.valueOf(o.getAttributes().getNamedItem("need").getNodeValue());
										achivment._fame = Integer.valueOf(o.getAttributes().getNamedItem("fame").getNodeValue());
										achivment._name = String.valueOf(o.getAttributes().getNamedItem("name").getNodeValue());
										achivment._icon = String.valueOf(o.getAttributes().getNamedItem("icon").getNodeValue());
										_tempachivments.add(achivment);
										lastlevel = achivment._level;
										lastid = achivment._id;
										lastcat = achivment._cat;
									}
								}
								if ("rewards".equalsIgnoreCase(i.getNodeName()))
								{
									mp = Double.valueOf(i.getAttributes().getNamedItem("multiplayer").getNodeValue());
									
									if ("item".equalsIgnoreCase(o.getNodeName()))
									{
										int Itemid = Integer.valueOf(o.getAttributes().getNamedItem("id").getNodeValue());
										int Itemcount = Integer.valueOf(o.getAttributes().getNamedItem("count").getNodeValue());
										_tempRewards.put(Itemid, Itemcount);
									}
								}
							}
						}
						for (IAchievement o : _tempachivments)
						{
							for (Entry<Integer, Integer> rr : _tempRewards.entrySet())
							{
								o.addReward(rr.getKey(), Math.round(rr.getValue() * mp));
							}
						}
						
						getCatById(lastcat).getAchievements().addAll(_tempachivments);
						_tempachivments.clear();
						_achivmentsLevels.put(lastid, lastlevel);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Achievements getInstance()
	{
		if (_instance == null)
		{
			_instance = new Achievements();
		}
		return _instance;
	}
}