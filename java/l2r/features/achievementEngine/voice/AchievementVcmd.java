/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package l2r.features.achievementEngine.voice;

import l2r.gameserver.handler.IVoicedCommandHandler;
import l2r.gameserver.model.actor.instance.L2PcInstance;

public class AchievementVcmd implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"disableachievnot"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equalsIgnoreCase("disableachievnot"))
		{
			if (activeChar.getVarB("noNotification"))
			{
				activeChar.setVar("noNotification", "false");
				activeChar.sendMessage("Achievement Notifications disabled.");
			}
			else
			{
				activeChar.setVar("noNotification", "true");
				activeChar.sendMessage("Achievement Notifications enabled.");
			}
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}