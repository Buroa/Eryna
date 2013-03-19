package org.apollo.game.model;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.event.impl.CloseInterfaceEvent;
import org.apollo.game.event.impl.EnterAmountEvent;
import org.apollo.game.event.impl.OpenInterfaceDialogueEvent;
import org.apollo.game.event.impl.OpenInterfaceEvent;
import org.apollo.game.event.impl.OpenInterfaceOverlayEvent;
import org.apollo.game.event.impl.OpenInterfaceSidebarEvent;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.model.inter.EnterAmountListener;
import org.apollo.game.model.inter.InterfaceListener;
import org.apollo.game.model.inter.dialog.DialogueListener;

/**
 * Represents the set of interfaces the player has open.
 * <p>
 * This class manages all six distinct types of interface (the last two are not
 * present on 317 servers).
 * </p>
 * <ul>
 * <li><strong>Windows:</strong> the ones people mostly associate with the word
 * interfaces. Things like your bank, the wildy warning screen, the trade
 * screen, etc.</li>
 * <li><strong>Overlays:</strong> display in the same place as windows, but
 * don't prevent you from moving. For example, the wilderness level indicator.</li>
 * <li><strong>Dialogues:</strong> interfaces which are displayed over the chat
 * box.</li>
 * <li><strong>Sidebars:</strong> an interface which displays over the inventory
 * area.</li>
 * <li><strong>Fullscreen windows:</strong> a window which displays over the
 * whole screen e.g. the 377 welcome screen.</li>
 * <li><strong>Fullscreen background:</strong> an interface displayed behind the
 * fullscreen window, typically a blank, black screen.</li>
 * </ul>
 * @author Graham
 */
public final class InterfaceSet {

	/**
	 * The player whose interfaces are being managed.
	 */
	private final Player player;

	/**
	 * A map of open interfaces.
	 */
	private final Map<InterfaceType, Integer> interfaces = new HashMap<InterfaceType, Integer>();

	/**
	 * The current listener.
	 */
	private InterfaceListener listener;

	/**
	 * The current enter amount listener.
	 */
	private EnterAmountListener amountListener;

	/**
	 * The current chat box dialogue listener.
	 */
	private DialogueListener dialogueListener;

	/**
	 * Creates an interface set.
	 * @param player The player.
	 */
	public InterfaceSet(Player player) {
		this.player = player;
	}

	/**
	 * Called when the player has clicked the specified button. Notifies the
	 * current interface listener.
	 * @param button The button's interface id.
	 * @return {@code true} if the event handler chain should be broken.
	 */
	public boolean buttonClicked(int button) {
		if (dialogueListener != null)
			return dialogueListener.buttonClicked(player, button);
		if (listener != null)
			if (listener.buttonClicked(player, button))
				return true;
		return false;
	}

	/**
	 * Closes the current open interface(s).
	 */
	public void close() {
		final int temp = interfaces.size();
		closeAndNotify(false);
		if (temp > 0)
			player.send(new CloseInterfaceEvent());
	}

	/**
	 * An internal method for closing the interface, notifying the listener if
	 * appropriate, but not sending any events.
	 * @param manually Flag for if the interface was closed manually (by the
	 * player).
	 */
	private void closeAndNotify(boolean manually) {
		amountListener = null;
		dialogueListener = null;
		interfaces.clear();
		if (listener != null) {
			listener.interfaceClosed(player, manually);
			listener = null;
		}
	}

	/**
	 * Checks if this interface sets contains the specified interface.
	 * @param id The interface's id.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean contains(int id) {
		return interfaces.containsValue(id);
	}

	/**
	 * Checks if this interface set contains the specified interface type.
	 * @param type The interface's type.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean contains(InterfaceType type) {
		return interfaces.containsKey(type);
	}

	/**
	 * Called when the player has clicked the "Click here to continue" button on
	 * a dialogue.
	 */
	public void continueRequested() {
		if (dialogueListener != null)
			dialogueListener.continued(player);
		else
			close();
	}

	/**
	 * Called when the client has entered the specified amount. Notifies the
	 * current listener.
	 * @param amount The amount.
	 */
	public void enteredAmount(int amount) {
		if (amountListener != null) {
			amountListener.amountEntered(amount);
			amountListener = null;
		}
	}

	/**
	 * Sent by the client when it has closed an interface.
	 */
	public void interfaceClosed() {
		closeAndNotify(true);
	}

	/**
	 * Sent by the client when it has closed an interface.
	 * @param closedInterface The interface that was closed.
	 */
	public void interfaceClosed(int closedInterface) {
		if (interfaces.containsValue(closedInterface))
			closeAndNotify(true);
	}

	/**
	 * Opens a chat box dialogue.
	 * @param listener The listener for the dialogue.
	 * @param dialogueId The dialogue's id.
	 */
	public void openDialogue(DialogueListener listener, int dialogueId) {
		closeAndNotify(false);
		this.dialogueListener = listener;
		this.listener = listener;
		interfaces.put(InterfaceType.DIALOGUE, dialogueId);
		player.send(new OpenInterfaceDialogueEvent(dialogueId));
	}

	/**
	 * Opens a chat box dialogue.
	 * @param dialogueId The dialogue's id.
	 */
	public void openDialogue(int dialogueId) {
		openDialogue(null, dialogueId);
	}

	/**
	 * Opens the enter amount dialog.
	 * @param listener The enter amount listener.
	 */
	public void openEnterAmountDialog(EnterAmountListener listener) {
		this.amountListener = listener;
		player.send(new EnterAmountEvent());
	}

	/**
	 * Opens a overlay window.
	 * @param windowId The window's id.
	 */
	public void openWalkable(int windowId) {
		interfaces.put(InterfaceType.OVERLAY, windowId);
		player.send(new OpenInterfaceOverlayEvent(windowId));
	}

	/**
	 * Opens a window.
	 * @param windowId The window's id.
	 */
	public void openWindow(int windowId) {
		openWindow(null, windowId);
	}

	/**
	 * Opens a window with the specified listener.
	 * @param listener The listener for this interface.
	 * @param windowId The window's id.
	 */
	public void openWindow(InterfaceListener listener, int windowId) {
		closeAndNotify(false);
		this.listener = listener;
		interfaces.put(InterfaceType.WINDOW, windowId);
		player.send(new OpenInterfaceEvent(windowId));
	}

	/**
	 * Opens a window and inventory sidebar.
	 * @param windowId The window's id.
	 * @param sidebarId The sidebar's id.
	 */
	public void openWindowWithSidebar(int windowId, int sidebarId) {
		openWindowWithSidebar(null, windowId, sidebarId);
	}

	/**
	 * Opens a window and inventory sidebar with the specified listener.
	 * @param listener The listener for this interface.
	 * @param windowId The window's id.
	 * @param sidebarId The sidebar's id.
	 */
	public void openWindowWithSidebar(InterfaceListener listener, int windowId, int sidebarId) {
		closeAndNotify(false);
		this.listener = listener;
		interfaces.put(InterfaceType.WINDOW, windowId);
		interfaces.put(InterfaceType.SIDEBAR, sidebarId);
		player.send(new OpenInterfaceSidebarEvent(windowId, sidebarId));
	}

	/**
	 * Returns the current listener and removes the old one.
	 * @return The listener.
	 */
	public InterfaceListener removeListener() {
		final InterfaceListener listener = this.listener;
		this.listener = null;
		return listener;
	}

	/**
	 * Sends an option.
	 * @param listener The dialogue listener.
	 * @param lines The lines.
	 */
	public void sendOption(DialogueListener listener, String... lines) {
		switch (lines.length) {
		case 2:
			setInterfaceText(2460, "Select an Option");
			setInterfaceText(2461, lines[0]);
			setInterfaceText(2462, lines[1]);
			openDialogue(listener, 2459);
			break;
		case 3:
			setInterfaceText(2470, "Select an Option");
			setInterfaceText(2471, lines[0]);
			setInterfaceText(2472, lines[1]);
			setInterfaceText(2473, lines[2]);
			openDialogue(listener, 2469);
			break;
		case 4:
			setInterfaceText(2482, lines[0]);
			setInterfaceText(2483, lines[1]);
			setInterfaceText(2484, lines[2]);
			setInterfaceText(2485, lines[3]);
			openDialogue(listener, 2480);
			break;
		}
	}

	/**
	 * Sends a statement.
	 * @param listener The dialogue listener.
	 * @param lines The lines.
	 */
	public void sendStatement(DialogueListener listener, String... lines) {
		switch (lines.length) {
		case 1:
			setInterfaceText(357, lines[0]);
			openDialogue(listener, 356);
			break;
		case 2:
			setInterfaceText(360, lines[0]);
			setInterfaceText(361, lines[1]);
			openDialogue(listener, 359);
			break;
		case 3:
			setInterfaceText(364, lines[0]);
			setInterfaceText(365, lines[1]);
			setInterfaceText(366, lines[2]);
			openDialogue(listener, 363);
			break;
		case 4:
			setInterfaceText(369, lines[0]);
			setInterfaceText(370, lines[1]);
			setInterfaceText(371, lines[2]);
			setInterfaceText(372, lines[3]);
			openDialogue(listener, 368);
			break;
		case 5:
			setInterfaceText(375, lines[0]);
			setInterfaceText(376, lines[1]);
			setInterfaceText(377, lines[2]);
			setInterfaceText(378, lines[3]);
			setInterfaceText(379, lines[4]);
			openDialogue(listener, 374);
			break;
		}
	}

	/**
	 * Sets the text on a window specified listener.
	 * @param interfaceId The window's id.
	 * @param text The text to be displayed
	 */
	public void setInterfaceText(int interfaceId, String text) {
		setInterfaceText(null, interfaceId, text);
	}

	/**
	 * Sets the text on a window specified listener.
	 * @param listener The listener for this interface.
	 * @param interfaceId The window's id.
	 * @param text The text to be displayed
	 */
	public void setInterfaceText(InterfaceListener listener, int interfaceId, String text) {
		closeAndNotify(false);
		this.listener = listener;
		interfaces.put(InterfaceType.DIALOGUE, interfaceId);
		player.send(new SetInterfaceTextEvent(interfaceId, text));
	}
}
