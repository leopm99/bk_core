/*
 * Copyright (C) 2004-2015 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package l2r.gameserver.network.clientpackets;

import l2r.features.achievementEngine.AchievementNotification;
import l2r.gameserver.model.actor.instance.L2ClassMasterInstance;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.quest.Quest;
import l2r.gameserver.model.quest.QuestState;

public class RequestTutorialQuestionMark extends L2GameClientPacket
{
	private static final String _C__87_REQUESTTUTORIALQUESTIONMARK = "[C] 87 RequestTutorialQuestionMark";
	
	private int _number = 0;
	
	@Override
	protected void readImpl()
	{
		_number = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance player = getActiveChar();
		if (player == null)
		{
			return;
		}
		
		if (!player.getVarB("noNotification") && ((_number == 100) || (_number == 200) || (_number == 300)))
		{
			AchievementNotification.onTutorialQuestionMark(player, _number);
		}
		else
		{
			L2ClassMasterInstance.onTutorialQuestionMark(player, _number);
			
			final QuestState qs = player.getQuestState(Quest.TUTORIAL);
			if (qs != null)
			{
				qs.getQuest().notifyEvent("QM" + _number + "", null, player);
			}
		}
	}
	
	@Override
	public String getType()
	{
		return _C__87_REQUESTTUTORIALQUESTIONMARK;
	}
}
