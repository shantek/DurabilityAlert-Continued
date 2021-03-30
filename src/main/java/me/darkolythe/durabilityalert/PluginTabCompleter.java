package de.darkolythe.durabilityalert.tabcompleter;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

// made by mindgam3s
public class PluginTabCompleter implements TabCompleter
{
	
	public static class TablistGroup
	{
		String permissionNode = "";
		ArrayList<String> tablistArgumentArrangement = new ArrayList<String>();
		
		public TablistGroup(ArrayList<String> m_tablistArgumentArrangement, String m_permissionNode)
		{
			tablistArgumentArrangement = m_tablistArgumentArrangement;
			permissionNode = m_permissionNode;
		}
	}
	
	
	private String commandString = "";
	private TablistGroup[] tabCompletions = null;
	
	
	public PluginTabCompleter(String m_command)
	{
		commandString = m_command;
	}
	
	
	public void setTabCompletions(TablistGroup[] m_tabCompletions)
	{
		tabCompletions = m_tabCompletions;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender m_sender, Command m_command, String m_alias, String[] m_args)
	{
		// check command		
		if ( !(m_command.getName().equalsIgnoreCase(commandString)))
		{
			return null;
		}
		
		// init tab completion list
		ArrayList<String> resultTablist = new ArrayList<String>();
		

		gatherTabCompletions : for (TablistGroup testCompletion : tabCompletions)
		{
			if (!m_sender.hasPermission(testCompletion.permissionNode))
			{
				// sender has no permission for this group anyways, so we can skip it
				continue gatherTabCompletions;
			}
			
			if (m_args.length > testCompletion.tablistArgumentArrangement.size())
			{
				// current argument list is longer than this groups list, so we can skip it
				continue gatherTabCompletions;
			}
			
			for (int index = 0; index < m_args.length - 1 ; index++)
			{
				if ( !(m_args[index].equalsIgnoreCase(testCompletion.tablistArgumentArrangement.get(index))))
				{
					// other arguments don't fit this group, so we can skip it
					continue gatherTabCompletions;
				}
			}
			
			String argument = testCompletion.tablistArgumentArrangement.get(m_args.length -1);
			
			if( !(argument.startsWith(m_args[m_args.length -1])))
			{
				//current argument does not partially match, so we can skip it
				continue gatherTabCompletions;
			}
			
			resultTablist.add(argument);
		}
		
		return resultTablist;
	}

}
