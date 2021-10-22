package l2r.gameserver.model.actor.instance.PcInstance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l2r.L2DatabaseFactory;
import l2r.features.achievementEngine.AchievementNotification;
import l2r.features.achievementEngine.Achievements;
import l2r.gameserver.model.actor.instance.L2PcInstance;

public class L2PlayerCounters extends L2PlayerExtension
{
	// Player
	private int _highest_karma = 0;
	private int _player_died = 0;
	private int _players_ressurected = 0;
	private int _duels_win = 0;
	
	private int _summons_kiled = 0;
	
	private int _recipes_crafted = 0;
	private int _recipes_failed = 0;
	
	private int _longest_spree = 0;
	private int _sprees_ended = 0;
	
	private int _crits_done = 0;
	private int _mcrits_done = 0;
	
	private long _most_adena_had = 0;
	private int _achivments_done = 0;
	private int _seeds_extracted = 0;
	
	// Enchant
	public int enchantNormalSucceeded = 0;
	public int enchantBlessedSucceeded = 0;
	public int highestEnchant = 0;
	
	// Clan
	private int _sieges_won = 0;
	private int _fortresses_won = 0;
	
	// Epic Bosses Kills.
	public int antharasKilled = 0;
	public int baiumKilled = 0;
	public int valakasKilled = 0;
	public int orfenKilled = 0;
	public int antQueenKilled = 0;
	public int coreKilled = 0;
	public int belethKilled = 0;
	public int sailrenKilled = 0;
	public int baylorKilled = 0;
	public int zakenKilled = 0;
	public int tiatKilled = 0;
	public int freyaKilled = 0;
	public int frintezzaKilled = 0;
	// Other Kills
	private int _mobs_killed = 0;
	private int _raids_killed = 0;
	
	// neeeds SQL support
	private int _fish_catched = 0;
	private int _treasure_box_opened = 0;
	
	public L2PlayerCounters(L2PcInstance activeChar)
	{
		super(activeChar);
	}
	
	public void onKarma(int _karma)
	{
		if (_highest_karma < _karma)
		{
			_highest_karma = _karma;
		}
	}
	
	public void onEnchant(int _karma)
	{
		if (highestEnchant < _karma)
		{
			highestEnchant = _karma;
		}
	}
	
	public void onBlessedScrollSucceed()
	{
		enchantBlessedSucceeded++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onNormalScrollSucceed()
	{
		enchantNormalSucceeded++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onAntharasKill()
	{
		antharasKilled++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onBaiumKill()
	{
		baiumKilled++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onValakasKill()
	{
		valakasKilled++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onOrfenKill()
	{
		orfenKilled++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onAntQueenKill()
	{
		antQueenKilled++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onCoreKill()
	{
		coreKilled++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onBelethKill()
	{
		belethKilled++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onSailrenKill()
	{
		sailrenKilled++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onBaylorKill()
	{
		baylorKilled++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onTiatKill()
	{
		tiatKilled++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onZakenKill()
	{
		zakenKilled++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onFreyaKill()
	{
		freyaKilled++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onFrintezzaKill()
	{
		frintezzaKilled++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onDie()
	{
		_player_died++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onRessurect()
	{
		_players_ressurected++;
	}
	
	public void onDuelWin()
	{
		_duels_win++;
	}
	
	public void onMobKill()
	{
		_mobs_killed++;
		AchievementNotification.update(getChar(), 0);
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onRaidKill()
	{
		_raids_killed++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onSummonKill()
	{
		_summons_kiled++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onFailCraft()
	{
		_recipes_failed++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onGoodCraft()
	{
		_recipes_crafted++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onSpree(int spree)
	{
		if (spree > _longest_spree)
		{
			_longest_spree++;
			Achievements.getInstance().checkAchievements(getChar());
		}
	}
	
	public void onEndSpree()
	{
		_sprees_ended++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onCHit()
	{
		_crits_done++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onMCHit()
	{
		_mcrits_done++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onSiegeWin()
	{
		_sieges_won++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onFortWin()
	{
		_fortresses_won++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onAdena(long adena)
	{
		if (adena > _most_adena_had)
		{
			_most_adena_had = adena;
			Achievements.getInstance().checkAchievements(getChar());
		}
	}
	
	public void onAchivmentDone()
	{
		_achivments_done++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onSeedExtract()
	{
		_seeds_extracted++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onCatchFish()
	{
		_fish_catched++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public void onTreasureBox()
	{
		_treasure_box_opened++;
		Achievements.getInstance().checkAchievements(getChar());
	}
	
	public int getPoints(String type)
	{
		// Miscellaneous
		if (type.equals("seedkill"))
		{
			return _seeds_extracted;
		}
		if (type.equals("catchFish"))
		{
			return _fish_catched;
		}
		if (type.equals("treasureBox"))
		{
			return _treasure_box_opened;
		}
		if (type.equals("ress"))
		{
			return _players_ressurected;
		}
		if (type.equals("mcrit"))
		{
			return _mcrits_done;
		}
		if (type.equals("crit"))
		{
			return _crits_done;
		}
		if (type.equals("mostadena"))
		{
			return (int) _most_adena_had;
		}
		if (type.equals("craft"))
		{
			return _recipes_crafted;
		}
		if (type.equals("craftFailed"))
		{
			return _recipes_failed;
		}
		if (type.equals("highKarma"))
		{
			return _highest_karma;
		}
		if (type.equals("achievementDone"))
		{
			return _achivments_done;
		}
		if (type.equals("spreesEnded"))
		{
			return _sprees_ended;
		}
		
		// Player
		if (type.equals("pvpkill"))
		{
			return getChar().getPvpKills();
		}
		if (type.equals("killingspree"))
		{
			return _longest_spree;
		}
		if (type.equals("summonkill"))
		{
			return _summons_kiled;
		}
		if (type.equals("die"))
		{
			return _player_died;
		}
		if (type.equals("duelKill"))
		{
			return _duels_win;
		}
		
		// siege
		if (type.equals("siegewon"))
		{
			return _sieges_won;
		}
		if (type.equals("fortressewon"))
		{
			return _fortresses_won;
		}
		
		// Enchant
		if (type.equals("highestEnchant"))
		{
			return highestEnchant;
		}
		if (type.equals("enchantBlessSuccess"))
		{
			return enchantBlessedSucceeded;
		}
		if (type.equals("enchantNormalSuccess"))
		{
			return enchantNormalSucceeded;
		}
		
		// Special mobs-raids
		if (type.equals("antharasKill"))
		{
			return antharasKilled;
		}
		if (type.equals("baiumKill"))
		{
			return baiumKilled;
		}
		if (type.equals("valakasKill"))
		{
			return valakasKilled;
		}
		if (type.equals("orfenKill"))
		{
			return orfenKilled;
		}
		if (type.equals("antQueenKill"))
		{
			return antQueenKilled;
		}
		if (type.equals("coreKill"))
		{
			return coreKilled;
		}
		if (type.equals("belethKill"))
		{
			return belethKilled;
		}
		if (type.equals("sailrenKill"))
		{
			return sailrenKilled;
		}
		if (type.equals("baylorKill"))
		{
			return baylorKilled;
		}
		if (type.equals("zakenKill"))
		{
			return zakenKilled;
		}
		if (type.equals("tiatKill"))
		{
			return tiatKilled;
		}
		if (type.equals("freyaKill"))
		{
			return freyaKilled;
		}
		if (type.equals("frintezzaKill"))
		{
			return frintezzaKilled;
		}
		
		// Other Kills
		if (type.equals("mobkill"))
		{
			return _mobs_killed;
		}
		if (type.equals("raidkill"))
		{
			return _raids_killed;
		}
		
		return 0;
	}
	
	public void save()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			//@formatter:off
			PreparedStatement statement = con.prepareStatement(""
				+ "UPDATE character_counters SET highest_karma=?,times_died=?,mobs_killed=?,raids_killed=?,summons_killed=?,recipes_crafted=?,recipes_failed=?,longest_spree=?,sprees_ended=?,crits_done=?,mscrits_done=?,sieges_won=?,fortresses_won=?,most_adena=?,achievments_done=?,duels_won=?,players_ressurected=?,seeds_extracted=?,fish_catched=?,treasure_box_opened=?,"
				+ "antharasKilled=?,baiumKilled=?,valakasKilled=?,orfenKilled=?,antQueenKilled=?,"
				+ "coreKilled=?,belethKilled=?,sailrenKilled=?,baylorKilled=?,zakenKilled=?,"
				+ "tiatKilled=?,freyaKilled=?,frintezzaKilled=?,"
				+ "highestEnchant=?,enchantBlessedSucceeded=?,enchantNormalSucceeded=?"
				+ " WHERE char_id=? LIMIT 1"))
			//@formatter:o
		{
			statement.setInt(1, _highest_karma);
			statement.setInt(2, _player_died);
			statement.setInt(3, _mobs_killed);
			statement.setInt(4, _raids_killed);
			statement.setInt(5, _summons_kiled);
			statement.setInt(6, _recipes_crafted);
			statement.setInt(7, _recipes_failed);
			statement.setInt(8, _longest_spree);
			statement.setInt(9, _sprees_ended);
			statement.setInt(10, _crits_done);
			statement.setInt(11, _mcrits_done);
			statement.setInt(12, _sieges_won);
			statement.setInt(13, _fortresses_won);
			statement.setLong(14, _most_adena_had);
			statement.setInt(15, _achivments_done);
			statement.setInt(16, _duels_win);
			statement.setInt(17, _players_ressurected);
			statement.setInt(18, _seeds_extracted);
			statement.setInt(19, _fish_catched);
			statement.setInt(20, _treasure_box_opened);
			
			// Epic bosses
			statement.setInt(21, antharasKilled);
			statement.setInt(22, baiumKilled);
			statement.setInt(23, valakasKilled);
			statement.setInt(24, orfenKilled);
			statement.setInt(25, antQueenKilled);
			statement.setInt(26, coreKilled);
			statement.setInt(27, belethKilled);
			statement.setInt(28, sailrenKilled);
			statement.setInt(29, baylorKilled);
			statement.setInt(30, zakenKilled);
			statement.setInt(31, tiatKilled);
			statement.setInt(32, freyaKilled);
			statement.setInt(33, frintezzaKilled);
			
			// Enchants
			statement.setInt(34, highestEnchant);
			statement.setInt(35, enchantBlessedSucceeded);
			statement.setInt(36, enchantNormalSucceeded);
			
			statement.setInt(37, getChar().getObjectId());
			statement.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void load()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement obj = con.prepareStatement("SELECT * FROM character_counters WHERE char_id = ?"))
		{
			obj.setInt(1, getChar().getObjectId());
			
			try (ResultSet rs = obj.executeQuery())
			{
			while (rs.next())
			{
				_highest_karma = rs.getInt("highest_karma");
				_player_died = rs.getInt("times_died");
				_mobs_killed = rs.getInt("mobs_killed");
				_raids_killed = rs.getInt("raids_killed");
				_summons_kiled = rs.getInt("summons_killed");
				_recipes_crafted = rs.getInt("recipes_crafted");
				_recipes_failed = rs.getInt("recipes_failed");
				_longest_spree = rs.getInt("longest_spree");
				_sprees_ended = rs.getInt("sprees_ended");
				_crits_done = rs.getInt("crits_done");
				_mcrits_done = rs.getInt("mscrits_done");
				_sieges_won = rs.getInt("sieges_won");
				_fortresses_won = rs.getInt("fortresses_won");
				_most_adena_had = rs.getLong("most_adena");
				_achivments_done = rs.getInt("achievments_done");
				_duels_win = rs.getInt("duels_won");
				_players_ressurected = rs.getInt("players_ressurected");
				_seeds_extracted = rs.getInt("seeds_extracted");
				_fish_catched = rs.getInt("fish_catched");
				_treasure_box_opened = rs.getInt("treasure_box_opened");
				
				// Epic bosses
				antharasKilled = rs.getInt("antharasKilled");
				baiumKilled = rs.getInt("baiumKilled");
				valakasKilled = rs.getInt("valakasKilled");
				orfenKilled = rs.getInt("orfenKilled");
				antQueenKilled = rs.getInt("antQueenKilled");
				coreKilled = rs.getInt("coreKilled");
				belethKilled = rs.getInt("belethKilled");
				sailrenKilled = rs.getInt("sailrenKilled");
				baylorKilled = rs.getInt("baylorKilled");
				zakenKilled = rs.getInt("zakenKilled");
				tiatKilled = rs.getInt("tiatKilled");
				freyaKilled = rs.getInt("freyaKilled");
				frintezzaKilled = rs.getInt("frintezzaKilled");
				
				// Enchants
				highestEnchant = rs.getInt("highestEnchant");
				enchantBlessedSucceeded = rs.getInt("enchantBlessedSucceeded");
				enchantNormalSucceeded = rs.getInt("enchantNormalSucceeded");
			}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
