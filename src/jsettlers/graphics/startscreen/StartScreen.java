package jsettlers.graphics.startscreen;

import go.graphics.GLDrawContext;
import go.graphics.RedrawListener;
import go.graphics.UIPoint;
import go.graphics.event.GOEvent;
import go.graphics.event.GOEventHandler;
import go.graphics.event.command.GOCommandEvent;
import go.graphics.text.EFontSize;

import java.util.LinkedList;

import jsettlers.common.images.EImageLinkType;
import jsettlers.common.images.ImageLink;
import jsettlers.common.position.FloatRectangle;
import jsettlers.graphics.SettlersContent;
import jsettlers.graphics.action.Action;
import jsettlers.graphics.action.EActionType;
import jsettlers.graphics.localization.Labels;
import jsettlers.graphics.map.controls.original.panel.content.UILabeledButton;
import jsettlers.graphics.map.draw.ImageProvider;
import jsettlers.graphics.startscreen.IStartScreenConnector.IGameSettings;
import jsettlers.graphics.startscreen.IStartScreenConnector.ILoadableGame;
import jsettlers.graphics.utils.UIPanel;

public class StartScreen implements SettlersContent {
	private static final ImageLink BACKGROUND = new ImageLink(
	        EImageLinkType.GUI, 2, 29, 0);

	private final LinkedList<RedrawListener> redrawListeners =
	        new LinkedList<RedrawListener>();
	private final IStartScreenConnector connector;
	private final UIPanel root;
	private final LinkedList<UILabeledButton> mainButtons =
	        new LinkedList<UILabeledButton>();
	private final UIPanel content;

	private NewGamePanel newGamePanel;

	private GOEventHandler commandHandler = new GOEventHandler() {
		@Override
		public void phaseChanged(GOEvent event) {
		}

		@Override
		public void finished(GOEvent event) {
			GOCommandEvent c = (GOCommandEvent) event;
			UIPoint position = c.getCommandPosition();
			performActionAt(position.getX(), position.getY());
		}

		@Override
		public void aborted(GOEvent event) {
		}
	};

	private LoadSavedGamePanel loadSavedGamePanel;

	public StartScreen(IStartScreenConnector connector) {
		this.connector = connector;
		root = new UIPanel();
		// root.setBackground(new ImageLink(EImageLinkType.GUI, 2, 29, 0));

		addMainButton(EActionType.SHOW_START_NEW, .9f);
		addMainButton(EActionType.SHOW_LOAD, .75f);
		addMainButton(EActionType.SHOW_START_NETWORK, .6f);
		addMainButton(EActionType.SHOW_CONNECT_NETWORK, .45f);
		addMainButton(EActionType.SHOW_RECOVER_NETWORK, .3f);

		content = new UIPanel();
		root.addChild(content, .55f, .05f, .95f, .95f);

		displayContent(EActionType.SHOW_START_NEW);
	}

	/**
	 * Performs a action
	 * 
	 * @param x
	 *            in screen space.
	 * @param y
	 */
	protected void performActionAt(double x, double y) {
		float realx = (float) x / root.getPosition().getWidth();
		float realy = (float) y / root.getPosition().getHeight();
		Action action = root.getAction(realx, realy);
		if (action == null) {
			return;
		}
		if (action instanceof ExecutableAction) {
			((ExecutableAction) action).execute();
		}

		switch (action.getActionType()) {
			case SHOW_CONNECT_NETWORK:
			case SHOW_LOAD:
			case SHOW_RECOVER_NETWORK:
			case SHOW_START_NETWORK:
			case SHOW_START_NEW:
				displayContent(action.getActionType());
				break;

			case START_NEW_GAME:
				if (newGamePanel != null) {
					IGameSettings gameSettings = newGamePanel.getGameSettings();
					if (gameSettings != null) {
						connector.startNewGame(gameSettings);
					}
				}
				break;

			case LOAD_GAME:
				if (loadSavedGamePanel != null) {
					ILoadableGame load = loadSavedGamePanel.getSelected();
					if (load != null) {
						connector.loadGame(load);
					}
				}
				break;
		}

		requestRedraw();
	}

	private void displayContent(EActionType displayAction) {
		content.removeAll();

		if (displayAction == EActionType.SHOW_START_NEW) {
			newGamePanel = new NewGamePanel(connector.getMaps());
			content.addChild(newGamePanel, 0, 0, 1, 1);
		} else if (displayAction == EActionType.SHOW_LOAD) {
			loadSavedGamePanel =
			        new LoadSavedGamePanel(connector.getLoadableGames());
			content.addChild(loadSavedGamePanel, 0, 0, 1, 1);
		}

		for (UILabeledButton b : mainButtons) {
			b.setActive(b.getAction(0, 0).getActionType() == displayAction);
		}

		requestRedraw();
	}

	private void addMainButton(EActionType type, float top) {
		UILabeledButton child =
		        new UILabeledButton(Labels.getName(type), new Action(type),
		                EFontSize.HEADLINE);
		root.addChild(child, .05f, top - .1f, .45f, top);
		mainButtons.add(child);
	}

	@Override
	public void drawContent(GLDrawContext gl2, int width, int height) {
		root.setPosition(new FloatRectangle(0, 0, width, height));
		gl2.color(.6f, .6f, .6f, 1);
		ImageProvider.getInstance().getImage(BACKGROUND)
		        .drawImageAtRect(gl2, 0, 0, width, height);
		root.drawAt(gl2);
	}

	@Override
	public void handleEvent(GOEvent event) {
		if (event instanceof GOCommandEvent) {
			event.setHandler(commandHandler);
		}
	}

	@Override
	public void addRedrawListener(RedrawListener l) {
		redrawListeners.add(l);
	}

	@Override
	public void removeRedrawListener(RedrawListener l) {
		redrawListeners.remove(l);
	}

	private void requestRedraw() {
		for (RedrawListener l : redrawListeners) {
			l.requestRedraw();
		}
	}
}
