package org.bukkit.conversations;

/**
 * An ExactMatchConversationCanceller cancels a conversation if the user enters
 * an exact input string
 */
public class ExactMatchConversationCanceller implements ConversationCanceller {
	private String escapeSequence;

	/**
	 * Builds an ExactMatchConversationCanceller.
	 *
	 * @param escapeSequence The string that, if entered by the user, will cancel
	 *                       the conversation.
	 */
	public ExactMatchConversationCanceller(String escapeSequence) {
		this.escapeSequence = escapeSequence;
	}

	@Override
	public void setConversation(Conversation conversation) {
	}

	@Override
	public boolean cancelBasedOnInput(ConversationContext context, String input) {
		return input.equals(escapeSequence);
	}

	@Override
	public ConversationCanceller clone() {
		return new ExactMatchConversationCanceller(escapeSequence);
	}
}
